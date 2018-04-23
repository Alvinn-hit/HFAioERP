package cn.tzw;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.httpclient.params.HttpClientParams;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.tempuri.ArrayOfInt;
import org.tempuri.ArrayOfString;
import org.tempuri.super_website_javaClient;
import org.tempuri.super_website_javaSoap;
import org.tempuri.mfmi_javaClient;
import org.tempuri.mfmi_javaSoap;

import cn.dns4.api.oa.TZOAClient;
import cn.dns4.api.oa.TZOASoap;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.email.EMailMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.ByteArrayDataSource;
import com.menyi.email.util.EMailMessage;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * ��������Ʒ�ӿ�
 * @author Administrator
 *
 */
public class Einterface {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	
	
	/**
	 * �û���ͨ
	 * @param token
	 * @param procuct
	 * @param startDate
	 * @param endDate
	 * @param agentName
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result setFunctionalModule(String token,String loginName,String procuct,String state, 
			String packageStartDate, String packageEndDate, String agentName,String attributeJson){
		Result rs = new Result();
		try{
			
			BaseEnv.log.debug("�ӿڵ��� setFunctionalModule ������token="+token+",loginName ="+loginName+","
					+ "procuct ="+procuct+",state ="+state+",packageStartDate ="+packageStartDate+",packageEndDate ="+packageEndDate
					+",agentName ="+agentName+",attributeJson ="+attributeJson);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.setFunctionalModule(token, loginName, procuct, Integer.parseInt(state), packageStartDate, packageEndDate, agentName, attributeJson);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� setFunctionalModule ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� setFunctionalModule ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	
 
	
	/**
	 * ��ȡ/����û���Ϣ
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result getUserInfo(Connection conn,String token,String loginName,HashMap retMap){
		Result rs = new Result();		  
		try{
			BaseEnv.log.debug("�ӿڵ��� getUserInfo ������token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
			TZOASoap service = client.getTZOASoap(); 
	        String rstr =service.getUserInfo(token, loginName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� getUserInfo ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	
	        	reMap.put("materialCount", (int)Double.parseDouble(reMap.get("materialCount").toString()));   
	        	retMap.put("icompanyName", reMap.get("cpName"));
	        	retMap.put("cpShortName", reMap.get("cpShortName"));
	        	String cpAddressName = reMap.get("cpAddressName")+"";
	        	
	        	if(cpAddressName == null  || cpAddressName.indexOf(">>")== -1){
	        		rs.retCode =ErrorCanst.DEFAULT_FAILURE;
		    		rs.retVal = "������վͨ�ӿڷ��ص�����"+cpAddressName+"��ʽ����ȷ";
		    		return rs;
	        	}else{
//	        		String Province = cpAddressName.substring(cpAddressName.indexOf(">>")+2,cpAddressName.lastIndexOf(">>"));
//	        		String City = cpAddressName.substring(cpAddressName.lastIndexOf(">>")+2);
//	        		
//	        		String sql = "select classCode as districtClassCode from tblDistrict " +
//	        				" where Province=? and  City=? and Area=''"; 
//	        		PreparedStatement pstmt = conn.prepareStatement(sql);
//	        		pstmt.setString(1, Province);
//	        		pstmt.setString(2, City);
//	        		ResultSet rset = pstmt.executeQuery();   
//	        		if(rset.next()){
//	        			retMap.put("icompanyZoneCode", rset.getString(1));
//	        		}else{
//	        			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
//			    		rs.retVal = "������վͨ�ӿڷ��ص�����"+cpAddressName+"�ڱ�ϵͳ�����ݲ�����";
//			    		return rs;
//	        		}
	        	}
//	        	cpAddressName.replace(">>", "-");
	        	retMap.put("icompanyZone", cpAddressName);
	    		retMap.put("icompanyAddress",reMap.get("cpAddress") );
	    		retMap.put("materialCount",reMap.get("materialCount") );
	    		retMap.put("icompanyUrl",reMap.get("cpSite") );
	    		retMap.put("icompanyID",reMap.get("cpID") );
	    		retMap.put("cpMember",reMap.get("cpMember") );
	    		retMap.put("cpPhone",reMap.get("cpPhone") );
	    		retMap.put("cpTel",reMap.get("cpTel") );
	    		return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� getUserInfo ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	/*
	 * ��վͨ�µ������ýӿڶԱȿͻ���Ϣ
	 */
	public Result compareUserInfo(Connection conn,String token,String loginName,String icompanyName,String district,String districtFullName,String materialCount,String connect,String tel,String address,HashMap retMap){
		Result rs = new Result();
		String compareResult="";		
		try{
			BaseEnv.log.debug("�ӿڵ��� compareUserInfo ������token="+token+",loginName ="+loginName+",district ="+district+",districtFullName ="+districtFullName+",materialCount ="+materialCount+",connect ="+connect
					+",tel ="+tel+",address ="+address);     
			
			if(materialCount==null || materialCount.length() ==0){
				materialCount= "0";  
			}  
			DecimalFormat  df= new DecimalFormat("#.0");    
			rs =getUserInfo(conn,token,loginName,retMap);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			
	    	compareResult =compareResult+"�ز�����OA��"+(int)Double.parseDouble(materialCount)+",��վͨ��"+retMap.get("materialCount")+"\\n ";
	    	
			if(!icompanyName.equals(retMap.get("icompanyName"))){  
	    		compareResult =compareResult+"��˾���Ʋ�һ�£�"+icompanyName+"  "+retMap.get("icompanyName")+"\\n ";      
	    	}
			districtFullName = districtFullName.replaceAll("-", ">>");
	    	if(!districtFullName.equals((retMap.get("icompanyZone")+""))){
	    		compareResult =compareResult+"���ڳ��в�һ�£�"+districtFullName+"  "+retMap.get("icompanyZone")+"\\n ";
	    	} 
	    	
	    	if(!connect.equals(retMap.get("cpMember"))){
	    		compareResult =compareResult+"��ϵ�˲�һ�£�"+connect+"  "+retMap.get("cpMember")+"\\n ";
	    	}
	    	if(!tel.equals(retMap.get("cpTel"))){
	    		compareResult =compareResult+"�̶��绰��һ�£�"+tel+"  "+retMap.get("cpTel")+"\\n ";
	    	}
	    	if(!address.equals(retMap.get("icompanyAddress"))){
	    		compareResult =compareResult+"��ϸ��ַ��һ�£�"+address+"  "+retMap.get("icompanyAddress")+"\\n ";
	    	}
	    	BaseEnv.log.debug("compareResult:"+compareResult);
			retMap.put("compareResult",compareResult);
			return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� compareUserInfo ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * �û���ͨ
	 * @param token
	 * @param procuct
	 * @param startDate
	 * @param endDate
	 * @param agentName
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result buyPackage(String token,String procuct,String startDate,String endDate,String agentName,String loginName){
		Result rs = new Result();
		try{
			
			BaseEnv.log.debug("�ӿڵ��� buyPackage ������token="+token+",loginName ="+loginName+",packageID ="+procuct+",packageStartDate ="+startDate+",packageEndDate ="+endDate+",agentName ="+agentName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.buyPackage(token, loginName, Integer.parseInt(procuct), startDate, endDate, agentName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� buyPackage ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� buyPackage ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * �û�����/����/�޸��ײͽӿ�
	 * @param token
	 * @param procuct
	 * @param startDate
	 * @param endDate
	 * @param agentName
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result renewPackage(String token,String procuct,String startDate,String endDate,String agentName,String loginName){
		Result rs = new Result();
		try{
			
			BaseEnv.log.debug("�ӿڵ��� renewPackage ������token="+token+",loginName ="+loginName+",packageID ="+procuct+",packageStartDate ="+startDate+",packageEndDate ="+endDate+",agentName ="+agentName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         


	        
	        String rstr =service.renewPackage(token, loginName, Integer.parseInt(procuct), startDate, endDate, agentName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� renewPackage ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� renewPackage ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * �û�ͣ�ýӿ�
	 * @param token
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result disablePackage(String token,String loginName,String agtName){  
		Result rs = new Result();
		try{
			BaseEnv.log.debug("�ӿڵ��� disablePackage ������token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();                 
	        String rstr =service.disablePackage(token, loginName,agtName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� disablePackage ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� disablePackage ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 
	 * ��ͨ��ϵͳ
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result openWebSite(String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("��ͨ��ϵͳ....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			BaseEnv.log.debug("�ӿڵ��� ���ⱦopenWebSite ������CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
//			mfmi_javaClient client = new mfmi_javaClient();        
//			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
	    	HttpClientParams params = new HttpClientParams();  
	    	params.setParameter(HttpClientParams.USE_EXPECT_CONTINUE, Boolean.FALSE);  
	    	params.setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, 3 * 60 * 1000l);
//	        org.codehaus.xfire.client.Client.getInstance(service).setProperty(CommonsHttpMessageSender.HTTP_CLIENT_PARAMS,params);
			String key = "10012002";
			String Signed = CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+key;
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
//			String rstr = service.openWebSite_XML(CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			String rstr = openWebSite_HTML(CPName, Domain, Model, Kind, SiteServer, EndDate, AgtName, OpUsID, key, md5Pwd);
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦopenWebSiteʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("<wsID>") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦopenWebSiteʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }
	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦopenWebSite ���أ�"+rstr);

	        String wsID = getValue(rstr,"wsID");
	        
	        String MakeDomain = getValue(rstr,"MakeDomain");
	        String info = getValue(rstr,"Info");
	        retMap.put("rwsID", wsID);
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦopenWebSite ���÷��ز�����rwsID="+wsID);
	        retMap.put("MakeDomain", MakeDomain);
	        retMap.put("remark", info);
	        
	        
	        
	        if(wsID.equals("0")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦopenWebSiteʧ�ܣ�"+info;
	    		return rs;
	        }else{
	        	return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦopenWebSite ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦopenWebSite"+e.getMessage();
    		return rs;
		}
	}
	
	private String openWebSite_HTML(String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,String Key,String Signed)
    {
                
        String result="";
        DataInputStream in=null;
        try {
        	String servicesUrl = "http://c8mff.m2.magic2008.cn/manage/web_services/oa.aspx";
            String params1 = "?method=OpenWebSite_XML&cpName=" + URLEncoder.encode(CPName,"UTF-8") + "&domain=" + URLEncoder.encode(Domain,"UTF-8") + "&model=" + Model + "&kind=" + Kind + "&&siteServer=" + SiteServer;
            String params2 = "&endDate=" + URLEncoder.encode(EndDate,"UTF-8") + "&agtName=" + URLEncoder.encode(AgtName,"UTF-8") + "&opusid=" + OpUsID + "&signed=" + Signed;

            URL realUrl = new URL(servicesUrl+params1+params2);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ʵ�ʵ�����
            connection.connect();
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new DataInputStream(connection.getInputStream());
            byte[] b=new byte[0];
            byte[] bs = new byte[1024];
            int count = -1;
            while ((count=in.read(bs)) != -1) {
                byte[] temp = new byte[b.length+count];
                System.arraycopy(b, 0, temp, 0, b.length);
                System.arraycopy(bs, 0, temp, b.length, count);
                b = temp;
            }
            result = new String(b,"UTF-8");
        } catch (Exception e) {
        	BaseEnv.log.error("�ӿڵ��� ���ⱦopenWebSite HTTP get ����",e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	BaseEnv.log.error("�ӿڵ��� ���ⱦopenWebSite HTTP get close inputStream ����",e2);
            }
        }
        return result;
    }

	
	/**
	 * ��ͨ΢���ֻ���
	 * @param wsID ����վ���� ������վʱ����
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result openMobileWechatApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("��ͨ΢���ֻ���....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� openMobileWechatApp ������wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.openMobileWechatApp(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦopenMobileWechatAppʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦopenMobileWechatAppʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦopenMobileWechatApp ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦopenMobileWechatApp�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦopenMobileWechatApp�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * ȡ��·����ʱ��������Ϊ�ǹ̶���
	 * @param Kind
	 * @return
	 */
	public Result getWebsiteServerLine(String Kind){
		Result rs = new Result();
		try{
			
			String signed;
						
			//BaseEnv.log.debug("�ӿڵ��� disablePackage ������token="+token+",loginName ="+loginName);       
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			String Signed = Kind+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.getWebsiteServerLine(Integer.parseInt(Kind), md5Pwd);
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�������ⱦ�ӿ�ʧ�ܣ����ؿն���";
	    		return rs;
	        }
			rstr = java.net.URLDecoder.decode(rstr,"utf-8");
			System.out.println("�����"+rstr);
	        
	        //BaseEnv.log.debug("�ӿڵ��� disablePackage ���أ�"+rstr);
	
	        HashMap reMap = new HashMap();
	        
	        /*
	        if(response.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        } */
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� disablePackage ����",e);
			e.printStackTrace();
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * ������վ
	 * @param wsID :��վ����
	 * @param EndDate ��������
	 * @return
	 */
	public Result xuFeiWebSite(String wsID,String EndDate){
		System.out.println("������վ....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd));  
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� xuFeiWebSite ������wsID="+wsID+";EndDate="+EndDate);       
			String Signed = wsID + EndDate+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.xuFeiWebSite(Integer.parseInt(wsID), date, md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiWebSiteʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(!rstr.equals("1")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiWebSiteʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦxuFeiWebSite ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦxuFeiWebSite�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiWebSite�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	
	
	/**
	 * ����΢��
	 * @param wsID ����վ���� ������վʱ����
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result xuFeiMobileWechatApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("΢��....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� xuFeiMobileWechatApp ������wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.xuFeiMobileWechatApp(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiMobileWechatAppʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|ok") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiMobileWechatAppʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦxuFeiMobileWechatApp ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦxuFeiMobileWechatApp�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiMobileWechatApp�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * ��ͨAPP
	 * @param wsID ����վ���� ������վʱ����
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result setApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("��ͨAPP....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� auto_Open_XuFei_App ������wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_App(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_Appʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_Appʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦauto_Open_XuFei_App ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦauto_Open_XuFei_App�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_App�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * ��ͨAPP
	 * @param wsID ����վ���� ������վʱ����
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result setMobileSite(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("��ͨ�ֻ���....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY)); 
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� auto_Open_XuFei_MobileSite ������wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_MobileSite(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_MobileSiteʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_MobileSiteʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦauto_Open_XuFei_MobileSite ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦauto_Open_XuFei_MobileSite�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_MobileSite�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * ��ͨAPP
	 * @param wsID ����վ���� ������վʱ����
	 * @param AgtName ����������
	 * @param CPName ��˾����
	 * @param Domain ����
	 * @param Model ģ��̶� 101
	 * @param Kind �̶� 1
	 * @param SiteServer ��· ����˫��BJ5������ȫ���û����Ƽ���|BJ5|0,�Ϻ�˫��SH4 ���㶫ʡ��ͻ�ר�ã�|SH4|0,������·(HK1)������ͻ����ã�|HK1|200
	 * @param EndDate ����ʱ��
	 * @param OpUsID ҵ��Ա���ţ��̶� "0"
	 * @param retMap ���ڷ���ֵ�� �� queryParamMap (������������wsID����վ���ţ�MakeDomain������ )
	 * @return
	 */
	public Result setWechat(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("��ͨ΢��....................................");
		Result rs = new Result();
		try{
			
			String signed;
			
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(EndDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
			mfmi_javaClient client = new mfmi_javaClient();           
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� auto_Open_XuFei_WechatSite ������wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_WechatSite(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_WechatSiteʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_WechatSiteʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� ���ⱦauto_Open_XuFei_WechatSite ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� ���ⱦauto_Open_XuFei_WechatSite�ӿ� ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� ���ⱦauto_Open_XuFei_WechatSite�ӿ�"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * ��������
	 * @param wsID
	 * @param NewPassword
	 * @param OpUsID
	 * @param retMap
	 * @return
	 */
	public Result resetPassWord(String wsID,String NewPassword,String OpUsID,HashMap retMap){
		Result rs = new Result();
		try{
			String signed;
			mfmi_javaClient client = new mfmi_javaClient();           
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			BaseEnv.log.debug("�ӿڵ��� resetPassWord ������wsID="+wsID+";NewPassword="+NewPassword+";OpUsID="+OpUsID);       
			String Signed = wsID + NewPassword+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.resetPassWord(Integer.parseInt(wsID), NewPassword, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("�����"+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� resetPassWord�����ؿն���";
	    		return rs;
	        }
			if(!rstr.equals("1")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� resetPassWord�����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� resetPassWord ���أ�"+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� resetPassWord ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "�ӿڵ��� resetPassWord"+e.getMessage();
    		return rs;
		}
	}
	
	
    public static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer(buffer.length);
		String temp;
		for (int i = 0; i < buffer.length; i++) {
			temp = Integer.toHexString(0xFF & buffer[i]);
			if (temp.length() < 2) {
				sb.append("0");
			}
			sb.append(temp.toUpperCase());
		}
		return sb.toString();
	}
    
    public String getValue(String str,String key){
    	if(str.indexOf("<"+key+">") == -1){
    		return "";
    	}
    	return str.substring(str.indexOf("<"+key+">")+("<"+key+">").length(),str.indexOf("</"+key+">"));
    }
    
    public Result getUserMagicMasterWebID(String token,String loginName){
    	Result rs = new Result();
		try{
			
			BaseEnv.log.debug("�ӿڵ��� GetUserMagicMasterWebID ������token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.getUserMagicMasterWebID(token, loginName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� GetUserMagicMasterWebID ���أ�"+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�״̬Ϊ��";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	rs.retVal = reMap.get("MasterID").toString();
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� getUserMagicMasterWebID ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
    }
    private static String super_website_key = "nx%,9s[BzYQnxXtPY~nyGdSUr5SAgD!*3vf,Q48t2[nh0+-7.VLe(g";
    public Result getSuperWebsiteGroup(String token,String loginName,HashMap retMap){
    	Result rs = new Result();
		try{
			rs = getUserMagicMasterWebID(token,loginName);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			int cWsID =(int) Double.parseDouble(rs.retVal+"");
			
			BaseEnv.log.debug("�ӿڵ��� getSuperWebsiteGroup ������token="+token+",loginName ="+loginName);       
			super_website_javaClient client = new super_website_javaClient();        
			super_website_javaSoap service = client.getsuper_website_javaSoap();         
	        
	        String rstr =service.getSuperWebsiteGroup(super_website_key, cWsID);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "���ýӿ�ʧ�ܣ����ؿ��ַ���";
	    		return rs;
	        }
	        BaseEnv.log.debug("�ӿڵ��� getSuperWebsiteGroup ���أ�"+rstr);
	        System.out.println(rstr);
	        
	        retMap.put("cWsID", cWsID);
	        retMap.put("swSWSID1", getValue(rstr,"swSWSID1"));
	        retMap.put("swSWSID2", getValue(rstr,"swSWSID2"));
	        retMap.put("swSWSID3", getValue(rstr,"swSWSID3"));
	        retMap.put("swSWSID4", getValue(rstr,"swSWSID4"));
	        retMap.put("swSWSID5", getValue(rstr,"swSWSID5"));
	        
	        retMap.put("MakeAddress1", getValue(rstr,"MakeAddress1"));
	        retMap.put("MakeAddress2", getValue(rstr,"MakeAddress2"));
	        retMap.put("MakeAddress3", getValue(rstr,"MakeAddress3"));
	        retMap.put("MakeAddress4", getValue(rstr,"MakeAddress4"));
	        retMap.put("MakeAddress5", getValue(rstr,"MakeAddress5"));
	        
	        retMap.put("swSiteEndDate1", getValue(rstr,"swSiteEndDate1").substring(0,10));
	        retMap.put("swSiteEndDate2", getValue(rstr,"swSiteEndDate2").substring(0,10));
	        retMap.put("swSiteEndDate3", getValue(rstr,"swSiteEndDate3").substring(0,10));
	        retMap.put("swSiteEndDate4", getValue(rstr,"swSiteEndDate4").substring(0,10));
	        retMap.put("swSiteEndDate5", getValue(rstr,"swSiteEndDate5").substring(0,10));
	        
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� getSuperWebsiteGroup ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
    }
    
    public Result xuFeiChildSiteByIndex(String mWsID,String siteIndex,String eDate){
    	Result rs = new Result();
		try{
			
			
			BaseEnv.log.debug("�ӿڵ��� xuFeiChildSiteByIndex ������mWsID="+mWsID+",siteIndex ="+siteIndex+",eDate ="+eDate);       
			super_website_javaClient client = new super_website_javaClient();        
			super_website_javaSoap service = client.getsuper_website_javaSoap();         
	        
			Calendar cal = Calendar.getInstance();  
            cal.setTime(BaseDateFormat.parse(eDate,BaseDateFormat.yyyyMMdd)); 
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			XMLGregorianCalendar date = dtf.newXMLGregorianCalendar();
			date.setYear(cal.get(Calendar.YEAR));
			date.setMonth(cal.get(Calendar.MONTH) + 1);
			date.setDay(cal.get(Calendar.DAY_OF_MONTH));
			date.setHour(cal.get(Calendar.HOUR_OF_DAY));
			date.setMinute(cal.get(Calendar.MINUTE));
			date.setSecond(cal.get(Calendar.SECOND));  
			
	        String rstr =service.xuFeiChildSiteByIndex(super_website_key, Integer.parseInt(mWsID), Integer.parseInt(siteIndex), date);
	       
	        if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiChildSiteByIndexʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiChildSiteByIndexʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� xuFeiChildSiteByIndex ���أ�"+rstr);
	        System.out.println(rstr);
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� xuFeiChildSiteByIndex ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
    }
    public Result xuFeiChildSiteByMonth(String mWsID,String siteIndex,String month){
    	Result rs = new Result();
		try{
			
			BaseEnv.log.debug("�ӿڵ��� xuFeiChildSiteByMonth ������mWsID="+mWsID+",siteIndex ="+siteIndex+",eDate ="+month);       
			super_website_javaClient client = new super_website_javaClient();        
			super_website_javaSoap service = client.getsuper_website_javaSoap();    
			
	        String rstr =service.xuFeiChildSiteByMonth(super_website_key, Integer.parseInt(mWsID), Integer.parseInt(siteIndex), Integer.parseInt(month));
	       
	        if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiChildSiteByMonthʧ�ܣ����ؿն���";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "�ӿڵ��� ���ⱦxuFeiChildSiteByMonthʧ�ܣ����������쳣"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("�ӿڵ��� xuFeiChildSiteByMonth ���أ�"+rstr);
	        System.out.println(rstr);
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ��� xuFeiChildSiteByMonth ����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "������վͨ�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
    }
    
    public String getSuperWebsiteGroup(HttpServletRequest request){
    	String token = request.getParameter("token");
    	String loginName = request.getParameter("loginName");
    	HashMap retMap = new HashMap();
    	Result rs = getSuperWebsiteGroup(token, loginName, retMap);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		return gson.toJson(retMap);
    	}else{
    		return "ERROR";
    	}
    }
	
	
	public static void main(String[] args) {
		Einterface ei = new Einterface();
//		ei.getUserMagicMasterWebID("d8c5ea22-f904-4e64-8566-f8cce7af0218", "gzsaicai");
		//ei.getWebsiteServerLine("1");
		//ei.xuFeiWebSite("16580", "2015-01-06");
		//ei.xuFeiMobileWechatApp("16580","DEF" , "www.tz1288.com", "101","1","BJ5", "2015-01-06","ABC", "22222222",new HashMap());
		//ei.resetPassWord("1588", "0417", "0", new HashMap());
		
		ei.getSuperWebsiteGroup("d8c5ea22-f904-4e64-8566-f8cce7af0218", "szrenhe",new HashMap());
	}

}
