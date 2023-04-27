package site.pointman.chatbot.service.serviceimpl;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.CarouselType;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ListCardItemVo;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.List;
@Service
public class KakaoJsonUiServiceImpl implements KakaoJsonUiService {
    private JSONParser jsonParser = new JSONParser();
    @Override
    public JSONObject createBasicCard(DisplayType displayType, String title, String description, String thumbnailImgUrl, List<ButtonVo> buttonVoList) throws ParseException {
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
        JSONArray buttons = new JSONArray(buttonVoList);
        String resultJson;
        if(displayType.equals(DisplayType.carousel)){
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
    public JSONObject createCommerceCard(DisplayType displayType,String description, int price, int discount, int discountedPrice, int discountRate, String currency, String thumbnailImgUrl, String thumbnailLink, String profileImgUrl, String ProfileNickname, List<ButtonVo> buttonVoList) throws ParseException {
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
        String resultJson;
        JSONArray buttons = new JSONArray(buttonVoList);
        if(displayType.equals(DisplayType.carousel)){
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
    public JSONObject createListCard(DisplayType displayType, String title, List<ListCardItemVo> listCardItemListDto, List<ButtonVo> buttonVoList) throws ParseException {
        String resultJson;
        JSONArray buttons = new JSONArray(buttonVoList);
        JSONArray listCardItems = new JSONArray(listCardItemListDto);
        if(displayType.equals(DisplayType.carousel)){
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
    public JSONObject createCarousel(CarouselType itemType, List items) throws ParseException {
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
}
