/**
 * 
 */
package indexDocs;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class ParserMainContent {

	String text = "";
	Metadata metadata;
	int levelOfExtraction = 4 ; 
	int maxNoCharacters = 500 ; 
	
	/**
	 * @throws NotificationException 
	 * 
	 */
	public ParserMainContent(String fileNameString) throws NotificationException {
		
		String allowedFileType = ".doc .docx .pdf .ppt";
		
		try {
			allowedFileType += " ";
			int i = fileNameString.lastIndexOf('.');
			String extension = fileNameString.substring(i)+" ";
			
			if (allowedFileType.indexOf(extension) < 0) throw new NotificationException(" the file extension is not allowed! ");
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new NotificationException(" the file extension is not allowed! ");
		}
		
		RemoveHeaderFooterStopwords.init("HeaderFooterStopwords.txt");
		ContentStopWords.init("ContentStopwords.txt");
		FileInputStream stream = null;
		
		try {
			levelOfExtraction = (int) IndexDocsGUI.spinnerLevelTextSize.getValue();
			maxNoCharacters= (int) IndexDocsGUI.spinnerMaxCharacter.getValue();
		} catch (Exception e1) {	}
		
		fileNameString = fileNameString.toLowerCase();
		boolean properFileName = checkProperFileName(fileNameString);
		
		if (properFileName && fileNameString.endsWith(".doc")) {
			ParserMainContentDOC mainContent;
			try {
				mainContent = new ParserMainContentDOC(fileNameString,levelOfExtraction,maxNoCharacters); 
				text = mainContent.header + mainContent.footer + mainContent.getText();
				
				text = ContentStopWords.removeStopwords(text);
				//text = TextProcessing.getLetterNumberAndPunctuation(text);
				
				if (!text.equals("")) return;		// if get some text data out of this Doc file then exit, else continue to try other parser
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (properFileName && fileNameString.endsWith(".docx")) {
			ParserMainContentDOCX mainContent;
			try {
				mainContent = new ParserMainContentDOCX(fileNameString,levelOfExtraction,maxNoCharacters); 
				text = mainContent.header + mainContent.footer + mainContent.getText();
				text = TextProcessing.getLetterNumberAndPunctuation(text);
				text = ContentStopWords.removeStopwords(text);
				if (!text.equals("")) return;		// if get some text data out of this Doc file then exit, else continue to try other parser
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (fileNameString.endsWith(".pdf")) {
			ParserMainContentPDF mainContent;
			try {
				mainContent = new ParserMainContentPDF(fileNameString,levelOfExtraction,maxNoCharacters); 
				text = mainContent.header + mainContent.footer + mainContent.getText();
				
				text = ContentStopWords.removeStopwords(text);
				//text = TextProcessing.getLetterNumberAndPunctuation(text);
				
				if (!text.equals("")) return;		// if get some text data out of this Doc file then exit, else continue to try other parser
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

/*		if (fileNameString.endsWith(".ppt")) {
			ParserMainContentPPT mainContent;
			try {
				mainContent = new ParserMainContentPPT(fileNameString,levelOfExtraction,maxNoCharacters); 
				text = mainContent.header + mainContent.footer + mainContent.getText();
				
				text = ContentStopWords.removeStopwords(text);
				//text = TextProcessing.getLetterNumberAndPunctuation(text);
				 * 
				if (!text.equals("")) return;		// if get some text data out of this Doc file then exit, else continue to try other parser
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		
		// Use Auto Detect Parser for any other type of document
		BodyContentHandler handler = new BodyContentHandler();

		AutoDetectParser parser = new AutoDetectParser();
		metadata = new Metadata();

		try {
			stream = new FileInputStream(fileNameString);
			parser.parse(stream, handler, metadata);
		} catch (Exception e) {

		} finally {
			try {
				stream.close();
			} catch (IOException e) {}
		}
		String s = handler.toString();
		
		s = ContentStopWords.removeStopwords(s);
		//s = TextProcessing.getLetterNumberAndPunctuation(s);
		
		if (s.length() > maxNoCharacters) {
			int vt = maxNoCharacters;
			try {
				while (Character.isLetter(s.charAt(vt))) vt--;
				s = s.substring(0, vt);
			} catch (Exception e) {	}
		}
		
		text = s;
		 
	}

	private boolean checkProperFileName(String fileNameString) {
		boolean OK = true; 
		for (int i = 0; i< fileNameString.length(); i++) {
			if (	!Character.isLetterOrDigit(fileNameString.charAt(i))	
					&& fileNameString.charAt(i) != ' ' && fileNameString.charAt(i) != '\\' && fileNameString.charAt(i) != ':'
					&& fileNameString.charAt(i) != '.' && fileNameString.charAt(i) != '_'&& fileNameString.charAt(i) != '-' 
					&& fileNameString.charAt(i) != '/'	&& fileNameString.charAt(i) != '\''		)  {
				OK = false;
				break;
			}
			
		}
		
		return OK;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "C:\\FAA2\\data\\60004807\\PDFs\\JO7110.65_p2-1-21.pdf";
		String fileName2 = "D:\\Phong's window datas\\FAA - SA249\\Desktop\\FAA Indexed Courses\\50019\\Course Documentation\\CDG\\archive\\2002 comparisons\\CDGlpobjcomparisonDIRECTIVELOAsLOPsLTAs.doc";
		ParserMainContent content = new ParserMainContent(fileName);
		System.out.println("Text = " + content.getText());

	}

	public String getText() {
		return text;
	}
	
	public void printMetaData() {
		// getting metadata of the document
		System.out.println("Metadata of the file:");
		String[] metadataNames;
		metadataNames = metadata.names();
		
		for (String name : metadataNames) {
			System.out.println(name + " : " + metadata.get(name));
		}

		
	}

}
