package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class HfData {
	public static void main(String[] args){
		File path = new File("c:/ÖÐ×ª/Ïä");

		File[] fs = path.listFiles();
		for(File f:fs){
			try {
				BufferedReader bf= new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String line="";
				while((line=bf.readLine()) != null){
					if(line.indexOf("e`")>-1)
					{
					System.out.println("insert into hf2015(seq) values('"+line.substring(2)+"')");
					}
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
