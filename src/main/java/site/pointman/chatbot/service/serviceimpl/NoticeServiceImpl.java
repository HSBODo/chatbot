package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.common.ListItem;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.ListCard;
import site.pointman.chatbot.repository.NoticeRepository;
import site.pointman.chatbot.service.NoticeService;

import java.util.List;
import java.util.Optional;

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
        List<Notice> notices = noticeRepository.findByStatus(NoticeStatus.작성);


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
            listItem.setActionBlock(BlockId.FIND_NOTICE);
            listItem.setExtra(ButtonParamKey.noticeId,String.valueOf(notice.getId()));

            listCard.setItem(listItem);
        });

        chatBotResponse.addListCard(listCard);
        return chatBotResponse;
    }

    @Override
    public Response getNotice(String noticeId, boolean isChatBotRequest) {
        try {
            long parseNoticeId = Long.parseLong(noticeId);

            Optional<Notice> mayBeNotice = noticeRepository.findByNoticeId(parseNoticeId);

            if(mayBeNotice.isEmpty()) return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다.");

            Notice notice = mayBeNotice.get();

            ChatBotResponse chatBotResponse = new ChatBotResponse();

            if(notice.getType().equals(NoticeType.TEXT_CARD)){
                chatBotResponse.addTextCard(notice.getTitle(),notice.getDescription());
                chatBotResponse.addQuickButton(ButtonName.이전으로.name(),ButtonAction.블럭이동,BlockId.FIND_NOTICES.getBlockId());
                return chatBotResponse;
            }

            if(notice.getType().equals(NoticeType.BASIC_CARD)){
                BasicCard basicCard = new BasicCard();
                basicCard.setThumbnail(notice.getImageUrl());
                basicCard.setTitle(notice.getTitle());
                basicCard.setDescription(notice.getDescription());
                notice.getButtons().forEach(button -> {
                    basicCard.setButton(button);
                });
                chatBotResponse.addBasicCard(basicCard);
                chatBotResponse.addQuickButton(ButtonName.이전으로.name(),ButtonAction.블럭이동,BlockId.FIND_NOTICES.getBlockId());
                return chatBotResponse;
            }

            return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다. e = type");
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }
}
