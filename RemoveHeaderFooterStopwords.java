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
	
	/**
	 * 
	 */
	public RemoveHeaderFooterStopwords() {
		// TODO Auto-generated constructor stub
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
		if (stopwords.isEmpty()) {
			RemoveHeaderFooterStopwords.ForceInit("HeaderFooterStopwords.txt");
		}
		String output = " " + input.toLowerCase() + " ";
		
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
		String acceptCharacter = " .";
		for (int i =0; i< output.length() ; i++) {
			if (	!Character.isLetterOrDigit(output.charAt(i)) 	&&  acceptCharacter.indexOf(output.charAt(i)) < 0	 )
				output = output.substring(0, i) + " " + output.substring(i+1); // replace with space
		}
		
		
		
		output = output.replaceAll(" \\.", ".").replaceAll("\\.\\.", ".");
		output = output.replaceAll("\\s{2,}", " ").replaceAll(" \\.", ".").trim();
		output = output.replaceAll("page [0-9]+"," ");    // remove Page 10, page 20 ....
		
		boolean hasLetter = false;		// Check if there is no letter in the string then return ""
		for (int i =0; i< output.length() ; i++) {
			if (	Character.isLetter(output.charAt(i)) 	 ) {
				hasLetter = true;
				break;
			}
		}
		if (!hasLetter) output = "";
		
		return output;
	}

	public static void init(String filename) {
		if (!stopwords.isEmpty()) {
			return;
		}
		ForceInit(filename);
	}
	public static void ForceInit(String filename) {
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();

		    while (line != null) {
		        line = line.trim().toLowerCase();
		    	if (!line.equals("") && !stopwords.contains(line) ) {
		        	stopwords.add(line);
		        }
		        line = br.readLine();
		    }
		    
		    /*for (String word : stopwords) {
		    	System.out.println(word);
		    }*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
