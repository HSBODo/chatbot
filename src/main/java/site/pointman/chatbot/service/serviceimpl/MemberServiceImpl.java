package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.HttpResponse;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.service.CustomerChatBotResponseService;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.utill.StringUtils;

import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;
    ChatBotExceptionResponse chatBotExceptionResponse;
    CustomerChatBotResponseService customerChatBotResponseService;

    public MemberServiceImpl(MemberRepository memberRepository, CustomerChatBotResponseService customerChatBotResponseService) {
        this.memberRepository = memberRepository;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
        this.customerChatBotResponseService = customerChatBotResponseService;
    }

    @Override
    public Response join(String userKey, String name, String phoneNumber, boolean isChatBotRequest) {
        try {
            MemberDto memberDto = MemberDto.builder()
                    .userKey(userKey)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .build();

            Member member = memberDto.toEntity();

            memberRepository.save(member);

            if(isChatBotRequest) return customerChatBotResponseService.joinSuccessChatBotResponse();

            return new HttpResponse(ApiResultCode.OK,"회원가입을 성공적으로 완료하였습니다.");
        }catch (Exception e) {
            if (isChatBotRequest) return chatBotExceptionResponse.createException();
            return new HttpResponse(ApiResultCode.FAIL,"회원가입을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public Response getCustomerProfile(String userKey, boolean isChatBotRequest) {
        try {
            Optional<Member> mayBeCustomer = memberRepository.findByCustomer(userKey);

            Member member = mayBeCustomer.get();

            String customerName = member.getName();
            String customerPhoneNumber = member.getPhoneNumber();
            String customerJoinDate = StringUtils.dateFormat(member.getCreateDate(), "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");

            if(isChatBotRequest) return customerChatBotResponseService.getCustomerProfileSuccessChatBotResponse(customerName,customerPhoneNumber,customerJoinDate);

            return member;
        }catch (Exception e) {
            if (isChatBotRequest) return chatBotExceptionResponse.createException();
            return new HttpResponse(ApiResultCode.FAIL,"회원 프로필 조회를 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public Response updateCustomerPhoneNumber(String userKey, String updatePhoneNumber, boolean isChatBotRequest) {
        try {
            memberRepository.updateCustomerPhoneNumber(userKey, updatePhoneNumber);

            if(isChatBotRequest) return customerChatBotResponseService.updateCustomerPhoneNumberSuccessChatBotResponse();

            return new HttpResponse(ApiResultCode.OK,"연락처를 수정하였습니다.");
        }catch (Exception e) {
            if (isChatBotRequest) return chatBotExceptionResponse.createException();
            return new HttpResponse(ApiResultCode.FAIL,"연락처 수정을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public Response withdrawalCustomer(String userKey, boolean isChatBotRequest) {
        try {
            memberRepository.delete(userKey);

            if(isChatBotRequest) return customerChatBotResponseService.deleteCustomerSuccessChatBotResponse();

            return new HttpResponse(ApiResultCode.OK,"회원탈퇴를 성공적으로 완료하였습니다.");
        }catch (Exception e) {
            if (isChatBotRequest) return chatBotExceptionResponse.createException();
            return new HttpResponse(ApiResultCode.FAIL,"회원탈퇴를 실패하였습니다. e= "+e.getMessage());
        }
    }


    @Override
    public boolean isCustomer(String userKey) {
        try {
            Optional<Member> mayBeCustomer = memberRepository.findByCustomer(userKey);

            if (mayBeCustomer.isEmpty()) return false;

            return true;
        } catch (Exception e){
            return false;
        }
    }
}
