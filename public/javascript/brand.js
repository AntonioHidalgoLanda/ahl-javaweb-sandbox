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

// populate autocomplete searchbox
    function selectBrand( brandid ) {
        // if (brandid <== 1) {open modal dialog;create new brand;}
        console.log("on development "+brandid);
    };

    function bindBrandAutocomplete(brandDivId){
        $( "#"+brandDivId ).autocomplete({
            source: function (request, response){
                var data = {'extended' : 'false', 'name':'%'+request.term+'%'};
                console.log("brand request: "+JSON.stringify(data));
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
                selectBrand( ui.item.value );
                $("#"+brandDivId).val(ui.item.label);
            }
        });
    };

// Subscribe to a brand

// Update Brand profile

// upsert branded Igotit


// upsert SKU


