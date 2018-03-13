package com.adobe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class getTinyUL {

	public static void main(String[] args) throws HttpException, IOException, ParserConfigurationException, SAXException {
		// TODO Auto-generated method stub
		String xURL = "https://www.behance.net/";
		System.out.println("encoding " + xURL);
		System.out.println("encode URL =" + getShortURL(xURL));
	}
	
	public static  java.lang.String getShortURL(String xURL) throws HttpException, IOException, ParserConfigurationException, SAXException {
		System.out.println("Generating Short URL" + xURL);
        HttpClient httpclient = new HttpClient();
        
        	HttpMethod method = new GetMethod("http://tinyurl.com/api-create.php");
        //HttpMethod method = new GetMethod("http://tiny-url.info/api/v1/create");
        method.setQueryString(
                new NameValuePair[]{
                        new NameValuePair("url",xURL),
               //         new NameValuePair("version","2.0.1"),
                        new NameValuePair("provider","bit.ly"),
                        new NameValuePair("apikey","A4437F47DBF58H6CF9"),
                        new NameValuePair("format","xml"),
                //        new NameValuePair("history","1")
                        }
                );
        
        httpclient.executeMethod(method);
       // String responseXml = method.getResponseBodyAsString();
        String response = method.getResponseBodyAsString();
 //       System.out.println("response"+response);
 //       String retVal = null;
 //       if(responseXml != null) {
 //           // parse the XML
 //           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
 //           DocumentBuilder db = dbf.newDocumentBuilder();
 //           StringReader st = new StringReader(responseXml);
 //           Document d = db.parse(new InputSource(st));
 //           NodeList nl = d.getElementsByTagName("shortUrl");
 //           if(nl != null) {
 //               Node n = nl.item(0);
 //               retVal = n.getTextContent();
 //           }
 //       }
 //       System.out.println("here");
 //       System.out.println("bit ly code =" + retVal);
		
		return response;
	}

}
