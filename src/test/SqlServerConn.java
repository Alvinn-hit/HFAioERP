package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;



public class SqlServerConn {
	private String pass="";
	private String host="localhost";
	private String DatabaseName="master";
	
	public  Connection getConnection()throws Exception{
        Connection conn=null;
        
        String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url="jdbc:sqlserver://"+host+";SelectMethod=Cursor;DatabaseName="+DatabaseName;
        String userName="sa";

        Class.forName(driver);
        conn=DriverManager.getConnection(url,userName,pass);

        Statement stmt =conn.createStatement();
        stmt.executeQuery("select getdate()");        
        return conn;
    }

	public static void main(String[] args){
		if(args==null || args.length<1){
			System.out.println("请输入数据库的地址和密码");
		}
		if(args[0]== null || args[0].length() ==0){
			System.out.println("请输入数据库的地址");
		}
		
				
		SqlServerConn  conn = new SqlServerConn();
		
		conn.host = args[0];
		
		if(args.length > 1){
			conn.DatabaseName = args[1];
			System.out.println("===================="+conn.DatabaseName);
		}
		if(args.length > 2){
			conn.pass = args[2];
		}
		
		ArrayList list = new ArrayList();
		for(int i=0;i<200;i++){
			try{
				Connection c=conn.getConnection();
				list.add(c);
				System.out.println("已建立连接数："+list.size());
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("建立第"+(list.size()+1)+"个连接失败");
			}			
		}
		try{
			System.in.read();
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}
}
