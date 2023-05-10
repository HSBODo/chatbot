package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.MemberAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member delete(String kakaoUserkey);
    List<Member> findAll();
    Optional<Member> findByMember(String kakaoUserkey);
    Optional<Member> findByKakaoMember(String kakaoUserkey);

    Optional<Member> findByKakaoWithdrawalMember(String kakaoUserkey);

    KakaoMemberLocation saveLocation (KakaoMemberLocation memberLocation);
    Optional<KakaoMemberLocation> updateLocation (String kakaoUserkey, BigDecimal x , BigDecimal y);
    Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey);

    Optional<MemberAttribute> findByAttribute (String kakaoUserkey);
    MemberAttribute saveAttribute (MemberAttribute memberAttribute);
    MemberAttribute updateAttribute (MemberAttribute updateAttribute);

    MemberAttribute updateQuantityAttribute (String kakaoUserkey, int quantity);
    MemberAttribute updateOptionAttribute (String kakaoUserkey, Long optionCode);
}
