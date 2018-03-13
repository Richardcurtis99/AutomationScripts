package com.adobe.adobeQueryAEDash;


	import sun.security.util.DerInputStream;
	import sun.security.util.DerValue;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
	import java.net.HttpURLConnection;
	import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
	import java.net.URLEncoder;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.security.GeneralSecurityException;
	import java.security.KeyFactory;
	import java.security.PrivateKey;
	import java.security.spec.PKCS8EncodedKeySpec;
	import java.security.spec.RSAPrivateCrtKeySpec;
	import java.util.Base64;
	import javax.crypto.spec.SecretKeySpec;
	import javax.xml.bind.DatatypeConverter;
	import java.security.Key;
	import io.jsonwebtoken.*;
	import io.jsonwebtoken.impl.DefaultClaims;
	import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
	import java.util.Map;
	import javax.crypto.Mac; 
	import javax.crypto.spec.SecretKeySpec;
	import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
	import org.json.JSONArray;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.PrintStream;
		
	
	public class CCuserAPI_report {
	
	public static void main (String[] args) throws GeneralSecurityException, IOException, URISyntaxException {	
		printoutput();
		System.out.println("Starting Groups / Users report");
		// API Key details go here
		String orgID = "{Org ID}";
		File privateKeyFile = new File("{Privatekey}");
		String xAPIKEY = "{xAPIKey}";
		// Load the private key file
		PrivateKey pk = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(privateKeyFile);
		// Generate an expiry date as a long
		JSONObject httpresultstring = postJWT(pk,orgID,xAPIKEY);
		// Run the group report, for each group get the users.
		groupreport(httpresultstring,orgID,xAPIKEY);
    }  
	


	private static void printoutput() throws FileNotFoundException{
	           //System.out.println("Print on console");

	           // Store console print stream.
	           PrintStream ps_console = System.out;

	           File file = new File("file.txt");
	           FileOutputStream fos = new FileOutputStream(file);

	           // Create new print stream for file.
	           PrintStream ps = new PrintStream(fos);

	           // Set file print stream.
	          // System.setOut(ps);
	           //System.out.println("Print in the file !!");

	           // Set console print stream.
	           System.setOut(ps_console);
	           //System.out.println("Console again !!");
	 }
	
	private static void parseAuth(String httpresultstring) {
	}
	
	private static void readJSONauth(String JSONstring) {
		// Decode the access token and use this for subsequent calls to the server
		JSONArray array = new JSONArray(JSONstring);
		System.out.println("JSON length "+ array.length());
		for (int i = 0; i < array.length(); i++) {			
		    JSONObject object = array.getJSONObject(i);
		    System.out.println("Expires in " + object.getLong("expires_in"));
		    System.out.println("token_type"+ object.getString("token_type"));
		    System.out.println("Access Token"+ object.getString("access_token"));		    
		}
	}
	
	private static void queryCCUserApi(JSONObject httpresultstring,String orgID,String xAPIKEY) throws URISyntaxException, ClientProtocolException, IOException {
		// Tell the users that we are now fetching the groups for the report
		System.out.println("Fetching Groups" + "https://usermanagement.adobe.io/v2/usermanagement/groups/"+orgID+"/0");
		URL url = new URL("https://usermanagement.adobe.io/v2/usermanagement/groups/"+orgID+"/0");
        URI uri = new URIBuilder()
        	        .setScheme("https")
        	        .setHost("usermanagement.adobe.io")
        	        .setPath("/v2/usermanagement/groups/"+orgID+"/0")
        	        .setParameter("organizationId", orgID)
        	        .setParameter("page", "0")
        	        .build();
        // Before executing the call to the server, we need add the authentication/access tokens	
        	CloseableHttpClient httpclient = HttpClients.createDefault();
        	HttpGet httpget = new HttpGet(uri);
        	JSONObject obj = new JSONObject(httpresultstring.toString());
        	String token_type = obj.getString("token_type");
        	String access_token =obj.getString("access_token");
        	System.out.println("access token"+access_token);
        	httpget.addHeader("Authorization", "Bearer " + access_token);

        	httpget.addHeader("x-api-key", xAPIKEY);
        	httpget.addHeader("Content-Type","“application/json");
        	httpget.addHeader("Accept","application/json");
        	
        	CloseableHttpResponse response = httpclient.execute(httpget);
        	// Output Status of the HTTPGET
        	System.out.println("status code "+response.getStatusLine().getStatusCode() + ", Reason code "+response.getStatusLine().getReasonPhrase());
        	
        	InputStream is = response.getEntity().getContent();
            
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            //System.out.println("Authentication token " + out.toString());	
	}

	private static JSONObject postJWT(PrivateKey pk, String orgID,String xAPIKEY) throws IOException {
		Long ttlMillis = new Long(199906);
		String JWTreturn = createJWT(ttlMillis,pk,orgID,xAPIKEY);   
		JSONObject obj=null;
		String clientId="{clientID}";
		String clientSecret= "clientSecret";
		System.out.println("JWT Return " + JWTreturn);
	
		URL url = new URL("https://ims-na1.adobelogin.com/ims/exchange/jwt/");

		String encode = "client_secret="+clientSecret+"&{techacct}&jwt_token="+JWTreturn;
		
       	Map<String,Object> params = new LinkedHashMap<>();

       	params.put("client_id", clientId);

        params.put("client_secret", clientSecret);
        params.put("jwt_token", JWTreturn);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
           	if (postData.length() != 0) postData.append('&');
           	postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
           	postData.append('=');
           	postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
 
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        if (conn.getResponseCode()==200) {
        		System.out.println("Connection is OK");

        int statusCode = conn.getResponseCode();

        InputStream is = null;

        if (statusCode >= 200 && statusCode < 400) {
        	// Create an InputStream in order to extract the response object
        		is = conn.getInputStream();
        	}
        	else {
        		is = conn.getErrorStream();
        	}
        
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        	StringBuilder out = new StringBuilder();
        	String line;
        	while ((line = reader.readLine()) != null) {
        		out.append(line);
        	}
        	reader.close();
        	// Show authentication token
        	//System.out.println(out.toString());
        	obj = new JSONObject(out.toString());
        	System.out.println("token type " + obj.getString("token_type") + ", access token " + obj.getString("access_token"));
    	
        } else {
    		System.out.println("Connection is Not OK");
    	};

        return obj;
	}

	//Sample method to construct a JWT (Java Web Token)

	private static String createJWT(Long ttlMillis, PrivateKey pk, String orgID,String xAPIKEY) {
		
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

		String setAudience="https://ims-na1.adobelogin.com/c/"+xAPIKEY;
		String setSubject="{techacct}";
		
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		Date exp = new Date();
	
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			exp = new Date(expMillis);
		}
	
		//We will sign our JWT with our ApiKey secret

		//Let's set the JWT Claims
		Claims claims = new DefaultClaims();
		claims.put("https://ims-na1.adobelogin.com/s/ent_user_sdk",true);
	
		JwtBuilder builder = Jwts.builder().setClaims(claims)
										.setExpiration(exp)

            							.setIssuer(orgID)

            							.setSubject(setSubject)

            							.setAudience(setAudience)
            							.signWith(signatureAlgorithm, pk);

		//if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			builder.setExpiration(exp);
		}
		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	// Get and decode the private key
			public static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName) throws GeneralSecurityException, IOException {
	        // PKCS#8 format
	        final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
	        final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

	        // PKCS#1 format
	        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
	        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

	        Path path = Paths.get(pemFileName.getAbsolutePath());

	        String privateKeyPem = new String(Files.readAllBytes(path));

	        if (privateKeyPem.indexOf(PEM_PRIVATE_START) != -1) { // PKCS#8 format
	            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
	            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

	            byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);

	            KeyFactory factory = KeyFactory.getInstance("RSA");
	            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

	        } else if (privateKeyPem.indexOf(PEM_RSA_PRIVATE_START) != -1) {  // PKCS#1 format

	            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
	            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

	            DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));

	            DerValue[] seq = derReader.getSequence(0);

	            if (seq.length < 9) {
	                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
	            }

	            // skip version seq[0];
	            BigInteger modulus = seq[1].getBigInteger();
	            BigInteger publicExp = seq[2].getBigInteger();
	            BigInteger privateExp = seq[3].getBigInteger();
	            BigInteger prime1 = seq[4].getBigInteger();
	            BigInteger prime2 = seq[5].getBigInteger();
	            BigInteger exp1 = seq[6].getBigInteger();
	            BigInteger exp2 = seq[7].getBigInteger();
	            BigInteger crtCoef = seq[8].getBigInteger();

	            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

	            KeyFactory factory = KeyFactory.getInstance("RSA");

	            return factory.generatePrivate(keySpec);
	        }

	        throw new GeneralSecurityException("Not supported format of a private key");
	    }
	
	// Group report 
	private static void groupreport(JSONObject paramHttpresultstring, String orgID, String xAPIKEY) throws URISyntaxException, ClientProtocolException, IOException {
		// configure the URI for the server (note groups defined) (200 at a time, but only first 200 implemneted)
		URL url = new URL("https://usermanagement.adobe.io/v2/usermanagement/groups/"+orgID+"/0");
		//System.out.println("groups uri" + "https://usermanagement.adobe.io/v2/usermanagement/groups/"+orgID+"/0");
        	URI uri = new URIBuilder()
        	        .setScheme("https")
        	        .setHost("usermanagement.adobe.io")
        	        .setPath("/v2/usermanagement/groups/"+orgID+"/0")
        	        .setParameter("organizationId", orgID)
        	        .setParameter("page", "0")
        	        .build();
        	
        	CloseableHttpClient httpclient = HttpClients.createDefault();
        	HttpGet httpget = new HttpGet(uri);
        	JSONObject obj = new JSONObject(paramHttpresultstring.toString());
        	System.out.println("access token "+obj.getString("access_token"));
        	httpget.addHeader("Authorization", "Bearer " + obj.getString("access_token"));
        	httpget.addHeader("x-api-key", xAPIKEY);
        	httpget.addHeader("Content-Type","“application/json");
        	httpget.addHeader("Accept","application/json");
        	

        	CloseableHttpResponse response = httpclient.execute(httpget);
        	// Output Status of the HTTPGET
        	System.out.println("status code "+response.getStatusLine().getStatusCode() + "Reason phase "+response.getStatusLine().getReasonPhrase());
        	  	
        	InputStream is = response.getEntity().getContent();
            
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            // Show authentication token
            
            JSONObject userApiQuery_result = new JSONObject(out.toString());
            JSONArray groupsObject1 = userApiQuery_result.getJSONArray("groups");
            //System.out.println(groupsObject1.toString());
            final int n = groupsObject1.length();
            for (int i = 0; i < n; ++i) {
              final JSONObject group = groupsObject1.getJSONObject(i);
              System.out.println("");
              System.out.println("Group Name " + group.getString("groupName") +", there are " + group.getString("memberCount") + " users in the group" );
              // Get the users for each group
              readUsersbyProdCfg(paramHttpresultstring, group.getString("groupName"),orgID,xAPIKEY,obj.getString("access_token"),group.getString("memberCount"));
            }		
	}
	
	
	private static void  readUsersbyProdCfg(JSONObject paramHttpresultstring,String productConfiguration, String orgID,String xAPIKEY,String access_token,String memberCount) throws URISyntaxException, ClientProtocolException, IOException {
    	URI uri = new URIBuilder()
    	        .setScheme("https")
    	        .setHost("usermanagement.adobe.io")
    	        .setPath("/v2/usermanagement/users/"+orgID+"/0/"+productConfiguration)
    	        .setParameter("organizationId", orgID)
    	        .setParameter("page", "0")
    	        .build();
    	// Execute call to the Adobe server, using the access credentials from the main procedure
     	//System.out.println("Users within the group list- ");
    	
    	// Get contents of the response from the server
    	StringBuilder usersOut=null;
    	
    	usersOut = getContent(uri,paramHttpresultstring,access_token,xAPIKEY,productConfiguration );

      	// Read users within the specified group
        JSONObject userApiQuery_result = new JSONObject(usersOut.toString());
        try {
        	JSONArray usersObject = userApiQuery_result.getJSONArray("users");
        		final int n = usersObject.length();
        		for (int i = 0; i < n; ++i) {
        			final JSONObject user = usersObject.getJSONObject(i);
        			System.out.println("	email:" + user.getString("email") + ", Name:"+ user.getString("firstname")+" " + user.getString("lastname") +", Country:" + user.getString("country") +", Status:" + user.getString("status"));
        	}
        } catch (JSONException e) {
        	
        }
               
	}

	private static StringBuilder getContent(URI uri,JSONObject paramHttpresultstring, String access_token, String xAPIKEY,String searchType) throws UnsupportedOperationException, IOException, JSONException {
	   	// Execute call to the Adobe server, using the access credentials from the main procedure
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpGet httpget = new HttpGet(uri);
    	JSONObject obj = new JSONObject(paramHttpresultstring.toString());
    	httpget.addHeader("Authorization", "Bearer " + access_token);
    	httpget.addHeader("x-api-key", xAPIKEY);
    	httpget.addHeader("Content-Type","“application/json");
    	httpget.addHeader("Accept","application/json");
 
    	StringBuilder usersOut = null;
    	// Implement a back off routine, if hitting the server too many times
        int maxRequests = 4;
        int currRequests = 0;
        boolean retry = false;
        do {
        	
        	retry = false;  
        	CloseableHttpResponse response = httpclient.execute(httpget);    	
    	
        	InputStream is = response.getEntity().getContent();
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        	usersOut = new StringBuilder();
        	String line;
        	while ((line = reader.readLine()) != null) {
        		usersOut.append(line);
        	}	
        	reader.close();
        	JSONObject anythingtoreport=null;
        	
        	anythingtoreport = new JSONObject(usersOut);
        	
        	//  Is there a too many requests on the server, then catch the error and sleep the thread.
        	boolean errorFound = usersOut.toString().contains("error_code");
        	if (errorFound && usersOut.toString().contains("429050")) {
        		
        
        		currRequests++;
        		retry = true;
        		// Sleep thread for 20 * loop value seconds (give subsequent requests more time), then try again
        		try {
        			int sleepValue = 20000 * currRequests;
        			System.out.println("Too Many Requests, sleeping "+sleepValue+" seconds");
					Thread.sleep(sleepValue);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	//  Was the group found ?        	
        	boolean noGroupstoreport = usersOut.toString().contains("result");
        	if (noGroupstoreport && usersOut.toString().contains("error.group.not_found")) {
        		System.out.println("Group " + searchType + " not found");
        	}
        		
        } while  (retry == true && currRequests < maxRequests);
        return usersOut;
	}
	
	
}