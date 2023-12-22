package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.notice.Notice;
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
    public Object add(@RequestBody Notice notice){
        return noticeService.addNotice(notice);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public List<Notice> getNotices(){
        return noticeService.getNoticeAll();
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.GET)
    public Object getNotice(@PathVariable String noticeId){
        return noticeService.getNotice(noticeId,false);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.DELETE)
    public Object deleteNotice(@PathVariable Long noticeId){
        return noticeService.removeNotice(noticeId);
    }
}
