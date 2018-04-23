package hf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class HFMap extends AIODBManager{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	public String exec(HttpServletRequest request){
		String op = request.getParameter("op");
		if("GetHTInf".equals(op)){
			return getHTInf(request);
		}else{
			return null;
		}
	}
	private String getHTInf(HttpServletRequest request){
		
		Map map = new HashMap();
		Result ret = getHTData();
		if(ret.retCode  == ErrorCanst.DEFAULT_SUCCESS){
			map.put("code",1);
			map.put("data",ret.retVal);
		}else{
			map.put("code",0);
		}
		return new Gson().toJson(map);
	}
	
	private Result getHTData(){
		final Result result = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try {
							StringBuffer sql = new StringBuffer();
							sql.append(" select a.ComFullName,a.ComAddress,(case when b.id is not null and b.id !='' then b.id else '' end) as ht from tblCompany a left join tblContract b on a.id = b.CompanyCode where a.statusID = 0 ");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							
							while(rs.next()){								
								Map m = new HashMap();
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}
								}
								data.add(m);
							}							
								result.retVal = data;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.query:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}               		               
                    }
                });
                return result.getRetCode();
            }
        });
        result.setRetCode(retCode);
		return result;
	}
}
