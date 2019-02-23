package indexDocs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */

public class ParserMainContentPDF extends PDFTextStripper
{
	public static final int DefaultLevelOfExtraction = 1000;
	public static final int DefaultMaxNoCharacters = 10000;
	
	String text = "";
	int levelOfExtraction  ; 
	int maxNoCharacters ; 
	
	public String header = "";
	public String footer = "";
	
	TreeMap<Integer, String> textDataLevel = new TreeMap<>((Collections.reverseOrder()));
	
	/**
	 * 
	 */

	public ParserMainContentPDF(String fileNameString) throws IOException {
		this(fileNameString,DefaultLevelOfExtraction); // default level Of Extraction 
	}
	public ParserMainContentPDF(String fileNameString,int levelSet) throws IOException {
		this(fileNameString,levelSet, DefaultMaxNoCharacters); // default maxNoCharacters 
	}

	@SuppressWarnings("resource")
	public ParserMainContentPDF(String fileNameString,int levelSet, int maxChar) throws IOException {
		levelOfExtraction = levelSet;
		maxNoCharacters = maxChar;
		
		PDDocument document = null;
		try {
			document = PDDocument.load(new File(fileNameString));
			if (document.isEncrypted()) {
                try {
                	document = PDDocument.load(new File(fileNameString), "");
                	document.setAllSecurityToBeRemoved(true);
                } catch (InvalidPasswordException e) {
                    System.out.println("Error: Document is encrypted with a password.");
                    return;
                }
            }
			
			setSortByPosition(true);
			
			int fromPage=1, toPage=10;
			try {
				fromPage = (int) IndexDocsGUI.spinnerFromPage.getValue();
				toPage = (int) IndexDocsGUI.spinnerToPage.getValue();
			} catch (Exception e1) { }
			
			setStartPage(fromPage-1);
			setEndPage( Math.min(toPage,document.getNumberOfPages()) );
			
			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
			writeText(document, dummy);
			
			sortedTextDataLevel();
		} finally {
			if (document != null) {
				document.close();
			}
		}
    }


	/**
     * Override the default functionality of PDFTextStripper.
     * The writeString method will be called many time in the extraction of the PDF file. After reading a new line
     * number of called time = number of line
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException
    {
    	
    	int fontSize;
    	String s;
    	for (TextPosition text : textPositions)
        {
            /*System.out.println( "P String[" + text.getXDirAdj() + "," +
                    text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" +
                    text.getXScale() + " height=" + text.getHeightDir() + " space=" +
                    text.getWidthOfSpace() + " width=" +
                    text.getWidthDirAdj() + "]" + text.getUnicode() );*/
    		
    		s = text.getUnicode();
    		fontSize = (int) ( text.getHeightDir()*10 + 0.5); // or use Math.round(d);
    		if (textDataLevel.containsKey(fontSize))
				textDataLevel.put(fontSize, textDataLevel.get(fontSize) + s );
			else
				textDataLevel.put(fontSize, s );
        }
 		
    	// Put a space at the end of each line to separate with text in the next line 
    	for (Entry<Integer, String> entry : textDataLevel.entrySet()) {
    		int key = entry.getKey();
    		String value = entry.getValue().trim() + " ";
    		textDataLevel.put(key, value);
    	}
    }
    
    private void sortedTextDataLevel() {

		// Display the TreeMap which is naturally sorted
		int level = 0;
		String s;
		text ="";

		// Remove the entry with no Letter in its value in textDataLevel Tree Map
		Iterator<Entry<Integer, String>> i = textDataLevel.entrySet().iterator();
		Map.Entry<Integer, String> me;
		while(i.hasNext()) {
			me = i.next();
            if ( TextProcessing.containNoLetter((String) me.getValue()	)	) {
                i.remove();
            }
        }
		
		for (Entry<Integer, String> entry : textDataLevel.entrySet()) {
			s = entry.getValue().trim();
			
			//System.out.println("Key = " + entry.getKey() + ", Value = " + s);
			int remain = maxNoCharacters - text.length();
			if (remain <= 0)
				break;

			if (s.length() <= remain)
				text += s ;
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

				text += add ;
				break;
			}
			text += ". ";
			level++;
			if (level >= levelOfExtraction)
				break;
		}
		
	}
	public static void main( String[] args ) throws IOException
    {
    	String fileName1 = "C:\\FAA2\\data\\60004807\\PDFs\\JO7110.65.pdf";
		String fileName2 = "C:\\FAA2\\data\\60004807\\PDFs\\N_JO_7110.626.pdf";
		String fileName3 = "C:\\FAA2\\data\\60004807\\PDFs\\JO7110.65_2-1-19.pdf";
		
		String fileName = fileName3; // file to parser
		try {
			ParserMainContentPDF content = new ParserMainContentPDF(fileName,2);
			//System.out.println("Header = " + content.header);
			//System.out.println("Footer = " + content.footer);
			System.out.println("Text = " + content.getText());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
        
    }
    public String getText() {
		return text;
	}
}
