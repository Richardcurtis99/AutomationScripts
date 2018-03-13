package com.adobe.qrcode.service;

import java.io.IOException;


public interface ImageComposite {

	public String compImg(String BottomImage_Photo, String TopImage_QRCodeLocation,String logo) throws IOException;

}

