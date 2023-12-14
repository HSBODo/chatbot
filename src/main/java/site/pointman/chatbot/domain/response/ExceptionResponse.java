package site.pointman.chatbot.domain.response;

import site.pointman.chatbot.domain.response.property.Component;
import site.pointman.chatbot.domain.response.property.components.SimpleText;
import site.pointman.chatbot.constant.BlockId;

public class ExceptionResponse extends ChatBotResponse {

    public ExceptionResponse notCustomerException(){
        SimpleText simpleText = new SimpleText();
        simpleText.setText("회원이 아닙니다.");
        Component component = new Component(simpleText);

        addQuickButton("처음으로", BlockId.MAIN.getBlockId());
        addQuickButton("회원가입", BlockId.CUSTOMER_JOIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
        return this;
    }

    public ExceptionResponse createException(String text){
        SimpleText simpleText = new SimpleText();
        simpleText.setText(text);
        Component component = new Component(simpleText);

        addQuickButton("처음으로", BlockId.MAIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
        return this;
    }

}
