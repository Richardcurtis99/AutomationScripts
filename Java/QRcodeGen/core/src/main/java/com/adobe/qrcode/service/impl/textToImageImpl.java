package com.adobe.qrcode.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.qrcode.service.textToImage;

@Component()
@Service(value = textToImage.class)

public class textToImageImpl implements textToImage {
	
	public String cnvTextToImg(String textToConvert) throws IOException {

        //create String object to be converted to image
	       String sampleText = "SAMPLE TEXT";
	 
	        //Image file name
	       String fName = "savingAnImage";
	       File file = new File(fName + "." + "jpg");
	       ImageOutputStream fileName = (ImageOutputStream) file;
	        
	        //create a File Object
	        File newFile = new File("./" + fName + ".jpeg");
	         
	        //create the font you wish to use
	        Font font = new Font("Tahoma", Font.PLAIN, 11);
	        
	        //create the FontRenderContext object which helps us to measure the text
	        FontRenderContext frc = new FontRenderContext(null, true, true);
	         
	        //get the height and width of the text
	        Rectangle2D bounds = font.getStringBounds(sampleText, frc);
	        int w = (int) bounds.getWidth();
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
	             
	       g.drawString(sampleText, (float) bounds.getX(), (float) -bounds.getY());
	       
	      //releasing resources
	      g.dispose();
	       
	        //creating the file
	       ImageIO.write(image, "jpeg", fileName);
		return "done";
	 }
}

