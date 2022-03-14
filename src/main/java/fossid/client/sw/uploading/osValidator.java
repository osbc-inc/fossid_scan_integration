package fossid.client.sw.uploading;

import fossid.client.sw.values.projectValues;

public class osValidator {
	
	  /**
	   * refer to http://blog.devez.net/214
	   */	
	  private static String OS = System.getProperty("os.name").toLowerCase();

      public static boolean isWindows() {
  	        return (OS.indexOf("win") >= 0);
	  }
	  
	  public static boolean isMac() {	  
	        return (OS.indexOf("mac") >= 0);	  
	  }
	  
	  public static boolean isUnix() {
	         return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	  }
	  
	  public static boolean isSolaris() {	  
	        return (OS.indexOf("sunos") >= 0);	  
	  }
	    
	  public static void setFileValues(String filePath, String fileName) {
		  projectValues filevalues = new projectValues();
		  String compressedFile = "";
		  
		  filevalues.setFilePath(filePath);
		  filevalues.setFileName(fileName);		  
		  
		  String end = filePath.substring(filePath.length() - 1, filePath.length());
		  
		  if(end.equals("/") || end.equals("\\")) {
			  		compressedFile = filePath;
		  } else {
				if(isWindows()) {
					compressedFile = filePath + "\\";
				} else if (isMac() || isSolaris() || isUnix()){					
					compressedFile = filePath + "/";
				}
		  }
		  
		  compressedFile = compressedFile + fileName;
		  
		  filevalues.setCompressedFile(compressedFile);
	  }
}
