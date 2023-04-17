package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.dto.kakaoui.ListCardItem;
import site.pointman.chatbot.dto.kakaoui.Button;

import java.util.List;

public interface KakaoApiService {

    JSONObject createBasicCard(
            String displayType,
            String title,
            String msg,
            String thumbnailImgUrl,
            List<Button> buttons
    ) throws ParseException;
    JSONObject createSimpleText(String msg) throws ParseException;
    JSONObject createSimpleImage(String altText, String imgUrl) throws ParseException;
    JSONObject createCommerceCard(
            String displayType,
            String description,
            int price,
            int discount,
            int discountedPrice,
            int discountRate,
            String currency,
            String thumbnailImgUrl,
            String thumbnailLink,
            String profileImgUrl,
            String ProfileNickname,
            List<Button> buttonsList
    ) throws ParseException;
    JSONObject createListCard(
            String displayType,
            String title,
            List<ListCardItem> listCardItems,
            List<Button> buttonList
    ) throws ParseException;
    JSONObject createTodayWeather(String kakaoUserkey) throws Exception;

    JSONObject createTodayNews(String searchText) throws Exception;

    JSONObject createLocationNotice(String kakaoUserkey) throws ParseException;
    JSONObject createDeveloperInfo() throws ParseException;
    JSONObject createRecommendItems() throws ParseException;

    JSONObject createCarousel(String itemType,List items) throws ParseException;

}
