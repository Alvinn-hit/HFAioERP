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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
		StringBuilder emailContent = new StringBuilder();
		ResultSet rest;     
		Statement statement; 
		String sql="select orders.clientName,orders.productName productType,(case when  DATEDIFF ( Day ,GETDATE() , orders.endDate) > 0 then '�ѿ�ͨ' else '��ͨ�ѵ���' end ) OrderStatus," +
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
		BaseEnv.log.debug("�����ʼ����ͽӿ�TocombineEmailContent sql="+sql); 
 
		try {
			emailContent.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\">");
			emailContent.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"../style/css/autocomplete.css\" /><style>.tr1 td{width:24%;font-weight:bold;font-size:12px;text-align:center;} .tr2 {height:34px;line-height:34px;} .tr2 td{color:#09F;background:#fff;font-size:12px;text-align:center;}</style></head>");		
			emailContent.append("<body><div style=\"width:100%; background:#f0f0f0;\"><img src=\"http://www.mf1288.com/img/logo.gif\" style=\"margin-left:30px;\"/></div>");   
			emailContent.append("<div style=\"font-weight:bold; font-size:14px;padding:20px 30px;\"><p>�𾴵�&nbsp;&nbsp;&nbsp;"+agtName+"&nbsp;&nbsp;&nbsp;�����ˣ����ã�</p><p>����<span style=\"color:red; margin:0px 5px;\">"+startDate+" �� "+endDate+"</span>�ڼ�<span style=\"color:red; margin:0px 5px;\">�������ڵ����ⱦϵ�в�Ʒ����</span>�Ѿ�Ϊ�����ܣ�������ǰ��������֪ͨ���������£�</p></div>");
			emailContent.append("<div id=\"dailishangmc\">");     
			emailContent.append("<table cellpadding=\"1\" cellspacing=\"1\" class=\"table1\" style=\"width: 600px; margin: 10px auto; font-size: 12px;\"><tr style=\"height: 34px; line-height: 34px;\"><td>  ���䣺<span id=\"agtemail\" runat=\"server\" style=\"color:#09F;\">"+email+"</span></td><td> �绰�� <span style=\"color: #09F;\">"+mobile+"</span></td></tr></table>");
			emailContent.append("<table cellpadding=\"1\" cellspacing=\"1\" class=\"table2\" style=\"width: 98%; background:#b5cde6; margin: 15px 0px; \">");
			emailContent.append("<tr class=\"tr1\" style=\"background: #b5cde6;  height: 34px; line-height: 34px;\">");
			emailContent.append("<td>�ͻ�����</td>");
			emailContent.append("<td> ҵ������</td>");
			emailContent.append("<td> ״̬  </td>");
			emailContent.append("<td> ����ʱ�� </td></tr>");
			 
			statement=conn.createStatement();
			rest=statement.executeQuery(sql);
			
			while(rest.next()){
				emailContent.append("<tr class =\"tr2\"><td>"+rest.getString("clientName")+"</td><td>"+rest.getString("productType")+"</td>");
				if(rest.getString("OrderStatus").equals("��ͨ�ѵ���")){
					emailContent.append("<td><font color=\"red\">"+rest.getString("OrderStatus")+"</font></td><td><font color=\"red\">"+rest.getString("OrderEndDate")+"</font></td></tr>");
				}else{
					emailContent.append("<td><font color=\"green\">"+rest.getString("OrderStatus")+"</font></td><td>"+rest.getString("OrderEndDate")+"</td></tr>");
				}
			}
			
			emailContent.append("</table><div style=\"padding:20px 30px;\">");
			emailContent.append("<p style=\"line-height:2em;color:#ff0000; font-size:12px;\">ע���������ݽ����ο�������������oa����Ʒ�������������Ѳ�Ʒ�����ᵥ����Ϊ׼��</p>");
			emailContent.append("<p style=\" text-align:right;line-height:150%; color:#333;font-size:14px;\">���ⱦ�ͷ�����<br /><label id=\"datetimenow\">"+formatter.format(new Date())+"</label></p></div>");
			emailContent.append("<div style=\"padding:0px 30px;\"><p style=\"line-height:2em; color:#666; font-size:12px;\">���ʼ���ϵͳ�Զ�����������ظ���<br />�������ʹ�ù����������κ����⣬��������ϵ�������ⱦ�ͷ���Ա��<br /> ��֩�� QQ��1035144485   �绰��0755-83461508<br />��֩�� QQ��1318272113   �绰��0755-83466100ת8053<br />���ߵ����ǵ���վ��http://www.mf1288.com ��������ϵ�����ǻᾡ��Ϊ�������</p>	</div></body></html>");
			BaseEnv.log.debug("��ȡ���ⱦ���ڶ����б�ɹ�");
			String content=emailContent.toString();
			
			com.menyi.email.util.AIOEMail aioEMail=new com.menyi.email.util.AIOEMail();
			aioEMail.send(emailAddress,displayName,smtpAuth,smtpHost,smtpPort,smtpUser,smtpPassword,smtpssl,email,subject,content);
			BaseEnv.log.debug("�����ʼ����ͽӿ�com.menyi.email.util.AIOEMail.send�ɹ�");
			rs.retCode =ErrorCanst.DEFAULT_SUCCESS;
    		rs.retVal = "�����ʼ����ͽӿ�com.menyi.email.util.AIOEMail.send�ɹ�";
    		return rs;
		}catch(Exception e){
			BaseEnv.log.error("�ӿڵ���com.menyi.email.util.AIOEMail.send����",e);
			rs.retCode =ErrorCanst.DEFAULT_FAILURE;
    		rs.retVal = "����com.menyi.email.util.AIOEMail.send�ӿ�ʧ�ܣ�"+e.getMessage();
    		return rs;
		}
		
	}
	
}
