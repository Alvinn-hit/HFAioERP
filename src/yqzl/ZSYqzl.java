package yqzl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

import yqzl.XmlPacket;
import yqzl.bean.NtdmtBean;

/**
 * ����ֱ��
 * @author dzp
 *
 */
public class ZSYqzl {
	private ServletContext context;
	private String host;
	private String lgnnam;
	public ZSYqzl(){
		
	}
	public ZSYqzl(ServletContext context,String host,String lgnnam){
		this.context=context;
		this.host=host;
		this.lgnnam=lgnnam;
	}
	public static DesUtil des = new DesUtil();
	public static Object msgKey = null;
	
	/**
	 * ����ֱ������
	 * @param conn  ���ݿ�����
	 * @param host  ǰ�û���ַ
	 * @param lgnnam  ��¼��
	 * @param accnbr  ʹ�õ��˺�
	 */
	public String deal(HttpServletRequest request)
    {
        String data = request.getParameter("msg");
        String op = request.getParameter("op");
        ServletContext context = request.getSession().getServletContext();
        String key = context.getInitParameter("yqzlkey");
        msgKey = context.getAttribute("org.apache.struts.action.MESSAGE");
        if("now".equals(op))
        {
           /*
        	Logger.getLogger("YQZL").info("*********************");
            Logger.getLogger("YQZL").info("���յ����챨��");
            Logger.getLogger("YQZL").info("*********************");
        	*/
        	BaseEnv.log.info("*********************");
        	BaseEnv.log.info("���յ����챨��");
        	BaseEnv.log.info("*********************");
        } else
        if("history".equals(op))
        {
            /*
        	Logger.getLogger("YQZL").info("*********************");
            Logger.getLogger("YQZL").info("���յ���ʷ����");
            Logger.getLogger("YQZL").info("*********************");
            */
            BaseEnv.log.info("*********************");
        	BaseEnv.log.info("���յ���ʷ����");
        	BaseEnv.log.info("*********************");
        }
        Jzb jzb = new Jzb();
        if(data == null || "".equals(data))
        {
            //Logger.getLogger("YQZL").info("����Ϊ��");
        	BaseEnv.log.info("����Ϊ��");
        	return "data empty";
        }
        try
        {
            data = DesUtil.decrypt(data, key);
        }
        catch(Exception e)
        {
            //Logger.getLogger("YQZL").error("���Ľ���ʧ��", e);
        	BaseEnv.log.error("���Ľ���ʧ��", e);
        	return "data decrypt failed";
        }
       /*
        Logger.getLogger("YQZL").info(">>>>>>>>>>>>>>>>>>>>>");
        Logger.getLogger("YQZL").info((new StringBuilder("���յ����ܺ���")).append(data).toString());
        Logger.getLogger("YQZL").info(">>>>>>>>>>>>>>>>>>>>>");
       */
        BaseEnv.log.info(">>>>>>>>>>>>>>>>>>>>>");
        BaseEnv.log.info((new StringBuilder("���յ����ܺ���")).append(data).toString());
        BaseEnv.log.info(">>>>>>>>>>>>>>>>>>>>>");
        
        List list = new ArrayList();
        XmlPacket resultXmlPkt = jzb.processResult(data);
        if(resultXmlPkt == null)
        	BaseEnv.log.info("resultXmlPkt is null");
        	//Logger.getLogger("YQZL").info("resultXmlPkt is null");
        if(resultXmlPkt != null)
        {
            List maps = null;
            if("now".equals(op))
                maps = resultXmlPkt.getProperty("NTDMTLSTZ");
            else
            if("history".equals(op))
                maps = resultXmlPkt.getProperty("NTDMTHLSZ");
            if(maps == null)
            	BaseEnv.log.info("maps is null");
            	//Logger.getLogger("YQZL").info("maps is null");
            if(maps != null)
            {
                NtdmtBean bean;
                for(Iterator iterator = maps.iterator(); iterator.hasNext(); list.add(bean))
                {
                    Map map = (Map)iterator.next();
                    bean = new NtdmtBean();
                    bean.setAccnbr(map.get("ACCNBR").toString());
                    bean.setDmanbr(map.get("DMANBR").toString());
                    bean.setDmanam(map.get("DMANAM").toString());
                    bean.setTrxnbr(map.get("TRXNBR").toString());
                    bean.setCcynbr(map.get("CCYNBR").toString());
                    bean.setTrxamt(map.get("TRXAMT").toString());
                    bean.setTrxdir(map.get("TRXDIR").toString());
                    bean.setTrxtim(map.get("TRXTIM").toString());
                    bean.setRpyacc(map.get("RPYACC") != null ? map.get("RPYACC").toString() : null);
                    bean.setRpynam(map.get("RPYNAM") != null ? map.get("RPYNAM").toString() : null);
                    bean.setTrxtxt(map.get("TRXTXT") != null ? map.get("TRXTXT").toString() : null);
                    bean.setNarinn(map.get("NARINN") != null ? map.get("NARINN").toString() : null);
                }

            }
        }
        dealRecord(list);
        return "msg dealed!";
    }
	
