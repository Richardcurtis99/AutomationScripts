package com.adobe.adobeQueryAEDash;


	import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
		
	
	@SuppressWarnings("restriction")
	public class CCuserAPI_Base {
	
		public static void main (String[] args) throws GeneralSecurityException, IOException, URISyntaxException {
			// Your Organisation ID goes here, example xx@AdobeOrg
			String orgID = "<!-Your Organisation ID goes in here-->";
			// Your APIKEY goes in here
			String apiKey = "<!-Your APIKey goes in here-->";
			// Locate the private key file, that has been downloaded from the dashboard API console
			// PC = PC notation, Mac is the relative notation
			File privateKeyFile = new File("/users/richardcurtis/AEDASH_API_KEY/private.key");
			PrivateKey pk = pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(privateKeyFile);
			// Call function to sign the URL to the CC User API server
			JSONObject httpresultstring = postJWT(pk,apiKey,orgID);

			//Have access ?
			if (httpresultstring != null) {
				queryCCUserApi(httpresultstring,orgID,apiKey);
			} else {
				System.out.println("Problem wih the signed URL");
			}
		}  
		 
			
	
		private static void queryCCUserApi(JSONObject httpresultstring,String orgID,String apiKey) throws URISyntaxException, ClientProtocolException, IOException {
			// Function to query the CC users API for groups - example, you will need to write your own for Read, Write, Delete  

			System.out.println("Looking for groups=");
  
        	URI uri = new URIBuilder()
        	        .setScheme("https")
        	        .setHost("usermanagement.adobe.io")
        	        .setPath("/v2/usermanagement/groups/"+orgID+"/0")
        	        .setParameter("organizationId", orgID)
        	        .setParameter("page", "0")
        	        .build();
        	
        	CloseableHttpClient httpclient = HttpClients.createDefault();
        	HttpGet httpget = new HttpGet(uri);
        	JSONObject obj = new JSONObject(httpresultstring.toString());
        	String access_token =obj.getString("access_token");
        	System.out.println("Access token="+access_token);
        	httpget.addHeader("Authorization", "Bearer " + access_token);
        	httpget.addHeader("x-api-key", apiKey);
        	httpget.addHeader("Content-Type","“application/json");
        	httpget.addHeader("Accept","application/json");

        	CloseableHttpResponse response = httpclient.execute(httpget);
        	// Output Status of the HTTPGET
        	System.out.println("status code "+response.getStatusLine().getStatusCode());
        	System.out.println("status code "+response.getStatusLine().getReasonPhrase());
        	
        	InputStream is = response.getEntity().getContent();
            // Read authentication token to show the user.
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            // Show result to user
            System.out.println("Query result=" + out.toString());
		
	}
		
	private static JSONObject postJWT(PrivateKey pk,String apiKey,String orgID) throws IOException {
		// Function to sign the access token request

		// make a future time (arbitary value), this will be used as an expiry time for the access token
		Long ttlMillis = new Long(199906);
		// Users variable definition from the CC Dashboard - You will need to add your keys to this section
		// Your secret key from the CC API Console goes in here
		String clientSecretKey = "<!-Your secret key goes in here-->";
		// Your techAccID from the CC API Console, example - xx@techacct.adobe.com
		String techAccID = "<!-Your Tech Acc ID goes in here-->";
		//String clientID = "368cc52aecfa48bab50570ced959a11d";
		// The CC JWT URL
		URL jwturl = new URL("https://ims-na1.adobelogin.com/ims/exchange/jwt/");

		// Sign the access token
		String JWTreturn = createJWT(ttlMillis,pk,orgID,techAccID,apiKey);
		// Show the value of the access token
		// Setup the URL parameters
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("client_id", apiKey);
        params.put("client_secret", clientSecretKey);
        params.put("jwt_token", JWTreturn);
        // Put URL parameters onto the URL
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
 
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		// Make the connection and Post the URL to the CC User API
		HttpURLConnection conn = (HttpURLConnection)jwturl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        // Status of the connection ?
        System.out.println("POST Connection Status = "+conn.getResponseCode());

        int statusCode = conn.getResponseCode();

        // Execute reading the output from the server if OK
        
        InputStream is = null;

        if (statusCode >= 200 && statusCode < 400) {
           // Create an InputStream in order to extract the response object
           is = conn.getInputStream();
        }
        else {
        	// Error from the server, need to diagnose the error
           is = conn.getErrorStream();
        }
        // Read the token from the JWT url Post
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        // Show authentication token
        System.out.println("authentication token " + out.toString());
        // Place the received token into a JSON string
        JSONObject obj = new JSONObject(out.toString());
        // How to extract data from the JSON token
        System.out.println("token type=" + obj.getString("token_type"));
        System.out.println("expires_in=" + obj.getLong("expires_in"));
        System.out.println("access_token=" + obj.getString("access_token"));
        return obj;
	}

	//Sample method to construct a JWT

	private static String createJWT(Long ttlMillis, PrivateKey pk,String IssuerID,String orgID, String clientID) {
		
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
		// Get current time
		long nowMillis = System.currentTimeMillis();

		Date exp = new Date();
		// If a future time was supplied as a parameter, make an expiry time
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			exp = new Date(expMillis);
		}
	
		//We will sign our JWT with our ApiKey secret

		//Set the JWT Claims, URL and Parameters
		Claims claims = new DefaultClaims();
		claims.put("https://ims-na1.adobelogin.com/s/ent_user_sdk",true);
		
		JwtBuilder builder = Jwts.builder().setClaims(claims)
										.setExpiration(exp)
            							.setIssuer(IssuerID)
            							.setSubject(orgID)
            							.setAudience("https://ims-na1.adobelogin.com/c/"+clientID)
            							.signWith(signatureAlgorithm, pk);

		//if it has been specified, let's add the expiration time to the request
		if (ttlMillis >= 0) {
//			long expMillis = nowMillis + ttlMillis;
			builder.setExpiration(exp);
		}

		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	// Get and decode the private key
	public static PrivateKey pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(File pemFileName) throws GeneralSecurityException, IOException {
			// Read private key and use this to sign the request to the User API.
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
}
	
