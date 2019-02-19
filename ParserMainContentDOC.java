/**
 * 
 */
package indexDocs;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */

public class ParserMainContentDOC {
	String text = "";
	
	/**
	 * 
	 */
	public ParserMainContentDOC() {
		// TODO Auto-generated constructor stub
	}

	public ParserMainContentDOC(String fileNameString) throws IOException {
		FileInputStream fis = new FileInputStream(fileNameString);
		HWPFDocument doc = new HWPFDocument(fis);
		WordExtractor we = new WordExtractor(doc);
		Range range = doc.getRange();

		HeaderStories headerStore = new HeaderStories(doc);
		int pageCount = we.getSummaryInformation().getPageCount();

		HashMap <String, Integer> headerMap = new HashMap<>();
		int max =0;
		int value;
		for (int i=1; i<= pageCount; i++) {
			String key = headerStore.getHeader(i).trim();
			if (key.isEmpty()) continue;
			if (headerMap.containsKey(key) ) 
				value = headerMap.get(key)+1; 
			else 
				value =1; 
			headerMap.put(key,value );
			if (max < value) max = value;
		}
		System.out.println("Max = " +max);
		System.out.print("Header Is: " );
		for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
		    String key = entry.getKey();
		    value = entry.getValue();
		    if (value == max) {
		    	System.out.println(key);
		    	
		    	text += ". " + key;
		    }
		}
		
		// Get the most common footer
		HashMap <String, Integer> footerMap = new HashMap<>();
		max =0;
		for (int i=1; i<= pageCount; i++) {
			String key = headerStore.getFooter(i).trim();
			if (key.isEmpty()) continue;
			if (footerMap.containsKey(key) ) 
				value = footerMap.get(key)+1; 
			else 
				value =1; 
			footerMap.put(key,value );
			if (max < value) max = value;
		}
		System.out.println("Max = " +max);
		System.out.print("Footer Is: " );
		for (Map.Entry<String, Integer> entry : footerMap.entrySet()) {
		    String key = entry.getKey();
		    value = entry.getValue();
		    if (value == max) {
		    	System.out.println(key);
		    	
		    	text += ". " + key;
		    }
		}
		text += ". \n";
		
		int defaultFontSize = 22;
				//document.getStyles().getDefaultRunStyles().getFontSize();
			
		String[] paragraphs = we.getParagraphText();
		HashMap <Integer,String> textDataLevel = new HashMap<>();
		
		for (int i = 0; i < paragraphs.length; i++) {
			Paragraph pr = range.getParagraph(i);
			//System.out.println("-------------- paragraphs : " + i );
			int k = 0;
			while (true) {
				CharacterRun run = pr.getCharacterRun(k++);

				int fontSize = run.getFontSize();
				String text = run.text().trim();
				if (!text.equals("")) {
					if (textDataLevel.containsKey(fontSize))						
						textDataLevel.put(fontSize, textDataLevel.get(fontSize) + text + ". " );
					else textDataLevel.put(fontSize, text + ". " );
				}
					
				
				/*
				
				if (fontSize > defaultFontSize && !text.equals("")) {
					System.out.println("************ paragraphs : " + i + ". Font Size: " + fontSize);
					System.out.println(text);
					//break;
				}*/
				if (run.getEndOffset() == pr.getEndOffset())
					break;
			}
		}
		we.close();
		doc.close();
		
		// Print the text content in textDataLevel
		// TreeMap to store values of HashMap 
        TreeMap<Integer,String> sorted = new TreeMap<>(); 
  
        // Copy all data from hashMap into TreeMap 
        sorted.putAll(textDataLevel); 
  
        // Display the TreeMap which is naturally sorted 
        for (Entry<Integer, String> entry : sorted.entrySet())  
            System.out.println("Key = " + entry.getKey() +  
                         ", Value = " + entry.getValue());
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\FAA2\\data\\55146001\\CDG\\Final-ATCS CDG_2.doc";
		try {
			ParserMainContentDOC content = new ParserMainContentDOC(fileName);
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
