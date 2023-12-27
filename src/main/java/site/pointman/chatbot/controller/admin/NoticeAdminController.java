package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.service.NoticeService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "admin/notice")
public class NoticeAdminController {
    NoticeService noticeService;

    public NoticeAdminController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse add(@RequestBody Notice notice){
        return noticeService.addNotice(notice);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public HttpResponse getNotices(){
        return noticeService.getNoticeAll();
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.GET)
    public HttpResponse getNotice(@PathVariable String noticeId){
        return noticeService.getNotice(noticeId);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.DELETE)
    public HttpResponse deleteNotice(@PathVariable Long noticeId){
        return noticeService.removeNotice(noticeId);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse updateNotice(@PathVariable Long noticeId, @RequestBody Notice notice){
        return noticeService.updateNotice(noticeId,notice);
    }
}
