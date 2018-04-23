package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;


public class SqlServerProc {
	private String pass="koron.aio";
	private String host="localhost";
	private String DatabaseName="aio_db";
	
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
			
		SqlServerProc  conn = new SqlServerProc();		
		
		try{
			Connection c=conn.getConnection();
			
			CallableStatement cs = c.prepareCall("{call report_test(?,?,?,?)}");
			
			
			cs.setString(1, "aa");
			cs.setString(2, "con");
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
                        
            ResultSet rs = cs.executeQuery();
            while(rs.next()) {
               System.out.println(""+rs.getString(1)+":"+rs.getString(2)+":"+rs.getString(3)+":"+rs.getString(4)+":");
            }
            System.out.println("retCode"+cs.getInt(3));
            System.out.println("retVal"+cs.getString(4));
            
            SQLWarning warn = cs.getWarnings();
            while(warn != null){            	
            	System.out.println("warn "+ warn.getMessage());
            	warn = warn.getNextWarning();
            }            
            rs.close();
            cs.close();
            c.close();
            
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
