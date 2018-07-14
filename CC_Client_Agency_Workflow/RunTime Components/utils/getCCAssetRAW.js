
// deploy me to the runtime (need --web true to make public)
// Create
// wsk action create utils/getCCAssetRAW getCCAssetRAW.js --web true
// Update
// wsk action update utils/getCCAssetRAW getCCAssetRAW.js --web true

// run me on the runtime - https://runtime.adobe.io/api/v1/web/rcurtis/default/getCCAssetRAW
// curl -X POST https://runtime.adobe.io/api/v1/web/rcurtis/default/getCCAssetRAW
	
// 
// Function Objective - To get the Pre-signed URL from CC Storage API. This is based on the 
// event path from the CC event payload


var request = require('request-promise');
const url = require('url');
var cc_eventPath = "";
var bearer = "";
var xAPIKey = "";
var CCAssetOBJ = "";
var AEMServer="";
var approvedFolderForAEM ="";

// Function to pick up the pre-signed URL from CC server. EventPath is from the cc event payload
var getCCRaw = {
		  token: null,
		  getCCRawfunc: function() {
				    return request({
				      "method":"GET", 
				      "uri": "https://cc-api-storage.adobe.io"+cc_eventPath+"/:raw",
				      "json": true,
				    
				      "headers": {
						'X-Request-Id': '123',
//						//'Authorization':'Bearer xxx bearer key',
						'Authorization': "Bearer " + bearer,
						'Accept': 'application/vnd.adobe.s3presignedurl+json',     
//						//'X-Api-Key': 'x-api key'
						'X-Api-Key': xAPIKey
				    	}
				    
				  })
				}
}

// To process the pre-signed URL and paramters for the next function in the sequence of actions 		  
var getCCRawProcess = {
		  token: null,	  
		   ProcessRaw: function(data) {
		   console.log("data="+JSON.stringify(data));
		   var ret = {"AEMServer" : AEMServer,
				   "presignedURL" : data.presigned_href}
		   console.log("ret="+JSON.stringify(ret));
		   return {
		   	AEMServer : AEMServer,
		   	presignedURL : data.presigned_href,
		   	cceventPath : cc_eventPath,
		   	STOP : "NO"
		   	}
	}
}

// main function. Receives the payload from the CC Event.
function main(params) {
	
	console.log("Raw Assets Parms =" +JSON.stringify(params));
	xAPIKey = params.config.clientID;
	console.log("client id = " + xAPIKey);
	bearer = params.config.bearer;
	console.log("bearer = " + bearer);
	CCAssetOBJ = params.params.CCAssetOBJ
	console.log("Raw Assets Parms =" +JSON.stringify(CCAssetOBJ));
	cc_eventPath = params.params.CCAssetOBJ.eventPath;
	console.log("cc_eventPath = " + cc_eventPath);
	AEMServer = params.config.AEMserver;
	console.log("AEM Server " + AEMServer);
	//approvedFolderForDAM processing
	approvedFolderForAEM = params.config.approvedFolderForDAM;
	console.log("approvedFolderForAEM " + approvedFolderForAEM);
	
	// before the function accepts the CC event, need to make sure that the agency from the event path filename
	// is in the agencies in play array in the configuration file (on S3)
	//
	// make sure it's an in play agency
	// split event path to get the second position
		var agencyOnEventPath = cc_eventPath.split("/");
		console.log("agency on event path="+agencyOnEventPath.length);
		// get agencies in play JSON Object
		console.log("agency in CC Event ="+agencyOnEventPath[2]);
		var agenciesInPlay= params.config.agenciesInPlay;
		console.log("agenciesInPlay="+agenciesInPlay);
		console.log("agenciesInPlay.length="+agenciesInPlay.length);
		var agenciesInPlayArr =agenciesInPlay.length;
		var campaignName = agencyOnEventPath[3];
		var approvedFolderName = agencyOnEventPath[4];
		console.log("approvedFolderName="+approvedFolderName);
			// Is the supplied agency in play (position 0) of the event path
			var currAgencyInPlay=false;
			for (i = 0; i <= agenciesInPlay.length + 1; i++) {
				if (agenciesInPlay[i] == agencyOnEventPath[2]){
					console.log("agency "+agencyOnEventPath[2]+", is in play, position " + i )
					currAgencyInPlay=true;
				}
	    }
		// Is the event from the approved folder (matched with the configuration file approvedFolderForAEM data
		 var approvedAssetFolder=false;
		 if (approvedFolderName==approvedFolderForAEM) {
			 approvedAssetFolder=true;
		 }
		
		 // If agency is ok and the request is from an approved folder, then process. otherwise stop.
		 
		 if (currAgencyInPlay && approvedAssetFolder ) {
			 return getCCRaw.getCCRawfunc()
			 .then(getCCRawProcess.ProcessRaw);
		 } else {
			 return {"STOP" :"YES"};
		 }

}
