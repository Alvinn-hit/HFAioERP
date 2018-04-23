package test;

import javax.crypto.*;  
import javax.crypto.spec.DESKeySpec;  
 
import java.security.NoSuchAlgorithmException;  
import java.security.InvalidKeyException;  
import java.security.SecureRandom;  
import java.security.spec.InvalidKeySpecException;  
 
/**  
* 通过DES加密解密实现一个String字符串的加密和解密.  
*   
* @author badpeas  
*   
*/ 
public class testDes {  
 
    public static void main(String[] args) throws NoSuchAlgorithmException,  
            InvalidKeyException, NoSuchPaddingException,  
            InvalidKeySpecException, IllegalBlockSizeException,  
            BadPaddingException {  
    	
    	//md5反向测试
    	//Get:502BEDFED20EC5E97279861822C7DF05

    	//Get:F0CB8D9E72AE6589121926B8C2677FA5
//    	byte[] bs= new byte[16] ;
//    	bs =hexStringToBytes("F0CB8D9E72AE6589121926B8C2677FA5");
//    	for(int i=0;i<bs.length ;i++){
//    		bs[i] = (byte)(bs[i]-4000);
//    	}
//    	System.out.println(bytesToHexString(bs));
    	
    	
        // 1.1 >>> 首先要创建一个密匙  
        // DES算法要求有一个可信任的随机数源  
//        SecureRandom sr = new SecureRandom();  
//        // 为我们选择的DES算法生成一个KeyGenerator对象  
//        KeyGenerator kg = KeyGenerator.getInstance("DES");  
//        kg.init(sr);  
//        // 生成密匙  
//        SecretKey key = kg.generateKey();  
        // 获取密匙数据  
        //byte rawKeyData[] = key.getEncoded();  
    	
        byte rawKeyData[] = "Kr.AIO00".getBytes();  
 
        String str = "123456"; // 待加密数据  
       
        System.out.println("明文长度===>" + str.length());
        // 2.1 >>> 调用加密方法  
        byte[] encryptedData = encrypt(rawKeyData, str);  
        System.out.println("密文长度===>" + encryptedData.length);
        System.out.println("明文HEX===>" + bytesToHexString(encryptedData));
        // 3.1 >>> 调用解密方法  
        decrypt(rawKeyData, encryptedData);  
    }  
 
    /**  
    * 加密方法  
    *   
    * @param rawKeyData  
    * @param str  
    * @return  
    * @throws InvalidKeyException  
    * @throws NoSuchAlgorithmException  
    * @throws IllegalBlockSizeException  
    * @throws BadPaddingException  
    * @throws NoSuchPaddingException  
    * @throws InvalidKeySpecException  
    */ 
    public static byte[] encrypt(byte rawKeyData[], String str)  
            throws InvalidKeyException, NoSuchAlgorithmException,  
            IllegalBlockSizeException, BadPaddingException,  
            NoSuchPaddingException, InvalidKeySpecException {  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建一个DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance("DES");  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        // 现在，获取数据并加密  
        byte data[] = str.getBytes();  
        byte ndata[] = new byte[data.length +1];
        System.arraycopy(data,0, ndata, 0, data.length);
        ndata[ndata.length -1] = 0;
        // 正式执行加密操作  
        byte[] encryptedData = cipher.doFinal(ndata);  
        System.out.println("加密后Hex===>" + bytesToHexString(encryptedData));
        return encryptedData;  
    }  
    
    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString().toUpperCase();  
    } 
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    } 
    private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    } 
    /**  
    * 解密方法  
    *   
    * @param rawKeyData  
    * @param encryptedData  
    * @throws IllegalBlockSizeException  
    * @throws BadPaddingException  
    * @throws InvalidKeyException  
    * @throws NoSuchAlgorithmException  
    * @throws NoSuchPaddingException  
    * @throws InvalidKeySpecException  
    */ 
    public static String decrypt(byte rawKeyData[], byte[] encryptedData)  
            throws IllegalBlockSizeException, BadPaddingException,  
            InvalidKeyException, NoSuchAlgorithmException,  
            NoSuchPaddingException, InvalidKeySpecException {  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom sr = new SecureRandom();  
        // 从原始密匙数据创建一个DESKeySpec对象  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance("DES");  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, key, sr);  
        // 正式执行解密操作  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        System.out.println("解密后===>" + new String(decryptedData));  
        return new String(decryptedData);  
    }  
 
}  
