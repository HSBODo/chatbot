package site.pointman.chatbot.service.serviceimpl;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;
import site.pointman.chatbot.dto.kakaopay.KakaoPayReadyDto;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCodeDto;
import site.pointman.chatbot.dto.wearherapi.WeatherRequestDto;
import site.pointman.chatbot.repository.KaKaoItemRepository;
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
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
public class OpenApiServiceImpl implements OpenApiService {

    private KaKaoItemRepository kaKaoItemRepository;
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

    @Value("${kakao.pay.cid}")
    private String cid;
    @Value("${kakao.pay.approval_url}")
    private String approval_url;
    @Value("${kakao.pay.fail_url}")
    private String fail_url;
    @Value("${kakao.pay.cancel_url}")
    private String cancel_url;
    @Value("${kakao.admin.key}")
    private String kakaoAdminKey;

    private JSONParser jsonParser = new JSONParser();

    public OpenApiServiceImpl(KaKaoItemRepository kaKaoItemRepository) {
        this.kaKaoItemRepository = kaKaoItemRepository;
    }

    @Override
    public WeatherPropertyCodeDto selectShortTermWeather(KakaoMemberLocation kakaoUserLocation) {
        WeatherPropertyCodeDto response = new WeatherPropertyCodeDto();
        try {
            convertGRID_GPS(kakaoUserLocation,0);

            BigDecimal x = kakaoUserLocation.getX();
            BigDecimal y = kakaoUserLocation.getY();
            log.info("convertX = {}, convertY = {}",x,y);

            WeatherRequestDto weatherRequestDto = new WeatherRequestDto();

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

            String serviceKey = weatherApiKey;
            String numOfRows = getStringEncoded("12");
            String pageNo = getStringEncoded("1");
            String dataType = getStringEncoded("JSON");
            String base_date = getStringEncoded(formatedDate);
            String base_time = getStringEncoded(basTime);
            String nx = getStringEncoded(x.toString());
            String ny = getStringEncoded(y.toString());

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

            Map<String,String> responsePropertys = new HashMap<>();
            responsePropertys.put("baseDate",formatedDate);
            for (int i = 0; i < jsonItemList.length(); i++) {
                JSONObject element = (JSONObject) jsonItemList.opt(i);
                responsePropertys.put(element.optString("category"),element.optString("fcstValue"));
            }
            response = getWeatherCodeMapping(responsePropertys);
        } catch ( Exception e) {
            throw new IllegalArgumentException("날씨 api 실패");
        }
        return response;
    }
    @Override
    public SearchDto selectNaverSearch(String searchText, String display, String start, String sort) {
        SearchDto searchDto = new SearchDto();
        try {
            String clientId = naverApiKey; //애플리케이션 클라이언트 아이디
            String clientSecret = naverApiSecretKey; //애플리케이션 클라이언트 시크릿

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


            searchDto.setLastBuildDate(formatedDate);
            searchDto.setTotal((int) responsejsonObject.get("total"));
            searchDto.setStart((int) responsejsonObject.get("start"));
            searchDto.setDisplay((int) responsejsonObject.get("display"));
            searchDto.setItems(items);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("naver 뉴스 정보를 불러오는 데 실패하였습니다.");
        }
        return searchDto;
    }
    @Override
    public KakaoPayReadyDto createKakaoPayReady(Long itemCode, String kakaoUserkey) {
        Optional<Item> maybeItem = kaKaoItemRepository.findByItem(itemCode);
        if(maybeItem.isEmpty()) throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        KakaoPayReadyDto kakaoPayReadyDto = new KakaoPayReadyDto(
                kakaoUserkey,
                cid,
                "partner_order_id",
                "partner_user_id",
                maybeItem.get().getProfileNickname(),
                1,
                maybeItem.get().getDiscountedPrice(),
                0,
                0,
                approval_url,
                fail_url,
                cancel_url,
                maybeItem.get().getItemCode());
        return kakaoPayReadyDto;
    }
    @Override
    public Order kakaoPayReady(KakaoPayReadyDto kakaoPayReadyDto) throws Exception {

        Long orderId = createOrderId();
        String itemName= kakaoPayReadyDto.getItem_name().replaceAll(" ","");
        String apiURL = "https://kapi.kakao.com/v1/payment/ready";    // JSON 결과
        StringBuilder urlBuilder = new StringBuilder(apiURL); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("cid","UTF-8") + "="+ kakaoPayReadyDto.getCid());
        urlBuilder.append("&" + URLEncoder.encode("partner_order_id","UTF-8") + "="+ kakaoPayReadyDto.getPartner_order_id());
        urlBuilder.append("&" + URLEncoder.encode("partner_user_id","UTF-8") + "="+ kakaoPayReadyDto.getPartner_user_id());
        urlBuilder.append("&" + URLEncoder.encode("item_name","UTF-8") + "="+itemName);
        urlBuilder.append("&" + URLEncoder.encode("quantity","UTF-8") + "="+ kakaoPayReadyDto.getQuantity());
        urlBuilder.append("&" + URLEncoder.encode("total_amount","UTF-8") + "="+ kakaoPayReadyDto.getTotal_amount());
        urlBuilder.append("&" + URLEncoder.encode("vat_amount","UTF-8") + "="+ kakaoPayReadyDto.getVat_amount());
        urlBuilder.append("&" + URLEncoder.encode("tax_free_amount","UTF-8") + "="+ kakaoPayReadyDto.getTax_free_amount());
        urlBuilder.append("&" + URLEncoder.encode("approval_url","UTF-8") + "="+"https://www.pointman.shop/kakaochat/v1/"+orderId+"/kakaopay-approve");
        urlBuilder.append("&" + URLEncoder.encode("fail_url","UTF-8") + "="+ kakaoPayReadyDto.getFail_url());
        urlBuilder.append("&" + URLEncoder.encode("cancel_url","UTF-8") + "="+ kakaoPayReadyDto.getCancel_url());
//        urlBuilder.append("&" + URLEncoder.encode("payment_method_type","UTF-8") + "=");
        log.info("kakaoPayReady={}",urlBuilder);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "KakaoAK "+kakaoAdminKey);
        requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        String responseBody = post(String.valueOf(urlBuilder),requestHeaders);
        log.info("responseBody={}",responseBody);
        JSONObject responsejsonObject = new JSONObject(responseBody);
        Order order = getKakaoPay(kakaoPayReadyDto, orderId, responsejsonObject);
        return kaKaoItemRepository.savePayReady(order);
    }
    @Override
    public KakaoPayReadyDto kakaoPayApprove(String pg_token , Long orderId) throws Exception {
        Optional<Order> maybeReadyOrder = kaKaoItemRepository.findByReadyOrder(orderId);
        if(maybeReadyOrder.isEmpty()) throw new NullPointerException("결제 승인대기 주문이 없습니다.");
        Order order = maybeReadyOrder.get();
        try {
            String apiURL = "https://kapi.kakao.com/v1/payment/approve";    // JSON 결과
            StringBuilder urlBuilder = new StringBuilder(apiURL); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("cid","UTF-8") + "="+"TC0ONETIME");
            urlBuilder.append("&" + URLEncoder.encode("tid","UTF-8") + "="+ order.getTid());
            urlBuilder.append("&" + URLEncoder.encode("partner_order_id","UTF-8") + "="+ order.getPartner_order_id());
            urlBuilder.append("&" + URLEncoder.encode("partner_user_id","UTF-8") + "="+ order.getPartner_user_id());
            urlBuilder.append("&" + URLEncoder.encode("pg_token","UTF-8") + "="+pg_token);
            urlBuilder.append("&" + URLEncoder.encode("payload","UTF-8") + "="+"1");
            urlBuilder.append("&" + URLEncoder.encode("total_amount","UTF-8") + "="+ order.getTotal_amount());

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", "KakaoAK "+kakaoAdminKey);
            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            String responseBody = post(String.valueOf(urlBuilder),requestHeaders);
            log.info("kakaoApprove responseBody={}",responseBody);

            JSONObject responsejsonObject = new JSONObject(responseBody);
            PayMethod payMethod = responsejsonObject.getString("payment_method_type").equalsIgnoreCase("MONEY")? PayMethod.카카오페이 :PayMethod.카드;
            order.setApproved_at(responsejsonObject.getString("approved_at"));
            order.setAid(responsejsonObject.getString("aid"));
            order.setPayment_method_type(payMethod);
            order.setStatus(OrderStatus.결제승인);
            kaKaoItemRepository.updatePayApprove(order);

        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("결제승인 실패하였습니다.");
        }
        return null;
    }
    @Override
    public Order kakaoPayCancel(Long orderId) throws Exception {

        Optional<Order> maybeApproveOrder = kaKaoItemRepository.findByApproveOrder(orderId);
        if(maybeApproveOrder.isEmpty()) throw  new NullPointerException("결제승인 완료된 주문이 없습니다.");
        try {
            String apiURL = "https://kapi.kakao.com/v1/payment/cancel";    // JSON 결과
            StringBuilder urlBuilder = new StringBuilder(apiURL); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("cid","UTF-8") + "="+"TC0ONETIME");
            urlBuilder.append("&" + URLEncoder.encode("tid","UTF-8") + "="+ maybeApproveOrder.get().getTid());
            urlBuilder.append("&" + URLEncoder.encode("cancel_amount","UTF-8") + "="+ maybeApproveOrder.get().getTotal_amount());
            urlBuilder.append("&" + URLEncoder.encode("cancel_tax_free_amount","UTF-8") + "="+ maybeApproveOrder.get().getTax_free_amount());
            urlBuilder.append("&" + URLEncoder.encode("cancel_vat_amount","UTF-8") + "="+maybeApproveOrder.get().getVat_amount());
//        urlBuilder.append("&" + URLEncoder.encode("cancel_available_amount","UTF-8") + "="+"0");

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", "KakaoAK "+kakaoAdminKey);
            requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
            String responseBody = post(String.valueOf(urlBuilder),requestHeaders);

            JSONObject responsejsonObject = new JSONObject(responseBody);
            maybeApproveOrder.get().setCanceled_at(responsejsonObject.getString("canceled_at"));
            maybeApproveOrder.get().setStatus(OrderStatus.결제취소);
            kaKaoItemRepository.updatePayCancel(maybeApproveOrder.get());

            log.info("responsejsonObject={}",responsejsonObject);

        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalAccessException("결제취소 실패하였습니다.");
        }

        return maybeApproveOrder.get();
    }

    private  Order getKakaoPay(KakaoPayReadyDto kakaoPayReadyDto, Long orderId, JSONObject responsejsonObject) {
        Order order = new Order();
        order.setKakao_userkey(kakaoPayReadyDto.getKakaoUserkey());
        order.setTid(responsejsonObject.getString("tid"));
        order.setCid(kakaoPayReadyDto.getCid());
        order.setItem_name(kakaoPayReadyDto.getItem_name());
        order.setItem_code(kakaoPayReadyDto.getItem_code());
        order.setQuantity(kakaoPayReadyDto.getQuantity());
        order.setStatus(OrderStatus.결제대기);
        order.setTax_free_amount(kakaoPayReadyDto.getTax_free_amount());
        order.setTotal_amount(kakaoPayReadyDto.getTotal_amount());
        order.setVat_amount(kakaoPayReadyDto.getVat_amount());
        order.setPartner_order_id(kakaoPayReadyDto.getPartner_order_id());
        order.setPartner_user_id(kakaoPayReadyDto.getPartner_user_id());
        order.setAndroid_app_scheme(responsejsonObject.getString("android_app_scheme"));
        order.setIos_app_scheme(responsejsonObject.getString("ios_app_scheme"));
        order.setNext_redirect_mobile_url(responsejsonObject.getString("next_redirect_mobile_url"));
        order.setNext_redirect_app_url(responsejsonObject.getString("next_redirect_app_url"));
        order.setNext_redirect_pc_url(responsejsonObject.getString("next_redirect_pc_url"));
        order.setOrder_id(orderId);
        return order;
    }
    public  Long createOrderId(){
      return  Long.parseLong(String.valueOf(ThreadLocalRandom.current().nextInt(100000000,999999999)));
    }
    private  String post(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("POST");
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
    private  String get(String apiUrl, Map<String, String> requestHeaders){
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
    private  String readBody(InputStream body){
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
    private  HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
    private  String getStringEncoded(String text) {
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
        return text;
    }
    private WeatherPropertyCodeDto getWeatherCodeMapping(Map<String, String> elementMap) {
        WeatherPropertyCodeDto weatherPropertyCodeDto = new WeatherPropertyCodeDto();
        weatherPropertyCodeDto.setBaseDate(elementMap.get("baseDate"));
        weatherPropertyCodeDto.setPty(Integer.parseInt(elementMap.get("PTY")));
        weatherPropertyCodeDto.setPop(elementMap.get("POP"));
        weatherPropertyCodeDto.setPcp(elementMap.get("PCP"));
        weatherPropertyCodeDto.setReh(elementMap.get("REH"));
        weatherPropertyCodeDto.setTmp(elementMap.get("TMP"));
        weatherPropertyCodeDto.setSky(Integer.parseInt(elementMap.get("SKY")));
        weatherPropertyCodeDto.setUuu(elementMap.get("UUU"));
        weatherPropertyCodeDto.setVec(elementMap.get("VEC"));
        weatherPropertyCodeDto.setWav(elementMap.get("WAV"));
        weatherPropertyCodeDto.setWsd(Float.parseFloat(elementMap.get("WSD")));
        weatherPropertyCodeDto.setSno(elementMap.get("SNO"));
        weatherPropertyCodeDto.setVvv(elementMap.get("VVV"));
        weatherPropertyCodeDto.setHostUrl(hostUrl);
        return weatherPropertyCodeDto;
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
