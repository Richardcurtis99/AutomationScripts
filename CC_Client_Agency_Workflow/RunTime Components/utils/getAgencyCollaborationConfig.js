
// deploy me to the runtime (need --web true to make public)
// Create
// wsk action create utils/getAgencyCollaborationConfig getAgencyCollaborationConfig.js --web true
// Update
// wsk action update utils/getAgencyCollaborationConfig getAgencyCollaborationConfig.js --web true

// run me on the runtime - https://runtime.adobe.io/api/v1/web/rcurtis/default/getAgencyCollaborationConfig
// curl -X POST https://runtime.adobe.io/api/v1/web/rcurtis/default/getAgencyCollaborationConfig

// function objective - This function reads the configuration from S3 and sends parts of to the next action in the 
// sequence.

var request = require('request-promise');
const url = require('url');

var bodyx=null;

var agencyConfig = {
  token: null,
  
  getConfig: function() {
    return request({
      "method":"GET", 
      "uri": "https://s3.amazonaws.com/rcb1/AgencyCollaborationConfig/FolderStructure_prod.json",
      "json": true,
    }
    )
  }
}

var GBLParams;
var xapikey;
var bearer;
var agencyConfig2 = {
		  token: null,
		  
		   processResult: function(data) {
		   console.log("data="+JSON.stringify(data));
		   return { 
			   params : GBLParams,
			   config : data
		  }
		}
}

function main(params) {
	GBLParams = params;
	return agencyConfig.getConfig()
		.then(agencyConfig2.processResult);
}

//main()
