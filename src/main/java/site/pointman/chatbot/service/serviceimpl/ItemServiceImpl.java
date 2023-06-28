package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.DisplayType;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.service.ItemService;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.ArrayList;
import java.util.List;
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
        ButtonDto 상품등록하기 = quickButton.createButtonBlock("상품등록하기", "649be953acaa9c34a7564b0b");
        resDto.addQuickButton(상품등록하기);
        return resDto.createKakaoResponse();
    }


    @Override
    public JSONObject createItemName(RequestDto requestDto) throws Exception {
        String uttr = requestDto.getUtterance();
        KakaoResponseDto resDto = new KakaoResponseDto();
        ButtonDto quickButton1 = new ButtonDto();
        ButtonDto quickButton2 = new ButtonDto();
        JSONObject simpleText = kakaoJsonUiService.createSimpleText("상품명을 " + uttr + "으/로 등록하시려면 다음으로 버튼을 눌러 주세요");
        resDto.addContent(simpleText);
        ButtonDto 다음으로 = quickButton1.createButtonBlock("다음으로", "649bece6200f9a46fce3df65");
        ButtonDto 처음으로 = quickButton2.createButtonBlock("처음으로", "64993967368ce63259b3faca");
        resDto.addQuickButton(다음으로);
        resDto.addQuickButton(처음으로);


        return resDto.createKakaoResponse();
    }
}
