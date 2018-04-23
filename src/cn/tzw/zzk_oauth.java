package cn.tzw;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class zzk_oauth extends AIODBManager {
	
	private static HashMap<String,String> tokenMap = new HashMap<String,String>();
	
	public String zzkOauthReturn(HttpServletRequest request) throws Exception
    {
		String action = request.getParameter("action");
        if ("return".equals(action))
        {
        	String uid = request.getParameter("uid");
        	String token = request.getParameter("token");
            if (tokenMap.get("zzk_user_code" + uid) != null && tokenMap.get("zzk_user_code" + uid).equals(token))
            {
            	BaseEnv.log.debug("zzk_oauth  取得"+uid+"的单点登陆对象");
            	return tokenMap.get("zzk_user_codeStr" + uid);
                
            }else if (tokenMap.get("zzk_user_code" + uid) == null)
            {
            	String sql  = " select a.id,b.agtName,agtSimpleName,corporation,sysName,b.corporationEmail,b.tel,b.url from tblEmployee a join tzAgent b on a.agentId=b.id where a.id=? ";
            	ArrayList param = new ArrayList();
            	param.add(uid);
            	Result rs = sqlList(sql, param);
            	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs.retVal).size()>0){
            		Object[] map = ((ArrayList<Object[]>)rs.retVal).get(0);
            		StringBuilder sb = new StringBuilder();
                    sb.append("<xml><status>1</status><uid>"+map[0]+"</uid>");
                    sb.append("<agent_name>"+map[1]+"</agent_name><agent_short_name>"+map[2]+"</agent_short_name>");
                    sb.append("<legal_person>"+map[3]+"</legal_person>");
                    sb.append("<user_name>"+map[4]+"</user_name>");
                    sb.append("<email>"+map[5]+"</email><tel>"+map[6]+"</tel><site>"+map[7]+"</site>");                    
                    sb.append("</xml>");
                    tokenMap.put("zzk_user_codeStr" + uid, sb.toString());
                    tokenMap.put("zzk_user_code" + uid, token);
                    BaseEnv.log.debug("zzk_oauth  "+uid+"的单点登陆读取对象成功"+sb.toString());
                    return tokenMap.get("zzk_user_codeStr" + uid);
            	}else{
            		BaseEnv.log.error("zzk_oauth  "+uid+"的单点登陆读取对象失败"+rs.retVal);
            		return ("<xml><status>0</status><msg>数据验证不通过，请重新登陆</msg></xml>");
            	}
            }
            else
            {
            	BaseEnv.log.error("zzk_oauth  "+uid+"的单点登陆token不正确");
            	return ("<xml><status>0</status><msg>数据验证不通过，请重新登陆</msg></xml>");
            }
        }else if ("getToken".equals(action)){
        	String uid = request.getParameter("uid");
        	if (tokenMap.get("zzk_user_code" + uid) != null )
            {
        		return tokenMap.get("zzk_user_code" + uid);
            }else{
            	return IDGenerater.getId();
            }
        }
        return "<xml><status>0</status><msg>参数不正确</msg></xml>";
    }
}
