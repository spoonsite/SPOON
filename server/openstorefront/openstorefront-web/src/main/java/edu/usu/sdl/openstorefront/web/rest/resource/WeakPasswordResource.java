package edu.usu.sdl.openstorefront.web.rest.resource;

import java.io.InputStream;

/**
 * 
 * @author jcook
 */
public class WeakPasswordResource {
    public static final String PASSWORDS_PATH = "/weakPasswords.txt";
    
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
		// final String PASSWORDS_PATH = "/weakPasswords.txt";
		InputStream input = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			input = classLoader.getResourceAsStream(PASSWORDS_PATH);
			StringBuilder word = new StringBuilder();
			while (input.available() != 0) {
				char next = (char)input.read();
				if (next == ('\n')) {
					//compare to password
					if (word.toString().equals(password)) {
                        if (needsException) {
                            throw new Exception("The new password is too weak");	//This exception has to be caught by the caller function
                        }
                        else {
                            return false;
                        }
					}
					word = new StringBuilder();
				}
				else {
					word.append(next);
				}
			}
			//TODO: add test if password contains weak word + [num+sym] or [sym+num] combo (i.e. Password1! or Password!1); easily guessed
		}
		catch (NullPointerException e) {
			System.out.println("Error occured");
			System.out.flush();
			e.printStackTrace();
		}
		return true;
	}
}