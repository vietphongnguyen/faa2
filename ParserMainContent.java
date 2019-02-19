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

	String text="";
	Metadata metadata;

	/**
	 * 
	 */
	public ParserMainContent() {
		// TODO Auto-generated constructor stub
	}

	public ParserMainContent(String fileNameString) {
		
		FileInputStream stream = null;
		
		fileNameString = fileNameString.toLowerCase();
		boolean properFileName = checkProperFileName(fileNameString);
		if (properFileName && fileNameString.endsWith(".doc")) {
			ParserMainContentDOC mainContent;
			try {
				mainContent = new ParserMainContentDOC(fileNameString,1); // level Of Extraction = 1
				text = mainContent.getText().trim();
				if (!text.equals("")) return;		// if get some text data out of this Doc file then exit, else continue to try other parser
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
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
			} catch (IOException e) {
			}
		}
		text = handler.toString().trim();
		 
	}

	private boolean checkProperFileName(String fileNameString) {
		boolean OK = true; 
		for (int i = 0; i< fileNameString.length(); i++) {
			if (	!Character.isLetterOrDigit(fileNameString.charAt(i))	
					&& fileNameString.charAt(i) != ' ' && fileNameString.charAt(i) != '\\' && fileNameString.charAt(i) != ':'
					&& fileNameString.charAt(i) != '.' && fileNameString.charAt(i) != '_'&& fileNameString.charAt(i) != '-' 
					&& fileNameString.charAt(i) != '/'			)  {
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
		// TODO Auto-generated method stub

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
