package com.syswin.temail.media.bank.utils.stoken;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 
 * @author admin 云存储安全令牌
 */
public class SramSecurityToken {

	public SramSecurityToken(String securityToken) throws UnsupportedEncodingException {
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

		this.account = plaintextList.get(1);
		this.ts = Long.valueOf(plaintextList.get(2));
		this.auth = plaintextList.get(3);
	}


	public SramSecurityToken() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * SRAM令牌
	 * 
	 * @param company
	 * @param product
	 * @param version
	 * @param num
	 * @param independence
	 * 
	 * @param ts
	 * @param auth
	 * @param account
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public void initSramSecurityToken(String company, String product, String version, int independence, long ts,
			String auth, String account, String secret)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

		this.company = "syswin";
		this.product = "SRAM";
		this.version = "1";
		this.num = 3;
		this.independence = 0;

		this.ts = ts;
		this.auth = "w";

		this.account = account;
		this.secret = secret;
		this.makeSecurityToken();
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
	 * 密钥对 账号
	 */
	private String account;
	/**
	 * 密钥对 密码
	 */
	private String secret;
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getSecret() {
		return secret;
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
	private String product = "SRAM";
	/**
	 * 产品版本
	 */

	private String version = "1";
	/**
	 * 令牌类型编号
	 */
	private int num = 3;
	/**
	 * 令牌时间窗口
	 */
	private long ts = System.currentTimeMillis();
	/**
	 * 令牌权限描述 r,w及action名称用","分隔
	 * 
	 */
	private String auth = "w";
	/**
	 * 租户是否独立使用
	 */
	private int independence = 0;


	private void makeSecurityToken()
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		this.securityTokenType = "" + this.company + SecurityTokenConstants.SecurityTokenTypeSeparator + this.product
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.version
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.num
				+ SecurityTokenConstants.SecurityTokenTypeSeparator + this.independence;
		securityToken = "";

		this.plaintext = "";
		this.plaintext += this.securityTokenType + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.account + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.ts + SecurityTokenConstants.SecurityTokenPlaintextSeparator;
		this.plaintext += this.auth + SecurityTokenConstants.SecurityTokenPlaintextSeparator;

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
		
		if (num == 3) {
			System.out.print("    SRAM专用令牌-");
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
		System.out.println("    ts:" + ts);
		System.out.println("    auth:" + auth);

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

	public static void main(String[] args)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		SramSecurityToken objectSecurityToken = new SramSecurityToken();

		// objectSecurityToken.initObjectSecurityToken(company, product,
		// version, independence, appid, ts, auth, stoid, secret);
		//
		// objectSecurityToken.setNum(1);
		// objectSecurityToken.setAppid(1001);
		// objectSecurityToken.setTs(1481622542651l);
		// objectSecurityToken.setAuth("w");
		// objectSecurityToken.setStoid("9f8k2PPXnKrYUBllkAwcGaLR1sK2oTondT8GEYNDiQ4fF");
		// objectSecurityToken.setSecret("123");
		// objectSecurityToken.makeSecurityToken();
		// System.out.println(objectSecurityToken.getSecurityToken());
		//
		// String stoken =
		// "syswin-oss-0-1-0:c3lzd2luLW9zcy0wLTEtMAoxMDAxCjE0ODE2MjI1NDI2NTEKdwo5ZjhrMlBQWG5LcllVQmxsa0F3Y0dhTFIxc0syb1RvbmRUOEdFWU5EaVE0ZkY=:yOKlTxvr1EdGzbtuUiwRCqTHG7M=";
		// stoken=objectSecurityToken.getSecurityToken();
		//
		// SecurityToken securityToken2 = new SecurityToken(stoken);
		//
		// securityToken2.displayString("123");
	}
}
