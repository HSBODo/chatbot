package site.pointman.chatbot.service.chatbot.serviceImpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.service.chatbot.CustomerChatBotResponseService;

@Service
public class CustomerChatBotResponseServiceImpl implements CustomerChatBotResponseService {

    @Override
    public ChatBotResponse joinSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원가입이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.메인으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getCustomerProfileSuccessChatBotResponse(Member member) {
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
    public ChatBotResponse updateCustomerPhoneNumberSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("연락처 변경이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse deleteCustomerSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원탈퇴가 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
