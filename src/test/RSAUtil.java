package test;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.io.*;
import java.math.BigInteger;
/**
* RSA 工具类。提供加密，解密，生成密钥对等方法。
* 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
*
*/
public class RSAUtil {
	
	public static void main(String[] args){
		RSAUtil t = new RSAUtil();
		try {
			t.PublicEnrypt();
			
			t.privateDecrypt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* 
	 * 公钥加密 
	 */  
	private static void PublicEnrypt()throws Exception {  
	    Cipher cipher =Cipher.getInstance("RSA");  
	    //实例化Key  
	    KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");  
	    keyPairGenerator.initialize(512);
	    //获取一对钥匙  
	    KeyPair keyPair=keyPairGenerator.generateKeyPair();  
	    //获得公钥  
	    RSAPublicKey publicKey=(RSAPublicKey)keyPair.getPublic();  
	    //获得私钥   
	    Key privateKey=keyPair.getPrivate();  
	    //用公钥加密  
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
	    byte [] result=cipher.doFinal("传智播客".getBytes("UTF-8"));  
	    //将Key写入到文件  
	    saveKey(privateKey,"zxx_private.key");  
	    //加密后的数据写入到文件  
	    saveData(result,"public_encryt.dat");  
	}  
	  
	/* 
	 * 私钥解密 
	 */  
	private static void privateDecrypt() throws Exception {  
	    Cipher cipher=Cipher.getInstance("RSA");  
	    //得到Key  
	    Key privateKey=readKey("zxx_private.key");  
	    //用私钥去解密  
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	    //读数据源  
	    byte [] src =readData("public_encryt.dat");  
	    //得到解密后的结果  
	    byte[] result=cipher.doFinal(src);  
	    //二进制数据要变成字符串需解码  
	    System.out.println(new String(result,"UTF-8"));  
	}  
	  
	private static void saveData(byte[] result, String fileName) throws Exception {  
	    // TODO Auto-generated method stub  
	    FileOutputStream fosData=new FileOutputStream(fileName);  
	    fosData.write(result);  
	    fosData.close();  
	}  
	public static void saveKey(Key key,String fileName)throws Exception{  
	    FileOutputStream fosKey=new FileOutputStream(fileName);  
	    ObjectOutputStream oosSecretKey =new ObjectOutputStream(fosKey);  
	    oosSecretKey.writeObject(key);  
	    
	    oosSecretKey.close();  
	    fosKey.close();  
	}  
	private static Key readKey(String fileName) throws Exception {  
	    FileInputStream fisKey=new FileInputStream(fileName);  
	    ObjectInputStream oisKey =new ObjectInputStream(fisKey);  
	    Key key=(Key)oisKey.readObject();  
	    oisKey.close();  
	    fisKey.close();  
	    return key;  
	}  
	private static byte[] readData(String filename) throws Exception {  
	    FileInputStream fisDat=new FileInputStream(filename);  
	    byte [] src=new byte [fisDat.available()];  
	    int len =fisDat.read(src);  
	    int total =0;  
	    while(total<src.length){  
	        total +=len;  
	        len=fisDat.read(src,total,src.length-total);  
	    }  
	    fisDat.close();  
	    return src;  
	}  
}
