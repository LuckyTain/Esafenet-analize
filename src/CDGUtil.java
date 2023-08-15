import java.lang.reflect.Array;

public class CDGUtil {

    private static byte key2[] = {
            -21, -112, 90, -68, 5, 44, 85, -86, -21, -112,
            90, -68, 5, 44, 85, -86
    };
    public static String decode(String info)
            throws Exception
    {
        info = CodeDecoder.getTransferUnEncrptString(info);
        byte abyte2[] = info.getBytes("ISO8859_1");
        int nLength = Array.getLength(abyte2);
        CodeDecoder.Decode(abyte2, nLength, key2);
        info = new String(abyte2);
        return info;
    }

    public static String encode(String str)
            throws Exception
    {
        byte abyte1[] = str.getBytes();
        int nLength = Array.getLength(abyte1);
        CodeDecoder.Encode(abyte1, nLength, key2);
        String src = new String(abyte1, "ISO8859_1");
        return CodeDecoder.getTransferEncrptString(src);
    }

    public static byte[] encode(byte abyte[])
            throws Exception
    {
        byte abyte1[] = abyte;
        int nLength = Array.getLength(abyte1);
        CodeDecoder.Encode(abyte1, nLength, key2);
        return abyte1;
    }
}
