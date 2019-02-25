package indexDocs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JList;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.xml.sax.SAXException;

@SuppressWarnings("rawtypes")
class createTextDataUsingTika extends SwingWorker {

	String ListInputFolderName;
	String OutputFolderName;
	String[] fileValues;
	int count;
	
	// Constructor
	public createTextDataUsingTika(String listInputFolderName , String outputFolderName) throws IOException, SAXException, TikaException {
		ListInputFolderName = listInputFolderName;
		OutputFolderName = outputFolderName;
		}
	
	@SuppressWarnings({ "unchecked", "null" })
	protected Object doInBackground() {
		
		String outputFolderName = OutputFolderName;
		Outln("Extracting text data to [" + outputFolderName + "] ...");
		
		// Delete the old file in this directory
		try {
			FileUtils.deleteDirectory(new File(outputFolderName ));
			Out("Deleted all of the old files in the directory ["+ outputFolderName +"] successfully \n");
		} catch (IOException e) {
			Out("Error in deleting the old files in the directory ["+ outputFolderName +"] \n" + e.getMessage());
			e.printStackTrace();
			return null;
		} 
		
		IndexDocsGUI.JListOfFiles.clear();
		
		if (new File(outputFolderName).mkdir()) {
			//Out("Make folder '"+ folder +"' successfully \n");
		}
		List<String> listFileName = new ArrayList<String>()  , 
				listFileName2 = new ArrayList<String>();
		for (String inputFolderName : ListInputFolderName.split("\n") ) {
			if (inputFolderName.length() <=0 ) continue;
			try {
				listFileName2 = Files.walk(Paths.get(inputFolderName))
						.filter(Files::isRegularFile)
						.map(x -> x.toString()).collect(Collectors.toList());
				
				listFileName.addAll(listFileName2);
				
			} catch (IOException e) {
				Out("Warning: CANNOT get files in [" + inputFolderName + "]  \n" + e.getMessage());
				e.printStackTrace();
			}
		}
		
		
		int totalNoFile = listFileName.size();
		IndexDocsGUI.list = new JList(IndexDocsGUI.JListOfFiles);
		fileValues = new String[listFileName.size() ];
		
		IndexDocsGUI.progressBar.setMaximum(totalNoFile);
		IndexDocsGUI.progressBar.setValue(0);
		IndexDocsGUI.progressBar.setStringPainted(true);
		long startTime,currentTime,traveredTime;
		count =0;
		int noSuccessFile=0;
		startTime = System.nanoTime(); // start counting time
		for (String fileNameString : listFileName) {
			File file = new File(fileNameString);

			IndexDocsGUI.progressBar.setValue(IndexDocsGUI.progressBar.getValue()+1);
			double percent = 100* IndexDocsGUI.progressBar.getValue()/  IndexDocsGUI.progressBar.getMaximum();
			currentTime =  System.nanoTime(); // get current time
			traveredTime = (currentTime - startTime);
			long remainingTime = (long) (traveredTime * ((100-percent) /percent ));
			remainingTime = remainingTime / 1000000000; // converted from nanoseconds to second
			IndexDocsGUI.progressBar.setString("Doing " + (count+1) + " / " + totalNoFile + " ( " + percent  
					+ " % )  Time remaining: " + remainingTime/60 +" m "  + remainingTime % 60 + " s" );
			
			if (IndexDocsGUI.process_createTextDataUsingTika.isCancelled()) { 
				cancelTikaProcess(); 
				return -1; 
			}
			if (file.isFile()) {
				Out("\nExtracting '" + fileNameString + "'\n");
				if (IndexDocsGUI.process_createTextDataUsingTika.isCancelled()) { 
					cancelTikaProcess(); 
					return -1; 
				}
				ParserMainContent mainContent = null;
				try {
					mainContent = new ParserMainContent(fileNameString);
				} catch (NotificationException e1) {
					igroreExtractingFile("Warning: [" + fileNameString+ "] " + e1.getMessage(),
							(count+1) + " ________ " + fileNameString);
					continue;
				}
				String s = mainContent.getText();
				
				if (s.equalsIgnoreCase("")) {
					igroreExtractingFile("Warning: file: [" + fileNameString+ "] is empty then it will be ignored!",
							(count+1) + " ________ " + fileNameString);
					continue;
				}
				
				
				String outputFileName = toFileName(fileNameString);
				if (IndexDocsGUI.chckbxEnglishOnly.isSelected() ) {
					String language = "Not detected";
					LanguageDetector detector;
					try {
						detector = new OptimaizeLangDetector().loadModels();
						LanguageResult result = detector.detect(s);
				        language = result.getLanguage();
					} catch (IOException e) {
						Out("Warning: CANNOT detect language in file [" + fileNameString + "]  \n" + e.getMessage());
						e.printStackTrace();
					}
			        
			        
					// System.out.println("Language of text:" + language);
					if ((!(language.equalsIgnoreCase("en") || language.equalsIgnoreCase("et")))) {
						igroreExtractingFile("Warning: the content of the file [" + outputFileName + "] have NOT been writen in English then this file will be ignored!",
								(count + 1) + " [" + language + "]________ " + outputFileName);
						continue;
					}
				} else {
					Writer writer = null;
					try {
						writer = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream( outputFolderName + "/" + outputFileName + ".txt"), "utf-8"));
						writer.write(s);
						
						fileValues[count] =  (count+1) + " : " + outputFileName;
						IndexDocsGUI.JListOfFiles.add(count, fileValues[count]);
						count ++;
						
						noSuccessFile++;
					} catch (IOException ex) {
						// report
						igroreExtractingFile("Warning: Error when saving file:" + outputFileName + ".txt  . This file had been ignore! \n" + ex.getMessage(),
								(count+1) + " ______ " + outputFileName);
						continue;
					} finally {
						try {
							writer.close();
						} catch (Exception ex) {	/* ignore */}
					}
					
				}
			}
		}
		
		IndexDocsGUI.progressBar.setString("Done " + (count) + " / " + totalNoFile + " ( 100 % )");
		
		IndexDocsGUI.btnGetFiles.setEnabled(true);
		IndexDocsGUI.btnXCancel.setEnabled(false);
		IndexDocsGUI.btnExtractTextContents.setEnabled(true);
        Outln("The extraction has been completed! Succeed: " + noSuccessFile + ", Ignored: " + (totalNoFile-noSuccessFile));
		
		return 0 ;
    }
	
	@SuppressWarnings("unchecked")
	private void igroreExtractingFile(String lineOutputInConsole, String lineAddedInGUIList) {
		Outln(lineOutputInConsole );
		fileValues[count] =  lineAddedInGUIList + "________ IGNORED ";
		IndexDocsGUI.JListOfFiles.add(count, fileValues[count]);
		count ++;
		
	}

	private String toFileName(String fileNameString) { // fileNameString = c:\FAA\text1.doc     -> toFileName = "c.,FAA,text1.doc"
		return fileNameString.replace(':', '.').replaceAll("\\\\", ",");
	}

	private void cancelTikaProcess() {
		Out("The process of creating text data had been canceled by user  \n"); 
		IndexDocsGUI.btnExtractTextContents.setEnabled(true);
		IndexDocsGUI.btnGetFiles.setEnabled(true);
		
	}

	private static String Check_Folder_Name(String folderName) {
		String s = folderName.trim(); // remove all the space at the beginning and at the end of
																// string S
		if (s.endsWith("/"))
			s = s.substring(0, s.length()-2) ; // delete the last '/' character at the end if it's present.
		return s;
	}
	private static void Out(int i) {
		String s = Integer.toString(i);
		Out(s);
	}
	private static void Outln(String string) {
		Out(string + "\n");
		//IndexDocsGUI.console.setCaretPosition(IndexDocsGUI.console.getDocument().getLength());
	}

	public static void Out(String s) {
		IndexDocsGUI.console.setText(IndexDocsGUI.console.getText() + s);
		IndexDocsGUI.console.setCaretPosition(IndexDocsGUI.console.getDocument().getLength());
		
		System.out.print(s);
	}
	
	
}