package site.pointman.chatbot.aop;


import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.log.ChatBotLog;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.LogService;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class Aop {
    LogService logService;
    AuthService authService;

    public Aop(LogService logService, AuthService authService) {
        this.logService = logService;
        this.authService = authService;
    }

    @Pointcut("execution(* site.pointman.chatbot.controller.kakaochatbot.*.*(..)) && !@annotation(site.pointman.chatbot.annotation.SkipLogging)") //Pointcut
    public void chatBotControllerPointcut() {
    }
    @Pointcut("execution(* site.pointman.chatbot.controller.admin.*.*(..)) && !@annotation(site.pointman.chatbot.annotation.SkipAop)") //Pointcut
    public void adminControllerPointcut() {
    }

    @Around("chatBotControllerPointcut()")
    public Object chatBotLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ChatBotLog logEntity = new ChatBotLog();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("==== 3.AOP JOINPOINT ====");
        log.info("{}",controllerName+"/"+methodName);
        //메서드에 들어가는 매개변수 배열을 읽어옴
        Object[] args = joinPoint.getArgs();

        //매개변수 배열의 종류와 값을 출력
        for(Object obj : args) {
            System.out.println("type : "+obj.getClass().getSimpleName());
            ChatBotRequest chatBotRequest = (ChatBotRequest) obj;
            logEntity = logService.insertChatBotRequestLog(chatBotRequest);
        }

        Object proceed = joinPoint.proceed();

        logService.insertChatBotResponseLog(logEntity,proceed);

        stopWatch.stop();
        log.info("==== 4.AOP END {} ====",stopWatch.getTotalTimeSeconds());
        return proceed;
    }

//    @Around("adminControllerPointcut()")
//    public Object adminAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
//        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
//        String methodName = joinPoint.getSignature().getName();
//        log.info("==== 3.AOP JOINPOINT ====");
//        log.info("controllerName={}",controllerName);
//        log.info("methodName={}",methodName);
//
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = requestAttributes.getRequest();
//        String token = request.getHeader("token");
//
//        if(StringUtils.isNullOrEmpty(token)) return new Response(ResultCode.FAIL,"로그인이 필요합니다.");
//
//        if(!authService.isTokenVerification(token)) return new Response(ResultCode.FAIL,"유효하지 않은 토큰입니다.");
//
//        Object proceed = joinPoint.proceed();
//
//        log.info("==== 4.AOP END ====");
//        return proceed;
//    }

    //    @Before("controllerPointcut()")
//    public void before(JoinPoint joinPoint) throws Exception {
//
//        //실행되는 함수 이름을 가져오고 출력
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        System.out.println(method.getName() + "메서드 실행");
//
//        //메서드에 들어가는 매개변수 배열을 읽어옴
//        Object[] args = joinPoint.getArgs();
//
//        //매개변수 배열의 종류와 값을 출력
//        for(Object obj : args) {
//            System.out.println("type : "+obj.getClass().getSimpleName());
//            ChatBotRequest chatBotRequest = (ChatBotRequest) obj;
//            logService.insert(chatBotRequest);
//        }
//    }

//    @AfterReturning(value = "controllerPointcut()",returning = "chatBotResponse")
//    public void after(JoinPoint joinPoint,ChatBotResponse chatBotResponse) throws Exception {
//
//        //실행되는 함수 이름을 가져오고 출력
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        System.out.println(method.getName() + "메서드 실행");
//
//        //logService.insert(chatBotResponse);
//    }
}
