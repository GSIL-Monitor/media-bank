package com.syswin.temail.media.bank.exception;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DefineExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(DefineExceptionHandler.class);


  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    Enumeration<String> headerNames = request.getHeaderNames();
    StringBuffer bufHeader = new StringBuffer();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      bufHeader.append(headerName).append("=").append(headerValue).append(";");
    }
    Enumeration<String> parameterNames = request.getParameterNames();
    StringBuffer bufParam = new StringBuffer();
    while (parameterNames.hasMoreElements()) {
      String paramKey = parameterNames.nextElement();
      String paramValue = request.getHeader(paramKey);
      bufParam.append(paramKey).append("=").append(paramValue).append(";");
    }
    logger.error("Parameter=[{}],Header=[{}]:", bufParam.toString(), bufHeader.toString(), e);

    Map resultDto = new HashMap();
    resultDto.put("code", 500);
    resultDto.put("message", "异常信息为:" + e.getMessage());
    return resultDto;
  }


  @ExceptionHandler({Exception.class})
  public ResponseEntity<String> handler(HttpServletRequest request, HttpServletResponse response, Exception e) {

    Enumeration<String> headerNames = request.getHeaderNames();
    StringBuffer bufHeader = new StringBuffer();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      bufHeader.append(headerName).append("=").append(headerValue).append(";");
    }
    Enumeration<String> parameterNames = request.getParameterNames();
    StringBuffer bufParam = new StringBuffer();
    while (parameterNames.hasMoreElements()) {
      String paramKey = parameterNames.nextElement();
      String paramValue = request.getHeader(paramKey);
      bufParam.append(paramKey).append("=").append(paramValue).append(";");
    }
    logger.error("Parameter=[{}],Header=[{}]:", bufParam.toString(), bufHeader.toString(), e);

    if (e instanceof DefineException) {
      DefineException defineException = (DefineException) e;
      return new ResponseEntity<>(defineException.getMsg(), HttpStatus.valueOf(defineException.getCode()));
    }
    return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(ResponseCodeConstants.SERVER_ERROR));
  }
}