package fossid.client.sw.uploading;

import java.io.File;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;

public class uploadFiles {
	
	static loginValues lvalues = loginValues.getInstance();
	static projectValues pvalues = projectValues.getInstance();
	static deleteCompressedFile dvalue = new deleteCompressedFile();
	static osValidator osValidation = new osValidator(); 
	
	public void uploadfiles(String count) {
		String compressedFile = pvalues.getCompressedFile();
		String filename = pvalues.getFileName();
		
		Encoder encoder = Base64.getEncoder();
		
		byte[] scancodeByte = pvalues.getScanCode().getBytes();
		byte[] scanCode = encoder.encode(scancodeByte);
		
		byte[] filenameByte = filename.getBytes();
		byte[] fileName = encoder.encode(filenameByte);
		
		int giga = (int) (((pvalues.getFilesize()/1024)/1024)/1024);
		
		uploadingFile(count);
	
	}
	
	public static void uploadingFile(String count) {
		String compressedFile = pvalues.getCompressedFile();
		String filename = pvalues.getFileName();
		
		Encoder encoder = Base64.getEncoder();
		
		byte[] scancodeByte = pvalues.getScanCode().getBytes();
		byte[] scanCode = encoder.encode(scancodeByte);
		
		byte[] filenameByte = filename.getBytes();
		byte[] fileName = encoder.encode(filenameByte);
		
		JSONObject dataObject = new JSONObject();
		dataObject.put("username", lvalues.getUsername());
		dataObject.put("key", lvalues.getApikey());
				
		JSONObject rootObject = new JSONObject();
		rootObject.put("FOSSID_SCAN_CODE", new String(scanCode));
		rootObject.put("FOSSID_FILE_NAME", new String(fileName));
		rootObject.put("data", dataObject);
                
		try {			
			HttpPost httpPost = new HttpPost (lvalues.getServerUploadUri());
			httpPost.addHeader("content-type", "application/json");
			
			String userApi = lvalues.getUsername() + ":" + lvalues.getApikey();
			String basicAuth = "Basic " + new String(encoder.encode((userApi.getBytes())));
			httpPost.addHeader("Authorization", basicAuth);						
			httpPost.addHeader("FOSSID-SCAN-CODE", new String(scanCode));
			httpPost.addHeader("FOSSID-FILE-NAME", new String(fileName));
			
			File file = new File(compressedFile);
			FileEntity fileEn = new FileEntity(file, ContentType.DEFAULT_BINARY);		
			httpPost.setEntity(fileEn);			
			System.out.println();
			System.out.println("The file \"" + compressedFile + "\" is uploading to FOSSID server, /fossid/uploads/files/scans/" + pvalues.getScanId());
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpClientResponse = httpClient.execute(httpPost);			
			
			if (httpClientResponse.getStatusLine().getStatusCode() != 200) {								
				pvalues.setSuccess(0);
				System.out.println();
				System.out.println("FAILED: HTTP Error code: " + httpClientResponse.getStatusLine().getStatusCode());
				System.out.println();
				System.exit(1);	
			}
			
			System.out.println("Uploading file is finished");		
			
			extractArchives(Integer.parseInt(count));
			
		} catch (Exception e) {			
			e.printStackTrace();
			pvalues.setSuccess(0);
			dvalue.deletecomparessedfile();
			System.exit(1);
		}
	}
	
	
	private static void extractArchives(int count) {
		// to map scan to project
		JSONObject dataObject = new JSONObject();
        dataObject.put("username", lvalues.getUsername());
        dataObject.put("key", lvalues.getApikey());
        dataObject.put("scan_code", pvalues.getScanCode());
        
		JSONObject rootObject = new JSONObject();
        rootObject.put("group", "scans");
        rootObject.put("action", "extract_archives");
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
				System.out.println("Failed : HTTP Error code : " + httpClientResponse.getStatusLine().getStatusCode());
				dvalue.deletecomparessedfile();
				System.exit(1);
			}
			
			System.out.println();
			System.out.print("Start extracting the file.");
			// wait for 10 seconds until extrating file is finished
			count = count * 1000;
			while(count != 0) {
				System.out.print(".");
				count = count - 1000;
				Thread.sleep(1000);
			}
			System.out.println();
			System.out.println("The file is successfuly extrated");
			
		} catch (Exception e) {
			e.printStackTrace();
			pvalues.setSuccess(0);
			dvalue.deletecomparessedfile();
			System.exit(1);
		}	
	}
}
