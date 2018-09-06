package com.syswin.temail.media.bank.utils.stoken;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author admin 云存储安全令牌
 */
public class SecurityToken {
	
	public SecurityToken(String securityToken) throws UnsupportedEncodingException {
		this.securityToken = securityToken;
		String[] s = this.securityToken.split(SecurityTokenConstants.SecurityTokenSeparator);
		this.securityTokenType = s[0];
		this.plaintext = Base64SafeUrlUtils.safeUrlDecode(s[1]);
		this.signature = s[2];
		String[] securityTokenTypeS = securityTokenType.split(SecurityTokenConstants.SecurityTokenTypeSeparator);
		this.company = securityTokenTypeS[0];
		this.product = securityTokenTypeS[1];
		this.version = securityTokenTypeS[2];
		this.num = Integer.valueOf(securityTokenTypeS[3]);
		this.independence = Integer.valueOf(securityTokenTypeS[4]);
		List<String> plaintextList = StringUtil.split(plaintext,
				SecurityTokenConstants.SecurityTokenPlaintextSeparator);

		this.appid = Integer.valueOf(plaintextList.get(1));
		this.ts = Long.valueOf(plaintextList.get(2));
		this.auth = plaintextList.get(3);
		int plaintextLen = plaintextList.size();
		if (this.num == 0) {
			if (plaintextLen == 7) {
				this.region = plaintextList.get(4);
				this.bucket = plaintextList.get(5);
				this.fileName = plaintextList.get(6);
			} else {
				this.bucket = plaintextList.get(4);
				this.fileName = plaintextList.get(5);
			}
		}
		if (this.num == 1) {
			if (plaintextLen == 6) {
				this.region = plaintextList.get(4);
				this.stoid = plaintextList.get(5);
			} else {
				this.stoid = plaintextList.get(4);
			}
		}
		if (this.num == 2) {
			this.formatParamNameString = plaintextList.get(4);
			this.formatParamString = "";
			if (formatParamNameString == null || formatParamNameString.equals("")) {
				this.paramNameList = new ArrayList();
				this.paramList=new ArrayList();
			} else {
				this.paramNameList = StringUtil.split(this.formatParamNameString,
						SecurityTokenConstants.ParamNamesSeparator);
			
			this.paramList = StringUtil.split(plaintext, SecurityTokenConstants.SecurityTokenPlaintextSeparator);
		
			for (int i = 0; i < 5; i++)
				paramList.remove(0);
			
			for (int i = 0; i < paramList.size(); i++) {
				if (i >= 1) {
					this.formatParamString += SecurityTokenConstants.ParamSeparator;
				}
				this.formatParamString += this.paramList.get(i);
			}
			
			}
		}

	}

	public SecurityToken() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 对象存储令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * 
	 * @param appid
	 * @param ts
	 * @param auth
	 * @param stoid
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public void initObjectSecurityToken(String company, String product, String version, int independence, int appid,
			long ts, String auth, String stoid, String secret)
					throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = company;
		this.product = product;
		this.version = version;
		this.num = 1;
		this.independence = independence;

		this.appid = appid;
		this.ts = ts;
		this.auth = auth;

		this.stoid = stoid;

