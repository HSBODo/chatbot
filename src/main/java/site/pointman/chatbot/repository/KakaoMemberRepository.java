package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.member.MemberAttribute;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface KakaoMemberRepository {
    KakaoMember save (KakaoMember member);
    KakaoMemberLocation saveLocation (KakaoMemberLocation memberLocation);
    Optional<KakaoMember> findByMember(String  kakaoUserkey);
    Optional<KakaoMemberLocation> updateLocation (String kakaoUserkey, BigDecimal x , BigDecimal y);
    Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey);

    Optional<MemberAttribute> findByAttribute (String kakaoUserkey);
    MemberAttribute saveAttribute (MemberAttribute memberAttribute);
    MemberAttribute updateAttribute (MemberAttribute updateAttribute);

    MemberAttribute updateQuantityAttribute (String kakaoUserkey, int quantity);
    MemberAttribute updateOptionAttribute (String kakaoUserkey, Long optionCode);



}
