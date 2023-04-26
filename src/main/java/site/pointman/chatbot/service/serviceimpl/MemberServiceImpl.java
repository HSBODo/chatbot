package site.pointman.chatbot.service.serviceimpl;

import net.bytebuddy.implementation.bytecode.Duplication;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.MemberService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    KakaoMemberRepository kakaoUserRepository;


    public MemberServiceImpl(KakaoMemberRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    @Override
    public void saveLocation(KakaoMemberLocation userLocation) {
        validateDuplicateMemberLocation(userLocation);
    }

    @Override
    public void join(KakaoMember user) {
        validateDuplicateMember(user);
    }


    private void validateDuplicateMember(KakaoMember member) {
        try {
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(member.getKakaoUserkey());
            if(findMember.isEmpty())kakaoUserRepository.save(member);
        }catch (Exception e){
            throw new IllegalArgumentException("회원가입에 실패하였습니다.");
        }
    }
    private void validateDuplicateMemberLocation(KakaoMemberLocation memberLocation) {
        String kakaoUserkey = memberLocation.getKakaoUserkey();
        try {
            Optional<KakaoMember> maybeFindMember = kakaoUserRepository.findByMember(kakaoUserkey);
            if(maybeFindMember.isEmpty()) throw new NullPointerException("존재하지 않은 회원입니다.");
            KakaoMember member = maybeFindMember.get();

            Optional<KakaoMemberLocation> maybeFindLocation = kakaoUserRepository.findByLocation(member.getKakaoUserkey());
            if(maybeFindLocation.isEmpty()){ //<================= 처음
                kakaoUserRepository.saveLocation(memberLocation);
            }else {//<========= 기존에 위치저장을 했던 회원
                kakaoUserRepository.updateLocation(kakaoUserkey,memberLocation.getX(),memberLocation.getY());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new NullPointerException("위치정보 저장에 실패하였습니다.");
        }
    }
}
