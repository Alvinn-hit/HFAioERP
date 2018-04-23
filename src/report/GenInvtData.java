package report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.web.util.BaseEnv;

public class GenInvtData {
	private static Gson gson;
	private static String url;
	private static String key;
	private static String proSQL;
	private static String bdSQL;
	private static String bdSQL2;
	private static String bd2SQL;
	private static String bd2RetSQL;
	private static String bd2RetSQL2; //雅座退品数据
	private static String bd2PayDetSQL;//销售单据数据
	
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
		url = "http://bd2.krrj.cn:8081/Servlet/action.htm";
		key = "6D4E60E55552386C759569836DC0F83869836DC0F838C0F7";
		
		//获取bd服务器数据
		proSQL = "Day_Invt";
		
		//获取商品配比数据
		bdSQL = "select a.name,b.qty,b.GoodsCode,a.goodsNo,b.isMain,t.price from posGoods a join posGoodsDet2 b on a.id = b.f_ref join (select a.goodsNo,(case when b.Price != null then b.Price else a.price end) price from posGoods a left join posGoodsPrice b on a.id = b.f_ref and b.CompanyCode = '?') t on t.goodsNo = a.goodsNo order by a.goodsNo asc";
		
		//获取bd2服务器数据（不包含套餐用量）				
		//bd2SQL = "declare @syjNo varchar(20),@date varchar(20) select @date='?',@syjNo = (case when @syjNo IS null then ''  else @syjNo+',' end)+syjNo from companyPos where companyCode = '?' select posGoods.name,a.GoodsCode,sum(a.QTY) as qty,a.Price,sum(isnull(a.discount,0)) as discount,b.GoodsCode as parentCode from (select syjNo,GoodsCode,qty,eMaster_c,f_ref,Price,discount from bdMDSalesDet where f_ref in (select ID from bdMDSales where date = @date and @syjNo like '%'+syjNo+'%') and  eMaster !=  '套餐主项        ') a left join bdMDSalesDet b on a.eMaster_c = b.salesCode and a.f_ref = b.f_ref left join posGoods on a.GoodsCode = posGoods.goodsNo group by a.GoodsCode,b.GoodsCode,a.Price,posgoods.name";
		bd2SQL = "declare @syjNo varchar(20),@date varchar(20) select @date='?',@syjNo = (case when @syjNo IS null then ''  else @syjNo+',' end)+syjNo from companyPos where companyCode = '?' select posGoods.name,a.GoodsCode,sum(a.QTY) as qty,a.Price,sum(isnull(a.discount,0)) as discount,'' as parentCode from (select syjNo,GoodsCode,qty,eMaster_c,f_ref,Price,discount from bdMDSalesDet where f_ref in (select ID from bdMDSales where date = @date and @syjNo like '%'+syjNo+'%') and  eMaster !=  '套餐主项        ') a left join posGoods on a.GoodsCode = posGoods.goodsNo group by a.GoodsCode,a.GoodsCode,a.Price,posgoods.name";
		
		//获取套餐组合明细数据
		bdSQL2 = "select a.goodsNo as parentNo,b.goodsNo,b.price,weight from posGoods a join posGoodsPackage b on a.id = b.f_ref order by a.goodsNo";		
	
		//获取退品数据
		bd2RetSQL = "declare @syjNo varchar(20),@date varchar(20) select @date='?',@syjNo = (case when @syjNo IS null then ''  else @syjNo+',' end)+syjNo from companyPos where companyCode = '?' select posGoods.name,a.GoodsCode,sum(a.QTY) as qty,a.Price,sum(a.Price*a.qty) as retAmt,b.GoodsCode as parentCode from (select syjNo,GoodsCode,qty,eMaster_c,f_ref,Price,discount from bdMDSalesDet where f_ref in (select ID from bdMDSales where date = @date and @syjNo like '%'+syjNo+'%') and  eMaster !=  '套餐主项        ' and amount<0) a left join bdMDSalesDet b on a.eMaster_c = b.salesCode and a.f_ref = b.f_ref left join posGoods on a.GoodsCode = posGoods.goodsNo group by a.GoodsCode,b.GoodsCode,a.Price,posgoods.name";
		
