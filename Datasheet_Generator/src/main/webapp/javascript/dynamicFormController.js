    function readConfig(){

    var files = document.getElementById('files').files;
    if (!files.length) {
        alert('Please select a file!');
        return;
    }else{
        $('#loading').show();
    }

        var file = files[0];
        var start = 0;
        var stop = file.size;

        var reader = new FileReader();

        reader.onloadend = function(evt){
            if (evt.target.readyState == FileReader.DONE) {
                $('#config').addClass("hidden");
                var info = evt.target.result.trim();

                var line, headerStart, headerEnd, i;
                var configArray = info.split("\n");
                for (i = 0; i < configArray.length; i++) {
                    line = configArray[i];

                    if(line.indexOf("<header>") != -1){
                        headerStart = i;                        
                    }

                    if(line.indexOf("</header>") != -1){
                        headerEnd = i;
                    }
                }

                var header = [""];

                for(i = headerStart; i < headerEnd + 1; i++)
                {
                    header.push(configArray[i]);
                }

                for(i = 0; i < configArray.length; i++) {
                    if(i < headerStart || i > headerEnd){
                        header.push(configArray[i]);
                    }
                }

                info = header.join("\n");
                info = info.replace(new RegExp("<header>","g"),"<well>");
                info = info.replace(new RegExp("</header>","g"),"</well>");

                idString = info;
                info = info.toLowerCase().replace(new RegExp("<well>","g"),"");
                var count = info.match(/\/well/g);
                info = info.split("</well>");
                for (var i = 0; i < count.length; i++) {
                    var well = info[i];
                    var start = well.indexOf("<title>") + 7;
                    var stop = well.indexOf("</title>");
                    if (start == -1 || stop == -1){
                        alert("You must choose a title for each well!");
                        return;
                        // $('#config').removeClass("hidden");
                        // $('#config').show();
                        break;
                    }
                    var titleString = well.substring(start,stop);
                    titleString = titleString.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                    var camelTitle = (titleString.substr(0,1).toLowerCase() + titleString.substr(1)).replace(/ /g,"");

                    if (i == 0){
                        document.getElementById('wellTabs').innerHTML = document.getElementById('wellTabs').innerHTML + "<li id =\"" +
                        camelTitle + "Tab" + "\"" + " class=\"active\">" + "<a data-toggle=\"tab\" href=\"#" + camelTitle + "\">" +
                        titleString + "</a></li>" + "\n";

                        document.getElementById('tabs').innerHTML = document.getElementById('tabs').innerHTML + "<div id=\"" + camelTitle +
                        "\" class=\"active tab-pane\" style=\"margin-top:-15px\">" + "\n" + "<h2>" + titleString + "</h2>" + "\n" +
                        "<div class=\"well\" id=\"" + i + "\">";
                    } else{
                        document.getElementById('wellTabs').innerHTML = document.getElementById('wellTabs').innerHTML + "<li id =\"" +
                        camelTitle + "Tab" + "\">" + "<a data-toggle=\"tab\" href=\"#" + camelTitle + "\">" + titleString + "</a></li>" + "\n";

                        document.getElementById('tabs').innerHTML = document.getElementById('tabs').innerHTML + "<div id=\"" + camelTitle +
                        "\" class=\"tab-pane\" style=\"margin-top:-15px\">" + "\n" + "<h2>" + titleString + "</h2>" + "\n" +
                        "<div class=\"well\" id=\"" + i + "\">";
                    }


                    var fieldCount = well.match(/\//g);
                    well = well.replace(new RegExp("\n","g"),"");
                    well = well.split("/");
                    for (var j = 0; j < fieldCount.length; j++){
                        var field = well[j];
                        var begin = field.indexOf("<");
                        var end = field.length -1;
                        if (begin == -1 || end == -1){
                            alert("Make sure all tags are enclosed in < > and ending tags begin with /");
                            return;
                            // $('#config').removeClass("hidden");
                            break;
                        }
                        field = field.substring(begin,end);
                        var demo = field.substring(field.indexOf(">") + 1,field.length);
                        var tag = field.substring(field.indexOf("<") + 1,field.indexOf(">"));
                        var heading = field.substring(field.indexOf(">") + 1,field.length);
                        heading = heading.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                        var upperHeading = heading.replace(/ /g,"");
                        if (tag == "text"){
                            document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML +
                            "<div class=\"row-fluid\">" + 
                            "<div id=\"" + upperHeading + "\"><h5>" + heading + "</h5><input id=\"" + upperHeading.toLowerCase() +
                            "\"></div></div>" + "\n";
                        } else if (tag == "box"){
                            document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML +
                            "<div class=\"row-fluid\">" + 
                            "<div id=\"" + upperHeading + "\"><h5>" + heading + "</h5><textarea id=\"" + upperHeading.toLowerCase() +
                            "\" rows=\"5\" style=\"width:98%\"></textarea></div></div>" + "\n";
                        } else if (tag == "img"){
                            document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML +
                            "<div class=\"row-fluid\">" + 
                            "<div><form enctype=\"multipart/form-data\" id=\"" + upperHeading + "\"><h5>" + heading + "</h5>" +
                            "<input id=\"" + upperHeading.toLowerCase() + "\" style=\"display:inline;\" placeholder=\"Link\">" + 
                            "<div style=\"font-weight:bold; font-size:15px; display:inline; padding-left:6px; padding-right:6px;\"> or <br></div>" +
                            "<input type=\"file\" id=\"" + upperHeading.toLowerCase() + "Alt" + "\" name=\"" + upperHeading.toLowerCase() +
                            "\" accept=\"image/png, image/jpg, image/jpeg, application/pdf\" style = \"display:inline; padding-top:12px;\"><p style = \"display:inline; padding-top:0px; padding-left:-70px;\">*Note: Image must have extension .jpg, .jpeg, .png, or .pdf</p></div></div>" + "\n";
                        } else if (tag == "menu"){
                            menuCount = field.match(/,/g);
                            var item = field.split(";");
                            item = item[1].split(",");
                            heading = field.substring(field.indexOf(">") + 1, field.indexOf(";"));
                            heading = heading.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                            upperHeading = heading.replace(/ /g,"");
                            document.getElementById(i.toString()).innerHTML += "<div class=\"row-fluid\">" + 
                            "<div id=\"" + upperHeading + "\"><h5>" + heading + "<br></h5>" + "<select id=\"" + upperHeading.toLowerCase() + "\"></select></div></div>";
                            
                            for(var l = 0; l <= menuCount.length; l++){
                                item[l] = item[l].replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                                document.getElementById(upperHeading.toLowerCase()).innerHTML += "<option>" + item[l] + "</option>";
                            }

                        } else if (tag == "button"){
                            buttonCount = field.match(/,/g);
                            var item = field.split(";");
                            item = item[1].split(",");
                            heading = field.substring(field.indexOf(">") + 1, field.indexOf(";"));
                            heading = heading.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                            upperHeading = heading.replace(/ /g,"");
                            document.getElementById(i.toString()).innerHTML += "<div class=\"row-fluid\">" + 
                            "<div id=\"" + upperHeading + "\"><h5>" + heading + "<br></h5>" + "<form id=\"" + upperHeading.toLowerCase() + "\"></form></div></div>";
                            
                            for(var m = 0; m <= buttonCount.length; m++){
                                item[m] = item[m].replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                                document.getElementById(upperHeading.toLowerCase()).innerHTML += "<input type=\"radio\" id=\"" + item[m].replace(new RegExp(" ","g"),"").toLowerCase() + "\" value=\"" + item[m] + "\" name=\"" + heading + "\">" + item[m] + "<br>";
                            }

                        } else if (tag == "check"){
                            checkCount = field.match(/,/g);
                            var item = field.split(";");
                            item = item[1].split(",");
                            heading = field.substring(field.indexOf(">") + 1, field.indexOf(";"));
                            heading = heading.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                            upperHeading = heading.replace(/ /g,"");
                            document.getElementById(i.toString()).innerHTML += "<div class=\"row-fluid\">" + 
                            "<div id=\"" + upperHeading + "\"><h5>" + heading + "<br></h5>" + "<form id=\"" + upperHeading.toLowerCase() + "\"></form></div></div>";
                            
                            for(var n = 0; n <= checkCount.length; n++){
                                item[n] = item[n].replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1);}).trim();
                                document.getElementById(upperHeading.toLowerCase()).innerHTML += "<input type=\"checkbox\" id=\"" + item[n].replace(new RegExp(" ","g"),"").toLowerCase() + "\" value=\"" + item[n] + "\" name=\"" + heading + "\">" + item[n] + "<br>";
                            }

                        }
                    }

                    //forward and back buttons
                    if (i == 0){
                        document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML + "</div></div>" + 
                        "<button class=\"btn next\" style=\"margin-top:10px\"><strong>Next</strong></button>" + "\n";
                    } else if (i == (count.length - 1)){
                        document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML + "</div></div>" + 
                        "<button class=\"btn back\" style=\"margin-top:10px\"><strong>Back</strong></button>" + "\n";
                    } else{
                        document.getElementById(i.toString()).innerHTML = document.getElementById(i.toString()).innerHTML + "</div></div>" + 
                        "<button class=\"btn back\" style=\"margin-top:10px\"><strong>Back</strong></button>" + "\n" + 
                        "<button class=\"btn next\" style=\"margin-top:10px\"><strong>Next</strong></button>" + "\n";
                    }
                    
                }
                //make datasheet button
                document.getElementById('wellTabs').innerHTML = document.getElementById('wellTabs').innerHTML +
                "<li id=\"viewButton\"><button class=\"btn btn-success\" id=\"designButton\" onclick=\"makeDatasheet()\" style=\"width:100%\">Make Datasheet</button></li>";
            };
            $('#output').show();                    
        };

        var blob = file.slice(start, stop);
        reader.readAsText(blob);

    };



    function makeDatasheet(){

            imgCount = 0;
            var line, start, end, title, k, v;
            var titleCount = 0;
            var dataPairs = "{";
            var idNames = idString.split("\n");

            var formData = new FormData();
            var xhr = new XMLHttpRequest();

            var d = new Date();
            var currentTime = d.getTime().toString();

            for (var i = 0; i < idNames.length; i++) {
                line = idNames[i];
                if(line.indexOf("<title>") != -1){

                    start = line.indexOf("<title>") + 7;
                    end = line.indexOf("</title>");
                    title = line.substring(start, end).trim();

                    dataPairs += "\"title" + ++titleCount + "\":\"" + title + "\",";
                
                }

                if(line.indexOf("<text>") != -1){

                    start = line.indexOf("<text>") + 6;
                    end = line.indexOf("</text>");
                    k = line.substring(start, end).trim();

                    idValue = k.replace(new RegExp(" ","g"),"").toLowerCase().trim();
                    v = document.getElementById(idValue).value;

                    if (v == undefined){v = "";}

                    if(line.indexOf("*") != -1){
                        if(v == ""){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

                    if(v.indexOf("\"") != -1)
                    {
                        //v = v.replace(/"/g,"\\\"");
                        v = v.replace(/"/g,'\\"');
                    }

                    if(k.indexOf("\"") != -1)
                    {
                        v = k.replace(/"/g,"\\\"");
                    }



                    // if(v.indexOf("\'") != -1)
                    // {
                    //     alert("here!");
                    //     v = v.replace(/'/g,"\\\'");
                    // }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";

                }

                if(line.indexOf("<box>") != -1){

                    start = line.indexOf("<box>") + 5;
                    end = line.indexOf("</box>");
                    k = line.substring(start, end).trim();

                    idValue = k.replace(new RegExp(" ","g"),"").toLowerCase().trim();
                    v = document.getElementById(idValue).value;

                    if (v == undefined){v = "";}

                    if(line.indexOf("*") != -1){
                        if(v == ""){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

                    if(v.indexOf("\"") != -1)
                    {
                        v = v.replace(/"/g,"\\\"");
                    }

                    if(k.indexOf("\"") != -1)
                    {
                        v = k.replace(/"/g,"\\\"");
                    }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";

                }

                if(line.indexOf("<img>") != -1){

                    start = line.indexOf("<img>") + 5;
                    end = line.indexOf("</img>");
                    k = line.substring(start, end).trim();

                    idValue = k.replace(new RegExp(" ","g"),"").toLowerCase().trim();
                    v = document.getElementById(idValue).value;

                    if ((v == undefined || v == "") && document.getElementById(idValue + "Alt").value != "")
                    {

                        formData.append("image" + imgCount.toString(), document.getElementById(idValue + "Alt").files[0]);
                        imgCount++;
                        //alert(formData);
                        
                        v = "";
                        k = "<imgupload>" + k;
                    }
                    else if (v != undefined && v != "")
                    {
                        k = "<imglink>" + k;
                    }

                    if(line.indexOf("*") != -1 && document.getElementById(idValue + "Alt").value == ""){
                        if(v == ""){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

                    if(k.indexOf("\"") != -1)
                    {
                        v = k.replace(/"/g,"\\\"");
                    }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";

                }

                if(line.indexOf("<menu>") != -1){

                    start = line.indexOf("<menu>") + 6;
                    end = line.indexOf(";");
                    k = line.substring(start, end).trim();

                    idValue = k.replace(new RegExp(" ","g"),"").toLowerCase().trim();
                    v = document.getElementById(idValue).value;

                    if (v == undefined){v = "";}

                    if(line.indexOf("*") != -1){
                        if(v == ""){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

//                            alert("key: " + k);
//                            alert("value: " + v);

                    if(v.indexOf("\"") != -1)
                    {
//                                alert("before: " + v);
                        v = v.replace(/"/g,"\\\"");
//                                alert("after: " + v);
                    }

                    if(k.indexOf("\"") != -1)
                    {
//                                alert("before: " + k);
                        v = k.replace(/"/g,"\\\"");
//                                alert("after: " + k);
                    }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";

                }

                if(line.indexOf("<check>") != -1){

                    start = line.indexOf("<check>") + 7;
                    end = line.indexOf(";");
                    k = line.substring(start, end).trim();

                    v = undefined;

                    vals = line.substring(line.indexOf(";") + 1,line.indexOf("</check>")).trim();
                    valArray = vals.split(",");

                    for(var j = 0; j < valArray.length; j++)
                    {

                        if(document.getElementById(valArray[j].trim()).checked && v != undefined)
                        {
                            v = v + ", " + document.getElementById(valArray[j].trim()).value;
                        }
                        else if(document.getElementById(valArray[j].trim()).checked)
                        {
                            v = document.getElementById(valArray[j].trim()).value;
                        } 
                    }

                    if(line.indexOf("*") != -1){
                        if(v == undefined){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if (v == undefined){v = "";}

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

                    if(v.indexOf("\"") != -1)
                    {
                        v = v.replace(/"/g,"\\\"");
                    }

                    if(k.indexOf("\"") != -1)
                    {
                        v = k.replace(/"/g,"\\\"");
                    }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";

                }

                if(line.indexOf("<button>") != -1){

                    start = line.indexOf("<button>") + 8;
                    end = line.indexOf(";");
                    k = line.substring(start, end).trim();

                    v = undefined;

                    vals = line.substring(line.indexOf(";") + 1,line.indexOf("</button>")).toLowerCase().trim();
                    valArray = vals.split(",");

                    for(var j = 0; j < valArray.length; j++)
                    {
                        if(document.getElementById(valArray[j].trim()).checked)
                        {
                            v = document.getElementById(valArray[j].trim()).value;
                            break;
                        }
                    }

                    if(line.indexOf("*") != -1){
                        if(v == undefined){
                            alert("Please fill in required fields!");
                            return;
                        }
                    }

                    if (v == undefined){v = "";}

                    if(k.indexOf("*") != -1)
                    {
                        k = k.replace(/\*/g,'');
                    }

                    if(v.indexOf("\"") != -1)
                    {
                        v = v.replace(/"/g,"\\\"");
                    }

                    if(k.indexOf("\"") != -1)
                    {
                        v = k.replace(/"/g,"\\\"");
                    }

                    dataPairs += "\"" + k + "\":\"" + v + "\",";
                }
            }
            
            dataPairs = dataPairs.substring(0, (dataPairs.length -1));
            dataPairs += "}";

//                    alert(dataPairs);

            formData.append('mode','makeLatex');
            formData.append('latex',dataPairs);

            xhr.open("POST", "ParserServlet", true);
            xhr.send(formData);                    
}


var t = setInterval(getLink, 1000);
var pdflink = undefined;
function getLink(){

    $.get( "ParserServlet", function(data) {})
            .done(function(data) {
                pdflink = data.filename;

                if(pdflink != undefined)
                {
                    //alert(data.filename);

                    document.getElementById('wellTabs').innerHTML += "<li style=\"margin-top:10px\"><form method=\"link\" id=\"dataLink\" action=\"" + pdflink + "\" style=\"margin-bottom:0px\">\n" +
                        "<input type=\"submit\" value =\"View Datasheet\" class =\"btn btn-success\" style=\"width:100%\"></form></li>";

                    //document.getElementById('viewButton').innerHTML = "<button class=\"btn btn-success\" id=\"designButton\" onclick=\"changeDatasheet()\"" + 
                        //"style=\"width:100%\">Make Datasheet</button>";

                    clearInterval(t);

                }
            })

            .fail(function() {
                //alert("A fatal error has occurred. Please try again");
            })

            .always(function() {
                //alert( "finished" );
            });

}



$(document).ready(function() {
    var $tabs = $('.tabbable li');
    $('#prevtab').on('click', function() {
        $tabs.filter('.active').prev('li').find('a[data-toggle="tab"]').tab('show');
    });

    $('#nexttab').on('click', function() {
        $tabs.filter('.active').next('li').find('a[data-toggle="tab"]').tab('show');
    });
});
            
        // function changeDatasheet(){
        //     document.getElementById('dataLink').action = "http://wikipedia.org/";
        // }

        // // Javascript for dynamicForm
// // Function to hide sections 
// $(document).ready(function() {
    
//     $.get("ParserServlet",function(data) {
//        //use data to fill out parts of the form.
//        console.log('called');
//         $("#name").text(data.name);
//     });
       
//     //allows "next" and "back" buttons to work properly
//     var $tabs = $('.tabbable li');
    
//     //these two functions allow forward and backward navigation while in the
//     //datasheet form
//     $('.back').on('click', function() {
//         $tabs.filter('.active').prev('li').find('a[data-toggle="tab"]').tab('show');
//     });

//     $('.next').on('click', function() {
//         $tabs.filter('.active').next('li').find('a[data-toggle="tab"]').tab('show');
//     });
    
//     //checkbox for "other assay" in assay tab    
//     $('#otherSel').change(function(){
//         if ($('#otherSel:checked').val() === "ON") {
//             //append new assay code
//             $('#otherAssay').removeClass("hidden");       
//         }
//         else
//         {
//             $('#otherAssay').addClass("hidden");
//             console.log('trying to hide');
//         }      
        
//     });

//     //checkbox for "restriction map" in assay tab       
//     $('#restSel').change(function(){
//         if ($('#restSel:checked').val() === "ON") {
//             $('#restrictionMap').removeClass("hidden");

//         }
//         else
//         {
//             $('#restrictionMap').addClass("hidden");
//         }
        
//     });
    
//     //checkbox for "flow cytometry" in assay tab       
//     $('#flowSel').change(function(){
//         if ($('#flowSel:checked').val() === "ON") {
//             $('#functionalityAssays').removeClass("hidden");

//         }
//         else
//         {
//             $('#functionalityAssays').addClass("hidden");
//         }        
//     });
      
//     //the following three methods only do what unchecking the checkboxes in the 
//     //"assay" tab do   
//     $('button#removeRDButton').click(function() {
//         $('#restrictionMap').addClass("hidden");
//         $('#restrictionMap input').each(function() {
//             //clear the values
//             $(this).val("");
//         });
//     });
//     $('button#removeFlowCytometryButton').click(function() {
//         $('#functionalityAssays').addClass("hidden");
//         $('#flowCytometry input').each(function() {
//             //clear the values
//             $(this).val("");
//         });
//     });
//     $('button#removeOtherButton').click(function() {
//         $('#otherAssay').addClass("hidden");
//         $('#otherAssay input').each(function() {
//             //clear the values
//             $(this).val("");
//         });
//     });
    
//     // the following three sections will fire when an image has been uploaded by
//     // the user. the code then submits an ajax post request behind the scenes to
//     // a node server that I setup to handle the storing of the photos. the node server
//     // stores the images using a service called cloudinary
     
//     $('#displayImage').change(function(){
        
//                 var formData = new FormData($('#pigeonImage')[0]);
//                 console.log(formData);
//                 $.ajax({
//                             url: 'https://owlimageserver.herokuapp.com/pigeon',  //Server script to process data
//                             type: 'POST',
//                             // Form data
//                             data: formData,
//                             //Options to tell jQuery not to process data or worry about content-type.
//                             cache: false,
//                             contentType: false,
//                             processData: false
//                         });  
//     });
//       $('#plasmidMap').change(function(){
        
//                 var formData = new FormData($('#plasmidImage')[0]);
//                 console.log('plasmid');
//                 $.ajax({
//                             url: 'https://owlimageserver.herokuapp.com/plasmid',  //Server script to process data
//                             type: 'POST',
//                             // Form data
//                             data: formData,
//                             //Options to tell jQuery not to process data or worry about content-type.
//                             cache: false,
//                             contentType: false,
//                             processData: false
//                         });     
//     });  
  
//     $('#assemblyImage').change(function(){
        
//         console.log('assembly');        
//         var formData = new FormData($('#assemblyForm')[0]);
//                 console.log(formData);
//                 $.ajax({
//                             url: 'https://owlimageserver.herokuapp.com/assembly',  //Server script to process data
//                             type: 'POST',
//                             // Form data
//                             data: formData,
//                             //Options to tell jQuery not to process data or worry about content-type.
//                             cache: false,
//                             contentType: false,
//                             processData: false
//                         });
//     });

//     //JSON object 
//     $('#designButton').click(function() {
//         //collect information here
        
//         var pigeonPath;
//         var plasmidPath;
//         var assemblyPath;
        
        
//         // for the following three sections: path names are the paths to the
//         // cloudinary servers that the node server stored the images on
//         // if an image is not uploaded but a link is used, that is taken instead
//         if ($('#displayImage').val())
//         {
//             var pigeonName = $('#displayImage').val().replace(/C:\\fakepath\\/i, '');
//             pigeonPath = 'https://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/pigeonImage/' + pigeonName;
//         }
//         else
//         {
//             if ($('#pigeonAlt').val())
//             {
//                 pigeonPath = $('#pigeonAlt').val();
//             }
//             else
//             {
//                 pigeonPath = "";
//             }
//         }
//          if ($('#plasmidMap').val())
//         {
//             var plasmidName = $('#plasmidMap').val().replace(/C:\\fakepath\\/i, '');
//             plasmidPath = 'https://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/plasmidMap/' + plasmidName;
//         }
//         else
//         {
//             if ($('#plasmidAlt').val())
//             {
//                 plasmidPath = $('#plasmidAlt').val();
//             }
//             else
//             {
//                 plasmidPath = "";
//             }
//         }
        
         
//         if ($('#assemblyImage').val())
//         {
//             var assemblyname = $('#assemblyImage').val().replace(/C:\\fakepath\\/i, '');
//             assemblyPath = 'https://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/assemblyMaps/' + assemblyname;
//         }
//         else
//         {
//             if ($('#assemblyAlt').val())
//             {
//                 assemblyPath = $('#assemblyAlt').val();
//             }
//             else
//             {
//                 assemblyPath = "";       
//             }
//         }
      
//         var data = {};
//         data["name"] = $('#name').val();
//         data["summary"] = $('#summary').val();
//         data["sequence"] = $('#sequence').val();
//         data["deviceImage"] = pigeonPath;
//         data["plasmidMap"] = plasmidPath;
//         data["assemblyImage"] = assemblyPath;
//         data["partType"] = $('#partType :selected').text();
//         data["relatedParts"] = $('#relatedParts').val();
        

//         //gather contact information
//         var contactInformation = {};
//         $('div#contactInformation input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             contactInformation[key] = value;
//         });
//         $('div#contactInformation textarea').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             contactInformation[key] = value;
//         });
//         data["contactInformation"] = contactInformation;
        
//         //gather contact information
//         var basicInfo = {};
//         $('div#basicInfo input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             basicInfo[key] = value;
//         });
        
//         //gather design information
//         var designDetails = {};
//         $('div#designDetails input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             designDetails[key] = value;
//         });
//         $('div#designDetails textarea').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             designDetails[key] = value;
//         });
//         data["designDetails"] = designDetails;

//         //gather contact information
//         var assemblyInformation = {};
//         $('div#assemblyInformation input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             assemblyInformation[key] = value;
//         });
//         $('div#assemblyInformation textarea').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
//             assemblyInformation[key] = value;
//         });
//         data["assemblyInformation"] = assemblyInformation;

        
//         if (!$('#restrictionMap').hasClass('hidden'))
//         {
//         var restrictionMap = {};      
//         var flag = 0;
//         $('div#restrictionMap input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val();
            
//             if (value.length > 0) {
//                 restrictionMap[key] = value;
//                 flag = 1;
//                 console.log(value);
//             }
//             else{
//              restrictionMap[key] = "";   
//             }
//         });
        
//         if (flag === 1)
//         {
         
//         for (var key in restrictionMap) {
//             if (restrictionMap[key] !== 'undefined') {
//                 data["restrictionMap"]=restrictionMap;
//             }
//         }
        
//       }
//     }
//         if (!$('#otherAssay').hasClass('hidden'))
//         {
//         var otherAssay = {};
//         console.log('other');
//         var flag = 0;
//         $('div#otherAssay input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val(); 
//             if (value.length > 1)
//             {
//                 flag = 1;
//             }
//             otherAssay[key] = value; 
//         });
//         if (flag)
//         {
//         otherAssay['comments'] = $('#comments').val();
//         data["otherAssay"] = otherAssay;
//     }

//     }
//     if (!$('#functionalityAssays').hasClass('hidden'))
//     {
//         var functionalityAssays ={};
//         var flag = 0;

//         $('div#functionalityAssays div.experiment input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val(); 
//             if (value.length > 1)
//             {
//                 flag = 1;
//             }
//             functionalityAssays[key] = value; 
//         });
//         var pre ={};
//         $('div#functionalityAssays div.setup div#preInductionGrowthConditions input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val(); 
//              if (value.length > 1)
//             {
//                 flag = 1;
//             }          
//             pre[key] = value; 
//         });

//         var post ={};
//         $('div#functionalityAssays div.setup div#inductionGrowthConditions input').each(function() {
//             var key = $(this).attr("id");
//             var value = $(this).val(); 
//                   if (value.length > 1)
//             {
//                 flag = 1;
//             }
//             post[key] = value; 
//         });
//         if (flag)
//         {
//         data["functionalityAssays"] = functionalityAssays;
//         data["pre"] =pre;
//         data["post"] = post;
//     }
//     }
    
//     // these are the required fields that must have information for the data to be submitted 
//     if (data["name"].length > 0 && data["summary"].length > 0 && data["sequence"].length > 0 && data["contactInformation"]["authors"].length > 0 && data["contactInformation"]["date"].length > 0)
//        {
//         //hide error messages
//         $('#required_1').hide();
//         $('#required_2').hide();    
            
//         //change colors to black    
//         $('#partAs').css("color", "black");
//         $('#sumAs').css("color", "black");
//         $('#seqAs').css("color", "black");
//         $('#authAs').css("color", "black");
//         $('#dateAs').css("color", "black");
        
//         // submit the info to the server
//         $.get("DataServlet",{"sending":JSON.stringify(data)},function(){
//              window.location.assign("output.html");
//         });
//         }
    
//     // handle the case if not all the required fields were filled out    
//     else
//     {
//         // if it was the first tab that didn't have everything in it
//         if (data["name"].length <= 0 || data["summary"].length <= 0 || data["sequence"].length <= 0)
//         {
//             // change the tab to the appropriate one here
//             $('ul li').removeClass('active');
//             $('#basicInfoTab').addClass('active');
//             $('#tabs .active').removeClass('active');
//             $('#basicInfo').addClass('active');
//             $('#required_1').show();
//             $('#required_2').hide();
            
//             // set the colors to black
//             $('#partAs').css("color", "black");
//             $('#sumAs').css("color", "black");
//             $('#seqAs').css("color", "black");
//             $('#authAs').css("color", "black");
//             $('#dateAs').css("color", "black");

            
//             // now emphasize the appropriate fields
//             if (data["name"].length <=0)
//             {
//                 // change the css for the partAs id
//                 $('#partAs').css("color", "rgb(233, 47, 47)");
//             }
//             if (data["summary"].length <= 0)
//             {
//                 // change the css for the sumAs id
//                 $('#sumAs').css("color", "rgb(233, 47, 47)");
//             }
//             if (data["sequence"].length <= 0)
//             {
//                 // change the css for the seqAs id
//                 $('#seqAs').css("color", "rgb(233, 47, 47)");
//             }
//         }
//         // it must have been the second tab that didn't have everything filled in
//         else
//         {
//             // change to the second tab
//             $('ul li').removeClass('active');
//             $('#contactInformationTab').addClass('active');
//             $('#tabs .active').removeClass('active');
//             $('#contactInformation').addClass('active');
//             $('#required_1').hide();
//             $('#required_2').show();
            
//             // set the initial colors to black
//             // set the colors to black
//             $('#partAs').css("color", "black");
//             $('#sumAs').css("color", "black");
//             $('#seqAs').css("color", "black");
//             $('#authAs').css("color", "black");
//             $('#dateAs').css("color", "black");
            
//             if (data["contactInformation"]["authors"].length <= 0)
//             {
//                 // change the css for the authAs id
//                 $('#authAs').css("color", "rgb(233, 47, 47)");
//             }
//             if (data["contactInformation"]["date"].length <= 0)
//             {
//                 // change the css for the dateAs id
//                 $('#dateAs').css("color", "rgb(233, 47, 47)");
//             }
//         }
//     }

//     });
// });
        