	/**
	 * ����ֱ������
	 * @param conn  ���ݿ�����
	 * @param host  ǰ�û���ַ
	 * @param lgnnam  ��¼��
	 * @param accnbr  ʹ�õ��˺�
	 */
	/*
	public void deal(String accnbr){
		//��ý����콻�׼�¼
		Jzb jzb = new Jzb(host, lgnnam);
		List<NtdmtBean> listNow=jzb.getNow(accnbr);
		dealRecord(listNow);
		//������콻�׼�¼
		SimpleDateFormat df= new SimpleDateFormat("yyyyMMdd");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);
		String begdat=df.format(now.getTime());
		now.add(Calendar.DATE, -1);
		String enddat=df.format(now.getTime());
		List<NtdmtBean> listHistory=jzb.getHistory(accnbr, begdat, enddat);
		dealRecord(listHistory);
	}*/
	/**
	 * ����ֱ�����׼�¼����
	 * @param list
	 */
	private void dealRecord(List<NtdmtBean> list){
		//���˵��Ѿ��������
		List<NtdmtBean> listNew=new ArrayList<NtdmtBean>();
		if(list!=null){
			System.out.println("����ֱ����¼����δ���ˣ���"+list.size()+" --"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			for (NtdmtBean ntdmtBean : list) {
				if(getRecordCount(ntdmtBean.getTrxnbr())==0){
					listNew.add(ntdmtBean);
				}
			}
		}
		System.out.println("����ֱ����¼�����ѹ��ˣ���"+list.size()+" --"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//�����½���
		for (NtdmtBean ntdmtBean : listNew) {
			try {
				addReceive(ntdmtBean);
			} catch (Exception e) {
				System.out.println("����տ����"+ntdmtBean.getDmanbr());
				e.printStackTrace();
			}
		}
	}
	/**
	 * ����տ��¼
	 * @param conn
	 * @param bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addReceive(NtdmtBean bean){
		System.out.println("��ʼ����տ��¼(δ����)��"+bean.getDmanbr());
		//������Ч����
		if(bean.getTrxamt().startsWith("-")||!bean.getTrxdir().equals("C")){
			System.out.println("��ʼ����տ��¼(δ����)��"+bean.getDmanbr()+"��Ч����");
			return;
		}
		System.out.println("��ʼ����տ��¼(δ����)��"+bean.getDmanbr()+"���ݱ��Ѱ�ҵ�");
		//���ݱ���ҵ���
		HashMap<String, String> company=getComapany(bean.getDmanbr());
		if(company==null){
			System.out.println("��ʼ����տ��¼(δ����)��"+bean.getDmanbr()+"�Ҳ�����");
			return;
		}
		System.out.println("��ʼ����տ��¼��"+bean.getDmanbr());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		Date now =new Date();
		HashMap saveValues = new HashMap();
		saveValues.put("id", IDGenerater.getId());
		saveValues.put("BillDate", dfShort.format(now));
		saveValues.put("ExeBalAmt", bean.getTrxamt());
		saveValues.put("AcceptTypeId", "Receive");
		saveValues.put("AccDetStatus", "0");
		saveValues.put("CreateBy", "1");
		saveValues.put("lastUpdateBy", "1");
		saveValues.put("createTime", df.format(now));
		saveValues.put("lastUpdateTime", df.format(now));
		saveValues.put("statusId", "0");
		saveValues.put("Remark", bean.getTrxtxt());
		saveValues.put("SettleType", "3");
		saveValues.put("SCompanyID", "00001");
		saveValues.put("workFlowName", "finish");
		saveValues.put("WorkFlowNode", "-1");
		saveValues.put("currentlyreceive", "0.00000000");
		saveValues.put("FCcurrentlyreceive", "0.00000000");
		saveValues.put("ReturnAmt", "0.00000000");
		saveValues.put("EmployeeID", "1");
		saveValues.put("BillFcAmt", "0.00000000");
		saveValues.put("ContractAmt", "0.00000000");
		saveValues.put("prntCount", "0");
		saveValues.put("CurrencyRate", "1.00000000");
		saveValues.put("finishTime", df.format(now));
		saveValues.put("FactIncome", bean.getTrxamt());
		saveValues.put("AccAmt", bean.getTrxamt());
		saveValues.put("DepartmentCode", "00101");
		saveValues.put("CompanyCode", company.get("classCode"));
		
		saveValues.put("CashUserID", "0");
		//saveValues.put("PeriodYear", "2015");
		//saveValues.put("PeriodMonth", "4");
		//saveValues.put("Period", "4");	
		//saveValues.put("PeriodMonth", "4");
		saveValues.put("AutoBillMarker", "0");
		//saveValues.put("CertificateNo", "04d8da956_1504291429536500010");
												
		String accnbr = bean.getAccnbr();
		String accCode = "100246";		
		//��ϸ
		ArrayList<HashMap> details= new ArrayList<HashMap>();
		HashMap detValues= new HashMap();
		detValues.put("id", IDGenerater.getId());
		detValues.put("f_ref", saveValues.get("id"));
		detValues.put("SettleType", "1");
		detValues.put("Account", accCode);		
		detValues.put("Amount", bean.getTrxamt());
		detValues.put("SCompanyID", "00001");
		detValues.put("Remark", "����ֱ��2");
		detValues.put("ExeBalFcAmt", "0.00000000");
		details.add(detValues);
		saveValues.put("TABLENAME_tblReceiveAccountDet", details);
		Hashtable props =BaseEnv.propMap;
		//Object ob = context.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		Object ob = null;
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		System.out.println("�����տ��¼ֵ��"+bean.getDmanbr());
		//����
		if(saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"),saveValues, resources,props)){
			System.out.println("����տ�ɹ���"+bean.getDmanbr());
			//��Ӵ�����Ľ��׵���ʷ��¼
			addRecord(bean);
			System.out.println("�����ʷ���׼�¼�ɹ���"+bean.getDmanbr());
		}
		
	}
	/**
	 * ��Ӵ����¼
	 * @param bean
	 * @return
	 */
	private Result addRecord(final NtdmtBean bean){
		final Result rs = new Result(); 
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             public void execute(Connection conn) {
		          	   try{
				          	String sql = "insert into TblYqzlRecord(trxnbr,dmanbr,dmanam,ccynbr,trxamt,trxdir,trxtim,rpyacc,rpynam,trxtxt,narinn,createtime) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				         	PreparedStatement pss = conn.prepareStatement(sql) ;
				         	pss.setObject(1, bean.getTrxnbr()) ;
				         	pss.setObject(2, bean.getDmanbr()) ;
				            pss.setObject(3, bean.getDmanam()) ;
				            pss.setObject(4, bean.getCcynbr()) ;
				            pss.setObject(5, bean.getTrxamt()) ;
				            pss.setObject(6, bean.getTrxdir()) ;
				            pss.setObject(7, bean.getTrxtim()) ;
				            pss.setObject(8, bean.getRpyacc()) ;
				            pss.setObject(9, bean.getRpynam()) ;
				            pss.setObject(10,bean.getTrxtxt()) ;
				            pss.setObject(11,bean.getNarinn()) ;
				            pss.setObject(12,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) ;
				            pss.execute();
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		   BaseEnv.log.error("Yqzl addRecord : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return rs;
	}
	/**
	 * ��ѯ����
	 * @param trxnbr
	 * @return
	 */
	private int getRecordCount(final String trxnbr){
		final Result rs = new Result(); 
		rs.setRealTotal(-1);
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             public void execute(Connection conn) {
		          	   try{
		          		 String sql = "select count(*) from TblYqzlRecord where trxnbr=?" ;
		     		    PreparedStatement pss = conn.prepareStatement(sql) ;
		     		    pss.setString(1, trxnbr) ;
		     		    ResultSet rss = pss.executeQuery() ;
		     		    if(rss.next()){
		     		    	rs.setRealTotal(rss.getInt(1));
		     		    }     
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		 BaseEnv.log.error("Yqzl getRecordCount : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return rs.getRealTotal();
	}
	/**
	 * ��ѯ�ŵ�
	 * @param trxnbr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HashMap getComapany(final String yqzlNo){
		final HashMap map = new HashMap();
		final Result rs = new Result(); 
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             @SuppressWarnings("unchecked")
					public void execute(Connection conn) {
		          	   try{
		          		 String sql = "select a.classCode"
		          				 	+" from tblCompany a"
		          				 	+"  where a.yqzlNo=?";
		     		    PreparedStatement pss = conn.prepareStatement(sql) ;
		     		    pss.setString(1, yqzlNo) ;
		     		    ResultSet rss = pss.executeQuery() ;
		     		    if(rss.next()){
		     		    	map.put("classCode", rss.getString("classCode"));
		     		    	rs.setRetVal(map);
		     		    }     
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		 BaseEnv.log.error("Yqzl getRecordCount : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return (HashMap) rs.getRetVal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean saveToDb(DBTableInfoBean tBean,HashMap saveValues,MessageResources resources,Hashtable props){
		
		
		
		Hashtable<String,DBTableInfoBean> allTables = BaseEnv.tableInfos;
		LoginBean loginBean = new LoginBean();
		loginBean.setId("1");
		loginBean.setSunCmpClassCode("00001");
		
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		if(table == null){
			table = new Hashtable();
			BaseEnv.sessionSet.put(loginBean.getId(), table);
		}
		
		Locale locale = Locale.getDefault();
		String addMessage = "��ӳɹ�";
		/**
		 * �ֶ������ǵ��ݱ���������õ�������
		 */
		String tableName = "";
		
		//����Ĭ��ֵ
		UserFunctionMgt userMgt = new UserFunctionMgt();
		try {							
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			//��ϸ������Ĭ��ֵ
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(tBean.getTableName(), allTables);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			return false ;
		}
				

		/*��֤�ǿ�-----���ﲻ����Ҫ����֤�Ĺ�������Ϊ��add�������и�ȫ��Ľ�����*/ 
		/********************************************
		 ִ����ؽӿ�  saveValues Ϊ�������ݵ�HashMap ����˲�������Ӧ�ӿ�������ݲ���
		 ********************************************/
		MOperation mop = new MOperation();
		mop.query = true;
		mop.add = true;
		mop.update = true;
		
		Result rs;
		try {
			rs = userMgt.add(tBean, saveValues, loginBean, "", allTables, "", "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			BaseEnv.log.error("ImportThread.importData add Error:",e);
			return false;
		}
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
		//new Yqzl().deal("http://192.168.0.66:9999", "����ֱ��ר����ͨ1", "591902896910206");
		//new Yqzl(null,"").addReceive(null);
	}
}
