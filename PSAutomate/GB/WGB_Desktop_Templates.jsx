﻿        //BT3021 - Canvas size H3415 W1267        //BT 7040 - Canvas size H3367 W1267        #include "RCFunctions_WGB.jsx" // RC Functions ignore line        // Clear java script console        clearConsole();        app.preferences.autoUpdateOpenDocuments=true;        // Set up global variables        var coreLocation = "";        var outputLocation = "";        var currentLayerSet=null;        var shaverType=null;        var campaignName=null;        var shaverTypeProcessed=0;        var campaignNameProcessed=0;        var currentFolderLevel=0;        var stdCanvasH0 = 3415;        var M1Position = 802; // std        var M1Position = 891; // 3042        var M2Position = 1008; //7040        var leading = 20;        // need to move the postion of LowerHalf M1 position if there are a lot of text lines > 25 lines                // set up global array for layer names in a document        var PNG_MAX = 0;    if (isOSX())    {        // House Keeping for wrunning the script on OSX        // Get Relative Folder location. This ensures that the script can run anywhere, in particular when shared on Creative Cloud.                // Ask user to select the processing file        var relativePath = Folder.selectDialog ('Select a Processing Folder', function (f) { return (f instanceof Folder) } );       // var relativePath = getRelativePath();                $.writeln ( "Based from Relative Path = " + relativePath);                    }    ///////////////////////////////////////////////////////////////////////////////    /// Main Line Processing    /// remove Last times output and set up new folder    // Output will be routed to a Process Output folder, this will be at the relative location of the script    var processedOutputName = "ProcessedOutput";    var processedOutputLocation = relativePath + "/"+processedOutputName;    //makes sure that the output folder and it's contents from the last run are remvoed , before new contents are created.    // remove contents from the processed output folder    deleteFolderAndContents(processedOutputLocation);    // Re-create output folder    createFolder(processedOutputLocation)    $.writeln("processed output = "+processedOutputLocation);    ///////////////////////////////////////////////////////////////////////////////    // Read all layers in PSD, used to make sure that a layer exists before it's contents are replaced    loadLayersHarness(activeDocument);    shaverTypeProcessed=0;      // make sure that lswitacble layers are alll off (these are the 3 wide and 4 wide.    // Turn off any layers that have 3 or 4 underscores, these are dynaimc sections of the template and will be determined by the data from the folder structure.    initVariableWLayersOFF();    /// Read disk files using the defined folder structure    // Process Graphics    $.write("----> read folder and contents " + relativePath);    readFolderAndContents(relativePath); //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ReadFolderAndContents- Function reads the file systems and looks for shaver types to process///////////////////////////////////////////////////////////////////////////////function readFolderAndContents(folderName) {         // Get a list of files and folders         // This shuold always start from the folder "Core_Process", then the shaver types wil break within         var aChildren = Folder(folderName).getFiles();        var aProcessedOBJ=0;                 // read files and folders, but we only intested in folders         for (var i = 0; i < aChildren.length; i++) {                                    var child = aChildren[i];            // don't process the processed output folder name            if (child.name != processedOutputName) {            ///////////////////////////////////////////////////////////////////////////////            // Read contents of folder                if (child instanceof Folder) {                    currentFolderLevel++;                      $.writeln ( "----------------->--------------Folder Name,"+child.name+ "Current Folder Level =" +currentFolderLevel);                                           // manage change of shaver type , as long as the folder chaild level is 2. This is used to segement the campaigns                       if (shaverTypeProcessed==0 && currentFolderLevel==2){                            // turn off any switchable layers                            initVariableWLayersOFF();                                // set the shaver type in process and that a shaver is being processed                            shaverTypeProcessed=1;                            shaverType=child.name;                      }                    // Manage the campain break level, but this must be the first level only                    if (campaignNameProcessed==0 & currentFolderLevel==1) {                        // Process each camapign                         // set the campaign type and the campain name for use layer (i.e. on the output file (PSD and JPG) processing                        campaignNameProcessed=1;                        campaignName=child.name;                    }                    ///////////////////////////////////////////////////////////////////////////////                    // manage layer visibility, if a folder name is found that has 3 underscores, then it relates to a layer visibility controlled area (see if it's a 3 wide or 4 wide). This function call                     // will turn the correct layer on.                                        var widthVersion = child.name.split('_');                     $.writeln("========= childname version " +child.name)                    $.writeln("========= childname length " +child.name.split('_').length)                    if (child.name.split('_').length==3) {                        switchLayerVisibility(child.name,"ON");                    }                                        ///////////////////////////////////////////////////////////////////////////////                    // If script finds the TEXT folder, then Process the Text Processing.                    if (child.name == "TEXT") {                        $.writeln("Procesing TEXT CSV file, folder name=" +folderName+",child name "+ child.name);                        $.writeln("    ");                            readCSVfortext(shaverType,folderName + "/" + child.name);                        }                                        // store current folder name, as will need this to find the layer (folder name as a prefix)                    currentLayerSet = child.name;                    // read next folder in the set                    readFolderAndContents(folderName+"/"+child.name);                    // Once the folder has been read, then reduce the folder level by one.                    currentFolderLevel--;                                      // if not folder, then must be a file, process the files and relink back to the PSD layer                 } else {                     // process if not a fodler -> must be a file                     // don't process any temporary files                     if (child.name != ".DS_Store") {                         ///////////////////////////////////////////////////////////////////////////////                        // split the child name                        // The file name is in two parts, number and name. The number is used in combination with the folder name. This provides a way for the folder structure to be self describing for easy population in the PSD. This way                        // we don't need any type of mapping file                        var splitvalue = child.name.split('_');                         $.writeln("Target Layer in Photoshop= " + currentLayerSet+"_"+splitvalue[0]);                        var PSLayerName = currentLayerSet+"_"+splitvalue[0];                            $.writeln("PS Layer Name="+PSLayerName);                            // ensure that if a file name is found, then a mathcing PSD layer exists, if not then don't process                            if (findMatchInlayerNamesGlobalArray(PSLayerName) >0) {                                $.writeln("Layer found in PS ="+PSLayerName);                                $.writeln("File name ="+child.name);                                // if all ok, then relink the file to the PSD                                 // TODO, should ideally test to see what the extension is (only process PSD, PNG, TIFF or JPG create an array to store these centrally.                                reLinkToFile(PSLayerName,child.path, child.name);                        }                  }                                                         }               // once all files in a folder have been processed, then create the JPG output and reset the next shaver break                   if (currentFolderLevel==1 && shaverType != null) {                    // Create Output PSD and JPG for each campaign / shaver type                    saveAs(campaignName+"_"+shaverType+"_Working.PSD", processedOutputLocation+"/")                    saveAsJPG(campaignName+"_"+shaverType+"_ForReview.jpg", processedOutputLocation+"/");                    $.writeln("shaverType processing for "+shaverType+", complete");                    // force Shaver Type CHange for next type                    shaverTypeProcessed=0;               }              // Manage campaign name change              if (currentFolderLevel==0 && campaignName != null) {                  $.writeln("campaign name changed "+campaignName+", complete");                  campaignNameProcessed=0;              }                          //not processedOutputName           }                     // main loop           }    // function end }    ///////////////////////////////////////////////////////////////////////////////// readCSVfortext - Once a TEXT fodler is found, then the CSV file in there will be processed and data entered into the Textual elements.///////////////////////////////////////////////////////////////////////////////     function readCSVfortext(shaverType,relativePath) {    var lineCount=0;    // locate and read each product text file     var sectionProcessing ="";     var sectionLines=new Array();     var sectionLinesFont=new Array();     var sectionLinesWeight=new Array();     var sectionLinesSize=new Array();     var sectionLinesColour =new Array();     var lineDetails = new Array();     var linesWithNewLine = new Array();     var sectionText="";     var textStartIndex=0;     var chgFlag=false;     var sectionLinesChg="";     var BulletSpaces="";     var BulletSpacesNumber=0;     var endBulletSpacesNumber=0;     var startBulletSpacesNumber=0;     var lengthStringToChange=0;     var addExtraSpace="";     var numberOfHEntries=0     var DentryFound=false;     var opacityValue=100;     $.writeln(relativePath+"/"+shaverType+"_"+"TextConfiguration.csv");     $.writeln("***");     //var csvFile = new File(relativePath+"/BT3021/TEXT/TextConfiguration.csv");     var csvFile = new File(relativePath+"/"+shaverType+"_"+"TextConfiguration.csv");          if (csvFile != null)    {     csvFile.open('r');     csvFile.seek(0, 0);     csvFile.encoding = "UTF-8";            while(!csvFile.eof)        {          // Manage Breaks by Country, defined by the pipe deliiter          // read each line         var thisLine = csvFile.readln();         // processing headings         if (thisLine.split(",")[0]=="H" || thisLine.split(",")[0]=="D" ) {            $.writeln("here procesing qty, dentry="+DentryFound);                if (thisLine.split(",")[0]=="D") {                                                       DentryFound=true;                    // process opacity value                    $.writeln("thisLine.split(5)=" +thisLine.split(",").length);                    if (thisLine.split(",").length==7)  {                    // if there is an opactity value, then set the opactity of the layer                        $.writeln("opacity value="+thisLine.split(",")[5]);                         if (thisLine.split(",")[5]>0) {                            $.writeln("thisLine.split[5]="+thisLine.split(",")[5]);                            $.writeln("temp");                            setLayerOpacity (thisLine.split(",")[1], thisLine.split(",")[5])                            setColourOverlay(thisLine.split(",")[1], thisLine.split(",")[6])                          }                    }                                                    // if (opacity value is found)                // setLayerOpacity (oplayerName, opValue)                };                                                                                                                if (lineCount>0)  {                                    // process last set of records                                     deSelectAllLayers();                                    // TODO change this section to call the select layer function                                   // Preselct the layer that we will be updating with the text from the CSV file.                                   var textLayerExists = selectLayer(sectionProcessing);                                   $.writeln("textLayerExists="+textLayerExists);                                   if (textLayerExists) {                                            sectionText="";                                            linesWithNewLine=[];                                            $.writeln("sectionLines.length="+sectionLines.length);                                                                                for (textIDX=0;textIDX<sectionLines.length;textIDX++) {                                                chgFlag=false;                                                sectionLinesChg="";                                                // replace special chars in the string                                                        $.writeln("========-> "+sectionLines[textIDX]);                                               // Manage Special Chars                                               // replace tilda with a comma                                               if (sectionLines[textIDX].indexOf("~")>-1) {                                                        sectionLinesChg=changeSpecialChar(sectionLines[textIDX],"~",",");                                                        chgFlag=true;                                              }                                              // replace up arrow with appostrophe                                              if (sectionLines[textIDX].indexOf("^")>-1) {                                                        sectionLinesChg=changeSpecialChar(sectionLines[textIDX],"^","'");                                                        chgFlag=true;                                             }                                             // replace special chars BULLET in the string with a bullet point                                             if (sectionLines[textIDX].indexOf("BULLET")>-1) {                                                        sectionLinesChg=changeSpecialChar(sectionLines[textIDX],"BULLET","•");                                                        // if number of spaces is specific using &n| notation, then add number of spaces specified by the number                                                        if ( sectionLinesChg.indexOf("&")>-1){                                                            endBulletSpacesNumber = sectionLinesChg.indexOf("|");                                                            startBulletSpacesNumber =sectionLinesChg.indexOf("&");                                                            BulletSpacesNumber=parseInt(sectionLinesChg.substr(startBulletSpacesNumber +1,endBulletSpacesNumber));                                                            BulletSpaces="";                                                            for (makeSpaces=0;makeSpaces<BulletSpacesNumber;makeSpaces++) {                                                                BulletSpaces=BulletSpaces+" ";                                                            }                                                            $.writeln("sectionLinesChg ="+sectionLinesChg);                                                            sectionLinesChg=changeSpecialChar(sectionLinesChg,sectionLinesChg.substr(startBulletSpacesNumber,endBulletSpacesNumber ),BulletSpaces);                                                            $.writeln("sectionLinesChg ="+sectionLinesChg);                                                            $.writeln("====== =");                                                        }                                                        // set the change text flag as true, as special procoessing below needs to be applied                                                        chgFlag=true;                                             }                                                                                       // replace special chars DOT in the string                                              linesWithNewLine.push(textIDX);                                             // If the change flag is on, then the text has been changed due to a special character, therefore, take the text from the change data and not the original array.                                             if (!chgFlag) {                                                                                            sectionText=sectionText+sectionLines[textIDX]+String.fromCharCode(13);                                                $.writeln("sectionText1="+sectionText);                                                                                                     } else {                                                 sectionText=sectionText+sectionLinesChg+String.fromCharCode(13);                                                 //sectionText=sectionText+sectionLinesChg;                                                 sectionLines[textIDX]=sectionLinesChg;                                                 $.writeln("=======sectionLinesChg="+sectionLinesChg);                                                 $.writeln("=======sectionText="+sectionText);                                                 $.writeln("sectionText2="+sectionLines[textIDX]);                                              }                                          }                                                                          ///////////////////////////////////////////////////////////////////////////////                                          // Set the text in the PSD document                                          for (findx=0;findx< linesWithNewLine.length;findx++) {                                                $.writeln("findx="+ linesWithNewLine[findx]+", sectionLines="+sectionLines[findx]);                                          }                                          // update the pre-selected layer                                          app.activeDocument.activeLayer.textItem.contents=sectionText;                                          // Start Text Formatting                                          textStartIndex=0;                                                                                                                         for (textIDX2=0;textIDX2<sectionLines.length;textIDX2++) {                                          addExtraSpace=false;                                          if (sectionLines[textIDX2]!="")  {                                                for (srchIdx=0;srchIdx<linesWithNewLine.length;srchIdx++) {                                                    if (linesWithNewLine[srchIdx]==textIDX2) {                                                        $.writeln("index found ="+textIDX2);                                                        addExtraSpace=true;                                                    };                                          }                                                                                ///////////////////////////////////////////////////////////////////////////////                                          // Format the text, but only after it's in the document.                                           //setFormatting(0, 4, "Tekton Pro", "Bold Condensed", 48, new Array(0, 0, 0),20);                                          // 15 +41 = 56                                                                              lengthStringToChange = textStartIndex+sectionLines[textIDX2].length;                                          $.writeln("lengthStringToChange="+lengthStringToChange);                                          var tempVar =                                           $.writeln("setFormatting = "+"setFormatting("+textStartIndex+", "+sectionLines[textIDX2].length+","+ sectionLinesFont[textIDX2]+", "+sectionLinesWeight[textIDX2]+", "+sectionLinesSize[textIDX2]+"colour=" +sectionLinesColour[textIDX2] +"+leading+");                                          var colourArray=[];                                          ////    if (sectionLinesColour[textIDX2]!="white"){colourArray(0, 0, 0)} else {colourArray(255, 255, 255)};                                          setFormatting(textStartIndex, sectionLines[textIDX2].length, sectionLinesFont[textIDX2], sectionLinesWeight[textIDX2], sectionLinesSize[textIDX2], new Array(0,0,0),leading);                                          // textStartIndex=textStartIndex+sectionLines[textIDX2].length+1;                                          textStartIndex=textStartIndex+sectionLines[textIDX2].length;                                          if (addExtraSpace) {textStartIndex++};                                        }                                    }                                }                            }                            ///////////////////////////////////////////////////////////////////////////////                            // reset the arrays for the next set of lines                            //         sectionProcessing=lineDetails[0];                            sectionLines=[];                            sectionLinesFont=[];                            sectionLinesWeight=[];                            sectionLinesSize=[];                            sectionLinesColour=[];                            sectionProcessing=thisLine.split(",")[1];                            $.writeln("processing section "+sectionProcessing);                                                        // process sections                            } else {                                lineCount++;                                // process data                                lineDetails = thisLine.split(",");                                // store lines in the arrays for text, font and size                                sectionLines.push(lineDetails[1]);                                sectionLinesFont.push(lineDetails[2]);                                sectionLinesWeight.push(lineDetails[3])                                sectionLinesSize.push(lineDetails[4]);                                sectionLinesColour.push(lineDetails[5])                           if (!DentryFound) {                                                                numberOfHEntries++;                                $.writeln("numberOfHEntries" + numberOfHEntries);                                $.writeln("++++++");                           }                    }              // end  of H or W processing                           }        }            // adjust height of LowerSection, based on if the number of H entries goes over 20 number of H lines        $.writeln("numberOfHEntries="+numberOfHEntries);         if (numberOfHEntries>21) {              moveLayer("LowerHalf",0,150);         }}        