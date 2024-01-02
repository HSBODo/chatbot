package site.pointman.chatbot.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import site.pointman.chatbot.domain.log.ChatBotLog;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.domain.log.service.LogService;
import site.pointman.chatbot.domain.member.service.MemberService;

@Aspect
@Component
@Slf4j
public class Aop {
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    LogService logService;
    AuthService authService;
    MemberService memberService;

    public Aop(LogService logService, AuthService authService, MemberService memberService) {
        this.logService = logService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @Pointcut("execution(* site.pointman.chatbot.controller.kakaochatbot.*.*(..)) && (!@annotation(site.pointman.chatbot.annotation.SkipLogging) ||  @annotation(site.pointman.chatbot.annotation.ValidateMember))") //Pointcut
    public void chatBotControllerPointcut() {
    }

    @Pointcut("execution(* site.pointman.chatbot.controller.kakaochatbot.*.*(..)) && @annotation(site.pointman.chatbot.annotation.ValidateMember)") //Pointcut
    public void chatBotControllerValidateMemberPointcut() {
    }

    @Around("chatBotControllerPointcut()")
    public Object chatBotLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ChatBotLog logEntity = new ChatBotLog();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("==== 3.AOP Log JOINPOINT ====");
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
        log.info("==== 4.AOP Log END {} ====",stopWatch.getTotalTimeSeconds());
        return proceed;
    }

    @Around("chatBotControllerValidateMemberPointcut()")
    public Object chatBotValidateMember(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("==== 3.AOP ValidateMember JOINPOINT  ====");
        Object[] args = joinPoint.getArgs();

        for(Object obj : args) {
            System.out.println("type : "+obj.getClass().getSimpleName());

            ChatBotRequest chatBotRequest = (ChatBotRequest) obj;
            String userKey = chatBotRequest.getUserKey();
            if (!memberService.isCustomer(userKey)) return chatBotExceptionResponse.notCustomerException();
        }

        Object proceed = joinPoint.proceed();

        log.info("==== 4.AOP ValidateMember END ====");
        return proceed;
    }

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
