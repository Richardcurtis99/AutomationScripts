/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.rcsample.rcsample.core.servlets;

import org.apache.sis.internal.jaxb.gmx.FileName;
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


/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */

@Component(service=Servlet.class,immediate=true,

property= {

Constants.SERVICE_DESCRIPTION + "=SimplePostAssettoAEM Demo Servlet",

"sling.servlet.paths=/bin/aem64app/SimplePostAssettoAEM",
          "sling.servlet.methods=" + HttpConstants.METHOD_POST,

          "sling.servlet.extensions=" + "json"

})


//Inject a Sling ResourceResolverFactory

//public class SimplePostAssetToAEM extends SlingSafeMethodsServlet{
public class SimplePostAssetToAEM extends SlingAllMethodsServlet{
	

private static final long serialVersionUID = 21L;


//Inject a Sling ResourceResolverFactory
@Reference
private ResourceResolverFactory resolverFactory;

@Reference
private CryptoSupport cryptoSupport;

private String username="admin";
private String password="admin";

  @Override
  protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

	RequestParameterMap requestDatax = request.getRequestParameterMap();
	RequestParameter myData = requestDatax.getValue("body");
	response.getWriter().println("target stream="+myData.toString());
	JSONObject myData_JSON=null;
	try {
		myData_JSON = new JSONObject(myData.toString());
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String URL=null;
	String cc_eventPath=null;
	try {
		 
	 URL = myData_JSON.getString("URL");
	 cc_eventPath = myData_JSON.getString("cc_eventPath");
	
	} catch (JSONException e){
		
	}
	
	URL CCs3 = new URL(URL);
	
  	
    /// write content to AEM DAM 
    //Save the uploaded file into the AEM DAM using AssetManager API
    
    try
    {
    	
    	// 	File initialFile = new File("/Users/richardcurtis_Production/Desktop//Document_Cloud_Pitch_Deck_FULL1.jpg");

    	//	InputStream targetStream = new FileInputStream(initialFile);
    	//	response.getWriter().println("target stream="+targetStream);
    	//	response.getWriter().println("here");
        //Inject a ResourceResolver
        @SuppressWarnings("deprecation")
        
        
        Map<String, Object> AEMDAMauthInfo = new HashMap<String,Object>();
        	AEMDAMauthInfo.put(ResourceResolverFactory.SUBSERVICE, "datawrite");	
        	AEMDAMauthInfo.put(ResourceResolverFactory.USER, username);
        	String unprotectedPass;
            try {
                unprotectedPass = cryptoSupport.unprotect(password);
            } catch (CryptoException e) {
                unprotectedPass = password;
                response.getWriter().println(e.getMessage());
            }
            AEMDAMauthInfo.put(ResourceResolverFactory.PASSWORD, unprotectedPass.toCharArray());
      
        
        ResourceResolver resourceResolver = resolverFactory.getResourceResolver(AEMDAMauthInfo);
        response.getWriter().println("here 2="+resourceResolver.toString());
        //Use AssetManager to place the file into the AEM DAM
        com.day.cq.dam.api.AssetManager assetMgr = resourceResolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
        // cannot use an absolute path
        response.getWriter().println("user id="+resourceResolver.getUserID());
        //
        response.getWriter().println("cc_eventPath=" + cc_eventPath);
        String ccfName = cc_eventPath.substring(6,cc_eventPath.length());
        response.getWriter().println("ccfName=" + ccfName);
        //Process = cc_eventPath for the file name 
        
        //String newFile = "/content/dam/travel/RC1.jpg";
        String newFile = "/content/dam" + ccfName;
        //String newFile = "/content/dam/Agency1/myTestCampaign1/approvedFolderForAEM/RC 1.jpg";
        response.getWriter().println("newFile=" + newFile);
        
        //+fileName ; 
        com.day.cq.dam.api.Asset abc = assetMgr.createAsset(newFile, CCs3.openStream(),"image/jpeg", true);
        response.getWriter().println("file name " +abc.getName());
        resourceResolver.commit();
        response.getWriter().println("new file Yes? = ");
        
        // Return the path to the file was stored
        
   }
    catch(Exception e)
    {
        e.printStackTrace();
        response.getWriter().println(e.toString());
    }
    //return null;
    //}
    
    
    
    
	response.setHeader("Content-Type", "text/html");
	try {
		response.getWriter().print("JSON signed uel "+myData_JSON.getString("URL"));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	response.getWriter().print("<h1>Sling Servlet injected this title");

	response.getWriter().close();

  }  
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

	  BufferedReader requestData = request.getReader();
		
		
		response.setHeader("Content-Type", "text/html");
		response.getWriter().print("JSON " + requestData.toString());
		response.getWriter().print("<h1>Sling Servlet injected this title");

		response.getWriter().close();

	  }  


}
