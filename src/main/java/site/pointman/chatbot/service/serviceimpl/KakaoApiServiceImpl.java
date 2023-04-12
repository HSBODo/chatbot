package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.kakaochatbotui.*;
import site.pointman.chatbot.domain.wearher.WeatherElementCode;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private BasicCard basicCard;
    private SimpleText simpleText;
    private SimpleImage simpleImage;
    private WeatherApiService weatherApiService;

    private CommerceCard commerceCard;

    public KakaoApiServiceImpl(BasicCard basicCard, SimpleText simpleText, SimpleImage simpleImage, WeatherApiService weatherApiService, CommerceCard commerceCard) {
        this.basicCard = basicCard;
        this.simpleText = simpleText;
        this.simpleImage = simpleImage;
        this.weatherApiService = weatherApiService;
        this.commerceCard = commerceCard;
    }

    @Override
    public JSONObject createBasicCard(String title, String msg, String thumbnailImgUrl,Buttons buttons) throws ParseException {
        return  basicCard.createBasicCard(
                title,
                msg,
                thumbnailImgUrl,
                buttons.createButtons()
        );
    }

    @Override
    public JSONObject createLocationNotice(String kakaoUserkey) throws ParseException {
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
    public JSONObject createDeveloperInfo() throws ParseException {
        Buttons buttons = new Buttons();
        Button button1 = new Button("webLink","블로그","https://pointman.tistory.com/");
        Button button2 = new Button("webLink","GitHub","https://github.com/HSBODo");
        Button button3 = new Button("webLink","포트폴리오","https://hsbodo.github.io/PointMan/iPortfolio/index.html");
        buttons.addButton(button1);
        buttons.addButton(button2);
        buttons.addButton(button3);
        return basicCard.createBasicCard(
                "Backend Developer",
                "안녕하세요.\n" +
                        "백엔드 개발자 한수빈입니다.\n" +
                        "Email: vinsulill@gmail.com",
                "https://www.pointman.shop/image/Ryan.jpg",
                buttons.createButtons()
        );
    }

    @Override
    public JSONObject createSimpleText(String msg) throws ParseException {
        return simpleText.createSimpleText(msg);
    }

    @Override
    public JSONObject createSimpleImage(String altText,String imgUrl) throws ParseException {
        return simpleImage.createSimpleImage(altText,imgUrl);
    }

    @Override
    public JSONObject createCommerceCard(String description, int price, int discount, String currency, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String ProfileNickname, Buttons buttons) throws ParseException {

        return commerceCard.createCommerceCard(
                description,
                price,
                discount,
                currency,
                thumbnailImgUrl,
                thumbnailLink,
                profileImgUrl,
                ProfileNickname,
                buttons.createButtons());
    }

    @Override
    public JSONObject createListCard() throws ParseException {
        return null;
    }

    @Override
    public JSONObject createTodayWeather(KakaoMemberLocation kakaoUserLocation) throws Exception {
        WeatherElementCode weatherCode =  weatherApiService.selectShortTermWeather(kakaoUserLocation);
        BasicCard basicCard = new BasicCard();
        Buttons buttons = new Buttons();


        JSONObject basicCardJson=basicCard.createBasicCard(
                weatherCode.getBaseDateValue()+" 오늘의 날씨",
                " 하늘상태:"+weatherCode.getSkyValue()+"\n" +
                        " 기온: "+weatherCode.getTmp()+"도"+"\n" +
                        " 습도: "+weatherCode.getReh()+"%"+"\n" +
                        " 바람: "+weatherCode.getWsdValue()+"\n" +
                        " 풍속: "+weatherCode.getWsd()+" m/s\n" +
                        " 강수형태:"+weatherCode.getPtyValue()+"\n" +
                        " 강수확률: "+weatherCode.getPop()+"%\n" +
                        " 1시간 강수량: "+weatherCode.getPcp()+"\n" +
                        " 적설량: "+weatherCode.getSno()+"\n",
                weatherCode.getImgUrl(),
                buttons.createButtons()
        );
        return  basicCardJson;
    }
}