		//获取雅座接口退品数据
		bd2RetSQL2 ="declare @syjNo varchar(20),@date varchar(20) select @date='?',@syjNo = (case when @syjNo IS null then ''  else @syjNo+',' end)+syjNo from companyPos where companyCode = '?' select posGoods.name,a.GoodsCode,sum(a.QTY) as qty,a.Price,sum(a.Price*a.qty-isnull(a.discount,0)) as retAmt,b.GoodsCode as parentCode from (select syjNo,GoodsCode,qty,eMaster_c,f_ref,Price,discount from bdMDSalesRetDet where f_ref in (select ID from bdMDSalesRet where date = @date and @syjNo like '%'+syjNo+'%') and  eMaster !=  '套餐主项        ' and amount<0) a left join bdMDSalesRetDet b on a.eMaster_c = b.salesCode and a.f_ref = b.f_ref left join posGoods on a.GoodsCode = posGoods.goodsNo group by a.GoodsCode,b.GoodsCode,a.Price,posgoods.name";
		
		//获取销售单据支付数据
		bd2PayDetSQL = "declare @syjNo varchar(20),@date varchar(20) select @date='?',@syjNo = (case when @syjNo IS null then ''  else @syjNo+',' end)+syjNo from companyPos where companyCode = '?' select sum(payAmount) payAmt,count(0) as count,payName from bdMDSales a join bdMDSalesPaysDet b on a.id = b.f_ref where a.date = @date and @syjNo like '%'+a.syjNo+'%' group by payName";
	}
	
	public List<HashMap> run(Connection conn,String companyCode,String date){
		Statement stmt = null;
		ResultSet rs = null;
		List<HashMap> list = new ArrayList<HashMap>();
		
		try {
	        	stmt = conn.createStatement();
	        	String sql = "{call "+proSQL+"('"+companyCode+"','"+date+"')}";                        	
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
			//获取商品 原料配比数据				
			String[] _p1 = {companyCode};
			String _s1 = _replaceSQL(bdSQL,_p1);
			HashMap hp = _getRatioData(conn,_s1);			
			
			//获取套餐明细数据
			HashMap packageDet = _getPackageDet(conn,bdSQL2);
			
			//获取bd2服务器数据			
			String[] _p2 = {date,companyCode};			

			String _s2 = _replaceSQL(bd2SQL, _p2);
					
			//sql串加密
			_s2 = _3DesEncode(_s2,key);					
			
			if(_s2 == null){
				return null;
			}			
					
			//**********end********//			
			//转码
			_s2 = URLEncoder.encode(_s2,"UTF-8");			
					
						
			List<HashMap> _d = _getRemoteData(_s2);						
			
			if(_d == null){
				BaseEnv.log.error("bd2数据获取失败。");
				return null;
			}		
						
			//整合数据返回最终表数据
			List<HashMap> data = _getAllData(list,_d,hp,packageDet);
						
			return data;
        } catch (Exception ex) {                            
        	BaseEnv.log.error("GenInvtData.run error:",ex);
        	return null;
        }
       
	}
	
	//获取门店销售支付汇总数据
	public List<HashMap> getPOSPayDet(String companyCode,String date){
		//获取bd2服务器数据			
		String[] _p = {date,companyCode};			

		String _s = _replaceSQL(bd2PayDetSQL, _p);
		//sql串加密
		_s = _3DesEncode(_s,key);					
		
		if(_s == null){
			return null;
		}			
				
		//**********end********//			
		//转码
		try {
			_s = URLEncoder.encode(_s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}	
		List<HashMap> _d = _getRemoteData(_s);	
					
		return _d;
	}
	
	public List<HashMap> getPOSRet(String companyCode,String date){
		
		//获取bd2服务器数据			
		String[] _p = {date,companyCode};			

		String _s = _replaceSQL(bd2RetSQL, _p);
		//sql串加密
		_s = _3DesEncode(_s,key);					
		
		if(_s == null){
			return null;
		}			
				
		//**********end********//			
		//转码
		try {
			_s = URLEncoder.encode(_s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}	
		List<HashMap> _d = _getRemoteData(_s);	
		
		return _d;
	}
	
	//获取成品配比数据
	private HashMap _getRatioData(Connection conn,String s){
		try{					
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(s);			
			List<HashMap> list = new ArrayList<HashMap>();
	
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
			
			//对原料配比用量进行数据结构变换
			HashMap<String,HashMap> m = new HashMap();
			for(HashMap item : list){
				HashMap tmp = null;
				if(!m.containsKey(item.get("goodsNo"))){
					HashMap<String,HashMap> _n = new HashMap();
					m.put((String)item.get("goodsNo"), _n);
					tmp = _n;
				} else{
					tmp = m.get(item.get("goodsNo"));
				}
				HashMap _m = new HashMap();
				_m.put("isMain", item.get("isMain"));
				_m.put("qty", item.get("qty"));
				tmp.put(item.get("GoodsCode"), _m);
				tmp.put("price",item.get("price"));
				m.put((String)item.get("goodsNo"), tmp);			
			}
			return m;
		} catch(Exception e){
			BaseEnv.log.error("_getRatioData error:",e);
			return null;
		}
	}
	
	//获取套餐配比明细
	private HashMap _getPackageDet(Connection conn,String s){
		try{					
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(s);			
			List<HashMap> list = new ArrayList<HashMap>();
	
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
			HashMap<String,HashMap> m = new HashMap();
			for(HashMap item : list){
				HashMap tmp = null;
				if(!m.containsKey(item.get("parentNo"))){
					HashMap<String,HashMap> _n = new HashMap();
					m.put((String)item.get("parentNo"), _n);
					tmp = _n;
				} else{
					tmp = m.get(item.get("parentNo"));
				}
				HashMap _m = new HashMap();				
				_m.put("weight", item.get("weight"));
				tmp.put(item.get("goodsNo"), _m);
				tmp.put("price",item.get("price"));
				m.put((String)item.get("parentNo"), tmp);
			}
			return m;
		}catch(Exception e){
			BaseEnv.log.error("_getPackageDet error:",e);
			return null;
		}					
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
	
	//获取表数据
	private List<HashMap> _getAllData(List<HashMap> d1,List<HashMap> d2,HashMap map,HashMap packageDet){		
		//*****套餐内商品数量转换*****//
		for(HashMap _i : d2){												
			if(_i.get("parentCode") != null && !"".equals(_i.get("parentCode"))){						
				String parentNo = (String)_i.get("parentCode");
				HashMap tmp = (HashMap) packageDet.get(parentNo);
				if(tmp != null){
					HashMap tmp2=(HashMap)tmp.get(_i.get("GoodsCode"));
					//System.out.println("parentCode = "+_i.get("parentCode"));
					//System.out.println("subCode = "+new Gson().toJson(_i.get("GoodsCode")));
					
					double _w = Double.parseDouble(tmp2.get("weight").toString());
					if(_w>0){
						double _total = _w * Double.parseDouble(_i.get("qty").toString());
						_i.put("weight", _total);						
					}					
				}						
			}
		}		
		//************end*********//		
		
		for(HashMap item : d1){
			double _qty = 0.0;
			double _turnover = 0.0;
			int _count = 0;
			double _sum = 0.0;//商品单价总和
			double _ratio = 0.0;//原料成品换算比例
			
			String isMain = null;			
			
			HashMap<String,Object> mQty = new HashMap();
			//try{
				for(HashMap _i : d2){					
					HashMap _t =  (HashMap) map.get(_i.get("GoodsCode"));
					if(_t != null && _t.containsKey(item.get("GdsCode"))){
						HashMap<String,Object> _m = new HashMap();
						HashMap _s = (HashMap) _t.get(item.get("GdsCode"));					
						if(_i.get("weight")!=null){
							//****判断是否免费赠送商品*****//
							if(_i.get("name") != null && ((String)_i.get("name")).indexOf("免费")>=0){
								Double productLossCount = Double.parseDouble((String)item.get("productLossCount"));
								Double giftCount = Double.parseDouble((String)item.get("giftCount"));
								productLossCount += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("weight").toString());
								giftCount += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("weight").toString());
								item.put("productLossCount",productLossCount);
								item.put("giftCount", giftCount);
								Double saleGift = Double.parseDouble((String)item.get("saleGift"));
								//saleGift += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("weight").toString()); 
								if("免费送豆浆".equals(_i.get("name"))){
									saleGift += Double.parseDouble(_i.get("weight").toString())*3;
								}
								item.put("saleGift", saleGift);
							} else{
								_m.put("Price",_i.get("Price"));
								_m.put("qty",Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("weight").toString()));
								_m.put("ratio",Double.parseDouble(_s.get("qty").toString()));
								_qty += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("weight").toString());							
								mQty.put(_i.get("parentCode")+"",_m);								
							}							
						} else{														
							//****判断是否免费赠送商品*****//
							if(_i.get("name") != null && ((String)_i.get("name")).indexOf("免费")>=0){
								Double productLossCount = Double.parseDouble((String)item.get("productLossCount"));
								Double giftCount = Double.parseDouble((String)item.get("giftCount"));
								productLossCount += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("qty").toString());
								giftCount += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("qty").toString());
								item.put("productLossCount",productLossCount);
								item.put("giftCount", giftCount);
								Double saleGift = Double.parseDouble((String)item.get("saleGift"));
								//saleGift += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("qty").toString());
								if("免费送豆浆".equals(_i.get("name"))){
									saleGift += Double.parseDouble(_i.get("qty").toString())*3;
								}
								item.put("saleGift", saleGift);
								
							} else{
								_m.put("Price",_i.get("Price"));
								_m.put("qty",Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("qty").toString()));
								_m.put("ratio",Double.parseDouble(_s.get("qty").toString()));
								_qty += Double.parseDouble(_s.get("qty").toString())*Double.parseDouble(_i.get("qty").toString());
								mQty.put(_i.get("GoodsCode")+"",_m);
								
							}
						}

						_ratio = 1/Double.parseDouble(_s.get("qty").toString());
						if(_s.get("isMain")!=null && "1".equals(_s.get("isMain").toString())){
							isMain = _s.get("isMain").toString();
						} 
						
						if(_i.get("Price") !=null){						
							if(_s.get("isMain")!=null && "1".equals(_s.get("isMain").toString())){
								_turnover += Double.parseDouble(_i.get("qty").toString())* Double.parseDouble(_i.get("Price").toString())-Double.parseDouble(_i.get("discount").toString());
							} 							
						} else{						
							if(_s.get("isMain")!=null && "1".equals(_s.get("isMain").toString())){
								_turnover += Double.parseDouble(_i.get("qty").toString())* Double.parseDouble(_t.get("price").toString())-Double.parseDouble(_i.get("discount").toString());
							}												
						}
						
						_count++;
						_sum += Double.parseDouble(_t.get("price")+"");
						
						item.put("flag", "1");
					}																					
				}
				item.put("posUseCount", _qty);
				//统计差异，计算营业额，理论营业额			
				if("1".equals(isMain)){
					double TheoryInvtCount = 0.0;
					double DiffCount = 0.0;
					double DiffAmount = 0.0;
					double CalTurnover = 0.0;
					double diffTurnover = 0.0;
					double avg = 0.0;
					if(_count != 0){
						avg = _sum/_count;
					}
									
					DecimalFormat df = new DecimalFormat("######0.00");
													
					//POS理论存量
					//TheoryInvtCount += new Double(item.get("yesterdayLevCount").toString())+new Double(item.get("todayInCount").toString())+new Double(item.get("todayTransferCount").toString())-new Double(item.get("retQty").toString())-new Double(item.get("materialLossCount").toString())-new Double(item.get("productLossCount").toString())-_qty;
					TheoryInvtCount += new Double(item.get("yesterdayLevCount").toString())+new Double(item.get("todayInCount").toString())+new Double(item.get("todayTransferCount").toString())-new Double(item.get("materialLossCount").toString())-new Double(item.get("productLossCount").toString())-_qty;
					
					//计算差异数量
					DiffCount = TheoryInvtCount-(new Double(item.get("todayInvtCount").toString()));					
					
					//计算差异金额					
					DiffAmount = new Double(item.get("ProSalesPrice").toString())*DiffCount;
											
					
					//差异
					Iterator i = mQty.entrySet().iterator();
					
					if(_qty > 0){
						while(i.hasNext()) {
							Entry entry = (java.util.Map.Entry)i.next();
							HashMap _m =(HashMap) mQty.get(entry.getKey());
							Double _r = Double.parseDouble(_m.get("ratio")+"");
							/*
							if(_r <= 0 ){
								continue;
							}
							*/
							Double _total = Double.parseDouble(_m.get("qty")+"");
							diffTurnover += Double.parseDouble(_m.get("Price")+"")*(_total/_qty)*DiffCount/_r;							
						}
					}
					//diffTurnover = avg*Math.abs(DiffCount)*_ratio;
					
					//预估营业额
					CalTurnover = _turnover+diffTurnover;	
					
					item.put("diffCount",df.format(DiffCount));
					item.put("diffAmount",df.format(DiffAmount));
					item.put("theoryInvtCount",df.format(TheoryInvtCount));
					item.put("calTurnover",df.format(CalTurnover));
					item.put("relTurnover",df.format(_turnover));			
					item.put("diffTurnover",df.format(diffTurnover));
				}			 
				isMain = null;
			/*
			} catch(Exception e){
				BaseEnv.log.error("_getAllData 数据处理异常：",e);
				continue;
			}
			*/
		}
		
		return d1;
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
			BaseEnv.log.error("_3DesEncode 数据加密异常：",e);						
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
			BaseEnv.log.error("_3DesDecode 数据解密失败：",e);			
		}
		return _s;
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
