package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.*;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.*;

@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private WeatherApiService weatherApiService;
    @Autowired
    public KakaoApiServiceImpl(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    @Override
    public JSONObject createBasicCard(BasicCard basicCard, String title, String msg, String thumbnailImgUrl,Buttons buttons) throws ParseException {
        return  basicCard.createBasicCard(
                title,
                msg,
                thumbnailImgUrl,
                buttons.createButtons()
        );
    }

    @Override
    public JSONObject createSimpleText(SimpleText simpleText, String msg) throws ParseException {
        return simpleText.createSimpleText(msg);
    }

    @Override
    public JSONObject createSimpleImage(SimpleImage simpleImage, String altText,String imgUrl) throws ParseException {
        return simpleImage.createSimpleImage(altText,imgUrl);
    }

    @Override
    public JSONObject createCommerceCard() throws ParseException {
        return null;
    }

    @Override
    public JSONObject createListCard() throws ParseException {
        return null;
    }

    @Override
    public JSONObject todayWeather(KakaoMemberLocation kakaoUserLocation) throws ParseException{
        Map<String,String> weatherCode =  weatherApiService.selectShortTermWeather(kakaoUserLocation);
        log.info("weatherCode ={}",weatherCode);
        Map<String,String> weatherInfo = weatherApiService.WeatherCodeFindByName(weatherCode);
        log.info("weatherInfo ={}",weatherInfo);
        BasicCard basicCard = new BasicCard();
        Buttons buttons = new Buttons();
        JSONObject basicCardJson=basicCard.createBasicCard(
                weatherInfo.get("baseDate")+" 날씨",
                " 하늘상태:"+weatherInfo.get("SKY")+"\n" +
                        " 기온: "+weatherInfo.get("TMP")+"도"+"\n" +
                        " 습도: "+weatherInfo.get("REH")+"%"+"\n" +
                        " 바람: "+weatherInfo.get("WSD")+"\n" +
                        " 풍속: "+weatherInfo.get("UUU")+" m/s\n" +
                        " 강수형태:"+weatherInfo.get("PTY")+"\n" +
                        " 강수확률: "+weatherInfo.get("POP")+"%\n" +
                        " 강수량: "+weatherInfo.get("PCP")+"\n" +
                        " 적설량: "+weatherInfo.get("SNO")+"\n",
                weatherInfo.get("imgUrl"),
                buttons.createButtons()
        );
        return  basicCardJson;
    }
}
