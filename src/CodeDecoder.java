import Rijndael.Rijndael_Algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.management.LockInfo;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.CRC32;

public class CodeDecoder
{

    public CodeDecoder()
    {
    }


    public static byte[] getFormattedKey(byte abyte0[])
    {
        int i = abyte0.length;
        byte abyte1[] = new byte[16];
        if(i >= 16)
        {
            System.arraycopy(abyte0, 0, abyte1, 0, 16);
        } else
        {
            System.arraycopy(abyte0, 0, abyte1, 16 - i, i);
            for(int j = 0; j < 16 - i; j++)
                abyte1[j] = 0;

        }
        return abyte1;
    }

    private static void fillrand(byte abyte0[], int i)
    {
        long l = System.currentTimeMillis() ^ 0xffffffff9abcb52cL;
        Random random = new Random(l);
        for(int j = 0; j < i; j++)
            random.nextBytes(abyte0);

    }

    private static Object makeKey(byte abyte0[])
            throws InvalidKeyException
    {
        byte abyte1[] = new byte[abyte0.length];
        for(int i = 0; i < 16; i++)
            abyte1[i] = (byte)(abyte0[i] ^ DEFAULTKEY[i]);

        return Rijndael_Algorithm.makeKey(abyte1);
    }

    private static void Encrypt(byte in[], byte result[], int n, Object obj)
            throws Exception
    {
        byte abyte[] = new byte[16];
        byte abyte0[] = new byte[16];
        if(0 == n)
            return;
        for(int i = 0; i < n / 16; i++)
        {
            System.arraycopy(in, i * 16, abyte, 0, 16);
            abyte0 = Rijndael_Algorithm.blockEncrypt(abyte, 0, obj);
            System.arraycopy(abyte0, 0, result, i * 16, 16);
        }

    }

    public static void Encode(byte abyte1[], int nLength, byte abyte0[])
            throws Exception
    {
        int groupsLen = 16 * (nLength / 16);
        Object obj = Rijndael_Algorithm.makeKey(abyte0);
        Encrypt(abyte1, abyte1, groupsLen, obj);
        if(groupsLen != nLength)
        {
            int nLeft = nLength - groupsLen;
            for(int i = 0; i < nLeft; i++)
                abyte1[groupsLen + i] ^= i;

        }
    }

    public static String setSafetyPolicyContextSelectValue()
            throws Exception
    {
        File file = new File("win.config");
        String str = "";
        String str1 = "";
        if(!file.exists())
        {
            str = "\u6307\u5B9A\u6587\u4EF6\u4E0D\u5B58\u5728\u6216\u6587\u4EF6\u5F02\u5E38";
            return str;
        }
        int i = (int)file.length();
        FileInputStream fis1;
        try
        {
            fis1 = new FileInputStream(file);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return "fileInputStream error!";
        }
        while(i > 2048)
        {
            byte abyte[] = new byte[2048];
            fis1.read(abyte, 0, 2048);
            Decode(abyte, 2048, DEFAULTKEY);
            i -= 2048;
            str1 = new String(abyte);
            str = (new StringBuilder()).append(str).append(str1).toString();
        }
        if(i < 2048)
        {
            byte abyte[] = new byte[i];
            fis1.read(abyte, 0, i);
            Decode(abyte, i, DEFAULTKEY);
            fis1.close();
            str1 = new String(abyte);
            str = (new StringBuilder()).append(str).append(str1).toString();
        }
        int j = str.indexOf("\u4EBF\u8D5B\u901A");
        str = str.substring(0, j);
        return str;
    }

    public static String setSafetyPolicyRelatingSelectValue()
            throws Exception
    {
        File file = new File("win.config");
        String str = "";
        String str1 = "";
        if(!file.exists())
        {
            str = "\u6307\u5B9A\u6587\u4EF6\u4E0D\u5B58\u5728\u6216\u6587\u4EF6\u5F02\u5E38";
            return str;
        }
        int i = (int)file.length();
        FileInputStream fis1;
        try
        {
            fis1 = new FileInputStream(file);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return "fileInputStream error!";
        }
        while(i > 2048)
        {
            byte abyte[] = new byte[2048];
            fis1.read(abyte, 0, 2048);
            Decode(abyte, 2048, DEFAULTKEY);
            i -= 2048;
            str1 = new String(abyte);
            str = (new StringBuilder()).append(str).append(str1).toString();
        }
        if(i < 2048)
        {
            byte abyte[] = new byte[i];
            fis1.read(abyte, 0, i);
            Decode(abyte, i, DEFAULTKEY);
            fis1.close();
            str1 = new String(abyte);
            str = (new StringBuilder()).append(str).append(str1).toString();
        }
        int j = str.lastIndexOf("\u4EBF\u8D5B\u901A");
        str = str.substring(j + 5);
        int iLen = str.length();
        if(str != null && !"".equals(str))
        {
            int iLen1 = str.indexOf("\u5317\u4EAC");
            String strFlag = str.substring(iLen1 + 4);
            if(strFlag.length() > 0)
            {
                int iLen2 = strFlag.indexOf("\u4E2D\u56FD");
                String str2 = strFlag.substring(0, iLen2);
                setInfoToHashMap(str2);
                String str3 = strFlag.substring(iLen2 + 4);
                setInfoToHashMap0(str3);
            }
            str = str.substring(0, iLen1);
        }
        return str;
    }

