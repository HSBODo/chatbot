package site.pointman.chatbot.controller.kakaochatbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.notice.service.NoticeService;
import site.pointman.chatbot.exception.NotFoundNotice;
import site.pointman.chatbot.view.kakaochatobotview.NoticeChatBotView;

import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kakaochatbot/notice")
public class NoticeController {

    private final NoticeChatBotView noticeChatBotResponseService;
    private final NoticeService noticeService;
    private final ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    @PostMapping(value = "GET" , headers = {"Accept=application/json; UTF-8"})
    public Object getNotices(@RequestBody ChatBotRequest chatBotRequest){
        int pageNumber = chatBotRequest.getPageNumber();

        Page<Notice> mainNoticePage = noticeService.getDefaultNotices(pageNumber);

        return noticeChatBotResponseService.noticeListPage(mainNoticePage,pageNumber);
    }


    @PostMapping(value = "GET/byNoticeId" , headers = {"Accept=application/json; UTF-8"})
    public Object getNotice(@RequestBody ChatBotRequest chatBotRequest){
        try {
            Long noticeId = Long.parseLong(chatBotRequest.getNoticeId());

            Notice notice = noticeService.getNotice(noticeId);

            return noticeChatBotResponseService.noticeDetailPage(notice);
        }catch (NotFoundNotice e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }
}
