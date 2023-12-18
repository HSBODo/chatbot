package site.pointman.chatbot.constant;

import java.util.Arrays;

public enum MemberRole {
    CUSTOMER_BRONZE("브론즈회원",1000L),
    CUSTOMER_SILVER("실버회원",10000L),
    CUSTOMER_PLATINUM("플래티넘회원",100000L),
    CUSTOMER_DIAMOND("다이아몬드회원",1000000L),
    ADMIN("관리자",Long.MAX_VALUE)
    ;

    private final String value;
    private final Long score;

    MemberRole(String value, Long score) {
        this.value = value;
        this.score = score;
    }

    public static MemberRole getRank(Long score){
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.getScore() >= score)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s점수 이상의 등급이 존재하지 않습니다.", score)));
    }

    public String getValue() {
        return value;
    }

    public Long getScore() {
        return score;
    }
}
