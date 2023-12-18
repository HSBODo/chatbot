package site.pointman.chatbot.domain.response;

import lombok.Getter;

@Getter
public class ChatBotValidationResponse {
    private String status;
    private String value;
    private String message;

    public void validationFail(){
        status = "FAIL";
    }

    public void validationSuccess(String validationPram){
        status = "SUCCESS";
        value = validationPram;
    }

    public void validationError(){
        status = "ERROR";
    }

}
