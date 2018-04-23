package report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.favourstyle.MessageBean;
import com.menyi.aio.web.importData.ExcelFieldInfoBean;
import com.menyi.aio.web.importData.ImportForm;
import com.menyi.aio.web.importData.ImportThread;
import com.menyi.aio.web.importData.JXLTOOL;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.mobile.Message;
import com.menyi.web.util.AIOConnect;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ImpExcelDataAction extends HttpServlet{
	
	private static String JSESSIONID;
	private static String userName="admin";
	private static String password="920F7BA364F910BDEC8AB85B32FF193A";
	
	/**
     * Action执行函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
	 * @throws IOException 
	 * @throws Exception
     * @todo Implement this com.menyi.web.util.BaseAction method
     */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse rsp) throws IOException{        	
              
        /****获取表单数据****/
        DiskFileItemFactory factory = new DiskFileItemFactory();   
        ServletFileUpload upload = new ServletFileUpload(factory);   
        upload.setHeaderEncoding("UTF-8");  
        List items = null;
        HashMap lm = login();
		
        rsp.setHeader("Content-type", "application/json;charset=UTF-8");
        rsp.setCharacterEncoding("UTF-8"); 
        try {
			items = upload.parseRequest(req);
		} catch (Exception e) {
			MessageBean msg = new MessageBean();
			msg.setCode(0);
			msg.setDescription("表单信息解析失败");
			respMsg(req,rsp,new Gson().toJson(msg));
			
		}  
        Map param = new HashMap();   
    	String uploadPath = BaseEnv.FILESERVERPATH+"/temp/";
    	String fileFullName =  "";
    	for(Object obj:items){  
            FileItem fileItem = (FileItem) obj;   
            if (fileItem.isFormField()) {   
                param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的   
            }  else{
           	 	String fileName = fileItem.getName();
           	 	File fullFile= new File(fileItem.getName());            	 
           	 	
           	 	fileFullName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.random() + fullFile.getName();
           	 	//要存入文件夹的路径  
                File savedFile=new File(uploadPath,fileFullName);  
                //存入文件夹  
                try {
					fileItem.write(savedFile);
				} catch(Exception e){					
					System.out.println("上传文件异常："+e.getMessage());
				}        	 
            }
        }  
        String tableName = (String)param.get("tableName");
        /******end*******/
        
        if("SaleReceive".equals(tableName)){
        	HashMap ret = impSaleReceive(uploadPath+fileFullName);
        	rsp.getWriter().print(new Gson().toJson(ret));
        } 
    }
    
	public static HashMap login(){
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(WXSetting.getInstance().getRemoteUrl(WXSetting.AGENTKEYNAME_WXQY)+"AIOApi");
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
		
			/*
			String md5=MD5.GetMD5Code(password);
			out.print("op=LOGIN&name="+userName+"&password="+md5);
		*/
			
			
			//String md5=MD5.GetMD5Code(password);
			out.print("op=LOGIN&name="+userName+"&password="+password);

			
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new DataInputStream(conn.getInputStream());
			byte bs[]=new byte[0];
			byte b[] = new byte[1024];
			int readCount = 0;
			while ((readCount=in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+readCount];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, readCount);
			}
			String ret = new String(bs,"UTF-8");
			System.out.println("执行登陆接口返回："+ret);
			HashMap map = new Gson().fromJson(ret, HashMap.class);
			if(map.get("code").equals("OK")){
				//登陆成功,取登陆标识JSESSIONID
				String responseCookie = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
				JSESSIONID = responseCookie;
				//JSESSIONID = responseCookie.substring(responseCookie.indexOf("JSESSIONID=")+"JSESSIONID=".length());
				//JSESSIONID = JSESSIONID.substring(0,JSESSIONID.indexOf(";"));
				System.out.println(responseCookie);
			}
			return map;
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			HashMap map = new HashMap();
			map.put("code", "ERROR");
			map.put("msg", e.getMessage());
			return map;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected HashMap impSaleReceive(String filePath){
		
		HashMap result = new HashMap();
		//*******解析excel文件获取数据*******//
		List<HashMap<String, ExcelFieldInfoBean>> data = getExcelData(filePath);
		//************end***********//
		
		//*******数据整理********//
		List<HashMap> ret = getSaleReceiveData(data);				
		//*********end********//
		
		//*******数据入库*********//
		int sNum = 0,fNum = 0;
		ArrayList<HashMap> fList = new ArrayList();
		for(HashMap item : ret){
			HashMap msg = addSaleReceive(item);
			HashMap m = new HashMap();
			if("ERROR".equals(msg.get("code"))){
				fNum++;
				m.put("BillDate",item.get("BillDate"));
				m.put("ComFullName", item.get("ComFullName"));
				m.put("Remark",item.get("Remark"));
				fList.add(m);
			} else{				
				sNum++;
			}
		}
		//*********end*********//
		result.put("sNum", sNum);
		result.put("fNum", fNum);
		result.put("fRec", fList);
		return result;
	}
	
	
	private HashMap addSaleReceive(HashMap data){
		String url = WXSetting.getInstance().getRemoteUrl(WXSetting.AGENTKEYNAME_WXQY)+"AIOApi?op=add&tableName=tblSaleReceive";
		//Message msg = AIOConnect.toURL(url, "", data);
		HashMap result = toUrl(url,JSESSIONID,data);		
		
		return result;
	}
	
