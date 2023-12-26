package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.MemberRole;
import site.pointman.chatbot.constant.OrderStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.MemberService;
import site.pointman.chatbot.service.S3FileService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    S3FileService s3FileService;
    ChatBotExceptionResponse chatBotExceptionResponse;

    public MemberServiceImpl(MemberRepository memberRepository, OrderRepository orderRepository, ProductRepository productRepository, S3FileService s3FileService) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.s3FileService = s3FileService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public HttpResponse join(String userKey, String name, String phoneNumber) {
        try {

            Member member = Member.builder()
                    .userKey(userKey)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .profile(new Profile(name))
                    .memberRole(MemberRole.CUSTOMER_BRONZE)
                    .build();

            memberRepository.save(member);

            return new HttpResponse(ApiResultCode.OK,"회원가입을 성공적으로 완료하였습니다.");
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"회원가입을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public HttpResponse getMembers() {
        List<Member> members = memberRepository.findByAll();
        if (members.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"회원이 존재하지 않습니다");

        return new HttpResponse(ApiResultCode.OK,"정상적으로 회원 조회를 완료하였습니다",members);
    }

    @Override
    public HttpResponse getMember(String userKey) {
        try {
            Member member = memberRepository.findByUserKey(userKey).get();

            if (Objects.isNull(member)) return new HttpResponse(ApiResultCode.FAIL,"회원이 존재하지 않습니다");

            return new HttpResponse(ApiResultCode.OK,"회원 조회를 성공하였습니다",member);
        }catch (Exception e) {
            return  new HttpResponse(ApiResultCode.FAIL,"회원 조회 실패");
        }
    }

    @Override
    public HttpResponse updateMember(String userKey, Member member) {
        try {
            memberRepository.updateMember(userKey,member);
            return new HttpResponse(ApiResultCode.OK,"회원정보 변경을 완료하였습니다.");
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"회원정보 변경을 실패하였습니다.");
        }
    }

    @Override
    public HttpResponse updateMemberPhoneNumber(String userKey, String updatePhoneNumber) {
        try {
            memberRepository.updateMemberPhoneNumber(userKey, updatePhoneNumber);

            return new HttpResponse(ApiResultCode.OK,"연락처를 수정하였습니다.");
        }catch (Exception e) {

            return new HttpResponse(ApiResultCode.FAIL,"연락처 수정을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public HttpResponse deleteMember(String userKey) {
        try {
            List<Order> orders = orderRepository.findByBuyerUserKey(userKey);
            orders.forEach(order -> {
                if (order.getStatus().equals(OrderStatus.주문체결)){
                    throw new IllegalStateException("구매중인 주문이 있어 회원탈퇴가 불가능합니다.");
                }
            });

            List<Product> products = productRepository.findByUserKey(userKey);
            products.forEach(product -> {
                Optional<Order> salesProduct = orderRepository.findByProductId(product.getId(), OrderStatus.주문체결);
                if (!salesProduct.isEmpty()) throw new IllegalStateException("거래가 체결된 상품이 있어 회원탈퇴가 불가능합니다.");
            });

            memberRepository.delete(userKey);

            return new HttpResponse(ApiResultCode.OK,"회원탈퇴를 성공적으로 완료하였습니다.");
        }catch (IllegalStateException i) {
            return new HttpResponse(ApiResultCode.FAIL,"회원탈퇴를 실패하였습니다. e= "+i.getMessage());
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"회원탈퇴를 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    @Transactional
    public HttpResponse updateMemberProfileImage(String userKey, String profileImageUrl) {
        try {
            Member member = memberRepository.findByUserKey(userKey).get();
            List<String> uploadImages = new ArrayList<>();
            uploadImages.add(profileImageUrl);

            ProductImageDto productImageDto = s3FileService.uploadImages(uploadImages, userKey,member.getName(),"image/profile");

            member.changeMemberProfileImage(productImageDto.getImageUrls().get(0));

            return new HttpResponse(ApiResultCode.OK,"성공적으로 프로필사진을 변경하였습니다.");
        }catch (Exception e) {
            return new HttpResponse(ApiResultCode.FAIL,"프로필사진 등록을 실패하였습니다.");
        }
    }

    @Override
    public boolean isCustomer(String userKey) {
        try {
            Optional<Member> mayBeCustomer = memberRepository.findByUserKey(userKey);

            if (mayBeCustomer.isEmpty()) return false;

            return true;
        } catch (Exception e){
            return false;
        }
    }
    @Override
    public boolean isAdmin(String name,String userKey) {
        try {
            Optional<Member> mayBeCustomer = memberRepository.findAdmin(name,userKey);

            if (mayBeCustomer.isEmpty()) return false;

            return true;
        } catch (Exception e){
            return false;
        }
    }

}
