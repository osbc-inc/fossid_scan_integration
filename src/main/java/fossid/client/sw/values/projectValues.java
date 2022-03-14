package fossid.client.sw.values;

import java.util.ArrayList;

public class projectValues {	
	
		private static projectValues values = new projectValues();

		public projectValues() {
		}

		public static projectValues getInstance() {
			return values;
		}

		private static String projectcode;	
		private static String projectname;
		private static String scancode;
		private static String scanname;
		private static String scanid;
		private static String filePath;
		private static String fileName;
		private static String compressedFile;
		private static String gitUrl;
		private static String gitBranch;
		private static long fileSize;
		private static int Success = 1;
		private static ArrayList<String> scanOption = new ArrayList<String>();
		
		public String getGitUrl() {
			return gitUrl;
		}
		public void setGitUrl(String gitUrl) {
			this.gitUrl = gitUrl;
		}
		
		public String getGitBranch() {
			return gitBranch;
		}
		public void setGitBranch(String gitBranch) {
			this.gitBranch = gitBranch;
		}
		
		public String getProjectCode() {
			return projectcode;
		}
		public void setProjectCode(String projectcode) {
			this.projectcode = projectcode;
		}

		public String getProjectName() {
			return projectname;
		}
		public void setProjectName(String projectname) {
			this.projectname = projectname;
		}
		
		public String getScanCode() {
			return scancode;
		}
		public void setScanCode(String scancode) {
			this.scancode = scancode;
		}

		public void setScanName(String scanname) {
			this.scanname = scanname;
		}		
		public String getScanName() {
			return scanname;
		}
		
		public void setScanId(String scannid) {
			this.scanid = scannid;
		}		
		public String getScanId() {
			return scanid;
		}
		
		public void setFilePath(String filePath) {
			this.filePath = filePath;			
		}
		public String getFilePath() {
			return filePath;
		}
		
		public void setFileName(String fileName) {
			this.fileName = fileName;			
		}
		public String getFileName() {
			return fileName;
		}
		
		public void setCompressedFile(String compressedFile) {
			this.compressedFile = compressedFile;			
		}
		public String getCompressedFile() {
			return compressedFile;
		}
		
		public void setScanOption(String scanOption) {
			this.scanOption.add(scanOption);			
		}
		public ArrayList getScanOption() {
			return scanOption;
		}
		
		public void setFilesize(long fileSize) {
			this.fileSize = fileSize;
		}
		public long getFilesize() {
			return this.fileSize;
		}
		
		public void setSuccess(int success) {
			this.Success = success;
		}
		public int getSuccess(){
			return this.Success;
		}
}