		this.secret = secret;
		this.makeSecurityToken(false);
	}

	/**
	 * 对象存储令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * 
	 * @param appid
	 * @param ts
	 * @param auth
	 * @param stoid
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public void initObjectSecurityToken(String company, String product, String version, int independence, int appid,
			long ts, String auth, String region, String stoid, String secret)
					throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = company;
		this.product = product;
		this.version = version;
		this.num = 1;
		this.independence = independence;

		this.appid = appid;
		this.ts = ts;
		this.auth = auth;
		this.region = region;
		
		this.stoid = stoid;

		this.secret = secret;
		this.makeSecurityToken(true);
	}
	
	/**
	 * 对象存储令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * 
	 * @param appid
	 * @param ts
	 * @param auth
	 * @param stoid
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public void initObjectAppAllAuthIndependenceSecurityToken(int appid, long ts, String secret)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = "syswin";
		this.product = "oss";
		this.version = "1";
		this.num = 1;
		this.independence = 0;

		this.appid = appid;
		this.ts = ts;
		this.auth = "w";

		this.stoid = "";
		this.secret = secret;

		this.makeSecurityToken(false);
	}

	/**
	 * 文件存储令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * @param appid
	 * @param ts
	 * @param auth
	 * @param bucket
	 * @param fileName
	 * @param secret
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */

	public void initFileSecurityToken(String company, String product, String version, int independence, int appid,
			long ts, String auth, String bucket, String fileName, String secret)
					throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = company;
		this.product = product;
		this.version = version;
		this.num = 0;
		this.independence = independence;

		this.appid = appid;
		this.ts = ts;
		this.auth = auth;

		this.bucket = bucket;
		this.fileName = fileName;

		this.secret = secret;
		this.makeSecurityToken(false);
	}

	/**
	 * 文件存储令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * @param appid
	 * @param ts
	 * @param auth
	 * @param bucket
	 * @param fileName
	 * @param secret
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public void initFileSecurityToken(String company, String product, String version, int independence, int appid,
			long ts, String auth, String region, String bucket, String fileName, String secret)
					throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = company;
		this.product = product;
		this.version = version;
		this.num = 0;
		this.independence = independence;

		this.appid = appid;
		this.ts = ts;
		this.auth = auth;
		this.region = region;
		
		this.bucket = bucket;
		this.fileName = fileName;

		this.secret = secret;
		this.makeSecurityToken(true);
	}
	
	/**
	 * 通用存储令牌初始化
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param independence
	 * @param appid
	 * @param ts
	 * @param action
	 * @param paramNameList
	 * @param paramList
	 * @param secret
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public void initCommonSecurityToken(String company, String product, String version, int independence, int appid,
			long ts, String action, List<String> paramNameList, List<String> paramList, String secret)
					throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = company;
		this.product = product;
		this.version = version;
		this.num = 2;
		this.independence = independence;

		this.appid = appid;
		this.ts = ts;
		this.auth = action;

		this.formatParamNameString = "";
		this.formatParamString = "";

		this.paramList = paramList;
		this.paramNameList = paramNameList;

		for (int i = 0; i < paramNameList.size(); i++) {
			if (i >= 1) {
				this.formatParamNameString += SecurityTokenConstants.ParamNamesSeparator;
				this.formatParamString += SecurityTokenConstants.ParamSeparator;
			}
			this.formatParamNameString += this.paramNameList.get(i);
			this.formatParamString += this.paramList.get(i);
		}
		// System.out.println("formatParamNameString:"+formatParamNameString);
		// System.out.println("formatParamString:"+this.formatParamString);
		// System.out.println("paramList:"+this.paramList);
		// System.out.println("paramNameList:"+this.paramNameList);
		this.secret = secret;
		this.makeSecurityToken(false);
	}

	private String securityToken;
	/**
	 * 令牌类型header
	 */
	private String securityTokenType;
	/**
	 * 令牌明文
	 */
	private String plaintext;
	/**
	 * 令牌密文
	 */
	private String secret;

	public String getSecret() {
		return secret;
	}

	public List<String> getParamNameList() {
		return paramNameList;
	}

	/**
	 * 令牌明文
	 */
	/**
	 * 数字签名
	 */
	private String signature;
	/**
	 * 公司
	 */

	private String company = "syswin";
	/**
	 * 产品
	 */
	private String product = "oss";
	/**
	 * 产品版本
	 */

	private String version = "0";
	/**
	 * 令牌类型编号
	 */
	private int num = 1;
	/**
	 * 令牌时间窗口
	 */
	private long ts = System.currentTimeMillis();
	/**
	 * 令牌权限描述 r,w及action名称用","分隔
	 * 
	 */
	private String auth = "r";
	/**
	 * 令牌访问域名
	 * 
	 */
	private String region = "";

	/**
	 * 租户是否独立使用
	 */
	private int independence = 0;
	/**
	 * 租户id
	 */
	private int appid = 0;
	// 文件访问令牌参数
	/**
	 * 文件命名空间
	 */
	private String bucket;
	/**
	 * 文件名
	 */
	private String fileName;
	// 对象访问令牌参数
	/**
	 * 文件存储ID
	 */
	private String stoid;
	/**
	 * 文件存储ID
	 */

	// 通用令牌参数

	/**
	 * 需要签名的参数名称格式化串 如 指定可以访问action1； 限定 参数Param1=1，Param2=2，Param3=3
	 * 那么fomatParamNameString 可以按照如下格式拼装 fomatParamNameString：
	 * Param1,Param2,Param2 fomatParamString: 1\n2\n3
	 */

	private String formatParamNameString;
	/**
	 * 格式化属性串对应如下 这些属性中如果均需先做 Base64SafeUrlUtils.safeUrlEncode安全转换
	 */

	private String formatParamString;
	private List<String> paramNameList;
	private List<String> paramList;

	private void makeSecurityToken(boolean needRegion)
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		this.securityTokenType = "" + this.company + SecurityTokenConstants.SecurityTokenTypeSeparator + this.product
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.version
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.num
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.independence;
		securityToken = "";

		this.plaintext = "";
		this.plaintext += this.securityTokenType + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.appid + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.ts + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.auth + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		if (needRegion) {
			this.plaintext += this.region + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		}
		if (num == 0) {
			this.plaintext += this.bucket + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
			this.plaintext += this.fileName;
		}
		if (num == 1) {
			this.plaintext += this.stoid;
		}
		if (num == 2) {
			this.plaintext += this.formatParamNameString + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
			this.plaintext += this.formatParamString;
		}

		this.signature = Base64SafeUrlUtils
				.safeUrlEncode(HmacSha1Utils.hmacSha1(this.plaintext.getBytes(), secret.getBytes()));
		this.securityToken = this.securityTokenType + SecurityTokenConstants.SecurityTokenSeparator
				+ Base64SafeUrlUtils.safeUrlEncode(this.plaintext) + SecurityTokenConstants.SecurityTokenSeparator
				+ this.signature;

	}

	public void displayString(String secret)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		System.out.println("------------------securityToken:解析-----------------");
		System.out.println("安全令牌 securityToken:\n" + securityToken);

		if (num == 0) {
			System.out.print("    文件存储安全令牌-");
		}

		if (num == 1) {
			System.out.print("    对象存储安全令牌-");
		}

		if (num == 2) {
			System.out.print("    通用安全令牌-");
		}

		if (this.independence == 0) {
			System.out.println("独立令牌");
		}

		if (this.independence == 1) {
			System.out.println("非独立令牌，必须和临时账户一起使用!");
		}

		System.out.println("\n令牌类型 securityTokenType:");
		System.out.println(securityTokenType);
		System.out.println("    company:" + company);
		System.out.println("    version:" + version);
		System.out.println("    num:" + num);
		System.out.println("    independence:" + independence);

		System.out.println("\n令牌明文 plaintext:");
		System.out.println("begin\n" + plaintext + "\nend\n");
		System.out.println("----------------------------------");

		System.out.println("    securityTokenType:" + securityTokenType);
		System.out.println("    appid:" + appid);
		System.out.println("    ts:" + ts);
		System.out.println("    auth:" + auth);
		System.out.println("    region:" + region);

		if (num == 0) {
			System.out.println("    bucket:" + bucket);
			System.out.println("    fileName:" + fileName);

		}
		if (num == 1) {

			System.out.println("    stoid:" + stoid);

		}

		if (num == 2) {
			System.out.println("    formatParamNameString:" + formatParamNameString);
			System.out.println("    formatParamString:\nbegin\n" + formatParamString + "\nend\n");
			System.out.println("    paramNameList:" + paramNameList);
			System.out.println("    paramList:" + paramList);
		}

		System.out.println("----------------------------------");
		String signature = Base64SafeUrlUtils
				.safeUrlEncode(HmacSha1Utils.hmacSha1(this.plaintext.getBytes(), secret.getBytes()));

		System.out.println("\n数字签名 signature:");
		System.out.println("令牌数字签名signature:" + this.signature);
		System.out.println("密文secret:" + secret);
		System.out.println("新算signature:" + signature);

	}

	public String getSecurityToken() {
		return securityToken;
	}

	public int getNum() {
		return num;
	}

	public int getAppid() {
		return appid;
	}

	public String getBucket() {
		return bucket;
	}

	public String getFileName() {
		return fileName;
	}

	public long getTs() {
		return ts;
	}

	public String getAuth() {
		return auth;
	}

	public int getIndependence() {
		return independence;
	}

	public String getSecurityTokenType() {
		return securityTokenType;
	}

	public String getPlaintext() {
		return plaintext;
	}

	public String getSignature() {
		return signature;
	}

	public String getCompany() {
		return company;
	}

	public String getProduct() {
		return product;
	}

	public String getVersion() {
		return version;
	}

	public List<String> getParamList() {
		return paramList;
	}

	public String getRegion() {
		return region;
	}

	public String getStoid() {
		return stoid;
	}

	public String getFormatParamNameString() {
		return formatParamNameString;
	}

	public String getFormatParamString() {
		return formatParamString;
	}

	public boolean existRegion(){
		List<String> plaintextList = StringUtil.split(plaintext,
				SecurityTokenConstants.SecurityTokenPlaintextSeparator);
		int plaintextLen = plaintextList.size();
		if (this.num == 0 && plaintextLen > 6) {
			return true;
		}
		if (this.num == 1 && plaintextLen > 5) {
			return true;
		}
		return false;
	}
}