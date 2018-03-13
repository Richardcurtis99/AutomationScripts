package com.adobe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
//import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class makeQRcodeGraphic {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String myGraphic = cnvTextToImg("abc","/users/richardcurtis/QRCODEDAFevent/QRcodeGraphics/","qrcode_gr1_code.png");
	}

	public static String cnvTextToImg(String shortURL, String location, String filename) throws IOException {
		//  width and height configuration
		
		int width = 800; 
        int height = 800;
        String fileType = "png";
        String barcodphysicalname = "qrcode_gr1_code.png";

		//BufferedImage image = null;
        //create String object to be converted to image
	      // String sampleText = shortURL;
	 
	        //Image file name
        // Output short URL as a graphic
	       String fName = "qrcode_gr1";
	       File file = new File(location + fName + "." + "jpg");
	        
	        //create a File Object
	       System.out.println(location + fName + ".jpeg");
	        File imageFile = new File(location + fName + ".jpeg");
	   
	         
	        //create the font you wish to use
	        Font font = new Font("Tahoma", Font.PLAIN, 11);
	        
	        //create the FontRenderContext object which helps us to measure the text
	        FontRenderContext frc = new FontRenderContext(null, true, true);
	         
	        //get the height and width of the text
	        Rectangle2D bounds = font.getStringBounds(shortURL, frc);
	        int w = (int) bounds.getWidth();
	        int h = (int) bounds.getHeight();
	        
	        //create a BufferedImage object
	       BufferedImage image = new BufferedImage(w, h,   BufferedImage.TYPE_INT_RGB);
	       
	    //   image = ImageIO.read(imageFile);
	       
	        //calling createGraphics() to get the Graphics2D
	        Graphics2D g = image.createGraphics();
	        
	        //set color and other parameters
	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, w, h);
	        g.setColor(Color.BLACK);
	        g.setFont(font);
	             
	       g.drawString(shortURL, (float) bounds.getX(), (float) -bounds.getY());
	       
	      //releasing resources
	      g.dispose();
	       
	        //creating the file 
	       ImageIO.write(image, "jpeg", imageFile);
	       
	       // Make Barcode
	       File barcodefile = new File(location+filename);
	       System.out.println("barcode file="+barcodefile);
	       //int size = 315;
	       int size = 375;
	        
	        try {
	        	QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        	BitMatrix byteMatrix = qrCodeWriter.encode(shortURL,BarcodeFormat.QR_CODE,size,size,null);
	          int barcodeWidth = byteMatrix.getWidth();
	         // System.out.println("barcode width" + barcodeWidth);
	          BufferedImage bimage = new BufferedImage(barcodeWidth,barcodeWidth,BufferedImage.TYPE_INT_RGB);
	          bimage.createGraphics();
	          
	          Graphics2D graphics = (Graphics2D) bimage.getGraphics();
	          graphics.setColor(Color.WHITE);
	          graphics.fillRect(0, 0, barcodeWidth, barcodeWidth);
	         graphics.setColor(Color.BLACK);
	          
	          for (int i=0; i< barcodeWidth;i++) {
	        	  for (int j=0;j<barcodeWidth;j++) {
	        		  if (byteMatrix.get(i, j)){
	        		  graphics.fillRect(i, j, 1, 1);
	        		//  System.out.println("here");
	        		  }
	        	  }
	          }
	         ImageIO.write(bimage, fileType, barcodefile);
	         System.out.println("short URL = " + shortURL);
	         System.out.println("QR code graphic done");
	        //	MatrixToImageWriter.writeToFile(bitMatrix, "PNG", file);
//	          MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(new File("/Users/rcurtis/Desktop/CQ5.5R212/CaptureEvent/ImageMagickWorkingFolder/myfile.png")));

	        } catch (WriterException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 

		return "done";
	 }
	
}
