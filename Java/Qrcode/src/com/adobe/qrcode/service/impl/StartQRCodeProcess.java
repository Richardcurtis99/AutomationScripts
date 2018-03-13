package com.adobe.qrcode.service.impl;

import java.util.*;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;


import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class StartQRCodeProcess {

	static Frame fr; 
    static 
    { 
        fr = new Frame(); 
        fr.pack(); 
    } 

	public static void main(String[] args) throws IOException,
			InterruptedException, ParserConfigurationException, SAXException {

		String logo="";
		Path faxFolder = Paths.get("/users/richardcurtis/monitorfolder");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		String fileName ="";
		
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
			//	System.out.println(StandardWatchEventKinds.ENTRY_CREATE.);
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
				// Is file a file or directory
						fileName = event.context().toString();
						fileName = faxFolder +"/"+ fileName;
						//System.out.println(fileName);
						//	System.out.println(new File(faxFolder+'/'+fileName).isFile());
						if (new File(fileName).isFile()) {

		// Generate Bit.ly
						String encodeString = "https://richardcurtis.marketing.adobe.com/assetdetails.html/content/dam/mac/richardcurtis/aod/daf/4.png"; 

				           HttpClient httpclient = new HttpClient();
				            HttpMethod method = new GetMethod("https://api-ssl.bitly.com/v3/shorten");
				            method.setQueryString(
				                    new NameValuePair[]{
				                            new NameValuePair("longUrl",encodeString),
				                            new NameValuePair("domain","bit.ly"),
				        //                    new NameValuePair("login","richardcurtisadobe"),
				        //                    new NameValuePair("apiKey","R_e4785235d82f7995a1bcd440dfe01f18"),
				                            new NameValuePair("format","xml"),
				        //                    new NameValuePair("history","1")
				            //                }
				          //          );
				                          
				            int rt =  httpclient.executeMethod(method);
				            String responseXml = method.getResponseBodyAsString();
				            String retVal = null;
				            if(responseXml != null) {
				                // parse the XML
				                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				                DocumentBuilder db;
								
									db = dbf.newDocumentBuilder();
								
				                StringReader st = new StringReader(responseXml);
				                Document d;
								
									d = db.parse(new InputSource(st));
								
				        
								NodeList nl = d.getElementsByTagName("shortUrl");
				                if(nl != null) {
				                    Node n = nl.item(0);
				                    retVal = n.getTextContent();
						
				                }
				            }
				            retVal = "abc";
				            System.out.println("here");
				            System.out.println("bit ly code =" + retVal);
		// do QR Code Stuff
						
						
		// End QR Stuff				
						
						System.out.println("File Created:" + fileName);
						
						/** 
						* Puts top image over bottom image. 
						* It paints top image on the bottom with zero offset. 
						* @param bottomImg  the image to be drawn on 
						* @param topImg     the image to draw on top of the bottom image 
						*/ 
								Image bottomImg=null;
								Image topImg=null;
								// Read from a file
//							    File file1 = new File("C:/temp/logo.jpg");
							    File file1 = new File(fileName);			
								bottomImg = ImageIO.read(file1);
							    File file2 = new File("C:/temp/xx.png");
					//		    File file2 = new File(TopImage_QRCodeLocation);
							    topImg = ImageIO.read(file2);
							    Image newImg = null;
						    
						    	System.out.println("Creating Canvas");
						    	Canvas c = new Canvas();

						    	System.out.println("Bottom Image Width="+bottomImg.getWidth(fr));
						        System.out.println("Bottom Image Height="+bottomImg.getHeight(fr));
						    	System.out.println("Top Image Width="+topImg.getWidth(fr));
						        System.out.println("Top Image Height="+topImg.getHeight(fr));

						        
						    	BufferedImage img = new BufferedImage(bottomImg.getWidth(fr), bottomImg.getHeight(fr), BufferedImage.TYPE_INT_RGB);
						    	img.createGraphics(); 
						    	Graphics2D g = (Graphics2D)img.getGraphics();  
						        g.drawImage(bottomImg, 0, 0, fr);
						        if (logo=="QR") {
						        	// If Qrcode then add QR code at the orignal image with the QRcode width and hight in mind
						        	g.drawImage(topImg, bottomImg.getWidth(fr)-topImg.getWidth(fr), bottomImg.getHeight(fr)-topImg.getHeight(fr), fr);
						        } else {
						        	// if not a QR code then add image at 0,0
						        	g.drawImage(topImg, 0, 0, fr);
						        	
						        }
						       	System.out.println("Write file to disk");
						        ImageIO.write(img, "jpg", new File("c:/temp/myImage.jpg"));  
						        
						        g.dispose(); 
						       	System.out.println("newImg");
						        System.out.println("Image Processing Done");

						  //     	return "Image Comp OK";    
					    	}
						
					
		//		}
	//		}
			valid = watchKey.reset();

		} while (valid);

	}
}