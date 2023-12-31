package site.pointman.chatbot.view.kakaochatobotview.kakaochatbotviewimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.view.kakaochatobotview.MemberChatBotView;

import java.util.Objects;

@Service
public class MemberChatBotViewImpl implements MemberChatBotView {

    MemberService memberService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public MemberChatBotViewImpl(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public ChatBotResponse joinChatBotResponse(String userKey, String name, String phoneNumber) {
        Response result = memberService.join(userKey, name, phoneNumber);

        if (result.getCode() != ResultCode.OK.getValue()) return chatBotExceptionResponse.createException("회원가입에 실패하였습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원가입이 완료 되었습니다.\n프로필 사진을 등록하시려면 버튼을 눌러주세요.");
        chatBotResponse.addQuickButton(ButtonName.프로필사진등록.name(),ButtonAction.블럭이동,BlockId.CUSTOMER_UPDATE_PROFILE_IMAGE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getCustomerProfileChatBotResponse(String userKey) {
        Member member = memberService.getMember(userKey);

        if (Objects.isNull(member)) return chatBotExceptionResponse.createException("회원조회를 실패하였습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String description =
                "등급: "+member.getRole().getValue()+"\n\n"+
                "닉네임: "+member.getName()+"\n\n"+
                "연락처: "+member.getPhoneNumber()+"\n\n"+
                "가입일자: "+member.getFormatCreateDate();

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
    public ChatBotResponse getMyPageSuccessChatBotResponse() {
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
    public ChatBotResponse getSalesCategorySuccessChatBotResponse() {
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
    public ChatBotResponse updateCustomerPhoneNumberBotResponse(String userKey, String updatePhoneNumber) {
        Response result = memberService.updateMemberPhoneNumber(userKey, updatePhoneNumber);
        if (result.getCode() != ResultCode.OK.getValue()) return chatBotExceptionResponse.createException("연락처 변경을 실패하였습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("연락처 변경이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse withdrawalCustomerChatBotResponse(String userKey) {
        Response result = memberService.deleteMember(userKey);

        if (result.getCode() != ResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원탈퇴가 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateCustomerProfileImageChatBotResponse(String userKey, String profileImageUrl) {
        Response result = memberService.updateMemberProfileImage(userKey, profileImageUrl);

        if (result.getCode() != ResultCode.OK.getValue()) return chatBotExceptionResponse.createException("프로필사진 등록을 실패하였습니다");

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 프로필사진을 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}