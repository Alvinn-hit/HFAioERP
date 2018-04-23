package com.menyi.aio.web.finance.cashier;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class CashierMgt extends AIODBManager {	
	
	//*****出纳明细过账
	public Result postCashierDets(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {						   												
							CallableStatement callStatement=conn.prepareCall("{call proc_postVoucher(?,?,?,?,?,?)}");
							callStatement.setString(1, (String)param.get("accCode"));
							callStatement.setString(2, (String)param.get("createBy"));		
							callStatement.setString(3, (String)param.get("EmployeeID"));	
							callStatement.setString(4, (String)param.get("dets"));
							callStatement.registerOutParameter(5, Types.INTEGER);
							callStatement.registerOutParameter(6,Types.VARCHAR);
							
							callStatement.execute();
							//获取返回值
							int retVal = callStatement.getInt(5);
							String retMsg = callStatement.getString(6);																					
							if(retVal == 1){
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;
								result.retVal = retMsg;
							}else{
								result.retCode = ErrorCanst.DEFAULT_FAILURE;
								result.retVal = retMsg;							
							}							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.postCashierDets:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});		
		return result;
	}
	
	//*****出纳明细反过账
		public Result repostCashierDets(final Map param){
			final Result result = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {						   												
								CallableStatement callStatement=conn.prepareCall("{call proc_repostVoucher(?,?,?)}");								
								callStatement.setString(1, (String)param.get("dets"));
								callStatement.registerOutParameter(2, Types.INTEGER);
								callStatement.registerOutParameter(3,Types.VARCHAR);
								
								callStatement.execute();
								//获取返回值
								int retVal = callStatement.getInt(2);
								String retMsg = callStatement.getString(3);																					
								if(retVal == 1){
									result.retCode = ErrorCanst.DEFAULT_SUCCESS;
									result.retVal = retMsg;
								}else{
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = retMsg;
								}							
							} catch (Exception ex) {						
								BaseEnv.log.error("CashierMgt.repostCashierDets:",ex) ;
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								result.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return result.getRetCode();
				}
			});		
			return result;
		}
	
	private Map dealResult(ResultSet res){
		Map r = new HashMap();
		try{
			int count = res.getMetaData().getColumnCount();
			List<Map> cols = new ArrayList();
			List<Map> data = new ArrayList();
			for(int i = 1 ;i<=count;i++){
				Map param = new HashMap();
				param.put("name", res.getMetaData().getColumnName(i));
				param.put("type", res.getMetaData().getColumnTypeName(i));
				cols.add(param);
			}
			
			while(res.next()){			
				Map m = new HashMap();
				for(Map item : cols){					
					if("int".equals(item.get("type"))){
						m.put(item.get("name"), res.getInt((String)item.get("name")));
					}else if("varchar".equals(item.get("type"))){
						m.put(item.get("name"), res.getString((String)item.get("name")));
					}else if("numeric".equals(item.get("type"))){					
						BigDecimal amt = res.getBigDecimal((String)item.get("name"));	
						amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
						m.put(item.get("name"), amt);
					}					
				}
				data.add(m);
			}
			r.put("code", ErrorCanst.DEFAULT_SUCCESS);
			r.put("data", data);
			return r;
		}catch(Exception e){
			r.put("code",ErrorCanst.DEFAULT_FAILURE);
			r.put("msg",e.getMessage());
			BaseEnv.log.error("CashierMgt.dealResult:",e) ;
			return r;
		}			
	}
	
	/**
	 * 现金对账单
	 */
	public Result BankAccount(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {						   					
							PreparedStatement st = conn.prepareStatement("{call proc_CashierBankAccount(?,?)}");
							st.setInt(1, (Integer)param.get("PeriodYear"));
							st.setInt(2, (Integer)param.get("PeriodMonth"));																									
							ResultSet rs = st.executeQuery();
																										
							int retVal = ErrorCanst.DEFAULT_SUCCESS;
							String retMsg = "";
							if(retVal == ErrorCanst.DEFAULT_SUCCESS){
								Map m = dealResult(rs);
								if((Integer)m.get("code") == ErrorCanst.DEFAULT_SUCCESS){
									result.retCode = ErrorCanst.DEFAULT_SUCCESS;
									result.retVal = m.get("data");
								}else{
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = m.get("msg");
								}
							}else{
								result.retCode = ErrorCanst.DEFAULT_FAILURE;
								result.retVal = retMsg;
							}							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.CashAccount:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});		
		return result;
	}
	
	
	/**
	 * 现金对账单
	 */
	public Result CashAccount(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {											
						    /*
							CallableStatement callStatement=conn.prepareCall("{call proc_CashierCashAccount(?,?,?,?)}");						    
						    callStatement.setInt(1, (Integer)param.get("PeriodYear"));
						    callStatement.setInt(2, (Integer)param.get("PeriodMonth"));
						    callStatement.registerOutParameter(3, Types.INTEGER);
							callStatement.registerOutParameter(4, Types.VARCHAR);													
							ResultSet rs = callStatement.executeQuery();							
							*/							
							PreparedStatement st = conn.prepareStatement("{call proc_CashierCashAccount(?,?)}");
							st.setInt(1, (Integer)param.get("PeriodYear"));
							st.setInt(2, (Integer)param.get("PeriodMonth"));																									
							ResultSet rs = st.executeQuery();
						
							//获取返回值
							//int retVal = callStatement.getInt(3);
							//String retMsg = callStatement.getString(4);								
							
							int retVal = ErrorCanst.DEFAULT_SUCCESS;
							String retMsg = "";
							if(retVal == ErrorCanst.DEFAULT_SUCCESS){
								Map m = dealResult(rs);
								if((Integer)m.get("code") == ErrorCanst.DEFAULT_SUCCESS){
									result.retCode = ErrorCanst.DEFAULT_SUCCESS;
									result.retVal = m.get("data");
								}else{
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = m.get("msg");
								}
							}else{
								result.retCode = ErrorCanst.DEFAULT_FAILURE;
								result.retVal = retMsg;
							}							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.CashAccount:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});		
		return result;
	}
	
	/**
	 * 日记账反月结
	 */
	public Result CashierRetBala(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {											
						    CallableStatement callStatement=conn.prepareCall("{call proc_CashierRetBala(?,?,?)}");
						    callStatement.setString(1,(String)param.get("eid"));
						    callStatement.registerOutParameter(2, Types.INTEGER);
							callStatement.registerOutParameter(3, Types.VARCHAR);
							
							callStatement.execute();
							//获取返回值
							int retVal = callStatement.getInt(2);
							String retMsg = callStatement.getString(3);
							
							result.retVal = retMsg;
							result.retCode = retVal;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.CashierRetBala:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});		
		return result;
	}
	
	/**
	 * 日记账月结
	 */
	public Result CashierBala(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {											
						    CallableStatement callStatement=conn.prepareCall("{call proc_CashierBala(?,?,?)}");
						    callStatement.setString(1,(String)param.get("eid"));
						    callStatement.registerOutParameter(2, Types.INTEGER);
							callStatement.registerOutParameter(3, Types.VARCHAR);
							
							callStatement.execute();
							//获取返回值
							int retVal = callStatement.getInt(2);
							String retMsg = callStatement.getString(3);
							
							result.retVal = retMsg;
							result.retCode = retVal;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.CashierBala:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});		
		return result;
	}
	
	/**
	 * 
	 * 获取本年累计金额
	 */
	public Result queryCurYearAmt(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" select AccCode,sum(DebitAmount) as DebitAmt,sum(LendAmount) as LendAmt from tblCashierAccount ");
							sql.append(" where  AccCode = ? and PeriodYear = ? group by AccCode ");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							st.setString(1, (String)param.get("accCode"));
							st.setInt(2,(Integer)param.get("curYear"));
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							Map m = new HashMap();
							while(rs.next()){								
								
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}else if("numeric".equals(item.get("type"))){
										//m.put(item.get("name"), rs.getFloat((String)item.get("name")));
										BigDecimal amt = rs.getBigDecimal((String)item.get("name"));	
										amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
										m.put(item.get("name"), amt);
									}
								}
								break;
							}							
								result.retVal = m;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCurYearAmt:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 
	 * 执行sql获取数据
	 */
	public Result query(final String execSQL){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(execSQL);
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							
							while(rs.next()){								
								Map m = new HashMap();
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}
								}
								data.add(m);
							}							
								result.retVal = data;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.query:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 获取期初
	 */
	public Result queryIni(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer();							
							sql.append(" select round((isnull(a.sumDebit,0)-isnull(a.sumLend,0))+isnull(b.CashEndBala,0),2) as CashEndBala,isnull(a.sumDebit,0) as CashCurDebit,isnull(a.sumLend,0) as CashCurLend from (select top 1 CashEndBala,CashCurDebit,CashCurLend,accCode from tblCashierBalance a where a.accCode = ? and ((a.PeriodYear < ?) or (a.PeriodYear = ? and a.PeriodMonth <?)) order by a.PeriodYear desc,a.PeriodMonth desc) b left join  ");
							sql.append(" (select accCode,sum(DebitAmount) as sumDebit,sum(LendAmount) as sumLend from tblCashierAccount where  AccCode = ? and (PeriodYear<? or (PeriodYear=? and PeriodMonth <?)) group by accCode ) a  on a.accCode=b.accCode ");
							PreparedStatement st = conn.prepareStatement(sql.toString());													
													
							st.setString(1, (String)param.get("accCode"));
							if("period".equals(param.get("qPeriod"))){								
								st.setInt(2, (Integer)param.get("qBeginYear"));
								st.setInt(3, (Integer)param.get("qBeginYear"));
								st.setInt(4, (Integer)param.get("qBeginMonth"));
								
								st.setString(5, (String)param.get("accCode"));
								st.setInt(6, (Integer)param.get("qBeginYear"));
								st.setInt(7, (Integer)param.get("qBeginYear"));
								st.setInt(8, (Integer)param.get("qBeginMonth"));
							}else{
								Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)param.get("qBegD"));
								Calendar c = Calendar.getInstance();
						        c.setTime(date);
						        
						        st.setInt(2, c.get(Calendar.YEAR));
						        st.setInt(3, c.get(Calendar.YEAR));
						        st.setInt(4, c.get(Calendar.MONTH)+1);
						        
						        st.setString(5, (String)param.get("accCode"));
						        st.setInt(6, c.get(Calendar.YEAR));
						        st.setInt(7, c.get(Calendar.YEAR));
						        st.setInt(8, c.get(Calendar.MONTH)+1);
							}
														
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							//List<Map> data = new ArrayList();
							Map data = null;
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							
							while(rs.next()){								
								Map m = new HashMap();
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}else if("numeric".equals(item.get("type"))){
										//m.put(item.get("name"), rs.getFloat((String)item.get("name")));										
										
										BigDecimal amt = rs.getBigDecimal((String)item.get("name"));	
										amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
										m.put(item.get("name"), amt);
										
									}
								}
								data = m;
								break;
							}							
								
								result.retVal = data;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryIni:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 获取当前区间序号
	 */
	public Result queryCurNo(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select isNull(max(No)+1,1) as CurNo from tblCashierAccount where PeriodYear=? and PeriodMonth = ?");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							st.setInt(1, (Integer)param.get("accYear"));
							st.setInt(2,(Integer)param.get("accMonth"));
							ResultSet rs = st.executeQuery();
							Map m = new HashMap();
							while(rs.next()){								
								m.put("CurNo", rs.getInt("CurNo"));	
								break;
							}
							if(m.get("CurNo") != null){
								result.retVal = m;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							} else{
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCurNo:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 查询出纳当前区间
	 * @return
	 */
	public Result queryCashier(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select top 1 * from tblCashierPeriod where statusID=1 order by PeriodYear desc,PeriodMonth desc");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							
							ResultSet rs = st.executeQuery();
							Map m = new HashMap();
							while(rs.next()){								
								m.put("PeriodYear", rs.getInt("PeriodYear"));
								m.put("PeriodMonth", rs.getInt("PeriodMonth"));
							}
							if(m.get("PeriodYear") != null && m.get("PeriodMonth") != null){
								result.retVal = m;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							} else{
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCashier:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	public Result initCashier(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" insert into tblCashierPeriod(id,PeriodYear,PeriodMonth,createBy,createTime,statusID) values(?,?,?,?,?,?)");
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,IDGenerater.getId());
							pss.setInt(2, (Integer)param.get("PeriodYear"));
							pss.setInt(3, (Integer)param.get("PeriodMonth"));
							pss.setString(4,(String)param.get("createBy"));
							pss.setString(5,(String)param.get("createTime"));
							pss.setInt(6, 1);
							pss.executeUpdate();
																					
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;												
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.initCashier:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***删除当前日记账
	public Result delCashierDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							StringBuffer sql = new StringBuffer(" delete from tblCashierAccount where id = ? ");											
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;																				
							pss.setString(1, (String)param.get("id"));							
							pss.executeUpdate();							
														
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
							BaseEnv.log.debug(sql.toString());	
							BaseEnv.log.debug("参数1："+(String)param.get("id"));
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.delCashierDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***查询当前当前科目现金期初
	public Result queryAccBala(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" select a.BillDate,a.No,(a.CredType+a.CredNo) as CredTypeID,(a.PeriodYear+'年'+a.PeriodMonth+'期') as Period,a.RecordComment,(a.RefAcc+b.AccName) as RefAcc,sum(a.DebitAmount) DebitAmount,sum(a.LendAmount) LendAmount,c.EmpFullName as handler,d.EmpFullName as creator  ");
							
							sql.append(" from tblCashierAccount a join tblAccTypeInfo b on a.accCode = b.AccNumber left join tblEmployee c on a.EmployeeID = c.id left join tblEmployee d on a.createBy = d.id where 1=1 ");
							
							sql.append(" and a.accCode = ? and a.currency = ? " );
							
							//****根据过滤条件拼接SQL
							if("period".equals((String)param.get("period"))){
								sql.append(" and a.PeriodYear between ? and ?  and a.PeriodMonth between ? and ? ");
							} else{
								sql.append(" and a.BillDate between ? and ? ");
							}
							
							sql.append(" group by a.BillDate,a.No,a.CredType,a.CredNo, a.PeriodYear,a.PeriodMonth,a.RecordComment,a.RefAcc,b.AccName,c.EmpFullName,d.EmpFullName ");
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("accCode"));							
							pss.setString(2,(String)param.get("currency"));
							
							if("period".equals((String)param.get("period"))){
								pss.setInt(3, (Integer)param.get("qBeginYear"));
								pss.setInt(4, (Integer)param.get("qEndYear"));
								pss.setInt(5, (Integer)param.get("qBeginMonth"));
								pss.setInt(6, (Integer)param.get("qEndMonth"));
							} else{
								pss.setString(3,(String)param.get("qBegD"));							
								pss.setString(4,(String)param.get("qEndD"));
							}
														
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {
								
								Object[] os = new Object[rs.getMetaData().getColumnCount()];
                                for (int i = 1; i <= os.length; i++) {
                                    os[i - 1] = rs.getObject(i);
                                }
                                list.add(os);
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryAccBala:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***查询银行对账单明细
	public Result queryBankBillDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer();																																		
							sql.append("select * from(");
							sql.append("select a.BillDate,a.AcceptTypeID as type,(case a.AcceptTypeID when 'PreReceive' then '预收' else '应收' end) as name,b.Amount as DebitAmt,0 as LendAmt from tblSaleReceive a join tblReceiveAccountDet b on a.id = b.f_ref where  b.Account = ? and  a.PeriodYear = ? and a.PeriodMonth = ?  ");
							sql.append(" union ");
							sql.append("select a.BillDate,a.PaytypeID as type,(case a.PaytypeID when 'PrePay' then '预付' else '应付' end) as name,0 as DebitAmt,b.Amount as LendAmt from tblPay a join tblPayAccountDet b on a.id = b.f_ref where  b.Account =? and a.PeriodYear=? and a.PeriodMonth = ?  ");
							sql.append(")t order by BillDate asc");
							
							
							BaseEnv.log.debug(sql.toString());
							BaseEnv.log.debug(param.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("accCode"));																					
							pss.setInt(2, (Integer)param.get("qBeginYear"));
							pss.setInt(3, (Integer)param.get("qBeginMonth"));															
							pss.setString(4,(String)param.get("accCode"));																					
							pss.setInt(5, (Integer)param.get("qBeginYear"));
							pss.setInt(6, (Integer)param.get("qBeginMonth"));		
							
							ResultSet rs = pss.executeQuery();							
							Map m = dealResult(rs);	
							if((Integer)m.get("code") == ErrorCanst.DEFAULT_SUCCESS){
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;
								result.retVal = m.get("data");
							}else{
								result.retCode = ErrorCanst.DEFAULT_FAILURE;
								result.retVal = m.get("msg");
							}						
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryBankBillDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***查询银行日记账	
	public Result queryBankDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select *,row_number() over(order by BillDate asc,No asc) as row from (select a.id,a.BillDate,a.No,(a.CredType+cast((case when a.refBillID is not null and a.refBillID !='' then f.orderNo else a.CredNo end) as varchar(20))) as CredTypeID,(cast(a.PeriodYear as varchar(10))+'年'+cast(a.PeriodMonth as varchar(10))+'期') as Period,a.RecordComment,(a.RefAcc+e.zh_CN) as RefAcc,sum(a.DebitAmount) DebitAmount,sum(a.LendAmount) LendAmount,c.EmpFullName as handler,d.EmpFullName as creator  ");
							
							sql.append(" , f.isAuditing, (case when a.workFlowNodeName ='finish' then '√' else '' end) as workFlowNodeName,f.workFlowNodeName as isPosted,(case when a.Posted = 'finish' then '√' else '' end) as hasPosted ");														
							
							sql.append(" from tblCashierAccount a  left join tblAccTypeInfo b on a.RefAcc = b.AccNumber left join tblAccMain f on a.refBillID = f.id left join tblLanguage e on e.id = b.AccName left join tblEmployee c on a.EmployeeID = c.id left join tblEmployee d on a.createBy = d.id where 1=1 ");
							
							sql.append(" and a.accCode = ? and a.currency = ? " );
							
							//****根据过滤条件拼接SQL
							if("period".equals((String)param.get("qPeriod"))){
								sql.append(" and a.PeriodYear between ? and ?  and a.PeriodMonth between ? and ? ");
							} else{
								sql.append(" and a.BillDate between ? and ? ");
							}
							
							sql.append(" group by a.id,a.BillDate,a.No,a.CredType,a.CredNo, a.PeriodYear,a.PeriodMonth,a.RecordComment,a.RefAcc,e.zh_CN,c.EmpFullName,d.EmpFullName,f.isAuditing, f.workFlowNodeName,a.workFlowNodeName,a.refBillID,f.orderNo,a.Posted)t ");
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("accCode"));					
							pss.setString(2,(String)param.get("currency"));
							
							if("period".equals((String)param.get("qPeriod"))){
								pss.setInt(3, (Integer)param.get("qBeginYear"));
								pss.setInt(4, (Integer)param.get("qEndYear"));
								pss.setInt(5, (Integer)param.get("qBeginMonth"));
								pss.setInt(6, (Integer)param.get("qEndMonth"));
							} else{
								pss.setString(3,(String)param.get("qBegD"));							
								pss.setString(4,(String)param.get("qEndD"));
							}
														
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {
								
								Map row = new HashMap();
								row.put("BillDate", rs.getString("BillDate"));
								row.put("No", rs.getInt("No"));
								row.put("CredTypeID",rs.getString("CredTypeID"));
								row.put("Period",rs.getString("Period"));
								row.put("RecordComment", rs.getString("RecordComment"));
								row.put("RefAcc",rs.getString("RefAcc"));
								row.put("DebitAmount", rs.getBigDecimal("DebitAmount"));
								row.put("LendAmount", rs.getBigDecimal("LendAmount"));
								row.put("handler", rs.getString("handler"));
								row.put("creator", rs.getString("creator"));
								row.put("id", rs.getString("id"));
								row.put("isAudited", rs.getString("isAuditing"));
								row.put("isPosted", rs.getString("isPosted"));
								row.put("hasPosted", rs.getString("hasPosted"));
								row.put("workFlowNodeName", rs.getString("workFlowNodeName"));
								row.put("row", rs.getInt("row"));
								list.add(row);
																
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryBankDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***获取当前期间最大凭证号
	public Result getMaxOrderNo(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();														
							
							sql.append(" select isnull(max(OrderNo),0)+1 as OrderNo from tblAccMain where CredYear = ? and CredMonth = ? ");																
							
							BaseEnv.log.debug(sql.toString());
							BaseEnv.log.debug(param.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setInt(1,(Integer)param.get("PeriodYear"));					
							pss.setInt(2,(Integer)param.get("PeriodMonth"));
														
							ResultSet rs = pss.executeQuery();
							//List list = new ArrayList();
							Map data = new HashMap();
							int pos = 0;
							while (rs.next()) {									
								data.put("OrderNo", rs.getInt("OrderNo"));																						
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(data);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.getMaxOrderNo:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***根据日记账合并生成凭证
	public Result genVoucherSummary(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							StringBuffer sql = new StringBuffer(" {call proc_genVoucher(?,?,?,?,?,?,?,?,?,?,?,?,?,?)} ");							
							BaseEnv.log.debug(sql.toString());
																																				
							CallableStatement pss = conn.prepareCall(sql.toString()) ;
							BaseEnv.log.debug("params:"+new Gson().toJson(param)) ;
							
							pss.setString(1,(String)param.get("accCode"));					
							pss.setString(2,(String)param.get("currency"));	
							pss.setString(3,(String)param.get("qPeriod"));
							pss.setInt(4, (Integer)param.get("qBeginYear"));
							pss.setInt(5, (Integer)param.get("qEndYear"));
							pss.setInt(6, (Integer)param.get("qBeginMonth"));
							pss.setInt(7, (Integer)param.get("qEndMonth"));
							pss.setString(8, (String)param.get("accWord"));							
							pss.setString(9,(String)param.get("qBegD"));			
							pss.setString(10,(String)param.get("qEndD"));
							pss.setString(11,(String)param.get("createBy"));
							pss.setString(12,(String)param.get("handler"));
							
							pss.registerOutParameter(13, Types.INTEGER);
							pss.registerOutParameter(14, Types.VARCHAR);
							pss.execute();							
																					
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;														
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.genVoucherSummary:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***根据日记账生成凭证
	public Result genVoucher(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							StringBuffer sql = new StringBuffer(" insert into tblAccMain(id, BillDate,RefBillType,CredYear,CredMonth,CreateBy,statusId,Period,EmployeeID,OrderNo,CredTypeID,SCompanyID,AutoBillMarker,workFlowNodeName,workFlowNode,printCount,isReview,isAuditing,CashFlag,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																					
							pss.setString(1, (String)param.get("vid"));
							pss.setString(2, (String)param.get("BillDate"));
							pss.setString(3, (String)param.get("tblAccMain"));
							pss.setInt(4, (Integer)param.get("PeriodYear"));
							pss.setInt(5, (Integer)param.get("PeriodMonth"));
							pss.setString(6, (String)param.get("createBy"));
							pss.setInt(7, 1);
							pss.setInt(8,(Integer)param.get("PeriodMonth"));
							pss.setString(9,(String)param.get("EmployeeID"));
							pss.setInt(10,(Integer)param.get("OrderNo"));
							pss.setString(11,(String)param.get("CredType"));
							pss.setString(12,"00001");
							pss.setInt(13,0);
							pss.setString(14,"notApprove");
							pss.setString(15,"0");	
							pss.setInt(16,0);
							pss.setInt(17,1);
							pss.setString(18,"start");
							pss.setString(19,"-1");
							pss.setString(20,new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
							pss.executeUpdate();							
							
							//*****生成凭证明细********//
							StringBuffer sql3 = new StringBuffer(" insert into tblAccDetail(id,RecordComment,AccCode,DebitAmount,LendAmount,EmployeeID,RefBillType,AccDate,PeriodMonth,PeriodYear,f_ref,createTime,SCompanyID,DebitCurrencyAmount,LendCurrencyAmount) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
							PreparedStatement pss3 = conn.prepareStatement(sql3.toString());
							//*****插入借方
							pss3.setString(1, IDGenerater.getId());
							pss3.setString(2, (String)param.get("RecordComment"));
							if((Float)param.get("DebitAmount") > 0){
								pss3.setString(3, (String)param.get("accCode"));
								pss3.setFloat(4, (Float)param.get("DebitAmount"));
								pss3.setFloat(5, 0);
							}else{
								pss3.setString(3, (String)param.get("RefAcc"));
								pss3.setFloat(4, (Float)param.get("LendAmount"));
								pss3.setFloat(5, 0);
							}
							
							pss3.setString(6, (String)param.get("EmployeeID"));
							pss3.setString(7, "tblAccMain");							
							pss3.setString(8,(String)param.get("BillDate"));
							pss3.setInt(9,(Integer)param.get("PeriodMonth"));
							pss3.setInt(10,(Integer)param.get("PeriodYear"));							
							pss3.setString(11,(String)param.get("vid"));
							pss3.setString(12,new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
							pss3.setString(13,"00001");	
							
							pss3.setFloat(14, (Float)param.get("DebitAmount"));			
							pss3.setFloat(15, (Float)param.get("LendAmount"));
							
							pss3.executeUpdate();					
							
							//*****插入贷方
							pss3.setString(1, IDGenerater.getId());							
							if((Float)param.get("DebitAmount") > 0){
								pss3.setString(3, (String)param.get("RefAcc"));
								pss3.setFloat(4, 0);
								pss3.setFloat(5, (Float)param.get("DebitAmount"));
							}else{
								pss3.setString(3, (String)param.get("accCode"));
								pss3.setFloat(4, 0);
								pss3.setFloat(5, (Float)param.get("LendAmount"));
							}																		
							pss3.executeUpdate();	
							//*****更新日记账关联id*******//
							StringBuffer sql2 = new StringBuffer(" update tblCashierAccount set refBillID = ?,isAudited=?,isPosted=? where id = ? ");
							PreparedStatement pss2 = conn.prepareStatement(sql2.toString()) ;
							
							pss2.setString(1, (String)param.get("vid"));
							pss2.setString(2, "start");
							pss2.setString(3, "notApprove");
							pss2.setString(4, (String)param.get("cid"));
							pss2.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
							BaseEnv.log.debug(sql2.toString());
							BaseEnv.log.debug("参数1："+(String)param.get("vid"));
							BaseEnv.log.debug("参数4："+(String)param.get("cid"));
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.genVoucher:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***查询未生成凭证切已审核日记账
	public Result queryCashData(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();
							
							//sql.append(" select a.id,a.BillDate,a.No,(a.CredType+cast(a.CredNo as varchar(20))) as CredTypeID,(cast(a.PeriodYear as varchar(10))+'年'+cast(a.PeriodMonth as varchar(10))+'期') as Period,a.RecordComment,(a.RefAcc+e.zh_CN) as RefAcc,sum(a.DebitAmount) DebitAmount,sum(a.LendAmount) LendAmount,c.EmpFullName as handler,d.EmpFullName as creator , f.isAuditing, f.workFlowNodeName ");
							
							sql.append(" select a.id, a.BillDate,a.No,a.CredType,a.CredNo,a.PeriodYear,a.PeriodMonth,a.RecordComment,a.RefAcc,a.createBy,a.EmployeeID,a.DebitAmount,a.LendAmount ");
							
							sql.append(" from tblCashierAccount a where 1=1 ");
							
							sql.append(" and a.accCode = ? and a.currency = ? " );													
							
							//****根据过滤条件拼接SQL
							if("period".equals((String)param.get("qPeriod"))){
								sql.append(" and a.PeriodYear between ? and ?  and a.PeriodMonth between ? and ? ");
							} else{
								sql.append(" and a.BillDate between ? and ? ");
							}
							
							sql.append(" and a.CredType = ?");
							
							sql.append(" and (a.refBillID is null or a.refBillID = '') and a.workFlowNodeName ='finish' ");
							//sql.append(" and ((a.refBillID is null or a.refBillID = '') or (a.BillType is not null and  a.BillType != '')) and a.workFlowNodeName ='finish' ");													
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("accCode"));					
							pss.setString(2,(String)param.get("currency"));
							
							if("period".equals((String)param.get("qPeriod"))){
								pss.setInt(3, (Integer)param.get("qBeginYear"));
								pss.setInt(4, (Integer)param.get("qEndYear"));
								pss.setInt(5, (Integer)param.get("qBeginMonth"));
								pss.setInt(6, (Integer)param.get("qEndMonth"));
								pss.setString(7, (String)param.get("accWord"));
							} else{
								pss.setString(3,(String)param.get("qBegD"));							
								pss.setString(4,(String)param.get("qEndD"));
								pss.setString(5, (String)param.get("accWord"));
							}							
							
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {								
								Map row = new HashMap();
								row.put("BillDate", rs.getString("BillDate"));
								row.put("No", rs.getInt("No"));
								row.put("CredType",rs.getString("CredType"));
								row.put("CredNo",rs.getString("CredNo"));
								row.put("PeriodYear",rs.getInt("PeriodYear"));
								row.put("PeriodMonth",rs.getInt("PeriodMonth"));
								row.put("RecordComment", rs.getString("RecordComment"));
								row.put("RefAcc",rs.getString("RefAcc"));
								row.put("DebitAmount", rs.getFloat("DebitAmount"));
								row.put("LendAmount", rs.getFloat("LendAmount"));								
								row.put("cid", rs.getString("id"));
								row.put("createBy",rs.getString("createBy"));
								row.put("EmployeeID", rs.getString("EmployeeID"));
								
								list.add(row);																
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCashData:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***查询现金日记账-根据id
	public Result queryCashDetByID(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();
							
							//sql.append(" select a.id,a.BillDate,a.No,(a.CredType+cast(a.CredNo as varchar(20))) as CredTypeID,(cast(a.PeriodYear as varchar(10))+'年'+cast(a.PeriodMonth as varchar(10))+'期') as Period,a.RecordComment,(a.RefAcc+e.zh_CN) as RefAcc,sum(a.DebitAmount) DebitAmount,sum(a.LendAmount) LendAmount,c.EmpFullName as handler,d.EmpFullName as creator , f.isAuditing, f.workFlowNodeName ");
							
							sql.append(" select a.id,a.AccCode,a.BillDate,a.No,a.CredType,a.CredNo,a.PeriodYear,a.PeriodMonth,(cast(a.PeriodYear as varchar(10))+'年'+cast(a.PeriodMonth as varchar(10))+'期') as Period,a.RecordComment,a.RefAcc,e.zh_CN as RefAccName,a.DebitAmount,a.LendAmount,c.EmpFullName as handlerName,d.EmpFullName as creator ,a.EmployeeID as handler, f.isAuditing, f.workFlowNodeName as isPosted ,a.workFlowNodeName ");
							sql.append(" from tblCashierAccount a left join tblAccTypeInfo b on a.RefAcc = b.AccNumber left join tblAccMain f on a.refBillID = f.id left join tblLanguage e on e.id = b.AccName left join tblEmployee c on a.EmployeeID = c.id left join tblEmployee d on a.createBy = d.id where 1=1 ");
							
							sql.append(" and a.id = ? " );														
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("id"));															
														
							ResultSet rs = pss.executeQuery();
							Map res = new HashMap();
							int pos = 0;
							while (rs.next()) {								
								res.put("id", rs.getString("id"));
								res.put("BillDate",rs.getString("BillDate"));
								res.put("No", rs.getInt("No"));		
								res.put("CredType",rs.getString("CredType"));
								res.put("CredNo",rs.getString("CredNo"));
								res.put("PeriodYear",rs.getString("PeriodYear"));
								res.put("PeriodMonth",rs.getString("PeriodMonth"));
								res.put("RecordComment", rs.getString("RecordComment"));
								res.put("RefAcc",rs.getString("RefAcc"));
								res.put("RefAccName",rs.getString("RefAccName"));
								res.put("DebitAmount", rs.getFloat("DebitAmount"));
								res.put("LendAmount", rs.getFloat("LendAmount"));
								res.put("handler", rs.getString("handler"));
								res.put("handlerName", rs.getString("handlerName"));
								res.put("creator", rs.getString("creator"));
								res.put("Period", rs.getString("Period"));
								res.put("isPosted", rs.getString("isPosted"));
								res.put("workFlowNodeName", rs.getString("workFlowNodeName"));
							}
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(res);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCashDetByID:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	
	
	
	//***查询现金日记账
	public Result queryCashDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();
							
							sql.append("select *,row_number() over(order by BillDate asc,No asc,CredNo asc) as row from (select a.id,a.BillDate,a.No,a.CredNo,(a.CredType+cast((case when a.refBillID is not null and a.refBillID !='' then f.orderNo else a.CredNo end) as varchar(20))) as CredTypeID,(cast(a.PeriodYear as varchar(10))+'年'+cast(a.PeriodMonth as varchar(10))+'期') as Period,a.RecordComment,(a.RefAcc+e.zh_CN) as RefAcc,sum(a.DebitAmount) DebitAmount,sum(a.LendAmount) LendAmount,c.EmpFullName as handler,d.EmpFullName as creator , f.isAuditing, (case when a.workFlowNodeName ='finish' then '√' else '' end) as workFlowNodeName,f.workFlowNodeName as isPosted,(case when f.isAuditing= 'finish' then '已审核' else '' end) as Audited,(case when f.workFlowNodeName = 'finish' then '已过账' else '' end) as Posted,(case when a.Posted = 'finish' then '√' else '' end) as hasPosted ");
							
							sql.append(" from tblCashierAccount a left join tblAccTypeInfo b on a.RefAcc = b.AccNumber left join tblAccMain f on a.refBillID = f.id left join tblLanguage e on e.id = b.AccName left join tblEmployee c on a.EmployeeID = c.id left join tblEmployee d on a.createBy = d.id where 1=1 ");
							
							sql.append(" and a.accCode = ? and a.currency = ? " );
							
							//****根据过滤条件拼接SQL
							if("period".equals((String)param.get("qPeriod"))){
								sql.append(" and a.PeriodYear between ? and ?  and a.PeriodMonth between ? and ? ");
							} else{
								sql.append(" and a.BillDate between ? and ? ");
							}
														
							sql.append("  group by a.id,a.BillDate,a.No,a.CredType,a.CredNo, a.PeriodYear,a.PeriodMonth,a.RecordComment,a.RefAcc,e.zh_CN,c.EmpFullName,d.EmpFullName,f.isAuditing, f.workFlowNodeName,a.workFlowNodeName,a.refBillID,f.orderNo,a.Posted)t");
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)param.get("accCode"));					
							pss.setString(2,(String)param.get("currency"));
							
							if("period".equals((String)param.get("qPeriod"))){
								pss.setInt(3, (Integer)param.get("qBeginYear"));
								pss.setInt(4, (Integer)param.get("qEndYear"));
								pss.setInt(5, (Integer)param.get("qBeginMonth"));
								pss.setInt(6, (Integer)param.get("qEndMonth"));
							} else{
								pss.setString(3,(String)param.get("qBegD"));							
								pss.setString(4,(String)param.get("qEndD"));
							}
														
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {
								
								Map row = new HashMap();
								row.put("BillDate", rs.getString("BillDate"));
								row.put("No", rs.getInt("No"));
								row.put("CredTypeID",rs.getString("CredTypeID"));
								row.put("Period",rs.getString("Period"));
								row.put("RecordComment", rs.getString("RecordComment"));
								row.put("RefAcc",rs.getString("RefAcc"));
								row.put("DebitAmount", rs.getBigDecimal("DebitAmount"));
								row.put("LendAmount", rs.getBigDecimal("LendAmount"));
								row.put("handler", rs.getString("handler"));
								row.put("creator", rs.getString("creator"));
								row.put("id", rs.getString("id"));
								row.put("isAudited", rs.getString("isAuditing"));
								row.put("Audited", rs.getString("Audited"));
								row.put("isPosted", rs.getString("isPosted"));
								row.put("hasPosted", rs.getString("hasPosted"));
								row.put("workFlowNodeName", rs.getString("workFlowNodeName"));
								row.put("row",rs.getInt("row"));
								list.add(row);																
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCashDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
		
	//***添加银行存款日记账	
	public Result addBankDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" insert into tblCashierAccount(BillDate,No,CredType,CredNo,PeriodYear,PeriodMonth,AccCode,RefAcc,DebitAmount,LendAmount,EmployeeID,RecordComment,Currency,CreateBy,CreateTime,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");																												
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																					
							pss.setString(1, (String)param.get("BillDate"));
							pss.setInt(2, (Integer)param.get("No"));
							pss.setString(3, (String)param.get("accWord"));
							pss.setInt(4, (Integer)param.get("orderNo"));
							pss.setInt(5, (Integer)param.get("accYear"));
							pss.setInt(6, (Integer)param.get("accMonth"));
							pss.setString(7,(String)param.get("accCode"));
							pss.setString(8,(String)param.get("refCode"));
							pss.setDouble(9,(Double)param.get("debitAmt"));
							pss.setDouble(10,(Double)param.get("lendAmt"));
							pss.setString(11,(String)param.get("handler"));
							pss.setString(12,(String)param.get("remark"));
							pss.setString(13,(String)param.get("currency"));
							pss.setString(14,(String)param.get("createBy"));
							pss.setString(15,(String)param.get("createTime"));
							pss.setString(16,(String)param.get("id"));
							
							pss.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.addBankDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//****反审核日记账
	public Result reAuditDet(final String dets){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" update tblCashierAccount  set workFlowNodeName='notApprove',workFlowNode=0 where ? like '%'+id+'%' ");																												
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																												
							pss.setString(1, "'"+dets.replaceAll(";", "','")+"'");							
							
							pss.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.reAuditDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//****审核日记账
	public Result auditDet(final String dets){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//StringBuffer sql = new StringBuffer(" update tblCashierAccount  set workFlowNodeName='finish',workFlowNode=-1 where id in (?) ");																												
							StringBuffer sql = new StringBuffer(" update tblCashierAccount  set workFlowNodeName='finish',workFlowNode=-1 where ? like '%'+id+'%' ");
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																												
							pss.setString(1, "'"+dets.replaceAll(";", "','")+"'");							
							BaseEnv.log.debug("参数1:"+"'"+dets.replaceAll(";", "','")+"'");
							pss.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.auditDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***更新日记账
	public Result upCashDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" update tblCashierAccount  set BillDate=?,No=?,CredType=?,CredNo=?,PeriodYear=?,PeriodMonth=?,AccCode=?,RefAcc=?,DebitAmount=?,LendAmount=?,EmployeeID=?,RecordComment=?,Currency=?,CreateBy=?,CreateTime=? where id=? ");																												
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																					
							pss.setString(1, (String)param.get("BillDate"));
							pss.setInt(2, (Integer)param.get("No"));
							pss.setString(3, (String)param.get("accWord"));
							pss.setInt(4, (Integer)param.get("orderNo"));
							pss.setInt(5, (Integer)param.get("accYear"));
							pss.setInt(6, (Integer)param.get("accMonth"));
							pss.setString(7,(String)param.get("accCode"));
							pss.setString(8,(String)param.get("refCode"));
							pss.setDouble(9,(Double)param.get("debitAmt"));
							pss.setDouble(10,(Double)param.get("lendAmt"));
							pss.setString(11,(String)param.get("handler"));
							pss.setString(12,(String)param.get("remark"));
							pss.setString(13,(String)param.get("currency"));
							pss.setString(14,(String)param.get("createBy"));
							pss.setString(15,(String)param.get("createTime"));
							pss.setString(16,(String)param.get("id"));
							
							pss.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.upCashDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***添加现金科目日记账
	public Result addCashDet(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							StringBuffer sql = new StringBuffer(" insert into tblCashierAccount(BillDate,No,CredType,CredNo,PeriodYear,PeriodMonth,AccCode,RefAcc,DebitAmount,LendAmount,EmployeeID,RecordComment,Currency,CreateBy,CreateTime,id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");																												
							
							BaseEnv.log.debug(sql.toString());
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
																					
							pss.setString(1, (String)param.get("BillDate"));
							pss.setInt(2, (Integer)param.get("No"));
							pss.setString(3, (String)param.get("accWord"));
							pss.setInt(4, (Integer)param.get("orderNo"));
							pss.setInt(5, (Integer)param.get("accYear"));
							pss.setInt(6, (Integer)param.get("accMonth"));
							pss.setString(7,(String)param.get("accCode"));
							pss.setString(8,(String)param.get("refCode"));
							pss.setDouble(9,(Double)param.get("debitAmt"));
							pss.setDouble(10,(Double)param.get("lendAmt"));
							pss.setString(11,(String)param.get("handler"));
							pss.setString(12,(String)param.get("remark"));
							pss.setString(13,(String)param.get("currency"));
							pss.setString(14,(String)param.get("createBy"));
							pss.setString(15,(String)param.get("createTime"));
							pss.setString(16,(String)param.get("id"));
							
							pss.executeUpdate();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.addCashDet:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***引用现金科目***//		
	public Result quoteAccMain(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {																										
							StringBuffer sql = new StringBuffer(" {call proc_quoteAcc(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							
							BaseEnv.log.debug(sql.toString());							
							CallableStatement pss = conn.prepareCall(sql.toString()) ;
																					
							pss.setString(1, (String)param.get("accCode"));							
							pss.setInt(2, (Integer)param.get("periodYear"));							
							pss.setInt(3, (Integer)param.get("periodMonth"));							
							pss.setString(4, (String)param.get("way"));							
							pss.setString(5, (String)param.get("date"));							
							pss.setString(6, (String)param.get("model"));							
							pss.setString(7,(String)param.get("word"));							
							pss.setString(8,(String)param.get("creator"));
							pss.setInt(9,(Integer)param.get("begOrderNo"));
							pss.setInt(10,(Integer)param.get("endOrderNo"));
							pss.setString(11,(String)param.get("status"));
							pss.setString(12,(String)param.get("post"));							
							
							//************
							BaseEnv.log.debug("参数1："+param.get("accCode"));
							BaseEnv.log.debug("参数2："+String.valueOf(param.get("periodYear")));
							BaseEnv.log.debug("参数3："+String.valueOf(param.get("periodMonth")));
							BaseEnv.log.debug("参数4："+param.get("way"));
							BaseEnv.log.debug("参数5："+param.get("date"));
							BaseEnv.log.debug("参数6："+param.get("model"));
							BaseEnv.log.debug("参数7："+param.get("word"));
							BaseEnv.log.debug("参数8："+param.get("creator"));
							BaseEnv.log.debug("参数9："+String.valueOf(param.get("begOrderNo")));
							BaseEnv.log.debug("参数10："+String.valueOf(param.get("endOrderNo")));
							BaseEnv.log.debug("参数11："+param.get("status"));
							BaseEnv.log.debug("参数12："+param.get("post"));
							
							pss.registerOutParameter(13, Types.INTEGER);
							
							boolean ret = pss.execute();
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.quoteAccMain:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***获取银行科目信息***//
	public Result getBankAcc(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();
							sql.append(" select a.AccNumber,a.AccNumber+b.zh_CN as AccName from tblAccTypeInfo a join tblLanguage b on a.AccFullName = b.id where AccNumber like  '1002%' and isCatalog = 0 ");
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;																												
														
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {
								
								Map row = new HashMap();
								row.put("AccNumber", rs.getString("AccNumber"));
								row.put("AccName", rs.getString("AccName"));								
								
								list.add(row);															
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
							
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.getBankAcc:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//***获取银行科目信息***//
	public Result getCashAcc(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							StringBuffer sql = new StringBuffer();
							sql.append(" select a.AccNumber,a.AccNumber+b.zh_CN as AccName from tblAccTypeInfo a join tblLanguage b on a.AccFullName = b.id where AccNumber like  '1001%' and isCatalog = 0 ");
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;																												
														
							ResultSet rs = pss.executeQuery();
							List list = new ArrayList();
							int pos = 0;
							while (rs.next()) {
								
								Map row = new HashMap();
								row.put("AccNumber", rs.getString("AccNumber"));
								row.put("AccName", rs.getString("AccName"));								
								
								list.add(row);															
						    }
							
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;	
							result.setRetVal(list);
							
							
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.getCashAcc:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//*****获取出纳权限设定******//
	public Result queryCashierSetting(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							StringBuffer sql = new StringBuffer();
							sql.append("select top 1 * from tblCashierSetting");
							PreparedStatement st = conn.prepareStatement(sql.toString());												
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							Map m = new HashMap();
							while(rs.next()){								
								
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}else if("numeric".equals(item.get("type"))){										
										BigDecimal amt = rs.getBigDecimal((String)item.get("name"));	
										amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
										m.put(item.get("name"), amt);
									}
								}
								break;
							}							
								result.retVal = m;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryCashierSetting:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;		
	}
	
	
	//*****获取员工信息******//
	public Result queryEmployees(final String emps){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {							
							String es = emps.replaceAll(",", "','");
							StringBuffer sql = new StringBuffer();
							sql.append("select id,EmpFullName from tblEmployee where id in('"+es+"')");
							PreparedStatement st = conn.prepareStatement(sql.toString());												
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}
							
							while(rs.next()){								
								Map m = new HashMap();
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}else if("numeric".equals(item.get("type"))){										
										BigDecimal amt = rs.getBigDecimal((String)item.get("name"));	
										amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
										m.put(item.get("name"), amt);
									}
								}
								data.add(m);
							}							
								result.retVal = data;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryEmployees:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;		
	}
	
	/***更新出纳设置***/
	public Result updateCashierSetting(final Map m){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer(" update tblCashierSetting set AuditingPersont =?,ReverseAuditing=? ,BinderPersont =?,ReverseBinder=? ");
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							
							pss.setString(1,(String)m.get("auditors"));						
							pss.setString(2,(String)m.get("reAuditors"));						
							pss.setString(3,(String)m.get("binder"));
							pss.setString(4,(String)m.get("reverseBinder"));
							pss.executeUpdate();
																					
							result.retCode = ErrorCanst.DEFAULT_SUCCESS;												
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.updateCashierSetting:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	//*****获取前面累计统计金额
	public Result queryPreAccum(final Map param){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {														
							StringBuffer sql = new StringBuffer();
							sql.append("select isnull(sum(DebitAmount),0) as preDebitAmt, isnull(sum(LendAmount),0) as preLendAmt from tblCashierAccount where PeriodYear = ? and PeriodMonth <? and AccCode = ?");
							PreparedStatement st = conn.prepareStatement(sql.toString());												
							st.setInt(1,(Integer)param.get("periodY"));
							st.setInt(2,(Integer)param.get("periodM"));
							st.setString(3,(String)param.get("accCode"));
							
							ResultSet rs = st.executeQuery();
							int count = rs.getMetaData().getColumnCount();
							List<Map> cols = new ArrayList();
							List<Map> data = new ArrayList();
							for(int i = 1 ;i<=count;i++){
								Map param = new HashMap();
								param.put("name", rs.getMetaData().getColumnName(i));
								param.put("type", rs.getMetaData().getColumnTypeName(i));
								cols.add(param);
							}							
							Map m = new HashMap();
							while(rs.next()){								
								for(Map item : cols){
									if("int".equals(item.get("type"))){
										m.put(item.get("name"), rs.getInt((String)item.get("name")));
									}else if("varchar".equals(item.get("type"))){
										m.put(item.get("name"), rs.getString((String)item.get("name")));
									}else if("numeric".equals(item.get("type"))){										
										BigDecimal amt = rs.getBigDecimal((String)item.get("name"));	
										amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
										m.put(item.get("name"), amt);
									}
								}
								break;
							}							
								result.retVal = m;
								result.retCode = ErrorCanst.DEFAULT_SUCCESS;													
						} catch (Exception ex) {						
							BaseEnv.log.error("CashierMgt.queryPreAccum:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;		
	}

}
