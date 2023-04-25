package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;
import site.pointman.chatbot.dto.kakaoui.*;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.dto.wearherapi.WeatherPropertyCodeDto;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OpenApiService;
import site.pointman.chatbot.utill.Utillity;

import java.util.*;


@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    private ItemRepository itemRepository;
    private KakaoMemberRepository KakaoMemberRepository;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private JSONParser jsonParser = new JSONParser();
    private Utillity utillity;

    public KakaoApiServiceImpl(ItemRepository itemRepository, KakaoMemberRepository kakaoMemberRepository, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService) {
        this.itemRepository = itemRepository;
        this.KakaoMemberRepository = kakaoMemberRepository;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createTodayNews(String searchText) throws Exception {

        List carouselItems = new ArrayList<>();
        List<ButtonDto> buttons = new ArrayList<>();
        List<ListCardItemDto> listCardItemDtos = new ArrayList<>();
        SearchDto searchDto = openApiService.selectNaverSearch(searchText,"30","1","date");
        searchDto.getItems().forEach(item -> {
            try {
                JSONObject jsonObject = new JSONObject((JSONObject) jsonParser.parse(item.toString()));
                Map webLink = new HashMap<>();
                String title =utillity.replaceAll((String) jsonObject.get("title"));
                String description =utillity.replaceAll((String) jsonObject.get("description"));
                webLink.put("web", jsonObject.get("link"));

                ListCardItemDto listCardItemDto = new ListCardItemDto(title,description,"https://www.pointman.shop/image/news.jpg",webLink);
                listCardItemDtos.add(listCardItemDto);

                int cnt =  searchDto.getItems().indexOf(item)+1;
                if(cnt%5==0){  // <= 케로셀 최대 5개 까지만 표시
                    carouselItems.add(kakaoJsonUiService.createListCard(DisplayType.carousel, searchDto.getLastBuildDate()+" 오늘의 뉴스", listCardItemDtos, buttons));
                    listCardItemDtos.clear();
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("뉴스정보를 불러오는 데 실패하였습니다.");
            }
        });
        return kakaoJsonUiService.createCarousel(CarouselType.listCard,carouselItems);
    }
    @Override
    public JSONObject createTodayWeather(String kakaoUserkey) throws Exception {
        Optional<KakaoMemberLocation> maybeMemberLocation = KakaoMemberRepository.findByLocation(kakaoUserkey);
        if(maybeMemberLocation.isEmpty()) throw new NullPointerException("회원의 위치정보가 없습니다.");

        WeatherPropertyCodeDto weatherCode =  openApiService.selectShortTermWeather(maybeMemberLocation.get());
        List buttonList = new ArrayList<ButtonDto>();
        return  kakaoJsonUiService.createBasicCard(DisplayType.basic,
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
        ButtonDto buttonDto = new ButtonDto("webLink","위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
        List buttonList = new ArrayList<ButtonDto>();
        buttonList.add(buttonDto);
        return kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
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
        List buttonList = new ArrayList<ButtonDto>();
        ButtonDto blog = new ButtonDto("webLink","블로그","https://pointman.tistory.com/");
        ButtonDto gitHub = new ButtonDto("webLink","GitHub","https://github.com/HSBODo");
        ButtonDto portfolio = new ButtonDto("webLink","포트폴리오","https://www.pointman.shop");
        buttonList.add(blog);
        buttonList.add(gitHub);
        buttonList.add(portfolio);
        return kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "Backend Developer",
                "안녕하세요.\n" +
                        "백엔드 개발자 한수빈입니다.\n" +
                        "Email: vinsulill@gmail.com",
                "https://www.pointman.shop/image/Ryan1.jpg",
                buttonList
        );
    }
    @Override
    public JSONObject createRecommendItems(String kakaoUserkey) throws Exception {
        List<Item> findItems = itemRepository.findByDisplayItems();
        if(findItems.isEmpty()) throw new NullPointerException("현재 특가상품이 없습니다.");
        List items = new ArrayList<>();
        findItems.stream()
                .forEach(item ->{
                    List<ButtonDto> buttons = new ArrayList<>();
                    ButtonDto buttonDto = new ButtonDto("webLink","구매하러가기",item.getThumbnailLink());
                    ButtonDto payButtonDto = new ButtonDto("webLink","카카오페이 결제","https://www.pointman.shop/kakaochat/v1/kakaopay-ready?itemcode="+item.getItemCode()+"&kakaouserkey="+kakaoUserkey);
                    buttons.add(buttonDto);
                    buttons.add(payButtonDto);
                    try {
                        JSONObject commerceCard = kakaoJsonUiService.createCommerceCard(
                                DisplayType.carousel,
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
        return kakaoJsonUiService.createCarousel(CarouselType.commerceCard,items);
    }
    @Override
    public JSONObject createOrderList(String kakaoUserkey) throws Exception {
        List orderItems = new ArrayList<>();
        List<Order> maybeOrders = itemRepository.findByApproveOrders(kakaoUserkey);
        if(maybeOrders.isEmpty()) throw new IllegalStateException("주문하신 상품이 없습니다.");
        maybeOrders.stream()
                .forEach(order ->{
                    try {
                        Optional<Item> maybeItem = itemRepository.findByItem(order.getItem_code());
                        if (maybeItem.isEmpty()) return;

                        List<ButtonDto> buttons = new ArrayList<>();
                        Map buttonParams = new HashMap<String,String>();
                        buttonParams.put("itemCode",maybeItem.get().getItemCode());
                        buttonParams.put("orderId",order.getOrder_id());
                        buttonParams.put("kakaoUserkey",kakaoUserkey);
                        ButtonBlockDto orderDetail = new ButtonBlockDto("block","결제 상세보기","",buttonParams);

                        if(order.getStatus().equals(OrderStatus.결제승인)){
                            ButtonDto payCancel = new ButtonDto("webLink","결제 취소","https://www.pointman.shop/kakaochat/v1/"+order.getOrder_id()+"/kakaopay-cancel");
                            buttons.add(payCancel);
                        }

                        buttons.add(orderDetail);

                        JSONObject basicCard = kakaoJsonUiService.createBasicCard(
                                DisplayType.carousel,
                                order.getItem_name(),
                                "결제 일자: "+utillity.formatApproveDate(String.valueOf(order.getCreateDate()))+"\n"+
                                        "결제 금액: "+utillity.formatMoney(order.getTotal_amount())+"원\n"
                                ,
                                maybeItem.get().getThumbnailImgUrl(),
                                buttons
                        );
                        orderItems.add(basicCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NullPointerException("주문하신 상품 조회에 실패했습니다.");
                    }
                });
        return kakaoJsonUiService.createCarousel(CarouselType.basicCard,orderItems);
    }
    @Override
    public JSONObject createOrderDetail(String kakaoUserkey, Long orderId) throws Exception {
        JSONObject basicCard;
        try {
            Optional<Order> maybeOrder = itemRepository.findByOrder(kakaoUserkey,orderId);
            if(maybeOrder.isEmpty()) throw new IllegalStateException("주문번호와 일치하는 주문이 없습니다.");
            Optional<Item> maybeItem = itemRepository.findByItem(maybeOrder.get().getItem_code());
            if(maybeItem.isEmpty()) throw new IllegalStateException("상품코드와 일치하는 상품이 없습니다.");

            List<ButtonDto> buttons = new ArrayList<>();
            if(maybeOrder.get().getStatus().equals(OrderStatus.결제승인)){
                ButtonDto payCancel = new ButtonDto("webLink","결제 취소","https://www.pointman.shop/kakaochat/v1/"+maybeOrder.get().getOrder_id()+"/kakaopay-cancel");
                buttons.add(payCancel);
            }
            PayMethod payMethod = maybeOrder.get().getPayment_method_type()==null?PayMethod.없음:maybeOrder.get().getPayment_method_type();
            basicCard = kakaoJsonUiService.createBasicCard(
                    DisplayType.basic,
                    maybeItem.get().getProfileNickname(),
                    "주문번호: " + maybeOrder.get().getOrder_id() +"\n"+
                            "결제일자: " + utillity.formatApproveDate(String.valueOf(maybeOrder.get().getCreateDate())) + "\n" +
                            "결제금액: " + utillity.formatMoney(maybeOrder.get().getTotal_amount()) + "원\n" +
                            "결제수량: " + maybeOrder.get().getQuantity() + "개\n" +
                            "결제수단: " + payMethod+"\n"+
                            "결제상태: " + maybeOrder.get().getStatus()
                    ,
                    maybeItem.get().getThumbnailImgUrl(),
                    buttons);

        }catch (Exception e){
            log.info(e.getStackTrace().toString());
            throw new IllegalStateException("주문 상세조회 실패하였습니다.");
        }
        return basicCard;
    }


}
