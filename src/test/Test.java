package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.web.util.CallSoftDll;
import com.menyi.web.util.GlobalsTool;

public class Test {

	public static void main(String[] args){
		String a="abc";
		System.out.println(a.substring(0,a.length()-1));
//		String sentence  ="from tblBuyApplication join tblBuyApplicationDet on tblBuyApplication.id=tblBuyApplicationDet.f_ref "
//				+ "join tblBuyQuote on 1=1 and tblBuyQuote.CompanyCode = '@ValueofDB:CompanyCode'  and tblBuyQuote.workFlowNodeName='finish' ";
//		String param = "@ValueofDB:CompanyCode";
//		int paramIndex = sentence.indexOf(param)+param.length();
//		String d=sentence.substring(0,paramIndex);
//  	  Pattern pattern = Pattern.compile("(and|or|\\()[\\s]*[\\w.]+[\\s]*=[\\s]*[']?"+param, Pattern.CASE_INSENSITIVE);
//		  Matcher matcher = pattern.matcher(d);
//		  if (matcher.find()) {
//				String all = matcher.group(0);
//				int mpos = matcher.start() ; 
//				System.out.println(all);
//				System.out.println(mpos);
//		  }else{
//			  throw new RuntimeException("弹出窗语句在去掉条件"+param+"时不能正确解释,请在该条件前加 1=1 and");
//		  }
		
//		System.out.println("aRES<oa.mail.msg.newMail>b".replaceAll("RES<oa.mail.msg.newMail>", "您有新的邮件"));
//		
//		Date standDate;
//		try {
//			standDate = BaseDateFormat.parse("2015-06-06 00:00:00", "yyyy-MM-dd HH:mm:ss");
//			System.out.println(standDate.compareTo(new Date())); 
//		} catch (IllegalArgumentException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		
//		System.out.println((int)2.5);
//		
//		DBFieldInfoBean bean = new DBFieldInfoBean();
//		Field[] fds = bean.getClass().getDeclaredFields();
//		for(Field fd:fds){
//			if(!Modifier.isFinal(fd.getModifiers())){
//				try {
//					String name = fd.getName();
//					Method md =bean.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1), null);
//					
//					System.out.println(md.invoke(bean, null));
//				}catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		try{
//		MessageDigest md5=MessageDigest.getInstance("MD5"); 
//		String newstr=bytesToHexString(md5.digest("KoronKoron00-26-6C-55-D1-77  E8-39-DF-05-D5-E9  KoronBFEBFBFF00020652-0000000000000000Koron".getBytes("utf-8"))); 
//		System.out.print(newstr);
//		}catch(Exception e){}
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
}
