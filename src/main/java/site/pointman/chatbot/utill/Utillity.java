package site.pointman.chatbot.utill;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utillity {
    public static String replaceAll (String text){
        text=text.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>","");
        text=text.replaceAll("&gt;",">");
        text=text.replaceAll("&lt;","<");
        text=text.replaceAll("&quot;","");
        text=text.replaceAll("&apos;","");
        text=text.replaceAll("&nbsp;"," ");
        text=text.replaceAll("&amp;","&");
        return text;
    }

    public static String formatMoney (int money){
        DecimalFormat df = new DecimalFormat("###,###");
        String formatMoney = df.format(money);
        return formatMoney;
    }
    public static String formatApproveDate (String date) throws ParseException {
        String formatDate = date.substring(0, 10);
        return formatDate;
    }
}
