/* 
 * Subscribe users to a brand (admin only)
 * 
 * Update contact details
 * 
 * Update page Url
 * 
 * Add branded Igotit
 * 
 * Add SKU???
 * 
 */



function generateBrandBasicForm(divId,brandTableId){
     var div = $("#"+divId);
     div.html('<form> '
             + '<div id="'+divId+'-brandid">Brand id: </div>'
             + '<fieldset> '
             + '   <input type="hidden" name="id" id="'+divId+'-id" value=""> '
             + '   <label for="'+divId+'-name">Name</label> '
             + '   <input type="text" name="name" id="'+divId+'-name" value="" class="text ui-widget-content ui-corner-all"> '
             + '   <label for="'+divId+'-pageurl">page URL</label> '
             + '   <input type="text" name="pageurl" id="'+divId+'-pageurl" value="" class="text ui-widget-content ui-corner-all"> '
             + '   <input type="submit" tabindex="-1" style="position:absolute; top:-1000px"> '
             + '</fieldset> '
             + '</form>'); 
     div.dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "Update Brand": function(){
            var id = $('#'+divId+'-id').val();
            var name = $('#'+divId+'-name').val();
            var pageurl = $('#'+divId+'-pageurl').val();
            updateBrand(id, name, pageurl,brandTableId);
            $(this).dialog("close");
        },
        Cancel: function() {
            $(this).dialog("close");
        }
      },
      close: function() {
      }
    });
 
    
}

function promtBrandEdit(id, divId){
    retrieveBrand(id,divId,displayBrandData);
}

function retrieveBrand(id,divId,displayFunction){
    var url = "/brands";
    var data = {'extended' : 'true','id':id};
    //alert(JSON.stringify(data)); 

    $.ajax({
           type: "GET",
           url: url,
           data: data,
           success: function(data)
           {
                if (data.constructor === Array){
                    displayFunction(divId,data[0]);
                }
           }
         });
}

function displayBrandData(divId,brandData){
   //alert(JSON.stringify(brandData));  // Display also Product IDs
   var dialog = $("#"+divId).dialog();
   var pBrandId = $('#'+divId+'-brandid');
   var fieldId = $('#'+divId+' input[name="id"]');
   var fieldName = $('#'+divId+' input[name="name"]');
   var fieldPageurl = $('#'+divId+' input[name="pageurl"]');
   pBrandId.html('Brand id: '+brandData["id"]);
   fieldId.val(brandData["id"]);
   fieldName.val(brandData["name"]);
   fieldPageurl.val(brandData['pageurl']);
   dialog.dialog( "open" );
}

// populate brands table
function populateBrandTable(brandTableId,divBrandFormId){
    $('#'+brandTableId).DataTable( {
        "processing": true,
        "ajax": {
            "url": "/brands",
            "dataSrc": function ( json ) {
                for (var i=0; i<json.length; i++){
                    var readonly = json[i].readonly;
                    json[i].action =  (readonly)?' <button onclick="promtBrandEdit('+json[i].id+',\''+divBrandFormId+'\')" type="button">edit</button> ':"";
                }
                return json;
            }
        },
        "columns": [
            { "data": "name" },
            { "data": "pageurl" },
            { "data": "action" }
        ]
    } );
}

// populate autocomplete searchbox
    function selectBrand( brandid , name, brandInputId, brandIdInputId,brandTableId) {
        var brandname = name;
        if (brandid <= 0){
            brandname = name.replace('(create new) ','');
            addNewBrand(brandname,brandIdInputId,brandTableId);
            
        }
        $("#"+brandInputId).val(brandname);
        $("#"+brandIdInputId).val(brandid);
    };

    function bindBrandAutocomplete(brandInputId, brandIdInputId,brandTableId){
        $( "#"+brandInputId ).autocomplete({
            source: function (request, response){
                var data = {'extended' : 'false', 'name':'%'+request.term+'%'};
                
                $.ajax({
                    type: "GET",
                    url: '/brands',
                    data: data,
                    success: function(data)
                    {   
                        var source = [];
                        for (var element in data){
                            var brand = data[element];
                            source.push({'label':brand.name,'value':brand.id});
                        }
                        source.push({'label':'(create new) '+request.term,'value':-1});
                        response(source);
                    }
                  });
            },
            minLength: 3,
            select: function( event, ui ) {
                event.preventDefault();
                selectBrand( ui.item.value, ui.item.label,brandInputId,brandIdInputId,brandTableId);
            }
        });
    };
    
// add new brand
function addNewBrand(name, brandIdInputId,brandTableId) {
    var data = {'name':name};
    
    $.ajax({
        type: "POST",
        url: '/brand',
        data: data,
        success: function(data)
        {   
            // Refresh the table
            $("#"+brandIdInputId).val(data);
            $('#'+brandTableId).DataTable().ajax.reload();
        }
      });
}

// Subscribe to a brand -- give an user write rights to a brand

// Update Brand profile
function updateBrand(id, name, pageurl,brandTableId) {
    var data = {};
    if (typeof id !== 'undefined'){
        data['id'] = id;
    }
    if (typeof name !== 'undefined'){
        data['name'] = name;
    }
    if (typeof pageurl !== 'undefined'){
        data['pageurl'] = pageurl;
    }
    
    $.ajax({
        type: "POST",
        url: '/brand',
        data: data,
        success: function(data)
        {   
            $('#'+brandTableId).DataTable().ajax.reload();
            //console.log(JSON.stringify(data));
        }
      });
}

// upsert branded Igotit


// upsert SKU?? (product, isn't it?)


