package site.pointman.chatbot.utill;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
    public static boolean isNumber(String number){
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }

    public static Long createProductId(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int num = random.nextInt(1000000);
        return Long.valueOf(num);
    }

    public static String formatPrice(int price){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }

    public static String formatPrice(Long price){
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }

}
