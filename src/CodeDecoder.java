import Rijndael.Rijndael_Algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

public class CodeDecoder {

    public CodeDecoder() {
    }


    private static void Encrypt(byte[] in, byte[] result, int n, Object obj)
            throws Exception {
        byte[] abyte = new byte[16];
        byte[] abyte0 = new byte[16];
        if (0 == n)
            return;
        for (int i = 0; i < n / 16; i++) {
            System.arraycopy(in, i * 16, abyte, 0, 16);
            abyte0 = Rijndael_Algorithm.blockEncrypt(abyte, 0, obj);
            System.arraycopy(abyte0, 0, result, i * 16, 16);
        }

    }

    public static void Encode(byte[] abyte1, int nLength, byte[] abyte0)
            throws Exception {
        int groupsLen = 16 * (nLength / 16);
        Object obj = Rijndael_Algorithm.makeKey(abyte0);
        Encrypt(abyte1, abyte1, groupsLen, obj);
        if (groupsLen != nLength) {
            int nLeft = nLength - groupsLen;
            for (int i = 0; i < nLeft; i++)
                abyte1[groupsLen + i] ^= i;

        }
    }

    public static boolean encryptFile(String s, String s1, byte[] abyte0)
            throws Exception {
        byte[] abyte1 = new byte[2048];
        byte[] abyte2 = new byte[2048];
        File file = new File(s);
        int i = (int) file.length();
        if (i == 0)
            return false;
        FileInputStream fileinputstream;
        try {
            fileinputstream = new FileInputStream(s);
        } catch (FileNotFoundException filenotfoundexception) {
            return false;
        }
        FileOutputStream fileoutputstream;
        try {
            fileoutputstream = new FileOutputStream(s1);
        } catch (FileNotFoundException filenotfoundexception1) {
            fileinputstream.close();
            return false;
        }
        for (; i > 2048; i -= 2048) {
            fileinputstream.read(abyte1, 0, 2048);
            Encode(abyte1, 2048, abyte0);
            fileoutputstream.write(abyte1, 0, 2048);
        }

        if (i > 0) {
            fileinputstream.read(abyte1, 0, i);
            Encode(abyte1, i, abyte0);
            fileoutputstream.write(abyte1, 0, i);
        }
        fileinputstream.close();
        fileoutputstream.close();
        return true;
    }

    private static void Decrypt(byte[] in, byte[] result, int n, Object obj)
            throws Exception {
        byte[] abyte = new byte[16];
        byte[] abyte0 = new byte[16];
        if (0 == n)
            return;
        for (int i = 0; i < n / 16; i++) {
            System.arraycopy(in, i * 16, abyte, 0, 16);
            abyte0 = Rijndael_Algorithm.blockDecrypt(abyte, 0, obj);
            System.arraycopy(abyte0, 0, result, i * 16, 16);
        }

    }

    public static void Decode(byte[] abyte1, int nLength, byte[] abyte0)
            throws Exception {
        int groupsLen = 16 * (nLength / 16);
        Object obj = Rijndael_Algorithm.makeKey(abyte0);
        Decrypt(abyte1, abyte1, groupsLen, obj);
        if (groupsLen != nLength) {
            int nLeft = nLength - groupsLen;
            for (int i = 0; i < nLeft; i++)
                abyte1[groupsLen + i] ^= i;

        }
    }

    public static boolean decryptFile(String s, String s1, byte[] abyte0)
            throws Exception {
        byte[] abyte1 = new byte[2048];
        byte[] abyte2 = new byte[2048];
        File file = new File(s);
        int i = (int) file.length();
        FileInputStream fileinputstream;
        try {
            fileinputstream = new FileInputStream(s);
        } catch (FileNotFoundException filenotfoundexception) {
            return false;
        }
        FileOutputStream fileoutputstream;
        try {
            fileoutputstream = new FileOutputStream(s1);
        } catch (FileNotFoundException filenotfoundexception1) {
            fileinputstream.close();
            return false;
        }
        for (; i > 2048; i -= 2048) {
            fileinputstream.read(abyte1, 0, 2048);
            Decode(abyte1, 2048, abyte0);
            fileoutputstream.write(abyte1, 0, 2048);
        }

        if (i > 0) {
            fileinputstream.read(abyte1, 0, i);
            Decode(abyte1, i, abyte0);
            fileoutputstream.write(abyte1, 0, i);
        }
        fileinputstream.close();
        fileoutputstream.close();
        return true;
    }

    public static String getTransferEncrptString(String s)
            throws Exception {
        byte[] byte_TransferEncrpt = s.getBytes("ISO8859_1");
        int nLength = Array.getLength(byte_TransferEncrpt);
        byte[] result = new byte[nLength * 2];
        for (int i = 0; i < nLength; i++) {
            byte c = byte_TransferEncrpt[i];
            int s_c = c;
            s_c >>>= 4;
            byte b_upper = (byte) s_c;
            byte b_lower = c;
            byte zero = 0;
            byte b_lower_char = (byte) (b_upper & remain_low);
            if (b_lower_char == zero)
                b_lower_char |= upper_bit_add_0101;
            else
                b_lower_char |= upper_bit_add_0100;
            byte c_lower_char = b_lower_char;
            byte b_lower_char1 = (byte) (b_lower & remain_low);
            if (b_lower_char1 == zero)
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
            throws Exception {
        byte[] abyte0 = s.getBytes("ISO8859_1");
        int nlength = Array.getLength(abyte0);
        byte[] result = new byte[nlength / 2];
        for (int i = 0; i < nlength; i += 2) {
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

    public static void main(String[] args)
            throws Exception {
        if (args.length != 3 || !args[2].equalsIgnoreCase("e") && !args[2].equalsIgnoreCase("d"))
            return;
        boolean flag = args[2].equalsIgnoreCase("e");
        if (flag) {
            encryptFile(args[0], args[1], new byte[16]);
        } else {
            decryptFile(args[0], args[1], new byte[16]);
        }
    }

    private static final byte remain_low = 15;
    private static final byte remain_up = -16;
    private static final byte upper_bit_add_0100 = 64;
    private static final byte upper_bit_add_0101 = 80;

}
