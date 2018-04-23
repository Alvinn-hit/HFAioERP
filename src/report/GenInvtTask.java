package report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.openplatform.MessageBean;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.web.util.BaseEnv;

public class GenInvtTask{
	
	private static String storeSQL;
	private static String Day_InvtExceptSQL;
	private static String DaybusinessSQL;
	private static String DeleteDay_InvtSQL;
	private static String DeleteDay_AllInvtSQL;
	/**
	 * 系列号
	 */
	private static int curNo = 0;
	
	static{
		storeSQL = "select a.* from tblcompany a  where Account1 in ('直营店','类直营店','加盟店') and statusId =0 and classCode in (select CompanyCode from posSYJ)";
		Day_InvtExceptSQL = "select b.EmployeeID from tblTaskManage a,tblTaskManageDet b where a.id=b.f_ref and a.DefineName='Day_InvtExcept'";
		DaybusinessSQL ="select companyCode,reportDate from posbd.dbo.posDaybusiness where status = 0";
		DeleteDay_InvtSQL = "delete from posBD.dbo.Day_InvtDet where f_ref in (select id from posBD.dbo.Day_Invt where companyCode = '?' and date = '?') delete from posBD.dbo.Day_Invt where companyCode = '?' and date = '?'";
		DeleteDay_AllInvtSQL = "delete from posBD.dbo.Day_InvtDet where f_ref in (select id from posBD.dbo.Day_Invt where date = '?') delete from posBD.dbo.Day_Invt where date = '?'";
	}
	
	public synchronized static String getId() {
		int no = ++curNo;
		if (curNo >= 9999) {
			curNo = 0;
		}
		return curNo+""+System.currentTimeMillis();
	}			
	
