package com.thank.rest.resources;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.jersey.core.util.Base64;
import com.thank.common.model.UserInfo;
import com.thank.rest.shared.model.WFRestException;
/**
 * Context util to wrap implementation of context mgmt
 * Token based
 * @author fenwang
 *
 */
public class UserContextUtil {
	private static final String SESSION_CUR_USER="CUR_USER";
	
	private static final String CIPHER_TRANSFORMATION = "DES/CBC/PKCS5Padding";
	private static final String KEY_ALGORITHM = "DES";
	private static final String SEPERATOR=":";
	private static IvParameterSpec ivSpec;
	private static Key encryptionKey;
	private static Key decryptionKey;

	static {
		try {
			String keySeed = "L7rgMRVetdM=";
			encryptionKey = new SecretKeySpec(Base64.decode(keySeed), KEY_ALGORITHM);
			decryptionKey = new SecretKeySpec(encryptionKey.getEncoded(), encryptionKey.getAlgorithm());

			byte[] ivBytes = new byte[] { 0x03, 0x02, 0x01, 0x00, 0x05, 0x04, 0x07, 0x06 };

			ivSpec = new IvParameterSpec(ivBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args ) {
		UserInfo user=new UserInfo();
		user.setName("fenwang");
		user.setEmailAddress("fenwang@ebay.com");
		String token=UserContextUtil.generateAuthToken(user);
		System.out.println(token);
		user=UserContextUtil.getUserFromAuthToken(token);
		System.out.println(user.toJson());
	}
	public static UserInfo getUserFromAuthToken(String token) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			byte[] cipherText = Base64.decode(token);
			cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
			byte[] plainText = new byte[cipher.getOutputSize(cipherText.length)];
			int ptLength = cipher.update(cipherText, 0, cipherText.length, plainText, 0);
			cipher.doFinal(plainText, ptLength);			
			String authzString = new String(plainText, "UTF-8");
			int colon=authzString.indexOf(":");			
			long expireTime=Long.parseLong(authzString.substring(0, colon));
		
			if(expireTime<System.currentTimeMillis()) return null;
			
			return UserInfo.fromJson(authzString.substring(colon+1));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateAuthToken(UserInfo user)  {
		String expireTime = (System.currentTimeMillis()-24*3600*1000) + ":";
		
		String inputStr = new StringBuffer(expireTime).append(user.toJson()).toString();

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			byte[] input = inputStr.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec);
			byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
			int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
			cipher.doFinal(cipherText, ctLength);
			return new String(Base64.encode(cipherText),"UTF-8");
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	public static void authenticate(HttpServletRequest request) {
		HttpSession session=request.getSession();
		Object ret=session.getAttribute(SESSION_CUR_USER);
		if(ret==null || !(ret instanceof UserInfo) ) {
			throw new WFRestException(401,"Not login yet");
		} 
	}
	public static UserInfo getCurUser(HttpSession session) {
		
		if(session==null) return null;
		Object ret=session.getAttribute(SESSION_CUR_USER);
		if(ret==null || !(ret instanceof UserInfo) ) {
			return null;
		} else {
			return (UserInfo) ret;
		}
	}

	public static UserInfo getCurUser(HttpServletRequest request) {
		HttpSession session=request.getSession();
		return getCurUser(session);	
	}
	public static void saveInSession(HttpServletRequest request,UserInfo user) {
		HttpSession session=request.getSession();
		session.setAttribute(SESSION_CUR_USER, user);
	}
	public static void logout(HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.invalidate();
	}
		
}
