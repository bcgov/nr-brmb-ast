/**
 * @(#)GUID.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author jross
 */
public final class GUID implements Serializable {
    private static final long serialVersionUID = -7368676679983381962L;
    /**
     * The fixed size of all GUID values.
     */
    private static final int GUID_SIZE = 32;
    private byte[] guidBytes;
    private String nativeGuidString;
    private String microsoftGuidString;
    private boolean isValid = false;

    /**
     * Converts the given GUID between native GUID and Microsoft GUID and
     * returns the result. For example: 00D26B7C1AECEE4F8B8353B74D2E5BA5 to
     * 7C6BD200EC1A4FEE8B8353B74D2E5BA5
     * 
     * @param guid
     *            The guid to convert.
     * @return The converted guid, or null if the given string is not a valid
     *         guid.
     */
    public static final String convertGUIDStringFormat(String guid) {
        if (!validate(guid)) {
            return null;
        }
        String segment1 = guid.substring(0, 2).toUpperCase();
        String segment2 = guid.substring(2, 4).toUpperCase();
        String segment3 = guid.substring(4, 6).toUpperCase();
        String segment4 = guid.substring(6, 8).toUpperCase();
        String segment5 = guid.substring(8, 10).toUpperCase();
        String segment6 = guid.substring(10, 12).toUpperCase();
        String segment7 = guid.substring(12, 14).toUpperCase();
        String segment8 = guid.substring(14, 16).toUpperCase();
        String segment9 = guid.substring(16).toUpperCase();
        String convertedGuid = segment4 + segment3 + segment2 + segment1 + segment6 + segment5
                + segment8 + segment7 + segment9;
        return convertedGuid.toUpperCase();
    }

    /**
     * Converts the given native guid to a byte array representation.
     * 
     * @param nativeGuid
     *            The native guid to convert.
     * @return The byte array containing the guid value in byte form, or null if
     *         the given value is not a valid guid.
     */
    public static byte[] convertNativeGUIDToNativeBytes(String nativeGuid) {
        if (!validate(nativeGuid)) {
            return null;
        }
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        for (int x = 0; x < nativeGuid.length(); x = x + 2) {
            String val = nativeGuid.substring(x, x + 2);
            bytes.add(new Byte((byte)Integer.parseInt(val, 16)));
        }
        byte[] guidbytes = new byte[bytes.size()];
        int x = 0;
        for (Iterator<Byte> i = bytes.iterator(); i.hasNext();) {
            Byte current = i.next();
            guidbytes[x] = current.byteValue();
            x++;
        }

        return guidbytes;
    }

    /**
     * Converts the given microsoft guid to a byte array representation.
     * 
     * @param microsoftGuid
     *            The microsoft guid to convert.
     * @return The byte array containing the guid value in byte form, or null if
     *         the given value is not a valid guid.
     */
    public static byte[] convertMicrosoftGUIDToNativeBytes(String microsoftGuid) {
        String nativeGuid = convertGUIDStringFormat(microsoftGuid);
        return convertNativeGUIDToNativeBytes(nativeGuid);
    }

    /**
     * Converts the given guid byte array to a native string representation.
     * 
     * @param guidBytes
     *            The guid to convert.
     * @return The native guid value in string form, or null if the given value
     *         is not a valid guid.
     */
    public static String convertNativeBytesToNativeGUID(byte[] guidBytes) {
        if (validate(guidBytes)) {
            return null;
        }
        String nativeGuid = "";
        for (int x = 0; x < guidBytes.length; x++) {
            String value = Integer.toHexString(guidBytes[x] & 0xFF);
            if (value.length() == 1) {
                value = "0" + value;
            }
            nativeGuid += value;
        }
        return nativeGuid.toUpperCase();
    }

