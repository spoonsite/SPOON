package edu.usu.sdl.openstorefront.web.rest.resource;

import java.io.InputStream;
import java.io.IOException;

/**
 * 
 * @author jcook
 */
public class WeakPasswordResource {
    public static final String PASSWORDS_PATH = "/weakPasswords.txt";
    private static final int BASE_WORD_LENGTH = 6;
    
    /**
	 * Test new password against a list of known bad passwords
     * Returns boolean
	 *
	 * @param password
	 * @return boolean
	 */
    public static boolean weakPasswordCheck(String password)
	{
		InputStream input = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		input = classLoader.getResourceAsStream(PASSWORDS_PATH);
		if (input == null) {
			System.out.println("The password dictionary was not found");
			System.out.flush();
			return true;	//Allows user registration/password changes to occur still
		}
		StringBuilder word = new StringBuilder();
		try {
			while (input.available() != 0) {	//throws IOException
				char next = (char)input.read();
				//Check agaist word if eol, word where next char is a number or symbol in position greater than minimum probable word length
				if (next == ('\n') || ((Character.isDigit(next) || !Character.isLetterOrDigit(next)) && !(word.length() < BASE_WORD_LENGTH))) {
					String testing = word.toString().toLowerCase();
					//checks against weak words, regardless of ending or casing in new User password
					password = password.toLowerCase();
					String sub1 = password.substring(0, password.length()-1);
					String sub2 = sub1.substring(0, sub1.length()-1);
					String sub3 = sub2.substring(0, sub2.length()-1);   //substring match for basewords of 5 letters
					if (testing.equals(sub1) || testing.equals(sub2) || testing.equals(sub3) || testing.equals(password)) {
						return false;
					}
					else if (next == '\n') {	
						word = new StringBuilder();
					} else {
						word.append(next);
					}
				}
				else {
					word.append(next);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}