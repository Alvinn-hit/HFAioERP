package test;

import javax.crypto.*;  
import javax.crypto.spec.DESKeySpec;  
 
import java.security.NoSuchAlgorithmException;  
import java.security.InvalidKeyException;  
import java.security.SecureRandom;  
import java.security.spec.InvalidKeySpecException;  
 
/**  
* ͨ��DES���ܽ���ʵ��һ��String�ַ����ļ��ܺͽ���.  
*   
* @author badpeas  
*   
*/ 
public class testDes {  
 
    public static void main(String[] args) throws NoSuchAlgorithmException,  
            InvalidKeyException, NoSuchPaddingException,  
            InvalidKeySpecException, IllegalBlockSizeException,  
            BadPaddingException {  
    	
    	//md5�������
    	//Get:502BEDFED20EC5E97279861822C7DF05

    	//Get:F0CB8D9E72AE6589121926B8C2677FA5
//    	byte[] bs= new byte[16] ;
//    	bs =hexStringToBytes("F0CB8D9E72AE6589121926B8C2677FA5");
//    	for(int i=0;i<bs.length ;i++){
//    		bs[i] = (byte)(bs[i]-4000);
//    	}
//    	System.out.println(bytesToHexString(bs));
    	
    	
        // 1.1 >>> ����Ҫ����һ���ܳ�  
        // DES�㷨Ҫ����һ�������ε������Դ  
//        SecureRandom sr = new SecureRandom();  
//        // Ϊ����ѡ���DES�㷨����һ��KeyGenerator����  
//        KeyGenerator kg = KeyGenerator.getInstance("DES");  
//        kg.init(sr);  
//        // �����ܳ�  
//        SecretKey key = kg.generateKey();  
        // ��ȡ�ܳ�����  
        //byte rawKeyData[] = key.getEncoded();  
    	
        byte rawKeyData[] = "Kr.AIO00".getBytes();  
 
        String str = "123456"; // ����������  
       
        System.out.println("���ĳ���===>" + str.length());
        // 2.1 >>> ���ü��ܷ���  
        byte[] encryptedData = encrypt(rawKeyData, str);  
        System.out.println("���ĳ���===>" + encryptedData.length);
        System.out.println("����HEX===>" + bytesToHexString(encryptedData));
        // 3.1 >>> ���ý��ܷ���  
        decrypt(rawKeyData, encryptedData);  
    }  
 
    /**  
    * ���ܷ���  
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
        // DES�㷨Ҫ����һ�������ε������Դ  
        SecureRandom sr = new SecureRandom();  
        // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����һ��SecretKey����  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        // Cipher����ʵ����ɼ��ܲ���  
        Cipher cipher = Cipher.getInstance("DES");  
        // ���ܳ׳�ʼ��Cipher����  
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        // ���ڣ���ȡ���ݲ�����  
        byte data[] = str.getBytes();  
        byte ndata[] = new byte[data.length +1];
        System.arraycopy(data,0, ndata, 0, data.length);
        ndata[ndata.length -1] = 0;
        // ��ʽִ�м��ܲ���  
        byte[] encryptedData = cipher.doFinal(ndata);  
        System.out.println("���ܺ�Hex===>" + bytesToHexString(encryptedData));
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
    * ���ܷ���  
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
        // DES�㷨Ҫ����һ�������ε������Դ  
        SecureRandom sr = new SecureRandom();  
        // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����  
        DESKeySpec dks = new DESKeySpec(rawKeyData);  
        // ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����һ��SecretKey����  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey key = keyFactory.generateSecret(dks);  
        // Cipher����ʵ����ɽ��ܲ���  
        Cipher cipher = Cipher.getInstance("DES");  
        // ���ܳ׳�ʼ��Cipher����  
        cipher.init(Cipher.DECRYPT_MODE, key, sr);  
        // ��ʽִ�н��ܲ���  
        byte decryptedData[] = cipher.doFinal(encryptedData);  
        System.out.println("���ܺ�===>" + new String(decryptedData));  
        return new String(decryptedData);  
    }  
 
}  
