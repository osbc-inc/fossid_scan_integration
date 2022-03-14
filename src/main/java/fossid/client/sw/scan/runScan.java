package fossid.client.sw.scan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;

public class runScan {
	
	loginValues lvalues = loginValues.getInstance();
	projectValues pvalues = projectValues.getInstance(); 

	public void runscan(String interval, String limit, String sensitivity, String replaceid, String reuseid, String idreusetype, String specificcode, 
			String autoiddetectdeclare, String autoiddetectcopyright, String autoidresolvependingids, String  scanfailedonly, String deltaonly, String fullfileonly) {

		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
        
        if(!limit.equals("")){        	 		
        	dataObject.put("limit", Integer.parseInt(limit));
            pvalues.setScanOption("limit: " + limit);
        	
        }
        
        if(!sensitivity.equals("")){
        	dataObject.put("sensitivity", Integer.parseInt(sensitivity));
            pvalues.setScanOption("sensitivity: " + sensitivity);        	
        }
        
        if(!replaceid.equals("")){
        	if(replaceid.equals("1")) {        		
        		dataObject.put("sensitivity", replaceid);
            	pvalues.setScanOption("sensitivity: " + "true");
        	} else if(replaceid.equals("0")) {        		
        		dataObject.put("replace_existing_identifications", replaceid);
            	pvalues.setScanOption("replace_existing_identifications: " + "false");
        	}
        }
        
        if(!reuseid.equals("")){
        	if(reuseid.equals("1")) {        		
        		dataObject.put("reuse_identification", reuseid);
            	pvalues.setScanOption("reuse_identification: " + "true");
        	} else if(reuseid.equals("0")) {        		
        		dataObject.put("reuse_identification", reuseid);
            	pvalues.setScanOption("reuse_identification: " + "false");
        	}
        }        
        
        if(!idreusetype.equals("")){        	
        	dataObject.put("identification_reuse_type", idreusetype);
        	pvalues.setScanOption("identification_reuse_type: " + idreusetype);
        }
        
        if(!specificcode.equals("")){
        	dataObject.put("specific_code", specificcode);
        	pvalues.setScanOption("specific_code: " + specificcode);
        }
        
        if(!autoiddetectdeclare.equals("")){
        	if(autoiddetectdeclare.equals("1")) {        		
        		dataObject.put("auto_identification_detect_declaration", autoiddetectdeclare);
            	pvalues.setScanOption("auto_identification_detect_declaration: " + "true");
        	} else if(autoiddetectcopyright.equals("0")) {        		
        		dataObject.put("auto_identification_detect_declaration", autoiddetectdeclare);
            	pvalues.setScanOption("auto_identification_detect_declaration: " + "false");
        	}
        }
        
        if(!autoiddetectcopyright.equals("")){
        	if(autoiddetectcopyright.equals("1")) {        		
        		dataObject.put("auto_identification_detect_copyright", autoiddetectcopyright);
            	pvalues.setScanOption("auto_identification_detect_copyright: " + "true");
        	} else if(autoiddetectcopyright.equals("0")) {        		
        		dataObject.put("auto_identification_detect_copyright", autoiddetectcopyright);
            	pvalues.setScanOption("auto_identification_detect_copyright: " + "false");
        	}        	
        }
        
         
        if(!autoidresolvependingids.equals("")){
        	if(autoidresolvependingids.equals("1")) {        		
        		dataObject.put("auto_identification_resolve_pending_ids", autoidresolvependingids);
            	pvalues.setScanOption("auto_identification_resolve_pending_ids: " + "true");
        	} else if(autoidresolvependingids.equals("0")) {        		
        		dataObject.put("auto_identification_resolve_pending_ids", autoidresolvependingids);
            	pvalues.setScanOption("auto_identification_resolve_pending_ids: " + "false");
        	}
        }       
        
        if(!scanfailedonly.equals("")){
        	if(scanfailedonly.equals("1")) {        		
        		dataObject.put("scan_failed_only", scanfailedonly);
            	pvalues.setScanOption("scan_failed_only: " + "true");
        	} else if(scanfailedonly.equals("0")) {        		
        		dataObject.put("scan_failed_only", scanfailedonly);
            	pvalues.setScanOption("scan_failed_only: " + "false");
        	}
        }
        
        if(!deltaonly.equals("")){
        	if(deltaonly.equals("1")) {        		
        		dataObject.put("delta_only", deltaonly);
            	pvalues.setScanOption("delta_only: " + "true");
        	} else if(deltaonly.equals("0")) {        		
        		dataObject.put("delta_only", deltaonly);
            	pvalues.setScanOption("delta_only: " + "false");
        	}
        }
        
        if(!fullfileonly.equals("")){
        	if(fullfileonly.equals("1")) {        		
        		dataObject.put("full_file_only", fullfileonly);
                pvalues.setScanOption("full_file_only: " + "true");
        	} else if(fullfileonly.equals("0")) {        		
        		dataObject.put("full_file_only", fullfileonly);
                pvalues.setScanOption("full_file_only: " + "false");
        	}
        }
                        
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "run");
		rootObject.put("data", dataObject);
		
