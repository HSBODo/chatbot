package site.pointman.chatbot.utill;


import site.pointman.chatbot.dto.product.ProductDto;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    public static String formatMoney (int money){
        DecimalFormat df = new DecimalFormat("###,###");
        String formatMoney = df.format(money);
        return formatMoney;
    }
    public static String formatApproveDate (String date) throws ParseException {
        String formatDate = date.substring(0, 10);
        return formatDate;
    }

    public static String formatProductDetail(String price, String description, String tradingLocation, String kakaoOpenChatUrl){
        String productDetail =
                "판매가격: "+ price+"원" + "\n"+
                "상품 설명: "+ description + "\n"+
                "거래 희망 장소: "+ tradingLocation + "\n"+
                "카카오 오픈 채팅방: "+ kakaoOpenChatUrl + "\n";
        return productDetail;
    }
    public static String createImgFileName(ProductDto productDto){
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTimeStamp = String.valueOf(System.currentTimeMillis());
        String imgFileName = productDto.getCustomer().getUserKey()+"_"+productDto.getProductName()+"_"+currentDate+"_"+currentTimeStamp;
        return imgFileName;
    }





}
