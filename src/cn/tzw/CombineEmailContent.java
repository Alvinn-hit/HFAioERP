package cn.tzw;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.dbfactory.Result;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;


public class CombineEmailContent {
	
	
	public Result TocombineEmailContent(String startDate,String endDate,String agtId,String agtName,String email,String mobile,String emailAddress,String displayName, String smtpAuth, String smtpHost,String smtpPort,
            String smtpUser, String smtpPassword,String smtpssl,String subject,Connection conn) throws Exception{
		Result rs = new Result();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		StringBuilder emailContent = new StringBuilder();
		ResultSet rest;     
		Statement statement; 
		String sql="select orders.clientName,orders.productName productType,(case when  DATEDIFF ( Day ,GETDATE() , orders.endDate) > 0 then '已开通' else '开通已到期' end ) OrderStatus," +
				" orders.endDate OrderEndDate from " +
				" (select tzRrybaoSales.clientName as clientName,tzRrybaoProduct.name as productName,tzRrybaoSales.endDate as endDate,tzRrybaoSales.agentName as agentName,tzRrybaoSales.agentId as agentId,tzRrybaoSales.isLatest as isLatest,tzRrybaoSales.createBy as createBy,tzRrybaoSales.id as unid " +
				" from tzRrybaoSales join tzRrybaoProduct on tzRrybaoProduct.flagKey=tzRrybaoSales.productType" +
				" union" +
				" select tzRDomainSales.clientName as clientName,tzRDomailProduct.name as productName,tzRDomainSales.endDate as endDate,tzRDomainSales.agentName as agentName,tzRDomainSales.agentId as agentId,tzRDomainSales.isLatest as isLatest,tzRDomainSales.createBy as createBy,tzRDomainSales.id as unid" +
				" from tzRDomainSales" +
				" join tzRDomailProduct on tzRDomailProduct.flagKey=tzRDomainSales.productType" +
				" union" +
				" select tzRDefineProductSales.clientName as clientName,tzRDefineProduct.name as productName,tzRDefineProductSales.endDate as endDate,tzRDefineProductSales.agentName as agentName,tzRDefineProductSales.agentId as agentId,tzRDefineProductSales.isLatest as isLatest,tzRDefineProductSales.createBy as createBy,tzRDefineProductSales.id as unid" +
				" from tzRDefineProductSales" +
				" join tzRDefineProduct on tzRDefineProduct.flagKey=tzRDefineProductSales.productType) orders" +
				" where orders.endDate >= '"+startDate+"' and orders.endDate <= '"+endDate+"' and orders.isLatest=1 " +
								"and orders.agentId='"+agtId+"' order by orders.endDate desc";
		BaseEnv.log.debug("调用邮件发送接口TocombineEmailContent sql="+sql); 
 
		try {
			emailContent.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">");
			emailContent.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"../style/css/autocomplete.css\" /><style>.tr1 td{width:24%;font-weight:bold;font-size:12px;text-align:center;} .tr2 {height:34px;line-height:34px;} .tr2 td{color:#09F;background:#fff;font-size:12px;text-align:center;}</style></head>");		
			emailContent.append("<body><div style=\"width:100%; background:#f0f0f0;\"><img src=\"http://www.mf1288.com/img/logo.gif\" style=\"margin-left:30px;\"/></div>");   
			emailContent.append("<div style=\"font-weight:bold; font-size:14px;padding:20px 30px;\"><p>尊敬的&nbsp;&nbsp;&nbsp;"+agtName+"&nbsp;&nbsp;&nbsp;负责人，您好！</p><p>您在<span style=\"color:red; margin:0px 5px;\">"+startDate+" 至 "+endDate+"</span>期间<span style=\"color:red; margin:0px 5px;\">即将到期的如意宝系列产品订单</span>已经为您汇总，建议提前做好续费通知，详情如下：</p></div>");
			emailContent.append("<div id=\"dailishangmc\">");     
			emailContent.append("<table cellpadding=\"1\" cellspacing=\"1\" class=\"table1\" style=\"width: 600px; margin: 10px auto; font-size: 12px;\"><tr style=\"height: 34px; line-height: 34px;\"><td>  邮箱：<span id=\"agtemail\" runat=\"server\" style=\"color:#09F;\">"+email+"</span></td><td> 电话： <span style=\"color: #09F;\">"+mobile+"</span></td></tr></table>");
			emailContent.append("<table cellpadding=\"1\" cellspacing=\"1\" class=\"table2\" style=\"width: 98%; background:#b5cde6; margin: 15px 0px; \">");
			emailContent.append("<tr class=\"tr1\" style=\"background: #b5cde6;  height: 34px; line-height: 34px;\">");
			emailContent.append("<td>客户名称</td>");
			emailContent.append("<td> 业务名称</td>");
			emailContent.append("<td> 状态  </td>");
			emailContent.append("<td> 到期时间 </td></tr>");
			 
			statement=conn.createStatement();
			rest=statement.executeQuery(sql);
			
			while(rest.next()){
				emailContent.append("<tr class =\"tr2\"><td>"+rest.getString("clientName")+"</td><td>"+rest.getString("productType")+"</td>");
				if(rest.getString("OrderStatus").equals("开通已到期")){
					emailContent.append("<td><font color=\"red\">"+rest.getString("OrderStatus")+"</font></td><td><font color=\"red\">"+rest.getString("OrderEndDate")+"</font></td></tr>");
				}else{
					emailContent.append("<td><font color=\"green\">"+rest.getString("OrderStatus")+"</font></td><td>"+rest.getString("OrderEndDate")+"</td></tr>");
				}
			}
			
			emailContent.append("</table><div style=\"padding:20px 30px;\">");
			emailContent.append("<p style=\"line-height:2em;color:#ff0000; font-size:12px;\">注：以上数据仅作参考，请合作伙伴以oa（产品管理——急需续费产品）的提单数据为准。</p>");
			emailContent.append("<p style=\" text-align:right;line-height:150%; color:#333;font-size:14px;\">如意宝客服中心<br /><label id=\"datetimenow\">"+formatter.format(new Date())+"</label></p></div>");
			emailContent.append("<div style=\"padding:0px 30px;\"><p style=\"line-height:2em; color:#666; font-size:12px;\">本邮件由系统自动发出，请勿回复。<br />如果您在使用过程中遇到任何问题，您可以联系我们如意宝客服人员，<br /> 海蜘蛛 QQ：1035144485   电话：0755-83461508<br />狼蜘蛛 QQ：1318272113   电话：0755-83466100转8053<br />或者到我们的网站：http://www.mf1288.com 与我们联系，我们会尽快为您解决。</p>	</div></body></html>");
			BaseEnv.log.debug("获取如意宝过期订单列表成功");
			String content=emailContent.toString();
			
			com.menyi.email.util.AIOEMail aioEMail=new com.menyi.email.util.AIOEMail();
			aioEMail.send(emailAddress,displayName,smtpAuth,smtpHost,smtpPort,smtpUser,smtpPassword,smtpssl,email,subject,content);
			BaseEnv.log.debug("调用邮件发送接口com.menyi.email.util.AIOEMail.send成功");
			rs.retCode =ErrorCanst.DEFAULT_SUCCESS;
    		rs.retVal = "调用邮件发送接口com.menyi.email.util.AIOEMail.send成功";
    		return rs;
		}catch(Exception e){
			BaseEnv.log.error("接口调用com.menyi.email.util.AIOEMail.send报错：",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "调用com.menyi.email.util.AIOEMail.send接口失败，"+e.getMessage();
    		return rs;
		}
		
	}
	
}
