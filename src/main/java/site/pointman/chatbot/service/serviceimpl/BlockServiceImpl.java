package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.domain.item.ItemOptionCategory;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.MemberAttribute;
import site.pointman.chatbot.dto.address.AddressDto;
import site.pointman.chatbot.dto.block.BlockDto;
import site.pointman.chatbot.dto.item.ItemDto;
import site.pointman.chatbot.dto.item.ItemOptionDto;
import site.pointman.chatbot.dto.member.MemberAttributeDto;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.repository.*;
import site.pointman.chatbot.service.BlockService;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.utill.Utillity;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.kakaoui.*;

import java.util.*;

@Service
@Slf4j
public class BlockServiceImpl implements BlockService {
    KakaoJsonUiService kakaoJsonUiService;
    ItemRepository itemRepository;
    BlockRepository blockRepository;
    KakaoApiService kakaoApiService;
    MemberRepository memberRepository;
    AddressRepository addressRepository;
    OrderService orderService;
    Utillity utillity;

    @Value("${host.url}")
    private String hostUrl;

    public BlockServiceImpl(KakaoJsonUiService kakaoJsonUiService, ItemRepository itemRepository, BlockRepository blockRepository, KakaoApiService kakaoApiService, MemberRepository memberRepository, AddressRepository addressRepository, OrderService orderService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.itemRepository = itemRepository;
        this.blockRepository = blockRepository;
        this.kakaoApiService = kakaoApiService;
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
        this.orderService = orderService;
    }

    @Override
    public JSONObject createBlock(BlockDto blockDto) throws Exception {
        Map<BlockType,JSONObject> kakaoJsonUiServiceMap= new HashMap<>();

        DisplayType displayType = blockDto.getDisplayType();
        BlockType blockType = blockDto.getBlockType();

        String title = blockDto.getTitle();
        String thumbnailImgUrl= blockDto.getThumbnailImgUrl();
        String thumbnailLink= blockDto.getThumbnailLink();
        String profileImgUrl= blockDto.getProfileImgUrl();
        String profileNickname= blockDto.getProfileNickname();
        String description = blockDto.getDescription();
        int price = blockDto.getPrice();
        int discount = blockDto.getDiscount();
        int discountedPrice = blockDto.getDiscountedPrice();
        int discountRate = blockDto.getDiscountRate();
        String currency = blockDto.getCurrency();
        List<ListCardItemDto> listCardItemList = blockDto.getListCardItemList();
        List<ButtonDto> buttonList = blockDto.getButtonList();

        kakaoJsonUiServiceMap.put(BlockType.basicCard,kakaoJsonUiService.createBasicCard(displayType,title,description,thumbnailImgUrl,buttonList));
        kakaoJsonUiServiceMap.put(BlockType.simpleText,kakaoJsonUiService.createSimpleText(description));
        kakaoJsonUiServiceMap.put(BlockType.commerceCard,kakaoJsonUiService.createCommerceCard(displayType,
                description,
                price,
                discount,
                discountedPrice,
                discountRate,
                currency,
                thumbnailImgUrl,
                thumbnailLink,
                profileImgUrl,
                profileNickname,
                buttonList));
        kakaoJsonUiServiceMap.put(BlockType.listCard,kakaoJsonUiService.createListCard(displayType,title,listCardItemList,buttonList));
        kakaoJsonUiServiceMap.put(BlockType.simpleImage,kakaoJsonUiService.createSimpleImage("123",thumbnailImgUrl));

        JSONObject createBlock = kakaoJsonUiServiceMap.get(blockType);
        return createBlock;
    }

