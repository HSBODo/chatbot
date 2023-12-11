package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.domain.customer.Customer;
import site.pointman.chatbot.dto.customer.CustomerDto;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.repository.CustomerRepository;
import site.pointman.chatbot.service.CustomerService;

import java.util.Optional;
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {


    CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public ResponseDto join(RequestDto requestDto) {
        ResponseDto responseDto = new ResponseDto();
        boolean isCustomer = isCustomer(requestDto);
        String userKey = requestDto.getUserKey();

        if(isCustomer){
            responseDto.addException("이미 존재하는 회원입니다.");
            return responseDto;
        }

        String name = requestDto.getEnterName();
        String phone = requestDto.getEnterPhone();

        CustomerDto customerDto = CustomerDto.builder()
                .userKey(userKey)
                .name(name)
                .phone(phone)
                .build();

        Customer customer = customerDto.toEntity();

        customerRepository.insertCustomer(customer);
        responseDto.addSimpleText("회원가입이 완료 되었습니다.");
        return responseDto;
    }

    @Override
    public boolean isCustomer(RequestDto requestDto) {
        String userKey = requestDto.getUserKey();
        Optional<Customer> mayBeCustomer = customerRepository.findByCustomer(userKey,"Y");

        if(mayBeCustomer.isEmpty()){
            return false;
        }
        return true;
    }
}
