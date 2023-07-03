package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.dto.kakaoui.ButtonAction;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.DisplayType;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.service.ItemService;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.ArrayList;
import java.util.List;

import static site.pointman.chatbot.utill.ConstantBlockId.*;

@Service
public class ItemServiceImpl implements ItemService {

    KakaoJsonUiService kakaoJsonUiService;

    public ItemServiceImpl(KakaoJsonUiService kakaoJsonUiService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createNotice(RequestDto reqDto) throws Exception {

        KakaoResponseDto resDto = new KakaoResponseDto();
        ButtonDto quickButton = new ButtonDto();



        JSONObject basicCard = kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "상품등록",
                "상품을 등록하시겠습니까?",
                "https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg",
                null);
        resDto.addContent(basicCard);
        ButtonDto 상품등록하기 = quickButton.createQuickButton("상품등록하기", "649be953acaa9c34a7564b0b");
        resDto.addQuickButton(ButtonAction.block,"상품등록하기",BLK_ITEM_ADD_NAME);
        return resDto.createKakaoResponse();
    }


    @Override
    public JSONObject createItemName(RequestDto requestDto) throws Exception {
        String uttr = requestDto.getUtterance();
        KakaoResponseDto resDto = new KakaoResponseDto();

        JSONObject simpleText = kakaoJsonUiService.createSimpleText("상품명을 [" + uttr + "] 으/로 등록하시려면 다음으로 버튼을 눌러 주세요");
        resDto.addContent(simpleText);
        resDto.addQuickButton(ButtonAction.block,"다음으로",BLK_ITEM_ADD_PRICE);
        resDto.addQuickButton(ButtonAction.block,"처음으로",BLK_MAIN);


        return resDto.createKakaoResponse();
    }
}
