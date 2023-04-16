package site.pointman.chatbot.service.serviceimpl;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.Search;
import site.pointman.chatbot.dto.wearherapi.WeatherElementCode;
import site.pointman.chatbot.dto.wearherapi.WeatherReq;
import site.pointman.chatbot.service.OpenApiService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class OpenApiServiceImpl implements OpenApiService {
    @Value("${host.url}")
    private String hostUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;
    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${naver.api.key}")
    private String naverApiKey;
    @Value("${naver.api.secret}")
    private String naverApiSecretKey;
    private JSONParser jsonParser = new JSONParser();
    @Override
    public WeatherElementCode selectShortTermWeather(KakaoMemberLocation kakaoUserLocation) {
        WeatherElementCode response = new WeatherElementCode();
        try {
            convertGRID_GPS(kakaoUserLocation,0);
            BigDecimal x = kakaoUserLocation.getX();
            BigDecimal y = kakaoUserLocation.getY();
            log.info("convertX = {}, convertY = {}",x,y);
            WeatherReq weatherReq = new WeatherReq();
            int formatedTime = currentTimeFormat();
            String formatedDate = currentDateFormat(formatedTime);

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
                basTime = "2300";
            }
            weatherReq.setServiceKey(weatherApiKey);
            weatherReq.setNumOfRows("12");
            weatherReq.setPageNo("1");
            weatherReq.setDataType("JSON");
            weatherReq.setBase_date(formatedDate);
            weatherReq.setBase_time(basTime);
            weatherReq.setNx(x.toString());
            weatherReq.setNy(y.toString());

            String serviceKey = weatherReq.getServiceKey();
            String numOfRows = getStringEncoded(weatherReq.getNumOfRows());
            String pageNo = getStringEncoded(weatherReq.getPageNo());
            String dataType = getStringEncoded(weatherReq.getDataType());
            String base_date = getStringEncoded(weatherReq.getBase_date());
            String base_time = getStringEncoded(weatherReq.getBase_time());
            String nx = getStringEncoded(weatherReq.getNx());
            String ny = getStringEncoded(weatherReq.getNy());

            String apiURL = weatherApiUrl+"?serviceKey=" + serviceKey+"&pageNo=" + pageNo + "&numOfRows="+numOfRows+"&dataType="+dataType+"&base_date="+base_date+"&base_time="+base_time+"&nx="+nx+"&ny="+ny;    // JSON 결과


            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Content-type", "application/json");
            String responseBody = get(apiURL,requestHeaders);

            JSONObject responsejsonObject = new JSONObject(responseBody);
            log.info("responsejsonObject={}",responsejsonObject);
            JSONObject jsonResponse = responsejsonObject.getJSONObject("response");
            JSONObject jsonHeader =  jsonResponse.getJSONObject("header");
            JSONObject jsonBody =  jsonResponse.getJSONObject("body");
            JSONObject jsonItems = jsonBody.getJSONObject("items");
            JSONArray  jsonItemList = jsonItems.getJSONArray("item");
            Map<String,String> elementMap = new HashMap<>();
            elementMap.put("baseDate",formatedDate);
            for (int i = 0; i < jsonItemList.length(); i++) {
                JSONObject element = (JSONObject) jsonItemList.opt(i);
                elementMap.put(element.optString("category"),element.optString("fcstValue"));
            }
            response = getWeatherCodeMapping(elementMap);
        } catch ( Exception e) {
            throw new IllegalArgumentException("날씨 api 실패");
        }
        return response;
    }



    @Override
    public Search selectNaverSearch(String searchText, String display,String start, String sort) throws ParseException {
        String clientId = naverApiKey; //애플리케이션 클라이언트 아이디
        String clientSecret = naverApiSecretKey; //애플리케이션 클라이언트 시크릿
        log.info("clientId={},clientSecret={}",clientId,clientSecret);

        String text = searchText;
        text = getStringEncoded(text);

        //query=%EC%A3%BC%EC%8B%9D&display=10&start=1&sort=sim
        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text+"&display=" + display + "&start="+start+"&sort="+sort;    // JSON 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        JSONObject responsejsonObject = new JSONObject(responseBody);

        JSONArray items = new JSONArray();
        items = (JSONArray) responsejsonObject.get("items");
        LocalDate nowDate =  LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedDate = nowDate.format(dateFormatter);

        Search search = new Search();
        search.setLastBuildDate(formatedDate);
        search.setTotal((int) responsejsonObject.get("total"));
        search.setStart((int) responsejsonObject.get("start"));
        search.setDisplay((int) responsejsonObject.get("display"));
        search.setItems(items);
        return search;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
    private static String getStringEncoded(String text) {
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
        return text;
    }

    private  WeatherElementCode getWeatherCodeMapping(Map<String, String> elementMap) {
        WeatherElementCode weatherElementCode = new WeatherElementCode();
        weatherElementCode.setBaseDate(elementMap.get("baseDate"));
        weatherElementCode.setPty(Integer.parseInt(elementMap.get("PTY")));
        weatherElementCode.setPop(elementMap.get("POP"));
        weatherElementCode.setPcp(elementMap.get("PCP"));
        weatherElementCode.setReh(elementMap.get("REH"));
        weatherElementCode.setTmp(elementMap.get("TMP"));
        weatherElementCode.setSky(Integer.parseInt(elementMap.get("SKY")));
        weatherElementCode.setUuu(elementMap.get("UUU"));
        weatherElementCode.setVec(elementMap.get("VEC"));
        weatherElementCode.setWav(elementMap.get("WAV"));
        weatherElementCode.setWsd(Float.parseFloat(elementMap.get("WSD")));
        weatherElementCode.setSno(elementMap.get("SNO"));
        weatherElementCode.setVvv(elementMap.get("VVV"));
        weatherElementCode.setHostUrl(hostUrl);
        return weatherElementCode;
    }
    private  int currentTimeFormat (){
        // 현재 날짜
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime nowSeoul = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        // 포맷 정의
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
        // 포맷 적용
        int formatedTime = Integer.parseInt(nowSeoul.format(timeFormatter));

        return formatedTime;
    }
    private  String currentDateFormat (int formatedTime){
        LocalDate nowDate =  LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatedDate;
        if(formatedTime<=1){
            formatedDate = nowDate.minusDays(1).format(dateFormatter);
        }else {
            formatedDate = nowDate.format(dateFormatter);
        }
        return formatedDate;
    }
    private  void convertGRID_GPS(KakaoMemberLocation kakaoUserLocation , int mode ) {

        double x = 0;
        double y = 0;
        try {

            double lat_x = Double.parseDouble(kakaoUserLocation.getX().toString());
            double lng_Y = Double.parseDouble(kakaoUserLocation.getY().toString());

            log.info("kakao lat={} , lng={}", lat_x, lng_Y);


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
            kakaoUserLocation.setX(BigDecimal.valueOf( (int)Math.round(x)));
            kakaoUserLocation.setY(BigDecimal.valueOf( (int)Math.round(y)));
        }
    }

}
