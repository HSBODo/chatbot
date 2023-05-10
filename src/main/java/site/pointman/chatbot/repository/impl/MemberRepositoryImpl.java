package site.pointman.chatbot.repository.impl;


import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.MemberAttribute;
import site.pointman.chatbot.domain.member.Platform;
import site.pointman.chatbot.repository.MemberRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberRepositoryImpl implements MemberRepository {
    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Member delete(String kakaoUserkey) {
        Member member = em.find(Member.class,kakaoUserkey);
        member.Withdrawal();
       return member;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Optional<Member> findByMember(String kakaoUserkey) {
        Member findMember = em.find(Member.class,kakaoUserkey);
        return Optional.ofNullable(findMember);
    }

    @Override
    public Optional<Member> findByKakaoMember(String kakaoUserkey) {
     List<Member> findKakaoMember = em.createQuery("select m from Member m where kakaoUserkey=:kakaoUserkey and isUse=:isUse and platform=:platform", Member.class)
                .setParameter("kakaoUserkey", kakaoUserkey)
                .setParameter("isUse", "Y")
                .setParameter("platform", Platform.KAKAO)
                .getResultList();
        return  findKakaoMember.stream().findAny();
    }

    @Override
    public Optional<Member> findByKakaoWithdrawalMember(String kakaoUserkey) {
        List<Member> findKakaoMember = em.createQuery("select m from Member m where kakaoUserkey=:kakaoUserkey and isUse=:isUse and platform=:platform", Member.class)
                .setParameter("kakaoUserkey", kakaoUserkey)
                .setParameter("isUse", "N")
                .setParameter("platform", Platform.KAKAO)
                .getResultList();
        return  findKakaoMember.stream().findAny();
    }

    @Override
    public KakaoMemberLocation saveLocation(KakaoMemberLocation memberLocation) {
        em.persist(memberLocation);
        return memberLocation;
    }

    @Override
    public Optional<KakaoMemberLocation> updateLocation(String kakaoUserkey, BigDecimal x , BigDecimal y) {
        KakaoMemberLocation memberLocation = em.find(KakaoMemberLocation.class,kakaoUserkey);
        memberLocation.changeX(x);
        memberLocation.changeY(y);
        return Optional.ofNullable(memberLocation);
    }

    @Override
    public Optional<KakaoMemberLocation> findByLocation(String kakaoUserkey) {
        KakaoMemberLocation member = em.find(KakaoMemberLocation.class,kakaoUserkey);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<MemberAttribute> findByAttribute(String kakaoUserkey) {
        MemberAttribute memberAttribute = em.find(MemberAttribute.class,kakaoUserkey);
        return Optional.ofNullable(memberAttribute);
    }

    @Override
    public MemberAttribute saveAttribute(MemberAttribute memberAttribute) {
        em.persist(memberAttribute);
        return memberAttribute;
    }

    @Override
    public MemberAttribute updateAttribute(MemberAttribute updateAttribute) {
        MemberAttribute findMemberAttribute = em.find(MemberAttribute.class,updateAttribute.getKakaoUserkey());
        findMemberAttribute.changeQuantity(updateAttribute.getQuantity());
        findMemberAttribute.changeOptionCode(updateAttribute.getOptionCode());
        return findMemberAttribute;
    }

    @Override
    public MemberAttribute updateQuantityAttribute(String kakaoUserkey, int quantity) {
        MemberAttribute findMemberAttribute = em.find(MemberAttribute.class,kakaoUserkey);
        findMemberAttribute.changeQuantity(quantity);
        return findMemberAttribute;
    }

    @Override
    public MemberAttribute updateOptionAttribute(String kakaoUserkey, Long optionCode) {
        MemberAttribute findMemberAttribute = em.find(MemberAttribute.class,kakaoUserkey);
        findMemberAttribute.changeOptionCode(optionCode);
        return findMemberAttribute;
    }
}
