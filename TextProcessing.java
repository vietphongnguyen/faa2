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
		// TODO Auto-generated method stub

	}

	public static boolean containNoLetter(String s) {
		s = s.toLowerCase();
		for (Character ch = 'a'; ch <= 'z'  ; ch++) {
			if (s.contains(ch.toString())	) 		
				return false;
			
		}
		return true;
	}

}
