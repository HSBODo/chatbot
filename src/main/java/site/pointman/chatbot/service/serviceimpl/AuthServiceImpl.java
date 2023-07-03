package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.dto.KakaoResponseDto;
import site.pointman.chatbot.dto.RequestDto;
import site.pointman.chatbot.dto.kakaoui.ButtonDto;
import site.pointman.chatbot.dto.kakaoui.ButtonAction;
import site.pointman.chatbot.dto.kakaoui.DisplayType;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.KakaoJsonUiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static site.pointman.chatbot.utill.ConstantBlockId.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    KakaoJsonUiService kakaoJsonUiService;
    MemberRepository memberRepository;

    public AuthServiceImpl(KakaoJsonUiService kakaoJsonUiService, MemberRepository memberRepository) {
        this.kakaoJsonUiService = kakaoJsonUiService;
        this.memberRepository = memberRepository;
    }

    @Override
    public JSONObject createAuthForm(RequestDto reqDto) throws Exception{

        KakaoResponseDto resDto = new KakaoResponseDto();
        ButtonDto quickButton = new ButtonDto();
        List<ButtonDto> buttons = new ArrayList<>();

        ButtonDto selectAddressButton = new ButtonDto(ButtonAction.webLink,"인증하기","https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg");
        buttons.add(selectAddressButton);
        JSONObject basicCard = kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "인증하기",
                "서비스를 사용하시려면 인증이 필요합니다.",
                "https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg",
                buttons);
        resDto.addContent(basicCard);

        resDto.addQuickButton(ButtonAction.block,"인증완료",BLK_AUTH_INFO);

        return resDto.createKakaoResponse();
    }

    @Override
    public JSONObject createAuthInfo(RequestDto reqDto) throws Exception {
        KakaoResponseDto resDto = new KakaoResponseDto();
        List<ButtonDto> buttons = new ArrayList<>();

        ButtonDto button =  new ButtonDto(ButtonAction.block,"인증철회",BLK_AUTH_CANCEL);
        buttons.add(button);

        MemberDto memberDto = MemberDto.builder()
                .name("홍길동")
                .phone("01000000000")
                .build();

        JSONObject basicCard = kakaoJsonUiService.createBasicCard(
                DisplayType.basic,
                "인증정보",
                "이름: " + memberDto.getName() + "\n" +
                        "연락처: " + memberDto.getPhone() + "\n",
                "https://newsimg.sedaily.com/2021/03/09/22JQPRY173_3.jpg",
                buttons);

        resDto.addContent(basicCard);
        resDto.addQuickButton(ButtonAction.block,"처음으로",BLK_MAIN);

        return resDto.createKakaoResponse();
    }

    @Override
    public JSONObject createAuthCancel(RequestDto reqDto) throws Exception {
        KakaoResponseDto resDto = new KakaoResponseDto();
        ButtonDto quickButton = new ButtonDto();

        JSONObject simpleText = kakaoJsonUiService.createSimpleText("인증철회가 왼료 되었습니다,");
        resDto.addContent(simpleText);
        resDto.addQuickButton(ButtonAction.block,"처음으로",BLK_MAIN);
        return resDto.createKakaoResponse();
    }

    @Override
    public boolean isAuthMember(String userKey) {
        try {
            Optional<Member> maybeMember = memberRepository.findByMember(userKey);
            if(maybeMember.isEmpty()){
                return false;
            }

        }catch (Exception e){
            log.info("error={}",e.getMessage());
            return true;
        }
        return true;
    }

    @Override
    public JSONObject createProfileSuccessMessage(String msg) throws Exception {
        KakaoResponseDto resDto = new KakaoResponseDto();
        ButtonDto quickButton = new ButtonDto();

        JSONObject simpleText = kakaoJsonUiService.createSimpleText(msg);
        resDto.addContent(simpleText);
        resDto.addQuickButton(ButtonAction.block,"다음으로","649e512b90088a2cf5c391e4");
        return resDto.createKakaoResponse();
    }

    @Override
    public JSONObject createFailMessage(String msg) throws Exception {
        KakaoResponseDto resDto = new KakaoResponseDto();

        JSONObject simpleText = kakaoJsonUiService.createSimpleText(msg);
        resDto.addContent(simpleText);
        return resDto.createKakaoResponse();
    }
}
