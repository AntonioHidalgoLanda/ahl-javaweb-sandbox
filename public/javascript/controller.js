/* 
 * 
 */

function Controller(api_get,api_post){
    this.formDivId = "";
    this.viewDivId = "";
    this.tableId = "";
    this.filterDiv = "";
    this.namingField = "name";
    this.identifyingField = "id";
    this.API_GET = api_get;
    this.API_POST = api_post;
    this.table = null;
    
    
}

Controller.prototype.setFormDivId = function (divId) {
    this.formDivId = divId;
    return this;
};

Controller.prototype.setViewDivId = function (divId) {
    this.viewDivId = divId;
    return this;
};

Controller.prototype.setTableId = function (divId) {
    this.tableId = divId;
    return this;
};

Controller.prototype.setFilterDiv = function (divId) {
    this.filterDiv = divId;
    return this;
};

Controller.prototype.setNamingField = function(name){
    this.namingField = name;
};

Controller.prototype.setIdentifyingField = function(id){
    this.identifyingField = id;
};

Controller.prototype.getDialog = function (readonly) {
    return (readonly)?$("#"+this.viewDivId).dialog()
    : $("#"+this.formDivId).dialog();
};

Controller.prototype.getInput = function (name) {
    return $('#'+this.formDivId+' input[name="'+name+'"]');
};

Controller.prototype.getIdInput = function () {
    return this.getInput(this.identifyingField);
};

Controller.prototype.getNameInput = function () {
    return this.getInput(this.namingField);
};

// This method should be override
Controller.prototype.generateTableStructure = function () {
    var tableDom = $('#'+this.tableId);
    tableDom.html("<thead> "
                 + " <tr> "
                 +  " <th>"+this.namingField+"</th> "
                 + " </tr> "
                 +"</thead>");
    return this;
};

// This method should be override
Controller.prototype.getTableColumnMap = function () {
    return [
            { "data": this.namingField }
        ];
};

// Should be override
Controller.prototype.generateInputFieldsForm = function (){
    return "";
};

// Could be override
Controller.prototype.generateForm = function (){
     var div = $("#"+this.formDivId);
     div.html('<form> '
             + '<div id="p-'+this.formDivId+'-id"> id: </div>'
             + '<fieldset> '
             +      this.generateInputFieldsForm()
             + '   <input type="hidden" name="id" id="'+this.formDivId+'-id" value=""> '
             + '   <input type="submit" tabindex="-1" style="position:absolute; top:-1000px"> '
             + '</fieldset> '
             + '</form>');
    return this;
};

// Could be override
Controller.prototype.generateView = function (){
     var div = $("#"+this.viewDivId);
     div.html( '<div id="p-'+this.formViewId+'-'+this.identifyingField+'"> id: </div>'
             + '<div id="p-'+this.formViewId+'-'+this.namingField+'"> name: </div>');
    return this;
};

// This method could be override
Controller.prototype.generateAutocompleteDiv = function () {
    if (this.filterDiv !== ""){
        var filterDom = $( "#"+this.filterDiv );
        filterDom.html("<label for=\""+this.filterDiv+"-id\">Select </label>"
                + "<input id=\""+this.filterDiv+"-"+this.namingField+"\">"
                + "<input type=\"hidden\" id=\""+this.filterDiv+"-id\" name=\"id\">");
    };
    return this;
};

Controller.prototype.generateViewDialog   = function (){
     var div = $("#"+this.viewDivId);
     this.generateView();
     div.dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "OK": function() {
            $(this).dialog("close");
        }
      },
      close: function() {
      }
    });
    return this;
};

