package com.ibeacon.utils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密介绍 DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，
 * 后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，
 * 24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现 。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public abstract class DesEncrypt {

	// 测试
	public static void main(String args[]) throws Exception {
		// // 待加密内容
		// String str = "030000000081";
		// // 密码，长度要是8的倍数
		// String password = "12345678";
		//
		// byte[] result = DesEncrypt.encrypt(str.getBytes(), password);
		// System.out.println("加密后：" + new String(result));
		//
		// // 直接将如上内容解密
		// byte[] decryResult = DesEncrypt.decrypt(result, password);
		// System.out.println("解密后：" + new String(decryResult));

		String mac = "404c496806000025";
		String data = "030000000081";

		// 密码，长度要是8的倍数
		String password = "12345678";

		byte[] macResult = DesEncrypt.encrypt(mac.getBytes(), password);
		byte[] dataResult = DesEncrypt.encrypt(data.getBytes(), password);

		// 发送参数
		String cmd = new String(macResult) + ":" + new String(dataResult);

		Map<String, String> params = new HashMap<String, String>();
		params.put("cmd", cmd);

		System.out.println("发送数据:" + cmd);

		String returndata = HttpUtils.post(
				"http://120.236.155.86:10020/comm/postresponse", params, 2000);

		// 判断成功还是失败
		if (returndata.equals("ok")) {
			System.out.println("发送数据成功:" + returndata);
		} else {
			System.out.println("发送数据失败:" + returndata);
		}
	}

	/**
	 * 加密
	 *
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] datasource, String password)
			throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(datasource);
	}

	/**
	 * 解密
	 *
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}
}