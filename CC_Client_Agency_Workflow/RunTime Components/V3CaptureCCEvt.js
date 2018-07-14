// deploy me to the runtime (need --web true to make public)
// Create
// wsk action create V3CaptureCCEvt V3CaptureCCEvt.js --web true
// Update
// wsk action update V3CaptureCCEvt V3CaptureCCEvt.js --web true

// run me on the runtime - https://runtime.adobe.io/api/v1/web/rcurtis/default/V3CaptureCCEvt
// curl -X POST https://runtime.adobe.io/api/v1/web/rcurtis/default/V3CaptureCCEvt

// function objective - This function is the webhook that is defined on Adobe IO console.
// It receives the event payload

function main(params) {

	var querystring = require('querystring')
	http = require('http'),
	fs = require('fs'),
	openwhisk = require('openwhisk'),
	request = require('request'),
	https = require('https'),
	fs = require('fs'),
	
	require('url')
	// get the method from the open whisk event
	var method = params.__ow_method;
	
	// if get (which will be used as a validator from the Adobe IO registration
	// return the challenge parameter. This is used as a validator
	if (method=="get" ) {
		
		  rex = params.challenge;
	}
	// If post, then it came from the webhook and we need to process the event
	if (method=="post") {
			console.log("starting v3 capture CCE EVT v2");
			var ow = openwhisk();
			var rex='';
			console.log(params);
			// pick up content from the payload.
			var event = JSON.stringify(params.event['activitystreams:object']);
			var eventObj = params.event['activitystreams:object'];
			var eventPath = JSON.stringify(eventObj['xdmAsset:path']);
					
			eventPath = eventPath.substr(1,eventPath.length-2);
			var activityType = JSON.stringify(params.__ow_headers['x-adobe-event-code']);
			activityType = activityType.substr(1,activityType.length-2);
			
			// Start Sequence Actions - don't wait for this spawn to complete
			// Create the CC Assets payload, this will be sento the virtual action, which will process the 
			// event request, over the sequence of actions
			CCAsset = '{ "eventObj" : "'+ eventObj + '", "eventPath": "'+eventPath+ '"}';
			console.log("ccasset =" +CCAsset);
			CCAssetOBJ = JSON.parse(CCAsset);
			console.log("calling sequence action to process request");
			
			ow.actions.invoke({actionName: "V3ProcessCCEvtpayload",blocking:true,result:true, params: {CCAssetOBJ}});
			
			rex = "POST sent";
	}
				
	return {
		        statusCode: 200,
		        headers: { 'Content-Type': 'text/plain' },
		        body: rex,
	};

}
	