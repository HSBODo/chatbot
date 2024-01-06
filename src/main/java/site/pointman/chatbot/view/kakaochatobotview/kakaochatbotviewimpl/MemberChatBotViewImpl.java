package site.pointman.chatbot.view.kakaochatobotview.kakaochatbotviewimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.chatbot.constatnt.block.BlockId;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonAction;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonName;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonParamKey;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.chatbot.response.property.components.TextCard;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.view.kakaochatobotview.MemberChatBotView;

import java.util.Optional;

@Service
public class MemberChatBotViewImpl implements MemberChatBotView {

    MemberService memberService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public MemberChatBotViewImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public ChatBotResponse joinMemberResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원가입이 완료 되었습니다.\n프로필 사진을 등록하시려면 버튼을 눌러주세요.");
        chatBotResponse.addQuickButton(ButtonName.프로필사진등록.name(), ButtonAction.블럭이동,BlockId.CUSTOMER_UPDATE_PROFILE_IMAGE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse myProfilePage(MemberProfileDto memberProfileDto) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String description =
                "등급: "+memberProfileDto.getRole().getValue()+"\n\n"+
                "닉네임: "+memberProfileDto.getName()+"\n\n"+
                "연락처: "+memberProfileDto.getPhoneNumber()+"\n\n"+
                "가입일자: "+memberProfileDto.getJoinDate();

        TextCard textCard = new TextCard();
        textCard.setTitle("프로필");
        textCard.setDescription(description);

        chatBotResponse.addTextCard(textCard);
        chatBotResponse.addQuickButton(ButtonName.회원탈퇴.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_ASK_DELETE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.연락처변경.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_UPDATE_PHONE_NUMBER.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.이전으로.name(), ButtonAction.블럭이동, BlockId.MY_PAGE.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse myPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        StringBuilder description = new StringBuilder();
        description
                .append("회원님 반갑습니다.")
                .append("\n")
                .append("마이페이지입니다.")
        ;

        TextCard textCard = new TextCard();
        textCard.setTitle("마이페이지");
        textCard.setDescription(description.toString());

        chatBotResponse.addTextCard(textCard);
        chatBotResponse.addQuickButton(ButtonName.프로필.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PROFILE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.판매내역.name(), ButtonAction.블럭이동, BlockId.SALES_HISTORY_PAGE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.구매내역.name(), ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse mySalesCategoryListPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        StringBuilder description = new StringBuilder();
        description
                .append("판매 내역입니다.")
                .append("\n")
                .append("조회하고 싶은 상품버튼을 클릭하세요.")
        ;

        TextCard textCard = new TextCard();
        textCard.setTitle("판매 내역");
        textCard.setDescription(description.toString());

        chatBotResponse.addTextCard(textCard);
        chatBotResponse.addQuickButton(ButtonName.판매중.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCTS.getBlockId(), ButtonParamKey.productStatus, ProductStatus.판매중.name());
        chatBotResponse.addQuickButton(ButtonName.판매대기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_GET_CONTRACT.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.숨김.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCTS.getBlockId(), ButtonParamKey.productStatus, ProductStatus.숨김.name());
        chatBotResponse.addQuickButton(ButtonName.예약.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCTS.getBlockId(), ButtonParamKey.productStatus, ProductStatus.예약.name());
        chatBotResponse.addQuickButton(ButtonName.판매완료.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCTS.getBlockId(), ButtonParamKey.productStatus, ProductStatus.판매완료.name());
        chatBotResponse.addQuickButton(ButtonName.이전으로.name(), ButtonAction.블럭이동, BlockId.MY_PAGE.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateMemberPhoneNumberResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("연락처 변경이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse withdrawalMemberResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원탈퇴가 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateMemberProfileImageResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 프로필사진을 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
