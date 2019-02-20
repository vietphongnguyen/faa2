/**
 * 
 */
package indexDocs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.xmlbeans.XmlException;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class TestPOI {

	/**
	 * 
	 */
	public TestPOI() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws XmlException
	 * @throws OpenXML4JException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, OpenXML4JException, XmlException {

		String fileName = "C:\\FAA2\\data\\57017\\CDG_57017.docx";
		FileInputStream fis = new FileInputStream(fileName);

		fileName = fileName.toLowerCase();
		if (fileName.endsWith(".xls")) {
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			Integer sheetNums = workbook.getNumberOfSheets();

		} else if (fileName.endsWith(".xlsx")) {
			XSSFWorkbook xwb = new XSSFWorkbook(fileName);
			Integer sheetNums = xwb.getNumberOfSheets();

		} else if (fileName.endsWith(".docx")) {
			XWPFDocument docx = new XWPFDocument(POIXMLDocument.openPackage(fileName));
			int pageCount = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
			
			
			
			
		} else if (fileName.endsWith(".doc")) {
			
			HWPFDocument doc = new HWPFDocument(fis);
			WordExtractor we = new WordExtractor(doc);
			Range range = doc.getRange();

			HeaderStories headerStore = new HeaderStories(doc);
			
//			String header = headerStore.getHeader(2).trim();
//			System.out.println("Header Is: " + header);
//
//			String footer = headerStore.getFooter(2).trim();
//			System.out.println("Footer Is: " + footer);
			
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
			    }
			}
			
			
			System.out.println();

			int defaultFontSize = 22;
			
			
					//document.getStyles().getDefaultRunStyles().getFontSize();
			//int size = doc.getSummaryInformation();
					
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
			
			

		} else if (fileName.endsWith(".ppt")) {
			HSLFSlideShow document = new HSLFSlideShow(fis);
//		            SlideShow slideShow = new SlideShow(document);
//		            int pageCount = slideShow.getSlides().length;
		} else if (fileName.endsWith(".pptx")) {
			XSLFSlideShow xdocument = new XSLFSlideShow(fileName);
//		            XMLSlideShow xslideShow = new XMLSlideShow(xdocument);
//		            return xslideShow.getSlides().length;
		}

	}

}
