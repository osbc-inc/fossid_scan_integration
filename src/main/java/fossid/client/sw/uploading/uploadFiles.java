package fossid.client.sw.uploading;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Base64.Encoder;

import fossid.client.sw.values.loginValues;
import fossid.client.sw.values.projectValues;
import fossid.client.sw.uploading.osValidator;

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
		
		//if(osValidation.isWindows()) {
		if(osValidation.isWindows() || osValidation.isUnix() || osValidation.isMac() || osValidation.isSolaris()){
			
			uploadingFile(count);
			
		//} else if(osValidation.isUnix() || osValidation.isMac() || osValidation.isSolaris()) {
		} else if(osValidation.isSolaris()) {						
			try {
				if(giga == 1 || giga < 1) {
					System.out.println();
					System.out.println("The file \"" + compressedFile + "\" is uploading to FOSSID server, /fossid/uploads/files/scans/" + pvalues.getScanId());

					String command = "curl --user " + lvalues.getUsername() + ":" + lvalues.getApikey()
							+ " -H \"FOSSID-SCAN-CODE: " + scanCode + "\"" + " -H \"FOSSID-FILE-NAME: " + fileName + "\"" + " -X POST -T \"" + pvalues.getCompressedFile() + "\" "
									+ lvalues.getServerUploadUri();
					
					shellCmd(command);
					System.out.println("curl command: " + command);
					
					System.out.println("Uploading file is finished");		
					
					extractArchives(Integer.parseInt(count));
					
				} else if(giga > 1) {
					
					System.out.println();
					System.out.println("The file \"" + compressedFile + "\" is uploading to FOSSID server, /fossid/uploads/files/scans/" + pvalues.getScanId());
					
					//split zip file into 1GB
					String command = "split --bytes 1073741824  --filter='curl -vv --user " + lvalues.getUsername() + ":" + lvalues.getApikey() + 
							" -H \"Transfer-Encoding: chunked\" -H \"FOSSID-SCAN-CODE: " + scanCode + "\""
							+ " -H \"FOSSID-FILE-NAME: " + fileName + "\"" + " -X POST -T - " + lvalues.getServerUploadUri() + "' " + pvalues.getCompressedFile();
					
					shellCmd(command);
					System.out.println("curl command: " + command);
					
					System.out.println("Uploading file is finished");	
					
					extractArchives(Integer.parseInt(count));
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pvalues.setSuccess(0);
				dvalue.deletecomparessedfile();
				System.exit(1);
			}
		}
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
			
			/**
			InputStream zipFile = new FileInputStream(compressedFile);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", zipFile, ContentType.create("application/zip"), filename);
            HttpEntity httpentity = builder.build();            
            httpPost.setEntity(httpentity);
            **/
			
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
