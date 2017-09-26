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

function BrandController(){
    this.formDivId = "";
    this.tableId = "";
    this.filterDiv = "";
    this.API_GET = "/brands";
    this.API_POST = "/brand";
    this.table = null;
    
    
}

BrandController.prototype.setFilterDiv = function (divId) {
    this.filterDiv = divId;
    return this;
};

BrandController.prototype.setFormDivId = function (divId) {
    this.formDivId = divId;
    return this;
};

BrandController.prototype.setTableId = function (divId) {
    this.tableId = divId;
    return this;
};

BrandController.prototype.getDialog = function () {
    return $("#"+this.formDivId).dialog();
};

BrandController.prototype.getIdInput = function () {
    return $('#'+this.formDivId+' input[name="id"]');
};

BrandController.prototype.getNameInput = function () {
    return $('#'+this.formDivId+' input[name="name"]');
};

BrandController.prototype.getPageurlInput = function () {
    return $('#'+this.formDivId+' input[name="pageurl"]');
};
   
BrandController.prototype.generateBrandBasicForm = function (){
     var div = $("#"+this.formDivId);
     var controller = this;
     div.html('<form> '
             + '<div id="'+this.formDivId+'-brandid">Brand id: </div>'
             + '<fieldset> '
             + '   <input type="hidden" name="id" id="'+this.formDivId+'-id" value=""> '
             + '   <label for="'+this.formDivId+'-name">Name</label> '
             + '   <input type="text" name="name" id="'+this.formDivId+'-name" value="" class="text ui-widget-content ui-corner-all"> '
             + '   <label for="'+this.formDivId+'-pageurl">page URL</label> '
             + '   <input type="text" name="pageurl" id="'+this.formDivId+'-pageurl" value="" class="text ui-widget-content ui-corner-all"> '
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
            var id = controller.getIdInput().val();
            var name = controller.getNameInput().val();
            var pageurl = controller.getPageurlInput().val();
            controller.updateBrand(id, name, pageurl);
            controller.getDialog().dialog("close");
        },
        Cancel: function() {
            $(this).dialog("close");
        }
      },
      close: function() {
      }
    });
    return this;
};

BrandController.prototype.promtBrandEdit = function (id){
    this.retrieveBrand(id,BrandController.displayBrandData);
};

BrandController.prototype.retrieveBrand = function(id,displayFunction){
    var data = {'extended' : 'true','id':id};
    var controller = this; 

    $.ajax({
           type: "GET",
           url: this.API_GET,
           data: data,
           success: function(data)
           {
                if (data.constructor === Array){
                    displayFunction(controller,data[0]);
                }
           }
         });
};

BrandController.displayBrandData = function (controller, brandData){
   //alert(JSON.stringify(brandData));  // Display also Product IDs
   var pBrandId = $('#'+controller.formDivId+'-brandid');
   pBrandId.html('Brand id: '+brandData["id"]);
   controller.getIdInput().val(brandData["id"]);
   controller.getNameInput().val(brandData["name"]);
   controller.getPageurlInput().val(brandData['pageurl']);
   controller.getDialog().dialog( "open" );
   return controller;
};

// populate brands table
BrandController.prototype.populateBrandTable = function (){
    var controller = this;
    var tableDom = $('#'+this.tableId);
    tableDom.html("<thead> "
                 + " <tr> "
                 +  " <th>Name</th> "
                 +  " <th>URL</th> "
                 + " </tr> "
                 +"</thead>");
    this.table= tableDom.DataTable( {
        "processing": true,
        "ajax": {
            "url": this.API_GET,
            "dataSrc": ""
        },
        "rowCallback": function( row, data, index ) {
            if (typeof data['readonly'] !== 'undefined' && !data['readonly']){
                $(row).css('color', 'gray');
            }
        },
        "columns": [
            { "data": "name" },
            { "data": "pageurl" }
        ]
    } );
    $('#'+this.tableId+' tbody').on( 'click', 'tr', function () {
        var rowData = controller.table.row(this).data();
        var id = rowData['id'];
        var readonly = rowData['readonly'];
        if (readonly){
            controller.promtBrandEdit(id);
        }
    } );
};

// populate autocomplete searchbox
BrandController.prototype.selectBrand = function ( brandid , name) {
        var brandname = name;
        if (brandid <= 0){
            brandname = name.replace('(create new) ','');
            this.addNewBrand(brandname);
            
        }
        if (this.filterDiv !== "") {
            $("#"+this.filterDiv+"-id").val(brandid);
            $("#"+this.filterDiv+"-name").val(brandname);
        }
    };

BrandController.prototype.bindBrandAutocomplete = function (){
        var controller = this;
        if (this.filterDiv !== ""){
            var filterDom = $( "#"+this.filterDiv );
            filterDom.html("<label for=\""+this.filterDiv+"-id\">Brand </label>"
                    + "<input id=\""+this.filterDiv+"-name\">"
                    + "<input type=\"hidden\" id=\""+this.filterDiv+"-id\" name=\"brandid\">");
        };
        $( "#"+this.filterDiv+"-name" ).autocomplete({
            source: function (request, response){
                var data = {'extended' : 'false', 'name':'%'+request.term+'%'};
                
                $.ajax({
                    type: "GET",
                    url: controller.API_GET,
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
                controller.selectBrand( ui.item.value, ui.item.label);
            }
        });
    };
    
// add new brand
BrandController.prototype.addNewBrand = function(name) {
    var data = {'name':name.trim()};
    var controller = this;
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function(data)
        {   
            // Refresh the table
            if (controller.filterDiv !== ""){
                $("#"+controller.filterDiv+"-id").val(data);
            }
            if (controller.tableId !== ""){
                controller.table.ajax.reload();
            }
        }
      });
};

// Subscribe to a brand -- give an user write rights to a brand

// Update Brand profile
BrandController.prototype.updateBrand = function (id, name, pageurl) {
    var data = {};
    var controller = this;
    if (typeof id !== 'undefined'){
        data['id'] = id;
    }
    if (typeof name !== 'undefined'){
        data['name'] = name.trim();
    }
    if (typeof pageurl !== 'undefined'){
        data['pageurl'] = pageurl.trim();
    }
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function(data)
        {   
            if (controller.tableId !== ""){
                controller.table.ajax.reload();
            }
        }
      });
};

// upsert branded Igotit


// upsert SKU?? (product, isn't it?)


