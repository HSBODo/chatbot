package site.pointman.chatbot.dto.weatherapi;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class WeatherPropertyCodeDto {
    private String baseDate;
    private int sky; //하늘상태
    private String pop; //강수확률
    private int pty; //강수형태
    private String reh; //습도
    private String vec; //풍향
    private String tmp; //1시간 기온
    private String uuu; //풍속(동서성분)
    private String vvv; //풍속(남북성분)
    private float wsd; //바람
    private String pcp; //1시간 강수량
    private String wav; //파고
    private String sno; //1시간 적설
    private String tmn; //일 최저기온
    private String tmx; //일 최고기온
    private String imgUrl;
    private String hostUrl;


    public String getImgUrl() {
        if(pty==1 || pty==2 ||pty==4){
            imgUrl = hostUrl+"/image/weather/raindrops.jpg";
        } else if (pty==3) {
            imgUrl = hostUrl+"/image/weather/snow.jpg";
        } else {

            if(sky ==3){
                imgUrl = hostUrl+"/image/weather/clouds.jpg";
            } else if (sky ==4) {
                imgUrl = hostUrl+"/image/weather/fog.jpg";
            }else {
                imgUrl = hostUrl+"/image/weather/sunshine.jpg";
            }

        }
        return imgUrl;
    }
    public String getSkyValue() {
        Map findBySkyVal = new HashMap<Integer,String>(){{
            put(1,"맑음");
            put(3,"구름많음");
            put(4,"흐림");
        }};
        return (String)findBySkyVal.get(sky);
    }


    public String getPtyValue() {
       Map findByPtyVal = new HashMap<Integer,String>(){{
            put(0,"없음");
            put(1,"비");
            put(2,"비/눈");
            put(3,"눈");
            put(4,"소나기");
        }};
        return (String)findByPtyVal.get(pty);
    }

    public String getBaseDateValue() throws ParseException {
        SimpleDateFormat strToDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateToStr = new SimpleDateFormat("yyyy-MM-dd");
        Date yyyyMMdd = strToDate.parse(baseDate);
        String yyyy_MM_dd = dateToStr.format(yyyyMMdd);
        return yyyy_MM_dd;
    }
    public String getWsdValue() {
        String wsdValue;
        if(wsd<4){
            wsdValue = "바람이 약하게 불어옵니다.";
        } else if (wsd>=4 && wsd<9) {
            wsdValue = "바람이 적당히 불어옵니다.";
        } else if (wsd>=9 && wsd<14) {
            wsdValue = "바람이 강하게 불어옵니다 .";
        } else if (wsd>=14) {
            wsdValue = "바람이 매우강하게 불어옵니다.";
        }else {
            wsdValue = "바람이 어떻게 불지 예측할수 없어요.";
        }
        return wsdValue;
    }

    public void mapToVo(Map<String, String> elementMap) {
        this.baseDate = elementMap.get("baseDate");
        this.pty = Integer.parseInt(elementMap.get("PTY"));
        this.pop = elementMap.get("POP");
        this.pcp = elementMap.get("PCP");
        this.reh = elementMap.get("REH");
        this.tmp = elementMap.get("TMP");
        this.sky = Integer.parseInt(elementMap.get("SKY"));
        this.uuu = elementMap.get("UUU");
        this.vec = elementMap.get("VEC");
        this.wav = elementMap.get("WAV");
        this.wsd = Float.parseFloat(elementMap.get("WSD"));
        this.sno = elementMap.get("SNO");
        this.vvv = elementMap.get("VVV");
        this.hostUrl = elementMap.get("hostUrl");
    }
}
