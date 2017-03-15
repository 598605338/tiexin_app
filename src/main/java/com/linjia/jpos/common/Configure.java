package com.linjia.jpos.common;

import com.linjia.wxpay.common.MD5;
import com.linjia.wxpay.common.Util;

/**
 * User: lxl
 * Date: 2016/09/21
 * Time: 14:40
 * 这里放置JPOS各种配置数据
 */
public class Configure {

	// JPOS账户ID
	private static String accountID = "ljdjfad87sad752jndfsh8s6dsf";

	// JPOS账户Secret
	private static String accountSecret = "lj978jd2kjk8s6dj2dls7522lfsd";
	
	

	public static String getAccountID() {
		return accountID;
	}

	public static void setAccountID(String accountID) {
		Configure.accountID = accountID;
	}

	/**
	 * 取得签名(SignParam加密方式门店号+账号+密钥 MD5加密32位大写)
	 * lixinling 
	 * 2016年9月21日 下午3:21:21
	 * @param storeNumber
	 * @return
	 * @throws IllegalAccessException
	 */
	public static String getSign(String storeNumber) {
		String result = storeNumber + accountID + accountSecret;

		String sign = MD5.MD5Encode(result).toUpperCase();
		Util.log("Sign Result:" + sign);
		return sign;
	}

	/**
	 * 验证签名的正确
	 * lixinling 
	 * 2016年9月21日 下午3:31:49
	 * @param storeNumber
	 * @param signParam
	 * @return
	 * @throws IllegalAccessException
	 */
	public static boolean checkSign(String storeNumber, String signParam){

		String sign = getSign(storeNumber);

		if (!sign.equals(signParam)) {
			// 签名验不过，表示这个API请求的数据有可能已经被篡改了
			Util.log("API请求的数据签名验证不通过，有可能被第三方篡改!!!");
			return false;
		}
		Util.log("恭喜，API返回的数据签名验证通过!!!");
		return true;
	}
}
