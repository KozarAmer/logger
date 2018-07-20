import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

import com.fasterxml.jackson.annotation.JsonIgnore;

class Logger {
	// Default values for level and category.
	public static final int LVL_DEBUG = 1;
	public static final int LVL_INFO = 2;
	public static final int LVL_WARNING = 4;
	public static final int LVL_ERROR = 8;
	public static final int LVL_PANIC = 16;
	
	// Used to verify correct user input.
	private Integer[] ALLOWED_LEVELS = new Integer[] { LVL_DEBUG, LVL_INFO, LVL_WARNING, LVL_ERROR, LVL_PANIC };

	public static final int CAT_SECURITY = 1;
	public static final int CAT_PERFORMANCE = 2;
	public static final int CAT_BUSINESS = 4;
	public static final int CAT_AUDIT = 8;
	public static final int CAT_SQL = 16;
	public static final int CAT_TECHNICAL = 32;
	public static final int CAT_TRACKING = 64;
	
	private Integer[] ALLOWED_CATEGORIES = new Integer[] { CAT_SECURITY, CAT_PERFORMANCE, CAT_BUSINESS, CAT_AUDIT, CAT_SQL, CAT_TECHNICAL, CAT_TRACKING };

	@JsonIgnore // Do not log the URL. -- JACKSON
	private String url;
	private int level;
	private int category;
	private String message;
	private String[] context;
	private String env;
	private String hostname;
	private String namespace;
	private String origin;
	private String binary;
	private String user;
	@JsonIgnore // Do not log the key. -- JACKSON
	private String auth;
	
	// Constructor
	public Logger(String url, String auth) {
		this.url = url;
		this.auth = (auth != "")? auth : "logger";
	}

	// Generate JSON out of the objects.
	private String toJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
	
	/**
	 * This method is used to establish a connection and execute the required request.
	 * 
	 * @param String method
	 * @param String data
	 * @return String
	 */
	private String httpRequest(String method, String data) {
		String response;
		Builder buildRequest;
		String url = this.url + "/api/notifications";
		
		// Build the client as required.
		OkHttpClient client = new OkHttpClient().newBuilder()
				.connectTimeout(60, TimeUnit.SECONDS)
				.writeTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.build();
		
		// Start building the request -- only necessary parts.
		buildRequest = new Request.Builder()
				.addHeader("Authorization", this.auth);
		
		// Further build the request depending on what is needed.
		if (method == "get") {
			buildRequest.url(url + data);
		}else if (method == "post") {
			RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
			buildRequest.url(url);
			buildRequest.post(body);
		}else if (method == "delete") {
			buildRequest.url(url + data);
			buildRequest.delete();
		}else {
			return "Error: unknown method";
		}
		
		// Finish building request and executing call.
		Request request = buildRequest.build();
		Call call = client.newCall(request);
		try {
			response = call.execute().body().string();
		} catch (Throwable e) {
			return "Error: "+ e;
		}
		
		return response;
	}

	/**
	 * Sends a log to the API using post.
	 * 
	 * @param int level
	 * @param int category
	 * @param String message	
	 * @param String[] context	
	 * @param String env
	 * @param String hostname
	 * @param String namespace 
	 * @param String origin
	 * @param String binary	
	 * @param String user		
	 * @return String
	 * @throws Exception
	 */
	public String postLog(
			int level, int category, String message, String context[], String env, String hostname, String namespace, 
			String origin, String binary, String user) throws Exception {
		
		this.level = (Arrays.asList(ALLOWED_LEVELS).contains(level)) ? LVL_INFO : level;
		this.category = (Arrays.asList(ALLOWED_CATEGORIES).contains(category)) ? CAT_TECHNICAL : category;
		
		this.message = message;
		this.context = context;
		this.env = env;
		this.hostname = hostname;
		this.namespace = namespace;
		this.origin = origin;
		this.binary = binary;
		this.user = user;
		
		// Generate JSON out of the object.
		String json = toJson();

		return httpRequest("post", json);
	}
	
	/**
	 * Get one or multiple logs depending on given parameters.
	 * @param int perPage
	 * @param int page
	 * @return String
	 */
	public String getLogs(int perPage, int page) {
		return httpRequest("get", "?page="+ page +"&per_page="+ perPage);
	}
	
	/**
	 * Get a single Log specified by its id.
	 * 
	 * @param int id
	 * @return String
	 */
	public String getLogById(int id) {
		return httpRequest("get", "/"+ id);
	}
	
	/**
	 * Delete a single Log specified by its id.
	 * 
	 * @param int id
	 * @return String
	 */
	public String deleteLogById(int id) {
		return httpRequest("delete", "/"+ id);
	}

	// Set and Get:
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getContext() {
		return context;
	}

	public void setContext(String[] context) {
		this.context = context;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}