    /**
     * Converts the given guid byte array to a microsoft string representation.
     * 
     * @param guidBytes
     *            The guid to convert.
     * @return The microsoft guid value in string form, or null if the given
     *         value is not a valid guid.
     */
    public static String convertNativeBytesToMicrosoftGUID(byte[] guidBytes) {
        String nativeGuid = convertNativeBytesToNativeGUID(guidBytes);
        return convertGUIDStringFormat(nativeGuid);
    }

    /**
     * Validates the given string to ensure its value represents a valid guid
     * value.
     * 
     * @param guidString
     *            The guid string to validate.
     * @return True if the string contains a valid guid string.
     */
    public static boolean validate(String guidString) {
        boolean valid = false;
        if (guidString != null && guidString.length() == GUID_SIZE) {
            valid = true;
            for (int x = 0; x < guidString.length(); x++) {
                char currentChar = guidString.charAt(x);
                if (!((currentChar >= '0' && currentChar <= '9')
                        || (currentChar >= 'a' && currentChar <= 'f') || (currentChar >= 'A' && currentChar <= 'F'))) {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * Validates the given byte array to ensure its value represents a valid
     * guid value.
     * 
     * @param guidBytes
     *            The guid byte array to validate.
     * @return True if the array contains a valid guid value.
     */
    public static boolean validate(byte[] guidBytes) {
        return (guidBytes != null && guidBytes.length == GUID_SIZE);
    }

    /**
     * Creates a GUID instance using a byte-array containing a native GUID
     * binary value.
     * 
     * @param guidBytes
     *            The GUID binary in byte array form.
     */
    public GUID(byte[] guidBytes) {
        if (validate(guidBytes)) {
            this.isValid = true;
            this.guidBytes = new byte[GUID_SIZE];
            System.arraycopy(guidBytes, 0, this.guidBytes, 0, GUID_SIZE);
            this.microsoftGuidString = convertNativeBytesToMicrosoftGUID(guidBytes);
            this.nativeGuidString = convertNativeBytesToNativeGUID(guidBytes);
        }
    }

    /**
     * Creates a GUID instance using a Microsoft-formatted GUID string.
     * 
     * @param guidString
     *            The GUID string value.
     */
    public GUID(String guidString) {
        this(guidString, false);
    }

    /**
     * Creates a GUID instance using a Hex-based GUID string.
     * 
     * @param guidString
     *            The GUID string value.
     * @param isNativeFormat
     *            A flag indicating whether the GUID string is in native or
     *            Microsoft format.
     */
    public GUID(String guidString, boolean isNativeFormat) {
        if (validate(guidString)) {
            this.isValid = true;
            if (isNativeFormat) {
                this.guidBytes = convertNativeGUIDToNativeBytes(guidString);
                this.microsoftGuidString = convertGUIDStringFormat(guidString);
                this.nativeGuidString = guidString.toUpperCase();
            } else {
                this.guidBytes = convertMicrosoftGUIDToNativeBytes(guidString);
                this.microsoftGuidString = guidString.toUpperCase();
                this.nativeGuidString = convertGUIDStringFormat(guidString);
            }
        } else {
            this.isValid = false;
            this.microsoftGuidString = guidString;
            this.nativeGuidString = guidString;
            if (guidString != null) {
                this.guidBytes = guidString.getBytes();
            }
        }
    }

    /**
     * @return The native GUID byte array representation of this GUID.
     */
    public byte[] toNativeByteArray() {
        return guidBytes;
    }

    /**
     * @return The native GUID string representation of this GUID.
     */
    public String toNativeGUIDString() {
        return nativeGuidString;
    }

    /**
     * @return The Microsoft GUID string representation of this GUID.
     */
    public String toMicrosoftGUIDString() {
        return microsoftGuidString;
    }

    /**
     * @return Returns true if the GUID is valid.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object object) {
        return (object instanceof GUID && ((GUID)object).toNativeGUIDString().equals(
                toNativeGUIDString()));
    }

	@Override
	public int hashCode() {
		return toNativeGUIDString().hashCode();
	}

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return toMicrosoftGUIDString();
    }
}
