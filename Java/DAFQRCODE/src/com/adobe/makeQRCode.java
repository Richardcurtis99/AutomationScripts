package com.adobe;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;

import com.adobe.getTinyUL;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxFiles.ListFolderException;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

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
import static java.nio.file.StandardCopyOption.*;


public class makeQRCode {

	public static void main(String[] args) throws HttpException, IOException, ParserConfigurationException, SAXException, InterruptedException, ListFolderException, DbxException {
		// TODO Auto-generated method stub
		String logo="";
		Path faxFolder = Paths.get("/users/richardcurtis/QRCODEDAFevent/monitorfolder");
		String QRCodeGraphicsFolder = "/users/richardcurtis/QRCODEDAFevent/QRcodeGraphics/";
		String ouptutLocation = "/users/richardcurtis/QRCODEDAFevent/PrintableImage/";
		String LightroomWatchedfolder = "/users/richardcurtis/QRCODEDAFevent/LightroomWatched Folder/";
		String dropboxPublic = "/users/richardcurtis/Dropbox/Public/";
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		String fileName ="";
		String shortURL="";
		boolean valid = true;
		// Wait loop
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
			//	System.out.println(StandardWatchEventKinds.ENTRY_CREATE.);
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
				// Is file a file or directory
						fileName = event.context().toString();
						String filenameonly = event.context().toString();
						fileName = faxFolder +"/"+ fileName;
						System.out.println("File name" +fileName);
						//	System.out.println(new File(faxFolder+'/'+fileName).isFile());
						if (new File(fileName).isFile()) {
							System.out.println("Generate Short URL");
							// Digital/Original version to public facing, then get the public link		
							String Dropboxdone = com.adobe.uploadtoDropBox.uploadtoDropBox(fileName, filenameonly);

							// 	Link to the AOD File name, using Filename from monitor file
						//	shortURL = com.adobe.getTinyUL.getShortURL("https://richardcurtis.marketing.adobe.com/assetdetails.html/content/dam/mac/richardcurtis/aod/daf/"+fileName);
							shortURL = com.adobe.getTinyUL.getShortURL(Dropboxdone);
							System.out.println("short URL=" +shortURL);
							String complete = com.adobe.makeQRcodeGraphic.cnvTextToImg(shortURL, QRCodeGraphicsFolder,filenameonly);
							System.out.println("QRCODE Complete");
							// Place QRcode on photo
							String res = com.adobe.applyGraphictoImage.compImg(fileName, QRCodeGraphicsFolder+filenameonly,ouptutLocation,filenameonly);
							System.out.println("JPG with Barcode Created");
							
							// Copy files around
							// Digital/Original version to public facing			
				//			String Dropboxdone = com.adobe.uploadtoDropBox.uploadtoDropBox(fileName, filenameonly);
							// QRcode to Lightoom Watched folder
							System.out.println("moving QRCode file to Lightroom watched folder");
							Files.move(Paths.get(ouptutLocation,filenameonly), Paths.get(LightroomWatchedfolder+filenameonly), REPLACE_EXISTING);
							// Remove Original file from Monitor folder.
							//System.out.print("removing "+faxFolder+filenameonly);
							Files.delete(Paths.get(fileName));
							valid = watchKey.reset();
						}
				}
//		System.out.println(com.adobe.getTinyUL.getShortURL("https://www.behance.net/"));
		//System.out.println("new URL = " + shortURL);
				
			}
		} while (valid);
	
}	
}