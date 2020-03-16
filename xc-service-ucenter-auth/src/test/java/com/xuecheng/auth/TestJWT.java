package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/12 - 19:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJWT {
	// 创建jwt令牌
	@Test
	public void test() {
		// 密钥库文件
		String keystore = "xc.keystore";
		// 密钥库的密码
		String keystore_password = "xuechengkeystore";
		// 密钥库文件路径
		ClassPathResource classPathResource = new ClassPathResource(keystore);
		// 密钥的别名
		String alias = "xckey";
		// 密钥的访问密码
		String key_password = "xuecheng";
		// 创建密钥工厂
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());
		// 密钥对 （公钥和私钥）
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
		// 拿到私钥
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		// jwt令牌的内容
		Map<String, String> body = new HashMap<>();
		body.put("name", "cl");
		String bodyStr = JSON.toJSONString(body);
		// 生成jwt令牌
		Jwt jwt = JwtHelper.encode(bodyStr, new RsaSigner(rsaPrivateKey));
		// 生成jwt令牌编码
		String encoded = jwt.getEncoded();
		System.out.println(encoded);
		//eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiY2wifQ.KsQ3yxPW2Ajqm_U5O9z5ogp2xtodu9nId4w8_mbmQIfxhqBfOBiXNGNArBQxfSKwI4GN-ytiVsq4LISD_fQLto76KL8XR7V-ZWA5fM8vZ1yBH6OibNkU8NXkFLbPuGx1Asp7BAUzKWuET0Cx-GPYbH-eKHkfjTqPFq7DPL0bc3ismoGRBdnrWeYyj1dmufIyTodQ6ip5QDDsPmzGeeI1e8o8eGDJ_2ErR_FCAGVW8Xz2N3CYlAgeRSkkc1ua6SSp0BUMnAEyy29QSWTAnsFv9XpxTIaclB1_b1SF0wxF0_oRe0qP537UsGVRAt6Iw7ZQ02TDqRZ1oiRKP0QiWS2yEQ
	}

	// 检验jwt令牌
	@Test
	public void test2() {
		// 公钥
		String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnFepqVSP2sBoKkAj+3vTO9MBvJKnf9e4XFs2hjV01ezjkUjdShbsBx1whgKi72C2PycduGj2fOkovDQTqi3/DBIvyFsC412EUGruZEPUKtoUUKqHPWYl+qRiT1h3tOprkvAHi5ej+j80w6A0cTcQn2BWhoJK4QbrlveXkHBK1tHHqQ40rQsWe+qcXbiXRn4oYlxVB2Gml417UKykbGauBbI/acjONemwhXKDFptoZxha/e6zfZ4qavRC0W+7ohBO/O/NorP/YDOXZ/6ZlPmVs+rRlJoXzzPpC83qlJ7a9IcyK2c5JewzptXyaUfSFMPAuoKgEbSnqvlNJV4zQX2CWQIDAQAB-----END PUBLIC KEY-----";
		// jwt令牌
		String encoded = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1ODQyOTc5MzAsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6ImUzMGJmZDY1LWYwNjYtNGIxMS04MDQ5LWM5ZTI1YTdiZGNjOCIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.k3e_vZz96Xy9mrJKwEO7MfMmOBZLTyVfXTJ3YqviXEMoNPKPI5oOmZQH0njM1JsfskxRSOeML_ljhqk5pupyyZCnwSznQ0mjhGf5ttaCAnj77EFadoay8jKW_KKZwGQta2jv06YafhPgs5-xqnXfw6OTIKtvyY2o-CGEd_7tm0f1OwYGSD82IR6q1swY0iIlZplew9Nz6JDCVFhJgS9a8eKrSTC7IoSZj0OmYxIAzyTfPjGMTsr8S6lY63nE1lkfh0kQDZRCGAoLg73t6Ol2-HO5B2B5wYLkWa32lI3qlvccMgRveJ5ej9TWsta_zNRBc6H8ulK6s-VFmHAV7KmPVA";
		// 校验jwt令牌
		Jwt jwt = JwtHelper.decodeAndVerify(encoded, new RsaVerifier(publickey));
		// 拿到jwt令牌自定义的内容
		String claims = jwt.getClaims();
		System.out.println(claims); //{"name":"cl"}
	}
}
