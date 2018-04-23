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
 * 天助网产品接口
 * @author Administrator
 *
 */
public class Einterface {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	
	
	/**
	 * 用户开通
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
			
			BaseEnv.log.debug("接口调用 setFunctionalModule 参数：token="+token+",loginName ="+loginName+","
					+ "procuct ="+procuct+",state ="+state+",packageStartDate ="+packageStartDate+",packageEndDate ="+packageEndDate
					+",agentName ="+agentName+",attributeJson ="+attributeJson);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.setFunctionalModule(token, loginName, procuct, Integer.parseInt(state), packageStartDate, packageEndDate, agentName, attributeJson);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 setFunctionalModule 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("接口调用 setFunctionalModule 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	
 
	
	/**
	 * 获取/检测用户信息
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result getUserInfo(Connection conn,String token,String loginName,HashMap retMap){
		Result rs = new Result();		  
		try{
			BaseEnv.log.debug("接口调用 getUserInfo 参数：token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
			TZOASoap service = client.getTZOASoap(); 
	        String rstr =service.getUserInfo(token, loginName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 getUserInfo 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	
	        	reMap.put("materialCount", (int)Double.parseDouble(reMap.get("materialCount").toString()));   
	        	retMap.put("icompanyName", reMap.get("cpName"));
	        	retMap.put("cpShortName", reMap.get("cpShortName"));
	        	String cpAddressName = reMap.get("cpAddressName")+"";
	        	
	        	if(cpAddressName == null  || cpAddressName.indexOf(">>")== -1){
	        		rs.retCode =ErrorCanst.DEFAULT_FAILURE;
		    		rs.retVal = "调用易站通接口返回的区域"+cpAddressName+"格式不正确";
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
//			    		rs.retVal = "调用易站通接口返回的区域"+cpAddressName+"在本系统中数据不存在";
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
			BaseEnv.log.error("接口调用 getUserInfo 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	/*
	 * 易站通下单，调用接口对比客户信息
	 */
	public Result compareUserInfo(Connection conn,String token,String loginName,String icompanyName,String district,String districtFullName,String materialCount,String connect,String tel,String address,HashMap retMap){
		Result rs = new Result();
		String compareResult="";		
		try{
			BaseEnv.log.debug("接口调用 compareUserInfo 参数：token="+token+",loginName ="+loginName+",district ="+district+",districtFullName ="+districtFullName+",materialCount ="+materialCount+",connect ="+connect
					+",tel ="+tel+",address ="+address);     
			
			if(materialCount==null || materialCount.length() ==0){
				materialCount= "0";  
			}  
			DecimalFormat  df= new DecimalFormat("#.0");    
			rs =getUserInfo(conn,token,loginName,retMap);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			
	    	compareResult =compareResult+"素材数量OA："+(int)Double.parseDouble(materialCount)+",易站通："+retMap.get("materialCount")+"\\n ";
	    	
			if(!icompanyName.equals(retMap.get("icompanyName"))){  
	    		compareResult =compareResult+"公司名称不一致："+icompanyName+"  "+retMap.get("icompanyName")+"\\n ";      
	    	}
			districtFullName = districtFullName.replaceAll("-", ">>");
	    	if(!districtFullName.equals((retMap.get("icompanyZone")+""))){
	    		compareResult =compareResult+"所在城市不一致："+districtFullName+"  "+retMap.get("icompanyZone")+"\\n ";
	    	} 
	    	
	    	if(!connect.equals(retMap.get("cpMember"))){
	    		compareResult =compareResult+"联系人不一致："+connect+"  "+retMap.get("cpMember")+"\\n ";
	    	}
	    	if(!tel.equals(retMap.get("cpTel"))){
	    		compareResult =compareResult+"固定电话不一致："+tel+"  "+retMap.get("cpTel")+"\\n ";
	    	}
	    	if(!address.equals(retMap.get("icompanyAddress"))){
	    		compareResult =compareResult+"详细地址不一致："+address+"  "+retMap.get("icompanyAddress")+"\\n ";
	    	}
	    	BaseEnv.log.debug("compareResult:"+compareResult);
			retMap.put("compareResult",compareResult);
			return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 compareUserInfo 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 用户开通
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
			
			BaseEnv.log.debug("接口调用 buyPackage 参数：token="+token+",loginName ="+loginName+",packageID ="+procuct+",packageStartDate ="+startDate+",packageEndDate ="+endDate+",agentName ="+agentName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.buyPackage(token, loginName, Integer.parseInt(procuct), startDate, endDate, agentName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 buyPackage 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("接口调用 buyPackage 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * 用户续费/升级/修改套餐接口
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
			
			BaseEnv.log.debug("接口调用 renewPackage 参数：token="+token+",loginName ="+loginName+",packageID ="+procuct+",packageStartDate ="+startDate+",packageEndDate ="+endDate+",agentName ="+agentName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         


	        
	        String rstr =service.renewPackage(token, loginName, Integer.parseInt(procuct), startDate, endDate, agentName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 renewPackage 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("接口调用 renewPackage 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 用户停用接口
	 * @param token
	 * @param loginName
	 * @param retMap
	 * @param conn
	 * @return
	 */
	public Result disablePackage(String token,String loginName,String agtName){  
		Result rs = new Result();
		try{
			BaseEnv.log.debug("接口调用 disablePackage 参数：token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();                 
	        String rstr =service.disablePackage(token, loginName,agtName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 disablePackage 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
	    		return rs;
	        }else if(Double.parseDouble(reMap.get("state").toString()) == 1){
	        	return rs;
	        }else{
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = reMap.get("reason");
	    		return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("接口调用 disablePackage 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 
	 * 开通单系统
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result openWebSite(String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("开通单系统....................................");
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
			
			BaseEnv.log.debug("接口调用 如意宝openWebSite 参数：CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
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
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝openWebSite失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("<wsID>") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝openWebSite失败，返回内容异常"+rstr;
	    		return rs;
	        }
	        
	        BaseEnv.log.debug("接口调用 如意宝openWebSite 返回："+rstr);

	        String wsID = getValue(rstr,"wsID");
	        
	        String MakeDomain = getValue(rstr,"MakeDomain");
	        String info = getValue(rstr,"Info");
	        retMap.put("rwsID", wsID);
	        BaseEnv.log.debug("接口调用 如意宝openWebSite 设置返回参数：rwsID="+wsID);
	        retMap.put("MakeDomain", MakeDomain);
	        retMap.put("remark", info);
	        
	        
	        
	        if(wsID.equals("0")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝openWebSite失败，"+info;
	    		return rs;
	        }else{
	        	return rs;
	        }
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝openWebSite 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝openWebSite"+e.getMessage();
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
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
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
        	BaseEnv.log.error("接口调用 如意宝openWebSite HTTP get 报错：",e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	BaseEnv.log.error("接口调用 如意宝openWebSite HTTP get close inputStream 报错：",e2);
            }
        }
        return result;
    }

	
	/**
	 * 开通微信手机版
	 * @param wsID ：网站代号 开能网站时给的
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result openMobileWechatApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("开通微信手机版....................................");
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
			
			BaseEnv.log.debug("接口调用 openMobileWechatApp 参数：wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.openMobileWechatApp(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝openMobileWechatApp失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝openMobileWechatApp失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝openMobileWechatApp 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝openMobileWechatApp接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝openMobileWechatApp接口"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 取线路，暂时不调，因为是固定的
	 * @param Kind
	 * @return
	 */
	public Result getWebsiteServerLine(String Kind){
		Result rs = new Result();
		try{
			
			String signed;
						
			//BaseEnv.log.debug("接口调用 disablePackage 参数：token="+token+",loginName ="+loginName);       
			mfmi_javaClient client = new mfmi_javaClient();        
			mfmi_javaSoap service = client.getmfmi_javaSoap();
			
			String Signed = Kind+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.getWebsiteServerLine(Integer.parseInt(Kind), md5Pwd);
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用如意宝接口失败，返回空对象";
	    		return rs;
	        }
			rstr = java.net.URLDecoder.decode(rstr,"utf-8");
			System.out.println("结果："+rstr);
	        
	        //BaseEnv.log.debug("接口调用 disablePackage 返回："+rstr);
	
	        HashMap reMap = new HashMap();
	        
	        /*
	        if(response.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
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
			BaseEnv.log.error("接口调用 disablePackage 报错：",e);
			e.printStackTrace();
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 续费网站
	 * @param wsID :网站代号
	 * @param EndDate 结束日期
	 * @return
	 */
	public Result xuFeiWebSite(String wsID,String EndDate){
		System.out.println("续费网站....................................");
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
			
			BaseEnv.log.debug("接口调用 xuFeiWebSite 参数：wsID="+wsID+";EndDate="+EndDate);       
			String Signed = wsID + EndDate+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.xuFeiWebSite(Integer.parseInt(wsID), date, md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiWebSite失败，返回空对象";
	    		return rs;
	        }
			if(!rstr.equals("1")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiWebSite失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝xuFeiWebSite 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝xuFeiWebSite接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝xuFeiWebSite接口"+e.getMessage();
    		return rs;
		}
	}
	
	
	/**
	 * 续费微信
	 * @param wsID ：网站代号 开能网站时给的
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result xuFeiMobileWechatApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("微信....................................");
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
			
			BaseEnv.log.debug("接口调用 xuFeiMobileWechatApp 参数：wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.xuFeiMobileWechatApp(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiMobileWechatApp失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|ok") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiMobileWechatApp失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝xuFeiMobileWechatApp 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝xuFeiMobileWechatApp接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝xuFeiMobileWechatApp接口"+e.getMessage();
    		return rs;
		}
	}
	
	/**
	 * 开通APP
	 * @param wsID ：网站代号 开能网站时给的
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result setApp(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("开通APP....................................");
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
			
			BaseEnv.log.debug("接口调用 auto_Open_XuFei_App 参数：wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_App(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_App失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_App失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝auto_Open_XuFei_App 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝auto_Open_XuFei_App接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_App接口"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * 开通APP
	 * @param wsID ：网站代号 开能网站时给的
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result setMobileSite(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("开通手机版....................................");
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
			
			BaseEnv.log.debug("接口调用 auto_Open_XuFei_MobileSite 参数：wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_MobileSite(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_MobileSite失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_MobileSite失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝auto_Open_XuFei_MobileSite 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝auto_Open_XuFei_MobileSite接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_MobileSite接口"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * 开通APP
	 * @param wsID ：网站代号 开能网站时给的
	 * @param AgtName 代理商名称
	 * @param CPName 公司名称
	 * @param Domain 域名
	 * @param Model 模板固定 101
	 * @param Kind 固定 1
	 * @param SiteServer 线路 北京双线BJ5（面向全国用户，推荐）|BJ5|0,上海双线SH4 （广东省外客户专用）|SH4|0,美国线路(HK1)（海外客户备用）|HK1|200
	 * @param EndDate 结束时间
	 * @param OpUsID 业务员代号，固定 "0"
	 * @param retMap 用于返回值的 传 queryParamMap (传出两个参数wsID：网站代号，MakeDomain：域名 )
	 * @return
	 */
	public Result setWechat(String wsID,String CPName,String Domain,String Model,String Kind,String SiteServer,String EndDate,String AgtName,String OpUsID,HashMap retMap){
		System.out.println("开通微信....................................");
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
			
			BaseEnv.log.debug("接口调用 auto_Open_XuFei_WechatSite 参数：wsID="+wsID+";CPName="+CPName+";Domain="+Domain+";Model="+Model+";Kind="+Kind+";SiteServer="+SiteServer+";EndDate="+EndDate+";AgtName="+AgtName+";OpUsID="+OpUsID);       
			String Signed = wsID + CPName+Domain+Model+Kind+SiteServer+EndDate+AgtName+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.auto_Open_XuFei_WechatSite(Integer.parseInt(wsID), CPName, Domain, Integer.parseInt(Model), Integer.parseInt(Kind), SiteServer, date, AgtName, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_WechatSite失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_WechatSite失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 如意宝auto_Open_XuFei_WechatSite 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 如意宝auto_Open_XuFei_WechatSite接口 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 如意宝auto_Open_XuFei_WechatSite接口"+e.getMessage();
    		return rs;
		}
	}
	/**
	 * 重置密码
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
			
			BaseEnv.log.debug("接口调用 resetPassWord 参数：wsID="+wsID+";NewPassword="+NewPassword+";OpUsID="+OpUsID);       
			String Signed = wsID + NewPassword+OpUsID+"10012002";
			Signed = Signed.toUpperCase();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Signed.getBytes("UTF-8")) ;
			String md5Pwd = toHex(md.digest()) ;
			System.out.println(md5Pwd);
			String rstr = service.resetPassWord(Integer.parseInt(wsID), NewPassword, Integer.parseInt(OpUsID), md5Pwd);
			
			System.out.println("结果："+rstr);
			if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 resetPassWord，返回空对象";
	    		return rs;
	        }
			if(!rstr.equals("1")){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 resetPassWord，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 resetPassWord 返回："+rstr);
			
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 resetPassWord 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
    		rs.retVal = "接口调用 resetPassWord"+e.getMessage();
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
			
			BaseEnv.log.debug("接口调用 GetUserMagicMasterWebID 参数：token="+token+",loginName ="+loginName);       
			TZOAClient client = new TZOAClient();        
	        TZOASoap service = client.getTZOASoap();         
	        
	        String rstr =service.getUserMagicMasterWebID(token, loginName);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 GetUserMagicMasterWebID 返回："+rstr);
	
	        rstr = rstr.replace(",}", "}");
	        System.out.println(rstr);
	        HashMap reMap = new HashMap();
	        reMap = gson.fromJson(rstr, HashMap.class);
	        
	        if(reMap.get("state") == null ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用易站通接口失败，状态为空";
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
			BaseEnv.log.error("接口调用 getUserMagicMasterWebID 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
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
			
			BaseEnv.log.debug("接口调用 getSuperWebsiteGroup 参数：token="+token+",loginName ="+loginName);       
			super_website_javaClient client = new super_website_javaClient();        
			super_website_javaSoap service = client.getsuper_website_javaSoap();         
	        
	        String rstr =service.getSuperWebsiteGroup(super_website_key, cWsID);
	        if(rstr == null || rstr.length() ==0){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "调用接口失败，返回空字符串";
	    		return rs;
	        }
	        BaseEnv.log.debug("接口调用 getSuperWebsiteGroup 返回："+rstr);
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
			BaseEnv.log.error("接口调用 getSuperWebsiteGroup 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
    }
    
    public Result xuFeiChildSiteByIndex(String mWsID,String siteIndex,String eDate){
    	Result rs = new Result();
		try{
			
			
			BaseEnv.log.debug("接口调用 xuFeiChildSiteByIndex 参数：mWsID="+mWsID+",siteIndex ="+siteIndex+",eDate ="+eDate);       
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
	    		rs.retVal = "接口调用 如意宝xuFeiChildSiteByIndex失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiChildSiteByIndex失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 xuFeiChildSiteByIndex 返回："+rstr);
	        System.out.println(rstr);
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 xuFeiChildSiteByIndex 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
    		return rs;
		}
    }
    public Result xuFeiChildSiteByMonth(String mWsID,String siteIndex,String month){
    	Result rs = new Result();
		try{
			
			BaseEnv.log.debug("接口调用 xuFeiChildSiteByMonth 参数：mWsID="+mWsID+",siteIndex ="+siteIndex+",eDate ="+month);       
			super_website_javaClient client = new super_website_javaClient();        
			super_website_javaSoap service = client.getsuper_website_javaSoap();    
			
	        String rstr =service.xuFeiChildSiteByMonth(super_website_key, Integer.parseInt(mWsID), Integer.parseInt(siteIndex), Integer.parseInt(month));
	       
	        if(rstr == null || rstr.length() ==0 ){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiChildSiteByMonth失败，返回空对象";
	    		return rs;
	        }
			if(rstr.indexOf("1|") == -1){
	        	rs.retCode =ErrorCanst.DEFAULT_FAILURE;
	    		rs.retVal = "接口调用 如意宝xuFeiChildSiteByMonth失败，返回内容异常"+rstr;
	    		return rs;
	        }	        
	        BaseEnv.log.debug("接口调用 xuFeiChildSiteByMonth 返回："+rstr);
	        System.out.println(rstr);
	        return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用 xuFeiChildSiteByMonth 报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用易站通接口失败，"+e.getMessage();
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
