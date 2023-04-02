package site.pointman.chatbot.service.serviceimpl;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import site.pointman.chatbot.domain.LocationXY;
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


@Slf4j
@Service
public class WeatherApiServiceImpl implements WeatherApiService {
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
        log.info("codeToName = {}",result);
        return result;
    }


    @Override
    public Map<String, String> selectShortTermWeather(LocationXY xy) {
        Map<String,String> response = new HashMap<>();
        LocationXY convertXY = convertGRID_GPS(xy);

        String x = convertXY.getX();
        String y = convertXY.getY();
        log.info("convertX = {}, convertY = {}",x,y);

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
        log.info("weather API current time = {} ",formatedTime);
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
        weatherReq.setNx(x);
        weatherReq.setNy(y);
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
            log.info("weatherAPI response = {} ",sb);
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


    public static LocationXY convertGRID_GPS(LocationXY xy) {
        double x = 0;
        double y = 0;
        try {
            log.info("convert Start = {}", xy);
            double lat_x = Double.parseDouble(xy.getX());
            double lng_Y = Double.parseDouble(xy.getY());

            log.info("lat={} , lng={}", lat_x, lng_Y);
            int mode = xy.getMode();
            Map<String, Double> latXlngY = new HashMap<>();
            final int TO_GRID = 0;
            final int TO_GPS = 1;

            final double RE = 6371.00877;  //지구 반경 km
            final double GRID = 5.0;       //격자 간격 km
            final double SLAT1 = 30.0;     //투영 위도1
            final double SLAT2 = 60.0;     //투영 위도2
            final double OLON = 126.0;     //기준점 경도
            final double OLAT = 38.0;      //기준점 위도
            final double XO = 43;          //기준점 X좌표
            final double YO = 136;         //기준점 Y좌표

            double DEGRAD = Math.PI / 180.0;
            double RADDEG = 180.0 / Math.PI;

            double re = RE / GRID;
            double slat1 = SLAT1 * DEGRAD;
            double slat2 = SLAT2 * DEGRAD;
            double olon = OLON * DEGRAD;
            double olat = OLAT * DEGRAD;

            double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2) / Math.log(sn));

            double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;

            double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);

            if (mode == TO_GRID) {
                double ra = Math.tan(Math.PI * 0.25 + (lat_x) * DEGRAD * 0.5);
                ra = re * sf / Math.pow(ra, sn);
                double theta = lng_Y * DEGRAD - olon;
                if (theta > Math.PI) theta -= 2.0 * Math.PI;
                if (theta < -Math.PI) theta += 2.0 * Math.PI;
                theta *= sn;
                x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
                y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
            } else {
                double xn = lat_x - XO;
                double yn = ro - lng_Y + YO;
                double ra = Math.sqrt(xn * xn * yn * yn);
                if (sn < 0.0) {
                    ra = -ra;
                }
                double alat = Math.pow((re * sf / ra), (1.0 / sn));
                alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

                double theta = 0.0;
                if (Math.abs(xn) <= 0.0) {
                    theta = 0.0;
                } else {
                    if (Math.abs(yn) <= 0.0) {
                        theta = Math.PI * 0.5;
                        if (xn < 0.0) {
                            theta = -theta;
                        }
                    } else {
                        theta = Math.atan2(xn, yn);
                    }
                }
                double alon = theta / sn + olon;
                x = alat * RADDEG;
                y = alon * RADDEG;
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } finally {
            xy.setX(String.valueOf( (int)Math.round(x)));
            xy.setY(String.valueOf( (int)Math.round(y)));
            return xy;
        }

    }

}
