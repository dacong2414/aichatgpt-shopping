package com.unfbx.chatgptsteamoutput.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    //private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public Result bizExceptionHandler(HttpServletRequest req, ServiceException e){
        return Result.fail(ErrorCodeEnum.ERROR,e.getMessage());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value =NullPointerException.class)
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return Result.fail(ErrorCodeEnum.ERROR,e.getMessage());
    }


    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value =Exception.class)
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("发生空指针异常！原因是:",e);
        return Result.fail(ErrorCodeEnum.ERROR,e.getMessage());
    }
}