package ca.bc.gov.webade.developer.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import ca.bc.gov.webade.developer.WebADEDeveloperException;

/**
 * Password encryption service.
 * 
 * @author Vivid Solutions Inc
 */
public final class PasswordService {

    private static PasswordService instance;
    private static final String SALT = "E11C689ABE0615DCBC96396E6739FA11";

    private PasswordService() {
    }

    /**
     * Encrypt a plain text string.
     * 
     * @param plaintext The string to encrypt.
     * @return The encrypted string.
     * @throws WebADEDeveloperException Thrown if any falure occurs during
     *         encryption.
     */
    public synchronized static String encrypt(String plaintext) throws WebADEDeveloperException {
        MessageDigest md = null;
        String saltyText = SALT + plaintext;
        try {
            md = MessageDigest.getInstance("SHA"); // step 2
        }
        catch (NoSuchAlgorithmException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }
        try {
            md.update(saltyText.getBytes("UTF-8")); // step 3
        }
        catch (UnsupportedEncodingException e) {
            throw new WebADEDeveloperException(e.getMessage(), e);
        }

        byte raw[] = md.digest(); // step 4
        String hash = StringUtils.newStringUtf8(Base64.encodeBase64(raw)); // step 5
        //String hash = (new BASE64Encoder()).encode(raw); 
        return hash; // step 6
    }

    /**
     * Compare a plain text string to an encrypted version of that string.
     * 
     * @param plaintext Plain text string
     * @param encripted Encripted string
     * @return True if and only if the plain text string is the source of the
     *         encrypted string.
     * @throws WebADEDeveloperException Thrown if any falure occurs during
     *         encryption.
     */
    public synchronized static boolean compare(String plaintext, String encripted) throws WebADEDeveloperException {
        return encripted.equals(encrypt(plaintext));
    }

    /**
     * Get the Singleton instance of the PasswordService.
     * 
     * @return the PasswordService Singleton
     */
    public static synchronized PasswordService getInstance() // step 1
    {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
}
