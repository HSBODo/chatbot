package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.*;

public interface KakaoApiService {
    JSONObject todayWeather(KakaoMemberLocation kakaoUserLocation) throws  ParseException;
    JSONObject createBasicCard(BasicCard basicCard, String title, String msg, String thumbnailImgUrl, Buttons buttons) throws ParseException;
    JSONObject createSimpleText(SimpleText simpleText, String msg) throws ParseException;
    JSONObject createSimpleImage(SimpleImage simpleImage, String altText, String imgUrl) throws ParseException;
    JSONObject createCommerceCard() throws ParseException;
    JSONObject createListCard() throws ParseException;

}