    @Override
    public JSONObject findByService(String kakaoUserkey, BlockDto blockDto, JSONObject buttonParams) throws Exception {
        BlockServiceType service = blockDto.getService();

        Optional<Block> maybeBlock = blockRepository.findByBlock(service);
        if (maybeBlock.isEmpty()) throw new NullPointerException("블럭이 존재하지 않습니다.");
        BlockDto findBlockDto  = maybeBlock.get().toBlockDto();

        KakaoResponseDto kakaoResponse = new KakaoResponseDto();
        List<ButtonDto> buttons = new ArrayList<>();
        switch (findBlockDto.getService()){
            case 회원가입:
                ButtonDto joinButton = new ButtonDto(ButtonType.webLink,"회원가입하기",hostUrl+"/kakaochat/v1/kakaoJoinForm?u="+kakaoUserkey);
                buttons.add(joinButton);
                BlockDto 회원가입 = findBlockDto.basicCard(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        "회원가입",
                        "고객님, 회원이 아니시군요... 회원가입이 필요합니다.",
                        "https://www.pointman.shop/image/Ryan1.jpg",
                        buttons
                );

                JSONObject joinNotice = createBlock(회원가입);
                kakaoResponse.addContent(joinNotice);
                return kakaoResponse.createKakaoResponse() ;

            case 회원정보조회:
                Optional<Member> maybeMember = memberRepository.findByMember(kakaoUserkey);
                if(maybeMember.isEmpty()) throw new NullPointerException("회원이 아닙니다.");
                MemberDto memberDto = maybeMember.get().toMemberDto();

                ButtonDto withdrawalButton = new ButtonDto(ButtonType.webLink,"회원탈퇴",hostUrl+"/kakaochat/v1/kakaoMemeberDeleteForm?u="+kakaoUserkey);
                buttons.add(withdrawalButton);
                BlockDto 회원정보 = findBlockDto.basicCard(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        "나의 회원정보",
                        "이름: "+memberDto.getName()+"\n"+
                                "연락처: "+memberDto.getPhone()+"\n"+
                                "이메일: "+memberDto.getEmail()+"\n"
                        ,
                        "",
                        buttons
                );
                JSONObject memberInfo = createBlock(회원정보);
                kakaoResponse.addContent(memberInfo);
                return kakaoResponse.createKakaoResponse() ;
            case 상품조회:
                JSONObject recommendItems = kakaoApiService.createRecommendItems(kakaoUserkey);
                kakaoResponse.addContent(recommendItems);
                return kakaoResponse.createKakaoResponse();

            case 주문조회:
                JSONObject orderList = kakaoApiService.createOrderList(kakaoUserkey);
                kakaoResponse.addContent(orderList);
                return kakaoResponse.createKakaoResponse();

            case 옵션:
                long itemCode = Long.parseLong((String) buttonParams.get("itemCode"));
                List<ItemOption> maybeItemOptions = itemRepository.findByItemOptions(itemCode, ItemOptionCategory.사이즈);
                if(CollectionUtils.isEmpty(maybeItemOptions)) throw  new NullPointerException("옵션이 존재하지 않습니다");
                BlockDto 옵션 = findBlockDto.simpleText(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        ItemOptionCategory.사이즈 + "을 선택하세요."
                );
                JSONObject optionList = createBlock(옵션);
                kakaoResponse.addContent(optionList);

                maybeItemOptions.stream().forEach(itemOption -> {
                    ButtonParamsDto params = new ButtonParamsDto("10",BlockServiceType.수량선택);  //<== 다음 블럭
                    params.addButtonParam("itemCode", String.valueOf(itemOption.getItemCode()));
                    params.addButtonParam("optionId", String.valueOf(itemOption.getId()));
                    ButtonDto quickButton = new ButtonDto(ButtonType.block,itemOption.getOptionName(),params.createButtonParams());
                    kakaoResponse.addQuickButton(quickButton);
                });

                return kakaoResponse.createKakaoResponse();

            case 수량선택:

                BlockDto 수량선택 = findBlockDto.simpleText(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        "수량을 선택하세요."
                );
                JSONObject quantityList = createBlock(수량선택);
                kakaoResponse.addContent(quantityList);

                for(int q=1; q<=10;q++){
                    ButtonParamsDto params = new ButtonParamsDto("9",BlockServiceType.배송지입력); //<== 다음 블럭
                    params.addButtonParam("quantity", String.valueOf(q));
                    ButtonDto quickButton = new ButtonDto(ButtonType.block,q+"개",params.createButtonParams());
                    kakaoResponse.addQuickButton(quickButton);
                }

                return kakaoResponse.createKakaoResponse();

            case 배송지입력:
                ButtonDto addressButton = new ButtonDto(ButtonType.webLink,"배송지입력하기",hostUrl+"/kakaochat/v1/address?u="+kakaoUserkey);
                buttons.add(addressButton);

                BlockDto 배송지입력 = findBlockDto.basicCard(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        "배송지등록",
                        "배송지를 입력해 주세요.",
                        "https://www.pointman.shop/image/Ryan1.jpg",
                        buttons
                );
                JSONObject addAddress = createBlock(배송지입력);
                kakaoResponse.addContent(addAddress);
                return kakaoResponse.createKakaoResponse();

            case 주문서:
                Optional<MemberAttribute> maybeMemberAttribute = memberRepository.findByAttribute(kakaoUserkey);
                if(maybeMemberAttribute.isEmpty()) throw new NullPointerException("주문서를 불러오기에 실패하였습니다.");
                MemberAttributeDto memberAttributeDto = maybeMemberAttribute.get().toMemberAttributeDto();

                Optional<ItemOption> maybeItemOption = itemRepository.findByItemOption(memberAttributeDto.getOptionCode());
                if(maybeItemOption.isEmpty()) throw new NullPointerException("상품 옵션이 존재하지 않습니다.");
                ItemOptionDto itemOptionDto = maybeItemOption.get().toItemOptionDto();

                Optional<Item> maybeItem = itemRepository.findByItem(itemOptionDto.getItemCode());
                if(maybeItem.isEmpty()) throw new NullPointerException("상품이 존재하지 않습니다.");
                ItemDto itemDto = maybeItem.get().toItemDto();

                Optional<Address> maybeAddress = addressRepository.findByAddress(kakaoUserkey);
                if(maybeAddress.isEmpty()) throw new NullPointerException("배송지를 등록해 주세요.");
                AddressDto addressDto = maybeAddress.get().toAddressDto();
                String address = addressDto.getPostCode()+" "+addressDto.getRoadAddress()+" "+addressDto.getDetailAddress();

                int totalPrice = orderService.calculateTotalPrice(itemDto.getItemCode(), itemOptionDto.getId(), memberAttributeDto.getQuantity());

                ButtonDto buyButton = new ButtonDto(ButtonType.webLink,"결제하기",hostUrl+"/kakaochat/v1/kakaopay-ready?" +
                        "itemcode="+itemDto.getItemCode()+
                        "&kakaouserkey="+kakaoUserkey+
                        "&optionId="+itemOptionDto.getId()+
                        "&totalPrice="+totalPrice+
                        "&quantity="+memberAttributeDto.getQuantity()
                );
                buttons.add(buyButton);

                BlockDto 주문서 = findBlockDto.basicCard(
                        findBlockDto.getBlockType(),
                        findBlockDto.getDisplayType(),
                        itemDto.getProfileNickname(),
                        "아래의 내용이맞는지 꼼꼼히 확인해주시기 바랍니다.\n\n"+
                                "총 결제금액은 "+utillity.formatMoney(totalPrice)+"원 입니다. \n\n"+
                                "상품명:"+itemDto.getProfileNickname()+"\n"+
                                "사이즈:"+itemOptionDto.getOptionName()+"\n"+
                                "수량:"+memberAttributeDto.getQuantity()+"개\n"+
                                "받으시는 분:"+addressDto.getName()+"\n"+
                                "연락처:"+addressDto.getPhone()+"\n"+
                                "배송지:"+address+"\n",
                        itemDto.getThumbnailImgUrl(),
                        buttons
                );
                JSONObject order = createBlock(주문서);
                kakaoResponse.addContent(order);
                return kakaoResponse.createKakaoResponse();

            case 주문상세정보:
                Long orderId =Long.parseLong((String)buttonParams.get("orderId"));
                JSONObject orderDetail = kakaoApiService.createOrderDetail(kakaoUserkey, orderId);
                kakaoResponse.addContent(orderDetail);
                return kakaoResponse.createKakaoResponse();
            default:

        }
        return null;
    }
}
