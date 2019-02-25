/**
 * 
 */
package indexDocs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class RemoveHeaderFooterStopwords {

	static List<String> stopwords = new LinkedList<String>();
	static List<String> regexs = new LinkedList<String>();
	
	/**
	 * 
	 */
	public RemoveHeaderFooterStopwords() {
		RemoveHeaderFooterStopwords.init("HeaderFooterStopwords.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RemoveHeaderFooterStopwords.init("HeaderFooterStopwords.txt");
		String header ="Course Design Guide	 	Version Date: 09/06/2016. \r\n" + 
				"57017 Military Operations: Special Use Airspace (SUA)/ATCAA		Page i. \r\n" + 
				"57017 Military Operations: Special Use Airspace (SUA)/ATCAA 	Page 19. \r\n" + 
				"<Course Name>	Page ii. "
				+ "NATIONAL. AIR TRAFFIC. TRAINING PROGRAM. Course Design Guide (CDG). "
				+ "57017 Military Operations: Special Use Airspace (SUA)/ATCAA. September 6, 2016. ";
		String header2 ="Course Design Guide 06 25 2014. \r\n" + 
				"En Route Wake Turbulence and Visual Separation Page 20. \r\n" + 
				"En Route Wake Turbulence and Visual Separation Page 21. \r\n" + 
				"En Route Wake Turbulence and Visual Separation Page 12. \r\n" + 
				"NATIONAL. AIR TRAFFIC. TRAINING PROGRAM. Air Traffic. Course Design Guide (CDG)";
		String header3 ="PAGE ii";
		System.out.println(removeStopwords(header3));
	}

	public static String removeStopwords(String input) {
		if (	stopwords.isEmpty() && regexs.isEmpty()	) {
			RemoveHeaderFooterStopwords.ForceInit("HeaderFooterStopwords.txt");
		}
		
		String output = " " + input.toLowerCase() + " ";
		
		for (String regex : regexs) {
			output = output.replaceAll(regex," ");    
		}
		
		for (String word : stopwords) {
			int start = 0;
			int vt1;
			do {
				vt1 = output.indexOf(word, start);
				if (vt1 >= 0) { // found a stop word
					if (!Character.isLetterOrDigit(output.charAt(vt1 - 1))
							&& !Character.isLetterOrDigit(output.charAt(vt1 + word.length()))) {
						output = output.substring(0, vt1) + output.substring(vt1 + word.length()); // delete the word in output
						start = vt1+1;
					}
					else {
						start = vt1 + word.length();
					}
				} 
				else break;
			} while (true);
	    	
	    }
		
		// Clean output
		output = TextProcessing.getLetterNumberAndPunctuation(output);
		
		return output;
	}

	public static void init(String filename) {
		if (	!stopwords.isEmpty() || !regexs.isEmpty()	) {
			return;
		}
		ForceInit(filename);
	}
	public static void ForceInit(String filename) {
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line, regex;

		    while ((line = br.readLine()) != null) {
		    	try {
					// test if the line is a comment line from // 
					line = line.split("//")[0];
					line = line.trim().toLowerCase();
					if (line.indexOf("regex:")==0) { 
						regex = line.substring(6).trim();
						if (!regex.equals("") && !regexs.contains(regex) ) {
							regexs.add(regex);
					    }
						continue;
					}
					else if (!line.equals("") && !stopwords.contains(line) ) {
						stopwords.add(line);
					}
				} catch (Exception e) {}
		    }
		    
		    /*System.out.println("-----------  Stopword: -----------");
		    for (String word : stopwords) {
		    	System.out.println(word);
		    }
		    System.out.println("-----------  RegEx: -----------");
		    for (String r : regexs) {
		    	System.out.println(r);
		    }*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
