package fossid.client.sw.setdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;;

public class setProjectInfo {
	loginValues lvalues = loginValues.getInstance();
	projectValues pvalues = projectValues.getInstance();
	
	public void setInfo(String projectName, String scanName, String projectCode, String scanCode, String gitUrl, String gitBranch) {
		
		projectValues pvalues = projectValues.getInstance();
		pvalues.setProjectName(projectName);
		pvalues.setScanName(scanName);		
		
		setProjectCode(projectCode);	
		checkScanCode(scanCode, gitUrl, gitBranch);
		
		
	}
	
	private void setProjectCode(String projectCode) {

		// create json to call FOSSID project/list_projects api 		
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
		
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "projects");
        rootObject.put("action", "list_projects");
		rootObject.put("data", dataObject);		
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();			
		
		String project_Code = "";
		
		if(projectCode.equals("")) {
        	project_Code = pvalues.getProjectName();
        } else {            	
        	project_Code = pvalues.getProjectName() + projectCode;
        }
		
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
			
			HttpResponse httpClientResponse = httpClient.execute(httpPost);
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {								
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}	
								
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
			String result = br.readLine();
			
			//System.out.println("list_projects" + result.toString());
			
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(result.toString());
            
            String checkStatus = "f";
            
            Object object = jsonObj.get("data");
            if(object instanceof JSONArray){    
                // JSONArray
            	
            	JSONArray dataArray = (JSONArray) jsonObj.get("data");
            	
                //set projectCode            
                for(int i=0; i < dataArray.size(); i++) {
                    JSONObject tempObj = (JSONObject) dataArray.get(i);
                         
                    if(tempObj.get("project_code").equals(project_Code)) {
                       	pvalues.setProjectCode(tempObj.get("project_code").toString());                	
                       	System.out.println("The projectName: \"" + pvalues.getProjectName() + "\" / projectCode: \"" + pvalues.getProjectCode() + "\" is exist");                	
                       	checkStatus = "t";
                    } 
                }
                
                // if there is no projectName in the list
                if(checkStatus.equals("f")) {            	
                	createProject(project_Code);            	
                }
            }else{
                // JSONObject            	
            	// if there no project assigned to the user
            	createProject(project_Code);
            	checkStatus = "t";
            }
            
          
            
		} catch (Exception e) {
			pvalues.setSuccess(0);
			System.out.println();
			System.out.println("FAILED: Please, check assigning project code");
			System.out.println();
			System.exit(1);
			e.printStackTrace();
		}		
	}
	
	// create Project if there is no projectName
	private void createProject(String projectCode) {
		
		JSONObject dataObject = new JSONObject();		
		dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("project_code", projectCode);
        dataObject.put("project_name", pvalues.getProjectName());

        JSONObject rootObject = new JSONObject();
        rootObject.put("group", "projects");
        rootObject.put("action", "create");
		rootObject.put("data", dataObject);			
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();		
		
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
			
			HttpResponse httpClientResponse = httpClient.execute(httpPost);			
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
				pvalues.setSuccess(0);				
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}					
			
            //set projectcode
            pvalues.setProjectCode(projectCode);
    		            
            System.out.println("Create New Project - ProjectName / ProjectCode: " + pvalues.getProjectName() + " / " + pvalues.getProjectCode());
			
		} catch (Exception e) {
			pvalues.setSuccess(0);
			System.out.println();
			System.out.println("ERROR: Please, check creating project");
			System.out.println();
			System.exit(1);
			e.printStackTrace();

		}
	}

	
	private void checkScanCode(String scanCode, String gitUrl, String gitBranch) {
		
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
		
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "list_scans");
		rootObject.put("data", dataObject);		
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		String scan_Code = "";
		
		 if(scanCode.equals("")) {
			 scan_Code = pvalues.getScanName();
         } else {            	
        	 scan_Code = pvalues.getScanName() + scanCode;
         }
				
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
			
			HttpResponse httpClientResponse = httpClient.execute(httpPost);			
			
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
			String result = br.readLine();
			
			//System.out.println("list_scans" + result.toString());
			
			JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());
			JSONObject jsonObj2 = (JSONObject) jsonObj1.get("data");  
            
            Iterator iter = jsonObj2.keySet().iterator();
            
            String checkStatus = "f";
            // set key value of jsonObj2 and run loop while(until) iter has value        
            while(iter.hasNext()) {
            	// set key value to key
            	String key = (String) iter.next();
            	// get values from key
            	JSONObject tempObj = (JSONObject) jsonObj2.get(key);            	
            	  
            	if(scan_Code.equals(tempObj.get("code").toString())) {
            		pvalues.setScanCode(tempObj.get("code").toString());
            		pvalues.setScanId(tempObj.get("id").toString());            		
            		System.out.println("The scanName: \"" + pvalues.getScanName() + "\" / scanCode: \"" + pvalues.getScanCode() + "\" is exist");            		
            		checkStatus = "t";            	
            	}
            }
            
            if(checkStatus.equals("f")) {
            	creatScan(scan_Code, gitUrl, gitBranch);
            } else if(!gitUrl.equals("") && checkStatus.equals("t")) {            	
            	System.out.println();
            	System.out.println("WARN: You applied to download source code from git. Downloading source code from git may not be triggered because the scan already exists");
            	System.out.println("WARN: Downloading source code from git will be triggered with a new scan");
            	System.out.println();
            }
            
		} catch (Exception e) {			
			pvalues.setSuccess(0);
			System.out.println();
			System.out.println("FAILED: Please, check assigning scan code");
			System.out.println();
			System.exit(1);
			e.printStackTrace();
		}	
	}
	
	
	private void creatScan(String scanCode, String gitUrl, String gitBranch) {
		
		JSONObject dataObject = new JSONObject();		
		dataObject.put("username", lvalues.getUsername());
	    dataObject.put("key", lvalues.getApikey());
	    dataObject.put("scan_code", scanCode);
	    dataObject.put("scan_name", pvalues.getScanName());
        if(!gitUrl.equals("")) {
        	dataObject.put("git_repo_url", gitUrl);
     	    dataObject.put("git_branch", gitBranch);
        }

	    JSONObject rootObject = new JSONObject();
	    rootObject.put("group", "scans");
	    rootObject.put("action", "create");
		rootObject.put("data", dataObject);			
		
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();		
		
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
			
			HttpResponse httpClientResponse = httpClient.execute(httpPost);
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}

			BufferedReader br = new BufferedReader(
					new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
			String result = br.readLine();		
			
			System.out.println("scancreate" + result.toString());
		
			JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());
	        JSONObject jsonObj2 = (JSONObject) jsonObj1.get("data");
			
			pvalues.setScanCode(scanCode);
			pvalues.setScanId(jsonObj2.get("scan_id").toString());
						
			System.out.println("Create New Scan - ScanName / ScanCode: " + pvalues.getScanName() + " / " + pvalues.getScanCode());
			
		} catch (Exception e) {
			pvalues.setSuccess(0);
			System.out.println();
			System.out.println("ERROR: Please, check creating scan");
			System.out.println();
			e.printStackTrace();
		}		
	}
	
}
