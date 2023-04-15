package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.item.ListCardItem;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.kakaochatbotui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface KakaoApiService {

    JSONObject createBasicCard(String title, String msg, String thumbnailImgUrl, Buttons buttons) throws ParseException;
    JSONObject createSimpleText(String msg) throws ParseException;
    JSONObject createSimpleImage(String altText, String imgUrl) throws ParseException;
    JSONObject createCommerceCard(String description, int price, int discount, int discountedPrice, int discountRate, String currency, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String ProfileNickname, Buttons buttons) throws ParseException;
    JSONObject createListCard(String title, List<ListCardItem> listCardItems, List<Button> buttonList ) throws ParseException;
    JSONObject createTodayWeather(String kakaoUserkey) throws Exception;

    JSONObject createTodayNews(String searchText) throws Exception;

    JSONObject createLocationNotice(String kakaoUserkey) throws ParseException;
    JSONObject createDeveloperInfo() throws ParseException;
    JSONObject createRecommendItems() throws ParseException;

}
