package report;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.menyi.web.util.BaseEnv;

public class CoderTools{
	public static String KEY_ALGORTHM="DES";//
    public static String SIGNATURE_ALGORITHM="MD5withRSA";   
    
  //算法名称/加密模式/填充方式 
    //DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    //public static String CIPHER_ALGORITHM = "desede/CBC/NoPadding";    
    public static String CIPHER_ALGORITHM = "desede/CBC/PKCS5Padding";
    //****根据参数获取key值****//
    public static Key getKey(String strKey){
    	Key key = null;
    	KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance(KEY_ALGORTHM);		
	    	keyGen.init(new SecureRandom(strKey.getBytes()));
	    	key = keyGen.generateKey();
	    	keyGen = null;
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
    	return key;
    }
    
    //****初始化3DES密钥****//
    public static Key initDESKey(String strKey){
    	Key key = null;
    	byte[] input = HexString2Bytes(strKey);
    	try {
			DESedeKeySpec keySpec = new DESedeKeySpec(input);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORTHM);
    		return ((Key)(keyFactory.generateSecret((java.security.spec.KeySpec)keySpec)));			
    	} catch (Exception e) {			
			e.printStackTrace();
		}
    	return key;    	
    }
    
    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
    
    public static byte[] HexString2Bytes(String hexStr){
    	byte[] b = new byte[hexStr.length()/2];
    	int j = 0;
    	for(int i=0;i<b.length;i++){
    		char c0 = hexStr.charAt(i);
    		char c1 = hexStr.charAt(i+1);
    		b[i] = (byte) ((parse(c0) << 4 ) | parse(c1));
    	}
    	return b;
    }
    //******3DES加密*******//
    public static byte[] get3DESEncodeByte(byte[] data,String keyStr){
    	Key key = initDESKey(keyStr);
    	byte[] keyiv = {1,2,3,4,5,6,7,8};
    	byte[] bOut = null;
    	try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec  ips =  new IvParameterSpec(keyiv);			
			cipher.init(Cipher.ENCRYPT_MODE,key,ips);
			bOut = cipher.doFinal(data);			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return bOut;
    }
    
    //******3DES解密******//
    public static byte[] get3DESDecodeByte(byte[] data,String keyStr){
    	Key key = initDESKey(new String(keyStr));
    	byte[] keyiv = {1,2,3,4,5,6,7,8};
    	byte[] bOut = null;
    	try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			IvParameterSpec  ips =  new IvParameterSpec(keyiv);			
			cipher.init(Cipher.DECRYPT_MODE,key,ips);
			bOut = cipher.doFinal(data);			
		} catch (Exception e) {			
			e.printStackTrace();
		}
    	return bOut;
    }       
}
