package site.pointman.chatbot.dto.customer;

import lombok.Builder;
import site.pointman.chatbot.domain.customer.Member;

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

    public Member toEntity(){
        return Member.builder()
                .userKey(userKey)
                .name(name)
                .phone(phone)
                .build();
    }
}
