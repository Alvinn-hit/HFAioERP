package yqzl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import yqzl.bean.NtdmtBean;


/**
 * 记账宝接口
 * @author Administrator
 *
 */
public class Jzb {
	
	public Jzb(){
		
	}
	/**
	 * 查询虚拟账户交易
	 * @param host
	 * @param lgnnam
	 */
	public Jzb(String host,String lgnnam){
		this.host=host;
		this.lgnnam=lgnnam;
	}
	private String host;
	private String lgnnam;
	
	/**
	 * 查询当天虚拟账户交易
	 * @param accnbr  账号
	 * @param dmanbr  虚拟户编号
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getNow(String accnbr,String dmanbr){
		List<NtdmtBean> list = new ArrayList<NtdmtBean>();
		XmlPacket xmlPkt = new XmlPacket("NTDMTLST", lgnnam);
		Map NTDMTLSTY = new Properties();
		NTDMTLSTY.put("ACCNBR", accnbr);//账号
		if(dmanbr!=null){
			NTDMTLSTY.put("DMANBR", dmanbr);//虚拟户编号
		}
		xmlPkt.putProperty("NTDMTLSTY", NTDMTLSTY);
		String result=sendRequest(xmlPkt.toXmlString());
		
		//*********发送前置机报文*********//
		Logger.getLogger("YQZL").info("************************");
		Logger.getLogger("YQZL").info("发送前置机报文 :"+xmlPkt.toXmlString());
		Logger.getLogger("YQZL").info("************************");
		//************end***********//
		
