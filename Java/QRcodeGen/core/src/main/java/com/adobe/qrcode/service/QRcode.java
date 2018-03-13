package com.adobe.qrcode.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;

public interface QRcode {

//	public String createQRcode(String encodeString,String QRcodefileName);
	public String createQRcode(String encodeString) throws HttpException, IOException, ParserConfigurationException, SAXException;
}
