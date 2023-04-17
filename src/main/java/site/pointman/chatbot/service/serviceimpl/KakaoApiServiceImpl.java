package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.dto.kakaoui.ListCardItem;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.Search;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCode;
import site.pointman.chatbot.dto.kakaoui.Button;
import site.pointman.chatbot.repository.KaKaoItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.OpenApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    private KaKaoItemRepository kaKaoItemRepository;
    private KakaoMemberRepository KakaoMemberRepository;
    private OpenApiService openApiService;
    private JSONParser jsonParser = new JSONParser();


    public KakaoApiServiceImpl(KaKaoItemRepository kaKaoItemRepository, KakaoMemberRepository kakaoMemberRepository, OpenApiService openApiService) {
        this.kaKaoItemRepository = kaKaoItemRepository;
        this.KakaoMemberRepository = kakaoMemberRepository;
        this.openApiService = openApiService;
    }

    @Override
    public JSONObject createTodayNews(String searchText) throws Exception {
        Search search = openApiService.selectNaverSearch(searchText,"30","1","date");
        List carouselItems = new ArrayList<>();
        List<ListCardItem> listCardItems = new ArrayList<>();
        List<Button> buttons = new ArrayList<>();
        Button button = new Button("webLink","구경가기","");
        buttons.add(button);
        search.getItems().forEach(item -> {
            try {
                JSONObject jsonObject = new JSONObject((JSONObject) jsonParser.parse(item.toString()));
                String title =replaceAll((String) jsonObject.get("title"));
                String description =replaceAll((String) jsonObject.get("description"));
                Map webLink = new HashMap<>();
                webLink.put("web", jsonObject.get("link"));
                ListCardItem listCardItem = new ListCardItem(title,description,"https://www.pointman.shop/image/news.jpg",webLink);
                listCardItems.add(listCardItem);
                int cnt =  search.getItems().indexOf(item)+1;
                log.info("index={}",cnt);
                if(cnt%5==0){
                    carouselItems.add(createListCard("carousel",search.getLastBuildDate()+" 오늘의 뉴스",listCardItems,buttons));
                    listCardItems.clear();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return createCarousel("listCard",carouselItems);
    }
    private String replaceAll (String text){
        text=text.replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>","");
        text=text.replaceAll("&gt;",">");
        text=text.replaceAll("&lt;","<");
        text=text.replaceAll("&quot;","");
        text=text.replaceAll("&apos;","");
        text=text.replaceAll("&nbsp;"," ");
        text=text.replaceAll("&amp;","&");
        return text;
    }
    @Override
    public JSONObject createTodayWeather(String kakaoUserkey) throws Exception {
        KakaoMemberLocation kakaoUserLocation = KakaoMemberRepository.findByLocation(kakaoUserkey).get();
        WeatherPropertyCode weatherCode =  openApiService.selectShortTermWeather(kakaoUserLocation);
        List buttonList = new ArrayList<Button>();

        return  createBasicCard("basic",
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
                buttonList
        );
    }

    @Override
    public JSONObject createLocationNotice(String kakaoUserkey) throws ParseException {
        Button button = new Button("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
        List buttonList = new ArrayList<Button>();
        buttonList.add(button);
        return createBasicCard(
                "basic",
                "위치정보의 수집ㆍ이용",
                " ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                        " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                        " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                "https://www.pointman.shop/image/location_notice.jpg",
                buttonList
        );
    }

    @Override
    public JSONObject createDeveloperInfo() throws ParseException {
        List buttonList = new ArrayList<Button>();
        Button blog = new Button("webLink","블로그","https://pointman.tistory.com/");
        Button gitHub = new Button("webLink","GitHub","https://github.com/HSBODo");
        Button portfolio = new Button("webLink","포트폴리오","https://www.pointman.shop");
        buttonList.add(blog);
        buttonList.add(gitHub);
        buttonList.add(portfolio);
        return createBasicCard(
                "basic",
                "Backend Developer",
                "안녕하세요.\n" +
                        "백엔드 개발자 한수빈입니다.\n" +
                        "Email: vinsulill@gmail.com",
                "https://www.pointman.shop/image/Ryan1.jpg",
                buttonList
        );
    }
    @Override
    public JSONObject createRecommendItems(String kakaoUserkey) throws ParseException {
        List<Item> findItems = kaKaoItemRepository.findByDisplayItems();
        List items = new ArrayList<>();
        findItems.stream()
                .forEach(item ->{
                    List<Button>buttons= new ArrayList<>();
                    Button button = new Button("webLink","구매하러가기",item.getThumbnailLink());
                    Button button1 = new Button("webLink","카카오페이 결제","https://www.pointman.shop/kakaochat/v1/kakaopay-ready?itemcode="+item.getItemCode()+"&kakaouserkey="+kakaoUserkey);
                    buttons.add(button);
                    buttons.add(button1);
                    try {
                        JSONObject commerceCard = createCommerceCard(
                                "carousel",
                                item.getDescription(),
                                item.getPrice(),
                                item.getDiscount(),
                                item.getDiscountedPrice(),
                                item.getDiscountRate(),
                                item.getCurrency(),
                                item.getThumbnailImgUrl(),
                                item.getThumbnailLink(),
                                item.getProfileImgUrl(),
                                item.getProfileNickname(),
                                buttons
                        );
                        items.add(commerceCard);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                });
        return createCarousel("commerceCard",items);
    }

    @Override
    public JSONObject createSimpleText(String msg) throws ParseException {
        /**
         이름	타입	        필수 여부	설명	제한
         text	string	        O	    전달하고자 하는 텍스트입니다	1000자
         */
        String resultJson ="{\n" +
                "               \"simpleText\": {\n" +
                "                   \"text\": \""+msg+"\",\n" +
                "               }\n" +
                "           }";
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
    @Override
    public JSONObject createBasicCard(String displayType, String title, String description, String thumbnailImgUrl, List<Button> buttonList) throws ParseException {
        /**
         Information. description 필드

         케로셀 타입에서 description은 최대 76자까지 가능합니다.
         케로셀의 경우 2줄, 일반 카드의 경우 4줄까지 노출됩니다.
         클라이언트에 따라서 230자, 76자보다 적게 노출될 수도 있습니다.

         필드명	        타입	        필수 여부	    설명	제한
         title	        string	        X	        카드의 제목입니다.	최대 2줄
         description	string	        X	        카드에 대한 상세 설명입니다.	최대 230자
         thumbnail	    Thumbnail	    O	        카드의 상단 이미지입니다.
         profile	    Profile	        X	        카드의 프로필 정보입니다.
         social	        Social	        X	        카드의 소셜 정보입니다.
         buttons	    Array<Button>	X	        카드의 버튼들을 포함합니다.	최대 3개
         * social과 profile은 현재 미지원 상태입니다.

         */
        JSONArray buttons = new JSONArray(buttonList);
        String resultJson;
        if(displayType=="carousel"){
             resultJson ="{\n" +
                    "          \"title\": \""+title+"\",\n" +
                    "          \"description\": \""+description+"\",\n" +
                    "          \"thumbnail\": {\n" +
                    "            \"imageUrl\": \""+thumbnailImgUrl+"\"\n" +
                    "          },\n" +
                    "          \"buttons\": "+buttons+"\n" +
                    "      }";
        }else {
             resultJson ="{\n" +
                    "        \"basicCard\": {\n" +
                    "          \"title\": \""+title+"\",\n" +
                    "          \"description\": \""+description+"\",\n" +
                    "          \"thumbnail\": {\n" +
                    "            \"imageUrl\": \""+thumbnailImgUrl+"\"\n" +
                    "          },\n" +
                    "          \"buttons\": "+buttons+"\n" +
                    "        }\n" +
                    "      }";
        }

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }


    @Override
    public JSONObject createSimpleImage(String altText,String imgUrl) throws ParseException {
        /**
         이름	        타입	        필수 여부	        설명	제한
         imageUrl	    string	        O	            전달하고자 하는 이미지의 url입니다	URL 형식
         altText	    string	        O	            url이 유효하지 않은 경우, 전달되는 텍스트입니다	최대 1000자
         */
        String resultJson ="{\n" +
                "               \"simpleImage\": {\n" +
                "                   \"imageUrl\": \""+imgUrl+"\",\n" +
                "                   \"altText\": \""+altText+"\",\n" +
                "               }\n" +
                "           }";

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);

        return resultJsonObj;
    }



    @Override
    public JSONObject createCommerceCard(String displayType,String description, int price, int discount, int discountedPrice, int discountRate, String currency, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String ProfileNickname, List<Button> buttonList) throws ParseException {
        /**
         Information. price, discount, discountedPrice 의 동작 방식

         discountedPrice 가 존재하면 price, discount, discountRate 과 관계 없이 무조건 해당 값이 사용자에게 노출됩니다.
         예) price: 10000, discount: 7000, discountedPrice: 2000 인 경우, 3000 (10000 - 7000)이 아닌 2000이 사용자에게 노출
         위의 예에서 discountedPrice가 없는 경우, 3000이 사용자에게 노출
         예) price: 10000, discountRate: 70, discountedPrice: 2000 인 경우, 3000 (10000 * 0.3)이 아닌 2000이 사용자에게 노출
         discountRate은 discountedPrice를 필요로 합니다. discountedPrice가 주어지지 않으면 사용자에게 >discountRate을 노출하지 않습니다.
         discountRate과 discount가 동시에 있는 경우, discountRate을 우선적으로 노출합니다.

         필드명	            타입	            필수 여부	            설명	                            제한
         description	    string	            O	        제품에 대한 상세 설명입니다.	            최대 40자
         price	            int             	O	        제품의 가격입니다.
         currency	        string	            O	        제품의 가격에 대한 통화입니다.	            현재 won만 가능
         discount	        int	                X	        제품의 가격에 대한 할인할 금액입니다.
         discountRate	    int	                X	        제품의 가격에 대한 할인율입니다.
         dicountedPrice	    int	                X           (discountRate을 쓰는 경우 필수)	        제품의 가격에 대한 할인가(할인된 가격)입니다.
         thumbnails	        Array<Thumbnail>	O	        제품에 대한 사진입니다.	현재 1개만 가능
         profile	        Profile	            X	        제품을 판매하는 프로필 정보입니다.
         buttons	        Array<Button>	    O	        다양한 액션을 수행할 수 있는 버튼입니다.	1개 이상, 3개 이하

         */
        JSONArray buttons = new JSONArray(buttonList);
        String resultJson;
        if(displayType=="carousel"){
            resultJson ="{\n" +
                    "          \"description\": \""+description+"\",\n" +
                    "          \"price\": "+price+",\n" +
                    "          \"discount\": "+discount+",\n" +
                    "          \"discountedPrice\": \""+discountedPrice+"\",\n" +
                    "          \"discountRate\": \""+discountRate+"\",\n" +
                    "          \"currency\": \""+currency+"\",\n" +
                    "          \"thumbnails\": [\n" +
                    "            {\n" +
                    "              \"imageUrl\": \""+thumbnailImgUrl+"\",\n" +
                    "              \"link\": {\n" +
                    "                \"web\": \""+thumbnailLink+"\"\n" +
                    "              }\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"profile\": {\n" +
                    "            \"imageUrl\": \""+profileImgUrl+"\",\n" +
                    "            \"nickname\": \""+ProfileNickname+"\"\n" +
                    "          },\n" +
                    "          \"buttons\":"+buttons+"\n"+
                    "      }";


        }else {
            resultJson = "{\n" +
                    "        \"commerceCard\": {\n" +
                    "          \"description\": \"" + description + "\",\n" +
                    "          \"price\": " + price + ",\n" +
                    "          \"discount\": " + discount + ",\n" +
                    "          \"discountedPrice\": \"" + discountedPrice + "\",\n" +
                    "          \"discountRate\": \"" + discountRate + "\",\n" +
                    "          \"currency\": \"" + currency + "\",\n" +
                    "          \"thumbnails\": [\n" +
                    "            {\n" +
                    "              \"imageUrl\": \"" + thumbnailImgUrl + "\",\n" +
                    "              \"link\": {\n" +
                    "                \"web\": \"" + thumbnailLink + "\"\n" +
                    "              }\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"profile\": {\n" +
                    "            \"imageUrl\": \"" + profileImgUrl + "\",\n" +
                    "            \"nickname\": \"" + ProfileNickname + "\"\n" +
                    "          },\n" +
                    "          \"buttons\":" + buttons + "\n" +
                    "        }\n" +
                    "      }";

        }
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
    @Override
    public JSONObject createListCard(String displayType,String title, List<ListCardItem> listCardItemList, List<Button> buttonList) throws ParseException {
        JSONArray buttons = new JSONArray(buttonList);
        JSONArray listCardItems = new JSONArray(listCardItemList);
        String resultJson;
        if(displayType=="carousel"){
             resultJson =
                    "      {\n" +
                            "          \"header\": {\n" +
                            "            \"title\": \""+title+"\"\n" +
                            "          },\n" +
                            "          \"items\": "+listCardItems+",\n" +
                            "          \"buttons\": "+buttons+"\n" +
                            "      }";
        }else {
             resultJson =" {\n" +
                    "        \"listCard\": {\n" +
                    "          \"header\": {\n" +
                    "            \"title\": \""+title+"\"\n" +
                    "          },\n" +
                    "          \"items\": "+listCardItems+",\n" +
                    "          \"buttons\": "+buttons+"\n" +
                    "        }\n" +
                    "      }";
        }

        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }


    @Override
    public JSONObject createCarousel(String itemType, List items) throws ParseException {
        /**
         * 필드명	    타입	            필수 여부	설명	                제한
         * type	        string	            O	케로셀의 타입입니다.	basicCard 혹은 commerceCard, listCard, itemCard
         * items	Array<BasicCard>, Array<CommerceCard>, Array<ListCard>, Array<itemCard>	    O	케로셀 아이템입니다.	최대 10개 *ListCard는 최대 5개
         * header	CarouselHeader	        X	케로셀의 커버를 제공합니다.	*ListCard는 케로셀헤더를 지원하지 않습니다.
         */
        String resultJson ="{\n" +
                "    \"carousel\": {\n" +
                "        \"type\": \""+itemType+"\",\n" +
                "        \"items\":"+items+
                "    }\n" +
                "}";
        JSONObject resultJsonObj = (JSONObject) jsonParser.parse(resultJson);
        return resultJsonObj;
    }
}