Controller.prototype.generateFormDialog = function (){
     var div = $("#"+this.formDivId);
     var controller = this;
     this.generateForm();
     div.dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "Update": function(){
            var arrInputs = $('#'+controller.formDivId+' input');
            var data = {};
            for (var i in arrInputs){
                var input=arrInputs[i];
                if (input.type !== 'submit'){
                    data[input.name] = input.value;
                }
            }
            
            controller.update(data);
            controller.getDialog(false).dialog("close");
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

Controller.prototype.retrieve = function(id,readonly){
    var data = {'extended' : 'true','id':id};
    var controller = this;

    $.ajax({
           type: "GET",
           url: this.API_GET,
           data: data,
           success: function(data)
           {
                if (data.constructor === Array){
                    controller.displayData(data[0], readonly);
                }
           }
         });
};

Controller.prototype.displayData = function (data, readonly){
   var pId = $('#p-'+this.formDivId+'-id');
   pId.html('id: '+data["id"]);
   for(var fieldname in data){
        var fieldInput = this.getInput(fieldname);
        if (fieldInput.length > 0){
            var fieldValue = data[fieldname];
            fieldInput.val(fieldValue);
        }
   }
   // TODO Display dependant objects.
   this.getDialog(readonly).dialog( "open" );
   return this;
};

// Could be Overrided
Controller.prototype.tableAdaptData = function (json){
    return json;
};

Controller.prototype.populateTable = function (){
    var controller = this;
    this.generateTableStructure();
    this.table= $('#'+this.tableId).DataTable( {
        "processing": true,
        "rowId" : this.identifyingField,
        "rowCallback": function( row, data, index ) {
            if (typeof data['readonly'] !== 'undefined' && data['readonly']){
                $(row).css('color', 'gray');
            }
        },
        "columns": this.getTableColumnMap()
    } );
    $('#'+this.tableId+' tbody').on( 'click', 'tr', function () {
        var rowData = controller.table.row(this).data();
        var id = rowData['id'];
        var readonly = rowData['readonly'];
        controller.retrieve(id, readonly);
    } );
    this.tableRefresh();
    return this;
};

Controller.prototype.tableRefresh = function(){
    var controller = this;
    $.ajax({
           type: "GET",
           url: this.API_GET,
           success: function(dataSet)
           {
                controller.tableLoad(dataSet);
           }
         });
};

Controller.prototype.tableLoad = function(dataSet){
    if (this.table !== null){
        this.table.clear(0);
        this.table.rows.add(dataSet);
        this.table.draw();
    }
    return this;
};

Controller.prototype.select = function ( id , name) {
    if (id <= 0){
        name = name.replace('(create new) ','');
        this.addNew(name);

    }
    if (this.filterDiv !== "") {
        $("#"+this.filterDiv+"-id").val(id);
        $("#"+this.filterDiv+"-"+this.namingField).val(name);
    }
};

Controller.prototype.bindAutocomplete = function (){
        var controller = this;
        this.generateAutocompleteDiv();
        $( "#"+this.filterDiv+"-"+this.namingField ).autocomplete({
            source: function (request, response){
                var data = {'extended' : 'false'};
                data[controller.namingField] = '%'+request.term+'%';
                
                $.ajax({
                    type: "GET",
                    url: controller.API_GET,
                    data: data,
                    success: function(data)
                    {
                        var source = [];
                        for (var field in data){
                            var item = data[field];
                            source.push({'label':item.name,'value':item.id});
                        }
                        source.push({'label':'(create new) '+request.term,'value':-1});
                        response(source);
                    }
                  });
            },
            minLength: 3,
            select: function( event, ui ) {
                event.preventDefault();
                controller.select( ui.item.value, ui.item.label);
            }
        });
        return this;
    };
    
Controller.prototype.addNew = function(name) {
    var data = {};
    data[this.namingField] = name.trim();
    var controller = this;
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function(data)
        {   
            if (controller.filterDiv !== ""){
                $("#"+controller.filterDiv+"-id").val(data);
            }
            controller.tableRefresh();
        }
      });
};

Controller.prototype.update = function (data) {
    var controller = this;
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function()
        {   
            controller.tableRefresh();
        }
      });
};




