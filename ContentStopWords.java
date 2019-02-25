/**
 * 
 */
package indexDocs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class ContentStopWords {
	static List<String> stopwords = new LinkedList<String>();
	static List<String> regexs = new LinkedList<String>();
	static int MinLengthOfAWord = 3;
	static int MaxLengthOfAWord = 30;
	
	/**
	 * 
	 */
	public ContentStopWords() {
		init("ContentStopwords.txt");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		init("ContentStopwords.txt");
		String s ="FAA Academy Training \r\n" + 
				"Terminal Stage IV, \r\n" + 
				"Nonradar Terminal \r\n" + 
				"STRIPMARKING \r\n" + 
				"STRIPMARKING. THIS PAGE INTENTIONALLY LEFT BLANK. M ";
		String s2 ="50019 apdx. j v. 2016 04 3\r\n" + 
				"AIRSPACE AND PROCEDURES Instructor \r\n" + 
				"Lab Guide \r\n" + 
				"Appendix J \r\n" + 
				"Course 50019 Appendix J, fAA form 6050 4 \r\n" + 
				"Appendix J, fAA form 6050 4, Continued, appendix J, holding pattern reference material";
		String s3 ="";
		System.out.println(removeStopwords(s2));

	}

	public static String removeStopwords(String input) {
		if (	stopwords.isEmpty() && regexs.isEmpty()	) {
			RemoveHeaderFooterStopwords.ForceInit("ContentStopwords.txt");
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
					if (line.indexOf("minlengthofaword:")==0) { 
						MinLengthOfAWord = toInt( line.substring(6).trim()	,1);
						continue;
					}
					if (line.indexOf("maxlengthofaword:")==0) { 
						MaxLengthOfAWord = toInt( line.substring(6).trim()	,100);
						continue;
					}
					if (!line.equals("") && !stopwords.contains(line) ) {
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

	private static int toInt(String s, int defaultValue) {
		int x = defaultValue;
		try {
			String sx = "";
			for (int i = 0; i< s.length() ; i++) {
				if (Character.isDigit(s.charAt(i))) sx += s.charAt(i);
			}
			x = Integer.parseInt(sx);
		} catch (NumberFormatException e) {}
		return x;
	}
}
