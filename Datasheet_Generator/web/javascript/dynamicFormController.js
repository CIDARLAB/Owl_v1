// Javascript for dynamicForm
// Function to hide sections 
$(document).ready(function() {
    
    $.get("ParserServlet",function(data) {
       //use data to fill out parts of the form.
       console.log('called');
        $("#name").text(data.name);
    });
    
     $('#uploadFileButton').click(function() {
        console.log('trying to upload');
        var newFileName = $('#file').val();
        if (newFileName !== "") {
            $('#uploadForm').submit();
        } else if ($('a.dynatree-title:contains("' + newFileName + '")').length === 0) {
            editor.setValue("");
            saveFile(newFileName);
        }
    });
    
    
    var data = {};

    var $tabs = $('.tabbable li');

    $('.back').on('click', function() {
        $tabs.filter('.active').prev('li').find('a[data-toggle="tab"]').tab('show');
    });

    $('.next').on('click', function() {
        $tabs.filter('.active').next('li').find('a[data-toggle="tab"]').tab('show');
    });

    //handlers for assays
   /* $('button#addAssayButton').click(function() {


    });*/
    
    $('#selectAssay').change(function(){
        
        var selected = $('#selectAssay :selected').text();
        
        if (selected === "Other") {
            //append new assay code
            $('#otherAssay').removeClass("hidden");
             if (!$('#restrictionMap').hasClass("hidden"))
             {
                 $('#restrictionMap').addClass("hidden")
             }
               if (!$('#functionalityAssays').hasClass("hidden"))
             {
                 $('#functionalityAssays').addClass("hidden")
             }
             
        } else if (selected === "Restriction Map") {
            $('#restrictionMap').removeClass("hidden");
              if (!$('#otherAssay').hasClass("hidden"))
             {
                 $('#otherAssay').addClass("hidden")
             }
               if (!$('#functionalityAssays').hasClass("hidden"))
             {
                 $('#functionalityAssays').addClass("hidden")
             }
        } else if (selected === "Flow Cytometry") {
            $('#functionalityAssays').removeClass("hidden");
               if (!$('#otherAssay').hasClass("hidden"))
             {
                 $('#otherAssay').addClass("hidden")
             }
               if (!$('#restrictionMap').hasClass("hidden"))
             {
                 $('#restrictionMap').addClass("hidden")
             }
        }
        
    });
    
    
    $('button#removeRDButton').click(function() {

        $('#restrictionMap').addClass("hidden");
        $('#restrictionMap input').each(function() {
            //clear the values
            $(this).val("");
        });
    });
    $('button#removeFlowCytometryButton').click(function() {
        $('#functionalityAssays').addClass("hidden");
        $('#flowCytometry input').each(function() {
            //clear the values
            $(this).val("");
        });
    });
    $('button#removeOtherButton').click(function() {
        $('#otherAssay').addClass("hidden");
        $('#otherAssay input').each(function() {
            //clear the values
            $(this).val("");
        });
    });
    
    $('#displayImage').change(function(){
        
                var formData = new FormData($('#pigeonImage')[0]);
                console.log(formData);
                $.ajax({
                            url: 'http://owlimageserver.herokuapp.com/pigeon',  //Server script to process data
                            type: 'POST',
                            //Ajax events
                            //beforeSend: beforeSendHandler,
                            //success: completeHandler,
                            //error: errorHandler,
                            // Form data
                            data: formData,
                            //Options to tell jQuery not to process data or worry about content-type.
                            cache: false,
                            contentType: false,
                            processData: false
                        });
                      
        
        
    });
      $('#plasmidMap').change(function(){
        
                var formData = new FormData($('#plasmidImage')[0]);
                console.log('plasmid');
                $.ajax({
                            url: 'http://owlimageserver.herokuapp.com/plasmid',  //Server script to process data
                            type: 'POST',
                            //Ajax events
                            //beforeSend: beforeSendHandler,
                            //success: completeHandler,
                            //error: errorHandler,
                            // Form data
                            data: formData,
                            //Options to tell jQuery not to process data or worry about content-type.
                            cache: false,
                            contentType: false,
                            processData: false
                        });
                      
        
        
    });  
  
    $('#assemblyImage').change(function(){
        
        console.log('assembly');        
        var formData = new FormData($('#assemblyForm')[0]);
                console.log(formData);
                $.ajax({
                            url: 'http://owlimageserver.herokuapp.com/assembly',  //Server script to process data
                            type: 'POST',
                            //Ajax events
                            //beforeSend: beforeSendHandler,
                            //success: completeHandler,
                            //error: errorHandler,
                            // Form data
                            data: formData,
                            //Options to tell jQuery not to process data or worry about content-type.
                            cache: false,
                            contentType: false,
                            processData: false
                        });
                      
        
        
    });
    
   /* document.getElementById('displayImage').addEventListener('change', function(e) {
    var file = this.files[0];
    var xhr = new XMLHttpRequest();
    xhr.file = file; // not necessary if you create scopes like this
    xhr.addEventListener('progress', function(e) {
        var done = e.position || e.loaded, total = e.totalSize || e.total;
        console.log('xhr progress: ' + (Math.floor(done/total*1000)/10) + '%');
    }, false);
    if ( xhr.upload ) {
        xhr.upload.onprogress = function(e) {
            var done = e.position || e.loaded, total = e.totalSize || e.total;
            console.log('xhr.upload progress: ' + done + ' / ' + total + ' = ' + (Math.floor(done/total*1000)/10) + '%');
        };
    }
    xhr.onreadystatechange = function(e) {
        if ( 4 == this.readyState ) {
            console.log(['xhr upload complete', e]);
        }
    };
    xhr.open('post', 'http://localhost:7070/pigeon', true);
    xhr.send(file);
}, false);*/

    //JSON object 
    $('#designButton').click(function() {
        //collect information here
      
        
        var pigeonPath;
        var plasmidPath;
        var assemblyPath;
        
        if ($('#displayImage').val())
        {
            var pigeonName = $('#displayImage').val().replace(/C:\\fakepath\\/i, '');
            pigeonPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/pigeonImage/' + pigeonName;
        }
        else
        {
            pigeonPath = "";
        }
         if ($('#plasmidMap').val())
        {
            var plasmidName = $('#plasmidMap').val().replace(/C:\\fakepath\\/i, '');
            plasmidPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/plasmidMap/' + plasmidName;
        }
        else
        {
            plasmidPath = "";
        }
        
         
        if ($('#assemblyImage').val())
        {
            var assemblyname = $('#assemblyImage').val().replace(/C:\\fakepath\\/i, '');
            assemblyPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/assemblyMaps/' + assemblyname;
        }
        else
        {
            assemblyPath = "";
        }
        
       
        var data = {};
        data["name"] = $('#name').val();
        data["summary"] = $('#summary').val();
        data["sequence"] = $('#sequence').val();
        data["deviceImage"] = pigeonPath;
        data["plasmidMap"] = plasmidPath;
        data["assemblyImage"] = assemblyPath;
        data["partType"] = $('#partType :selected').text();
        data["relatedParts"] = $('#relatedParts').val();
        
        
        //console.log(pigeonName);
        

        //gather contact information
        var contactInformation = {};
        $('div#contactInformation input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            contactInformation[key] = value;
        });
        $('div#contactInformation textarea').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            contactInformation[key] = value;
        });
        data["contactInformation"] = contactInformation;
        
        //gather contact information
        var basicInfo = {};
        $('div#basicInfo input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            basicInfo[key] = value;
        });
        $('div#basicInfo textarea').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            basicInfo[key] = value;
        });
        data["basicInfo"] = basicInfo;
        
        //gather design information
        var designDetails = {};
        $('div#designDetails input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            designDetails[key] = value;
        });
        $('div#designDetails textarea').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            designDetails[key] = value;
        });
        data["designDetails"] = designDetails;

        //gather contact information
        var assemblyInformation = {};
        $('div#assemblyInformation input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            assemblyInformation[key] = value;
        });
        $('div#assemblyInformation textarea').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            assemblyInformation[key] = value;
        });
        data["assemblyInformation"] = assemblyInformation;

        
        if (!$('#restrictionMap').hasClass('hidden'))
        {
        var restrictionMap = {}       
        //DEVINA POPULATE THIS ARRAY
        var flag = 0;
        $('div#restrictionMap input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val();
            
            if (value.length > 0) {
                restrictionMap[key] = value;
                flag = 1;
                console.log(value);
            }
            else{
             restrictionMap[key] = "";   
            }
        });
        
        if (flag === 1)
        {
         
        for (var key in restrictionMap) {
            if (restrictionMap[key] !== 'undefined') {
                data["restrictionMap"]=restrictionMap;
            }
        }
        
      }
    }
        if (!$('#otherAssay').hasClass('hidden'))
        {
        var otherAssay = {};
        console.log('other');
        var flag = 0;
        $('div#otherAssay input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val(); 
            if (value.length > 1)
            {
                flag = 1;
            }
            otherAssay[key] = value; 
        });
        if (flag)
        {
        otherAssay['comments'] = $('#comments').val();
        data["otherAssay"] = otherAssay;
    }

    }
    if (!$('#functionalityAssays').hasClass('hidden'))
    {
        var functionalityAssays ={};
        var flag = 0;
        console.log('abc');
        //DEVINA POPULATE THIS ARRAY
        $('div#functionalityAssays div.experiment input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val(); 
            if (value.length > 1)
            {
                flag = 1;
            }
            functionalityAssays[key] = value; 
        });
        var pre ={};
        $('div#functionalityAssays div.setup div#preInductionGrowthConditions input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val(); 
             if (value.length > 1)
            {
                flag = 1;
            }          
            pre[key] = value; 
        });

        var post ={};
        $('div#functionalityAssays div.setup div#inductionGrowthConditions input').each(function() {
            var key = $(this).attr("id");
            var value = $(this).val(); 
                  if (value.length > 1)
            {
                flag = 1;
            }
            post[key] = value; 
        });
        if (flag)
        {
        data["functionalityAssays"] = functionalityAssays;
        data["pre"] =pre;
        data["post"] = post;
    }
    }
      $.get("DataServlet",{"sending":JSON.stringify(data)},function(){
             window.location.assign("output.html");
            //window.open("output.html");
            //win.focus();
        });

    });











});
