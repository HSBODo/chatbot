package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;
import site.pointman.chatbot.domain.notice.service.NoticeService;
import site.pointman.chatbot.exception.NotFoundMember;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity add(@RequestBody NoticeDto noticeDto){

        HttpHeaders headers = getHeaders();
        try {
            noticeService.addNotice(noticeDto);

            return new ResponseEntity<>(new Response<>(ResultCode.OK,"정상적으로 글을 작성하였습니다."),headers, HttpStatus.OK);
        }catch (NotFoundMember e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"글 작성을 실패하였습니다",e.getMessage()),headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.FAIL,"글 작성을 실패하였습니다",e.getMessage()),headers, HttpStatus.OK);
        }

    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity getNotices(){
        HttpHeaders headers = getHeaders();
        try {
            List<NoticeDto> noticeDtoAll = noticeService.getNoticeDtoAll();

            if (noticeDtoAll.isEmpty()) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"게시글이 존재하지 않습니다"),headers,HttpStatus.OK);

            return new ResponseEntity<>(new Response(ResultCode.OK,"성공적으로 게시글을 조회하였습니다.",noticeDtoAll),headers,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Response<>(ResultCode.FAIL,"게시글 조회를 실패하였습니다.",e.getMessage()),headers, HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.GET)
    public ResponseEntity getNotice(@PathVariable Long noticeId){
        HttpHeaders headers = getHeaders();
        try {

            NoticeDto noticeDto = noticeService.getNoticeDto(noticeId);

            return new ResponseEntity<>(new Response<>(ResultCode.OK,"성공적으로 게시글을 조회하였습니다",noticeDto),headers,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"게시글 조회를 실패하였습니다.",e.getMessage()),headers,HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.DELETE)
    public ResponseEntity deleteNotice(@PathVariable Long noticeId){
        HttpHeaders headers = getHeaders();
        try {

            noticeService.removeNotice(noticeId);

            return new ResponseEntity<>(new Response<>(ResultCode.OK,"성공적으로 게시글 삭제를 완료하였습니다.",noticeId),headers,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"게시글 삭제를 실패하였습니다.",e.getMessage()),headers,HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDto noticeDto){
        HttpHeaders headers = getHeaders();
        try {

            noticeService.updateNotice(noticeId, noticeDto);

            return new ResponseEntity<>(new Response<>(ResultCode.OK,"성공적으로 게시글 수정을 완료하였습니다.",noticeId),headers,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Response<>(ResultCode.EXCEPTION,"게시글 수정을 실패하였습니다.",e.getMessage()),headers,HttpStatus.OK);
        }
    }


    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