		//****保存原始报文到日志 2016-09-13		
		Logger.getLogger("YQZL").info("========查询当天虚拟账户交易==========");			
		Logger.getLogger("YQZL").info("前置机返回报文："+result);
		Logger.getLogger("YQZL").info("===============================");
		/*
		XmlPacket resultXmlPkt= processResult(result);
		if (resultXmlPkt!=null) {
			List<Map> maps=resultXmlPkt.getProperty("NTDMTLSTZ");
			if (maps!=null) {
				for (Map map : maps) {
					NtdmtBean bean = new NtdmtBean();
					bean.setAccnbr(map.get("ACCNBR").toString());
					bean.setDmanbr(map.get("DMANBR").toString());
					bean.setDmanam(map.get("DMANAM").toString());
					bean.setTrxnbr(map.get("TRXNBR").toString());
					bean.setCcynbr(map.get("CCYNBR").toString());
					bean.setTrxamt(map.get("TRXAMT").toString());
					bean.setTrxdir(map.get("TRXDIR").toString());
					bean.setTrxtim(map.get("TRXTIM").toString());
					bean.setRpyacc(map.get("RPYACC")==null?null:map.get("RPYACC").toString());
					bean.setRpynam(map.get("RPYNAM")==null?null:map.get("RPYNAM").toString());
					bean.setTrxtxt(map.get("TRXTXT")==null?null:map.get("TRXTXT").toString());
					bean.setNarinn(map.get("NARINN")==null?null:map.get("NARINN").toString());
					list.add(bean);
				}
			}
			
		}*/
		return result;
	}
	/**
	 * 查询当天虚拟账户交易
	 * @param accnbr  账号
	 * @return
	 */
	public String getNow(String accnbr){
		return getNow(accnbr,null);
	}
	/**
	 * 查询虚拟账户历史交易
	 * @param accnbr  账号
	 * @param dmanbr  虚拟户编号
	 * @param begdat  起始日期  20140506  只能查询最近13个月 100天内
	 * @param enddat  结束日期 20140607
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getHistory(String accnbr,String dmanbr,String begdat,String enddat){
		List<NtdmtBean> list = new ArrayList<NtdmtBean>();
		// 查询虚拟账户历史交易
		XmlPacket xmlPkt = new XmlPacket("NTDMTHLS", lgnnam);
		Map NTDMTHLSY = new Properties();
		NTDMTHLSY.put("ACCNBR",accnbr);//账号
		if(dmanbr!=null){
			NTDMTHLSY.put("DMANBR", dmanbr);//虚拟户编号
		}
		NTDMTHLSY.put("BEGDAT", begdat);//开始日期
		NTDMTHLSY.put("ENDDAT", enddat);//结束日期
		xmlPkt.putProperty("NTDMTHLSY", NTDMTHLSY);
		String result=sendRequest(xmlPkt.toXmlString());//获取最近一天交易记录
		
		//****保存原始报文到日志 2016-09-12
		//Logger.getLogger("YQZL").info("lgnnam="+lgnnam+";accnbr="+accnbr+";host="+host);
		Logger.getLogger("YQZL").info("=========查询虚拟账户历史交易==========");
		Logger.getLogger("YQZL").info("BEGDAT="+begdat+";ENDDAT="+enddat);		
		Logger.getLogger("YQZL").info("前置机返回报文："+result);
		Logger.getLogger("YQZL").info("=================================");
		
		/*
		XmlPacket resultXmlPkt= processResult(result);
		if (resultXmlPkt!=null) {
			List<Map> maps=resultXmlPkt.getProperty("NTDMTHLSZ");
			if (maps!=null) {
				for (Map map : maps) {
					NtdmtBean bean = new NtdmtBean();
					bean.setAccnbr(map.get("ACCNBR").toString());
					bean.setDmanbr(map.get("DMANBR").toString());
					bean.setDmanam(map.get("DMANAM").toString());
					bean.setTrxnbr(map.get("TRXNBR").toString());
					bean.setCcynbr(map.get("CCYNBR").toString());
					bean.setTrxamt(map.get("TRXAMT").toString());
					bean.setTrxdir(map.get("TRXDIR").toString());
					bean.setTrxtim(map.get("TRXTIM").toString());
					bean.setRpyacc(map.get("RPYACC")==null?null:map.get("RPYACC").toString());
					bean.setRpynam(map.get("RPYNAM")==null?null:map.get("RPYNAM").toString());
					bean.setTrxtxt(map.get("TRXTXT")==null?null:map.get("TRXTXT").toString());
					bean.setNarinn(map.get("NARINN")==null?null:map.get("NARINN").toString());
					list.add(bean);
				}
			}
			
		}		
		*/
		return result;
	}
	/**
	 * 查询所有虚拟账户历史交易
	 * @param accnbr  账号
	 * @param begdat  起始日期  20140506  只能查询最近13个月 100天内
	 * @param enddat  结束日期 20140607
	 * @return
	 */
	public String getHistory(String accnbr,String begdat,String enddat){
		return getHistory(accnbr,null, begdat, enddat);
	}
	/**
	 * 连接前置机，发送请求报文，获得返回报文
	 * 
	 * @param data
	 * @return
	 * @throws MalformedURLException
	 */
	private String sendRequest(String data) {
		String result = "";
		try {
			URL url;
			url = new URL(host);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream os;
			os = conn.getOutputStream();
			os.write(data.toString().getBytes("GBK"));
			os.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 处理返回的结果
	 * 
	 * @param result
	 */
	public XmlPacket processResult(String result) {
		if (result != null && result.length() > 0) {
			XmlPacket pktRsp = XmlPacket.valueOf(result);
			if (pktRsp != null) {
				String sRetCod = pktRsp.getRETCOD();
				if (sRetCod.equals("0")) {
					return pktRsp;
				}else {
					System.out.println("操作失败：" + pktRsp.getERRMSG());
				}
			} else {
				System.out.println("响应报文解析失败");
			}
		}
		return null;
	}
	public static void main(String[] args) {
		//new Jzb("http://localhost:9999", "银企直连专用普通1").getNow("591902896910206");
		new Jzb("http://localhost:9999", "银企直连专用普通1").getHistory("591902896910206", "20141001", "20141004");
	}
}
