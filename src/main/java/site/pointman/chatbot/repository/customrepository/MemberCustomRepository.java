package site.pointman.chatbot.repository.customrepository;

import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;


public interface MemberCustomRepository {
    Member updateMember(String userKey, MemberProfileDto memberDto, boolean isUse);
    void delete(String userKey);
}
