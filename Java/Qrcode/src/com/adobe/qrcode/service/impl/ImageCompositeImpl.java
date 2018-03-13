package com.adobe.qrcode.service.impl;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image; 
import java.awt.Graphics; 
//import java.awt.Component; 
import java.awt.Frame; 
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;

import com.adobe.qrcode.service.ImageComposite;

@Component()
@Service(value = ImageComposite.class)

public class ImageCompositeImpl implements ImageComposite {

    static Frame fr; 
    static 
    { 
        fr = new Frame(); 
        fr.pack(); 
    } 

	public String compImg(String BottomImage_Photo, String TopImage_QRCodeLocation,String logo) throws IOException {

		// logo will be  "Adobe"
		// or logo will be "QR", but only testing for QR

	/** 
	* Puts top image over bottom image. 
	* It paints top image on the bottom with zero offset. 
	* @param bottomImg  the image to be drawn on 
	* @param topImg     the image to draw on top of the bottom image 
	*/ 
			Image bottomImg=null;
			Image topImg=null;
			// Read from a file
//		    File file1 = new File("C:/temp/logo.jpg");
		    File file1 = new File(BottomImage_Photo);			
			bottomImg = ImageIO.read(file1);
//		    File file2 = new File("C:/temp/xx.png");
		    File file2 = new File(TopImage_QRCodeLocation);
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

	       	return "Image Comp OK";    
    	}
}

