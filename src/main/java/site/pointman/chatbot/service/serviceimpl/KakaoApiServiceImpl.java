package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
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
    public JSONObject createLocationNotice(BasicCard basicCard, String kakaoUserkey) throws ParseException {
        Buttons buttons = new Buttons();
        Button button = new Button("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
        buttons.addButton(button);
        return basicCard.createBasicCard(
                "위치정보의 수집ㆍ이용",
                " ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                        " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                        " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                "https://www.pointman.shop/image/location_notice.jpg",
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
    public JSONObject createTodayWeather(KakaoMemberLocation kakaoUserLocation) throws ParseException{
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