private HashMap toUrl(String url,String JSESSIONID,HashMap data){
	PrintWriter out = null;		
	DataInputStream in = null;
	try{
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
		// 设置通用的请求属性			
		//conn.setConnectTimeout(5*1000);
		//conn.setReadTimeout(5*1000);
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");			
		
		conn.setRequestProperty("Cookie", JSESSIONID);//传入会话ID，服务器需验证身份
		
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		//conn.setRequestProperty("Content-Length", String.valueOf(paramStr.length()+100));
		
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
					
		// 发送请求参数
		//String u=MD5.GetMD5Code(password);
					
		// 获取URLConnection对象对应的输出流			
		//out = new PrintWriter(conn.getOutputStream());									
		out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
		//paramStr = URLEncoder.encode(paramStr, "utf-8");			
		out.print("values="+new Gson().toJson(data));			
											
		
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new DataInputStream(conn.getInputStream());
		byte bs[]=new byte[0];
		byte b[] = new byte[1024];
		int readCount = 0;
		
		while ((readCount=in.read(b)) != -1) {
			byte[] temp = bs;
			bs = new byte[temp.length+readCount];
			System.arraycopy(temp, 0, bs, 0, temp.length);
			System.arraycopy(b, 0, bs, temp.length, readCount);
		}
		
		String ret = new String(bs,"UTF-8");
		System.out.println("执行接口返回："+ret);
		HashMap map = new Gson().fromJson(ret, HashMap.class);
						
		return map;
		
	} catch (Exception e) {
		System.out.println("发送 POST 请求出现异常！" + e);
		e.printStackTrace();
		HashMap map = new HashMap();
		map.put("code", "ERROR");
		map.put("msg", e.getMessage());
		return map;
	}
	// 使用finally块来关闭输出流、输入流
	finally {
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	}
	
	@SuppressWarnings("unchecked")
	private List<HashMap> getSaleReceiveData(List<HashMap<String, ExcelFieldInfoBean>> data){
		List<HashMap> ret = new ArrayList(); 
		
		ArrayList<String> fields = new ArrayList();		
		fields.add("单据日期");
		fields.add("往来单位全称");
		fields.add("经手人");
		fields.add("部门");
		fields.add("收款类型");
		fields.add("往来结算合计");
		fields.add("科目名称");
		fields.add("金额");
		fields.add("账户备注");
		HashMap<String,HashMap> map = new HashMap();
		for(String item : fields){
			
			HashMap<String,String> mm = new HashMap();
			
			if("往来单位全称".equals(item)){				
				mm.put("name", "ComFullName");
				mm.put("fieldName", "classCode");
				mm.put("tranName", "CompanyCode");
				map.put("往来单位全称",mm);				
			} else if("经手人".equals(item)){
				mm.put("name", "EmpFullName");
				mm.put("fieldName", "id");
				mm.put("tranName", "EmployeeID");
				map.put("经手人",mm);	
			} else if("部门".equals(item)){
				mm.put("name", "DeptFullName");
				mm.put("fieldName", "DepartmentCode");
				mm.put("tranName", "DepartmentCode");
				map.put("部门",mm);
			} else if("收款类型".equals(item)){
				mm.put("name", "AcceptTypeID");
				mm.put("fieldName", "AcceptTypeID_val");
				mm.put("tranName", "AcceptTypeID");
				map.put("收款类型",mm);
			} else if("科目名称".equals(item)){
				mm.put("name", "AccName");
				mm.put("fieldName", "AccNumber");
				mm.put("tranName", "Account");
				//mm.put("tranName", "tblReceiveAccountDet_Account");
				map.put("科目名称",mm);
			} else if("单据日期".equals(item)){
				mm.put("fieldName","BillDate");
				mm.put("tranName", "BillDate");
				map.put("单据日期", mm);
			} else if("往来结算合计".equals(item)){
				mm.put("fieldName", "ExeBalAmt");
				mm.put("tranName", "ExeBalAmt");
				map.put("往来结算合计", mm);
			} else if("金额".equals(item)){
				/*
				mm.put("fieldName", "ExeBalFcAmt");
				mm.put("tranName", "tblReceiveAccountDet_ExeBalFcAmt");
				mm.put("tranName", "ExeBalFcAmt");
				*/
				mm.put("fieldName", "Amount");
				mm.put("tranName", "Amount");
				mm.put("tranName", "Amount");
				map.put("金额", mm);
			} else if("账户备注".equals(item)){
				mm.put("fieldName", "Remark");
				//mm.put("tranName", "tblReceiveAccountDet_Remark");
				mm.put("tranName", "Remark");
				map.put("账户备注", mm);
			}
			
		}
			
		HashMap<String,ArrayList> fs = new HashMap();
		fs.put("往来单位全称",new ArrayList());
		fs.put("经手人", new ArrayList());
		fs.put("部门", new ArrayList());
		fs.put("收款类型", new ArrayList());
		fs.put("科目名称", new ArrayList());
		
		ArrayList<String> refs = new ArrayList();		
			
		refs.add("往来单位全称");
		refs.add("经手人");
		refs.add("部门");
		refs.add("收款类型");
		refs.add("科目名称");
		
		//*****获取关联数据*****//
		for(HashMap item :data){
			HashMap mp = new HashMap();
			Iterator iter = item.entrySet().iterator();
			   while(iter.hasNext()){
				   Map.Entry entry = (Map.Entry)iter.next();
				   Object key = entry.getKey();
				   Object val = entry.getValue();
				   ExcelFieldInfoBean v =(ExcelFieldInfoBean)item.get((String)key);
				   
				  if(refs.contains(v.getName())){
					  ArrayList list = fs.get(v.getName());
					  list.add(v.getValue());
				  }else{
					  continue;
				  }				   
			   }
			
		}
		
		for(String item : fields){
			if("往来单位全称".equals(item)){
				String  names = StringUtils.join(fs.get(item).toArray(),"','");				
				String sql = " select ComFullName,classCode from tblCompany where 1=1  and (tblCompany.ClientFlag=2 or tblCompany.ClientFlag=3) and tblCompany.ComFullName in('"+names+"')";
				Result rs = getData(sql);
				if(rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
					fs.put(item, new ArrayList());
				} else{
					fs.put(item, (ArrayList)rs.getRetVal());
				}
			} else if("经手人".equals(item)){
				String  names = StringUtils.join(fs.get(item).toArray(),"','");				
				String sql = " select tblEmployee.id,DepartmentCode,EmpFullName,DeptFullName from tblEmployee left join tblDepartment on tblEmployee.DepartmentCode=tblDepartment.classCode where 1=1  and ((tblEmployee.SCompanyID='00001' and (tblEmployee.statusId!='-1' or tblEmployee.id='1') and tblEmployee.workFlowNodeName='finish') and tblEmployee.workFlowNodeName='finish' and tblDepartment.workFlowNodeName='finish') and tblEmployee.EmpFullName in('"+names+"')";
				Result rs = getData(sql);
				if(rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
					fs.put(item, new ArrayList());
				} else{
					fs.put(item, (ArrayList)rs.getRetVal());
				}
			} else if("部门".equals(item)){
				String  names = StringUtils.join(fs.get(item).toArray(),"','");				
				String sql = " select tblEmployee.id,DepartmentCode,EmpFullName,DeptFullName from tblEmployee left join tblDepartment on tblEmployee.DepartmentCode=tblDepartment.classCode where 1=1  and ((tblEmployee.SCompanyID='00001' and (tblEmployee.statusId!='-1' or tblEmployee.id='1') and tblEmployee.workFlowNodeName='finish') and tblEmployee.workFlowNodeName='finish' and tblDepartment.workFlowNodeName='finish') and tblDepartment.DeptFullName in('"+names+"')";
				Result rs = getData(sql);
				if(rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
					fs.put(item, new ArrayList());
				} else{
					fs.put(item, (ArrayList)rs.getRetVal());
				}
			} else if("收款类型".equals(item)){
				ArrayList rs = new ArrayList();
				HashMap m = new HashMap();
				m.put("AcceptTypeID", "应收款");
				m.put("AcceptTypeID_val", "Receive");			
				rs.add(m);
				fs.put(item,rs);
			} else if("科目名称".equals(item)){
				String names = StringUtils.join(fs.get(item).toArray(),"','");
				String sql = " select AccNumber,tblLanguage.zh_CN as AccName from tblAccTypeInfo join tblLanguage on tblAccTypeInfo.AccFullName = tblLanguage.id where 1=1 and (tblAccTypeInfo.isCalculate !=1 OR tblAccTypeInfo.isCalculate IS NULL) and tbllanguage.zh_CN in('"+names+"') ";
				Result rs = getData(sql);
				if(rs.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
					fs.put(item, new ArrayList());
				} else{
					fs.put(item, (ArrayList)rs.getRetVal());
				}
			}
						
		}
		
		//*****字表字段*****//
		ArrayList<String> childFields = new ArrayList();		
		childFields.add("科目名称");
		childFields.add("金额");
		childFields.add("账户备注");	
		//*******end*****//
				
		//*******组装数据********//		
		for(HashMap item :data){
			HashMap d = new HashMap();
			HashMap c = new HashMap();
			ArrayList<HashMap> accountDets = new ArrayList();
			for(String str :fields){
				ExcelFieldInfoBean v =(ExcelFieldInfoBean) item.get(str);
				HashMap fieldInf = (HashMap) map.get(str);
				ArrayList<HashMap> arr = fs.get(str);
				String fieldName =(String)fieldInf.get("fieldName");
				String valueName = (String)fieldInf.get("name");
				if(valueName != null){
					for(HashMap m : arr){
						String _v = (String)m.get(valueName);
						if(_v.equals(v.getValue())){
							if(childFields.contains(str)){
								c.put(fieldInf.get("tranName"), m.get(fieldName));
							} else{
								d.put(fieldInf.get("tranName"), m.get(fieldName));
							}
							
							if(str.equals("往来单位全称")){
								d.put("ComFullName", v.getValue());
							}
							/*
							if(str.equals("科目名称")){
								d.put("tblReceiveAccountDet_tblAccTypeInfo_AccName", v.getValue());
							}*/							
							break;
						}
					}	
				} else{
					if(str.equals("单据日期")){
						d.put(fieldInf.get("tranName"), v.getValue().substring(0, 10));
					} else{
						d.put(fieldInf.get("tranName"), v.getValue());
					}
					if(childFields.contains(str)){
						c.put(fieldInf.get("tranName"), v.getValue());
					} 
				}
				
			}
			accountDets.add(c);
			d.put("TABLENAME_tblReceiveAccountDet", accountDets);
			ret.add(d);
		}	
		//*******end*********//
		
		/*
		for(HashMap item :data){
			HashMap map = new HashMap();
			Iterator iter = item.entrySet().iterator();
			   while(iter.hasNext()){
				   Map.Entry entry = (Map.Entry)iter.next();
				   Object key = entry.getKey();
				   Object val = entry.getValue();
				   ExcelFieldInfoBean v =(ExcelFieldInfoBean)item.get((String)key);
				   
				  if(fields.contains(v.getName())){
					  ArrayList list = fs.get(v.getName());
					  list.add(v.getValue());
				  }else{
					  continue;
				  }				   
			   }
			
		}
		*/
		//********end*******//
		
		//HashMap map = dealSaleReceiveData();
		
		return ret;
	}
	
	private Result getData(final String sql){
		final Result rs = new Result();
		final Result rsPop = new Result();
		//在配置文件中查找此单据中弹出框显示语句
				int retCode1 = DBUtil.execute(new IfDB() {
					public int exec(Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection) throws SQLException {
								Connection conn = connection;
								String querysql = "";
								try {								
										Statement cs = conn.createStatement();
										ResultSet rset = cs.executeQuery(sql);

										java.sql.ResultSetMetaData rsm = rset.getMetaData();
										int colCount = rsm.getColumnCount();
										List list = new ArrayList();
										while (rset.next()) {
											HashMap fieldMap = new HashMap();
											
											for (int i = 1; i <= colCount; i++) {
												String columnName = rsm.getColumnName(i);
												int type = rsm.getColumnType(i);
												if (type == Types.NUMERIC) {
													BigDecimal bigDec = rset.getBigDecimal(i);
													if (bigDec != null && bigDec.doubleValue() == 0) {
														fieldMap.put(columnName, bigDec.doubleValue());
													} else {
														fieldMap.put(columnName, bigDec);
													}
												} else{
													fieldMap.put(columnName, GlobalsTool.replaceSpecLitter(rset.getString(i)));
												}
												if (fieldMap.get(columnName) == null) {
													fieldMap.put(columnName, "");
												}
											}
											
											list.add(fieldMap);											
										}
									
									rsPop.setRetVal(list);
								} catch (Exception ex) {
									rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									BaseEnv.log.error("Query data Error :" + sql, ex);
									return;
								}
							}
						});
						return rs.getRetCode();
					}
				});
				rsPop.retCode = retCode1;
				
				return rsPop;
				
	}
	
	private List getExcelData(String filePath){
		List<HashMap<String, ExcelFieldInfoBean>> list = null;
		try{
			JXLTOOL jxlTool = new JXLTOOL(filePath);		 
			 Result exceldatars = jxlTool.getExcelData(true);
			    if (exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			    	//读取数据成功				
					list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
					jxlTool.close();
			    }
		} catch(Exception e){
			System.out.println("excel数据获取异常："+e.getMessage());
		}
		return list;
	}
    
	private void respMsg(HttpServletRequest req, HttpServletResponse rsp, String msgStr) {
		try {
			if (BaseEnv.log.isDebugEnabled()) { //打印调试信息
				String uName = "";
				if (this.getLoginBean(req) != null) {
					uName = this.getLoginBean(req).getEmpFullName();
				}
				//BaseEnv.log.debug("MobileAjax 操作人:" + uName + ";" + "返回数据：" + (msgStr.length() > 500 ? msgStr.substring(0, 500) + "等等！" : msgStr));
			}
			rsp.getOutputStream().write(msgStr.getBytes("UTF-8"));
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.respMsg", e);
		}
	}
	
	private LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}
	
	/**
     * @param src
     * @param dest
     */
    public static boolean upload(File src, File dest) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(dest));
            while (((len = bis.read(buf)) != -1)) {
                bos.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                bos = null;
                bis = null;
            }
        }
        return true;
    }

}