	public void run(final String date,final String companyCode){
		int retCode = DBUtil.execute(new IfDB() {
        final String _d = date;   
		final String _c = companyCode;
        	public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	if(companyCode == null || "".equals(companyCode)){
                    		//****删除该日期所有进销存数据****//
                    		String[] _p = {_d,_d};
            				String _sql = _replaceSQL(DeleteDay_AllInvtSQL,_p);            				
            				boolean _ret = _execUpdate(conn,_sql);
                    		//***********end**********//
                    		call(conn,_d);
                    	} else{
                    		//****删除该门店该日期进销存数据****//
                    		String[] _p = {companyCode,_d,companyCode,_d};
            				String  _sql = _replaceSQL(DeleteDay_InvtSQL,_p);
            				boolean _ret = _execUpdate(conn,_sql);
                    		//************end**********//
                    		MessageBean msg = null;
                    		GenInvtData _g = new GenInvtData();	
                			List<HashMap> _l = _g.run(conn, companyCode, _d);                					
                			//获取门店退品数据
                			List<HashMap> _r = _g.getPOSRet(companyCode, _d);
                			_updatePOSRet(conn,_r,companyCode,_d);
                			//门店进销存表入库
                			msg = _saveSQL(conn,_l,companyCode,_d);
                			if(msg.getCode() == 1){				
                				return ;
                			}		
                    	}
                    	                   	
                    }
                }); 
                return 0;
            }
        });	
	}
	
	public void call(Connection conn,String date){
		//获取门店列表信息
		List<HashMap> stores = _execSQL(conn,storeSQL);
		GenInvtData _g = new GenInvtData();
		String _d = date;			
	
		int f = 0;		
		for(HashMap item : stores){
			
			MessageBean msg = null;	
			List<HashMap> _l = _g.run(conn, (String)item.get("classCode"), _d);					
						
			//获取门店退品数据
			List<HashMap> _r = _g.getPOSRet((String)item.get("classCode"), _d);
			_updatePOSRet(conn,_r,(String)item.get("classCode"),_d);
			
			//门店进销存表入库
			msg = _saveSQL(conn,_l,(String)item.get("classCode"),_d);
			if(msg.getCode() == 1 && (Integer)msg.getData() == 1){				
				f++;
			}			
		}
		/******调用接口发送消息*******/
		if(f>0){
			String cur = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String tixing = "您 "+cur+" 收到收银机异常提醒（"+f+" 家门店进销存异常）";
			String bt = "<a href='javascript:mdiwin(\"/ReportDataAction.do?reportNumber=Day_InvtExcept&date="+date+"\",\"门店进销存异常提醒\")'> 您 "+cur+" 收到门店进销存异常提醒("+f+" 家门店进销存异常)</a>";
			String emp = "";//获取发送人员列表
			List<HashMap> emps = _execSQL(conn,Day_InvtExceptSQL);
			
			if(emps != null && emps.size()>0){
				for(int i = 0;i<emps.size();i++){
					emp += emps.get(i).get("EmployeeID")+";";
				}
				new AdviceMgt().add("管理员",tixing, bt, emp ,"关联单据号","notApprove",conn);
				//com.menyi.aio.web.advice.AdviceMgt.add("管理员",tixing, bt, emp ,"关联单据号","notApprove",conn);	
			}			
		}
		/*************end*************/
	}
	
	private MessageBean _updatePOSRet(Connection conn,List<HashMap> list,String companyCode,String date){
		MessageBean msg = new MessageBean();
		Statement st = null;
		Double retAmt = 0.0;
		
		for(HashMap item : list){
			retAmt += Double.parseDouble((String)item.get("retAmt"));			
		}
								
		try{			
			st = conn.createStatement();
		} catch(Exception e){
			msg.setCode(0);
			msg.setDescription("statement 创建失败。");
			return msg;
		}
		try{			
			//****更新营业日报表标志位*******//
			String _s = " update posbd.dbo.posDaybusiness set retAmt= "+retAmt.toString()+" where reportDate = '"+date+"' and companyCode = '"+companyCode+"' ";
			st.addBatch(_s);																		
			st.executeBatch();			
			msg.setCode(1);		
		} catch(Exception e){
			msg.setCode(0);
			msg.setDescription(e.getMessage());			
			BaseEnv.log.error("_updatePOSRet error:",e);
		}
		return msg;
	}
	
	private boolean _checkData(List<HashMap> list){
		Statement st = null;	
		Double calAmount = 0.0;
		Double relAmount = 0.0;
		for(int i = 0;i<list.size();i++){
			Map<String,String> _m = list.get(i);
			if(_m.get("calTurnover")!=null){
				calAmount += Double.parseDouble(_m.get("calTurnover"));
			}
			if(_m.get("relTurnover")!=null){
				relAmount += Double.parseDouble(_m.get("relTurnover"));
			}			
		}
		if(Math.abs(relAmount-calAmount)>300){
			return true;
		}
		
		return false;
	}
	
	public void alterDaybusiness(Connection conn){
		List<HashMap> list = _execSQL(conn,DaybusinessSQL);
		GenInvtData _g = new GenInvtData();	
		for(int i = 0;i<list.size();i++){
			Map<String,String> _m = list.get(i);
			if(_m.get("companyCode")!=null && _m.get("reportDate") != null){
				String[] _p = {_m.get("companyCode"),_m.get("reportDate"),_m.get("companyCode"),_m.get("reportDate")};
				String  _sql = _replaceSQL(DeleteDay_InvtSQL,_p);
				boolean _r = _execUpdate(conn,_sql);
				if(!_r){
					continue;
				}
				MessageBean msg = null;	
				List<HashMap> _l = _g.run(conn, _m.get("companyCode"), _m.get("reportDate"));					
							
				//门店进销存表入库
				msg = _saveSQL(conn,_l,_m.get("companyCode"),_m.get("reportDate"));
			}
		}
	}
	
	public void exec(Connection conn){
		
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间
		String _d = new SimpleDateFormat("yyyy-MM-dd").format(dBefore);
		call(conn,_d);
	}
	
	private MessageBean _saveSQL(Connection conn,List<HashMap> list,String companyCode,String date){
		
		MessageBean msg = new MessageBean();
		Statement st = null;				
		GenInvtData _g = new GenInvtData();
		try{			
			st = conn.createStatement();
		} catch(Exception e){
			msg.setCode(0);
			msg.setDescription("statement 创建失败。");
			return msg;
		}
		
		try{
			//****获取门店支付汇总数据****//
			Integer OnlineCusTotal = 0;
			List<HashMap>PayDet = _g.getPOSPayDet(companyCode,date);
			for(HashMap item : PayDet){
				if("美团外卖".equals(item.get("payName")) || "百度外卖".equals(item.get("payName")) || "饿了么外卖".equals(item.get("payName"))){
					OnlineCusTotal += Integer.valueOf((String)item.get("count"));
				}
			}
			String _id = getId();
			String _s = null;			
			//****更新营业日报表标志位*******//
			_s = " update posbd.dbo.posDaybusiness set status = 1 where reportDate = '"+date+"' and companyCode = '"+companyCode+"' ";
			st.addBatch(_s);
			_s = "insert into POSBD.dbo.Day_Invt(id,companyCode,date) values('"+_id+"','"+companyCode+"','"+date+"')";
			st.addBatch(_s);		
			//*****统计差异总金额
			Double diffTurnover = 0.0;
			//*****统计预估总金额
			Double calTurnover = 0.0;
			for(HashMap item : list){
				String _p = "";
				_p +="'"+ _id;
				_p +="','"+item.get("GdsCode");
				_p +="','"+item.get("goodsNumber");
				_p +="','"+item.get("GoodsFullName");
				_p +="','"+item.get("BaseUnit");
				_p +="',"+item.get("ProSalesPrice");
				_p +=","+(item.get("yesterdayLevCount")!=null?item.get("yesterdayLevCount"):"0");
				_p +=","+(item.get("todayInCount")!=null?item.get("todayInCount"):"0");
				_p +=","+(item.get("todayTransferCount")!=null?item.get("todayTransferCount"):"0");
				_p +=","+(item.get("materialLossCount")!=null?item.get("materialLossCount"):"0");
				_p +=","+(item.get("productLossCount")!=null?item.get("productLossCount"):"0");
				_p +=","+(item.get("worthlessCount")!=null?item.get("worthlessCount"):"0");
				_p +=","+(item.get("tasteCount")!=null?item.get("tasteCount"):"0");
				_p +=","+(item.get("foodCount")!=null?item.get("foodCount"):"0");
				_p +=","+(item.get("returnCount")!=null?item.get("returnCount"):"0");
				_p +=","+(item.get("giftCount")!=null?item.get("giftCount"):"0");
				_p +=","+(item.get("posUseCount")!=null?item.get("posUseCount"):"0");
				_p +=","+(item.get("todayInvtCount")!=null?item.get("todayInvtCount"):"0");
				_p +=","+(item.get("diffCount")!=null?item.get("diffCount"):"0");
				_p +=","+(item.get("diffAmount")!=null?item.get("diffAmount"):"0");
				_p +=","+(item.get("calTurnover")!=null?item.get("calTurnover"):"0");
				_p +=","+(item.get("relTurnover")!=null?item.get("relTurnover"):"0");
				_p +=","+(item.get("diffTurnover")!=null?item.get("diffTurnover"):"0");
				_p +=","+(item.get("theoryInvtCount")!=null?item.get("theoryInvtCount"):"0");
				_p +=","+(item.get("flag")!=null?item.get("flag"):"0");
				_p +=","+(item.get("saleGift")!=null?item.get("saleGift"):"0");
				_p +=","+(item.get("retQty")!=null?item.get("retQty"):"0");
				
				_s = " insert into POSBD.dbo.Day_InvtDet(f_ref,goodsCode,goodsNum,goodsName,BaseUnit,Price,yesterdayLevCount,todayInCount,todayTransferCount,materialLossCount,productLossCount,worthlessCount,tasteLossCount,foodLossCount,returnCount,giftCount,posUseCount,todayInvtCount,difference,diffAmount,calTurnover,relTurnover,diffTurnover,theoryInvtCount,flag,saleGift,retQty) values("+_p+") ";
				
				st.addBatch(_s);
				
				if(item.get("diffTurnover")!=null){
					diffTurnover += Double.parseDouble(item.get("diffTurnover")+"");
				}
				if(item.get("calTurnover")!=null){
					calTurnover += Double.parseDouble(item.get("calTurnover")+"");
				}
			}
			if(diffTurnover > 300 || diffTurnover < -300){
				msg.setData(1);
			} else{
				msg.setData(0);
			}
			
			_s = " update posbd.dbo.posDaybusiness set calculationAmount="+calTurnover+",OnlineCusTotal = "+OnlineCusTotal+" where CompanyCode ='"+companyCode+"' and reportDate = '"+date+"'";
			st.addBatch(_s);
			
			st.executeBatch();			
			msg.setCode(1);		
		} catch(Exception e){
			msg.setCode(0);
			msg.setDescription(e.getMessage());			
			BaseEnv.log.error("_saveSQL error:",e);
		}
		return msg;
	}
	
	private boolean _execUpdate(Connection conn,String s){		
		Statement stmt = null;		
		try{
			stmt = conn.createStatement();
			int rs = stmt.executeUpdate(s);
			return true;
		} catch(Exception e){
			BaseEnv.log.error("_execUpdate error:",e);
			return false;
		} finally{
			
		}
	}
	
	private List<HashMap> _execSQL(Connection conn,String s){
		
		List<HashMap> list = new ArrayList<HashMap>();
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(s);			
			list = new ArrayList<HashMap>();
	
			ResultSetMetaData metaData = rs.getMetaData();  
			int columnCount = metaData.getColumnCount(); 
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
		        for (int i = 1; i <= columnCount; i++) {  
		            String columnName =metaData.getColumnLabel(i);  
		            String value = rs.getString(columnName); 
		            map.put(columnName, value);		                    
		        }		       
		        list.add(map);				
			}
		} catch(Exception e){
			return null;
		}
		return list;
	}
	
	//替换字符串内字符
	private String _replaceSQL(String str,String[] arr){
		String _s = str;		
		Matcher m = Pattern.compile("\\?").matcher(str);
		Integer i = 0;
		while(m.find()){
			_s = _s.replaceFirst("\\?", arr[i]);
			i++;
		}
		return _s;		
	}
}
