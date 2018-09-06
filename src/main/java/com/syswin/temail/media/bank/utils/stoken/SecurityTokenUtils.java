package com.syswin.temail.media.bank.utils.stoken;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class SecurityTokenUtils {

	private static SecurityTokenCheckResult baseCheckIndependenceSecurityToken(SecurityToken securityToken, int appId,
			long ts, String authNeed, String secret)
					throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		SecurityTokenCheckResult r = new SecurityTokenCheckResult();
		String signatureSecurityToken = Base64SafeUrlUtils
				.safeUrlEncode(HmacSha1Utils.hmacSha1(securityToken.getPlaintext().getBytes(), secret.getBytes()));

		if (!signatureSecurityToken.equals(securityToken.getSignature())) {
			r.setPass(false);
			r.setMsg("The token is a forgery!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		if (appId != securityToken.getAppid()) {
			r.setPass(false);
			r.setMsg("There is no access to this tenant!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		if (ts < securityToken.getTs() - RequestConstants.requestUrl_expires
				|| ts > securityToken.getTs() + RequestConstants.requestUrl_expires) {
			r.setPass(false);
			r.setMsg("The token is not in the time window!");
			r.setCode(ResponseCodeConstants.PARAM_ERROR);
			return r;
		}

		if (!checkAuth(securityToken, authNeed)) {
			r.setPass(false);
			r.setMsg("The token does not have the permissions to this directory or resource!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		return null;
	}

	private static boolean checkAuth(SecurityToken securityToken, String auth) {
		if (auth == null)
			return true;
		if (auth.equals(""))
			return true;
		if (auth.equals(securityToken.getAuth()))
			return true;
		if (auth.equals("r"))
			if ("w".equals(securityToken.getAuth()))
				return true;
		return false;
	}

	public static SecurityTokenCheckResult checkSecurityToken(final HttpServletRequest request, int appId, String fileId, String authNeed, String secret)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		long ts = System.currentTimeMillis();
		String stoken = request.getHeader("stoken");
		SecurityTokenCheckResult r = new SecurityTokenCheckResult();

		if(StringUtils.isBlank(stoken)) {
			r.setPass(false);
			r.setMsg("The token is empty!");
			return r;
		}
		SecurityToken securityToken = new SecurityToken(stoken);
		return checkFileIndependenceSecurityToken(securityToken, appId, ts, fileId, authNeed, secret);
	}

	/**
	 *
	 * @param securityToken
	 * @param appId
	 *            访问资源所有者
	 * @param ts
	 *            访问时间
	 * @param fileId
	 *            文件ID
	 * @param authNeed
	 *            访问action 所需的权限
	 * @param secret
	 *            访问资源所有者密码
	 * @return 检验结果
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */

	public static SecurityTokenCheckResult checkFileIndependenceSecurityToken(SecurityToken securityToken, int appId,
																				long ts, String fileId, String authNeed, String secret)
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		SecurityTokenCheckResult t = baseCheckIndependenceSecurityToken(securityToken, appId, ts, authNeed, secret);
		if (t != null)
			return t;

		SecurityTokenCheckResult r = new SecurityTokenCheckResult();
		if ((securityToken.getNum() != 1) || (securityToken.getIndependence() != 0)) {
			r.setPass(false);
			r.setMsg("The token is not a security token that the file stores for its own use!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		String appFileId = securityToken.getStoid();
		if (StringUtils.isNotBlank(appFileId) && StringUtils.isNotBlank(fileId) && !appFileId.equals(fileId)) {
			r.setPass(false);
			r.setMsg("your token does not have access to the file!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		String plaintext = "";
		plaintext += securityToken.getSecurityTokenType() + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		plaintext += securityToken.getAppid() + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		plaintext += securityToken.getTs() + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		plaintext += securityToken.getAuth() + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		if (StringUtils.isNotBlank(appFileId))
			plaintext += fileId;
		String signature = Base64SafeUrlUtils
				.safeUrlEncode(HmacSha1Utils.hmacSha1(plaintext.getBytes(), secret.getBytes()));

		if (!signature.equals(securityToken.getSignature())) {
			r.setPass(false);
			r.setMsg("Your parameter signature is inconsistent with the token, and your token does not have access to the file!");
			r.setCode(ResponseCodeConstants.FORBID_ACCESS_ERROR);
			return r;
		}

		r.setPass(true);
		r.setMsg("success!");
		r.setCode(ResponseCodeConstants.SUCCESS);
		return r;
	}

}