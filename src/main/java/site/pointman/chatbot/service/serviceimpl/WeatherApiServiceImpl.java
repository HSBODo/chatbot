package site.pointman.chatbot.service.serviceimpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import site.pointman.chatbot.domain.WeatherReqVo;
import site.pointman.chatbot.service.WeatherApiService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Service
public class WeatherApiServiceImpl implements WeatherApiService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Map<String, String> WeatherCodeFindByName(Map<String, String> param) {
        Map<String,String> result = new HashMap<>();
        Map<String,String> findBySkyVal = new HashMap<String,String>(){{
            put("1","맑음");
            put("3","구름많음");
            put("4","흐림");
        }};
        Map<String,String> findByPtyVal = new HashMap<String,String>(){{
            put("0","없음");
            put("1","비");
            put("2","비/눈");
            put("3","눈");
            put("4","소나기");
        }};
        String baseDate = param.get("baseDate");
        String POP = param.get("POP"); //강수확률
        String ptyCode = param.get("PTY"); //강수형태
        String PCP = param.get("PCP"); //1시간 강수량
        String REH = param.get("REH"); //습도
        String SNO = param.get("SNO"); //1시간 적설
        String skyCode = param.get("SKY"); //하늘상태
        String TMP = param.get("TMP"); //1시간 기온
        String TMN = param.get("TMN"); //일 최저기온
        String TMX = param.get("TMX"); //일 최고기온
        String UUU = param.get("UUU"); //풍속(동서성분)
        String VVV = param.get("VVV"); //풍속(남북성분)
        String WAV = param.get("WAV"); //파고
        String VEC = param.get("VEC"); //풍향
        Float wsdCode = Float.parseFloat(param.get("WSD")); //풍속
        String WSD;

       if(wsdCode<4){
           WSD = "바람이 약하게 불어옵니다.";
       } else if (wsdCode>=4 && wsdCode<9) {
           WSD = "바람이 적당히 불어옵니다.";
       } else if (wsdCode>=9 && wsdCode<14) {
           WSD = "바람이 강하게 불어옵니다 .";
       } else if (wsdCode>=14) {
           WSD = "바람이 매우강하게 불어옵니다.";
       }else {
           WSD = "바람이 어떻게 불지 예측할수 없어요.";
       }

        result.put("baseDate",baseDate);
        result.put("SKY",findBySkyVal.get(skyCode));
        result.put("POP",POP);
        result.put("PTY",findByPtyVal.get(ptyCode));
        result.put("PCP",PCP);
        result.put("REH",REH);
        result.put("SNO",SNO);
        result.put("TMP",TMP);
        result.put("UUU",UUU);
        result.put("WSD",WSD);
        logger.info("codeToName"+result);
        return result;
    }

    @Override
    public Map<String, String> selectShortTermWeather() {
        Map<String,String> response = new HashMap<>();

        WeatherReqVo weatherReq = new WeatherReqVo();
        // 현재 날짜
        LocalDate nowDate =  LocalDate.now();
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime nowSeoul = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        // 포맷 정의
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
        // 포맷 적용
        String formatedDate = nowDate.format(dateFormatter);
        int formatedTime = Integer.parseInt(nowSeoul.format(timeFormatter));
        logger.info("time::: "+formatedTime);
        String basTime;
        if(formatedTime>=2 && formatedTime<=4){
            basTime = "0200";
        } else if (formatedTime>=5 && formatedTime<=7) {
            basTime = "0500";
        } else if (formatedTime>=8 && formatedTime<=10) {
            basTime = "0800";
        } else if (formatedTime>=11 && formatedTime<=13) {
            basTime = "1100";
        } else if (formatedTime>=14 && formatedTime<=16) {
            basTime = "1400";
        } else if (formatedTime>=17 && formatedTime<=19) {
            basTime = "1700";
        } else if (formatedTime>=20 && formatedTime<=22) {
            basTime = "2000";
        } else if (formatedTime>=23) {
            basTime = "2300";
        }else {
            formatedDate = nowDate.minusDays(1).format(dateFormatter);
            basTime = "2300";
        }
        weatherReq.setServiceKey("9gnt6hr%2FHUiuAFBAUa0tmYIksePfXZfo9sDFe8Nw7oySE15LFBR2mZ%2BsEPsITToh1s4up2xzcbrtPfVCZUoGFg%3D%3D");
        weatherReq.setNumOfRows("12");
        weatherReq.setPageNo("1");
        weatherReq.setDataType("JSON");
        weatherReq.setBase_date(formatedDate);
        weatherReq.setBase_time(basTime);
        weatherReq.setNx("55");
        weatherReq.setNy("127");
        try {

            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+weatherReq.getServiceKey()); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(weatherReq.getPageNo(), "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(weatherReq.getNumOfRows(), "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(weatherReq.getDataType(), "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(weatherReq.getBase_date(), "UTF-8")); /*‘21년 6월 28일발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(weatherReq.getBase_time(), "UTF-8")); /*05시 발표*/
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(weatherReq.getNx(), "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(weatherReq.getNy(), "UTF-8")); /*예보지점의 Y 좌표값*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            logger.info("response::: "+sb);
            rd.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject jsonResponse = jsonObject.getJSONObject("response");
            JSONObject jsonHeader =  jsonResponse.getJSONObject("header");
            JSONObject jsonBody =  jsonResponse.getJSONObject("body");
            JSONObject jsonItems =  jsonBody.getJSONObject("items");
            JSONArray jsonArray = jsonItems.optJSONArray("item");

            Map<String,String> elementMap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = (JSONObject) jsonArray.opt(i);
                elementMap.put(element.optString("category"),element.optString("fcstValue"));
            }
            elementMap.put("baseDate",nowDate.toString());
            response = elementMap ;
        } catch ( Exception e) {
          System.out.println(e);
        }
        return response;
    }


}
