package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.naverapi.SearchDto;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OpenApiService;
import site.pointman.chatbot.utill.Utillity;
import site.pointman.chatbot.dto.kakaoui.*;
import site.pointman.chatbot.dto.weatherapi.WeatherPropertyCodeDto;

import java.util.*;


@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;
    private MemberRepository memberRepository;
    private OpenApiService openApiService;
    private KakaoJsonUiService kakaoJsonUiService;
    private JSONParser jsonParser = new JSONParser();
    private Utillity utillity;

    public KakaoApiServiceImpl(ItemRepository itemRepository, OrderRepository orderRepository, MemberRepository memberRepository, OpenApiService openApiService, KakaoJsonUiService kakaoJsonUiService) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.openApiService = openApiService;
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createTodayNews(String searchText) throws Exception {

        List listCards = new ArrayList<>();
        List<ButtonDto> buttons = new ArrayList<>();
        List<ListCardItemDto> listCardItems = new ArrayList<>();
        SearchDto searchDto = openApiService.selectNaverSearch(searchText,"30","1","date");
        Optional.ofNullable(searchDto).orElseThrow(() -> new NullPointerException("뉴스정보가 없습니다."));
        searchDto.getItems().forEach(item -> {
            try {
                JSONObject searchJsonObject = new JSONObject((JSONObject) jsonParser.parse(item.toString()));
                Map webLink = new HashMap<>();
                String title =utillity.replaceAll((String) searchJsonObject.get("title"));
                String description =utillity.replaceAll((String) searchJsonObject.get("description"));
                webLink.put("web", searchJsonObject.get("link"));

                ListCardItemDto listCardItem = new ListCardItemDto(title,description,"https://www.pointman.shop/image/news.jpg",webLink);
                listCardItems.add(listCardItem);

                int cnt =  searchDto.getItems().indexOf(item)+1;
                if(cnt%5==0){  // <= 케로셀 최대 5개 까지만 표시
                    listCards.add(kakaoJsonUiService.createListCard(DisplayType.carousel, searchDto.getLastBuildDate()+" 오늘의 뉴스", listCardItems, buttons));
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
        Optional<KakaoMemberLocation> maybeMemberLocation = memberRepository.findByLocation(kakaoUserkey);
        if(maybeMemberLocation.isEmpty()) throw new NullPointerException("회원의 위치정보가 없습니다.");
        KakaoMemberLocation memberLocation = maybeMemberLocation.get();

        WeatherPropertyCodeDto weatherCode =  openApiService.selectShortTermWeather(memberLocation);
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
        ButtonDto locationNoticeButton = new ButtonDto(ButtonType.webLink,"위치정보 동의하기","https://www.pointman.shop/kakaochat/v1/location-notice?u="+kakaoUserkey);
        List<ButtonDto> buttons = new ArrayList<>();
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

        ButtonDto blogButton = new ButtonDto(ButtonType.webLink,"블로그","https://pointman.tistory.com/");
        ButtonDto gitHubButton = new ButtonDto(ButtonType.webLink,"GitHub","https://github.com/HSBODo");
        ButtonDto portfolioButton = new ButtonDto(ButtonType.webLink,"포트폴리오","https://www.pointman.shop");
        List buttons = new ArrayList<ButtonDto>();
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
        if(findItems.isEmpty()) throw new NullPointerException("현재 판매상품이 없습니다.");
        List commerceCards = new ArrayList<>();
        findItems.stream()
                .forEach(item ->{
                    List<ButtonDto> buttons = new ArrayList<>();
                    ButtonDto detailButton = new ButtonDto(ButtonType.webLink,"상세정보",item.getThumbnailLink());
                    //"https://www.pointman.shop/kakaochat/v1/kakaopay-ready?itemcode="+item.getItemCode()+"&kakaouserkey="+kakaoUserkey
                    ButtonParamsDto buttonParamsDto = new ButtonParamsDto("5", BlockServiceType.옵션);
                    buttonParamsDto.addButtonParam("itemCode", String.valueOf(item.getItemCode()));
                    ButtonDto buyButton = new ButtonDto(ButtonType.block,"구매하기", buttonParamsDto.createButtonParams());
                    buttons.add(detailButton);
                    buttons.add(buyButton);
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

                        ButtonParamsDto params = new ButtonParamsDto("12",BlockServiceType.주문상세정보);  //<== 다음 블럭
                        params.addButtonParam("itemCode", String.valueOf(item.getItemCode()));
                        params.addButtonParam("orderId", String.valueOf(order.getOrder_id()));

                        ButtonDto orderDetailButton = new ButtonDto(ButtonType.block,"결제 상세보기",params.createButtonParams());

                        List<ButtonDto> buttons = new ArrayList<>();
                        buttons.add(orderDetailButton);

                        if(order.getStatus().equals(OrderStatus.결제승인)){ //<== 결제승인 완료된 주문만 취소 가능
                            ButtonDto payCancelButton = new ButtonDto(ButtonType.webLink,"결제 취소","https://www.pointman.shop/kakaochat/v1/"+order.getOrder_id()+"/kakaopay-cancel");
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

        List<ButtonDto> buttons = new ArrayList<>();
        if(order.getStatus().equals(OrderStatus.결제승인)){ //<==결제 승인된 주문만 취소 가능
            ButtonDto payCancelButton = new ButtonDto(ButtonType.webLink,"결제 취소","https://www.pointman.shop/kakaochat/v1/"+order.getOrder_id()+"/kakaopay-cancel");
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
