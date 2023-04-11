package site.pointman.chatbot.service.serviceimpl;

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
    public Map<String,String> join(KakaoMember user) {
        return  validateDuplicateMember(user);
    }


    private Map<String,String> validateDuplicateMember(KakaoMember member) {
        Map result = new HashMap<>();
        try {
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(member.getKakaoUserkey());
            if(!findMember.isPresent()){
                kakaoUserRepository.save(member);
                result.put("msg","회원가입 완료.");
                result.put("code",1);
                result.put("kakaoUserkey",member.getKakaoUserkey());
            }else {
                result.put("msg","중복회원");
                result.put("code",1);
                result.put("kakaoUserkey",member.getKakaoUserkey());
            }
        }catch (Exception e){
            throw e;
        }
        return result;
    }

    private Map<String,String> validateDuplicateMemberLocation(KakaoMemberLocation memberLocation) {
        Map result = new HashMap<>();
        try {
            String kakaoUserkey = memberLocation.getKakaoUserkey();
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(kakaoUserkey);
            if(!findMember.isPresent()){
                result.put("msg","위치저장 실패. 존재하지 않은 회원");
                result.put("code",1);
                result.put("kakaoUserkey",kakaoUserkey);
                return result;
            }

            Optional<KakaoMemberLocation> findLocation = kakaoUserRepository.findByLocation(kakaoUserkey);
            if(!findLocation.isPresent()){
                kakaoUserRepository.saveLocation(memberLocation);
                result.put("msg","위치저장 완료");
                result.put("code",0);
                result.put("kakaoUserkey",kakaoUserkey);
                result.put("X",memberLocation.getX());
                result.put("Y",memberLocation.getY());
            }else {
                Map<String, BigDecimal> updateParams= new HashMap<>();
                updateParams.put("X",memberLocation.getX());
                updateParams.put("Y",memberLocation.getY());
                kakaoUserRepository.updateLocation(kakaoUserkey,updateParams);
                result.put("msg","위치업데이트 완료.");
                result.put("code",0);
                result.put("kakaoUserkey",kakaoUserkey);
                result.put("X",memberLocation.getX());
                result.put("Y",memberLocation.getY());
            }
        }catch (Exception e){
            result.put("msg","위치저장 실패. ");
            result.put("code",1);
        }

        return result;
    }
}
