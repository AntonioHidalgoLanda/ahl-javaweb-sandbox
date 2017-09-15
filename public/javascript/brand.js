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

function promtBrandEdit(id){
    alert('Editing Brand '+id);
}

// populate brands table
function populateBrandTable(brandTableId){
    $('#'+brandTableId).DataTable( {
        "processing": true,
        //"serverSide": true,
        "ajax": {
            "url": "/brands",
            "dataSrc": function ( json ) {
                for (var i=0; i<json.length; i++){
                    var readonly = json[i].readonly;
                    json[i].action =  (readonly)?' <button onclick="promtBrandEdit('+json[i].id+')" type="button">edit</button> ':"";
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
    function selectBrand( brandid , name, brandInputId, brandIdInputId) {
        var brandname = name;
        if (brandid <= 0){
            brandname = name.replace('(create new) ','');
            addNewBrand(brandname, brandInputId,brandIdInputId);
            
        }
        $("#"+brandInputId).val(brandname);
        $("#"+brandIdInputId).val(brandid);
    };

    function bindBrandAutocomplete(brandInputId, brandIdInputId){
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
                selectBrand( ui.item.value, ui.item.label,brandInputId,brandIdInputId);
            }
        });
    };
    
// add new brand
function addNewBrand(name, brandInputId, brandIdInputId) {
    var data = {'name':name};
    
    $.ajax({
        type: "POST",
        url: '/brand',
        data: data,
        success: function(data)
        {   
            $("#"+brandIdInputId).val(data);
        }
      });
}

// Subscribe to a brand -- give an user write rights to a brand

// Update Brand profile
function updateBrand(id, name, pageurl) {
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
            console.log(JSON.stringify(data));
        }
      });
}

// upsert branded Igotit


// upsert SKU?? (product, isn't it?)


