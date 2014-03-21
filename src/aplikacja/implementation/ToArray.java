package aplikacja.implementation;

import javax.xml.bind.DatatypeConverter;

public class ToArray {
	
	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
}
