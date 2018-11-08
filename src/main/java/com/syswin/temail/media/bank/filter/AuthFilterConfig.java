package com.syswin.temail.media.bank.filter;

import com.alibaba.fastjson.JSONObject;
import com.syswin.temail.media.bank.bean.AppInfo;
import com.syswin.temail.media.bank.config.AppInfoBean;
import com.syswin.temail.media.bank.config.TemailAuthVerify;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.service.impl.TokenService;
import com.syswin.temail.media.bank.utils.AESEncrypt;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.stoken.SecurityToken;
import com.syswin.temail.media.bank.utils.stoken.SecurityTokenCheckResult;
import com.syswin.temail.media.bank.utils.stoken.SecurityTokenUtils;
import com.syswin.temail.media.bank.utils.stoken.StokenHelper;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthFilterConfig implements WebMvcConfigurer {

  private static final Logger logger = LoggerFactory.getLogger("async-info");

  @Bean
  AuthInterceptor authInterceptor() {
    return new AuthInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    List<String> excludeUrl = new ArrayList();
    excludeUrl.add("/error");
    registry.addInterceptor(authInterceptor()).excludePathPatterns(excludeUrl);
  }

  public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AppInfoBean appInfoConfig;

    @Autowired
    private FileService fileService;

    @Autowired
    private TemailAuthVerify temailAuthVerify;

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler)
        throws Exception {
      response.setHeader("Cache-Control", "public");
      response.setHeader("Access-Control-Allow-Headers", "*");
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("tMark", getTMark(request));

      String action = request.getRequestURI();
      // 只验证stoken
      String stoken = request.getHeader("stoken");
      if (temailAuthVerify.getVerifySwitch()) {
        tokenService.verify(stoken);
      } else {
        stoken = StokenHelper.defaultStoken();
        checkActionUploadFile(request, action, stoken);
        checkActionContinueUploadFile(request, action, stoken);
        checkActionDownloadFile(action, stoken, request, response);
      }
      return true;
    }

    private boolean checkActionDownloadFile(String action, String stoken, HttpServletRequest request,
        HttpServletResponse response) {
      if (action.equals("/downloadFile")) {
        String fileId = request.getParameter("fileId");
        int pub = fileService.getPubByFileId(fileId);
        if (pub != 1) {
          response.setHeader("Cache-Control", "private");
          int appId = fileService.getAppIdByFileId(fileId);
          return authCheck(stoken, appId, fileId);
        }
      }
      return true;
    }

    private Boolean checkActionContinueUploadFile(HttpServletRequest request, String action, String stoken)
        throws Exception {
      if (action.equals("/continueUpload")) {
        if (checkTemailSignature(temailAuthVerify.getUrl(), request)) {
          if (StringUtils.isBlank(stoken)) {
            throw new DefineException(ResponseCodeConstants.AUTH_ERROR, "please add stoken to header");
          }
          SecurityToken securityToken = new SecurityToken(stoken);
          int appId = securityToken.getAppid();
          String uuid = request.getParameter("uuid");
          if (StringUtils.isNotBlank(uuid) && uuid.length() > 13) {
            uuid = AESEncrypt.getInstance().decrypt(uuid);
            String currentTime = uuid.substring(0, 13);
            if (Long.parseLong(currentTime) > System.currentTimeMillis()) {
              throw new DefineException(ResponseCodeConstants.PARAM_ERROR,
                  "file upload time error");
            }
            String fileId = uuid.substring(13);
            fileId = AESEncrypt.getInstance().encrypt(fileId);
            appId = fileService.getAppIdByFileId(fileId);
          }
          return authCheck(stoken, appId, "");
        } else {
          return false;
        }
      }
      return null;
    }

    private Boolean checkActionUploadFile(HttpServletRequest request, String action, String stoken) throws Exception {
      if (action.equals("/uploadFile")) {
        if (checkTemailSignature(temailAuthVerify.getUrl(), request)) {
          if (StringUtils.isBlank(stoken)) {
            throw new DefineException(ResponseCodeConstants.AUTH_ERROR, "please add stoken to header");
          }
          SecurityToken securityToken = new SecurityToken(stoken);
          return authCheck(stoken, securityToken.getAppid(), "");
        } else {
          return false;
        }
      }
      return null;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o,
        ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        Object o, Exception e) throws Exception {
    }

    /**
     *
     * @param stoken
     * @param fileId
     * @throws Exception
     */
    public boolean authCheck(String stoken, int appId, String fileId) {
      if (StringUtils.isBlank(stoken)) {
        throw new DefineException(ResponseCodeConstants.AUTH_ERROR, "please add stoken to header");
      }
      SecurityToken securityToken = new SecurityToken(stoken);
      String authNeed = "w";
      SecurityTokenCheckResult r;
      AppInfo appInfo = appInfoConfig.getAppInfo().get(Integer.toString(securityToken.getAppid()));
      if (appInfo == null) {
        throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "appId invalid");
      }
      String secret = appInfo.getSecurity();
      try {
        r = SecurityTokenUtils.checkSecurityToken(stoken, appId, fileId, authNeed, secret);
      } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
        throw new DefineException(ResponseCodeConstants.AUTH_ERROR, "check stoken error");
      }
      if (r == null) {
        throw new DefineException(ResponseCodeConstants.FORBID_ACCESS_ERROR,
            "The token is a forgery!");
      }
      if (!r.isPass()) {
        throw new DefineException(r.getCode(), r.getMsg());
      }
      return true;
    }
  }

  private String getTMark(HttpServletRequest request) {
    String tMark = request.getHeader("tMark");
    if (tMark == null || tMark.length() == 0 || "unknown".equalsIgnoreCase(tMark)) {
      tMark = request.getHeader("trace_reserve_mark");
    }
    if (tMark == null || tMark.length() == 0 || "unknown".equalsIgnoreCase(tMark)) {
      tMark = (String) request.getAttribute("tMark");
    }
    if (tMark == null || tMark.length() == 0 || "unknown".equalsIgnoreCase(tMark)) {
      tMark = (String) request.getAttribute("trace_reserve_mark");
    }
    if (tMark == null || tMark.length() == 0 || "unknown".equalsIgnoreCase(tMark)) {
      tMark = UUID.randomUUID().toString();
    }
    return tMark;
  }

  private boolean checkTemailSignature(String url, HttpServletRequest request) throws Exception {
    JSONObject params = new JSONObject();
    params.put("algorithm", StringUtils.defaultIfBlank(request.getHeader("algorithm"), ""));
    params.put("SIGNATURE", StringUtils.defaultIfBlank(request.getHeader("SIGNATURE"), ""));
    params.put("TeMail", StringUtils.defaultIfBlank(request.getHeader("TeMail"), ""));
    params.put("UNSIGNED_BYTES", StringUtils.defaultIfBlank(request.getHeader("UNSIGNED_BYTES"), ""));
    return HttpClientUtils.httpPostWithForm(url, params);
  }
}