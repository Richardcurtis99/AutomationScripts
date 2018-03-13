package com.adobe.qrcode.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.util.Base64;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.adobe.qrcode.service.FTPupload;

@Component()
@Service(value = FTPupload.class)

public class FTPuploadImpl implements FTPupload {
	
	public String TalktoMe() {
		return "loud and clear..";
	}
	
	public String ftp(String servername, String username, String password,
			String StoreFileName,String originalfilename) throws IOException {

        InetAddress addr = null;
    	String result=null;	
    	FTPClient client = new FTPClient();
    	FileInputStream fis = null;    
        String uploadDir = "CaptureEvent";
        String ftp_filename;
        String print_filename;
        String storage_print_filename;
        try {
        	System.out.println("Store File Name" + StoreFileName);
 //       	System.out.println("localhost:4502 - filename =http://localhost:4502/content/dam/uploads/"+originalfilename+"/jcr:content/renditions/"+StoreFileName);
    		URL ftpurl = new URL("http://localhost:4502/content/dam/uploads/"+originalfilename+"/jcr:content/renditions/AdobeWebResized800600.jpg");
    		URL printurl = new URL("http://localhost:4502/content/dam/uploads/"+originalfilename+"/jcr:content/renditions/AdobePrint.jpg");
//==== Write Print file        	
        	URLConnection print_connection = printurl.openConnection();
        	print_filename = "d:/CTEdropbox/Dropbox/CaptureEventLR/"+StoreFileName;
        	System.out.println("print_filename=" + print_filename);
        	InputStream print_stream = print_connection.getInputStream();
    		BufferedInputStream print_in = new BufferedInputStream(print_stream);

//    		FileOutputStream file = new FileOutputStream("/Users/cqce/CQ5.5R212PROD/FTPUploadFolder/"+StoreFileName);
    		FileOutputStream print_file = new FileOutputStream(print_filename);
    		BufferedOutputStream print_out = new BufferedOutputStream(print_file);
    		int print_i;
    		while ((print_i = print_in.read()) != -1) {
    			print_out.write(print_i);
    		}
    		print_out.flush();
//=== Write to storage area
        	URLConnection storage_print_connection = ftpurl.openConnection();

    		storage_print_filename = "d:/CTEdropbox/Dropbox/Public/CaptureEventLR/"+StoreFileName;
    		System.out.println("storage_print_filename=" + storage_print_filename);
        	InputStream storage_print_stream = storage_print_connection.getInputStream();
    		BufferedInputStream storage_print_in = new BufferedInputStream(storage_print_stream);

    		FileOutputStream storage_print_file = new FileOutputStream(storage_print_filename);
    		BufferedOutputStream storage_print_out = new BufferedOutputStream(storage_print_file);
    		int storage_print_i;
    		while ((storage_print_i = storage_print_in.read()) != -1) {
    			storage_print_out.write(storage_print_i);
    		}
    		storage_print_out.flush();
//--------
    		
    		
    		//      		URL url = new URL("http://localhost:4502/content/dam/uploads/my1image.jpg/jcr:content/renditions/AdobeWebResized800600.jpg");
 //       	URL url = new URL("http://www.richard-curtis.net/CaptureEvent/AdobeLogoRC12801280.png");
//====
//    		System.out.println(ftpurl);
//==== manage FTP URL        	
//        	URLConnection ftp_connection = ftpurl.openConnection();
//        	ftp_filename = "d:/cqce/CQ5.5R212PROD/FTPUploadFolder/"+StoreFileName;
//        	InputStream ftp_stream = ftp_connection.getInputStream();
//    		BufferedInputStream ftp_in = new BufferedInputStream(ftp_stream);

//    		FileOutputStream file = new FileOutputStream("/Users/cqce/CQ5.5R212PROD/FTPUploadFolder/"+StoreFileName);
//    		FileOutputStream ftp_file = new FileOutputStream(ftp_filename);
//    		BufferedOutputStream ftp_out = new BufferedOutputStream(ftp_file);
//    		int ftp_i;
//    		while ((ftp_i = ftp_in.read()) != -1) {
//    		    ftp_out.write(ftp_i);
//    		}
//    		ftp_out.flush();
//=====Upload to FTP site
//        		client.connect("207.45.187.74");
//                client.login("rich@richard-curtis.net", "110970");
//                client.setFileType(FTP.BINARY_FILE_TYPE);
//                System.out.print(client.getReplyString());

    // Create upload directory
//                boolean cd1 = client.changeWorkingDirectory("public_html");
//                System.out.print(cd1);
                
//                boolean cd2 = client.changeWorkingDirectory(uploadDir);
//                System.out.print(cd2);
//                // create directory if ok.
//                if (!cd2) {
//                        boolean mkdir = client.makeDirectory(uploadDir);
//                        System.out.print(client.getReplyString());
//                        if (!mkdir) {
//        //                        System.err.println("Cannot create upload directory: " + uploadDir + " at server: " + servername);
//        //                        System.exit(1);
//                        }
//                        // navigate to new directory
//                        cd2 = client.changeWorkingDirectory(uploadDir);
//                }
/////========      
//                System.out.println("Uploading file... "+ftp_filename);
//                File uploadedfile = new File(ftp_filename);
//                if (uploadedfile.exists()) {
//                    System.out.println("Uploading file... "+ftp_filename);
//                    fis = new FileInputStream(uploadedfile);
//                    client.storeFile(uploadedfile.getName(), fis);
//     //               client.storeFile(StoreFileName, fis);
//
//                    System.out.print(client.getReplyString());
//                } else {
//                    System.out.println("Not uploading file, because it doesn't exists.");
/////==========
////                    fis = new FileInputStream(filename);
////                    client.storeFile(filename, fis);
////                    client.logout();
//                	}        	
//        		} catch (IOException ex) {
//                    ex.printStackTrace();
//    				} finally {
//                    try {
//                    	if (fis != null) {
//                    		fis.close();
//                    	}
//                } catch (IOException e) {
//                                // do nothing
                } finally {
                //	System.out.println("write to drop box failed");
                }
    	return "OK";    
    	}
}

