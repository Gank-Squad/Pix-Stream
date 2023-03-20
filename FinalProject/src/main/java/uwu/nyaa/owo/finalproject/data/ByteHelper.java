package uwu.nyaa.owo.finalproject.data;

public class ByteHelper
{
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    
    /**
     * converts the given byte[] into a string of hexadecimal
     * @param bytes the array of bytes to convert
     * @return a string of hexadecimal
     */
    public static String bytesToHex(byte[] bytes) 
    {
        char[] hexChars = new char[bytes.length * 2];
        
        for (int j = 0; j < bytes.length; j++) 
        {
            int v = bytes[j] & 0xFF;
            // hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j << 1] = HEX_ARRAY[v >>> 4];
            // hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[(j << 1) + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] bytesFromHex(String s)
    {
        if(s == null || s.length() == 0 || s.length() % 2 != 0)
        {
            return new byte[0];
        }

        int len = s.length();

        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * checks if thatBytes starts with thisBytes
     * @param thisBytes the bytes to check if the otherbyte startswith
     * @param thatBytes the bytes to see if it starts with
     * @return true if thatBytes starts with thisBytes else false
     */
    public static boolean startsWith(byte[] thisBytes, byte[] thatBytes)
    {
        return startsWith(thisBytes, thatBytes, 0);
    }
    
    /**
     * checks if thatBytes starts with thisBytes
     * @param thisBytes the bytes to check if the otherbyte startswith
     * @param thatBytes the bytes to see if it starts with
     * @param offset the offset to apply for thatBytes indexing
     * @return true if thatBytes starts with thisBytes else false
     */
    public static boolean startsWith(byte[] thisBytes, byte[] thatBytes, int offset)
    {
        int shortest = thisBytes.length;

        if (thatBytes.length < shortest)

            shortest = thatBytes.length;

        for (int i = 0; i < shortest; i += 1)
        {
            if(i + offset >= thatBytes.length)
                return false;
            
            if (thatBytes[i  + offset] != thisBytes[i])
                return false;
        }

            

        return true;
    }
}
