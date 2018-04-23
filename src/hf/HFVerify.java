package hf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;

public class HFVerify extends AIODBManager{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	public String exec(HttpServletRequest request){
		String op = request.getParameter("op");
		if("barcode".equals(op)){
			return verifyBar(request);
		}else if("verifyBarcode".equals(op)){
			return verifyBarByEmp(request);
		}else{
			return "";
		}
	}
	
	
	/**
	 * 防伪验证（客户验证）
	 */
	public String verifyBarByEmp(HttpServletRequest request){
		final String barcode = request.getParameter("code");
		final String phone = request.getParameter("phone");
		final String way = request.getParameter("way");
		final Map ret = new HashMap();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	CallableStatement cs=null;
                        try {
                        	String sql = "{call AIOFW.dbo.mp_find_barcode(?,?,?)}";
                        	cs= conn.prepareCall(sql);
                        	cs.setString(1, barcode);
                        	cs.setString(2,phone);
                        	cs.setInt(3,Integer.parseInt(way));
                        	ResultSet rset = cs.executeQuery();
                        	if(rset.next()){
                        		ret.put("code", rset.getString("code"));
                        		ret.put("msg", rset.getString("msg"));                        		                        		                        		
                        		if(rset.getString("code").equals("2")){
                        			ret.put("barcode", rset.getString("barcode"));
                            		ret.put("spec", rset.getString("spec"));
                            		ret.put("goodsSpec", rset.getString("goodsSpec"));
                            		ret.put("type", rset.getInt("type"));
                            		ret.put("seq", rset.getString("seq"));
                            		ret.put("gift", rset.getInt("gift"));
                            		ret.put("inDate", rset.getString("inDate"));
                            		
                        		}                       		                      		                        		                        	                        	
                        	}else{
                        		ret.put("code", "-1");
                        		ret.put("msg", "没有相关记录.");
                        		return;
                        	}                        	                 	                        	
                        }catch (Exception e) {
                            BaseEnv.log.error("HFData.verify_barcode_emp",e);
                            ret.put("code","-1");
                            ret.put("msg", "查询失败！");
                            return;
                        }
                    }
                });
                return 1;
            }
        });
        return gson.toJson(ret);
	}
	
	/**
	 * 防伪验证(业务员扫码)
	 */
	public String verifyBar(HttpServletRequest request){
		final String barcode = request.getParameter("code");
		final String way = request.getParameter("way");
		final Map ret = new HashMap();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	CallableStatement cs=null;
                        try {
                        	String sql = "{call HF_verify_barcode(?,?)}";
                        	cs= conn.prepareCall(sql);
                        	cs.setString(1, barcode);                        	
                        	cs.setString(2, way);
                        	ResultSet rset = cs.executeQuery();
                        	if(rset.next()){
                        		ret.put("code", rset.getString("code"));
                        		ret.put("msg", rset.getString("msg"));                        		                        		                        		
                        		if(rset.getString("code").equals("2")){
                        			ret.put("barcode", rset.getString("barcode"));
                            		ret.put("spec", rset.getString("spec"));
                            		ret.put("goodsSpec", rset.getString("goodsSpec"));
                            		ret.put("type", rset.getInt("type"));
                            		ret.put("seq", rset.getString("seq"));
                        		}                        		
                        	}else{
                        		ret.put("code", "-1");
                        		ret.put("msg", "没有相关记录.");
                        		return;
                        	}                        	                 	                        	
                        }catch (Exception e) {
                            BaseEnv.log.error("HFData.verify_barcode",e);
                            ret.put("code","-1");
                            ret.put("msg", "查询失败！");
                            return;
                        }
                    }
                });
                return 1;
            }
        });
        return gson.toJson(ret);
	}
}
