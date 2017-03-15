package com.linjia.wxpay.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

import com.linjia.tools.Tools;
import com.mysql.jdbc.util.Base64Decoder;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/** 
 * AES加解密
 * @author  lixinling: 
 * @date 2016年11月8日 下午7:39:15 
 * @version 1.0 
*/
public class AES {

	/**
	 * AES加密
	 * lixinling 
	 * 2016年11月8日 下午7:48:45
	 * @param content 待价密的内容
	 * @param encryptKey 加密秘钥
	 * @return
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(encryptKey.getBytes()));
			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
			return cipher.doFinal(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES 加密
	 * lixinling 
	 * 2016年11月8日 下午7:52:24
	 * @param content
	 * @param encryptKey
	 * @return
	 */
	public static String aesEncrypt(String content, String encryptKey) throws UnsupportedEncodingException {
		return parseByte2HexStr(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * AES解密
	 * lixinling 
	 * 2016年11月8日 下午8:05:02
	 * @param encryBytes 待解密的byte[] 
	 * @param decryptKey 解密密钥 
	 * @return
	 */
	public static byte[] aesDecryptByBytes(byte[] encryBytes, String decryptKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(decryptKey.getBytes()));

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

			byte[] decryptBytes = cipher.doFinal(encryBytes);

			return decryptBytes;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES解密 
	 * lixinling 
	 * 2016年11月8日 下午8:31:49
	 * @param encryptStr
	 * @param decryptKey
	 * @return
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws IOException {
		return new String(aesDecryptByBytes(parseHexStr2Byte(encryptStr), decryptKey));
	}

	/**
	 * 二进制转换成十六进制
	 * lixinling 
	 * 2016年11月9日 上午9:26:56
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1)
				hex = '0' + hex;
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 十六进制转换成二进制
	 * lixinling 
	 * 2016年11月9日 上午9:26:56
	 * @param buf
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
