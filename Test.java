package indexDocs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) {
		
		
		String fileNameString = "C:\\FAA2\\data\\60004807\\PDFs\\KSN_TCAS_II_V7.1_Intro_booklet.pdf";
		String outputFileName = "";
		outputFileName = toFileName(fileNameString);
		System.out.println("outputFileName = " +outputFileName);
		
		

	}
	private static String toFileName(String fileNameString) { // fileNameString = c:\FAA\text1.doc     -> toFileName = "c.,FAA,text1.doc"
		
		return fileNameString.replace(':', '.').replaceAll("\\\\", ",");
	}

}
