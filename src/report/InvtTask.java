package report;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.menyi.web.util.BaseEnv;

public class InvtTask implements ServletContextListener{
	
	//定时器
	private static Timer timer = new Timer();
	
	//时间间隔
	private static final long  PERIOD_DAY = 24*60*60*1000;
	
	//任务
	private static GenInvtTask task = new GenInvtTask();
	
	//执行时间
	private Date taskDate = null;
	
	public InvtTask(){		
		Calendar calendar = Calendar.getInstance();
		
		//定制每日2:00执行方法
		calendar.set(Calendar.HOUR_OF_DAY, 2);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		
		taskDate = calendar.getTime();//第一次任务执行时间
		
		//如果当前时间大于当前执行时间点，则往后延迟一天执行
		if(taskDate.before(new Date())){
			taskDate = this._addDay(taskDate,1);
		}		
	}
	
	 // 增加或减少天数
	 public Date _addDay(Date date, int num) {
	  Calendar startDT = Calendar.getInstance();
	  startDT.setTime(date);
	  startDT.add(Calendar.DAY_OF_MONTH, num);
	  return startDT.getTime();
	 }
	
	 public void contextInitialized(ServletContextEvent event) {			
		 //timer.schedule(task, taskDate, PERIOD_DAY);
	 }

	 public void contextDestroyed(ServletContextEvent event) {
		//timer.cancel(); 
	 }	
}