    public static boolean encryptFile(String s, String s1, byte abyte0[])
            throws Exception
    {
        byte abyte1[] = new byte[2048];
        byte abyte2[] = new byte[2048];
        File file = new File(s);
        int i = (int)file.length();
        if(i == 0)
            return false;
        FileInputStream fileinputstream;
        try
        {
            fileinputstream = new FileInputStream(s);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return false;
        }
        FileOutputStream fileoutputstream;
        try
        {
            fileoutputstream = new FileOutputStream(s1);
        }
        catch(FileNotFoundException filenotfoundexception1)
        {
            fileinputstream.close();
            return false;
        }
        for(; i > 2048; i -= 2048)
        {
            fileinputstream.read(abyte1, 0, 2048);
            Encode(abyte1, 2048, abyte0);
            fileoutputstream.write(abyte1, 0, 2048);
        }

        if(i > 0)
        {
            fileinputstream.read(abyte1, 0, i);
            Encode(abyte1, i, abyte0);
            fileoutputstream.write(abyte1, 0, i);
        }
        fileinputstream.close();
        fileoutputstream.close();
        return true;
    }

    private static void Decrypt(byte in[], byte result[], int n, Object obj)
            throws Exception
    {
        byte abyte[] = new byte[16];
        byte abyte0[] = new byte[16];
        if(0 == n)
            return;
        for(int i = 0; i < n / 16; i++)
        {
            System.arraycopy(in, i * 16, abyte, 0, 16);
            abyte0 = Rijndael_Algorithm.blockDecrypt(abyte, 0, obj);
            System.arraycopy(abyte0, 0, result, i * 16, 16);
        }

    }

    public static void Decode(byte abyte1[], int nLength, byte abyte0[])
            throws Exception
    {
        int groupsLen = 16 * (nLength / 16);
        Object obj = Rijndael_Algorithm.makeKey(abyte0);
        Decrypt(abyte1, abyte1, groupsLen, obj);
        if(groupsLen != nLength)
        {
            int nLeft = nLength - groupsLen;
            for(int i = 0; i < nLeft; i++)
                abyte1[groupsLen + i] ^= i;

        }
    }

    public static boolean decryptFile(String s, String s1, byte abyte0[])
            throws Exception
    {
        byte abyte1[] = new byte[2048];
        byte abyte2[] = new byte[2048];
        File file = new File(s);
        int i = (int)file.length();
        FileInputStream fileinputstream;
        try
        {
            fileinputstream = new FileInputStream(s);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return false;
        }
        FileOutputStream fileoutputstream;
        try
        {
            fileoutputstream = new FileOutputStream(s1);
        }
        catch(FileNotFoundException filenotfoundexception1)
        {
            fileinputstream.close();
            return false;
        }
        for(; i > 2048; i -= 2048)
        {
            fileinputstream.read(abyte1, 0, 2048);
            Decode(abyte1, 2048, abyte0);
            fileoutputstream.write(abyte1, 0, 2048);
        }

        if(i > 0)
        {
            fileinputstream.read(abyte1, 0, i);
            Decode(abyte1, i, abyte0);
            fileoutputstream.write(abyte1, 0, i);
        }
        fileinputstream.close();
        fileoutputstream.close();
        return true;
    }

    public static String getTransferEncrptString(String s)
            throws Exception
    {
        byte byte_TransferEncrpt[] = s.getBytes("ISO8859_1");
        int nLength = Array.getLength(byte_TransferEncrpt);
        byte result[] = new byte[nLength * 2];
        for(int i = 0; i < nLength; i++)
        {
            byte c = byte_TransferEncrpt[i];
            int s_c = c;
            s_c >>>= 4;
            byte b_upper = (byte)s_c;
            byte b_lower = c;
            byte zero = 0;
            byte b_lower_char = (byte)(b_upper & remain_low);
            if(b_lower_char == zero)
                b_lower_char |= upper_bit_add_0101;
            else
                b_lower_char |= upper_bit_add_0100;
            byte c_lower_char = b_lower_char;
            byte b_lower_char1 = (byte)(b_lower & remain_low);
            if(b_lower_char1 == zero)
                b_lower_char1 |= upper_bit_add_0101;
            else
                b_lower_char1 |= upper_bit_add_0100;
            byte c_lower_char1 = b_lower_char1;
            result[i * 2] = c_lower_char;
            result[i * 2 + 1] = c_lower_char1;
        }

        String encrptString = new String(result, "ISO8859_1");
        return encrptString;
    }

