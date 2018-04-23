package hf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class HFDistribute extends AIODBManager{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	public String exec(HttpServletRequest request){
		String op = request.getParameter("op");
		if("GetSeq".equals(op)){
			return getSeq(request);
		} else if("getBill".equals(op)){
			return getBill(request);
		} else if("getStockDetByBarcode".equals(op)){
			return getStockDetByBarcode(request);
		} else{
			return "";
		}
	}
	
	//**��ѯ������Ϣ
	public String getBill(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "��½ʧЧ");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String BillNo = request.getParameter("BillNo");
		
		BaseEnv.log.debug("ȡ���ⵥ����Ʒ��"+BillNo);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	String sql = "";
                    	if(BillNo.startsWith("SS")){
                    		sql  = " select a.id, BillNo,BillDate,a.workFlowNodeName from fxSalesOutStock a  " + "where BillNo=? ";
                    	}else{
                    		sql = "  select a.id, BillNo,BillDate,a.workFlowNodeName from fxRetailOrder a  " + "where BillNo=?  ";
                    	}
                    	
                    	PreparedStatement pst = conn.prepareStatement(sql);
                    	pst.setString(1, BillNo);
                    	ResultSet rset = pst.executeQuery();
                    	String id="";
                    	if(rset.next()){
                    		rMap.put("BillNo", BillNo);
                    		rMap.put("BillDate", rset.getString("BillDate"));                    		                    		
                    		id = rset.getString("id");
                    		rMap.put("id", id);
                    		
                    		String workFlowNodeName = rset.getString("workFlowNodeName");
                    		if(workFlowNodeName.equals("finish") && BillNo.startsWith("SS")){
                    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        		rst.setRetVal("�����Ѿ�������");
                        		return;
                    		}else if(workFlowNodeName.equals("draft") && BillNo.startsWith("SS")){
                    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        		rst.setRetVal("�����ǲݸ壬���ȹ�����ɨ��");
                        		return;
                    		}
                    		
                    	}else{
                    		rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                    		rst.setRetVal("���ݱ��"+BillNo+"������");
                    		return;
                    	}
                    	                    	
                    	if(BillNo.startsWith("SS")){
                    		sql = " select a.id,GoodsCode,GoodsNumber,Qty,ScanQty,Qty-ScanQty NoScanQty from fxSalesOutStockDet a join AIOERP.dbo.tblGoods b on a.GoodsCode=b.classCode where f_ref=?  ";
                    	}else{
                    		sql = " select a.id,GoodsCode,GoodsNumber,Qty,ScanQty,Qty-ScanQty NoScanQty from fxRetailOrderDet a join AIOERP.dbo.tblGoods b on a.GoodsCode=b.classCode where f_ref=?  ";
                    	}
                    	
                    	pst = conn.prepareStatement(sql);
                    	pst.setString(1, id);
                    	rset = pst.executeQuery();
                    	ArrayList gList = new ArrayList();
                    	rMap.put("Dets", gList);
                    	while(rset.next()){
                    		HashMap map = new HashMap();
                    		map.put("GoodsNumber", rset.getString("GoodsNumber"));
                    		map.put("GoodsCode", rset.getString("GoodsCode"));
                    		map.put("Qty", rset.getInt("Qty"));
                    		map.put("ScanQty", rset.getInt("ScanQty"));
                    		map.put("NoScanQty", rset.getInt("NoScanQty"));
                    		map.put("id", rset.getString("id"));
                    		gList.add(map);
                    	}
                    	
                    	if(BillNo.startsWith("SS")){
                        	sql = " select yearNo,Seq from fxSalesOutStockScan a  where f_ref=?  ";
                    	}else{
                    		sql = " select yearNo,Seq from fxRetailOrderScan a  where f_ref=?  ";
                    	}
                    	
                    	pst = conn.prepareStatement(sql);
                    	pst.setString(1, id);
                    	rset = pst.executeQuery();
                    	gList = new ArrayList();
                    	rMap.put("Seqs", gList);
                    	while(rset.next()){
                    		HashMap map = new HashMap();
                    		map.put("yearNo", rset.getString("yearNo"));
                    		map.put("Seq", rset.getString("Seq"));
                    		gList.add(map);
                    	}
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		
		ret = gson.toJson(rMap);
		return ret;
	}		
	
	/**
	 * ����ɨ�� ��ɨ��������
	 * 1���ж�û�вݸ壬���Զ������ݸ塣
	 * 2�������кŲ���ݸ�
	 * @param request
	 * @return
	 */
	public String getSeq(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		
		final String Seq = request.getParameter("Seq");
		final String SalesOutId = request.getParameter("SalesOutId");
		final String BillNo = request.getParameter("BillNo");
		
		BaseEnv.log.debug("ɨ����⣺��ţ�"+Seq);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	CallableStatement cs=null;
                        try {
                        	String sql = "";
                        	if(BillNo.startsWith("SS")){
                        		sql = "{call proc_scanSeq(?,?,?,?)}";
                        	}else{
                        		sql = "{call proc_scanRetailSeq(?,?,?,?)}";
                        	}
                        	
                        	cs= conn.prepareCall(sql);
                        	cs.setString(1, SalesOutId);
                        	cs.setString(2, Seq);
                        	cs.registerOutParameter(3, Types.BIGINT);
                        	//cs.registerOutParameter(3, Types.INTEGER);
                            cs.registerOutParameter(4, Types.VARCHAR, 50);
                            cs.execute();
                            rst.setRetCode(cs.getInt(3));
                            rst.setRetVal(cs.getString(4));
                            
                            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                	            SQLWarning warn = cs.getWarnings();
                	            while(warn != null){  
                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("����ֱ��ִ�� SQL�����α�") == -1){
                	            		BaseEnv.log.debug("�洢�����ڲ���Ϣ�� "+warn.getMessage());
                	            	}
                	            	warn = warn.getNextWarning();
                	            }
                            }	
                        	
                        }catch(SQLException ex){
                        	try{
                        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                    	            SQLWarning warn = cs.getWarnings();
                    	            while(warn != null){  
                    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("����ֱ��ִ�� SQL�����α�") == -1){
                    	            		BaseEnv.log.debug("�洢�����ڲ���Ϣ�� "+warn.getMessage());
                    	            	}
                    	            	warn = warn.getNextWarning();
                    	            }
                                }
                        	} catch(SQLException ex2){
                        		BaseEnv.log.error("HFDistribute.getSeq Error",ex);
                        	}
                        	BaseEnv.log.error("HFDistribute.getSeq Error",ex);
                        	BaseEnv.log.error("HFDistribute.getSeq",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        } catch (Exception ex) {
                            BaseEnv.log.error("HFDistribute.getSeq",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        BaseEnv.log.debug("ɨ����⣺��ţ�"+Seq+";���="+rst.getRetVal());
        
        String retstr=null;
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
	        retstr = rst.getRetVal()+"";
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		ret = gson.toJson(rMap);
		if(retstr != null) { 
			ret = ret.substring(0,ret.length()-1)+","+retstr+"}";
		} 
		return ret;
	}
	
	/**
	 * ����ɨ��ȡ�����ϸ
	 * �������������ţ���ѯ������Ʒ�����������Ϣ�б�
	 * @param request
	 */
	public String getStockDetByBarcode(HttpServletRequest request) {
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "��½ʧЧ");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String barcode = request.getParameter("barcode") == null ? null : request.getParameter("barcode").trim();
		final String departmentCode = request.getParameter("departmentCode") == null ? null : request.getParameter("departmentCode").trim();
		
		// ������󷵻�
		Boolean barcodeOk = false;
		Boolean isSeq = false;
		
		if (barcode != null){			
			// ������check
			if (barcode.length() == 13 && ("69559".equals(barcode.substring(0, 5)) || "79559".equals(barcode.substring(0, 5)))) {
				BaseEnv.log.debug("jay case1");
				barcodeOk = true;
				isSeq = true;
			} else if (barcode.length() == 13 && "888".equals(barcode.substring(0, 3))) {	// ���check - ֽ��
				BaseEnv.log.debug("jay case2");
				barcodeOk = true;
				isSeq = false;
			} else if (barcode.length() == 9 && "777".equals(barcode.substring(0, 3))) {	// ���check - ���壨��Ͱװ�ã�
				BaseEnv.log.debug("jay case3");
				barcodeOk = true;
				isSeq = false;
			} else {
				
			}
		}
		
		if(!barcodeOk){
			rMap.put("code", "QUERY_NO_ERR");
        	rMap.put("msg", "���벻���Ϲ��� ");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		// �ж�������Ż���������
		if(isSeq) {
			BaseEnv.log.debug("ȡ��Ʒ�����Ϣ�������룺" + barcode);
		} else {
			BaseEnv.log.debug("ȡ��Ʒ�����Ϣ����ţ�" + barcode);
		}

		// ��ѯ����	
		final Boolean isSeqFinal = isSeq;	
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {   
                    	// ��ѯ�����̵ȼ�
                    	String sql = "select [level] from fxAgent where tblDepartmentCode = ?";
                    	PreparedStatement pst = conn.prepareStatement(sql);
                    	pst.setString(1, departmentCode);
                    	
                    	ResultSet rset = pst.executeQuery();
                    	
                    	String level = "";
                    	if(rset.next()){           		                    		
                    		level =String.valueOf("level");
                    	}else{
                    		// ��ѯ���������̵ȼ�
                    	}
                    	
                    	
                    	// ֻȡ�����δ����������루��tblStockDet���жϣ�
                    	sql = 
	            			" SELECT tblGoods.classCode AS goodsCode, tblGoods.GoodsNumber AS goodsNumber,  " 
	            			+ " tblGoods.GoodsFullName AS goodsFullName, tblGoods.GoodsSpec AS goodsSpec,  " 
	            			+ " tblGoods.BaseUnit AS baseUnit, tblStockDet.BatchNo AS batchNo,  " 
	            			+ " tblStockDet.yearNO AS yearNO, tblStockDet.Seq as seq,  " 
	            			+ " price.level1,price.level2,price.level3,price.level4,price.level5,price.level6,price.level7,price.level8,price.level9,price.level10 " 
	            			+ " FROM tblStockDet tblStockDet  " 
	            			+ " left join PDCompanyPriceDet price on price.GoodsCode = tblStockDet.GoodsCode " 
	            			+ " JOIN AIOERP.dbo.tblgoods tblGoods ON tblGoods.classCode = tblStockDet.GoodsCode  " 
	            			+ " LEFT JOIN AIOERP.dbo.tblGoodsUnit tblGoodsUnit ON tblGoods.id = tblGoodsUnit.f_ref  ";

						if(isSeqFinal) {
							sql += " WHERE tblStockDet.Seq = ? ";
						} else {
							sql += " WHERE tblStockDet.yearNO = ? ";
						}
                    	sql = sql + " and totalQty > 0  " 
                    			+ " and not exists (select 1 from aioerp.dbo.tblStockDet b where tblStockDet.Seq = b.Seq and tblStockDet.BillDate < b.BillDate) ";
                    	pst = conn.prepareStatement(sql);
//                    	BaseEnv.log.debug("barcode:" + barcode);
                    	pst.setString(1, barcode);
                    	
                    	rset = pst.executeQuery();
                    	ArrayList gList = new ArrayList();
                    	while(rset.next()){
                    		HashMap map = new HashMap();
                    		map.put("goodsCode", rset.getString("goodsCode"));
                    		map.put("goodsNumber", rset.getString("goodsNumber"));
                    		map.put("goodsFullName", rset.getString("goodsFullName"));
                    		map.put("goodsSpec", rset.getString("goodsSpec"));
                    		map.put("baseUnit", rset.getString("baseUnit"));
                    		map.put("batchNo", rset.getString("batchNo"));
                    		map.put("yearNO", rset.getString("yearNO"));
                    		map.put("seq", rset.getString("seq"));
                    		if (level.length() > 0) {
                    			if ("1".equals(level))
                    				map.put("price", rset.getString("level1"));
                    			else if ("1".equals(level))
                    				map.put("price", rset.getString("level1"));
                    			else if ("2".equals(level))
                    				map.put("price", rset.getString("level2"));
                    			else if ("3".equals(level))
                    				map.put("price", rset.getString("level3"));
                    			else if ("4".equals(level))
                    				map.put("price", rset.getString("level4"));
                    			else if ("5".equals(level))
                    				map.put("price", rset.getString("level5"));
                    			else if ("6".equals(level))
                    				map.put("price", rset.getString("level6"));
                    			else if ("7".equals(level))
                    				map.put("price", rset.getString("level7"));
                    			else if ("8".equals(level))
                    				map.put("price", rset.getString("level8"));
                    			else if ("9".equals(level))
                    				map.put("price", rset.getString("level9"));
                    			else if ("10".equals(level))
                    				map.put("price", rset.getString("level10"));
                    		}
                    		
                    		gList.add(map);

                        	BaseEnv.log.debug("map:" + map);
                    	}
                    	rMap.put("seqInfos", gList);
                    	BaseEnv.log.debug("sql:" + sql);
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		
		ret = gson.toJson(rMap);
		return ret;
	}

}
