/**
 * 
 */
package indexDocs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.sis.util.Characters;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */

public class ParserMainContentDOC {
	public static final int DefaultLevelOfExtraction = 1000;
	public static final int DefaultMaxNoCharacters = 10000;
	
	String text = "";
	int levelOfExtraction  ; 
	int maxNoCharacters ; 
	
	public String header = "";
	public String footer = "";
	
	/**
	 * 
	 */
	public ParserMainContentDOC(String fileNameString) throws IOException {
		this(fileNameString,DefaultLevelOfExtraction); // default level Of Extraction
	}
	public ParserMainContentDOC(String fileNameString,int levelSet) throws IOException {
		this(fileNameString,levelSet, DefaultMaxNoCharacters); // default maxNoCharacters 
	}

	public ParserMainContentDOC(String fileNameString,int levelSet, int maxChar) throws IOException {
		levelOfExtraction = levelSet;
		maxNoCharacters = maxChar;
		RemoveHeaderFooterStopwords.init("HeaderFooterStopwords.txt");
		FileInputStream fis = new FileInputStream(fileNameString);
		HWPFDocument doc = new HWPFDocument(fis);
		WordExtractor we = new WordExtractor(doc);
		Range range = doc.getRange();

		// ********** Collect the most frequent Header ****************/
		
		HeaderStories headerStore = new HeaderStories(doc);
		int pageCount = we.getSummaryInformation().getPageCount();
		
		HashMap <String, Integer> headerMap = new HashMap<>();
		int max =0;
		int value;
		for (int i=1; i<= pageCount; i++) {
			String key = headerStore.getHeader(i).trim();
			key = RemoveHeaderFooterStopwords.removeStopwords(key);
			key = TextProcessing.getLetterNumberAndPunctuation(key);
			if (key.isEmpty()) continue;
			if (headerMap.containsKey(key) ) 
				value = headerMap.get(key)+1; 
			else 
				value =1; 
			headerMap.put(key,value );
			if (max < value) max = value;
		}
		//System.out.println("Max = " +max);
		//System.out.print("Header Is: " );
		for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
		    String key = entry.getKey();
		    value = entry.getValue();
		    if (value >= max) {
		    	//System.out.println(key);
		    	header +=  key + "\n";
		    }
		}
		
		
		// ********** Collect the most frequent Footer ****************/

		HashMap <String, Integer> footerMap = new HashMap<>();
		max =0;
		for (int i=1; i<= pageCount; i++) {
			String key = headerStore.getFooter(i).trim();
			
			//System.out.print("Key = " + key);
			key = RemoveHeaderFooterStopwords.removeStopwords(key);
			key = TextProcessing.getLetterNumberAndPunctuation(key);
			//System.out.println(" --> Key = " + key);
			
			if (key.isEmpty()) continue;
			if (footerMap.containsKey(key) ) 
				value = footerMap.get(key)+1; 
			else 
				value =1; 
			footerMap.put(key,value );
			if (max < value) max = value;
		}
		//System.out.println("Max = " +max);
		//System.out.print("Footer Is: " );
		for (Map.Entry<String, Integer> entry : footerMap.entrySet()) {
		    String key = entry.getKey();
		    value = entry.getValue();
		    if (value >= max) {
		    	//System.out.println(key);
		    	footer +=  key + "\n";
		    }
		}
		
		
		// ********** Collect the biggest paragraph in the content ****************/
			
		String[] paragraphs = we.getParagraphText();
		
		TreeMap<Integer,String> textDataLevel = new TreeMap<>((Collections.reverseOrder())); 
		
		int fromPage=1, toPage=10;
		try {
			fromPage = (int) IndexDocsGUI.spinnerFromPage.getValue();
			toPage = (int) IndexDocsGUI.spinnerToPage.getValue();
		} catch (Exception e1) { }
		
		int pageNumber=1;
		for (int i = 0; i < paragraphs.length; i++) {
			
			Paragraph pr = range.getParagraph(i);
			int estimatedCurPageNumber = (int) ( (pageCount-1) * i / (paragraphs.length-1) + 1 );
			if (pr.pageBreakBefore()) {
				pageNumber++;
				if (pageNumber > toPage) break;
			}
			if (Math.max(pageNumber,estimatedCurPageNumber) < fromPage) continue;	
			
			//System.out.println("paragraphs : " + i +" pageBreakBefore = " + pr.pageBreakBefore()+" " + pr.text());
			
			int k = 0;
			while (true) {
				CharacterRun run = pr.getCharacterRun(k++);
				int fontSize = run.getFontSize()*2;
				if (run.isBold()) fontSize++;
				String s = run.text().trim();
				if (!s.equals("")) {
					s += " \n";
					if (textDataLevel.containsKey(fontSize))						
						textDataLevel.put(fontSize, textDataLevel.get(fontSize) + s + ". " );
					else textDataLevel.put(fontSize, s + ". " );
				}
				if (run.getEndOffset() == pr.getEndOffset())
					break;
			}
		}
		we.close();
		doc.close();
		
		// Remove the entry with no Letter in its value
				Iterator<Entry<Integer, String>> i = textDataLevel.entrySet().iterator();
				Map.Entry<Integer, String> me;
				while(i.hasNext()) {
					me = i.next();
		            if ( TextProcessing.containNoLetter((String) me.getValue()	)	) {
		                i.remove();
		            }
		        }
				
        // Display the TreeMap which is naturally sorted 
        int currentLevel = 0;
        for (Entry<Integer, String> entry : textDataLevel.entrySet()) {  
            int remainCharacters = maxNoCharacters - text.length();
            if (remainCharacters <=0 ) break;
            
            String s;
            s = entry.getValue().trim();
            //System.out.println("Key = " + entry.getKey() + ", Value = " + s);
            if (s.length() <= remainCharacters ) 
            	text += s + ". ";
            else {
            	String add =  s.substring(0, remainCharacters );
            	//System.out.println("add = " + add);
            	try {
					if (	Character.isLetter(s.charAt(remainCharacters)) 
							&& Character.isLetter(s.charAt(remainCharacters-1))	) { // cut in the midle of a word
						int lastSpace = add.length()-1;
						while (	Character.isLetter(add.charAt(Integer.max(lastSpace,0))) && (lastSpace>=0)	) {
							lastSpace --;
						}
						if (lastSpace<0) add = "";
						else add = add.substring(0, lastSpace).trim();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
            	
            	text += add + ". ";
            	break;
            }
            currentLevel ++;
            if (currentLevel >= levelOfExtraction ) break;
        }
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\FAA2\\data\\55146001\\CDG\\Final-ATCS CDG_2.doc";
		try {
			ParserMainContentDOC content = new ParserMainContentDOC(fileName,1);
			System.out.println("Header = " + content.header);
			System.out.println("Footer = " + content.footer);
			System.out.println("Text = " + content.getText());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public String getText() {
		return text;
	}

}
