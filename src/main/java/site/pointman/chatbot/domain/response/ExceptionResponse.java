package site.pointman.chatbot.domain.response;

import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.domain.response.property.Component;
import site.pointman.chatbot.domain.response.property.components.SimpleText;

public class ExceptionResponse extends ChatBotResponse {

    public ExceptionResponse notCustomerException(){
        SimpleText simpleText = new SimpleText();
        simpleText.setText("회원이 아닙니다.");
        Component component = new Component(simpleText);

        addQuickButton(ButtonName.처음으로, BlockId.MAIN.getBlockId());
        addQuickButton(ButtonName.회원가입, BlockId.CUSTOMER_JOIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
        return this;
    }

    public ExceptionResponse createException(String text){
        SimpleText simpleText = new SimpleText();
        simpleText.setText(text);
        Component component = new Component(simpleText);

        addQuickButton(ButtonName.처음으로, BlockId.MAIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
        return this;
    }

    public ExceptionResponse createException(){
        SimpleText simpleText = new SimpleText();
        simpleText.setText("시스템에 오류가 발생하였습니다.\n다시 시작해주세요.");
        Component component = new Component(simpleText);

        addQuickButton(ButtonName.처음으로, BlockId.MAIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
        return this;
    }

}
