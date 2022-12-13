package site.pointman.chatbot.service.serviceimpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import site.pointman.chatbot.domain.weatherReqVo;
import site.pointman.chatbot.service.weatherApiService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

@Service
public class weatherApiServiceImpl implements weatherApiService {

    @Override
    public JSONObject selectShortTermWeather() {
        JSONObject response = new JSONObject();

        weatherReqVo weatherReq = new weatherReqVo();
        weatherReq.setServiceKey("9gnt6hr%2FHUiuAFBAUa0tmYIksePfXZfo9sDFe8Nw7oySE15LFBR2mZ%2BsEPsITToh1s4up2xzcbrtPfVCZUoGFg%3D%3D");
        weatherReq.setNumOfRows("11");
        weatherReq.setPageNo("1");
        weatherReq.setDataType("JSON");
        weatherReq.setBase_date("20221211");
        weatherReq.setBase_time("0500");
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
            System.out.println("Response code: " + conn.getResponseCode());
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
            rd.close();
            conn.disconnect();
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONObject jsonResponse = jsonObject.getJSONObject("response");
            JSONObject jsonHeader =  jsonResponse.getJSONObject("header");
            JSONObject jsonBody=  jsonResponse.getJSONObject("body");
            JSONObject jsonItems=  jsonBody.getJSONObject("items");

            response =jsonItems ;
        } catch ( Exception e) {
          System.out.println(e);
        }
        return response;

    }
}
