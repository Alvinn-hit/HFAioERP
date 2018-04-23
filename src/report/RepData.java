package report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class RepData extends AIODBManager{
	private static Gson gson;
	private static String url;
	private static String key;
	private static String comSQL;
	private static String goodSQL;
	private static String dailyBusinessPro;
	private static String storesWeight;
	private static String logWeight;
	private static String excelPath;
	private static String bdProLoss;
	private static String bd2SalesDet;
	private static String comsScpe;		
	
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
		url = "http://bd2.krrj.cn:8081/Servlet/action.htm";
		//url = "http://localhost:8888/Servlet/action.htm";
		key = "6D4E60E55552386C759569836DC0F83869836DC0F838C0F7";		
		comSQL = "select classCode,ComNumber,ComFullName,ComFullNamePYM from tblCompany where (isnull(ComNumber,'')+''+ComFullName+''+ComFullNamePYM) like '%?%' and Account1 in ('加盟店','直营店','类直营店')";
		//goodSQL = "select b.classCode,b.GoodsFullName,b.ProSalesPrice,goodsNumber from AIOBD.dbo.posGoods b where b.isCatalog='0' and substring(b.classCode,1,5) in ('00034','0002T','0002U','0002V','0002W','0002X','0002Y','0003R') and statusId = 0 and (GoodsNumber+''+GoodsFullName) like '%?%'";
		goodSQL = "select b.name as GoodsFullName,b.price as ProSalesPrice,b.goodsNo as goodsNumber from AIOBD.dbo.posGoods b where b.isCatalog='0'  and statusId = 0 and (convert(varchar(20),b.goodsNo)+''+b.name) like '%?%'";
		dailyBusinessPro ="exec POSSERVER.dbo.PosDailyBusiness '?','?'";
		storesWeight = "exec POSSERVER.dbo.PosDailyBusiness";
		logWeight = "{call POSSERVER.dbo.logWeight('?','?','?','?','?','?')}";
		excelPath = "D:/KoronSoft/AIOBD/website/service/report/tmp/";
		//excelPath = "D:/web/tomcat7/webapps/tmp/";
		
		//****bd成品损耗数据
		bdProLoss ="select * from posbd.dbo.posCPSH a join posBD.dbo.posCPSHDet b on a.id = b.f_ref left join AIOBD.dbo.posGoods c on b.GoodsCode = c.goodsNo where a.BillDate between ? and ?  and a.CompanyCode= ?";
		
		//****bd2销售数据
		bd2SalesDet ="select * ,ROW_NUMBER() over( order by year asc,month asc,day asc,No asc) as row_id from(select DATENAME(YEAR,CAST(a.date as datetime)) as year,DATENAME(MONTH,CAST(a.date as datetime)) as month,DATENAME(DAY,CAST(a.date as datetime)) as day, c.ComFullName,(SUBSTRING(a.id,0,6)+SUBSTRING(a.id,10,LEN(a.id))) as No,'正常销售' as salesType,e.name lb,d.name,d.unit,b.Price,Convert(decimal(18,2),sum(b.Price*b.qty)) TotalAccount ,sum(b.qty) qty from bdMDSales a join bdMDSalesDet b on a.id = b.f_ref left join CompanyPOS c on a.syjNo = c.syjNo left join posGoods d on b.GoodsCode = d.goodsNo left join POSSERVER.dbo.posGoodsCat e on d.lb = e.lb  where 1=1 #{condition} group by b.GoodsCode,c.ComFullName,b.Price,d.unit,e.name,a.Date,c.companyCode,a.id,d.name)t";
		//bd2SalesDet ="select * ,ROW_NUMBER() over( order by year asc,month asc,day asc,No asc) as row_id from(select DATENAME(YEAR,CAST(a.date as datetime)) as year,DATENAME(MONTH,CAST(a.date as datetime)) as month,DATENAME(DAY,CAST(a.date as datetime)) as day, c.ComFullName,(c.companyCode+SUBSTRING(a.id,10,LEN(a.id))) as No,'正常销售' as salesType,e.name lb,d.name,d.unit,b.Price,Convert(decimal(18,2),sum(b.Price*b.qty)) TotalAccount ,sum(b.qty) qty from bdMDSales a join bdMDSalesDet b on a.id = b.f_ref left join CompanyPOS c on a.syjNo = c.syjNo left join posGoods d on b.GoodsCode = d.goodsNo left join POSSERVER.dbo.posGoodsCat e on d.lb = e.lb  where 1=1 #{condition} group by b.GoodsCode,c.ComFullName,b.Price,d.unit,e.name,a.Date,c.companyCode,a.id,d.name)t";
		//excelPath = "D:/AIO/website/service/report/tmp/";
		//logWeight = "exec POSSERVER.dbo.logWeight";
	
		//****当前登录用户门店权限****//
		comsScpe = "select companyId,tblCompany.classCode as CompanyCode from tblparticipant join tblparticipantDet on tblparticipant.id = tblparticipantDet.f_ref join tblCompany on tblCompany.id = tblparticipantDet.companyId where tblparticipant.Emp = '?'";
	}
	
	public String exec(HttpServletRequest request){	
		String op = request.getParameter("op");
		if("getAllInvt".equals(op)){
			List<HashMap> list = _getAllInvt(request);
			Map mp = new HashMap();
			if(list != null){
				mp.put("code", "OK");
				mp.put("data", list);
			} else{
				mp.put("code", "Error");
			}
			String ret = gson.toJson(mp,HashMap.class);
			return ret;
		} else if("getSingleInvt".equals(op)){
			HashMap _r = _getSingleInvt(request);
			Map mp = new HashMap();
			if(_r != null){
				mp.put("code","OK");
				mp.put("data",_r.get("data"));
			} else{
				mp.put("code","Error");
			}
			String ret = gson.toJson(mp,HashMap.class);
			return ret;
			
		} else if("queryCom".equals(op)){
			return _getCom(request);
		} else if("queryGood".equals(op)){
			return _getGood(request);
		} else if("genInvt".equals(op)){
			return _genInvt(request);
		} else if("getDailyBussiness".equals(op)){
			return _getDailyBusiness(request);
		} else if("getStoresWeight".equals(op)){
			return _getStoresWeight(request);
		} else if("getLogWeight".equals(op)){
			//返回日志结果集
			return _getLogWeight(request);
		} else if("getSalesRank".equals(op)){
			//返回销售排行
			return _getSalesRank(request);
		} else if("genSalesRankExcel".equals(op)){
			//返回生成文件名
			return _genSalesRankExcel(request);
		} else if("getPackageRank".equals(op)){
			//返回套餐销售排行
			return _getPackageRank(request);
		} else if("genPackageRankExcel".equals(op)){
			//返回生成文件名
			return _genPackageRankExcel(request);
		} else if("getSalesInf".equals(op)){
			//返回门店销售数据
			return _getSalesInf(request);
		} else if("getSalesInfExcel".equals(op)){
			//返回门店销售数据
			return _getSalesInfExcel(request);
		}
		return null;
	}
	
	
