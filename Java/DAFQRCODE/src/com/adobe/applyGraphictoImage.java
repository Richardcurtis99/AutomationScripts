package com.adobe;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;


public class applyGraphictoImage {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Test harness
		String BottomImage_Photo = "/users/richardcurtis/QRCODEDAFevent/monitorfolder/2.png";
		String TopImage_QRCodeLocation = "/users/richardcurtis/QRCODEDAFevent/QRcodeGraphics/2.png";
		String outputLocation = "/users/richardcurtis/QRCODEDAFevent/PrintableImage/print.jpg";
		String res = compImg(BottomImage_Photo, TopImage_QRCodeLocation,outputLocation,"2.png");
	}
	// fr is a graphic container.
    static Frame fr; 
    static 
    { 
        fr = new Frame(); 
        fr.pack(); 
    } 

	public static String compImg(String BottomImage_Photo, String TopImage_QRCodeLocation,String outputLocation, String filename) throws IOException {

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
			Image whiteRectImg=null;
			Image newImg = null;
			Image adobeRedSash = null; 
			
			// Read from a file
//		    File file1 = new File("C:/temp/logo.jpg");
// temp code
			System.out.println("** Start");
			File file1 = new File(BottomImage_Photo);			
			bottomImg = ImageIO.read(file1);
			//boolean bottomImgRotate = (ImageIO.read(file1).getWidth()>ImageIO.read(file1).getHeight());
			//System.out.println("Bottom Image Width="+ImageIO.read(file1).getWidth());
			//System.out.println("rotate image?="+bottomImgRotate);
			BufferedImage exifRotationBottomImage = Thumbnails.of(BottomImage_Photo).useExifOrientation(true).scale(1).asBufferedImage();
			//BufferedImage ximage = Thumbnails.of(BottomImage_Photo).scale(1).asBufferedImage();
			System.out.println("thumbnator width " +exifRotationBottomImage.getWidth());
			System.out.println("thumbnator height " +exifRotationBottomImage.getHeight());
			System.out.println("** END");

//			File file1 = new File(BottomImage_Photo);			
//			bottomImg = ImageIO.read(file1);
			System.out.println("Height = " + ImageIO.read(file1).getHeight());
			System.out.println("Width = " + ImageIO.read(file1).getWidth());
//		    File file2 = new File("C:/temp/xx.png");
		    File file2 = new File(TopImage_QRCodeLocation);
		    topImg = ImageIO.read(file2);
		    File whiteRectangle = new File("/users/richardcurtis/QRCODEDAFevent/BaseTemplateAssets/WhiteRectangle.png");
		    whiteRectImg = ImageIO.read(whiteRectangle);
		    File file3 = new File("/users/richardcurtis/QRCODEDAFevent/BaseTemplateAssets/Adobe_logo_tag_top_7_8in_rgb_ai 2.png");
		    adobeRedSash = ImageIO.read(file3);
		    
	    
	    	System.out.println("Creating Canvas");
	    	Canvas c = new Canvas();

	   // 	System.out.println("Bottom Image Width="+bottomImg.getWidth(fr));
	   //     System.out.println("Bottom Image Height="+bottomImg.getHeight(fr));
	    	System.out.println("Top Image Width="+topImg.getWidth(fr));
	        System.out.println("Top Image Height="+topImg.getHeight(fr));

	               
//	    	BufferedImage img = new BufferedImage(bottomImg.getWidth(fr), bottomImg.getHeight(fr), BufferedImage.TYPE_INT_RGB);
	        BufferedImage img = new BufferedImage(exifRotationBottomImage.getWidth(fr),  exifRotationBottomImage.getHeight(fr), BufferedImage.TYPE_INT_RGB);
	        img.createGraphics(); 
	    	Graphics2D g = (Graphics2D)img.getGraphics();  
	        //g.drawImage(bottomImg, 0, 0, fr);
	    	g.drawImage(exifRotationBottomImage, 0, 0, fr);
	        // Add background White Image to the image
	        g.drawImage(whiteRectImg, 0, exifRotationBottomImage.getHeight(fr)-whiteRectImg.getHeight(fr), fr);
	        // Add QRCode to the image
//	        g.drawImage(topImg, bottomImg.getWidth(fr)-topImg.getWidth(fr), bottomImg.getHeight(fr)-topImg.getHeight(fr), fr);
	        g.drawImage(topImg, exifRotationBottomImage.getWidth(fr)-topImg.getWidth(fr), exifRotationBottomImage.getHeight(fr)-topImg.getHeight(fr), fr);
	       	//System.out.println("Write file to disk="+ outputLocation);
// 	Apply Adobe Red Sash
	        int calc = adobeRedSash.getWidth(fr)-topImg.getWidth(fr)-20;
	        System.out.println("red sash ="+calc);
	        System.out.println("total width="+topImg.getWidth(fr));
	        g.drawImage(adobeRedSash, exifRotationBottomImage.getWidth(fr)-adobeRedSash.getWidth(fr)-50, 0, fr);
	        	
	       	ImageIO.write(img, "JPG", new File(outputLocation+filename));
	        //ImageIO.write(img, "jpg", new File("/users/richardcurtis/QRCODEDAFevent/PrintableImage/print.jpg"));  
	        
	        g.dispose(); 
	       	System.out.println("newImg");
	        System.out.println("Image Processing Done");

	       	return "Image Comp OK";    
    	}
}
