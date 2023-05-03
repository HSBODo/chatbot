package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.domain.item.ItemOptionCategory;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.repository.BlockRepository;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.service.BlockService;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.vo.KakaoResponseVo;
import site.pointman.chatbot.vo.kakaoui.*;

import java.util.*;

@Service
@Slf4j
public class BlockServiceImpl implements BlockService {
    KakaoJsonUiService kakaoJsonUiService;
    ItemRepository itemRepository;
    BlockRepository blockRepository;
    KakaoApiService kakaoApiService;

    public BlockServiceImpl(KakaoJsonUiService kakaoJsonUiService, ItemRepository itemRepository, BlockRepository blockRepository, KakaoApiService kakaoApiService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.itemRepository = itemRepository;
        this.blockRepository = blockRepository;
        this.kakaoApiService = kakaoApiService;
    }

    @Override
    public JSONObject createBlock(BlockVo blockVo) throws Exception {
        KakaoResponseVo kakaoResponse = new KakaoResponseVo();
        Map<BlockType,JSONObject> kakaoJsonUiServiceMap= new HashMap<>();

        DisplayType displayType = blockVo.getDisplayType();
        BlockType blockType =blockVo.getBlockType();

        String title = blockVo.getTitle();
        String thumbnailImgUrl=blockVo.getThumbnailImgUrl();
        String thumbnailLink=blockVo.getThumbnailLink();
        String profileImgUrl=blockVo.getProfileImgUrl();
        String profileNickname=blockVo.getProfileNickname();
        String description =blockVo.getDescription();
        int price = blockVo.getPrice();
        int discount = blockVo.getDiscount();
        int discountedPrice = blockVo.getDiscountedPrice();
        int discountRate = blockVo.getDiscountRate();
        String currency = blockVo.getCurrency();
        List<ListCardItemVo> listCardItemList = blockVo.getListCardItemList();
        List<ButtonVo> buttonList = blockVo.getButtonList();
        List<ButtonVo> quickButtonList = blockVo.getQuickButtonList();

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
        KakaoResponseVo kakaoResponse = new KakaoResponseVo();
        BlockServiceType service = blockDto.getService();
        Optional<Block> maybeBlock = blockRepository.findByBlock(service);
        if (maybeBlock.isEmpty()) throw new NullPointerException("블럭이 존재하지 않습니다.");
        Block block = maybeBlock.get();
        BlockDto findBlockDto = block.toBlockDto();
        BlockVo blockVo;
        switch (findBlockDto.getService()){
            case 회원가입:
                List<ButtonVo> buttons = new ArrayList<>();
                ButtonVo joinButton = new ButtonVo(ButtonType.webLink,"회원가입하기","https://www.pointman.shop/admin-page/join?u="+kakaoUserkey);
                buttons.add(joinButton);

                blockVo = BlockVo.builder()
                        .blockType(findBlockDto.getBlockType())
                        .displayType(findBlockDto.getDisplayType())
                        .title("회원가입")
                        .description("고객님, 회원이 아니시군요... 회원가입이 필요합니다.")
                        .thumbnailImgUrl("https://www.pointman.shop/image/Ryan1.jpg")
                        .buttonList(buttons)
                        .build();
                JSONObject joinNotice = createBlock(blockVo);
                kakaoResponse.addContent(joinNotice);
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
                List<ItemOption> maybeItemOptions = itemRepository.findByItemOptions(itemCode, ItemOptionCategory.색상);
                if(maybeItemOptions.isEmpty()) new NullPointerException("옵션이 존재하지 않습니다");

                maybeItemOptions.stream().forEach(itemOption -> {
                    Map<String,String> buttonParam = new HashMap<>();
                    buttonParam.put(String.valueOf(itemOption.getCategory()),itemOption.getOptionName());
                    ButtonVo quickButton = new ButtonVo(ButtonType.block,itemOption.getOptionName(),buttonParam);
                    kakaoResponse.addQuickButton(quickButton);
                });
                BlockVo option = BlockVo.builder()
                    .blockType(BlockType.simpleText)
                    .displayType(DisplayType.basic)
                    .description(ItemOptionCategory.색상+"을 선택하세요.")
                    .build();

                JSONObject optionList = createBlock(option);
                kakaoResponse.addContent(optionList);
                return kakaoResponse.createKakaoResponse();
            default:

        }




//        if(blockDto.getService().equals(BlockServiceType.옵션) && !Optional.ofNullable(buttonParams.get("itemCode")).isEmpty()){
//            long itemCode = Long.parseLong((String) buttonParams.get("itemCode"));
//            List<ItemOption> maybeItemOptions = itemRepository.findByItemOptions(itemCode, ItemOptionCategory.색상);
//            if(maybeItemOptions.isEmpty()) new NullPointerException("옵션이 존재하지 않습니다");
//            maybeItemOptions.stream().forEach(itemOption -> {
////                Map<String,String> buttonParam = new HashMap<>();
////                buttonParam.put(String.valueOf(itemOption.getCategory()),itemOption.getOptionName());
////                ButtonVo quickButton = new ButtonVo(ButtonType.block,itemOption.getOptionName(),buttonParam);
////                kakaoResponse.addQuickButton(quickButton);
////            });
////            BlockVo blockVo = BlockVo.builder()
////                    .blockType(BlockType.simpleText)
////                    .displayType(DisplayType.basic)
////                    .description(ItemOptionCategory.색상+"을 선택하세요.")
////                    .build();
////
////            JSONObject block = createBlock(blockVo);
//            kakaoResponse.addContent(block);
//            return kakaoResponse.createKakaoResponse();
//        }else{
//
//        }
        return null;
    }
}
