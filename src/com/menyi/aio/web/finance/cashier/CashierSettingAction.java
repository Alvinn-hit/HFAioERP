package com.menyi.aio.web.finance.cashier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class CashierSettingAction extends MgtBaseAction {
	
	CashierMgt mgt = new CashierMgt();

	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		int operation = getOperation(request);
		ActionForward forward = null;	
		switch (operation) {
		case OperationConst.OP_CASHIERSET_UPPRE:
			forward = CashierSettingPre(mapping,form,request,response);
			break;		
		case OperationConst.OP_CASHIERSET_SAVE:
			forward = CashierSettingSave(mapping,form,request,response);							
			break;
		default:
			break;
		}
		return forward;		
	}
	
	protected ActionForward CashierSettingPre(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		/* 查詢出納權限设置*/
		Result result = mgt.queryCashierSetting();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Map m = (Map)result.retVal;
			if(m != null){
				//审核人
				List<Map> auditingList = new ArrayList();
				if(m.get("AuditingPersont") != null && !"".equals(m.get("AuditingPersont"))){					
						Result r1 = mgt.queryEmployees((String)m.get("AuditingPersont"));
						if(r1.retCode == ErrorCanst.DEFAULT_SUCCESS){							
							auditingList = (List)r1.retVal;				
						}					
				}
				request.setAttribute("auditingList", auditingList);
				//反审核人
				List<Map> reverseAuditingList = new ArrayList();
				if(m.get("ReverseAuditing") != null && !"".equals(m.get("ReverseAuditing"))){					
						Result r2 = mgt.queryEmployees((String)m.get("ReverseAuditing"));
						if(r2.retCode == ErrorCanst.DEFAULT_SUCCESS){							
							reverseAuditingList = (List)r2.retVal;				
						}														
				}
				request.setAttribute("reverseAuditingList", reverseAuditingList);							
			
				//过账人				
				List<Map> BinderPersontList = new ArrayList();
				if(m.get("BinderPersont") != null && !"".equals(m.get("BinderPersont"))){					
						Result r2 = mgt.queryEmployees((String)m.get("BinderPersont"));
						if(r2.retCode == ErrorCanst.DEFAULT_SUCCESS){							
							BinderPersontList = (List)r2.retVal;				
						}														
				}
				request.setAttribute("binderList", BinderPersontList);	
			
				//反过账人				
				List<Map> ReverseBinderList = new ArrayList();
				if(m.get("ReverseBinder") != null && !"".equals(m.get("ReverseBinder"))){					
						Result r2 = mgt.queryEmployees((String)m.get("ReverseBinder"));
						if(r2.retCode == ErrorCanst.DEFAULT_SUCCESS){							
							ReverseBinderList = (List)r2.retVal;				
						}														
				}
				request.setAttribute("reverseBinderList", ReverseBinderList);	
			}
			request.setAttribute("setting", m);			
			
		}
		
		return getForward(request, mapping, "CashierSettingPre");		 
	}
		
	
	protected ActionForward CashierSettingSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String auditors = request.getParameter("auditingPersont");		
		String reAuditors = request.getParameter("reverseAuditing");		
		String reverseBinder = request.getParameter("reverseBinder");
		String binder = request.getParameter("binderPersont");
		
		Map t = request.getParameterMap();
		Map m = new HashMap();
		m.put("auditors",auditors);
		m.put("reAuditors", reAuditors);
		m.put("reverseBinder", reverseBinder);
		m.put("binder", binder);
		Result r = mgt.updateCashierSetting(m);
		if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess"))
                    .setBackUrl("/CashierSettingAction.do?operation=137").
                    setAlertRequest(request);
		} else{
			 EchoMessage.success().add(getMessage(
                     request, "common.msg.updateErro"))
                     .setBackUrl("/CashierSettingAction.do?operation=137").
                     setAlertRequest(request);
		}
		return getForward(request, mapping, "message");		
	}
	
}
