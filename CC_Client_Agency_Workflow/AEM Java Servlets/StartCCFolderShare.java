package com.rcsample.rcsample.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.caconfig.annotation.Property;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.json.*;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.crypto.spec.RC2ParameterSpec;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.social.filelibrary.client.api.Asset;
import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
//AssetManager
import com.day.cq.dam.api.AssetManager; 


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.DataInputStream;

import java.io.InputStream;
import java.net.URI;
import java.util.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
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
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import java.nio.file.Files;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.json.*;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.FormBody;

import org.apache.tika.Tika;
/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */

@Component(service=Servlet.class,immediate=true,

property= {

Constants.SERVICE_DESCRIPTION + "=StartCCFolderShare",

"sling.servlet.paths=/bin/aem64app/StartCCFolderShare",

          "sling.servlet.methods=" + HttpConstants.METHOD_POST,

          "sling.servlet.extensions=" + "json"

})

// Function objective : Read the configuration JSON file and re-iterate over the folders and subfolders, then 
// send request to CC to create actual CC folders.
// 

//parameters from request
// request body
//"Agency2","jobTemplate2","myTestCampaign2"
// {"Agency" : "Agency2", 
//   "Template" : "jobTemplate2",
//	 "Campaign" : "myTestCampaign2"
//}
//Inject a Sling ResourceResolverFactory

//public class SimplePostAssetToAEM extends SlingSafeMethodsServlet{
public class StartCCFolderShare extends SlingAllMethodsServlet{
	

private static final long serialVersionUID = 52L;


//Inject a Sling ResourceResolverFactory
//@Reference
//private ResourceResolverFactory resolverFactory;

//@Reference
//private CryptoSupport cryptoSupport;

//private String username="admin";
//private String password="admin";

static String GBLclientID = "";
static String GBLbearer = "";
static String GBLtxm="";
static String GBLapprovedFolderforAEM="";

static String access_token = new String("eyJ4NXUiOiJpbXNfbmExLWtleS0xLmNlciIsImFsZyI6IlJTMjU2In0.eyJpZCI6IjE1Mjc2MjgyMjA3MjZfNDM2OGU4ZjItZGVmNC00M2MyLWEzYjAtMmNhNTUyZDY2ZGQ3X3VlMSIsImNsaWVudF9pZCI6ImJiYWNkNTkxOTNhZTQzYTJiMTJiNzU5Zjk1ZmI2ZmQ2IiwidXNlcl9pZCI6IjU1MkM2MTA0NDY2OTVEMTE5OTIwMTZCN0BBZG9iZUlEIiwic3RhdGUiOiIiLCJ0eXBlIjoiYWNjZXNzX3Rva2VuIiwiYXMiOiJpbXMtbmExIiwiZmciOiJTTzY1TTZENFhMTjc2UFlQQUFBQUFBQUFRVT09PT09PSIsInJ0aWQiOiIxNTI3NjI4MjIwNzI3XzIyZjIyNDlkLTIzYzUtNGUxOC05ZDliLTgxNTRhOTk2OGRjNl91ZTEiLCJvYyI6InJlbmdhKm5hMXIqMTYzYWRiY2M3NTQqQTdRWUszMkFXWDNKM0M0R1ZHSjdaNDBIQUciLCJydGVhIjoiMTUyODgzNzgyMDcyNyIsIm1vaSI6IjU3ZWIxZDgyIiwiYyI6ImhzelR4a0tERVlzTzF0WmlOZlZWWnc9PSIsImV4cGlyZXNfaW4iOiI4NjQwMDAwMCIsInNjb3BlIjoib3BlbmlkLGNyZWF0aXZlX3NkayxBZG9iZUlELGFkZHJlc3MsZW1haWwscHJvZmlsZSIsImNyZWF0ZWRfYXQiOiIxNTI3NjI4MjIwNzI2In0.EKs77M-X3bUVlZbUcGZiQ1_QZvOapFtwBSCr8UQsmqT9zCGJJgT53Nnj-UH_9vZ8RSWPLFrEwBBubW9tALpBbt-6eKV1ZuU5pInUNnIswkHUn6pd_iJTaKF-_x05Kq29KnHjDrmIJl1UygT1Cj90j_zD8zGKE1EYdjUxsiYHhRV_741Ve_dqN2pXeDYfCenJ1Mkps4hWY4wVjeBiupE8GmPmvXlM8TPMvqEKTdcNkwg1JJ2Nzs9qPSs06oFB8xkoLkQjv-2jHEeTdMajoH4MaBHJNY1URHi1ESVcStFxh9ikByaiAWvnO6ufShLV_B1_yFaCg8sizvymTuHPc5ezYg");


