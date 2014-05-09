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
    $('#otherSel').change(function(){
        if ($('#otherSel:checked').val() === "ON") {
            //append new assay code
            $('#otherAssay').removeClass("hidden");       
        }
        else
        {
            $('#otherAssay').addClass("hidden");
            console.log('trying to hide');
        }      
        
    });
    
    $('#restSel').change(function(){
        if ($('#restSel:checked').val() === "ON") {
            $('#restrictionMap').removeClass("hidden");

        }
        else
        {
            $('#restrictionMap').addClass("hidden");
        }
        
    });
    
    $('#flowSel').change(function(){
        if ($('#flowSel:checked').val() === "ON") {
            $('#functionalityAssays').removeClass("hidden");

        }
        else
        {
            $('#functionalityAssays').addClass("hidden");
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
    
    
    // the following three sections will fire when an image has been uploaded by
    // the user. the code then submits an ajax post request behind the scenes to
    // a node server that I setup to handle the storing of the photos. the node server
    // stores the images using a service called cloudinary
    $('#displayImage').change(function(){
        
                var formData = new FormData($('#pigeonImage')[0]);
                console.log(formData);
                $.ajax({
                            url: 'http://owlimageserver.herokuapp.com/pigeon',  //Server script to process data
                            type: 'POST',
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
                            // Form data
                            data: formData,
                            //Options to tell jQuery not to process data or worry about content-type.
                            cache: false,
                            contentType: false,
                            processData: false
                        });
                      
        
        
    });
    

    //JSON object 
    $('#designButton').click(function() {
        //collect information here
      

        
        
        var pigeonPath;
        var plasmidPath;
        var assemblyPath;
        
        
        // for the following three sections: path names are the paths to the
        // cloudinary servers that the node server stored the images on
        // if an image is not uploaded but a link is used, that is taken instead
        if ($('#displayImage').val())
        {
            var pigeonName = $('#displayImage').val().replace(/C:\\fakepath\\/i, '');
            pigeonPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/pigeonImage/' + pigeonName;
        }
        else
        {
            if ($('#pigeonAlt').val())
            {
                pigeonPath = $('#pigeonAlt').val();
            }
            else
            {
                pigeonPath = "";
            }
        }
         if ($('#plasmidMap').val())
        {
            var plasmidName = $('#plasmidMap').val().replace(/C:\\fakepath\\/i, '');
            plasmidPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/plasmidMap/' + plasmidName;
        }
        else
        {
            if ($('#plasmidAlt').val())
            {
                plasmidPath = $('#plasmidAlt').val();
            }
            else
            {
                plasmidPath = "";
            }
        }
        
         
        if ($('#assemblyImage').val())
        {
            var assemblyname = $('#assemblyImage').val().replace(/C:\\fakepath\\/i, '');
            assemblyPath = 'http://res.cloudinary.com/dvncno7qp/image/upload/v1398725130/assemblyMaps/' + assemblyname;
        }
        else
        {
            if ($('#assemblyAlt').val())
            {
                assemblyPath = $('#assemblyAlt').val();
            }
            else
            {
                assemblyPath = "";       
            }
        }
        
        console.log(pigeonPath);
        
       
        
        var data = {};
        data["name"] = $('#name').val();
        data["summary"] = $('#summary').val();
        data["sequence"] = $('#sequence').val();
        data["deviceImage"] = pigeonPath;
        data["plasmidMap"] = plasmidPath;
        data["assemblyImage"] = assemblyPath;
        data["partType"] = $('#partType :selected').text();
        data["relatedParts"] = $('#relatedParts').val();
        

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
    console.log(data);
    // these are the required fields that must have information for the data to be submitted 
    if (data["name"].length > 0 && data["summary"].length > 0 && data["sequence"].length > 0 && data["contactInformation"]["authors"].length > 0 && data["contactInformation"]["date"].length > 0)
       {
        // hide the alert if it is not already hidden
        $('#required_1').hide();
        $('#required_2').hide();
        
        // change the css for the different fields back to normal
        // partAs, sumAs, seqAs,authAs,dateAs
        $('#partAs').css("color", "black");
        $('#sumAs').css("color", "black");
        $('#seqAs').css("color", "black");
        $('#authAs').css("color", "black");
        $('#dateAs').css("color", "black");
        
        // submit the info to the server
        $.get("DataServlet",{"sending":JSON.stringify(data)},function(){
             window.location.assign("output.html");
        });
        }
    
    // handle the case if not all the required fields were filled out    
    else
    {
        // if it was the first tab that didn't have everything in it
        if (data["name"].length <= 0 || data["summary"].length <= 0 || data["sequence"].length <= 0)
        {
            // change the tab to the appropriate one here
            $('ul li').removeClass('active');
            $('#basicInfoTab').addClass('active');
            $('#tabs .active').removeClass('active');
            $('#basicInfo').addClass('active');
            $('#required_1').show();
            $('#required_2').hide();
            
            // set the colors to black
            $('#partAs').css("color", "black");
            $('#sumAs').css("color", "black");
            $('#seqAs').css("color", "black");
            $('#authAs').css("color", "black");
            $('#dateAs').css("color", "black");

            
            // now emphasize the appropriate fields
            if (data["name"].length <=0)
            {
                // change the css for the partAs id
                $('#partAs').css("color", "rgb(233, 47, 47)");
            }
            if (data["summary"].length <= 0)
            {
                // change the css for the sumAs id
                $('#sumAs').css("color", "rgb(233, 47, 47)");
            }
            if (data["sequence"].length <= 0)
            {
                // change the css for the seqAs id
                $('#seqAs').css("color", "rgb(233, 47, 47)");
            }
        }
        // it must have been the second tab that didn't have everything filled in
        else
        {
            // change to the second tab
            $('ul li').removeClass('active');
            $('#contactInformationTab').addClass('active');
            $('#tabs .active').removeClass('active');
            $('#contactInformation').addClass('active');
            $('#required_1').hide();
            $('#required_2').show();
            
            // set the initial colors to black
            // set the colors to black
            $('#partAs').css("color", "black");
            $('#sumAs').css("color", "black");
            $('#seqAs').css("color", "black");
            $('#authAs').css("color", "black");
            $('#dateAs').css("color", "black");
            
            if (data["contactInformation"]["authors"].length <= 0)
            {
                // change the css for the authAs id
                $('#authAs').css("color", "rgb(233, 47, 47)");
            }
            if (data["contactInformation"]["date"].length <= 0)
            {
                // change the css for the dateAs id
                $('#dateAs').css("color", "rgb(233, 47, 47)");
            }
        }
      
    }

    });











});
