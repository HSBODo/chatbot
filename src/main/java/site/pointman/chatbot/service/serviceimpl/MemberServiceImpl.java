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
    public Map<String,String> saveLocation(KakaoMemberLocation userLocation) {

        return  validateDuplicateMemberLocation(userLocation);
    }

    @Override
    public void join(KakaoMember user) {
        validateDuplicateMember(user);
    }


    private void validateDuplicateMember(KakaoMember member) {
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(member.getKakaoUserkey());
            if(!findMember.isEmpty())throw new DuplicateKeyException("중복회원입니다.");
            kakaoUserRepository.save(member);
    }
    private Map<String,String> validateDuplicateMemberLocation(KakaoMemberLocation memberLocation) {
        Map result = new HashMap<>();
        String kakaoUserkey = memberLocation.getKakaoUserkey();
        try {
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(kakaoUserkey);
            if(!findMember.isEmpty()){
                result.put("msg","위치저장 실패. 존재하지 않은 회원");
                result.put("code",1);
                result.put("kakaoUserkey",kakaoUserkey);
                return result;
            }
            Optional<KakaoMemberLocation> findLocation = kakaoUserRepository.findByLocation(kakaoUserkey);
            if(!findLocation.isPresent()){
                KakaoMemberLocation kakaoMemberLocation = kakaoUserRepository.saveLocation(memberLocation);
                result.put("msg","위치저장 완료");
                result.put("code",0);
                result.put("kakaoUserkey",kakaoUserkey);
                result.put("kakaoMemberLocation",kakaoMemberLocation);
                result.put("X",memberLocation.getX());
                result.put("Y",memberLocation.getY());
            }else {
                kakaoUserRepository.updateLocation(kakaoUserkey,memberLocation.getX(),memberLocation.getY());
                result.put("msg","위치업데이트 완료.");
                result.put("code",0);
                result.put("kakaoUserkey",kakaoUserkey);
                result.put("X",memberLocation.getX());
                result.put("Y",memberLocation.getY());
            }
        }catch (Exception e){
            throw new NullPointerException("위치저장 실패");
        }
        return result;
    }
}
