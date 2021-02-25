/*
 * Copyright 2019 Adobe
 * All Rights Reserved.
 *
 * NOTICE: Adobe permits you to use, modify, and distribute this file in
 * accordance with the terms of the Adobe license agreement accompanying
 * it. If you have received this file from a source other than Adobe,
 * then your use, modification, or distribution of it requires the prior
 * written permission of Adobe.
 */




import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.adobe.platform.operation.ExecutionContext;
import com.adobe.platform.operation.auth.Credentials;
import com.adobe.platform.operation.exception.SdkException;
import com.adobe.platform.operation.exception.ServiceApiException;
import com.adobe.platform.operation.exception.ServiceUsageException;
import com.adobe.platform.operation.io.FileRef;
import com.adobe.platform.operation.pdfops.CreatePDFOperation;
import com.adobe.platform.operation.pdfops.ExportPDFOperation;
import com.adobe.platform.operation.pdfops.OCROperation;
import com.adobe.platform.operation.pdfops.options.exportpdf.ExportPDFTargetFormat;

/**
 * This sample illustrates how to export a PDF file to a Word (DOCX) file
 * <p>
 * Refer to README.md for instructions on how to run the samples.
 */
public class MG_ExportImageToPDF {

    // Initialize the logger.
    private static final Logger LOGGER = LoggerFactory.getLogger(MG_ExportImageToPDF.class);

    public static void main(String[] args) throws IOException, ServiceUsageException, ServiceApiException {

        {
        	
            // Initial setup, create credentials instance.
            Credentials credentials = null;
			try {
				credentials = Credentials.serviceAccountCredentialsBuilder()
				        .fromFile("pdftools-api-credentials.json")
				        .build();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //Create an ExecutionContext using credentials and create a new operation instance.
			ExecutionContext executionContext = ExecutionContext.create(credentials);
			// Set operation input from a source file.
			
			//  Convert image to PDF
			FileRef source = FileRef.createFromLocalFile("/Users/richardcurtis_Production/Desktop/MGTesting/images/-our-approach-tablet.png");
			CreatePDFOperation createPdfOperation = CreatePDFOperation.createNew();
			createPdfOperation.setInput(source);
			  
			// Execute the operation.
			FileRef PDFCRTresult = createPdfOperation.execute(executionContext);

			// OCR
				OCROperation ocrOperation = OCROperation.createNew();
				ocrOperation.setInput(PDFCRTresult);

			   // Execute the operation
			   FileRef PDFOCRresult = ocrOperation.execute(executionContext);
			   
            // Save the result to the specified location.
			
			
			
			   PDFOCRresult.saveAs("/Users/richardcurtis_Production/Desktop/MGTesting/output/-our-approach-tablet.PDF");
        }
    }
}
