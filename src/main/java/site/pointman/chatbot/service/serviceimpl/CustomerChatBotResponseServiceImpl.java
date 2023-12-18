package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.service.CustomerChatBotResponseService;

@Service
public class CustomerChatBotResponseServiceImpl implements CustomerChatBotResponseService {

    @Override
    public ChatBotResponse joinSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원가입이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.메인으로, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getCustomerProfileSuccessChatBotResponse(String customerRank,String customerName, String customerPhoneNumber, String customerJoinDate) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addTextCard("회원정보",
                "등급: "+customerRank+"\n\n"+
                    "닉네임: "+customerName+"\n\n"+
                    "연락처: "+customerPhoneNumber+"\n\n"+
                    "가입일자: "+customerJoinDate);
        chatBotResponse.addQuickButton(ButtonName.회원탈퇴,BlockId.CUSTOMER_ASK_DELETE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.연락처변경,BlockId.CUSTOMER_UPDATE_PHONE_NUMBER.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.판매내역,BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateCustomerPhoneNumberSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("연락처 변경이 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse deleteCustomerSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원탈퇴가 완료 되었습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
