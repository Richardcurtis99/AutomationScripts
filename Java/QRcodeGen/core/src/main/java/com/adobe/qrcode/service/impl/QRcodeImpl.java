package com.adobe.qrcode.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.adobe.qrcode.service.QRcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@Component()
@Service(value = QRcode.class)


public class QRcodeImpl implements QRcode {

 //   public  String createQRcode(String encodeString,String QRcodefileName) {
       public  String createQRcode(String encodeString) throws HttpException, IOException, ParserConfigurationException, SAXException {
        
    	   String destination = "https://richardcurtis.marketing.adobe.com/assetdetails.html/content/dam/mac/richardcurtis/aod/daf/4.png"; 
           
// Get Bit.ly code
           HttpClient httpclient = new HttpClient();
            HttpMethod method = new GetMethod("http://api.bit.ly/shorten");
            method.setQueryString(
                    new NameValuePair[]{
                            new NameValuePair("longUrl",encodeString),
                            new NameValuePair("version","2.0.1"),
                            new NameValuePair("login","richardcurtisadobe"),
                            new NameValuePair("apiKey","R_e4785235d82f7995a1bcd440dfe01f18"),
                            new NameValuePair("format","xml"),
                            new NameValuePair("history","1")
                            }
                    );
            httpclient.executeMethod(method);
            String responseXml = method.getResponseBodyAsString();
            String retVal = null;
            if(responseXml != null) {
                // parse the XML
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                StringReader st = new StringReader(responseXml);
                Document d = db.parse(new InputSource(st));
                NodeList nl = d.getElementsByTagName("shortUrl");
                if(nl != null) {
                    Node n = nl.item(0);
                    retVal = n.getTextContent();
                }
            }
            System.out.println("here");
            System.out.println("bit ly code =" + retVal);
     //  
            // write text to jpg for QRCode inclusion
            //create String object to be converted to image
        //       String sampleText = "SAMPLE TEXT";

               String fName = "DynBitly";
               String fpath = "d:/cqce/CQ5.5R212PROD/CaptureEvent/ImageMagickWorkingFolder/";
               
               File bitlyfile = new File(fpath + fName + "." + "jpg");
               System.out.println("rc1");

        //       ImageOutputStream fileName = (ImageOutputStream) bitlyfile;
                //Image file name
//             String fileName = "Image";
                
                //create a File Object
        //        File newFile = new File("d:/cqce/CQ5.5R212PROD/CaptureEvent/ImageMagickWorkingFolder/" + fileName + ".jpg");
        //         System.out.println("file name "+newFile);
                //create the font you wish to use
                Font font = new Font("Tahoma", Font.BOLD, 25);
                
                //create the FontRenderContext object which helps us to measure the text
                FontRenderContext frc = new FontRenderContext(null, true, true);
                 
                //get the height and width of the text
                Rectangle2D bounds = font.getStringBounds(retVal, frc);
                int w = (int) bounds.getWidth()+5;
                int h = (int) bounds.getHeight();
                
                //create a BufferedImage object
               BufferedImage image = new BufferedImage(w, h,   BufferedImage.TYPE_INT_RGB);
                
                //calling createGraphics() to get the Graphics2D
                Graphics2D g = image.createGraphics();
                
                //set color and other parameters
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, w, h);
                g.setColor(Color.BLACK);
                g.setFont(font);
                     
               g.drawString(retVal, (float) bounds.getX(), (float) -bounds.getY());
               
              //releasing resources
              g.dispose();
               
                //creating the file
               ImageIO.write(image, "jpg",bitlyfile);
               System.out.println("writing file");
        
          // 
        int width = 300; 
        int height = 300;
//        int width = 400; 
//        int height = 400;
        
        
        
        
       System.out.println("running qrcode");
       System.out.println("String to encode " + encodeString);
//       System.out.println("QRcode File name" + QRcodefileName);

//       String filePath = QRcodefileName;
 //      String filePath = "/Users/cqce/CQ5.5R212PROD/CaptureEvent/ImageMagickWorkingFolder/DYNQRCode.png";
         String filePath = "d:/cqce/CQ5.5R212PROD/CaptureEvent/ImageMagickWorkingFolder/DYNQRCode.png";

       File file = new File(filePath);
       BitMatrix bitMatrix;
        try {
            bitMatrix = new   QRCodeWriter().encode(retVal,BarcodeFormat.QR_CODE,width,height,null);
            MatrixToImageWriter.writeToFile(bitMatrix, "PNG", file);
//          MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("/Users/rcurtis/Desktop/CQ5.5R212/CaptureEvent/ImageMagickWorkingFolder/myfile.png")));

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        //
        
        return "QRCodeFinished";
    }

}
