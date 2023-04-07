package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.domain.KakaoUserLocation;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.BasicCard;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.Button;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.Buttons;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.KakaoResponse;
import site.pointman.chatbot.repository.KakaoUserRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.*;

@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    private  final KakaoUserRepository kakaoUserRepository;
    private WeatherApiService weatherApiService;
    @Autowired
    public KakaoApiServiceImpl(KakaoUserRepository kakaoUserRepository, WeatherApiService weatherApiService) {
        this.kakaoUserRepository = kakaoUserRepository;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public JSONObject todayWeather(KakaoUserLocation kakaoUserLocation) throws ParseException{
        Map<String,String> weatherCode =  weatherApiService.selectShortTermWeather(kakaoUserLocation);
        log.info("weatherCode ={}",weatherCode);
        Map<String,String> weatherInfo = weatherApiService.WeatherCodeFindByName(weatherCode);
        log.info("weatherInfo ={}",weatherInfo);
        BasicCard basicCard = new BasicCard();
        Buttons buttons = new Buttons();
        Button button = new Button("message","좋은 하루 되세요","짜자잔");
        buttons.addButton(button);
        JSONObject basicCardJson=basicCard.createBasicCard(
                weatherInfo.get("baseDate")+" 날씨",

                " 하늘상태:"+weatherInfo.get("SKY")+"\n" +
                        " 기온: "+weatherInfo.get("TMP")+"도"+"\n" +
                        " 습도: "+weatherInfo.get("REH")+"%"+"\n" +
                        " 바람: "+weatherInfo.get("WSD")+"\n" +
                        " 풍속: "+weatherInfo.get("UUU")+"\n" +
                        " 강수형태:"+weatherInfo.get("PTY")+"\n" +
                        " 강수확률: "+weatherInfo.get("POP")+"\n" +
                        " 강수량: "+weatherInfo.get("PCP")+"\n" +
                        " 적설량: "+weatherInfo.get("SNO")+"\n",

                "https://www.pointman.shop/image/location_notice.png",

                buttons.createButtons()
        );
        return  basicCardJson;
    }

    @Override
    public JSONObject locationAgree() throws ParseException {
        BasicCard basicCard = new BasicCard();
        Buttons buttons = new Buttons();
        Button button = new Button("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/locationAgree");
        buttons.addButton(button);
        JSONObject basicCardJson=basicCard.createBasicCard(
                "위치정보의 수집ㆍ이용"," ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                        " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                        " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                "https://www.pointman.shop/image/location_notice.png",
                buttons.createButtons()
        );
        return  basicCardJson;
    }

    @Override
    public String join(KakaoUser user) {
       //회원 검증
//        kakaoUserRepository.save(user);
        return  validateDuplicateMember(user);
    }

    private String validateDuplicateMember(KakaoUser user) {
        Optional<KakaoUser> findUser = kakaoUserRepository.findByUserkey(user.getKakaoUserkey());
        String result ="중복회원";
        if(!findUser.isPresent()){
            kakaoUserRepository.save(user);
            result=user.getKakaoUserkey();
        }
        return result;
    }
}
