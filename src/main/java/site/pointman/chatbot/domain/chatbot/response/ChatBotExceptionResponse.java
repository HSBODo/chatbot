package site.pointman.chatbot.domain.chatbot.response;

import site.pointman.chatbot.domain.chatbot.constatnt.block.BlockId;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonAction;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonName;

public class ChatBotExceptionResponse {


    public ChatBotResponse notCustomerException(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("회원이 아닙니다.");
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.회원가입.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_JOIN.getBlockId());

        return chatBotResponse;
    }

    public ChatBotResponse createException(String text){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText(text);
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    public ChatBotResponse createException(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("시스템에 오류가 발생하였습니다.\n처음부터 다시 시작해주세요.");
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

}
