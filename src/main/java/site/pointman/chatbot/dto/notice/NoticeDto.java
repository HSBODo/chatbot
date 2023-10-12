package site.pointman.chatbot.dto.notice;


import lombok.Getter;

@Getter
public class NoticeDto {
    private String sellerNoticeId;
    private String postCategoryType;
    private String title;
    private boolean importantNotice;
    private boolean wholeNotice;
    private String displayStartDate;
    private String displayEndDate;
}
