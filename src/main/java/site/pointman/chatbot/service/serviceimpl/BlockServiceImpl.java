package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.block.BlockType;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.service.BlockService;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.vo.KakaoResponseVo;
import site.pointman.chatbot.vo.kakaoui.ButtonVo;
import site.pointman.chatbot.vo.kakaoui.DisplayType;
import site.pointman.chatbot.vo.kakaoui.ListCardItemVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlockServiceImpl implements BlockService {
    KakaoJsonUiService kakaoJsonUiService;

    public BlockServiceImpl(KakaoJsonUiService kakaoJsonUiService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createBlock(String kakaoUserkey, BlockDto blockDto) throws Exception {
        KakaoResponseVo kakaoResponse = new KakaoResponseVo();
        Map<BlockType,JSONObject> kakaoJsonUiServiceMap= new HashMap<>();

        DisplayType displayType = blockDto.getDisplayType();
        BlockType blockType =blockDto.getBlockType();

        String title = "";
        String thumbnailImgUrl="";
        String thumbnailLink="";
        String profileImgUrl="";
        String profileNickname="";
        String description ="";
        int price = 0;
        int discount = 0;
        int discountedPrice = 0;
        int discountRate = 0;
        String currency = "won";
        List<ListCardItemVo> listCardItemList = new ArrayList<>();
        List<ButtonVo> buttonList = new ArrayList<>();
        List<ButtonVo> quickButtonList = new ArrayList<>();

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

        kakaoResponse.addContent(createBlock);

        quickButtonList.stream().forEach(buttonVo ->{
            kakaoResponse.addQuickButton(buttonVo);
        } );

        return kakaoResponse.createKakaoResponse();
    }
}
