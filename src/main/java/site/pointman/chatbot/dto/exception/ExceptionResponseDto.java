package site.pointman.chatbot.dto.exception;

import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.Component;
import site.pointman.chatbot.dto.response.property.components.SimpleText;
import site.pointman.chatbot.utill.BlockId;

public class ExceptionResponseDto extends ResponseDto {

    public void addNotCustomerException(){
        SimpleText simpleText = new SimpleText();
        simpleText.setText("회원이 아닙니다.");
        Component component = new Component(simpleText);

        addQuickButton("처음으로", BlockId.MAIN.getBlockId());
        addQuickButton("회원가입", BlockId.CUSTOMER_JOIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
    }

    public void addException(String text){
        SimpleText simpleText = new SimpleText();
        simpleText.setText(text);
        Component component = new Component(simpleText);

        addQuickButton("처음으로", BlockId.MAIN.getBlockId());
        super.getTemplate().getOutputs().add(component);
    }
}
