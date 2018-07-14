	
	// deploy me to the runtime (need --web true to make public)
	// Create
	// wsk action create utils/postCCAssetToAEM postCCAssetToAEM.js --web true
	// Update
	// wsk action update utils/postCCAssetToAEM postCCAssetToAEM.js --web true

// 
// Function Objective - Take the Pre-signed URL and post to the AEM Servlet 
//

	var http = require("http"),
	https = require("https");

	var request = require('request');
	// Authorisation for AEM Servlet
	var username = "admin"
    var password = "admin"
    var auth = 'Basic ' + Buffer.from(username + ':' + password).toString('base64');
	// Global variables
	var GBLAemServer="";
	var GBLpresignedURL="";
	var GBLStop="NO";
	var bodyx="";
	
	// receive processing request from previous component in the chain
	// send JSON body to the AEM Servlet, body is multipart and contains the Pre-signed URL
	function main(params) {
		GBLStop = params.STOP;
		console.log("GBLStop="+GBLStop);
		// If not STOP, then post data to AEM
		if (GBLStop=="NO") {
			console.log("Params" + JSON.stringify(params));
			GBLAemServer = params.AEMServer;
			GBLpresignedURL = params.presignedURL;
			GBLcc_eventPath = params.cceventPath;
			var bodyJSON = {"URL" : GBLpresignedURL,"cc_eventPath" : GBLcc_eventPath};
			bodyx = JSON.stringify(bodyJSON);
			urlx = GBLAemServer +"/bin/aem64app/SimplePostAssettoAEM",
			
			console.log("GBLAemServer = "+GBLAemServer);
			console.log("GBLpresignedURL = "+GBLpresignedURL);
			console.log("bodyx = "+bodyx);
			// Set up AEM post options
			
			var options = {
					url : GBLAemServer +"/bin/aem64app/SimplePostAssettoAEM",
		    	    method: 'POST',
		    	    "headers": {	      
		    	        'authorization': auth,
		    	        'content-type': 'application/vnd.adobe.target.v1+json',    	        
		    	        },
		    	    "formData" : {
		    	    	 	"body" : JSON.stringify(bodyJSON)
				    	
		    	  }
				}
			
			//	Post data
			request.post(options, function optionalCallback(err, httpResponse, body) {
				if (err) {
					return console.error('upload failed:', err);
				}
				console.log('Upload successful!  Server responded with:', body);
			
  		});
	} else {
		console.log('Processing  was stopped due to agency not in play:');
	
	}
	}
	// Useful to call from a local node.js instance.
	//main()	
	