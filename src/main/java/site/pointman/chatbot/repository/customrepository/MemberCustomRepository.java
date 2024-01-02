package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.domain.member.Member;


public interface MemberCustomRepository {
    Member updateMember(String userKey, Member member, boolean isUse);
    void delete(String userKey, boolean isUse);
}
