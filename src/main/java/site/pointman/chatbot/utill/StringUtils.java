package site.pointman.chatbot.utill;


import site.pointman.chatbot.constant.ProductStatus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class StringUtils {

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

    public static String formatPrice(int price){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }

    public static String formatPrice(Long price){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }

    public static String formatApproveDate (String date) throws ParseException {
        String formatDate = date.substring(0, 10);
        return formatDate;
    }

    public static String createImgFileName(String userKey, String productName){
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTimeStamp = String.valueOf(System.currentTimeMillis());
        String imgFileName = userKey+"_"+productName+"_"+currentDate+"_"+currentTimeStamp;
        return imgFileName;
    }

    public static String dateFormat(String date,String beforeFormatType, String afterFormatType){
        try {
            SimpleDateFormat beforeFormat = new SimpleDateFormat(beforeFormatType);
            SimpleDateFormat afterFormat = new SimpleDateFormat(afterFormatType);

            Date beforeFormatParseDate = beforeFormat.parse(date);

            return afterFormat.format(beforeFormatParseDate);
        }catch (ParseException e){
            return "****-**-**";
        }
    }
}
