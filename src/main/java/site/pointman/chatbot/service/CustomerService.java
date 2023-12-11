package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.domain.response.ValidationResponse;

public interface CustomerService {
    ResponseDto join(ChatBotRequest chatBotRequest);
    boolean isCustomer(ChatBotRequest chatBotRequest);
    ValidationResponse validationFormatPhoneNumber(ChatBotRequest chatBotRequest);

    ResponseDto getCustomerInfo(ChatBotRequest chatBotRequest);

    ResponseDto updateCustomerPhoneNumber(ChatBotRequest chatBotRequest);

    ResponseDto deleteCustomer(ChatBotRequest chatBotRequest);

}
