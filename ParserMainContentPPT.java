/**
 * 
 */
package indexDocs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */

public class ParserMainContentPPT {
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
	public ParserMainContentPPT(String fileNameString) throws IOException {
		this(fileNameString,DefaultLevelOfExtraction); // default level Of Extraction 
	}
	public ParserMainContentPPT(String fileNameString,int levelSet) throws IOException {
		this(fileNameString,levelSet, DefaultMaxNoCharacters); // default maxNoCharacters 
	}

	@SuppressWarnings("resource")
	public ParserMainContentPPT(String fileNameString,int levelSet, int maxChar) throws IOException {
		levelOfExtraction = levelSet;
		maxNoCharacters = maxChar;
		RemoveHeaderFooterStopwords.init("HeaderFooterStopwords.txt");
		FileInputStream fis = new FileInputStream(fileNameString);

		XWPFDocument document = new XWPFDocument(fis);
		fis.close();
		List<XWPFParagraph> paragraphs = document.getParagraphs();

		// ********** Collect the most frequent Header ****************/
		
		HashMap <String, Integer> headerMap = new HashMap<>();
		int max =0;
		int value;
		for (XWPFHeader h : document.getHeaderList() ) {
			String key = h.getText().trim();
			
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
		// System.out.println("Max = " +max);
		// System.out.print("Header Is: " );
		for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
			String key = entry.getKey();
			value = entry.getValue();
			if (value >= max) {
				// System.out.println(key);
				header += key + "\n";
			}
		}
		
		// ********** Collect the most frequent Footer ****************/
		
		HashMap <String, Integer> footerMap = new HashMap<>();
		max =0;
		for (XWPFFooter f : document.getFooterList()) {
			String key = f.getText().trim();
			key = RemoveHeaderFooterStopwords.removeStopwords(key);
			key = TextProcessing.getLetterNumberAndPunctuation(key);
			if (key.isEmpty()) continue;
			if (footerMap.containsKey(key) ) 
				value = footerMap.get(key)+1; 
			else 
				value =1; 
			footerMap.put(key,value );
			if (max < value) max = value;
		}
		// System.out.println("Max = " +max);
		// System.out.print("Footer Is: " );
		for (Map.Entry<String, Integer> entry : footerMap.entrySet()) {
			String key = entry.getKey();
			value = entry.getValue();
			if (value >= max) {
				// System.out.println(key);

				footer += key + "\n";
			}
		}
		
		
		int defaultFontSize = document.getStyles().getDefaultRunStyle().getFontSize();
		
		TreeMap<Integer, String> textDataLevel = new TreeMap<>((Collections.reverseOrder()));
		
		int fromPage=1, toPage=10;
		try {
			fromPage = (int) IndexDocsGUI.spinnerFromPage.getValue();
			toPage = (int) IndexDocsGUI.spinnerToPage.getValue();
		} catch (Exception e1) { }
		
		int pageNumber=1;
		for (XWPFParagraph para : paragraphs) {
			if (para.isPageBreak()) {
				pageNumber++;
				if (pageNumber > toPage) break;
			}
			if (pageNumber < fromPage) continue;
			
			String s = para.getText().trim();
			if (s.equals(""))
				continue;

			// System.out.println(para.getText());

			for (XWPFRun run : para.getRuns()) {
				int fontSize = run.getFontSize();
				// System.out.print("fontSize = " +fontSize + ". Final: ");
				if (fontSize == -1) {
					//System.out.println(defaultFontSize);
					fontSize = defaultFontSize;
				} else {
					//System.out.println(fontSize);
				}
				if (textDataLevel.containsKey(fontSize))
					textDataLevel.put(fontSize, textDataLevel.get(fontSize) + s + ". ");
				else
					textDataLevel.put(fontSize, s + ". ");

				break;
			}
		}

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
		int level = 0;
		for (Entry<Integer, String> entry : textDataLevel.entrySet()) {
			String s;
			s = entry.getValue().trim();
			//System.out.println("Key = " + entry.getKey() + ", Value = " + s);
			int remain = maxNoCharacters - text.length();
			if (remain <= 0)
				break;

			if (s.length() <= remain)
				text += s + " ";
			else {
				String add = s.substring(0, remain);
				//System.out.println("add = " + add);
				try {
					if (Character.isLetter(s.charAt(remain)) && Character.isLetter(s.charAt(remain - 1))) { // cut in the midle of a word
						int lastSpace = add.length() - 1;
						while (Character.isLetter(add.charAt(Integer.max(lastSpace, 0))) && (lastSpace >= 0)) {
							lastSpace--;
						}
						if (lastSpace < 0)
							add = "";
						else
							add = add.substring(0, lastSpace);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				text += add + " ";
				break;
			}
			level++;
			if (level >= levelOfExtraction)
				break;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\FAA2\\data\\LP08\\PRS08.ppt";
		String fileName2 = "";
		try {
			ParserMainContentPPT content = new ParserMainContentPPT(fileName,2,100);
			System.out.println("Header = " + content.header);
			System.out.println("Footer = " + content.footer);
			System.out.println("Text = " + content.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	public String getText() {
		return text;
	}

}
