package report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.menyi.web.util.BaseEnv;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class GenExcel {
	
	public boolean genExcel(String FilePath,List<HashMap> data){
		String Fields = getFields(data.get(0));
		return genExcel(FilePath,data,Fields);
	}
	
	public boolean genExcel(String FilePath,List<HashMap> data,String Fields){		
		OutputStream os = null;
		WritableWorkbook wwb = null;
		try{			
			String[] FieldsList = Fields.split(",");
			os = new FileOutputStream(new File(FilePath));			
			wwb = Workbook.createWorkbook(os);
			//创建Excel工作表 指定表名称和位置
			WritableSheet ws = wwb.createSheet("Data", 0);			
			//***插入Excel数据***//
            for(int i = 0;i<data.size();i++){
            	for(int j=0;j<FieldsList.length;j++){
            		Label label = null;
            		if(data.get(i).get(FieldsList[j])!=null){
            			label = new Label(j,i,data.get(i).get(FieldsList[j])+"");
            		} else{
            			continue;
            		}            		 
            		ws.addCell(label);
            	}            		
            }
            wwb.write();
            wwb.close();
            os.close();
            return true;
		} catch(Exception e){
			BaseEnv.log.error("genExcel：excel生成失败：",e);
			return false;
		} finally{
			try{
				 wwb.close();
		         os.close();
			} catch(Exception e){
				
			}
		}
	}
	
	public String getFields(HashMap mp){
		String fields = "";
		if(mp==null){
			return fields;
		}
		Iterator it = mp.entrySet().iterator();
		while(it.hasNext()){ 
			Entry entry = (java.util.Map.Entry)it.next(); 
			fields += ("".equals(fields)?"":",")+entry.getKey();
		}
		return fields;
	}
}
