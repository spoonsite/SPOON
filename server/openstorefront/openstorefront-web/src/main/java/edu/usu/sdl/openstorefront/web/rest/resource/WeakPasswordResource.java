package edu.usu.sdl.openstorefront.web.rest.resource;

import java.io.InputStream;

/**
 * 
 * @author jcook
 */
public class WeakPasswordResource {
    public static final String PASSWORDS_PATH = "/weakPasswords.txt";
    private static final int BASE_WORD_LENGTH = 6;
    
    /**
	 * Test new password against a list of known bad passwords
     * Throws an error if the API logic requires, or returns boolean otherwise
	 *
	 * @param password
     * @param needsException
	 * @throws Exception
	 */
    public static boolean weakPasswordCheck(String password, boolean needsException) throws Exception
	{
		InputStream input = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			input = classLoader.getResourceAsStream(PASSWORDS_PATH);
			StringBuilder word = new StringBuilder();
			while (input.available() != 0) {
                char next = (char)input.read();
                //Check agaist word if eol, word where next char is a number or symbol in position greater than minimum probable word length
				if (next == ('\n') || ((Character.isDigit(next) || !Character.isLetterOrDigit(next)) && !(word.length() < BASE_WORD_LENGTH))){
                    String testing = word.toString().toLowerCase();
                    //checks against weak words, regardless of ending or casing in new User password
                    password = password.toLowerCase();
                    String sub1 = password.substring(0, password.length()-1);
                    String sub2 = sub1.substring(0, sub1.length()-1);
                    String sub3 = sub2.substring(0, sub2.length()-1);   //substring match for basewords of 5 letters
                    if (testing.equals(sub1) || testing.equals(sub2) || testing.equals(sub3) || testing.equals(password)) {
                        if (needsException) {
                            throw new Exception("The new password is too weak");	//This exception has to be caught by some methods to work with existing logic
                        }
                        else {
                            return false;
                        }
                    }
                    else if (next == '\n') word = new StringBuilder();
                    else word.append(next);
				}
				else {
					word.append(next);
				}
			}
		}
		catch (NullPointerException e) {
			System.out.println("Error occured");
			System.out.flush();
			e.printStackTrace();
		}
		return true;
	}
}