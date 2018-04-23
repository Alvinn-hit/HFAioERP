package com.menyi.aio.web.finance.cashier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.importData.ExcelFieldInfoBean;
import com.menyi.aio.web.importData.ImportForm;
import com.menyi.aio.web.importData.JXLTOOL;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class CashierAction extends MgtBaseAction {
		
	CashierMgt mgt = new CashierMgt();
	private static String excelPath;
	private static Integer cashierYear;
	private static Integer cashierMonth;
	
	static{
		excelPath = BaseEnv.FILESERVERPATH+"/temp/";
		//excelPath = "../../fileServer_v7/temp/";		
	}
	
	private void initCashierPeriod(String op){
		if(cashierYear == null || cashierMonth == null || "reload".equals(op)){
			Result ret = mgt.queryCashier();
			if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
				
			} else{
				Map m = (Map)ret.retVal;
				cashierYear = (Integer)m.get("PeriodYear");
				cashierMonth =(Integer)m.get("PeriodMonth");				
			}
		}		
	}
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
						
		initCashierPeriod(null);
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;		
		
		switch (operation) {
		case OperationConst.OP_CASHIER_INIT_PRE:
			//forward = CashierInitPre(mapping, form, request, response);
			//���ó�������
			//forward = addVoucherPre(mapping, form, request, response);
			break;		
		case OperationConst.OP_CASHIER_INIT:
			forward = CashierInit(mapping,form,request,response);
			break;		
		case OperationConst.OP_CASHIER_CASH:
			forward = CashierCash(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_QUERY:
			forward = CashierCashQuery(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_ADD:
			forward = CashierCashAdd(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_QUOTE:
			forward = CashierCashQuote(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_EDIT:
			forward = CashierCashEdit(mapping,form,request,response);
			break;		
		case OperationConst.OP_CASHIER_CASH_UPDATE:
			forward = CashierEdit(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_GENBYSINGLE:
			forward = CashierCashGenBySingle(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_GENBYSUMMARY:
			forward = CashierCashGenBySummary(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASHACCOUNT_PRE:
			forward = CashierCashAccountPre(mapping,form,request,response);
			break;	
		case OperationConst.OP_CASHIER_CASHACCOUNT:
			forward = CashierCashAccount(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_DELETE:
			forward = CashierCashDetDelete(mapping,form,request,response);
			break;	
		case OperationConst.OP_CASHIER_CASH_EXPORT:
			forward = CashierExport(mapping,form,request,response,"cash");
			break;	
		case OperationConst.OP_CASHIER_BANK:
			forward = CashierBank(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BANK_QUERY:
			forward = CashierBankQuery(mapping,form,request,response); 
			break;
		case OperationConst.OP_CASHIER_BANK_ADD:
			forward = CashierBankAdd(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BANK_EDIT:
			forward = CashierBankEdit(mapping,form,request,response);
			break;		
		case OperationConst.OP_CASHIER_BANK_UPDATE:
			forward = CashierEdit(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BANK_QUOTE:
			forward = CashierBankQuote(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BANK_DELETE:
			forward = CashierBankDetDelete(mapping,form,request,response);
			break;	
		case OperationConst.OP_CASHIER_BANK_GENBYSINGLE:
			forward = CashierBankGenBySingle(mapping,form,request,response);
			break;			
		case OperationConst.OP_CASHIER_BANK_EXPORT:
			forward = CashierExport(mapping,form,request,response,"bank");
			break;
		case OperationConst.OP_CASHIER_BANKACCOUNT_PRE:
			forward = CashierBankAccountPre(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BANKACCOUNT:
			forward = CashierBankAccount(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_IMPORT_PRE:
			forward = CashierImportPre(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_IMPORT_DATA:
			forward = CashierImportData(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_AUDIT:
			forward = CashierAuditDet(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_REAUDIT:
			forward = CashierReAuditDet(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_DOWNLOAD:
			forward = CashierDownload(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BALANCE_PRE:
			forward = CashierBalaPre(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_BALANCE:
			forward = CashierBala(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_REBALANCE_PRE:
			forward = CashierRetBalaPre(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_REBALANCE:
			forward = CashierRetBala(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_POST:
			forward = CashierPostDets(mapping,form,request,response);
			break;
		case OperationConst.OP_CASHIER_CASH_REPOST:
			forward = CashierRePostDets(mapping,form,request,response);
			break;
		default:			
			forward = CashierErr(mapping, form, request, response);			
		}
		return forward;
	}
	
	//*****���ɹ���
	protected ActionForward CashierPostDets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8"); 
		String accCode = request.getParameter("accCode");
		String dets = request.getParameter("dets");
		String createBy = getLoginBean(request).getId();
		String employeeID = getLoginBean(request).getId();				
		
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		param.put("accCode",accCode);
		param.put("createBy",createBy);
		param.put("EmployeeID",employeeID);
		param.put("dets",dets);
		Result r = mgt.postCashierDets(param);
		Map map = new HashMap();
		if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
			map.put("code",1);
			map.put("msg",r.retVal);
		}else{
			map.put("code",0);
			map.put("msg",r.retVal);
		}		
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close(); 
		return null;	
	}
	
	//*****���ɷ�����
	protected ActionForward CashierRePostDets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8"); 		
		String dets = request.getParameter("dets");			
		
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();		
		param.put("dets",dets);
		Result r = mgt.repostCashierDets(param);
		Map map = new HashMap();
		if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
			map.put("code",1);
			map.put("msg",r.retVal);
		}else{
			map.put("code",0);
			map.put("msg",r.retVal);
		}		
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close(); 
		return null;		
	}
	
	//*****���½�
	protected ActionForward CashierRetBala(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map map = new HashMap();
		Map m = new HashMap();
		m.put("eid", getLoginBean(request).getId());
		Result r = mgt.CashierRetBala(m);
		if(r.retCode ==ErrorCanst.DEFAULT_SUCCESS){												
			map.put("code",1);
			initCashierPeriod("reload");
			//��ǰ�ڼ��Ƶ���һ�ڼ�
			/*
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cashierYear.toString()+"-"+cashierMonth.toString()+"-1");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH,-1);
			cashierYear = c.get(Calendar.YEAR);
			cashierMonth = c.get(Calendar.MONTH)+1;
			*/
		}else{
			map.put("code", 0);
			map.put("msg",r.retVal);
		}
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close(); 
		return null;	
	}
	
	//*****�½�
	protected ActionForward CashierBala(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map map = new HashMap();
	
		Map m = new HashMap();
		m.put("eid", getLoginBean(request).getId());
		Result r = mgt.CashierBala(m);
		if(r.retCode ==ErrorCanst.DEFAULT_SUCCESS){												
			map.put("code",1);
			initCashierPeriod("reload");
			//��ǰ�ڼ��Ƶ���һ�ڼ�
			/*
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cashierYear.toString()+"-"+cashierMonth.toString()+"-1");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MONTH,1);
			cashierYear = c.get(Calendar.YEAR);
			cashierMonth = c.get(Calendar.MONTH)+1;
			*/
		}else{
			map.put("code", 0);
			map.put("msg",r.retVal);
		}
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close(); 
		return null;
	}
	
	//*****�����½����
	protected ActionForward CashierBalaPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException{
		request.setAttribute("CashierYear", cashierYear);
		request.setAttribute("CashierMonth", cashierMonth);
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cashierYear.toString()+"-"+cashierMonth.toString()+"-1");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,1);
		request.setAttribute("CashierMonthEnd",c.get(Calendar.MONTH)+1);
		request.setAttribute("CashierYearEnd",c.get(Calendar.YEAR));
		return getForward(request, mapping, "CashierBalaPre");		
	}
	
	//*****���ط��½����
	protected ActionForward CashierRetBalaPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException{
		request.setAttribute("CashierYear", cashierYear);
		request.setAttribute("CashierMonth", cashierMonth);
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cashierYear.toString()+"-"+cashierMonth.toString()+"-1");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,-1);
		request.setAttribute("CashierMonthPre",c.get(Calendar.MONTH)+1);
		request.setAttribute("CashierYearPre",c.get(Calendar.YEAR));
		return getForward(request, mapping, "CashierRetBalaPre");		
	}
	
	//*****���d����
	protected ActionForward CashierDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
				
		//��������ļ���  
        String filename = request.getParameter("filename");         
          
        //�����ļ�MIME����  
        response.setContentType(request.getSession().getServletContext().getMimeType(filename));  
        //����Content-Disposition  
        response.setHeader("Content-Disposition", "attachment;filename="+filename);  
        //��ȡĿ���ļ���ͨ��response��Ŀ���ļ�д���ͻ���  
        //��ȡĿ���ļ��ľ���·��  
        //String fullFileName = request.getServletContext().getRealPath("/download/" + filename);  
        String fullFileName = excelPath+filename;
        //��ȡ�ļ�  
        InputStream in = new FileInputStream(fullFileName);  
        OutputStream out = response.getOutputStream();  
          
        //д�ļ�  
        int b;  
        while((b=in.read())!= -1)  
        {  
            out.write(b);  
        }  
          
        in.close();  
        out.close(); 
		
		return null;
	}		
	
	//*****�����ϸ
	protected ActionForward CashierAuditDet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map map = new HashMap();
		String dets = request.getParameter("dets");
		
		dets = dets.replaceAll(";", "','");
		
		String sql = "  select * from tblCashierAccount where id in ('"+dets+"')";
		
		Result r = mgt.query(sql);
		//*****���ǰ����У��*****//
		if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
			boolean flag = true;
			ArrayList<Map> list = (ArrayList<Map>)r.retVal;
			for(Map item:list){
				if("finish".equals(item.get("Posted"))){
					map.put("code",0);
					map.put("msg","������ϸ��"+item.get("No")+" �ѹ���");
					flag = false;
				}
			}
			if(flag){
				Map vm = verifyDets((ArrayList<Map>)r.retVal);
			    
				Result ret = mgt.auditDet((String)vm.get("yesDets"));
				if(ret.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
					map.put("code",1);
				} else{
					map.put("code",0);
				}
				if(vm.get("noDets")!=null && !"".equals(vm.get("noDets"))){
					map.put("noDetsNo", vm.get("noDetsNo"));
					map.put("msg", " ��ϸ�� "+vm.get("noDetsNo")+" У��ʧ�ܣ��������ݡ�");
				}
			}					
		} else{
			map.put("code",1);
			map.put("msg","����У��ʧ��");
		}									
		
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close();  
		return null;
	}
	
	//****�������ϸ
	protected ActionForward CashierReAuditDet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String dets = request.getParameter("dets");
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map map = new HashMap();
		Result ret = mgt.reAuditDet(dets);
		if(ret.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			map.put("code",1);
		} else{
			map.put("code", 0);
		}
		out.write(new Gson().toJson(map));
		out.flush();  
        out.close();  
		return null;
	}
	
	//*****�������ؽ���
	protected ActionForward CashierImportPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		return getForward(request, mapping, "CashierImportPre");
	}
	
	//*****��������
	protected ActionForward CashierImportData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
				
		Map ret = new HashMap();
		List<HashMap<String, ExcelFieldInfoBean>> list = null;
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		//*****��������***//
		 ImportForm importForm = (ImportForm) form;
		 try {
			 boolean noTitle = false;
			 JXLTOOL jxlTool = new JXLTOOL(importForm.getFileName());			 
			 Result exceldatars = jxlTool.getExcelData(noTitle);
			 if(exceldatars.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {				
				if(jxlTool.totalCells==0){						
					request.setAttribute("code",0);
					request.setAttribute("msg", "����ģ����������Ϊ0����ȷ��ģ���������ݣ����븴�����ݵ�һ���µ�excel�ĵ������µ���");
					ret.put("code", 0);
					out.write(new Gson().toJson(ret));
					out.flush();  
			        out.close();  
					return null;
				}			
					        			        		        			   
				list = (List<HashMap<String, ExcelFieldInfoBean>>) exceldatars.getRetVal();
				jxlTool.close();
			}else {
				jxlTool.close();
				// ����������Ϣ,��ȡ����ʧ��					
				ret.put("code", 0);
				out.write(new Gson().toJson(ret));
				out.flush();  
		        out.close();  
				return null;
		}
		} catch (Exception e) {
			ret.put("code", 0);
			out.write(new Gson().toJson(ret));
			out.flush();  
	        out.close(); 
			return null;
		}
		
		//*****��������***//
		 Result r = ImpExcelData(list);
		 if(r.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			 ret.put("code", 1);
			 ret.put("exts", r.getRetVal());	
			 ret.put("count", list.size());
			 ret.put("failCount", ((List)r.getRetVal()).size());
			 ret.put("successCount",list.size()-((List)r.getRetVal()).size());
		 }else{
			 ret.put("code", 0);
		 }
		//******end
		out.write(new Gson().toJson(ret));
		out.flush();  
        out.close();  
		return null;
	}
	
	//*****��������
	protected Result ImpExcelData(List<HashMap<String, ExcelFieldInfoBean>> data){
		Result ret = new Result();
		ExcelFieldInfoBean cell = null;
		List<Map> exts = new ArrayList();
		try{
			for(HashMap<String, ExcelFieldInfoBean> item : data){
				Map param = new HashMap();						
				
				//***���ò���ֵ
				cell = item.get("��������:��Ŀ");			
				
				param.put("accCode", cell.getValue());				
				param.put("currency", "RMB");
				cell = item.get("��������:����");
				param.put("BillDate", cell.getValue().substring(0, 10));
				
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cell.getValue());
				Calendar c = Calendar.getInstance();
		        c.setTime(date);		     
		        int year = c.get(Calendar.YEAR);						
				param.put("period", c.get(Calendar.MONTH)+1);
				param.put("No", 0);
				param.put("accYear", c.get(Calendar.YEAR));
				param.put("accMonth", c.get(Calendar.MONTH)+1);
				param.put("accWord", "��");
				param.put("orderNo", 0);
				param.put("remark", "");
				
				//��ȡ��ǰ������
				param.put("PeriodYear", c.get(Calendar.YEAR));
				param.put("PeriodMonth", c.get(Calendar.MONTH)+1);
				Result res = mgt.queryCurNo(param);
				if(res.retCode ==ErrorCanst.DEFAULT_SUCCESS){
					Map d = (Map)res.retVal;
					param.put("No", d.get("CurNo"));
				}
				
				cell = item.get("��������:������");
				
				//*****��ȡԱ����Ϣ
				String sql1 = "select id,sysName from tblEmployee";
				Result r1 = mgt.query(sql1);
				if(r1.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
					ret.retCode = ErrorCanst.DEFAULT_FAILURE;
					ret.retVal = "�������ݻ�ȡʧ��";
				}
				List<Map> d1 = (List<Map>)r1.getRetVal();
				param.put("handler", "");
				param.put("handlerName",item.get("��������:������").getValue());
				param.put("createBy", "");
				param.put("createByName",item.get("��������:�Ƶ���").getValue());
				for(Map i1 : d1){
					if(!"".equals(item.get("��������:�Ƶ���").getValue()) && i1.get("sysName").equals(item.get("��������:�Ƶ���").getValue())){
						param.put("createBy",i1.get("id"));						
					}
					if(!"".equals(item.get("��������:������").getValue()) && i1.get("sysName").equals(item.get("��������:������").getValue())){
						param.put("handler",i1.get("id"));
					}
				}			
				cell = item.get("��������:�Է���Ŀ");
				if(!"".equals(cell.getValue())){					
					param.put("refCode", cell.getValue().split("-")[0]);
				}else{
					param.put("refCode", "");
				}
				
				cell = item.get("��������:�跽���");
				if(!"".equals(cell.getValue())&&Double.valueOf(cell.getValue())!=0){
					param.put("debitAmt",Double.parseDouble(cell.getValue()));
					param.put("lendAmt", 0.0);
				} else{
					cell= item.get("��������:�������");
					param.put("debitAmt",0.0);
					param.put("lendAmt", Double.parseDouble(cell.getValue()));
				}		
				cell = item.get("��������:ժҪ");
				param.put("remark", cell.getValue());			
				param.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				param.put("id", IDGenerater.getId());			
							
				//***�ж������Ƿ��쳣
				//if("".equals(param.get("accCode")) || "".equals(param.get("refCode")) || "".equals(param.get("BillDate"))){
				if("".equals(param.get("accCode")) || "".equals(param.get("BillDate"))){	
					exts.add(param);
					continue;
				}
				
				Result r = mgt.addCashDet(param);			
			}
			ret.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			ret.setRetVal(exts);
			return ret;
		}catch(Exception e){
			ret.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ret.setRetVal("���ݵ���ʧ�ܡ�");
			return ret;
		}
	}
	
	//*****ɾ�������ռ���	
	protected ActionForward CashierBankDetDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		return CashierCashDetDelete(mapping,form,request,response);		
	}
	
	//*****ɾ���ֽ��ռ���	
	protected ActionForward CashierCashDetDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		
		Map r = new HashMap();
		String[] ids = request.getParameter("ids").split(";");
		for(String item:ids){
			param.put("id",item);
			Result r1 = mgt.queryCashDetByID(param);		
			if(r1.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
				r.put("code",0);
				r.put("msg","�ռ������ݻ�ȡʧ��");
				break;
			} else{
				Map _r = (Map)r1.getRetVal();
				String detOrder =  _r.get("BillDate")+" "+_r.get("CredType")+"-"+_r.get("CredNo");
				if("finish".equals(_r.get("workFlowNodeName"))){
					r.put("code",0);
					r.put("msg",detOrder+"���������!");
					break;
				}else{
					Result r2 = mgt.delCashierDet(param);				
					if(r2.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
						r.put("code", 0);
						r.put("msg",detOrder+"����ɾ��ʧ��");
						break;
					} else{
						r.put("code", 1);		
					}	
				}			
			}
		
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//*****�޸��ռ���	
	protected ActionForward CashierEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode", request.getParameter("accCode"));
		param.put("currency", request.getParameter("currency"));
		param.put("BillDate", request.getParameter("BillDate"));
		param.put("period", request.getParameter("period"));
		param.put("No", Integer.parseInt(request.getParameter("No")));
		param.put("accYear", Integer.parseInt(request.getParameter("accYear")));
		param.put("accMonth",Integer.parseInt(request.getParameter("accMonth")));
		param.put("accWord", request.getParameter("accWord"));
		param.put("orderNo", Integer.parseInt(request.getParameter("orderNo")));
		param.put("remark", request.getParameter("remark"));
		param.put("handler", request.getParameter("handler"));
		param.put("refCode", request.getParameter("refCode"));
		param.put("handler", request.getParameter("handler"));
		if(!"".equals(request.getParameter("debitAmt"))){
			param.put("debitAmt",Double.parseDouble(request.getParameter("debitAmt")));
			param.put("lendAmt", 0.0);
		} else{
			param.put("debitAmt",0.0);
			param.put("lendAmt", Double.parseDouble(request.getParameter("lendAmt")));
		}		
		
		param.put("comment", request.getParameter("comment"));
		param.put("createBy", this.getLoginBean(request).getId());
		param.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		param.put("id", request.getParameter("id"));
		
		Map r = new HashMap();
		Result ret = mgt.upCashDet(param);
		
		if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);			
		} else{
			r.put("code", 1);		
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//*****���ռ�������ƾ֤
	protected ActionForward CashierCashGenBySummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		
		Map param = new HashMap();
		param.put("op", "GenBySummary");
		//��ȡ����
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));
		param.put("showDisable" , Integer.parseInt(request.getParameter("showDisable")));		
		param.put("qPeriod" , request.getParameter("qPeriod"));
		if("period".equals(param.get("qPeriod"))){
			param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
			param.put("qBeginMonth" ,Integer.parseInt(request.getParameter("qBeginMonth")));
			param.put("qEndYear" , Integer.parseInt(request.getParameter("qEndYear")));
			param.put("qEndMonth" ,Integer.parseInt(request.getParameter("qEndMonth")));
		} else{
			param.put("qBeginYear" , -1);
			param.put("qBeginMonth" ,-1);
			param.put("qEndYear" , -1);
			param.put("qEndMonth" ,-1);
		}
		
		param.put("qBegD" , request.getParameter("qBegD"));
		param.put("qEndD" , request.getParameter("qEndD"));
		param.put("initBala" , Integer.parseInt(request.getParameter("initBala")));
		param.put("showDet" , Integer.parseInt(request.getParameter("showDet")));
		param.put("todayAccount" , Integer.parseInt(request.getParameter("todayAccount")));
		param.put("curPeriodAccount" , Integer.parseInt(request.getParameter("curPeriodAccount")));
		param.put("accuTotal" , Integer.parseInt(request.getParameter("accuTotal")));
		param.put("total" ,  Integer.parseInt(request.getParameter("accuTotal")));				
		param.put("accWord",request.getParameter("accWord"));
				
		Map r = new HashMap();
		Map result = new HashMap();
		
		Result ret = mgt.genVoucherSummary(param);
		
		if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);
		}else{
			r.put("code", 1);								
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;	
		
	}
	
	//*****�������ռ�������ƾ֤	
	protected ActionForward CashierBankGenBySingle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		return CashierCashGenBySingle(mapping,form,request,response);
	}
	//*****���ռ�������ƾ֤
	protected ActionForward CashierCashGenBySingle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		
		Map param = new HashMap();
		param.put("op", "GenBySingle");
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));
		param.put("showDisable" , Integer.parseInt(request.getParameter("showDisable")));		
		param.put("qPeriod" , request.getParameter("qPeriod"));
		if("period".equals(param.get("qPeriod"))){
			param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
			param.put("qBeginMonth" ,Integer.parseInt(request.getParameter("qBeginMonth")));
			param.put("qEndYear" , Integer.parseInt(request.getParameter("qEndYear")));
			param.put("qEndMonth" ,Integer.parseInt(request.getParameter("qEndMonth")));
		}
		
		param.put("qBegD" , request.getParameter("qBegD"));
		param.put("qEndD" , request.getParameter("qEndD"));
		param.put("initBala" , Integer.parseInt(request.getParameter("initBala")));
		param.put("showDet" , Integer.parseInt(request.getParameter("showDet")));
		param.put("todayAccount" , Integer.parseInt(request.getParameter("todayAccount")));
		param.put("curPeriodAccount" , Integer.parseInt(request.getParameter("curPeriodAccount")));
		param.put("accuTotal" , Integer.parseInt(request.getParameter("accuTotal")));
		param.put("total" ,  Integer.parseInt(request.getParameter("accuTotal")));				
		param.put("accWord",request.getParameter("accWord"));
		
		Map r = new HashMap();
		Map result = new HashMap();
		Result ret = mgt.queryCashData(param);
		
		if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);
		}else{
			r.put("code", 1);
			List<Map> list = (List<Map>)ret.retVal;
			for(Map item : list){
				Result _r1 = mgt.getMaxOrderNo(item);
				if(_r1.retCode == ErrorCanst.DEFAULT_FAILURE){
					//****�ж�����ģʽ
					
					
				} else{
					result = (Map)_r1.getRetVal();
					item.put("vid", IDGenerater.getId());
					item.put("OrderNo", result.get("OrderNo"));
					item.put("accCode", request.getParameter("accCode"));				
					Result _r2 = mgt.genVoucher(item);
				}				
				
			}
			r.put("code", 1);
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;		
	}
	
	//*****��ѯ��ϸ��Ϣ	
	protected ActionForward CashierBankEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		return 	CashierCashEdit(mapping,form,request,response);
	}
	
	//*****��ѯ��ϸ��Ϣ
	protected ActionForward CashierCashEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		
		Map param = new HashMap();
		//��ȡ����
		param.put("id" ,request.getParameter("id"));			
		
		Map r = new HashMap();
		Result ret = mgt.queryCashDetByID(param);
		
		if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);			
		} else{			
			Map _r = (Map)ret.getRetVal();
			if("finish".equals(_r.get("workFlowNodeName"))){
				r.put("code",0);
				r.put("msg", "���������");
			}else{
				r.put("code", 1);
				r.put("data",ret.getRetVal());
			}			
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;		
	}
	
	//*****��ƾ֤�����������ռ���
	protected ActionForward CashierBankQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		
		String[] accCodes = request.getParameter("accCodes").split(";");
				
		param.put("periodYear",Integer.parseInt(request.getParameter("periodYear")));
		param.put("periodMonth", Integer.parseInt(request.getParameter("periodMonth")));
		param.put("way", request.getParameter("way"));
		param.put("date", request.getParameter("date"));
		param.put("model", request.getParameter("model"));
		param.put("word", request.getParameter("word"));
		param.put("creator", request.getParameter("creator"));
		param.put("begOrderNo", Integer.parseInt(request.getParameter("begOrderNo")));
		param.put("endOrderNo", Integer.parseInt(request.getParameter("endOrderNo")));
		param.put("status", request.getParameter("status"));
		param.put("post", request.getParameter("post"));
		
		StringBuffer extAcc = new StringBuffer();
		for(int i = 0;i<accCodes.length;i++){
			param.put("accCode", accCodes[i]);
			/***����ƾ֤***/
			Result ret = mgt.quoteAccMain(param);
			if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
				extAcc.append(accCodes[i]);
			}
		}
		
		Map r = new HashMap();				
		r.put("code", 1);
		if(!"".equals(extAcc.toString())){
			r.put("msg", extAcc.toString()+" ����ʧ��");
		}			
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//*****��ƾ֤�����ֽ��ռ���
	protected ActionForward CashierCashQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		
		String[] accCodes = request.getParameter("accCodes").split(";");
				
		param.put("periodYear",Integer.parseInt(request.getParameter("periodYear")));
		param.put("periodMonth", Integer.parseInt(request.getParameter("periodMonth")));
		param.put("way", request.getParameter("way"));
		param.put("date", request.getParameter("date"));
		param.put("model", request.getParameter("model"));
		param.put("word", request.getParameter("word"));
		param.put("creator", request.getParameter("creator"));
		param.put("begOrderNo", Integer.parseInt(request.getParameter("begOrderNo")));
		param.put("endOrderNo", Integer.parseInt(request.getParameter("endOrderNo")));
		param.put("status", request.getParameter("status"));
		param.put("post", request.getParameter("post"));
		
		StringBuffer extAcc = new StringBuffer();
		for(int i = 0;i<accCodes.length;i++){
			param.put("accCode", accCodes[i]);
			/***����ƾ֤***/
			Result ret = mgt.quoteAccMain(param);
			if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
				extAcc.append(accCodes[i]);
			}
		}
		
		Map r = new HashMap();				
		r.put("code", 1);
		if(!"".equals(extAcc.toString())){
			r.put("msg", extAcc.toString()+" ����ʧ��");
		}			
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//****������д��	
	protected ActionForward CashierBankAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode", request.getParameter("accCode"));
		param.put("currency", request.getParameter("currency"));
		param.put("BillDate", request.getParameter("BillDate"));
		param.put("period", request.getParameter("period"));
		param.put("No", Integer.parseInt(request.getParameter("No")));
		param.put("accYear", Integer.parseInt(request.getParameter("accYear")));
		param.put("accMonth",Integer.parseInt(request.getParameter("accMonth")));
		param.put("accWord", request.getParameter("accWord"));
		param.put("orderNo", Integer.parseInt(request.getParameter("orderNo")));
		param.put("remark", request.getParameter("remark"));
		param.put("handler", request.getParameter("handler"));
		param.put("refCode", request.getParameter("refCode"));
		param.put("handler", request.getParameter("handler"));
		if(!"".equals(request.getParameter("debitAmt"))){
			param.put("debitAmt",Double.parseDouble(request.getParameter("debitAmt")));
			param.put("lendAmt", 0.0);
		} else{
			param.put("debitAmt",0.0);
			param.put("lendAmt", Double.parseDouble(request.getParameter("lendAmt")));
		}		
		
		param.put("comment", request.getParameter("comment"));
		param.put("createBy", this.getLoginBean(request).getId());
		param.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		param.put("id", IDGenerater.getId());			
		
		Map r = new HashMap();
		//****��ȡ��ǰ�¿���������
		Result res = mgt.queryCurNo(param);
		if(res.getRetCode() ==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);
		}else{
			Map resMap = (Map)res.retVal;
			param.put("No", resMap.get("CurNo"));
			Result ret = mgt.addBankDet(param);		
			if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
				r.put("code", 0);			
			} else{
				r.put("code", 1);
			}
		}
		
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;		
	}
	
	//****����ֽ��ռ���
	protected ActionForward CashierCashAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode", request.getParameter("accCode"));
		param.put("currency", request.getParameter("currency"));
		param.put("BillDate", request.getParameter("BillDate"));
		param.put("period", request.getParameter("period"));
		param.put("No", Integer.parseInt(request.getParameter("No")));
		param.put("accYear", Integer.parseInt(request.getParameter("accYear")));
		param.put("accMonth",Integer.parseInt(request.getParameter("accMonth")));
		param.put("accWord", request.getParameter("accWord"));
		param.put("orderNo", Integer.parseInt(request.getParameter("orderNo")));
		param.put("remark", request.getParameter("remark"));
		param.put("handler", request.getParameter("handler"));
		param.put("refCode", request.getParameter("refCode"));
		param.put("handler", request.getParameter("handler"));
		if(!"".equals(request.getParameter("debitAmt"))){
			param.put("debitAmt",Double.parseDouble(request.getParameter("debitAmt")));
			param.put("lendAmt", 0.0);
		} else{
			param.put("debitAmt",0.0);
			param.put("lendAmt", Double.parseDouble(request.getParameter("lendAmt")));
		}		
		
		param.put("comment", request.getParameter("comment"));
		param.put("createBy", this.getLoginBean(request).getId());
		param.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		param.put("id", IDGenerater.getId());		
		
		Map r = new HashMap();
		//****��ȡ��ǰ�¿���������
		Result res = mgt.queryCurNo(param);
		if(res.getRetCode() ==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);
		}else{
			Map resMap = (Map)res.retVal;
			param.put("No", resMap.get("CurNo"));
			Result ret = mgt.addCashDet(param);			
			if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
				r.put("code", 0);			
			} else{
				r.put("code", 1);
			}
		}
		
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
		
	}
	
	/**
	 * ��ȡ�����ֽ���ϸ����	
	*/
	private Map CashierCashData(Map param){
		
		Map r = new HashMap();
		//****��ȡ�ڳ����****//		
		Result r1 = mgt.queryIni(param);
		if(r1.getRetCode() == ErrorCanst.DEFAULT_FAILURE){
			r.put("code",0);
		} else{			
			Result ret = mgt.queryCashDet(param);			
			if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
				r.put("code", 0);			
			} else{
				/*
				if((Integer)param.get("initBala") == 1){					
					Result retBala = mgt.queryAccBala(param);
				}*/							
				
				BigDecimal preAmt = new BigDecimal(0.0);
				List<Map> data = (List)ret.getRetVal();
				Map iniMap = (Map)r1.getRetVal();
				Map row = new HashMap();
				String curYear = "";
				Integer curMonth = 0;
				
				if("period".equals(param.get("qPeriod"))){
					row.put("BillDate", param.get("qBeginYear")+"-"+param.get("qBeginMonth"));
					curYear = param.get("qBeginYear").toString();
					curMonth = Integer.valueOf(param.get("qBeginMonth").toString());
														
				}else{
					row.put("BillDate",param.get("qBegD"));
					curYear = param.get("qBegD").toString().substring(0, 4);
					curMonth = Integer.valueOf(param.get("qBegD").toString().split("-")[1]);					
				}
				
				//****��ȡ��ʼ�ڼ�ǰ�����ۼ�
				BigDecimal curYearDebitTotal = null;
				BigDecimal curYearLendTotal = null;
				Map _p = new HashMap();
				_p.put("periodY", Integer.parseInt(curYear));
				_p.put("periodM", curMonth);
				_p.put("accCode", param.get("accCode"));
				Result pre = mgt.queryPreAccum(_p);
				if(pre.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
					r.put("code", 0);
					r.put("msg", "��ȡ��ʷ��Ϣʧ�ܣ�");
					return r;
				}else{
					Map _d = (Map)pre.getRetVal();
					curYearDebitTotal = new BigDecimal(_d.get("preDebitAmt").toString());
					curYearLendTotal = new BigDecimal(_d.get("preLendAmt").toString());
				}		
																					
				row.put("RecordComment", "�ڳ�");	
				row.put("Amount", iniMap.get("CashEndBala"));																		
				row.put("others", 1);
				
				BigDecimal amt = (BigDecimal)iniMap.get("CashEndBala");
				
				DecimalFormat fnum = new DecimalFormat("##0.00");  
				BigDecimal  total = new BigDecimal(0.0);
				BigDecimal accuTotal = new BigDecimal(0.0);
				BigDecimal debitTotal = new BigDecimal(0.0);
				BigDecimal lendTotal = new BigDecimal(0.0);
				BigDecimal curdebitTotal = new BigDecimal(0.0);
				BigDecimal curlendTotal = new BigDecimal(0.0);
				//BigDecimal curYearDebitTotal = new BigDecimal(0.0);
				//BigDecimal curYearLendTotal = new BigDecimal(0.0);
				List<Map> arr = new ArrayList();
				
				for(Map item : data){					
							
					if((Integer)param.get("curPeriodAccount")==1){
						if(Integer.valueOf(item.get("BillDate").toString().split("-")[1]) != curMonth){
							//*****��ȡ�����ۼ�
							//param.put("curYear", Integer.valueOf(curYear));						
							Map accMap = new HashMap(row);							
							accMap.put("RecordComment","���ںϼ�");
							accMap.put("BillDate", "");
							accMap.put("Amount", fnum.format(total));							
							accMap.put("DebitAmount",fnum.format(curdebitTotal));
							accMap.put("LendAmount", fnum.format(curlendTotal));														
							arr.add(accMap);
							curdebitTotal = new BigDecimal(0.0);
							curlendTotal = new BigDecimal(0.0);							
						}
					}
					
					if((Integer)param.get("accuTotal")==1){	
						if(Integer.valueOf(item.get("BillDate").toString().split("-")[1]) != curMonth){
							Map accMap1 = new HashMap(row);							
							accMap1.put("RecordComment","�����ۼ�");
							accMap1.put("BillDate", "");
							accMap1.put("Amount", fnum.format(total));
							accMap1.put("DebitAmount",fnum.format(curYearDebitTotal));
							accMap1.put("LendAmount", fnum.format(curYearLendTotal));
							arr.add(accMap1);
						}
											
						if( !"".equals(curYear) && !curYear.equals(((String)item.get("BillDate")).substring(0, 4))){
							//*****��ȡ�����ۼ�
							param.put("curYear", Integer.valueOf(curYear));
							
							curYearDebitTotal = new BigDecimal(0.0);
							curYearLendTotal = new BigDecimal(0.0);							
						}																
					}
					debitTotal = debitTotal.add(new BigDecimal(item.get("DebitAmount").toString()));
					lendTotal = lendTotal.add(new BigDecimal(item.get("LendAmount").toString()));
					
					
					curYearDebitTotal = curYearDebitTotal.add(new BigDecimal(item.get("DebitAmount").toString()));
					curYearLendTotal = curYearLendTotal.add(new BigDecimal(item.get("LendAmount").toString()));
					//curYearDebitTotal = debitTotal;
					//curYearLendTotal = lendTotal;
					
					curdebitTotal =curdebitTotal.add(new BigDecimal(item.get("DebitAmount").toString()));
					curlendTotal = curlendTotal.add(new BigDecimal(item.get("LendAmount").toString()));
					
					amt = amt.add(new BigDecimal(item.get("DebitAmount").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal(item.get("LendAmount").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP);										
					
					total = amt;
					item.put("Amount", fnum.format(amt));
										
					curYear = ((String)item.get("BillDate")).substring(0, 4);
					curMonth = Integer.valueOf(item.get("BillDate").toString().split("-")[1]);
					if(new BigDecimal(item.get("DebitAmount").toString()).compareTo(BigDecimal.ZERO) ==0){
						item.put("DebitAmount", 0);
					}else{						
						item.put("DebitAmount", (fnum.format(item.get("DebitAmount"))));
					}
					if(new BigDecimal(item.get("LendAmount").toString()).compareTo(BigDecimal.ZERO)==0){
						item.put("LendAmount", 0);
					}else{						
						item.put("LendAmount", (fnum.format(item.get("LendAmount"))));
					}
					
					arr.add(item);
				}
								
				if((Integer)param.get("initBala")==1){
					arr.add(0, row);
				}							
				if((Integer)param.get("curPeriodAccount")==1){					
					//*****��ȡ�����ۼ�
					//param.put("curYear", Integer.valueOf(curYear));				
					Map accMap = new HashMap(row);
					accMap.put("RecordComment","���ںϼ�");
					accMap.put("BillDate", "");
					accMap.put("Amount", fnum.format(total));							
					accMap.put("DebitAmount",fnum.format(curdebitTotal));
					accMap.put("LendAmount", fnum.format(curlendTotal));														
					arr.add(accMap);		
				}				
				
				if((Integer)param.get("accuTotal")==1){
					//*****��ȡ�����ۼ�
					param.put("curYear", Integer.valueOf(curYear));
					//Result _r = mgt.queryCurYearAmt(param);
					Map accMap = new HashMap(row);
					accMap.put("RecordComment","�����ۼ�");
					accMap.put("BillDate", "");
					accMap.put("Amount", fnum.format(total));
					accMap.put("DebitAmount",curYearDebitTotal);
					accMap.put("LendAmount", curYearLendTotal);
					arr.add(accMap);
					/*
					if(_r.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						Map _m = (Map)_r.getRetVal();						
						if(_m.get("DebitAmt")==null){
							accMap.put("DebitAmount",0);
						}else{
							accMap.put("DebitAmount",fnum.format(_m.get("DebitAmt")));
						}
						if(_m.get("LendAmt")==null){
							accMap.put("LendAmount", 0);
						}else{
							accMap.put("LendAmount", fnum.format(_m.get("LendAmt")));
						}
												
					}		
					arr.add(accMap);
					*/
				}
				
				if((Integer)param.get("total")==1){
					Map tMap = new HashMap(row);
					tMap.put("RecordComment","�ܼ�");
					tMap.put("DebitAmount", fnum.format(debitTotal));
					tMap.put("LendAmount", fnum.format(lendTotal));
					tMap.put("Amount", fnum.format(total));
					tMap.put("BillDate", "");
					arr.add(tMap);
				}
								
				r.put("code", 1);
				r.put("data",arr);
			}
		}
		return r;
	}
	
	
	/**
	 * �����ռ��˵���
	 */	
	protected ActionForward CashierBankExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		
		return null;
	}
	
	/**
	 * �����ֽ��ռ��˵���
	 */
	protected ActionForward CashierExport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,String type) throws IOException{
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));
		param.put("showDisable" , Integer.parseInt(request.getParameter("showDisable")));		
		param.put("qPeriod" , request.getParameter("qPeriod"));
		if("period".equals(param.get("qPeriod"))){
			param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
			param.put("qBeginMonth" , Integer.parseInt(request.getParameter("qBeginMonth")));
			param.put("qEndYear" , Integer.parseInt(request.getParameter("qEndYear")));
			param.put("qEndMonth" , Integer.parseInt(request.getParameter("qEndMonth")));
		}		
		param.put("qBegD" , request.getParameter("qBegD"));
		param.put("qEndD" , request.getParameter("qEndD"));
		param.put("initBala" , Integer.parseInt(request.getParameter("initBala")));
		param.put("showDet" , Integer.parseInt(request.getParameter("showDet")));
		param.put("todayAccount" , Integer.parseInt(request.getParameter("todayAccount")));
		param.put("curPeriodAccount" , Integer.parseInt(request.getParameter("curPeriodAccount")));
		param.put("accuTotal" , Integer.parseInt(request.getParameter("accuTotal")));
		param.put("total" ,  Integer.parseInt(request.getParameter("total")));				
		Map ret = null;
		if("cash".equals(type)){
			ret = CashierCashData(param);
		} else{
			ret = CashierBankData(param);
		}
		
		if((Integer)ret.get("code")==0){
			ret.put("code", 0);			
			ret.put("msg", "������ȡʧ��");			
		}else{			
			String fileName = "Cash_"+IDGenerater.getId()+".xls";
			String filePath = excelPath + fileName;			
			String fields = "row,workFlowNodeName,BillDate,No,CredTypeID,Period,Audited,Posted,RecordComment,RefAcc,DebitAmount,LendAmount,Amount,handler,creator";
			List<Map> list = (List<Map>)ret.get("data");
			
			Map head = new HashMap();
			head.put("row", "No");
			head.put("workFlowNodeName", "���");
			head.put("BillDate","����");
			head.put("No", "��ǰ���");
			head.put("CredTypeID", "ƾ֤�ֺ�");
			head.put("Period", "ƾ֤����");
			head.put("DebitAmount", "�跽���");
			head.put("LendAmount", "�������");
			head.put("EmployeeID", "������");
			head.put("RecordComment", "ժҪ");
			head.put("Audited", "���");
			head.put("Posted", "�Է���Ŀ");
			head.put("AccCode", "��Ŀ");
			head.put("Amount", "���");
			head.put("handler", "������");
			head.put("creator", "�Ƶ���");			
			list.add(0,head);
			Map _r = CashierExcel(list,fields,filePath);
			if((Integer)_r.get("code")==0){
				ret.put("code", 0);
				if(_r.get("msg")!=null){
					ret.put("msg", _r.get("msg"));
				}
			}else{
				ret.put("data",fileName);
			}
		}
		
		out.write(new Gson().toJson(ret));
        out.flush();  
        out.close();  
		return null;
	}
	
	/**
	 * ����Excel
	 */
	private Map CashierExcel(List<Map>data,String Fields,String FilePath){
		Map r = new HashMap();
		OutputStream os = null;
		WritableWorkbook wwb = null;
		try{			
			String[] FieldsList = Fields.split(",");
			os = new FileOutputStream(new File(FilePath));			
			wwb = Workbook.createWorkbook(os);
			//����Excel������ ָ�������ƺ�λ��
			WritableSheet ws = wwb.createSheet("Data", 0);			
			//***����Excel����***//
            for(int i = 0;i<data.size();i++){
            	for(int j=0;j<FieldsList.length;j++){
            		Label label = null;
            		if(data.get(i).get(FieldsList[j])!=null){
            			label = new Label(j,i,data.get(i).get(FieldsList[j])+"");
            		} else{
            			continue;
            		}            		 
            		ws.addCell(label);
            	}            		
            }
            wwb.write();
            wwb.close();
            os.close();
            r.put("code", 1);
		} catch(Exception e){
			BaseEnv.log.error("CashierExcel��excel����ʧ�ܣ�",e);
			r.put("code",0);
		} finally{
			try{
				 wwb.close();
		         os.close();
			} catch(Exception e){
				
			}
		}		
		return r;
		
		
	}
	
	/**
	 * �����ֽ��ռ��˲�ѯ
	 */
	protected ActionForward CashierCashQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{		
       
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));
		param.put("showDisable" , Integer.parseInt(request.getParameter("showDisable")));		
		param.put("qPeriod" , request.getParameter("qPeriod"));
		if("period".equals(param.get("qPeriod"))){
			param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
			param.put("qBeginMonth" , Integer.parseInt(request.getParameter("qBeginMonth")));
			param.put("qEndYear" , Integer.parseInt(request.getParameter("qEndYear")));
			param.put("qEndMonth" , Integer.parseInt(request.getParameter("qEndMonth")));
		}		
		param.put("qBegD" , request.getParameter("qBegD"));
		param.put("qEndD" , request.getParameter("qEndD"));
		param.put("initBala" , Integer.parseInt(request.getParameter("initBala")));
		param.put("showDet" , Integer.parseInt(request.getParameter("showDet")));
		param.put("todayAccount" , Integer.parseInt(request.getParameter("todayAccount")));
		param.put("curPeriodAccount" , Integer.parseInt(request.getParameter("curPeriodAccount")));
		param.put("accuTotal" , Integer.parseInt(request.getParameter("accuTotal")));
		param.put("total" ,  Integer.parseInt(request.getParameter("total")));				
		Map r = CashierCashData(param);
						
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//���ش����쳣��Ϣ
	protected ActionForward CashierErr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){		
        EchoMessage.error().add("ҳ�治����").setRequest(request);       
		return getForward(request, mapping, "message");
	}
	
	//���ɹ����ʼ��
	protected ActionForward CashierInitPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,String type) {
			
		request.setAttribute("type", type);		
		return getForward(request, mapping, "CashierInitPre");
	}
	
	//������ɹ���ģ�����ó�ʼ������
	protected ActionForward CashierInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String periodYear = request.getParameter("PeriodYear");
		String periodMonth = request.getParameter("PeriodMonth");
		String to = request.getParameter("to");
		
		LoginBean loginBean = getLoginBean(request);
		Map param = new HashMap();
		param.put("PeriodYear",Integer.parseInt(periodYear));
		param.put("PeriodMonth",Integer.parseInt(periodMonth));
		param.put("createBy", loginBean.getId());
		param.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//****������ɹ���ģ���ʼ��������***//
		Result ret = mgt.initCashier(param);
		//***********end************//
		if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
			 EchoMessage.error().add("���ɳ�ʼ��ʧ��").setRequest(request);       
			 return getForward(request, mapping, "message");
		} 
		
		if("cash".equals(to)){
			return CashierCash(mapping,form,request,response);
		} else if("bank".equals(to)){
			return CashierBank(mapping,form,request,response);
		}
		return CashierErr(mapping,form,request,response);
	}		
	
	//�����ֽ��ռ���ģ��
	protected ActionForward CashierCash(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Result ret = mgt.queryCashier();
		if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
			request.setAttribute("to", "cash");
			return CashierInitPre(mapping,form,request,response,"CashierInit");
		}
		
		//****��ѯȨ����Ϣ****//		
		String sql = "select * from tblCashierSetting";
		Result result = mgt.query(sql);
		if(result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			Map m = (Map)((List)result.retVal).get(0);
			if(((String)m.get("AuditingPersont")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasAudit", 1);
			}
			if(((String)m.get("ReverseAuditing")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasReAudit", 1);
			}
			if(((String)m.get("BinderPersont")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasPosted", 1);
			}
			if(((String)m.get("ReverseBinder")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasRePosted", 1);
			}
		}else{
			request.setAttribute("hasAudit", 0);
			request.setAttribute("hasReAudit", 0);
		}
		//******end******//
		
		Map m =(Map) ret.retVal;				
		
		//****��ȡ���п�Ŀ��Ϣ****//
		Result r = mgt.getCashAcc();
		//*******end*********//
		request.setAttribute("AccCodes", r.getRetVal());
		request.setAttribute("PeriodYear", m.get("PeriodYear"));
		request.setAttribute("PeriodMonth", m.get("PeriodMonth"));
		
		return getForward(request, mapping, "cashierCash");			
	}
	
	//�����ֽ���˵���ѯ
	protected ActionForward CashierCashAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		Map r = new HashMap();
		Map param = new HashMap();
		param.put("PeriodYear", Integer.valueOf(request.getParameter("periodYear")));
		param.put("PeriodMonth", Integer.valueOf(request.getParameter("periodMonth")));
		Result res = mgt.CashAccount(param);
		
		if(res.retCode==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);
			r.put("msg", res.retVal);
		} else{
			r.put("code", 1);
			r.put("data",res.retVal);
		}
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;
	}
	
	//�����ֽ���˵�
	protected ActionForward CashierCashAccountPre(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) {
			
		//Map param = new HashMap();
		//param.put("PeriodYear", cashierYear);
		//param.put("PeriodMonth", cashierYear);		
		return getForward(request, mapping, "CashierCashAccount");
	}		
	
	//�������д���ռ���ģ��
	protected ActionForward CashierBank(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Result ret = mgt.queryCashier();
		if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
			request.setAttribute("to", "bank");
			return CashierInitPre(mapping,form,request,response,"CashierInit");
		}
		
		//****��ѯȨ����Ϣ****//		
		String sql = "select * from tblCashierSetting";
		Result result = mgt.query(sql);
		if(result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			Map m = (Map)((List)result.retVal).get(0);
			if(((String)m.get("AuditingPersont")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasAudit", 1);
			}
			if(((String)m.get("ReverseAuditing")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasReAudit", 1);
			}
			if(((String)m.get("BinderPersont")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasPosted", 1);
			}
			if(((String)m.get("ReverseBinder")).contains(this.getLoginBean(request).getId())){
				request.setAttribute("hasRePosted", 1);
			}
		}else{
			request.setAttribute("hasAudit", 0);
			request.setAttribute("hasReAudit", 0);
		}
		//******end******//
		
		Map m =(Map) ret.retVal;				
		
		//****��ȡ���п�Ŀ��Ϣ****//
		Result r = mgt.getBankAcc();
		//*******end*********//
		request.setAttribute("AccCodes", r.getRetVal());
		request.setAttribute("PeriodYear", m.get("PeriodYear"));
		request.setAttribute("PeriodMonth", m.get("PeriodMonth"));					
				
		return getForward(request, mapping, "cashierBank");
	}
	
	
	//��ȡ���д����ϸ����
	private Map CashierBankData(Map param){
		Map r = new HashMap();
		Result ret = mgt.queryBankDet(param);
		
		//****��ȡ�ڳ����****//		
		Result r1 = mgt.queryIni(param);
		
		if(ret.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
			r.put("code", 0);			
		} else{
			List<Map> data = (List)ret.getRetVal();
			Map iniMap = (Map)r1.getRetVal();
			Map row = new HashMap();
			String curYear = "";
			Integer curMonth = 0;
			if("period".equals(param.get("qPeriod"))){
				row.put("BillDate", param.get("qBeginYear")+"-"+param.get("qBeginMonth"));
				curYear = param.get("qBeginYear").toString();
				curMonth = Integer.valueOf(param.get("qBeginMonth").toString());
			}else{
				row.put("BillDate",param.get("qBegD"));
				curYear = param.get("qBegD").toString().substring(0, 4);
				curMonth = Integer.valueOf(param.get("qBegD").toString().split("-")[1]);
			}
																				
			//****��ȡ��ʼ�ڼ�ǰ�����ۼ�
			BigDecimal curYearDebitTotal = null;
			BigDecimal curYearLendTotal = null;
			Map _p = new HashMap();
			_p.put("periodY", Integer.parseInt(curYear));
			_p.put("periodM", curMonth);
			_p.put("accCode", param.get("accCode"));
			Result pre = mgt.queryPreAccum(_p);
			if(pre.getRetCode()==ErrorCanst.DEFAULT_FAILURE){
				r.put("code", 0);
				r.put("msg", "��ȡ��ʷ��Ϣʧ�ܣ�");
				return r;
			}else{
				Map _d = (Map)pre.getRetVal();
				curYearDebitTotal = new BigDecimal(_d.get("preDebitAmt").toString());
				curYearLendTotal = new BigDecimal(_d.get("preLendAmt").toString());
			}
			
			row.put("RecordComment", "�ڳ�");				
			row.put("Amount", iniMap.get("CashEndBala"));	
			row.put("others", 1);
			BigDecimal amt = (BigDecimal)iniMap.get("CashEndBala");
			
			DecimalFormat fnum = new DecimalFormat("##0.00");  
			BigDecimal  total = new BigDecimal(0.0);
			BigDecimal accuTotal = new BigDecimal(0.0);
			BigDecimal debitTotal = new BigDecimal(0.0);
			BigDecimal lendTotal = new BigDecimal(0.0);
			BigDecimal curdebitTotal = new BigDecimal(0.0);
			BigDecimal curlendTotal = new BigDecimal(0.0);			
			List<Map> arr = new ArrayList();
			
			for(Map item : data){						
				
				if((Integer)param.get("curPeriodAccount")==1){
					if(Integer.valueOf(item.get("BillDate").toString().split("-")[1]) != curMonth){
						//*****��ȡ�����ۼ�
						//param.put("curYear", Integer.valueOf(curYear));						
						Map accMap = new HashMap(row);
						accMap.put("RecordComment","���ںϼ�");
						accMap.put("BillDate", "");
						accMap.put("Amount", fnum.format(total));							
						accMap.put("DebitAmount",fnum.format(curdebitTotal));
						accMap.put("LendAmount", fnum.format(curlendTotal));														
						arr.add(accMap);
						curdebitTotal = new BigDecimal(0.0);
						curlendTotal = new BigDecimal(0.0);						
					}
				}
				
				if((Integer)param.get("accuTotal")==1){					
					if(Integer.valueOf(item.get("BillDate").toString().split("-")[1]) != curMonth){
						Map accMap1 = new HashMap(row);
						accMap1.put("RecordComment","�����ۼ�");
						accMap1.put("BillDate", "");
						accMap1.put("Amount", fnum.format(total));
						accMap1.put("DebitAmount",fnum.format(curYearDebitTotal));
						accMap1.put("LendAmount", fnum.format(curYearLendTotal));
						arr.add(accMap1);		
					}
					
					if(!"".equals(curYear) && !curYear.equals(((String)item.get("BillDate")).substring(0, 4))){
						//*****��ȡ�����ۼ�
						param.put("curYear", Integer.valueOf(curYear));						
						curYearDebitTotal = new BigDecimal(0.0);
						curYearLendTotal = new BigDecimal(0.0);						
					}					
				}
				
				debitTotal = debitTotal.add(new BigDecimal(item.get("DebitAmount").toString()));
				lendTotal = lendTotal.add(new BigDecimal(item.get("LendAmount").toString()));
				
				curYearDebitTotal = curYearDebitTotal.add(debitTotal);
				curYearLendTotal = curYearLendTotal.add(lendTotal);
				//curYearDebitTotal = debitTotal;
				//curYearLendTotal = lendTotal;
				
				curdebitTotal =curdebitTotal.add(new BigDecimal(item.get("DebitAmount").toString()));
				curlendTotal = curlendTotal.add(new BigDecimal(item.get("LendAmount").toString()));
				
				amt = amt.add(new BigDecimal(item.get("DebitAmount").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)).subtract(new BigDecimal(item.get("LendAmount").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP);										
				
				total = amt;
				item.put("Amount", fnum.format(amt));
								
				curYear = ((String)item.get("BillDate")).substring(0, 4);
				curMonth = Integer.valueOf(item.get("BillDate").toString().split("-")[1]);				
				if(new BigDecimal(item.get("DebitAmount").toString()).compareTo(BigDecimal.ZERO) ==0){
					item.put("DebitAmount", 0.00);
				}else{
					//item.put("DebitAmount", ((BigDecimal)item.get("DebitAmount")).setScale(2));
					item.put("DebitAmount", (fnum.format(item.get("DebitAmount"))));
				}
				if(new BigDecimal(item.get("LendAmount").toString()).compareTo(BigDecimal.ZERO)==0){
					item.put("LendAmount", 0.00);
				}else{
					//item.put("LendAmount", ((BigDecimal)item.get("LendAmount")).setScale(2));
					item.put("LendAmount", (fnum.format(item.get("LendAmount"))));
				}
				arr.add(item);
			}
			if((Integer)param.get("initBala")==1){
				arr.add(0, row);
			}						
			if((Integer)param.get("curPeriodAccount")==1){					
				//*****��ȡ�����ۼ�
				//param.put("curYear", Integer.valueOf(curYear));
			
				Map accMap = new HashMap(row);
				accMap.put("RecordComment","���ںϼ�");
				accMap.put("BillDate", "");
				accMap.put("Amount", fnum.format(total));							
				accMap.put("DebitAmount",fnum.format(curdebitTotal));
				accMap.put("LendAmount", fnum.format(curlendTotal));														
				arr.add(accMap);		
			}
			
			if((Integer)param.get("accuTotal")==1){
				//*****��ȡ�����ۼ�
				param.put("curYear", Integer.valueOf(curYear));
				//Result _r = mgt.queryCurYearAmt(param);
				Map accMap = new HashMap(row);
				accMap.put("RecordComment","�����ۼ�");
				accMap.put("BillDate", "");
				accMap.put("Amount", fnum.format(total));
				accMap.put("DebitAmount",curYearDebitTotal);
				accMap.put("LendAmount", curYearLendTotal);				
				arr.add(accMap);
				/*
				if(_r.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					Map _m = (Map)_r.getRetVal();						
					if(_m.get("DebitAmt") != null && !"".equals(_m.get("DebitAmt"))){
						accMap.put("DebitAmount",fnum.format(_m.get("DebitAmt")));
					}
					if(_m.get("LendAmt") != null && !"".equals(_m.get("LendAmt"))){
						accMap.put("LendAmount", fnum.format(_m.get("LendAmt")));	
					}
											
				}		
				arr.add(accMap);
				*/
			}
			
			if((Integer)param.get("total")==1){
				Map tMap = new HashMap(row);
				tMap.put("RecordComment","�ܼ�");
				tMap.put("DebitAmount", fnum.format(debitTotal));
				tMap.put("LendAmount", fnum.format(lendTotal));
				tMap.put("Amount", fnum.format(total));
				tMap.put("BillDate", "");
				arr.add(tMap);
			}
			r.put("code", 1);
			r.put("data",arr);
		}
		return  r;
	}
	
	//�������д���ռ��˲�ѯ
	protected ActionForward CashierBankQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {			
		
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));
		param.put("showDisable" , Integer.parseInt(request.getParameter("showDisable")));		
		param.put("qPeriod" , request.getParameter("qPeriod"));
		if("period".equals(param.get("qPeriod"))){
			param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
			param.put("qBeginMonth" , Integer.parseInt(request.getParameter("qBeginMonth")));
			param.put("qEndYear" , Integer.parseInt(request.getParameter("qEndYear")));
			param.put("qEndMonth" , Integer.parseInt(request.getParameter("qEndMonth")));
		}		
		param.put("qBegD" , request.getParameter("qBegD"));
		param.put("qEndD" , request.getParameter("qEndD"));
		param.put("initBala" , Integer.parseInt(request.getParameter("initBala")));
		param.put("showDet" , Integer.parseInt(request.getParameter("showDet")));
		param.put("todayAccount" , Integer.parseInt(request.getParameter("todayAccount")));
		param.put("curPeriodAccount" , Integer.parseInt(request.getParameter("curPeriodAccount")));
		param.put("accuTotal" , Integer.parseInt(request.getParameter("accuTotal")));
		param.put("total" ,  Integer.parseInt(request.getParameter("total")));	
				
		Map r = CashierBankData(param);		
				
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;			
	}
	
	//����-���ж��˵�
	protected ActionForward CashierBankAccountPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
			
		Result ret = mgt.queryCashier();
		if(ret.retCode == ErrorCanst.DEFAULT_FAILURE){
			request.setAttribute("to", "bank");
			return CashierInitPre(mapping,form,request,response,"CashierInit");
		}
		Map m =(Map) ret.retVal;
		//****��ȡ���п�Ŀ��Ϣ****//
		Result r = mgt.getBankAcc();
		//*******end*********//
		request.setAttribute("AccCodes", r.getRetVal());
		request.setAttribute("PeriodYear", m.get("PeriodYear"));
		request.setAttribute("PeriodMonth", m.get("PeriodMonth"));	
		return getForward(request, mapping, "CashierBankAccount");
	}
	
	//����-���ж��˵���ѯ
	protected ActionForward CashierBankAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("application/json;charset=UTF-8");  
		PrintWriter out = response.getWriter(); 
		
		
		Map param = new HashMap();	
		param.put("PeriodYear", Integer.valueOf(request.getParameter("periodYear")));
		param.put("PeriodMonth", Integer.valueOf(request.getParameter("periodMonth")));
		Map r = new HashMap();
		//��ȡ�����ռ�����Ϣ
		Result ret = mgt.BankAccount(param);
		if(ret.retCode == ErrorCanst.DEFAULT_SUCCESS){
			r.put("code", 1);
			r.put("data", ret.retVal);			
		}else{
			r.put("code", 0);
			r.put("msg", ret.retVal);
		}
		/*
		Map param = new HashMap();
		//��ȡ����
		param.put("accCode" ,request.getParameter("accCode"));
		param.put("currency",  request.getParameter("currency"));			
		param.put("qPeriod" , request.getParameter("qPeriod"));
		param.put("qPeriod","period");
		
		param.put("qBeginYear" , Integer.parseInt(request.getParameter("qBeginYear")));
		param.put("qBeginMonth" , Integer.parseInt(request.getParameter("qBeginMonth")));
		param.put("qEndYear" ,  Integer.parseInt(request.getParameter("qBeginYear")));
		param.put("qEndMonth" , Integer.parseInt(request.getParameter("qBeginMonth")));		
				
		param.put("total" , 0);
		param.put("curPeriodAccount" , 0);
		param.put("accuTotal" , 0);
		param.put("initBala", 0);
		Map r = new HashMap();
		try{
			//��ȡ�տ�����ϸ
			Map r1 = CashierBankBill(param);
			if((Integer)r1.get("code")==1){
				r.put("r1", r1.get("data"));
			}						
			//��ȡ�����ռ�����Ϣ
			Map r2 = CashierBankData(param);
			if((Integer)r2.get("code")==1){
				r.put("r2",r2.get("data"));
			}
			r.put("code", 1);		
		} catch(Exception e){
			r.put("code", 0);
			r.put("msg", "���ݻ�ȡ�쳣");
			BaseEnv.log.debug("CashierAction.CashierBankAccount:",e);
		}*/
		out.write(new Gson().toJson(r));
        out.flush();  
        out.close();  
		return null;	
	}
	
	private Map CashierBankBill(Map param){
		Map r = new HashMap();
		Result ret = mgt.queryBankBillDet(param);
		if(ret.retCode == ErrorCanst.DEFAULT_SUCCESS){
			r.put("code", 1);
			//��������
			List<Map> list = (List<Map>)ret.retVal;
			r.put("data", list);
		}else{
			r.put("code", 0);		
		}
		return r;
	}
	
	//��ѯժҪ����Ϣ
	private ActionForward queryRecordComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("application/json;charset=UTF-8");
		/* ��ѯժҪ����Ϣ */
		String searchValue = request.getParameter("searchValue");
		VoucherMgt mgt_v = new VoucherMgt();
		Map ret = new HashMap();
		try {
			/* ����URL�д������� */
			searchValue = java.net.URLDecoder.decode(searchValue, "UTF-8");
		} catch (Exception e) {
			ret.put("code", 0);
			ret.put("msg","�ؼ���ת��ʧ��");
		}

		/* ����������ѯ���� */
		Result result = mgt_v.queryRecord(searchValue);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("RecordList", result.retVal);
		}
		request.setAttribute("searchValue", searchValue);
		if(result.retCode == ErrorCanst.DEFAULT_FAILURE){
			ret.put("code", 0);
			ret.put("msg","���ݻ�ȡʧ��");
		} else{
			ret.put("code",1);
			ret.put("data",result.retVal);
		}
		
		PrintWriter out = response.getWriter(); 
		out.write(new Gson().toJson(ret));
        out.flush();  
        out.close();  
		return null;			
	}
	
	//*****У������
	private Map verifyDets(ArrayList<Map> dets){
		StringBuffer yesDets = new StringBuffer();
		StringBuffer noDets = new StringBuffer();
		StringBuffer noDetsNo = new StringBuffer();
		Map m = new HashMap();
		for(Map item : dets){
			if(item.get("RefAcc")==null || "".equals(item.get("RefAcc"))){
				noDets.append(item.get("RefAcc")+",");
				if(noDetsNo.length() == 0){
					noDetsNo.append(item.get("No"));
				}else{
					noDetsNo.append(","+item.get("No"));
				}
			}else{
				yesDets.append(item.get("id")+",");
			}
		}
		m.put("yesDets",yesDets.toString());
		m.put("noDets",noDets.toString());
		m.put("noDetsNo", noDetsNo.toString());
		return m;
	}
}
