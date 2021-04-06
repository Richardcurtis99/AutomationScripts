// Clear java script console
        clearConsole();

        app.preferences.autoUpdateOpenDocuments=true;


    if (isOSX())
    {
        // House Keeping for wrunning the script on OSX
        // Get Relative Folder location. This ensures that the script can run anywhere, in particular when shared on Creative Cloud.
                // Ask user to select the processing file
        var relativePath = Folder.selectDialog ('Select a Processing Folder', function (f) { return (f instanceof Folder) } );

       // var relativePath = getRelativePath();
        
        $.writeln ( "Based from Relative Path = " + relativePath);
                

    }

    function clearConsole() {
        // Clear console
        var bt = new BridgeTalk();
        bt.target = 'estoolkit-4.0';
        bt.body = function(){
        app.clc();
        }.toSource()+"()";
        bt.send(5);
    
    }

    function isOSX()
{
    return $.os.match(/Macintosh/i);
}



 readCSVfortext(relativePath);

 ///////////////////////////////////////////////////////////////////////////////
// selectLayer - Select a layer
///////////////////////////////////////////////////////////////////////////////
function selectLayer(layerNameSL) {
    // B1_ProdName_Text 
    
    $.writeln("layer name="+layerNameSL);
    
    // =======================================================
var idselect = stringIDToTypeID( "select" );
var desc2030 = new ActionDescriptor();
var idnull = stringIDToTypeID( "null" );
    var ref22 = new ActionReference();
    var idlayer = stringIDToTypeID( "layer" );
    //ref22.putName( idlayer, "TEXTLAYER_1" );
    ref22.putName( idlayer, layerNameSL );
desc2030.putReference( idnull, ref22 );
var idmakeVisible = stringIDToTypeID( "makeVisible" );
desc2030.putBoolean( idmakeVisible, false );
var idlayerID = stringIDToTypeID( "layerID" );
    var list38 = new ActionList();
    list38.putInteger( 2 );
desc2030.putList( idlayerID, list38 );
executeAction( idselect, desc2030, DialogModes.NO );

  
    //return selectLayerCont;
    return;
}

    ///////////////////////////////////////////////////////////////////////////////
// readCSVfortext - Once a TEXT fodler is found, then the CSV file in there will be processed and data entered into the Textual elements.
///////////////////////////////////////////////////////////////////////////////
    
 function readCSVfortext(relativePath) {
    var lineCount=0;
    // locate and read each product text file
     var languageProcessing ="";
     

     $.writeln("***");
     //var csvFile = new File(relativePath+"/BT3021/TEXT/TextConfiguration.csv");
     var csvFile = new File(relativePath+"/data.csv");
     $.writeln(csvFile);
     if (csvFile != null)
    {
     csvFile.open('r');
     csvFile.seek(0, 0);
     csvFile.encoding = "UTF-8";
    
        while(!csvFile.eof)
        {
            var thisLine = csvFile.readln();
            //if (lineCount>0) {
            // Manage Breaks by Country, defined by the pipe deliiter
            // read each line
                var thisLine = csvFile.readln();
                $.writeln(thisLine);
                var layername = thisLine.split(',');
                var newvalue = thisLine.split(',');
                $.writeln("layername " + layername[1]);
                $.writeln("newvalue " + newvalue[2]);
                selectLayer(layername[1]);
                app.activeDocument.activeLayer.textItem.contents=newvalue[2];
           // }
         lineCount++;
        }
    }                                      
                
}
