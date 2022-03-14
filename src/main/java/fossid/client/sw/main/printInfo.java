package fossid.client.sw.main;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;

public class printInfo {
	
	static void usage() {		   
		System.out.println("Usage:");
		System.out.println("$ java -jar class [args....]");
		System.out.println();
		System.out.println("e.g:");
		System.out.println("$ java -jar fossid_scan_integration.jar --protocol http --address fossid.co.kr/webapp --username username --apikey a22d2s2s23 "
				+ "--projectname testProject --projectcode 0000 --scanname testScan --scancode 0000 --targetpath /path/to/scan --dependencyscanrun 0 "
				+ "--gitrepourl https://github.com/twbs/bootstrap.git --gitbranch master --sourcepath /fossid/uploads/files/scans "
				+ "--ignorevalue licenses,lib --ignoretype directory,directory --interval 30 --filepath /path/to/scan --filename filename.zip --decompresstime 30 "
				+ "--excludepath /exclude/path1/*,/exclude/path2/*,*.txt");
		System.out.println();
		System.out.println();
		System.out.println("Arguments");
		System.out.println();
		System.out.println("Server Information");
		System.out.println("--protocol: (Optional) FossID web interface protocol");
		System.out.println("            (default: http)");
		System.out.println("--address: FossID address");
		System.out.println("--username: username");
		System.out.println("--apikey: apikey");
		System.out.println();
		System.out.println("Project/Scan Information");
		System.out.println("--projectname: Project Name");
		System.out.println("(Optional) --projectcode: Project Code");
		System.out.println("--scanname: Scan Name");
		System.out.println("(Optional) --scancode: Scan Code");
		System.out.println();
		System.out.println("Option (Optional)");
		System.out.println("--targetpath: Full path including source code to be analyzed in FossID server");
		System.out.println("  + Need to change 'webapp_scan_path_enable=1' to use this option in /fossid/etc/fossid.conf file");
		System.out.println("  (NOTE: Please, do not use `--targetpath` with `Git Config` and `Upload Target File`)");		
		System.out.println("--dependencyscanrun: Set 0 or 1 to trigger dependency scan after source code scan");
		System.out.println("                     (default: 0)");
		System.out.println("--ignorevalue: Set ignore values. This value is separated by commas and the order of this values is matched with the order of ignoretype value");
		System.out.println("--ignoretype: Set ignore types. This value is separated by commas and the order of this values is matched with the order of ignorevalue value");		
		System.out.println("--interval: Set the seconds for intervals displaying the scan log");
		System.out.println("            (default: 10 seconds)");		
		System.out.println();
		System.out.println("Git Config (Optional)");
		System.out.println("(NOTE: Please, do not use `Git Config` with `--targetpath` and `Upload Target File`)");
		System.out.println("(NOTE: Applying `--gitrepourl` and `--gitbranch` can be applied when creating a new scan)");
		System.out.println("--gitrepourl: Set git repo url");
		System.out.println("--gitbranch: Set git repo branch");
		System.out.println("--gitrepourlchange: Set '1' if git repo url is changed");
		System.out.println("                    (0 default,1)");
		System.out.println("--gitbranchchange: Set '1' if git repo branch is changed");
		System.out.println("                   (0 default,1)");
		System.out.println();
		System.out.println("Upload Local Target File (Optional)");
		System.out.println("(NOTE: Please, do not use `Upload Target File` with `--targetpath` and `Git Config`)");
		System.out.println("--filepath: full file path with file name to be analyzed. The souce code must be archived before starting this tool");
		System.out.println("--filename: compressed file name");
		System.out.println("--decompresstime: Set the seconds to decompress the compressed file");
		System.out.println("			      (default: 30 seconds)");
		System.out.println();
		System.out.println("Scan Option (Optional)");		
		System.out.println("--limit: Limit on number of FOSSID results");
		System.out.println("         (Default/Recommended: 10)");
		System.out.println("--sensitivity: Sensitivity of the scan");
		System.out.println("               (Default/Recommended: 10. A value lower than 6 will return full file matches only)");
		System.out.println("--replaceid: Replace existing identifications");
		System.out.println("             (0 default,1)");
		System.out.println("--reuseid: If exists, try to use an existing identification depending on parameter 'identification_reuse_type'");
		System.out.println("           (0 default,1)");
		System.out.println("--idreusetype: Last identification found will be used for files with the same hash ");
		System.out.println("               (any, only_me, specific_project, specific_scan)");
		System.out.println("--specificcode: Only when 'identification_reuse_type' is 'specific_project' or 'specific_scan'");
		System.out.println("--autoiddetectdeclare: Automatically detect license declaration inside files");
		System.out.println("                       (0 default,1)");
		System.out.println("--autoiddetectcopyright: Automatically detect copyright statements inside files");
		System.out.println("                         (0 default,1)");
		System.out.println("--autoidresolvependingids: Automatically resolve pending identifications");
		System.out.println("                           (0 default,1)");	
		System.out.println("(not available)  --autoiddetectcomponent: Automatically detect components based on client results");
		System.out.println("                         (0 default,1)");
		System.out.println("--scanfailedonly: If true, this will only scan files that have failed in the previous scan");
		System.out.println("                  (0 default,1)");
		System.out.println("--deltaonly: Only newly added files or modified files will be scanned");
		System.out.println("             (0,1 default)");
		System.out.println("(not available)  --fullfileonly:	Get only full file matches as result");
		System.out.println("                (0,1 default)");		
	}
	
	public static void startFOSSID() {		
		System.out.println();
		System.out.println("******                                 *****    ****");
		System.out.println("*                                        *      *   *");
		System.out.println("*                                        *      *    *");
		System.out.println("*                                        *      *     *");
		System.out.println("******    ****     *****     *****       *      *     *");
		System.out.println("*        *    *   *         *            *      *     *");
		System.out.println("*        *    *    *****     *****       *      *    *");
		System.out.println("*        *    *         *         *      *      *   *");		
		System.out.println("*         ****     *****     *****     *****    ****");
		System.out.println();
	}
	
	public static void printinfo() {
		loginValues lvalues = new loginValues();
		projectValues pvalues = new projectValues();
		
		System.out.println();
		System.out.println("Server URL: " + lvalues.getServerApiUri());
		System.out.println("UserName: " + lvalues.getUsername());
		System.out.println("ApiKey: " + "*******");
		System.out.println("Project Name/Code: " + pvalues.getProjectName() + " / " + pvalues.getProjectCode());
		System.out.println("Scan Name/Code/Id: " + pvalues.getScanName() + " / " + pvalues.getScanCode() + " / " + pvalues.getScanId());		
		System.out.println();
	}

}