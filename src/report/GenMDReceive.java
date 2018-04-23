package report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.openplatform.MessageBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class GenMDReceive {
	
	public static String YZBusi  = "https://ipos.yazuo.com/ipos-open/bill/queryBillBusiData";
	
	private DynDBManager dbmgt = new DynDBManager();
	
	private List<HashMap> plats = new ArrayList();
	
	private static String Account = "100227";
		
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();			
	}
	
	public GenMDReceive(){
		HashMap map1 = new HashMap();		
		map1.put("platform", "΢��");
		map1.put("period", 1);
		plats.add(map1);
		HashMap map2 = new HashMap();
		map2.put("platform", "֧����");
		map2.put("period", 1);
		plats.add(map2);
		HashMap map3 = new HashMap();
		map3.put("platform", "����ȯ");
		map3.put("period", 1);
		plats.add(map3);
		HashMap map4 = new HashMap();
		map4.put("platform", "����ô����");
		map4.put("period", 1);
		plats.add(map4);		
	}
	
	public List<HashMap> getYZComs(Connection conn){		
		List<HashMap> list = new ArrayList();			
	  	try{		          		
	  		String sql = " select * from posSYJ where posType = 2 ";
		    PreparedStatement pss = conn.prepareStatement(sql) ;		     		
		    ResultSet rss = pss.executeQuery() ;
		    while(rss.next()){	
		    	HashMap map = new HashMap();
		    	map.put("syjNo", rss.getString("No"));
		    	map.put("CompanyCode", rss.getString("CompanyCode"));
		    	map.put("ComFullName", rss.getString("name"));
		    	list.add(map);
		    }     
		    return list;
	  	   }catch(Exception ex){	  		  
	  		   BaseEnv.log.error("GenMDReceive_getYZComs : ",ex);
	  		   return list;
	  	   }		        	       	    	
	}
	
	public List<HashMap> getYZData(List<HashMap>comps){
		HttpTransfer tran = new HttpTransfer();
		Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(new Date());  
        rightNow.add(Calendar.DAY_OF_YEAR, -1);// ���ڼ�n��
		String preT = new SimpleDateFormat("yyyy-MM-dd").format(rightNow.getTime());
		List<HashMap> arr = new ArrayList();
		
		for(HashMap item : comps){			
			String code = (String)item.get("syjNo");
			String companyCode = (String)item.get("CompanyCode");			
			Map m = new HashMap();		
			m.put("code", code);			
			m.put("date", preT);			
			String str = gson.toJson(m);			
			String jsonStr = tran.postHttps(YZBusi,new Gson().toJson(m),"application/json; charset=UTF-8");			
			try{
				HashMap ret = new HashMap();
				HashMap map = gson.fromJson(jsonStr, HashMap.class);
				if(!"200".equals(map.get("code")) || map.get("data") == null){
					BaseEnv.log.error("�����ŵ��˵������쳣��"+jsonStr);	
					//continue;				
				}
				ret.put("code", code);
				ret.put("createBy", "1");
				ret.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				ret.put("id",IdGenerated.getId());
				ret.put("BillDate",preT);
				if(map.get("data") != null){
					HashMap data = (HashMap)map.get("data");
					ret.put("billModeStat", (List<HashMap>)data.get("billModeStat"));
					ret.put("receAmount", data.get("receAmount"));
					ret.put("prefAmount", data.get("prefAmount"));
					ret.put("realAmount", data.get("realAmount"));
					//ret.put("realAmount", data.get("realAmount"));
					HashMap platSrv = (HashMap)data.get("platSrvAmount");
					if(platSrv!=null){						
						ret.put("meituanAmount", platSrv.get("meituanAmount")==null?0:platSrv.get("meituanAmount"));
						ret.put("eleAmount", platSrv.get("eleAmount")==null?0:platSrv.get("eleAmount"));
						ret.put("baiduAmount", platSrv.get("baiduAmount")==null?0:platSrv.get("baiduAmount"));
						ret.put("wechatAmount", platSrv.get("wechatAmount") ==null?0:platSrv.get("wechatAmount"));
						ret.put("alipayAmount", platSrv.get("alipayAmount")==null?0:platSrv.get("alipayAmount"));
					}else{
						
					}
				}				
				arr.add(ret);
			} catch(Exception e){
				BaseEnv.log.error("�����ŵ��˵����ݴ����쳣��",e);
				BaseEnv.log.error("�ύ�������ݣ�"+gson.toJson(m));			 
		   }
		}	
		return arr;
	}
	
	public MessageBean addYZAccount(HashMap map,Connection conn){
	  
		Result rs = new Result(); 
	   MessageBean msg = new MessageBean();			
  	   try{
	  		conn.setAutoCommit(false);
	  		//HashMap map = new HashMap();	  		
	  		String sql = " insert into bdBusiData(id,createBy,createTime,code,eleAmount,baiduAmount,meituanAmount,wechatAmount,alipayAmount,receAmount,prefAmount,realAmount,BillDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		    String sqlDet = " insert into bdBusiDataDet(f_ref,payName,payAmount,Amt) ";		          	
		    
		    PreparedStatement pss = conn.prepareStatement(sql) ;	
	  		pss.setString(1, (String)map.get("id"));
	  		pss.setString(2, (String)map.get("createBy"));
	  		pss.setString(3, (String)map.get("createTime"));
	  		pss.setString(4, (String)map.get("code"));
	  		pss.setDouble(5, (Double)map.get("eleAmount"));
	  		pss.setDouble(6, (Double)map.get("baiduAmount"));
	  		pss.setDouble(7, (Double)map.get("meituanAmount"));
	  		pss.setDouble(8, (Double)map.get("wechatAmount"));
	  		pss.setDouble(9, (Double)map.get("alipayAmount"));
	  		pss.setDouble(10, (Double)map.get("receAmount"));
	  		pss.setDouble(11, (Double)map.get("prefAmount"));
	  		pss.setDouble(12, (Double)map.get("realAmount"));
	  		pss.setString(13, (String)map.get("BillDate"));
	  		/*
	  		pss.setFloat(5, (float)map.get("eleAmount"));
	  		pss.setFloat(6, (float)map.get("baiduAmount"));
	  		pss.setFloat(7, (float)map.get("meituanAmount"));
	  		pss.setFloat(8, (float)map.get("wechatAmount"));
	  		pss.setFloat(9, (float)map.get("alipayAmount"));
	  		pss.setFloat(10, (float)map.get("receAmount"));
	  		pss.setFloat(11, (float)map.get("prefAmount"));
	  		pss.setFloat(12, (float)map.get("realAmount"));
	  		pss.setString(13, (String)map.get("BillDate"));
	  		*/
		    int rss = pss.executeUpdate();		     		      
		    Statement pss2 = conn.createStatement();
			
			for(HashMap item : (List<HashMap>)map.get("billModeStat")){
				String _sql ="";
				if("֧����".equals(item.get("payName"))){
					_sql= sqlDet + " values('"+map.get("id")+"','"+item.get("payName")+"',"+item.get("payAmount")+","+map.get("alipayAmount")+") ";
				} else if("΢��".equals(item.get("payName"))){
					_sql= sqlDet + " values('"+map.get("id")+"','"+item.get("payName")+"',"+item.get("payAmount")+","+map.get("wechatAmount")+") ";
				} else if("����ȯ".equals(item.get("payName"))){
					_sql= sqlDet + " values('"+map.get("id")+"','"+item.get("payName")+"',"+item.get("payAmount")+","+map.get("meituanAmount")+") ";
				} else if("����ô".equals(item.get("payName"))){
					_sql= sqlDet + " values('"+map.get("id")+"','"+item.get("payName")+"',"+item.get("payAmount")+","+map.get("eleAmount")+") ";
				}		     				
				pss2.addBatch(_sql);
			}
			pss2.executeBatch();
		    conn.commit();
		    msg.setCode(1);	   
  	   }catch(Exception e){		
  		   msg.setCode(0);
  		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
  		   BaseEnv.log.error("GenMDReceive_addYZAccount : ",e);
  	   }		             		         		     
       return msg;
	}
	
	public void exec(Connection conn){
		
		//******��ȡ�����ŵ��б�******//
		List<HashMap> comps = getYZComs(conn);
		//******���������ⲿ�ӿڻ�ȡ�˵�����******//
		List<HashMap> data = getYZData(comps);
		
		for(HashMap item : data){
			addYZAccount(item,conn);
		}
		/*�����տ
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		Date now =new Date();
		HashMap saveValues = new HashMap();
		saveValues.put("id", IDGenerater.getId());
		saveValues.put("BillDate", dfShort.format(now));
		saveValues.put("ExeBalAmt", bean.getAmt());
		saveValues.put("AcceptTypeId", "Receive");
		saveValues.put("AccDetStatus", "0");
		saveValues.put("CreateBy", "1");
		saveValues.put("lastUpdateBy", "1");
		saveValues.put("createTime", df.format(now));
		saveValues.put("lastUpdateTime", df.format(now));
		saveValues.put("statusId", "0");
		saveValues.put("Remark", "ũ��ֱ��" + bean.getAccName());
		saveValues.put("SettleType", "3");
		saveValues.put("SCompanyID", "00001");
		saveValues.put("workFlowName", "finish");
		saveValues.put("WorkFlowNode", "-1");
		saveValues.put("currentlyreceive", "0.00000000");
		saveValues.put("FCcurrentlyreceive", "0.00000000");
		saveValues.put("ReturnAmt", "0.00000000");
		saveValues.put("EmployeeID", "1");
		saveValues.put("BillFcAmt", "0.00000000");
		saveValues.put("ContractAmt", "0.00000000");
		saveValues.put("prntCount", "0");
		saveValues.put("CurrencyRate", "1.00000000");
		saveValues.put("finishTime", df.format(now));
		saveValues.put("FactIncome", bean.getAmt());
		saveValues.put("AccAmt", bean.getAmt());
		saveValues.put("DepartmentCode", "00101");
		saveValues.put("CompanyCode", company.get("classCode"));
		
		saveValues.put("CashUserID", "0");		
		saveValues.put("AutoBillMarker", "0");
		
		//��ϸ
		ArrayList<HashMap> details= new ArrayList<HashMap>();
		HashMap detValues= new HashMap();
		detValues.put("id", IDGenerater.getId());
		detValues.put("f_ref", saveValues.get("id"));
		detValues.put("SettleType", "1");
		String Account = req.getParameter("Account");
		if(Account==null||Account.equals("")){  
			detValues.put("Account", "100208"); 
		}else{
			detValues.put("Account", Account); 
		}
		
		detValues.put("Amount", bean.getAmt());
		detValues.put("SCompanyID", "00001");
		detValues.put("Remark", "ũ������ֱ��");
		detValues.put("ExeBalFcAmt", "0.00000000");
		details.add(detValues);
		saveValues.put("TABLENAME_tblReceiveAccountDet", details);
		
		Object ob = req.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		Hashtable props =BaseEnv.propMap;
		//����տ
		Result rs =saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"),saveValues, resources,props,req);
		*/
	}
	
	/*
	public void run(Connection conn){		
		//��ȡ�����ŵ��б�//
		List<HashMap> comps = getYZComs(conn);				
		
		//***�����ŵ������տ
		for(HashMap item : comps){					
			//�����տ
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
			Date now =new Date();
			float totalAmt = 0;
			HashMap saveValues = new HashMap();
			saveValues.put("id", IDGenerater.getId());
			saveValues.put("BillDate", dfShort.format(now));			
			saveValues.put("AcceptTypeId", "Receive");
			saveValues.put("AccDetStatus", "0");
			saveValues.put("CreateBy", "1");
			saveValues.put("lastUpdateBy", "1");
			saveValues.put("createTime", df.format(now));
			saveValues.put("lastUpdateTime", df.format(now));
			saveValues.put("statusId", "0");
			saveValues.put("Remark", "�ŵ����");
			saveValues.put("SettleType", "3");
			saveValues.put("SCompanyID", "00001");
			saveValues.put("workFlowName", "finish");
			saveValues.put("WorkFlowNode", "-1");
			saveValues.put("currentlyreceive", "0.00000000");
			saveValues.put("FCcurrentlyreceive", "0.00000000");
			saveValues.put("ReturnAmt", "0.00000000");
			saveValues.put("EmployeeID", "1");
			saveValues.put("BillFcAmt", "0.00000000");
			saveValues.put("ContractAmt", "0.00000000");
			saveValues.put("prntCount", "0");
			saveValues.put("CurrencyRate", "1.00000000");
			saveValues.put("finishTime", df.format(now));
			
			saveValues.put("DepartmentCode", "00101");
			saveValues.put("CompanyCode", item.get("CompanyCode"));
			
			saveValues.put("CashUserID", "0");		
			saveValues.put("AutoBillMarker", "0");
			//��ȡ�ŵ��տ��΢��/֧����/����/����ô��
			List<HashMap> dets = getReceives((String)item.get("CompanyCode"),dfShort.format(now),conn);
									
			//��ϸ
			ArrayList<HashMap> details= new ArrayList<HashMap>();
								
			String Remark = "";
			for(HashMap sub : dets){
					
				if(sub.get("amt")!= null){
					totalAmt += (float)sub.get("amt");
				}
				Remark += ("".equals(Remark)?"":";")+sub.get("platform");
				
//				detValues.put("id", IDGenerater.getId());
//				detValues.put("f_ref", saveValues.get("id"));
//				detValues.put("SettleType", "1");									
//				detValues.put("Amount", sub.get("amt"));
//				detValues.put("SCompanyID", "00001");
//				detValues.put("Remark", sub.get("platform"));
//				detValues.put("ExeBalFcAmt", "0.00000000");
//				detValues.put("Account", Account);
//				details.add(detValues);
			
			}
			if(dets.size() > 0  && totalAmt > 0 ){
				//�����տ��ϸ//
				HashMap detValues= new HashMap();
				detValues.put("id", IDGenerater.getId());
				detValues.put("f_ref", saveValues.get("id"));
				detValues.put("SettleType", "1");									
				detValues.put("Amount", totalAmt);
				detValues.put("SCompanyID", "00001");
				detValues.put("Remark", Remark);
				detValues.put("ExeBalFcAmt", "0.00000000");
				detValues.put("Account", Account);
				details.add(detValues);
			}
			if(details.size()<=0){				  
				continue;
			}
			saveValues.put("ExeBalAmt", totalAmt);//�趨�տ���
			saveValues.put("FactIncome", totalAmt);
			saveValues.put("AccAmt", totalAmt);
			saveValues.put("TABLENAME_tblReceiveAccountDet", details);
						
			Object ob = null;
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			Hashtable props =BaseEnv.propMap;
			//����տ
			Result rs =saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"),saveValues, resources,props,conn);			
		}
		
	}
	*/
	public void run(Connection conn){		
		//��ȡ�����ŵ��б�//
		List<HashMap> comps = getYZComs(conn);				
		
		//***�����ŵ������տ
		for(HashMap item : comps){					
			//�����տ
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
			Date now =new Date();
			
			//��ȡ�ŵ��տ��΢��/֧����/����/����ô��
			List<HashMap> dets = getReceives((String)item.get("CompanyCode"),dfShort.format(now),conn);
			
			for(HashMap sub : dets){
				String Remark = "";
				float totalAmt = 0;
				HashMap saveValues = new HashMap();
				saveValues.put("id", IDGenerater.getId());
				saveValues.put("BillDate", dfShort.format(now));			
				saveValues.put("AcceptTypeId", "Receive");
				saveValues.put("AccDetStatus", "0");
				saveValues.put("CreateBy", "1");
				saveValues.put("lastUpdateBy", "1");
				saveValues.put("createTime", df.format(now));
				saveValues.put("lastUpdateTime", df.format(now));
				saveValues.put("statusId", "0");
				saveValues.put("Remark", "�ŵ����");
				saveValues.put("SettleType", "3");
				saveValues.put("SCompanyID", "00001");
				saveValues.put("workFlowName", "finish");
				saveValues.put("WorkFlowNode", "-1");
				saveValues.put("currentlyreceive", "0.00000000");
				saveValues.put("FCcurrentlyreceive", "0.00000000");
				saveValues.put("ReturnAmt", "0.00000000");
				saveValues.put("EmployeeID", "1");
				saveValues.put("BillFcAmt", "0.00000000");
				saveValues.put("ContractAmt", "0.00000000");
				saveValues.put("prntCount", "0");
				saveValues.put("CurrencyRate", "1.00000000");
				saveValues.put("finishTime", df.format(now));				
				saveValues.put("DepartmentCode", "00101");
				saveValues.put("CompanyCode", item.get("CompanyCode"));				
				saveValues.put("CashUserID", "0");		
				saveValues.put("AutoBillMarker", "0");
				
				//��ϸ
				ArrayList<HashMap> details= new ArrayList<HashMap>();
				if(sub.get("amt")!= null){
					totalAmt += (float)sub.get("amt");
				}
				Remark += ("".equals(Remark)?"":";")+sub.get("platform");			
				
				if(dets.size() > 0  && totalAmt > 0 ){
					//�����տ��ϸ//
					HashMap detValues= new HashMap();
					detValues.put("id", IDGenerater.getId());
					detValues.put("f_ref", saveValues.get("id"));
					detValues.put("SettleType", "1");									
					detValues.put("Amount", totalAmt);
					detValues.put("SCompanyID", "00001");
					detValues.put("Remark", Remark);
					detValues.put("ExeBalFcAmt", "0.00000000");
					detValues.put("Account", Account);
					details.add(detValues);
				}
				if(details.size()<=0){				  
					continue;
				}
				saveValues.put("Remark", Remark+"-�ŵ����");
				saveValues.put("ExeBalAmt", totalAmt);//�趨�տ���
				saveValues.put("FactIncome", totalAmt);
				saveValues.put("AccAmt", totalAmt);
				saveValues.put("TABLENAME_tblReceiveAccountDet", details);
				Object ob = null;
				MessageResources resources = null;
				if (ob instanceof MessageResources) {
					resources = (MessageResources) ob;
				}
				Hashtable props =BaseEnv.propMap;
				//����տ
				Result rs =saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"),saveValues, resources,props,conn);
			}																																		
		}
		
	}
	
	private List<HashMap> getReceives(String companyCode,String curDate,Connection conn){
		List<HashMap> list = new ArrayList<HashMap>();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  		
		try{
			for(HashMap item : plats){
				HashMap map = item;
				Date d = sdf.parse(curDate);
				Calendar rightNow = Calendar.getInstance();  
				rightNow.setTime(d);  
				rightNow.add(Calendar.DAY_OF_YEAR, -((int)item.get("period")));// ���ڼ�n��
				map.put("begD", sdf.format(rightNow.getTime()));				
				map.put("endD", sdf.format(rightNow.getTime()));
				map.put("CompanyCode", companyCode);				
				try{						
					//String sql = " select a.code,b.payName,sum(b.payAmount) Amt from bdBusiData a join bdBusiDataDet b on a.id = b.f_ref join posSYJ c on c.No = a.code where a.BillDate between ? and ? and c.CompanyCode =? group by a.code,b.payName ";		     		             
					String sql = " select a.code,b.payName,sum(b.payAmount-(case when b.payName='����ȯ' then 0 else isnull(b.Amt,0) end)) Amt from bdBusiData a join bdBusiDataDet b on a.id = b.f_ref join posSYJ c on c.No = a.code where a.BillDate between ? and ? and c.CompanyCode =? group by a.code,b.payName ";
					PreparedStatement pss = conn.prepareStatement(sql) ;	
					pss.setString(1, (String)map.get("begD"));
					pss.setString(2, (String)map.get("endD"));
					pss.setString(3, companyCode);
					
				    ResultSet rs = pss.executeQuery();				   
				    while (rs.next()){
						if(rs.getString("payName").equals(map.get("platform"))){
							map.put("amt", rs.getFloat("Amt"));
							list.add(map);
							break;
						}					   		        			
				    }				   
				  }catch(Exception e){					  
					   BaseEnv.log.error("GenMDReceive_getReceives : ",e);
					   BaseEnv.log.error("companyCode : "+companyCode+" curDate : "+curDate);
				  }				             				         
			}
			return list; 
		}catch(Exception e){
			return null;
		}
	}
	
	private Result saveToDb(DBTableInfoBean tBean, HashMap saveValues, MessageResources resources, Hashtable props,Connection conn) {

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		LoginBean loginBean = new LoginBean();
		loginBean.setId("1");
		loginBean.setSunCmpClassCode("00001");

		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		if (table == null) {
			table = new Hashtable();
			BaseEnv.sessionSet.put(loginBean.getId(), table);
		}

		BillNo billno = BillNoManager.find("tblSaleReceive_BillNo",conn);
		if (billno != null) {
			// ���õ��ݱ�������󣬴����ݿ�����,���ߵ��ݱ��Ϊ��

			String valStr = BillNoManager.find("tblSaleReceive_BillNo", saveValues, loginBean,conn);
			if ("".equals(valStr)) {
				/* ���ݱ���޷����� */
				Result rs = new Result();
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "���ɵ��ݱ���쳣";
				return rs;
			} else {
				saveValues.put("BillNo", valStr);
			}
		}

		Locale locale = Locale.getDefault();
		String addMessage = "��ӳɹ�";
		/**
		 * �ֶ������ǵ��ݱ���������õ�������
		 */
		String tableName = "";

		// ����Ĭ��ֵ
		UsrMgtBD userMgt = new UsrMgtBD();
		//UserFunctionMgt userMgt = new UserFunctionMgt();
		try {
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			// ��ϸ������Ĭ��ֵ
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(tBean.getTableName(), allTables);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			Result rs = new Result();
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "����Ĭ��ֵʧ��";
			return rs;
		}

		/* ��֤�ǿ�-----���ﲻ����Ҫ����֤�Ĺ�������Ϊ��add�������и�ȫ��Ľ����� */
		/********************************************
		 * ִ����ؽӿ� saveValues Ϊ�������ݵ�HashMap ����˲�������Ӧ�ӿ�������ݲ���
		 ********************************************/
		MOperation mop = new MOperation();
		mop.query = true;
		mop.add = true;
		mop.update = true;

		Result rs;
		try {
			rs = userMgt.add(conn,tBean, saveValues, loginBean, "", allTables, "", "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			BaseEnv.log.error("GenMDReceive.saveToDb add Error:", e);
			rs = new Result();
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "ִ���Զ������ʧ��";
			return rs;
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		} else {
			/* ɾ�����ݱ�� */
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale.toString(), "");
			rs.retVal = saveErrrorObj.getMsg();
			return rs;
		}
	}
}
