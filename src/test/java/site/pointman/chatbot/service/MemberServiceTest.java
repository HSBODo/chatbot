package site.pointman.chatbot.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.constant.member.MemberRole;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.domain.response.Response;

import java.util.Optional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    private String userKey;
    private String name;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        userKey = "test123";
        name = "테스트123";
        phoneNumber = "01012345678";

        memberService.join(userKey, name, phoneNumber);
    }

    @ParameterizedTest
    @CsvSource(value = {
         "userKey1:name1:phoneNumber1"
    }, delimiter = ':')
    void join(String userKey, String name, String phoneNumber) {
        //give

        //when
        Response response = memberService.join(userKey, name, phoneNumber);

        //then
        Assertions.assertThat(response.getCode()).isEqualTo(200);
    }

    @Test
    void getMembers() {
        //give

        //when
        Page<MemberProfileDto> members = memberService.getMemberProfiles(1);

        //then
        members.forEach(member -> {
            Assertions.assertThat(member.getName()).isNotNull();
        });

    }

    @Test
    void getMember() {
        //give

        //when
        Optional<MemberProfileDto> mayBeMember = memberService.getMemberProfileDto(name);


        //then
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);
    }

    @Test
    void updateMember() {
        //give
        MemberProfileDto updateMember = MemberProfileDto.builder()
                .name("변경")
                .phoneNumber("01000000000")
                .role(MemberRole.CUSTOMER_PLATINUM)
                .build();

        //when
        Response response = memberService.updateMember(userKey, updateMember);

        //then
        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    void updateMemberPhoneNumber() {
        //give
        String updatePhoneNumber ="01011112222";

        //when
        Response result = memberService.updateMemberPhoneNumber(userKey, updatePhoneNumber);
        //then
        Assertions.assertThat(result.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    void deleteMember() {
        //give

        //when
        Response response = memberService.deleteMember(userKey);
        //then
        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    void updateMemberProfileImage() {
        //give
        String updateProfileImage ="https://item.kakaocdn.net/do/a7fd7c0630f8aea8419a565fb2773bbc82f3bd8c9735553d03f6f982e10ebe70";
        //when
        Response response = memberService.updateMemberProfileImage(userKey,updateProfileImage);
        //then
        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    void isCustomer() {
        //give

        //when
        boolean customer = memberService.isCustomer(userKey);

        //then
        Assertions.assertThat(customer).isTrue();
    }

    @Test
    void isAdmin() {
        //give
        String adminKey= "QFJSyeIZbO77";
        String adminName= "라이언";
        //when
        boolean customer = memberService.isAdmin(adminName,adminKey);

        //then
        Assertions.assertThat(customer).isTrue();
    }

}