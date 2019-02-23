/**
 * 
 */
package indexDocs;

/**
 * @author Phong Nguyen (vietphong.nguyen@gmail.com)
 *
 */
public class TextProcessing {

	/**
	 * 
	 */
	public TextProcessing() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getLetterNumberAndPunctuation(" & text = ;!? he ( ) : '  \"    end \\ tiep {} tiep2  [] += tiep3 - t4 _ \n------------------")	);
		
		System.out.println(getLetterNumberAndPunctuation(" V.1.00\r\n" + 
				"Presented by:\r\n" + 
				"Initial Separation of Arriving and Departing Aircraft\r\n" + 
				"*\r\n:;" + 
				"\r\n ---- te**st  ." + 
				"\r\n" + 
				"*\r\n" + 
				"Initial Separation of Arriving and Departing Aircraft / V.1.00\r\n" + 
				"\r\n" + 
				"Gain Attention")	);

	}

	public static boolean containNoLetter(String s) {
		s = s.toLowerCase();
		for (Character ch = 'a'; ch <= 'z'  ; ch++) {
			if (s.contains(ch.toString())	) 		
				return false;
			
		}
		return true;
	}

	public static String getLetterAndNumber(String text) {
		String output="";
		Character ch;
		for (int i = 0; i< text.length(); i++) {
			ch = text.charAt(i);
			if (Character.isLetterOrDigit(ch)  ||  ch == ' '	)
				output += ch;
			else output += " ";
		}
		output = removeDuplicateSpace(output);
		return output;
	}

	private static String removeDuplicateSpace(String s) {
		s = s.replaceAll("\\s{2,}", " ");
		return s;
	}
	
	public static String getLetterNumberAndPunctuation(String s) {
		s =  s.replaceAll("[;!?]", ".");						// replace those characters ;!?   by a point
		s =  s.replaceAll("[\\\\():'\"{}\\[\\]+=&]", ",");	// replace those characters \():'"{}[]+=&   by a comma
		
		s = removeDuplicatePunctuation(s);
		
		return s;
	}

	private static String removeDuplicatePunctuation(String s) {
		String output="";
		Character ch;
		boolean point = false , comma = false, space = false, lineSeparator = false, isTheFisrtWord =true;
		for (int i = 0; i< s.length(); i++) {
			ch = s.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				if (!isTheFisrtWord) {
					if (point)
						output += ". ";
					else 
						if (comma)
							output += ", ";
						else 
							if (space)
								output += " ";
					if (lineSeparator) {
						output += System.lineSeparator();
						isTheFisrtWord =true;
					}
				}
				output += ch;
				point = false;
				comma = false;
				space = false;
				lineSeparator = false;
				isTheFisrtWord =false;
				continue;
			}

			if (ch == '\n' || ch == '\r' || ch == Character.LINE_SEPARATOR 	) {
				lineSeparator = true;
				continue;
			}

			if (ch == '.') {
				point = true;
				continue;
			}
			if (ch == ',') {
				comma = true;
				continue;
			}
			space = true;	// for every other special characters -> let's consider as a space
		}
		
		return output;
	}

}