private Map _getSalesInfData(final String companyCode,final String GoodsCode,final String GoodsType,final String begD,final String endD){
		
		final String bdSQL = bdProLoss;
		
		
		Map map = new HashMap();
		String condition = "";
		
		if(begD != null && endD != null){
			condition += " and a.Date between '"+begD+"' and '"+endD+"'";
		}
		if(companyCode != null){
			condition += " and c.companyCode in ('"+companyCode+"')";
			//condition += " and c.companyCode = '"+companyCode+"'";
		}
		
		if(GoodsType != null && !"all".equals(GoodsType)){
			condition += " and d.lb = "+GoodsType;
		}
		
		if(GoodsCode != null && !"".equals(GoodsCode)){
			condition += " and b.GoodsCode='"+GoodsCode+"'";
		}
		
		
		String bd2SQL =bd2SalesDet.replaceAll("#\\{condition\\}",condition);
		//******获取bd2销售数据******//
		//加密
		String sql = _3DesEncode(bd2SQL,key);
		if(sql == null){
			BaseEnv.log.error("_getPackageRank：数据加密失败");
			map.put("code","Error");
			return map;
		}		
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (Exception e) {			
			map.put("code","Error");
			return map;
		}
		List<HashMap> r = _getRemoteData(sql);
		
		map.put("code", "OK");
		map.put("data", r);
		//**********end**********//
		return map;
	}
	
	private String _getSalesInf(HttpServletRequest request){
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
		String GoodsCode = request.getParameter("GoodsCode");	
		String GoodsType = request.getParameter("GoodsType");
		
		LoginBean usrBean = getLoginBean(request);
		Map rMap = new HashMap();
		if(usrBean == null){			
			rMap.put("msg","未登录");
			rMap.put("code", "Error");
			return gson.toJson(rMap);
			
		}
		//*****获取门店范围*******//
		//****获取门店权限信息		
		if("".equals(companyCode)){				
			ArrayList param = new ArrayList();
			String[] e = {usrBean.getId()};
			String sql2 = _replaceSQL(comsScpe,e);
			Result rs = sqlListMap(sql2,param);	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
				ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
				if(list.size()>0){					
					for(HashMap item : list){
						companyCode += ("".equals(companyCode) ? "":"','")+item.get("CompanyCode");
					}							
				} 							
			}else{
				rMap.put("msg","加载门店信息失败");
				rMap.put("code", "Error");
				return gson.toJson(rMap);
			}
		}
		
		//*****获取bd2销售数据*****//
		Map ret =_getSalesInfData(companyCode,GoodsCode,GoodsType,begD,endD);
		//********end*******//
		
		Map msg = new HashMap();
		if("OK".equals(ret.get("code"))){
			msg.put("code", "OK");
			msg.put("data", ret.get("data"));
		} else{
			msg.put("code", "Error");
			msg.put("description", "无相关数据！");
		}
		String rs = gson.toJson(msg,Map.class);
		return rs;
	}
	
	private String _getSalesInfExcel(HttpServletRequest request){
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
	    String GoodsCode = request.getParameter("GoodsCode");	
		String GoodsType = request.getParameter("GoodsType");
		
		String fileName = IdGenerated.getId()+".xls";
		String filePath = excelPath + fileName;
		//*****获取bd2销售数据*****//
		Map ret =_getSalesInfData(companyCode,GoodsCode,GoodsType,begD,endD);
		//********end*******//		
		Map mp = new HashMap();
		if("Error".equals(ret.get("code"))){
			mp.put("code", "Error");
			mp.put("description", "无相关数据！");
			
		} else{
			
			List<HashMap> data =(List<HashMap>) ret.get("data");
			//******生成Excel
			GenExcel gen = new GenExcel();
			HashMap head = new HashMap();
			head.put("row_id", "序号");
			head.put("year", "年份");
			head.put("month", "月");
			head.put("day", "日");
			head.put("ComFullName", "门店名称");
			head.put("No", "打票单据号");		
			head.put("salesType", "销售属性");						
			head.put("lb", "品类");
			head.put("name", "产品名称");	
			head.put("unit", "单位");	
			head.put("qty", "数量");	
			head.put("TotalAccount", "金额");	
			data.add(0,head);
			String fields = "row_id,year,month,day,ComFullName,No,salesType,lb,name,unit,qty,TotalAccount";
			boolean r = gen.genExcel(filePath, data,fields);
			
			if(r){
				mp.put("code", "OK");
				mp.put("data", fileName);
			} else{
				mp.put("code", "Error");
				mp.put("description", "文件导出失败！");
			}
		}
		
		
		String res = gson.toJson(mp,HashMap.class);
		return res;
	}
	
	private String _genPackageRankExcel(HttpServletRequest request){
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
		String GoodsCode = request.getParameter("GoodsCode");				
		
		String fileName = IdGenerated.getId()+".xls";
		String filePath = excelPath + fileName;
		String sql = "exec PackageRank '"+(companyCode == null ? "":companyCode)+"','"+(GoodsCode == null ? "":GoodsCode)+"','"+(begD == null ? "": begD)+"','"+(endD == null ? "":endD)+"'";
				
		//加密
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_getPackageRank：数据加密失败");
			return null;
		}		
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (UnsupportedEncodingException e) {			
			return null;
		}
				
		List<HashMap> data = _getRemoteData(sql);		
		HashMap mp = new HashMap();
		if(data==null){
			mp.put("code", "Error");
			mp.put("description", "无相关数据！");
		} 
		//******生成Excel
		GenExcel gen = new GenExcel();
		HashMap head = new HashMap();
		head.put("no", "序号");
		head.put("ComFullName", "门店");
		head.put("year", "年度");
		head.put("month", "月份");
		head.put("day", "日");		
		head.put("name", "产品名称");						
		head.put("salesCount", "销量");
		head.put("salesAmount", "销额");		
		data.add(0,head);
		String fields = "no,ComFullName,year,month,day,name,salesCount,salesAmount";
		boolean r = gen.genExcel(filePath, data,fields);
		
		if(r){
			mp.put("code", "OK");
			mp.put("data", fileName);
		} else{
			mp.put("code", "Error");
			mp.put("description", "文件导出失败！");
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	
	}
	
	private String _getPackageRank(HttpServletRequest request){
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
		String GoodsCode = request.getParameter("GoodsCode");				
		
		LoginBean usrBean = getLoginBean(request);
		Map rMap = new HashMap();
		if(usrBean == null){			
			rMap.put("msg","未登录");
			rMap.put("code", "Error");
			return gson.toJson(rMap);
			
		}
		//****获取门店权限信息
		if("".equals(companyCode)){				
			ArrayList param = new ArrayList();
			String[] e = {usrBean.getId()};
			String sql2 = _replaceSQL(comsScpe,e);
			Result rs = sqlListMap(sql2,param);	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
				ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
				if(list.size()>0){					
					for(HashMap item : list){
						companyCode += ("".equals(companyCode) ? "":",")+item.get("CompanyCode");
					}							
				} 							
			}else{
				rMap.put("msg","加载门店信息失败");
				rMap.put("code", "Error");
				return gson.toJson(rMap);
			}
		}
		
		
		String sql = "exec PackageRank '"+(companyCode == null ? "":companyCode)+"','"+(GoodsCode == null ? "":GoodsCode)+"','"+(begD == null ? "": begD)+"','"+(endD == null ? "":endD)+"'";
				
		//加密
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_getPackageRank：数据加密失败");
			return null;
		}		
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (UnsupportedEncodingException e) {			
			return null;
		}
				
		List<HashMap> data = _getRemoteData(sql);		
		HashMap mp = new HashMap();
		if(data==null){
			mp.put("code", "Error");
			mp.put("description", "无相关数据！");
		} else{
			mp.put("code", "OK");
			mp.put("data", data);
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	}
	
	private String _genSalesRankExcel(HttpServletRequest request){
		HashMap mp = new HashMap();
		String fileName = IdGenerated.getId()+".xls";
		String filePath = excelPath + fileName;
		//*****获取数据*****//
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
		String GoodsCode = request.getParameter("GoodsCode");				
		
		String sql = "exec SalesRank '"+(companyCode == null ? "":companyCode)+"','"+(GoodsCode == null ? "":GoodsCode)+"','"+(begD == null ? "": begD)+"','"+(endD == null ? "":endD)+"','',''";
				
		//加密
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_genSalesRankExcel：数据加密失败");
			return null;
		}		
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (Exception e) {			
			BaseEnv.log.error("_genSalesRankExcel：数据转码失败：",e);
			mp.put("code", "Error");
			mp.put("description", "数据转码失败！");
			return gson.toJson(mp,HashMap.class);
		}
				
		List<HashMap> data = _getRemoteData(sql);		
		
		if(data==null){
			mp.put("code", "Error");
			mp.put("description", "无相关数据！");			
		} 
		
		//******生成Excel
		GenExcel gen = new GenExcel();
		HashMap head = new HashMap();
		head.put("no", "序号");
		head.put("ComFullName", "门店");
		head.put("year", "年度");
		head.put("month", "月份");
		head.put("day", "日");
		head.put("type", "产品类别");
		head.put("name", "产品名称");
		head.put("GoodsSpec", "产品规格");
		head.put("unit", "单位名称");
		head.put("ProSalesPrice", "产品成本");
		head.put("salesCount", "销量");
		head.put("salesAmount", "销额");		
		data.add(0,head);
		String fields = "no,ComFullName,year,month,day,type,name,GoodsSpec,unit,ProSalesPrice,salesCount,salesAmount";
		boolean r = gen.genExcel(filePath, data,fields);
		
		if(r){
			mp.put("code", "OK");
			mp.put("data", fileName);
		} else{
			mp.put("code", "Error");
			mp.put("description", "文件导出失败！");
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	}
	
	private String _getSalesRank(HttpServletRequest request){
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String companyCode = request.getParameter("companyCode");
		String GoodsCode = request.getParameter("GoodsCode");				
		
		//****获取页数****//
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		
		LoginBean usrBean = getLoginBean(request);
		Map rMap = new HashMap();
		if(usrBean == null){			
			rMap.put("msg","未登录");
			rMap.put("code", "Error");
			return gson.toJson(rMap);
			
		}
		//****获取门店权限信息
		if("".equals(companyCode)){				
			ArrayList param = new ArrayList();
			String[] e = {usrBean.getId()};
			String sql2 = _replaceSQL(comsScpe,e);
			Result rs = sqlListMap(sql2,param);	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
				ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
				if(list.size()>0){					
					for(HashMap item : list){
						companyCode += ("".equals(companyCode) ? "":",")+item.get("CompanyCode");
					}							
				} 							
			}else{
				rMap.put("msg","加载门店信息失败");
				rMap.put("code", "Error");
				return gson.toJson(rMap);
			}
		}
		
		String sql = "exec SalesRank '"+(companyCode == null ? "":companyCode)+"','"+(GoodsCode == null ? "":GoodsCode)+"','"+(begD == null ? "": begD)+"','"+(endD == null ? "":endD)+"','"+(pageNo == null ? "":pageNo)+"','"+(pageSize == null ? "":pageSize)+"'";
				
		//加密
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_getSalesRank：数据加密失败");
			return null;
		}		
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (UnsupportedEncodingException e) {			
			return null;
		}				
		List<HashMap> data = _getRemoteData(sql);		
		HashMap mp = new HashMap();
		if(data==null){
			mp.put("code", "Error");
			mp.put("description", "无相关数据！");
		} else{
			mp.put("code", "OK");
			mp.put("data", data);			
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	}
	
	private String _getLogWeight(HttpServletRequest request){		
		String begD = request.getParameter("begD");
		String endD = request.getParameter("endD");
		String syjNo = request.getParameter("syjNo");
		String status = request.getParameter("status");
		String goodsNo = request.getParameter("goodNo");
		String companyCode = request.getParameter("companyCode");
				
		LoginBean usrBean = getLoginBean(request);
		Map rMap = new HashMap();
		if(usrBean == null){			
			rMap.put("msg","未登录");
			rMap.put("code", "Error");
			return gson.toJson(rMap);
			
		}
		//****获取门店权限信息
		if("".equals(companyCode)){				
			ArrayList param = new ArrayList();
			String[] e = {usrBean.getId()};
			String sql2 = _replaceSQL(comsScpe,e);
			Result rs = sqlListMap(sql2,param);	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
				ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
				if(list.size()>0){					
					for(HashMap item : list){
						companyCode += ("".equals(companyCode) ? "":",")+item.get("CompanyCode");
					}							
				} 							
			}else{
				rMap.put("msg","加载门店信息失败");
				rMap.put("code", "Error");
				return gson.toJson(rMap);
			}
		}
		
		String[] p = {companyCode,goodsNo,syjNo,status,begD,endD};		
		String sql = _replaceSQL(logWeight,p);
		HashMap mp = new HashMap();		
		//加密
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_getLogWeight：数据加密失败");
			return null;
		}
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (UnsupportedEncodingException e) {			
			return null;
		}
		List<HashMap> data = _getRemoteData(sql);
		if(data==null){
			mp.put("code", "Error");			
		} else{
			mp.put("code", "OK");
			mp.put("data", data);
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;			
	}
	
	private String _getStoresWeight(HttpServletRequest request){
		String date = request.getParameter("date");
		return null;
	}
	
	private String _getDailyBusiness(HttpServletRequest request){
		String date = request.getParameter("date");
		String companyCode = request.getParameter("companyCode");
		String[] p = {companyCode,date};
		HashMap mp = new HashMap();
		String sql = _replaceSQL(dailyBusinessPro,p);
		
		//***加密****//
		sql = _3DesEncode(sql,key);
		if(sql == null){
			BaseEnv.log.error("_getDailyBusiness：数据加密失败");
			return null;
		}
		//转码			
		try {
			sql = URLEncoder.encode(sql,"UTF-8");			
		} catch (UnsupportedEncodingException e) {			
			return null;
		}
		
		List<HashMap> data = _getRemoteData(sql);
		if(data==null){
			mp.put("code", "Error");			
		} else{
			mp.put("code", "OK");
			mp.put("data", data);
		}
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	}
	
	private String _genInvt(HttpServletRequest request){			
		String _d = request.getParameter("date");
		String _c = request.getParameter("companyCode");
		GenInvtTask task = new GenInvtTask();
		task.run(_d,_c);
		
		HashMap mp = new HashMap();	
		mp.put("code", "OK");
		String ret = gson.toJson(mp,HashMap.class);
		return ret;
	}
	
	private String _getGood(HttpServletRequest request){
		String keyword = request.getParameter("keyword");
				
		String[] p = {keyword};
		String sql = _replaceSQL(goodSQL,p);
		ArrayList param = new ArrayList();						
		
		HashMap rMap = _queryData(sql,param);
		String ret = gson.toJson(rMap);
		return ret;
	}
	
	private String _getCom(HttpServletRequest request){
		String keyword = request.getParameter("keyword");	
						
		ArrayList param = new ArrayList();
		
		//********获取当前登录用户********//
		LoginBean usrBean = getLoginBean(request);
		Map rMap = new HashMap();
		if(usrBean == null){			
			rMap.put("msg","未登录");
			rMap.put("code", "Error");
			return gson.toJson(rMap);
			
		}
		//************end***********//
		
		String[] e = {usrBean.getId()};
		String sql2 = _replaceSQL(comsScpe,e);
		Result rs = sqlListMap(sql2,param);		
		String[] p = {keyword};
		String sql = _replaceSQL(comSQL,p);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){			
			ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
			if(list.size()>0){
				String comps = "";
				for(HashMap item : list){
					comps += ("".equals(comps) ? "":"','")+item.get("companyId");
				}
				sql += " and id in('"+comps+"') ";
				
			} 
			
			rMap = _queryData(sql,param);		
			String ret = gson.toJson(rMap);	
			return ret;
		}
		
		rMap.put("code", "Error");
		return gson.toJson(rMap);
	}
	
	private HashMap _queryData(String sql,ArrayList param){
		
		HashMap rMap = new HashMap();
		Result rs = sqlListMap(sql,param);		
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
			rMap.put("code", "OK");
			rMap.put("data",list);
		} else{
			rMap.put("code", "Error");
		}
		return rMap;
	}
	
	private HashMap _getSingleInvt(HttpServletRequest request){
		final String goodCode = request.getParameter("goodCode");
		final String begD = request.getParameter("begD");
		final String endD = request.getParameter("endD");
		final String companyCode = request.getParameter("companyCode");
				
		String _s = "select * from POSBD.dbo.Day_Invt a join POSBD.dbo.Day_InvtDet b on a.id = b.f_ref and b.goodsNum = '"+goodCode+"' where a.companyCode = '"+companyCode+"' and a.date between '"+begD+"' and '"+endD+"'";
		ArrayList _p = new ArrayList();
			
		HashMap ret = _queryData(_s,_p);		
		return ret;
	}
	
	private List<HashMap> _getAllInvt(HttpServletRequest request){	
		final String companyCode = request.getParameter("companyCode");
		final String date = request.getParameter("date");					
		
		//调用接口获取bd服务器相关数据		
		final HashMap<String,List<HashMap>> data = new HashMap();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	data.put("data",_getAllInvtData(conn,companyCode,date));                    	
                    }
                }); 
                return 0;
            }
        });		
		return data.get("data");		
	}		
	
	private List<HashMap> _getAllInvtData(Connection conn,String companyCode,String date){
		Statement stmt = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		
		String sql = "select b.* from POSBD.dbo.Day_Invt a join POSBD.dbo.Day_InvtDet b on a.id = b.f_ref where a.companyCode='"+companyCode+"' and a.date = '"+date+"'";		
		try {
	        	stmt = conn.createStatement();	        	                        
	        	rs = stmt.executeQuery(sql);	        	                                                                             	        	
	
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
			}catch(Exception e){				
				BaseEnv.log.error("_getAllInvtData 获取数据出错 error:",e);
				return null;
			}				
			
			return list;
        /*
        } catch (Exception ex) {                            
        	BaseEnv.log.error("_getRepData error:"+ex.getMessage());
        	return null;
        }
        */
	}		
	
	//获取远程数据方法
	private List<HashMap> _getRemoteData(String sql){		
				
		String  data = new HttpTransfer().postHttp(url+"?op=select","execSQL="+sql);		
		
		if("".equals(data)){			
			return null;
		}		
		//解码
		try {
			data = URLDecoder.decode(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			BaseEnv.log.error("_getRemoteData error:",e);
			return null;
		}		
		//解密
		data = _3DesDecode(data, key);
			
		if(data == null){
			BaseEnv.log.error("_getRemoteData 解密失败。");		
			return null;
		}								
		//转为HashMap
		HashMap mp = gson.fromJson(data, HashMap.class);
		List<HashMap> _l = (List<HashMap>) mp.get("data");					
		return _l;		
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
	
	//数据加密
	private String _3DesEncode(String data,String k){
				
		String _s = null;		
		try {	
			CoderTools coder = new CoderTools();
			coder.KEY_ALGORTHM = "DESede";					
			byte[] _d = coder.get3DESEncodeByte(data.getBytes("UTF-8"), k);					
			_s = new sun.misc.BASE64Encoder().encode(_d);					
		} catch(Exception e){						
			BaseEnv.log.error("_3DesEncode 数据加密异常。");
			BaseEnv.log.error(e.toString());								
			return null;
		}
		return _s;
	}
	
	//数据解密
	private String _3DesDecode(String data,String k){
				
		String _s = null;
		try{			
			CoderTools coder = new CoderTools();
			coder.KEY_ALGORTHM = "DESede";		
			byte[] _d = coder.get3DESDecodeByte(new sun.misc.BASE64Decoder().decodeBuffer(data), k);
			_s = new String(_d,"UTF-8");
		} catch(Exception e){			
			BaseEnv.log.error("_3DesDecode 数据解密失败。");
			BaseEnv.log.error(e.toString());				
		}
		return _s;
	}
	
	protected LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}
}