		//System.out.println(rootObject.toString());
		
		if(pvalues.getScanOption().size() > 0) {
			System.out.println();
			System.out.println("Set below scan options:");				
			for(int i=0; pvalues.getScanOption().size() > i; i++) {				
				System.out.println("- " + pvalues.getScanOption().get(i));	
			}
		}		
				
		HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
		HttpClient httpClient = HttpClientBuilder.create().build();
						
		try {

			StringEntity entity = new StringEntity(rootObject.toString());
			httpPost.addHeader("content-type", "application/json");
			httpPost.setEntity(entity);
					
			HttpResponse httpClientResponse = httpClient.execute(httpPost);
				
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
				pvalues.setSuccess(0);
				System.out.println("Failed : HTTP Error code : " + httpClientResponse.getStatusLine().getStatusCode());
				System.exit(1);
			}		
			
			System.out.println();
			System.out.println("Scan has been launched!!");
			System.out.println();			
					
			checkScanStatus(interval);
			
			System.out.println();	
			System.out.println("Scan has been finished!!");
			System.out.println();			
			
		} catch (Exception e) {				
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}
	}
	
	private void checkScanStatus(String interval) {

		// to map scan to project
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
        dataObject.put("type", "SCAN");
        
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "check_status");
		rootObject.put("data", dataObject);		
						
		String finished = "false";
		//1000 = 1 second
		int intervals = Integer.parseInt(interval) * 1000;	   
		
		try {
			int i = 1;
			//int loopCount = 1;
			
			while(finished.equals("false")) {
				HttpPost httpPost = new HttpPost(lvalues.getServerApiUri());
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();		
					
				HttpResponse httpClientResponse = null;
			
				//int postCount = 1;			
					
				StringEntity entity = new StringEntity(rootObject.toString());
				httpPost.addHeader("content-type", "application/json");
				httpPost.setEntity(entity);
				httpClientResponse = httpClient.execute(httpPost);					
						
				if (httpClientResponse.getStatusLine().getStatusCode() != 200) {
					pvalues.setSuccess(0);
					System.out.println("Failed : HTTP Error code : " + httpClientResponse.getStatusLine().getStatusCode());
					System.exit(1);					
				}
					
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpClientResponse.getEntity().getContent(), "utf-8"));
				String result = br.readLine();				
					
				JSONParser jsonParser = new JSONParser();
			    JSONObject jsonObj1 = (JSONObject) jsonParser.parse(result.toString());            
			    JSONObject jsonObj2 = (JSONObject) jsonObj1.get("data");
			       		        
			    if(jsonObj2.get("is_finished").toString().equals("true")) {
			        finished = jsonObj2.get("is_finished").toString();		        	
			    }		       
			        
			    System.out.println(i + ". "  + "file: "+ jsonObj2.get("current_filename") + " / percentage: " + jsonObj2.get("percentage_done") + "%");	        
			    i++;
			        
			    Thread.sleep(intervals);	
			    
			    httpClient.close();
		    }
			
		} catch (Exception e) {			
			e.printStackTrace();
			pvalues.setSuccess(0);
			System.exit(1);
		}
	}
}
