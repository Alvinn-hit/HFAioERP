package test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class TestFile {
	public void one(){
		two();
	}
	public void two(){
		
		
		int i;   
	    StackTraceElement stack[] = (new Throwable()).getStackTrace();   
	    for (i=0; i < stack.length; i++) {   
	      StackTraceElement ste=stack[i];   
	      System.out.println(ste.getClassName()+"."+ste.getMethodName()+"(...)");   
	      System.out.println(i+"--"+ste.getMethodName());   
	      System.out.println(i+"--"+ste.getFileName());   
	      System.out.println(i+"--"+ste.getLineNumber());   
	    }   
	}
	public static void main(String[] args){
		
		String aa[]="a,b,c,,".split(",");
		System.out.println(aa+""); 
		
//		TestFile t = new TestFile();
//		t.one();
		try {
			String path = "C:/Windows/0_0.txt";
			FileOutputStream fis = new FileOutputStream(new File(path));
	        FileChannel fc = fis.getChannel();
	        FileLock lock = fc.tryLock();
	        if(lock == null){
	            System.out.println("�ѱ���");
	        }else{
	            System.out.println("��ȡ�Դ�ͨ�����ļ��Ķ�ռ������");
	            fis.write("dddddddddddd".getBytes());
	            fis.flush();
	            try {
	                Thread.sleep(40000000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}
