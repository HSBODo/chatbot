package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.ApiResultCode;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.NoticeStatus;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.common.ListItem;
import site.pointman.chatbot.domain.response.property.components.ListCard;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    NoticeRepository noticeRepository;
    ChatBotExceptionResponse chatBotExceptionResponse;

    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public Response addNotice(Notice notice) {
        try {
            Long noticeId = noticeRepository.save(notice);

            return new HttpResponse(ApiResultCode.OK,"정상적으로 게시글을 등록하였습니다. 게시글 ID="+noticeId);
        }catch (Exception e){
            return new HttpResponse(ApiResultCode.FAIL,"게시글 등록에 실패하였습니다.");
        }
    }

    @Override
    public Response getNotices(boolean isChatBotRequest) {
        List<Notice> notices = noticeRepository.findAll(NoticeStatus.작성);


        return getNoticesSuccessChatBotResponse(notices);
    }

    private ChatBotResponse getNoticesSuccessChatBotResponse(List<Notice> notices){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        if (notices.isEmpty()) return chatBotExceptionResponse.createException("등록된 게시글이 없습니다.");

        ListCard listCard = new ListCard();
        listCard.setHeader("공지사항");

        notices.forEach(notice -> {
            ListItem listItem = new ListItem(notice.getTitle());
            listItem.setDescription(notice.getDescription());
            listItem.setImageUrl(notice.getImageUrl());
            listItem.setActionBlock(BlockId.MAIN);
            listItem.setExtra("choice",String.valueOf(notice.getId()));

            listCard.setItem(listItem);
        });

        chatBotResponse.addListCard(listCard);
        return chatBotResponse;
    }
}
