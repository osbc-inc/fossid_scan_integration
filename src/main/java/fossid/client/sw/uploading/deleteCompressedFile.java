package fossid.client.sw.uploading;

import java.io.File;

import fossid.client.sw.values.projectValues;

public class deleteCompressedFile {
	projectValues pvalues = new projectValues();	
	
	public void validationFile() {
	
		String deleteFile = pvalues.getCompressedFile();
		File file = new File(deleteFile); 
		
		if( file.exists() ) {			
			if(file.delete()) {				
				System.out.println("The comparessed file is exist and has been deleted");				
				System.out.println("- Comparessed file: " + deleteFile);
				System.out.println();
			} else {
				System.out.println("FAILED: Deleting the compressed file has been failed");
				System.out.println("- Comparessed file: " + deleteFile);
				System.out.println();
				pvalues.setSuccess(0);
				System.exit(1);
			} 			
		}		
	}
	
	public void deletecomparessedfile() {				
		
		String deleteFile = pvalues.getCompressedFile();
		File file = new File(deleteFile); 
		
		if( file.exists() ) {
			
			if(file.delete()) {
				System.out.println();
				System.out.println("The compressed file has been deleted");				
				System.out.println("- Comparessed file: " + deleteFile);
			} else { 			
				System.out.println();
				System.out.println("FAILED: Deleting the compressed file has been failed");
				System.out.println("- Comparessed file: " + deleteFile);
				pvalues.setSuccess(0);
				System.exit(1);
			} 
			
		} else {	
			System.out.println();
			System.out.println("ERROR: The comparessed file is not exist"); 
			System.out.println("- Comparessed file: " + deleteFile);
			pvalues.setSuccess(0);
			System.exit(1);
		}		
	}
	
}
