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
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.vo.naverapi.SearchVo;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OpenApiService;
import site.pointman.chatbot.utill.Utillity;
import site.pointman.chatbot.vo.kakaoui.*;
import site.pointman.chatbot.vo.weatherapi.WeatherPropertyCodeVo;

import java.util.*;


@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    private ItemRepository itemRepository;
    private KakaoMemberRepository KakaoMemberRepository;
    private OrderRepository orderRepository;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private JSONParser jsonParser = new JSONParser();
    private Utillity utillity;

    public KakaoApiServiceImpl(ItemRepository itemRepository, KakaoMemberRepository kakaoMemberRepository, OrderRepository orderRepository, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService) {
        this.itemRepository = itemRepository;
        this.KakaoMemberRepository = kakaoMemberRepository;
        this.orderRepository = orderRepository;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createTodayNews(String searchText) throws Exception {

        List listCards = new ArrayList<>();
        List<ButtonVo> buttons = new ArrayList<>();
        List<ListCardItemVo> listCardItems = new ArrayList<>();
        SearchVo searchVo = openApiService.selectNaverSearch(searchText,"30","1","date");
        Optional.ofNullable(searchVo).orElseThrow(() -> new NullPointerException("뉴스정보가 없습니다."));
        searchVo.getItems().forEach(item -> {
            try {
                JSONObject searchJsonObject = new JSONObject((JSONObject) jsonParser.parse(item.toString()));
                Map webLink = new HashMap<>();
                String title =utillity.replaceAll((String) searchJsonObject.get("title"));
                String description =utillity.replaceAll((String) searchJsonObject.get("description"));
                webLink.put("web", searchJsonObject.get("link"));

                ListCardItemVo listCardItem = new ListCardItemVo(title,description,"https://www.pointman.shop/image/news.jpg",webLink);
                listCardItems.add(listCardItem);

                int cnt =  searchVo.getItems().indexOf(item)+1;
                if(cnt%5==0){  // <= 케로셀 최대 5개 까지만 표시
                    listCards.add(kakaoJsonUiService.createListCard(DisplayType.carousel, searchVo.getLastBuildDate()+" 오늘의 뉴스", listCardItems, buttons));
                    listCardItems.clear();
                }

            } catch (ParseException e) {
                throw new IllegalArgumentException("뉴스정보를 불러오는 데 실패하였습니다.");
            }
        });
        return kakaoJsonUiService.createCarousel(CarouselType.listCard,listCards);
    }
    @Override
    public JSONObject createTodayWeather(String kakaoUserkey) throws Exception {
        Optional<KakaoMemberLocation> maybeMemberLocation = KakaoMemberRepository.findByLocation(kakaoUserkey);
        if(maybeMemberLocation.isEmpty()) throw new NullPointerException("회원의 위치정보가 없습니다.");
        KakaoMemberLocation memberLocation = maybeMemberLocation.get();

        WeatherPropertyCodeVo weatherCode =  openApiService.selectShortTermWeather(memberLocation);
        List buttonList = new ArrayList<ButtonVo>();
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
        ButtonVo locationNoticeButton = new ButtonVo(ButtonType.webLink,"위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
        List<ButtonVo> buttons = new ArrayList<>();
        buttons.add(locationNoticeButton);
        return kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "위치정보의 수집ㆍ이용",
                " ◇ 위치정보수집ㆍ이용 목적 : 이동통신망사업자 및 이동통신재판매사업자로부터 총포 또는 총포소지자의 실시간 위치정보 확인을 통해 법령 위반행위 확인 및 사고 발생시 신속한 대처 등 총포 오남용 및 안전사고 예방에 활용\n" +
                        " ◇ 위치정보의 보유 및 이용기간 : 총포 보관해제시부터 총포 재보관 또는 당해 수렵기간 종료 후 1개월\n" +
                        " ◇ 동의 거부권리 안내 : 위 위치정보 수집에 대한 동의는 거부할 수 있습니다. \n",
                "https://www.pointman.shop/image/location_notice.jpg",
                buttons
        );
    }
    @Override
    public JSONObject createDeveloperInfo() throws ParseException {

        ButtonVo blogButton = new ButtonVo(ButtonType.webLink,"블로그","https://pointman.tistory.com/");
        ButtonVo gitHubButton = new ButtonVo(ButtonType.webLink,"GitHub","https://github.com/HSBODo");
        ButtonVo portfolioButton = new ButtonVo(ButtonType.webLink,"포트폴리오","https://www.pointman.shop");
        List buttons = new ArrayList<ButtonVo>();
        buttons.add(blogButton);
        buttons.add(gitHubButton);
        buttons.add(portfolioButton);
        return kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "Backend Developer",
                "안녕하세요.\n" +
                        "백엔드 개발자 한수빈입니다.\n" +
                        "Email: vinsulill@gmail.com",
                "https://www.pointman.shop/image/Ryan1.jpg",
                buttons
        );
    }
    @Override
    public JSONObject createRecommendItems(String kakaoUserkey) throws Exception {
        List<Item> findItems = itemRepository.findByDisplayItems();
        if(findItems.isEmpty()) throw new NullPointerException("현재 특가상품이 없습니다.");
        List commerceCards = new ArrayList<>();
        findItems.stream()
                .forEach(item ->{
                    List<ButtonVo> buttons = new ArrayList<>();
                    ButtonVo buyButton = new ButtonVo(ButtonType.webLink,"구매하러가기",item.getThumbnailLink());
                    ButtonVo payButton = new ButtonVo(ButtonType.webLink,"카카오페이 결제","https://www.pointman.shop/kakaochat/v1/kakaopay-ready?itemcode="+item.getItemCode()+"&kakaouserkey="+kakaoUserkey);
                    buttons.add(buyButton);
                    buttons.add(payButton);
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
                        commerceCards.add(commerceCard);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException("특가상품 정보를 불러오는 데 실패하였습니다.");
                    }

                });
        return kakaoJsonUiService.createCarousel(CarouselType.commerceCard,commerceCards);
    }
    @Override
    public JSONObject createOrderList(String kakaoUserkey) throws Exception {
        List basicCards = new ArrayList<>();
        List<Order> Orders = orderRepository.findByApproveOrders(kakaoUserkey);
        if(Orders.isEmpty()) throw new NullPointerException("주문하신 상품이 없습니다.");

        Orders.stream()
                .forEach(order ->{
                    try {
                        Optional<Item> maybeItem = itemRepository.findByItem(order.getItem_code());
                        if (maybeItem.isEmpty()) throw new NullPointerException("주문내역에 있는 상품을 찾을 수 없습니다.");
                        Item item = maybeItem.get();


                        Map buttonParams = new HashMap<String,String>();
                        buttonParams.put("itemCode",item.getItemCode());
                        buttonParams.put("orderId",order.getOrder_id());
                        buttonParams.put("kakaoUserkey",kakaoUserkey);
                        ButtonVo orderDetailButton = new ButtonVo(ButtonType.block,"결제 상세보기",buttonParams);

                        List<ButtonVo> buttons = new ArrayList<>();
                        buttons.add(orderDetailButton);

                        if(order.getStatus().equals(OrderStatus.결제승인)){ //<== 결제승인 완료된 주문만 취소 가능
                            ButtonVo payCancelButton = new ButtonVo(ButtonType.webLink,"결제 취소","https://www.pointman.shop/kakaochat/v1/"+order.getOrder_id()+"/kakaopay-cancel");
                            buttons.add(payCancelButton);
                        }

                        JSONObject basicCard = kakaoJsonUiService.createBasicCard(
                                DisplayType.carousel,
                                order.getItem_name(),
                                "결제 일자: "+utillity.formatApproveDate(String.valueOf(order.getCreateDate()))+"\n"+
                                        "결제 금액: "+utillity.formatMoney(order.getTotal_amount())+"원\n"
                                ,
                                item.getThumbnailImgUrl(),
                                buttons
                        );
                        basicCards.add(basicCard);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NullPointerException("주문하신 상품 조회에 실패했습니다.");
                    }
                });
        return kakaoJsonUiService.createCarousel(CarouselType.basicCard,basicCards);
    }
    @Override
    public JSONObject createOrderDetail(String kakaoUserkey, Long orderId) throws Exception {
        JSONObject basicCard;

        Optional<Order> maybeOrder = orderRepository.findByOrder(kakaoUserkey,orderId);
        if(maybeOrder.isEmpty()) throw new NullPointerException("주문번호와 일치하는 주문이 없습니다.");
        Order order = maybeOrder.get();

        Optional<Item> maybeItem = itemRepository.findByItem(order.getItem_code());
        if(maybeItem.isEmpty()) throw new NullPointerException("상품코드와 일치하는 상품이 없습니다.");
        Item item = maybeItem.get();

        List<ButtonVo> buttons = new ArrayList<>();
        if(order.getStatus().equals(OrderStatus.결제승인)){ //<==결제 승인된 주문만 취소 가능
            ButtonVo payCancelButton = new ButtonVo(ButtonType.webLink,"결제 취소","https://www.pointman.shop/kakaochat/v1/"+order.getOrder_id()+"/kakaopay-cancel");
            buttons.add(payCancelButton);
        }

        PayMethod payMethod = order.getPayment_method_type()==null?PayMethod.없음:order.getPayment_method_type();
        basicCard = kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                item.getProfileNickname(),
                "주문번호: " + order.getOrder_id() +"\n"+
                        "결제일자: " + utillity.formatApproveDate(String.valueOf(order.getCreateDate())) + "\n" +
                        "결제금액: " + utillity.formatMoney(order.getTotal_amount()) + "원\n" +
                        "결제수량: " + order.getQuantity() + "개\n" +
                        "결제수단: " + payMethod+"\n"+
                        "결제상태: " + order.getStatus()
                ,
                item.getThumbnailImgUrl(),
                buttons);
        return basicCard;
    }


}
