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
	
	//**查询订单信息
	public String getBill(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "登陆失效");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String BillNo = request.getParameter("BillNo");
		
		BaseEnv.log.debug("取出库单：商品："+BillNo);
		
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
                        		rst.setRetVal("本单已经审核完成");
                        		return;
                    		}else if(workFlowNodeName.equals("draft") && BillNo.startsWith("SS")){
                    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        		rst.setRetVal("本单是草稿，请先过帐再扫描");
                        		return;
                    		}
                    		
                    	}else{
                    		rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                    		rst.setRetVal("单据编号"+BillNo+"不存在");
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
	 * 出库扫描 ，扫描物流码
	 * 1、判断没有草稿，则自动建立草稿。
	 * 2、把序列号插入草稿
	 * @param request
	 * @return
	 */
	public String getSeq(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		
		final String Seq = request.getParameter("Seq");
		final String SalesOutId = request.getParameter("SalesOutId");
		final String BillNo = request.getParameter("BillNo");
		
		BaseEnv.log.debug("扫描出库：序号："+Seq);
		
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
                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                	            	}
                	            	warn = warn.getNextWarning();
                	            }
                            }	
                        	
                        }catch(SQLException ex){
                        	try{
                        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                    	            SQLWarning warn = cs.getWarnings();
                    	            while(warn != null){  
                    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
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
        BaseEnv.log.debug("扫描出库：序号："+Seq+";结果="+rst.getRetVal());
        
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
	 * 零售扫码取库存明细
	 * 输入物流码或箱号，查询返回商品库存物流码信息列表
	 * @param request
	 */
	public String getStockDetByBarcode(HttpServletRequest request) {
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "登陆失效");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String barcode = request.getParameter("barcode") == null ? null : request.getParameter("barcode").trim();
		final String departmentCode = request.getParameter("departmentCode") == null ? null : request.getParameter("departmentCode").trim();
		
		// 编码错误返回
		Boolean barcodeOk = false;
		Boolean isSeq = false;
		
		if (barcode != null){			
			// 物流码check
			if (barcode.length() == 13 && ("69559".equals(barcode.substring(0, 5)) || "79559".equals(barcode.substring(0, 5)))) {
				BaseEnv.log.debug("jay case1");
				barcodeOk = true;
				isSeq = true;
			} else if (barcode.length() == 13 && "888".equals(barcode.substring(0, 3))) {	// 箱号check - 纸箱
				BaseEnv.log.debug("jay case2");
				barcodeOk = true;
				isSeq = false;
			} else if (barcode.length() == 9 && "777".equals(barcode.substring(0, 3))) {	// 箱号check - 卡板（载桶装用）
				BaseEnv.log.debug("jay case3");
				barcodeOk = true;
				isSeq = false;
			} else {
				
			}
		}
		
		if(!barcodeOk){
			rMap.put("code", "QUERY_NO_ERR");
        	rMap.put("msg", "编码不符合规则 ");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		// 判断属于箱号还是物流码
		if(isSeq) {
			BaseEnv.log.debug("取商品库存信息：物流码：" + barcode);
		} else {
			BaseEnv.log.debug("取商品库存信息：箱号：" + barcode);
		}

		// 查询数据	
		final Boolean isSeqFinal = isSeq;	
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {   
                    	// 查询经销商等级
                    	String sql = "select [level] from fxAgent where tblDepartmentCode = ?";
                    	PreparedStatement pst = conn.prepareStatement(sql);
                    	pst.setString(1, departmentCode);
                    	
                    	ResultSet rset = pst.executeQuery();
                    	
                    	String level = "";
                    	if(rset.next()){           		                    		
                    		level =String.valueOf("level");
                    	}else{
                    		// 查询不到经销商等级
                    	}
                    	
                    	
                    	// 只取箱号中未出库的物流码（在tblStockDet中判断）
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