    public static String getTransferUnEncrptString(String s)
            throws Exception
    {
        byte abyte0[] = s.getBytes("ISO8859_1");
        int nlength = Array.getLength(abyte0);
        byte result[] = new byte[nlength / 2];
        for(int i = 0; i < nlength; i += 2)
        {
            byte byte_r_lower = abyte0[i];
            byte byte_l_lower = abyte0[i + 1];
            byte_r_lower &= remain_low;
            byte_r_lower <<= 4;
            byte_r_lower &= remain_up;
            byte_l_lower &= remain_low;
            byte_l_lower |= byte_r_lower;
            byte b_singleChar_upper = byte_l_lower;
            result[i / 2] = b_singleChar_upper;
        }

        String unencrptString = new String(result, "ISO8859_1");
        return unencrptString;
    }

    public static void main(String args[])
            throws Exception
    {
        if(args.length != 3 || !args[2].equalsIgnoreCase("e") && !args[2].equalsIgnoreCase("d"))
            return;
        boolean flag = args[2].equalsIgnoreCase("e");
        if(flag)
        {
            encryptFile(args[0], args[1], new byte[16]);
            return;
        } else
        {
            decryptFile(args[0], args[1], new byte[16]);
            return;
        }
    }

    private static void setInfoToHashMap(String information)
            throws Exception
    {
        int _first_index = 1;
        for(boolean flag = true; flag;)
        {
            _first_index = information.indexOf(';');
            if(_first_index == -1)
            {
                int offSet = information.indexOf('=');
                String paramName = information.substring(0, offSet);
                String paramValue = information.substring(offSet + 1);
                paramHashMap.put(paramName, paramValue);
                flag = false;
            } else
            {
                String temp = information.substring(0, _first_index);
                int offSet = temp.indexOf('=');
                String paramName = temp.substring(0, offSet);
                String paramValue = temp.substring(offSet + 1);
                paramHashMap.put(paramName, paramValue);
                information = information.substring(_first_index + 1);
            }
        }

    }

    private static void setInfoToHashMap0(String information)
            throws Exception
    {
        int _first_index = 1;
        for(boolean flag = true; flag;)
        {
            _first_index = information.indexOf(';');
            if(_first_index == -1)
            {
                int offSet = information.indexOf('=');
                String paramName = information.substring(0, offSet);
                String paramValue = information.substring(offSet + 1);
                paramHashMap0.put(paramName, paramValue);
                flag = false;
            } else
            {
                String temp = information.substring(0, _first_index);
                int offSet = temp.indexOf('=');
                String paramName = temp.substring(0, offSet);
                String paramValue = temp.substring(offSet + 1);
                paramHashMap0.put(paramName, paramValue);
                information = information.substring(_first_index + 1);
            }
        }

    }

    public static String decodeInfo(String info)
            throws Exception
    {
        String returnInfo = "";
        returnInfo = getTransferUnEncrptString(info);
        byte abyte2[] = returnInfo.getBytes("ISO8859_1");
        int nLength = Array.getLength(abyte2);
        Decode(abyte2, nLength, DEFAULTKEY);
        returnInfo = new String(abyte2);
        return returnInfo;
    }

    public static String encodeInfo(String info)
            throws Exception
    {
        String returnInfo = "";
        byte abyte2[] = info.getBytes("ISO8859_1");
        int nLength = Array.getLength(abyte2);
        Encode(abyte2, nLength, DEFAULTKEY);
        returnInfo = new String(abyte2, "ISO8859_1");
        return getTransferEncrptString(returnInfo);
    }

    private static final int BLOCK_LEN = 16;
    private static final int ENCRYPT_GROUP_LEN = 16;
    private static final int BUFFER_LEN = 2048;
    public static HashMap paramHashMap = new HashMap();
    public static HashMap paramHashMap0 = new HashMap();
    private static byte remain_low = 15;
    private static byte remain_up = -16;
    private static byte upper_bit_add_0100 = 64;
    private static byte upper_bit_add_0101 = 80;
    private static byte DEFAULTKEY[] = {
            -21, -112, 90, -68, 5, 44, 85, -86, -21, -112,
            90, -68, 5, 44, 85, -86
    };

}
