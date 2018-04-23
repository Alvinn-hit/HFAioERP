package hf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class HandScode {
	public String exec(HttpServletRequest request){
		String op = request.getParameter("op");
		if("HandScode".equals(op)){
			return hand(request);
		}
		return null;
	}
	/**
	 * 出库扫描 ，扫描物流码
	 * 1、判断没有草稿，则自动建立草稿。
	 * 2、把序列号插入草稿
	 * @param request
	 * @return
	 */
	public String hand(HttpServletRequest request){
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//置空SecUnit字段
							String sql  = " update tblStockDet set SecUnit=''  ";
							PreparedStatement st=conn.prepareStatement(sql);
							st.execute();
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryBeginPeriod Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		final ArrayList<String[]> list = new ArrayList();
		retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from HFimport where workflownodename='finish' and WorkOrderNo != '期初' order by billDate";
							PreparedStatement st=conn.prepareStatement(sql);
							ResultSet rst=st.executeQuery();
							while(rst.next()){
								String id= rst.getString("id");
								String BillNo= rst.getString("BillNo");
								String scode=rst.getString("scode");
								String WorkOrderNo=rst.getString("WorkOrderNo");
								String BillDate = rst.getString("BillDate");
								String createTime = rst.getString("createTime");
								list.add(new String[]{id, BillNo, scode, WorkOrderNo, BillDate, createTime});
							}
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryBeginPeriod Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		
		for(String[] ss:list){
			handOne(ss[0],ss[1],ss[2],ss[3],ss[4],ss[5]);
		}
		
		retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//置空SecUnit字段
//							String sql  = " update tblStockDet set SecUnit=''  ";
//							PreparedStatement st=conn.prepareStatement(sql);
//							st.execute();
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryBeginPeriod Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
		

		
		return "ok";
	}
	
	public void handOne(final String id,final String BillNo,final String scode,final String WorkOrderNo,final String BillDate,final String createTime){
		BaseEnv.log.debug("BillNo="+BillNo+";BillDate="+BillDate);
		final Result rs = new Result();
		int retCode= DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {

								String WorkOrderId="";
								String wsql = "select id WorkOrderId from PDWorkOrder where billNo=?";
								PreparedStatement wst=conn.prepareStatement(wsql);
								wst.setString(1, WorkOrderNo);
								ResultSet wrst=wst.executeQuery();
								if(wrst.next()){
									WorkOrderId = wrst.getString(1);
								}else{
									BaseEnv.log.error("=====================WorkOrderId 不存在:"+id+":"+BillNo);
								}
								StringBuffer SuccessSCode = new StringBuffer();//记录执行成功条数
								int SuccessNum=0;
								int OldNum=0;
								String[] ds = scode.split("\\n");
								if( ds.length == 0) return;
								for(int i=0;i<ds.length;i++){
									String one = ds[i];
									if(one.trim().length() > 0&&one.split(",").length>3){
										String[] os = one.split(",");
										String BatchNo=os[1].trim();
										String yearNO=os[2].trim();
										String Seq=os[3].trim();
										
										//修改出入库明细的入库时间
										//先查本条数据是否存在
										wsql = "select id sdId,SecUnit,BillDate,TrackNo from tblStockDet where seq=? and instoreQty > 0 ";
										wst=conn.prepareStatement(wsql);
										wst.setString(1, Seq);
										wrst=wst.executeQuery();
										if(wrst.next()){
											String sdId = wrst.getString("sdId");
											String SecUnit = wrst.getString("SecUnit");
											String sdBillDate = wrst.getString("BillDate");
											String TrackNo = wrst.getString("TrackNo");
											if(!SecUnit.equals("")){//已经被别的导入单占有
												continue;
											}
											if(!BillDate.equals(sdBillDate) || !WorkOrderId.equals(TrackNo)){
												BaseEnv.log.info("=====================信息不一致修改:"+id+":"+BillNo+":"+Seq+":"+BillDate+":"+sdBillDate+":"+WorkOrderId+":"+TrackNo);
											}	
											String sql = " update tblStockDet set BillDate=?,TrackNo=?,SecUnit=? where id=? ";
											PreparedStatement ust=conn.prepareStatement(sql);
											ust.setString(1, BillDate);
											ust.setString(2, WorkOrderId);
											ust.setString(3, id);
											ust.setString(4, sdId);
											ust.execute();
											sql = "update [AIOFW].[dbo].[stockDetail] set inDateTime=?  where seq=?";
											ust=conn.prepareStatement(sql);
											ust.setString(1, BillDate+(createTime.substring(10)));
											ust.setString(2, Seq);
											ust.execute();
											SuccessSCode.append(one+"\n");
											SuccessNum++;
											
										}else{
											BaseEnv.log.error("=====================seq 不存在:"+id+":"+BillNo+":"+Seq);
										}
										OldNum++;
									}
								}
								String sql = " update HFImport set SuccessSCode=? ,OldNum=?,SuccessNum=? where id=? ";
								PreparedStatement qpst = conn.prepareStatement(sql);
								qpst.setString(1, SuccessSCode.toString());
								qpst.setInt(2, OldNum);
								qpst.setInt(3, SuccessNum);
								qpst.setString(4, id);
								qpst.execute();
						} catch (Exception ex) {
							BaseEnv.log.debug("RecalcucateThread.queryBeginPeriod Error:",ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});	
		rs.retCode = retCode;
	}
	
}
