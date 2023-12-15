package site.pointman.chatbot.utill;


import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
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
        String formatPrice = NumberUtils.formatPrice(Integer.parseInt(price));
        String productDetail =
                "판매가격: " + formatPrice +"원" + "\n\n"+
                "상품 설명: " + description + "\n\n"+
                "거래 희망 장소: " + tradingLocation + "\n"+
                "카카오 오픈 채팅방: " + kakaoOpenChatUrl + "\n";
        return productDetail;
    }
    public static String formatProductDetail(ProductStatus productStatus, Long price, String description, String tradingLocation, String kakaoOpenChatUrl, String createDate){
        String formatPrice = NumberUtils.formatPrice(price);
        String productDetail =
                "상품상태: " + productStatus + "\n"+
                "판매가격: " + formatPrice+"원" + "\n\n"+
                "상품 설명: " + description + "\n\n"+
                "거래 희망 장소: " + tradingLocation + "\n"+
                "카카오 오픈 채팅방: " + kakaoOpenChatUrl + "\n\n"+
                "등록일자: " + createDate ;
                ;
        return productDetail;
    }
    public static String formatProductInfo(String price, ProductStatus productStatus){
        String productDetail =
                "상품 상태: "+ productStatus + "\n"+
                "판매가격: "+ price+"원" + "\n";
        return productDetail;
    }

    public static String createImgFileName(String userKey, String productName){
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String currentTimeStamp = String.valueOf(System.currentTimeMillis());
        String imgFileName = userKey+"_"+productName+"_"+currentDate+"_"+currentTimeStamp;
        return imgFileName;
    }





}
