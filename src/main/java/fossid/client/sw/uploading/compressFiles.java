package fossid.client.sw.uploading;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import fossid.client.sw.values.projectValues;

public class compressFiles {	
	
	public void compressfiles(String excludePath) throws IOException {		
		projectValues filevalues = new projectValues();
		deleteCompressedFile dvalue = new deleteCompressedFile(); 
		
		try {
			
			List<String> exclude = Arrays.asList(excludePath.split(","));
			
			String command = "zip -r " + filevalues.getCompressedFile() + " " + filevalues.getFilePath();
			
			if(0 < excludePath.length()) {
				for(int i = 0; i < exclude.size(); i++) {
					//this is to exclude path
					command = command + " -x " + exclude.get(i);
				}
			}
			
			System.out.println("zip command: " + command);
			
			shellCmd(command);	   
			 
			 //set FileSize
			 File oFile = new File(filevalues.getCompressedFile());			 
			 
			 if (oFile.exists()) {		     
				  filevalues.setFilesize(oFile.length());
				  double mega = (filevalues.getFilesize()/1024)/1024;
				  double giga = ((filevalues.getFilesize()/1024)/1024)/1024;
				  
			 	  System.out.println();
			 	  if(mega > 1024) {
			 		 System.out.println("Compressed File Size: " + filevalues.getFilesize()/1024 + " KiB / " + mega + " MiB / "
	    		   				+ giga + " GiB");
			 	  } else if (mega < 1024) {
			 		 System.out.println("Compressed File Size: " + filevalues.getFilesize()/1024 + " KiB / " + mega + " MiB");
			 	  }
			 	  
			 }  else {
				System.err.println("Please, check compressed file path and name");
				filevalues.setSuccess(0);
				System.exit(1);
			 }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filevalues.setSuccess(0);
			dvalue.deletecomparessedfile();
			System.exit(1);
		}
	}
	
	public static void shellCmd(String command) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(command);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        
        while((line = br.readLine()) != null) {
                       System.out.println(line);
        }
	}

	
	/**
	 * refer to https://javacpro.tistory.com/21
	 */
	/**
	public void compressfiles() throws IOException {
		projectValues pvalues = new projectValues();	
		
		String ZIP_PATH = pvalues.getFilePath();
		String ZIP_NM = pvalues.getFileName();
		
		String end = ZIP_PATH.substring(ZIP_PATH.length() - 1, ZIP_PATH.length());
		if(end.equals("/") || end.equals("\\")) {
			ZIP_PATH = ZIP_PATH.substring(0, ZIP_PATH.length() - 1);
		} 
		
		File dir = new File(ZIP_PATH); 
		File file = null; 
		String files[] = null; 
		
		System.out.println("Compressing the path has been started");
		System.out.println("Compress Path: " + ZIP_PATH);
		System.out.print("Under Processing");
		
		if(dir.isDirectory() ){			
			files = dir.list();			
		} else {				
			files = new String[1]; 
			files[0] = dir.getName();			
		}
		
		ZipArchiveOutputStream zos = null; 
		
		try {
			zos = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(ZIP_PATH+"/"+ZIP_NM))); 
			
			for( int i=0; i < files.length; i++ ){
				file = new File(ZIP_PATH+"/"+files[i]); 
				zip("",file,zos, ZIP_PATH);
				System.out.print(".");
			} 			
			zos.close();
		} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
			} finally { 
				if( zos != null ){ zos.close(); 
				} 
		}
		
		System.out.println();
		System.out.println("Compressing the path has been finished");		
		System.out.println();
	} 

    public static void zip(String parent, File file, ZipArchiveOutputStream zos, String ZIP_PATH) throws IOException {

    	osValidator validator = new osValidator();
	    FileInputStream fis = null; 
    	BufferedInputStream bis = null;
	
    	int size = 1024; 
    	byte[] buf = new byte[size];
		    	
	    if( !file.exists() ){ 
	    	System.out.println("FAILED: " + file.getName()+" does not exist"); 
	    } 

	    if( file.isDirectory() ){	    		    	
		    	
	    	String dirName = file.getPath().replace(ZIP_PATH, "");
	    	String parentName = "";
	    	if(validator.isWindows()) {
	    		parentName = dirName.substring(1)+"\\";
	    		
	    		dirName = dirName.substring(1,dirName.length() - file.getName().length()); 
		    	ZipArchiveEntry entry = new ZipArchiveEntry(dirName+file.getName()+"\\"); 
		    	zos.putArchiveEntry(entry); String[] files = file.list();
			    	
		    	for( int i=0; i<files.length; i++ ) { 
		    		zip(parentName,new File(file.getPath()+"\\"+files[i]),zos,ZIP_PATH); 
		    	} 
		    	
	    	} else if(validator.isMac() || validator.isSolaris() || validator.isUnix()) {
	    		parentName = dirName.substring(1)+"/";
	    		
	    		dirName = dirName.substring(1,dirName.length() - file.getName().length()); 
		    	ZipArchiveEntry entry = new ZipArchiveEntry(dirName+file.getName()+"/"); 
		    	zos.putArchiveEntry(entry); String[] files = file.list();
			    	
		    	for( int i=0; i<files.length; i++ ) { 
		    		zip(parentName,new File(file.getPath()+"/"+files[i]),zos,ZIP_PATH); 
		    	} 
	    	}
		    	
    	} else { 

    	zos.setEncoding("UTF-8"); 
	    	 
    	fis = new FileInputStream(file); 
    	bis = new BufferedInputStream(fis,size);	    	

    	ZipArchiveEntry entry = new ZipArchiveEntry(parent+file.getName()); 
    	zos.putArchiveEntry(entry); 

    	int len;	    	
    	while((len = bis.read(buf,0,size)) != -1) { 
    		zos.write(buf,0,len); 
    	}
	    	
    	bis.close();
     	fis.close(); 
	   	zos.closeArchiveEntry(); 
	    	
	   	}
	}	 
	**/ 	
}
