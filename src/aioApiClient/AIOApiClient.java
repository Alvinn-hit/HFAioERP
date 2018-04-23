package aioApiClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AIOApiClient {
	private static String url="http://112.74.100.216:9877/AIOApi";
	private static String userName="fuhaoqiang";
	private static String password="123";
	
	private static String JSESSIONID=""; //登陆后的会话ID
	
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	private static HashMap login(){
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			String md5=MD5.GetMD5Code(password);
			out.print("op=LOGIN&name="+userName+"&password="+md5);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new DataInputStream(conn.getInputStream());
			byte bs[]=new byte[0];
			byte b[] = new byte[1024];
			int readCount = 0;
			while ((readCount=in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+readCount];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, readCount);
			}
			String ret = new String(bs,"UTF-8");
			System.out.println("执行登陆接口返回："+ret);
			HashMap map = gson.fromJson(ret, HashMap.class);
			if(map.get("code").equals("OK")){
				//登陆成功,取登陆标识JSESSIONID
				String responseCookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
				JSESSIONID = responseCookie;
				//JSESSIONID = responseCookie.substring(responseCookie.indexOf("JSESSIONID=")+"JSESSIONID=".length());
				//JSESSIONID = JSESSIONID.substring(0,JSESSIONID.indexOf(";"));
				System.out.println(responseCookie);
			}
			return map;
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			HashMap map = new HashMap();
			map.put("code", "ERROR");
			map.put("msg", e.getMessage());
			return map;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * 接口请求通用方法
	 */
	private static HashMap request(String method,HashMap<String,String> param){
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		String paramStr = "";
		if(param != null){
			for(Entry<String, String> set :param.entrySet()){
				paramStr +="&"+set.getKey()+"="+set.getValue();
			}
		}
		if(paramStr.length() > 0){
			paramStr = paramStr.substring(1);
		}
		System.out.println("执行接口：op="+method+"&"+paramStr);
		try {
			//判断是否登陆
			if(JSESSIONID==null || JSESSIONID.equals("")){
				HashMap lmap = login();
				if(!"OK".equals(lmap.get("code"))){
					return lmap;
				}
			}
			
			URL realUrl = new URL(url+"?op="+method);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Cookie", JSESSIONID);//传入会话ID，服务器需验证身份
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			String md5=MD5.GetMD5Code(password);
			
			out.print(paramStr);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new DataInputStream(conn.getInputStream());
			byte bs[]=new byte[0];
			byte b[] = new byte[1024];
			int readCount = 0;
			while ((readCount=in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+readCount];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, readCount);
			}
			String ret = new String(bs,"UTF-8");
			System.out.println("执行接口返回："+ret);
			HashMap map = gson.fromJson(ret, HashMap.class);
			if(map.get("code").equals("OK")){
				//执行成功
				//在此加入成功后要执行的代码
				System.out.println("执行成功啦");
				return map;
			}else{
				//执行失败
				if(map.get("code").equals("NOLOGIN")){
					//未登陆，或登陆失效，需生新登陆
					HashMap lret = login();
					if("OK".equals(lret.get("code"))){
						//重新登陆成功，重新执行
						return request(method,param);
					}else{
						return lret;
					}
				}else{
					return map;
				}
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			HashMap map = new HashMap();
			map.put("code", "ERROR");
			map.put("msg", e.getMessage());
			return map;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 添加前准备
	 */
	public static String addPrepare(String tableName, String parentTableName, String f_brother, String parentCode, String moduleType) {
		HashMap param = new HashMap();
		param.put("tableName", tableName);
		param.put("parentTableName", parentTableName);
		param.put("f_brother", f_brother);
		param.put("parentCode", parentCode);
		param.put("moduleType", moduleType);
		
		HashMap map = request("addPrepare",param);
		System.out.println(map.get("code"));
		return map.get("obj")+"";
	}
	/**
	 * 添加前准备
	 */
	public static String execDefine(String defineName, String buttonTypeName, String keyId, String tableName, String defineInfo) {
		HashMap param = new HashMap();
		param.put("defineName", defineName);
		param.put("buttonTypeName", buttonTypeName);
		param.put("keyId", keyId);
		param.put("tableName", tableName);
		param.put("defineInfo", defineInfo);
		
		HashMap map = request("execDefine",param);
		System.out.println(map.get("code"));
		return map.get("obj")+"";
	}
	
	public static void main(String[] args){
		AIOApiClient client = new AIOApiClient();
		//client.addPrepare("tblGoods", "", "", "", "");
		client.execDefine("tblProduceTest_Add_One", "", "66189EC83F4E68A27691C2A47F871A", "tblProduceTest", "");
		
	}
}
