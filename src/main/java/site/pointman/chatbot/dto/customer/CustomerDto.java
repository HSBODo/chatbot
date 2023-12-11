package site.pointman.chatbot.dto.customer;

import lombok.Builder;
import site.pointman.chatbot.service.domain.customer.Customer;

public class CustomerDto {
    private String userKey;
    private String name;
    private String phone;

    @Builder
    public CustomerDto(String userKey, String name, String phone) {
        this.userKey = userKey;
        this.name = name;
        this.phone = phone;
    }

    public Customer toEntity(){
        return Customer.builder()
                .userKey(this.userKey)
                .name(this.name)
                .phone(this.phone)
                .build();
    }
}
