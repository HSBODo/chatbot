package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.dto.customer.CustomerDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.repository.CustomerRepository;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.utill.NumberUtils;

import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {


    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public ChatBotResponse join(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String userKey = chatBotRequest.getUserKey();

        if (isCustomer(chatBotRequest)) {
            ExceptionResponse exceptionResponse = new ExceptionResponse();

            return exceptionResponse.createException("이미 존재하는 회원입니다.");
        }

        String joinName = chatBotRequest.getCustomerName();
        String joinPhone = chatBotRequest.getCustomerPhone();

        CustomerDto customerDto = CustomerDto.builder()
                .userKey(userKey)
                .name(joinName)
                .phone(joinPhone)
                .build();

        Customer customer = customerDto.toEntity();

        customerRepository.insert(customer);
        chatBotResponse.addSimpleText("회원가입이 완료 되었습니다.");
        return chatBotResponse;
    }

    @Override
    public boolean isCustomer(ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();
            Optional<Customer> mayBeCustomer = customerRepository.findByCustomer(userKey);

            if (mayBeCustomer.isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public ValidationResponse validationFormatPhoneNumber(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String inputPhone = chatBotRequest.getValidationData();
        inputPhone = inputPhone.replaceAll("-", "");

        if(!NumberUtils.isNumber(inputPhone)){
            validationResponse.validationFail();
            return validationResponse;
        }

        if (inputPhone.length() != 11) {
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(inputPhone);
        return validationResponse;
    }

    @Override
    public ChatBotResponse getCustomerInfo(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String userKey = chatBotRequest.getUserKey();

        if (!isCustomer(chatBotRequest)) {
            chatBotResponse.addSimpleText("회원이 아닙니다.");
            chatBotResponse.addQuickButton("처음으로", BlockId.MAIN.getBlockId());
            chatBotResponse.addQuickButton("회원가입", BlockId.CUSTOMER_JOIN.getBlockId());
            return chatBotResponse;
        }

        Optional<Customer> mayBeCustomer = customerRepository.findByCustomer(userKey);
        Customer customer = mayBeCustomer.get();

        chatBotResponse.addTextCard("회원정보",
                "이름: "+customer.getName()+"\n"+
                "연락처: "+customer.getPhone()+"\n"+
                "가입일자: "+customer.getCreateDate());
        chatBotResponse.addQuickButton("탈퇴하기",BlockId.CUSTOMER_ASK_DELETE.getBlockId());
        chatBotResponse.addQuickButton("연락처변경",BlockId.CUSTOMER_UPDATE_PHONE_NUMBER.getBlockId());
        chatBotResponse.addQuickButton("등록상품조회",BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateCustomerPhoneNumber(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String userKey = chatBotRequest.getUserKey();
        String updatePhoneNumber = chatBotRequest.getCustomerPhone();

        customerRepository.updateCustomerPhoneNumber(userKey, updatePhoneNumber);

        chatBotResponse.addSimpleText("연락처 변경이 완료 되었습니다.");
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse deleteCustomer(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String userKey = chatBotRequest.getUserKey();

        customerRepository.delete(userKey);

        chatBotResponse.addSimpleText("회원탈퇴가 완료 되었습니다.");
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
