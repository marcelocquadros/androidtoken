package uk.co.bitethebullet.android.token.util;

public class Base32 {

	private static String ValidChars = "QAZ2WSX3" + "EDC4RFV5" + "TGB6YHN7" + "UJM8K9LP";
	
	public static String encodeBytes(byte[] input){
		StringBuffer sb = new StringBuffer();
        byte index;
        int hi = 5;
        int currentByte = 0;

        while (currentByte < input.length) {
              if (hi > 8) {
                    // get the last piece from the current byte, shift it to the right
                    // and increment the byte counter
                    index = (byte)(input[currentByte++] >> (hi - 5));
                    if (currentByte != input.length) {
                          // if we are not at the end, get the first piece from
                          // the next byte, clear it and shift it to the left
                          index = (byte)(((byte)(input[currentByte] << (16 - hi)) >> 3) | index);
                    }

                    hi -= 3;
              } else if(hi == 8) { 
                    index = (byte)(input[currentByte++] >> 3);
                    hi -= 3; 
              } else {

                    // simply get the stuff from the current byte
                    index = (byte)((byte)(input[currentByte] << (8 - hi)) >> 3);
                    hi += 5;
              }

              sb.append(ValidChars.charAt(index));
        }

        return sb.toString();
	}
	
	public static byte[] decodeBytes(String input){
		int numBytes = input.length() * 5 / 8;
        byte[] bytes = new byte[numBytes];

        // all UPPERCASE chars
        input = input.toUpperCase();

        int bit_buffer;
        int currentCharIndex;
        int bits_in_buffer;

        if (input.length() < 3) {
              bytes[0] = (byte)(ValidChars.indexOf(input.charAt(0)) | ValidChars.indexOf(input.charAt(1)) << 5);
              return bytes;
        }

        bit_buffer = (ValidChars.indexOf(input.charAt(0)) | ValidChars.indexOf(input.charAt(1)) << 5);
        bits_in_buffer = 10;
        currentCharIndex = 2;
        for (int i = 0; i < bytes.length; i++) {
              bytes[i] = (byte)bit_buffer;
              bit_buffer >>= 8;
              bits_in_buffer -= 8;
              while (bits_in_buffer < 8 && currentCharIndex < input.length()) {
                    bit_buffer |= ValidChars.indexOf(input.charAt(currentCharIndex++)) << bits_in_buffer;
                    bits_in_buffer += 5;
              }
        }

        return bytes;
	}
	
}
