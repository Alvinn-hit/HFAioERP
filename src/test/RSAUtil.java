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
* RSA �����ࡣ�ṩ���ܣ����ܣ�������Կ�Եȷ�����
* ��Ҫ��http://www.bouncycastle.org����bcprov-jdk14-123.jar��
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
	 * ��Կ���� 
	 */  
	private static void PublicEnrypt()throws Exception {  
	    Cipher cipher =Cipher.getInstance("RSA");  
	    //ʵ����Key  
	    KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");  
	    keyPairGenerator.initialize(512);
	    //��ȡһ��Կ��  
	    KeyPair keyPair=keyPairGenerator.generateKeyPair();  
	    //��ù�Կ  
	    RSAPublicKey publicKey=(RSAPublicKey)keyPair.getPublic();  
	    //���˽Կ   
	    Key privateKey=keyPair.getPrivate();  
	    //�ù�Կ����  
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
	    byte [] result=cipher.doFinal("���ǲ���".getBytes("UTF-8"));  
	    //��Keyд�뵽�ļ�  
	    saveKey(privateKey,"zxx_private.key");  
	    //���ܺ������д�뵽�ļ�  
	    saveData(result,"public_encryt.dat");  
	}  
	  
	/* 
	 * ˽Կ���� 
	 */  
	private static void privateDecrypt() throws Exception {  
	    Cipher cipher=Cipher.getInstance("RSA");  
	    //�õ�Key  
	    Key privateKey=readKey("zxx_private.key");  
	    //��˽Կȥ����  
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	    //������Դ  
	    byte [] src =readData("public_encryt.dat");  
	    //�õ����ܺ�Ľ��  
	    byte[] result=cipher.doFinal(src);  
	    //����������Ҫ����ַ��������  
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
