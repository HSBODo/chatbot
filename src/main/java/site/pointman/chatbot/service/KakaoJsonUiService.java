package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.CarouselType;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ListCardItemVo;

import java.util.List;

public interface KakaoJsonUiService {

    JSONObject createBasicCard(
            DisplayType displayType,
            String title,
            String msg,
            String thumbnailImgUrl,
            List<ButtonVo> buttonList
    ) throws ParseException;
    JSONObject createCommerceCard(
            DisplayType displayType,
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
            List<ButtonVo> buttonList
    ) throws ParseException;

    JSONObject createListCard(
            DisplayType displayType,
            String title,
            List<ListCardItemVo> listCardItemList,
            List<ButtonVo> buttonList
    ) throws ParseException;

    JSONObject createCarousel(CarouselType itemType, List items) throws ParseException;

    JSONObject createSimpleText(String msg) throws ParseException;
    JSONObject createSimpleImage(String altText, String imgUrl) throws ParseException;

}
