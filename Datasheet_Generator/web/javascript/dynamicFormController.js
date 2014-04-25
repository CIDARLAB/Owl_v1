// Javascript for dynamicForm
// Function to hide sections 
$(document).ready(function() {
    
    $.get("ParserServlet",function(data) {
       //use data to fill out parts of the form.
        $("#name").text(data.name);
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

    //JSON object 
    $('#designButton').click(function() {
        //collect information here
        var data = {};
        data["name"] = $('#name').val();
        data["summary"] = $('#summary').val();
        data["sequence"] = $('#sequence').val();
        data["deviceImage"] = $('#deviceImage').val();
        
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
        data["otherAssay"] = otherAssay;
    }
    }
    if (!$('#functionalityAssays').hasClass('hidden'))
    {
        var functionalityAssays ={};
        var flag = 0;
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
            var win = window.open("output.html",'_blank');
            win.focus();
        });

    });











});
