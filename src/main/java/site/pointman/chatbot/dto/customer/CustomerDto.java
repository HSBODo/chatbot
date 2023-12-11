package site.pointman.chatbot.dto.customer;

import lombok.Builder;
import site.pointman.chatbot.domain.customer.Customer;

public class CustomerDto {
    private String userKey;
    private String name;
    private String phone;
    private String address;

    @Builder
    public CustomerDto(String userKey, String name, String phone, String address) {
        this.userKey = userKey;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Customer toEntity(){
        return Customer.builder()
                .userKey(userKey)
                .name(name)
                .phone(phone)

                .build();
    }
}
