package com.adobe;

import com.dropbox.core.*;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v2.*;
import com.dropbox.core.v2.DbxFiles.FileMetadata;
import com.dropbox.core.v2.DbxFiles.ListFolderException;
import com.dropbox.core.v2.DbxFiles.Metadata;
import com.dropbox.core.v2.DbxSharing.LinkMetadata;
import com.dropbox.core.v2.DbxSharing.PathLinkMetadata;
import com.dropbox.core.v2.DbxSharing.UserInfo;
import  com.dropbox.core.v2.DbxUsers;
import com.dropbox.core.v2.DbxUsers.FullAccount;

import java.io.*;

import java.nio.file.Files;
import java.util.ArrayList;

public class uploadtoDropBox {

	public static void main(String[] args) throws ListFolderException, DbxException, IOException {
		// TODO Auto-generated method stub
		String res = uploadtoDropBox("dd","dd");
		System.out.println("complete");

	}

	public static String uploadtoDropBox(String UploadFilename, String filenameonly) throws ListFolderException, DbxException, IOException {
		
		final String ACCESS_TOKEN = "_seO61sIHdMAAAAAAAAEkk9CqRKb0dTxRESBiIJbxjnkcd1VzXV73hOkaRXjFARO";
		 DbxRequestConfig config = new DbxRequestConfig("dropbox/Public", "en_UK");
		 
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        System.out.println("dropbox file name =" + filenameonly);
        System.out.println("dropbox file name =" + UploadFilename);
        FullAccount account = client.users.getCurrentAccount();
        System.out.println(account.name.displayName);
        String publicURL ="";
		//System.out.println(filenameonly);
		//Users.FullAccount account = client.users.getCurrentAccount();
		
	
     // within the main method
        //InputStream in = new FileInputStream("/users/richardcurtis/QRCODEDAFevent/PrintableImage/4-gear.jpg");
		InputStream in = new FileInputStream(UploadFilename);
		FileMetadata metadata = client.files.uploadBuilder("/Public/"+filenameonly).run(in);
		//FileMetadata metadata = client.files.uploadBuilder("/Public/4-gear.jpg").run(in);
        boolean found;
        //System.out.println("d"+client.sharing.getSharedLinks("/Public/4-gear.jpg"));
        PathLinkMetadata myURL = client.sharing.createSharedLink("/Public/"+filenameonly);
        System.out.println("mY url is " + myURL.url);
       // ArrayList<LinkMetadata> xentries = client.sharing.getSharedLinks().links;
       // for (LinkMetadata xmetadata : xentries) {
       // 	System.out.println("xm"+xmetadata.url);
       // 	if (xmetadata.url.contains(filenameonly)){	
       // 		System.out.println("found the one ");
       // 		publicURL = xmetadata.url;
       // 	}
       // }
        //System.out.println("!" + entries.get(1));
        //entries.
      //  for (int a=0; a<entries.size() ;a++) {
      //  	found = Linkmetadata.url.contains(filename); //this method is what I want to know
      //  	if (found) {
      //  		System.out.println("x "+xmetadata.url);
      //  	}
      //  }
  //     System.out.println(client.sharing.getSharedLinks().links);
    //    System.out.println("media info " + metadata.mediaInfo.toString());
        
    //    System.out.println("dd name=" + metadata.id);
        
		// within the main method
	//	ArrayList<Metadata> entries = client.files.listFolder("").entries;
	//	for (Metadata metadata : entries) {
	//	    System.out.println(metadata.name);
	//	}
		return myURL.url;
	}
}
