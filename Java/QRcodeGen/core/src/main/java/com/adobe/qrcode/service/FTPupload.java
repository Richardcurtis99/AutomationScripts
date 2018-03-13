package com.adobe.qrcode.service;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FTPupload {

	public String ftp(String servername,String username,String password,String filename,String originalfilename) throws MalformedURLException, IOException;
	public String TalktoMe();
}