  @Override
  @SuppressWarnings("restriction")
  
  protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

	  RequestParameterMap requestDatax = request.getRequestParameterMap();
		RequestParameter myData = requestDatax.getValue("body");
		JSONObject myData_JSON=null;
		try {
			myData_JSON = new JSONObject(myData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String SlctAgency=null;
		String SlctTemplate=null;
		String SlctCampaign=null;
		
		try {
			 //"Agency2","jobTemplate2","myTestCampaign2"
			SlctAgency = myData_JSON.getString("Agency");
			SlctTemplate = myData_JSON.getString("Template");
			SlctCampaign = myData_JSON.getString("Campaign");
			response.getWriter().println("SlctAgency=" + SlctAgency);
			response.getWriter().println("SlctTemplate=" + SlctTemplate);
			response.getWriter().println("SlctCampaign=" + SlctCampaign);
		
		} catch (JSONException e){
			
		}
	// create cc goes here
	// Output Status of the HTTPGET

	  JSONObject agencyCollabConfig_desc =null;
		try {
			agencyCollabConfig_desc = getAgencyCollaborationConfig();
			GBLclientID = agencyCollabConfig_desc.getString("clientID");
			GBLbearer = agencyCollabConfig_desc.getString("bearer");
			GBLtxm = agencyCollabConfig_desc.getString("txm");
			GBLapprovedFolderforAEM = agencyCollabConfig_desc.getString("approvedFolderForDAM");
			
			response.getWriter().println("GBLclientID=" + GBLclientID);
			response.getWriter().println("GBLbearer=" + GBLtxm);
			response.getWriter().println("GBLtxm=" + GBLtxm);
		} catch(Exception e)
	    {
	        e.printStackTrace(); 
	    }
		
		String returnCreateStructure =null;
		try {
			returnCreateStructure = processJsonFolderStructure(getAgencyCollaborationConfig(),SlctAgency,SlctTemplate,SlctCampaign);
		} catch(Exception e)
	    {
	        e.printStackTrace(); 
	    }
	 	  
	// end of CC folder create
	response.getWriter().print("StartCCFolderShare - Kick Off Agency Completed");

	response.getWriter().close();

	
	
  } 
 
  
  private static String processJsonFolderStructure(JSONObject folderStructure,String agencyname,String jobTemplateName,String campaignName) throws FileNotFoundException, IOException, ParseException, JSONException, URISyntaxException {
	    
		issueCreateFolderReq(agencyname);
		issueCreateFolderReq(agencyname+"/"+campaignName);		
	
	    JSONArray jobTempalteJsonArray = folderStructure.getJSONArray("agency");
	    System.out.println("length of entries " + jobTempalteJsonArray.length());

	    for (int i=0;i<jobTempalteJsonArray.length();i++) {
  		System.out.println("jobTempalteJsonArray="+jobTempalteJsonArray.get(i).toString());
		    JSONObject jobTemplateSubFolderJsonObject = jobTempalteJsonArray.getJSONObject(i);
		    System.out.println("refname = "+jobTemplateSubFolderJsonObject.getString("refName"));
		    
		    // create Approved for AEM folder under main camapign
		    issueCreateFolderReq(agencyname+"/"+campaignName+"/"+GBLapprovedFolderforAEM);
		    
		    // Create the AE Shared folder (one that will be shared with the AE on the Agency side)
		    String AEShareName = jobTemplateSubFolderJsonObject.getString("AEShareName");
		    
		    
		    issueCreateFolderReq(agencyname+"/"+campaignName+"/"+campaignName+AEShareName);
		    
		    // process only records that match the jobTempateName that was selected
		    if (jobTemplateSubFolderJsonObject.getString("refName").equals(jobTemplateName)) {
		    System.out.println("ok found the job template name =" + jobTemplateName + " .processing");
		    if (jobTemplateSubFolderJsonObject.has("subFolders")){
		    	// send SubFolder array to processing.
		    	System.out.println("proessing subfolders ");
		    	processSubfolder(agencyname+"/"+campaignName+"/"+campaignName+AEShareName,jobTemplateSubFolderJsonObject.getJSONArray("subFolders"));
		    }
		    	//process the Job Temlate Structure
		    	System.out.println("here 1");
		    	
		    }
	    }
	
		return "Complete";
  }

  private static JSONObject getAgencyCollaborationConfig() throws URISyntaxException, ClientProtocolException, IOException{
		
		// configuration of the agencyCollaboration is on an S3 Bucket, fetch it for processing.
		URI uri = new URIBuilder()
		        .setScheme("https")
		        .setHost("s3.amazonaws.com")
		        .setPath("/rcb1/AgencyCollaborationConfig/FolderStructure_prod.json")
		        .build();

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet agencyCollobarationConfigHTTP = new HttpGet(uri);

		HttpResponse response = httpclient.execute(agencyCollobarationConfigHTTP);
		String responseBody = EntityUtils.toString(response.getEntity());
		JSONObject agencyCollabConfig=null;
		try {
			agencyCollabConfig = new JSONObject(responseBody);
		} catch(Exception e)
	    {
	        e.printStackTrace(); 
	    }
		return agencyCollabConfig;
	}

  private static  HttpResponse issueCreateFolderReq(String folderName) throws URISyntaxException, ClientProtocolException, IOException {

	  URI uri = new URIBuilder()
	          .setScheme("https")
	          .setHost("cc-api-storage.adobe.io")
	          .setPath("/files/"+folderName)
	          .build();

	  CloseableHttpClient httpclient = HttpClients.createDefault();
	  HttpPut httpput = new HttpPut(uri);

	  httpput.addHeader("X-Request-Id","123");
	  httpput.addHeader("Authorization", "Bearer " + GBLbearer);
	  httpput.addHeader("x-api-key", GBLclientID);
	  httpput.addHeader("Content-Type","application/vnd.adobe.directory+json");

	  //Execute and get the response.

	  HttpResponse response = httpclient.execute(httpput);
	  System.out.println("status code "+response.getStatusLine().getStatusCode());
	  System.out.println("status code "+response.getStatusLine().getReasonPhrase());

	  return response;
	  }

  private static  void  processSubfolder(String relativePath, JSONArray subFolder) throws ClientProtocolException, JSONException, URISyntaxException, IOException {
		// get sub Folders Array
			for (int subfolderidx = 0;subfolderidx<subFolder.length();subfolderidx++){
				//get name of the subfolder
				JSONObject subFolderDetails_Object = subFolder.getJSONObject(subfolderidx);
				System.out.println("subFolderDetails_Object _name=" + subFolderDetails_Object.getString("name"));
				issueCreateFolderReq(relativePath+"/"+subFolderDetails_Object.getString("name"));
				if (subFolderDetails_Object.has("subFolders")){
					System.out.println("nested subfolders found");
					JSONArray subFolderDetailssubfolder_Array = subFolderDetails_Object.getJSONArray("subFolders");
					System.out.println("sub folder length" +subFolderDetailssubfolder_Array.length());
					processSubfolder(relativePath+"/"+subFolderDetails_Object.getString("name"),subFolderDetailssubfolder_Array);
					
				}
			}
		}
  
  private static String splitString(String fileName) {
		String[] parts = fileName.split("/");
		System.out.println("String file name = "+fileName);
		System.out.println("String file length = "+parts.length);
		System.out.println("returning = " +parts[parts.length-1]);
		return parts[parts.length-1];
	}

}
