package fossid.client.sw.values;

public class loginValues {

	private static loginValues values = new loginValues();

	public loginValues() {
	}

	public static loginValues getInstance() {
		return values;
	}

	private String serveruri;
	private static String serverApiuri;
	private static String serverUploaduri;
	private static String username;
	private static String apikey;	
	
	public String getServerUri() {
		return serveruri;
	}

	public void setServerUri(String serveruri) {
		this.serveruri = serveruri;
	}

	public String getServerUploadUri() {
		return serverUploaduri;
	}

	public void setServerUploadUri(String serverUploaduri) {
		this.serverUploaduri = serverUploaduri;
	}
	
	public String getServerApiUri() {
		return serverApiuri;
	}

	public void setServerApiUri(String serverApiuri) {
		this.serverApiuri = serverApiuri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
}
