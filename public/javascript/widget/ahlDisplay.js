/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function AhlDisplay(api_get, api_post,refreshFunction ){
    this.namingField = "name";
    this.identifyingField = "id";
    this.API_GET = api_get;
    this.API_POST = api_post;
    this.refresh = refreshFunction;
    this.fields = {};
    
    this.formDivId = "";
    this.viewDivId = "";
}

AhlDisplay.prototype.setNamingField = function(name){
    this.namingField = name;
};

AhlDisplay.prototype.setIdentifyingField = function(id){
    this.identifyingField = id;
};

AhlDisplay.prototype.setFormDivId = function (divId) {
    this.formDivId = divId;
    return this;
};

AhlDisplay.prototype.setViewDivId = function (divId) {
    this.viewDivId = divId;
    return this;
};

AhlDisplay.prototype.setFields = function (fields) {
    this.fields = fields;
    return this;
};

AhlDisplay.prototype.getDialog = function (readonly) {
    return (readonly)?$("#"+this.viewDivId).dialog()
    : $("#"+this.formDivId).dialog();
};

AhlDisplay.prototype.getInput = function (name) {
    return $('#'+this.formDivId+' input[name="'+name+'"]');
};

AhlDisplay.prototype.getDisplayField = function (name) {
    return $('#f-'+this.viewDivId+'-'+name+'');
};

AhlDisplay.prototype.getIdInput = function () {
    return this.getInput(this.identifyingField);
};

AhlDisplay.prototype.getNameInput = function () {
    return this.getInput(this.namingField);
};

AhlDisplay.prototype.displayDataField = function (fieldname,fieldValue){
    this.getInput(fieldname).val(fieldValue);
    this.getDisplayField(fieldname).html(fieldValue);
    return this;
};

AhlDisplay.prototype.generateInputFieldsForm = function (){
    var domInputFields = "";
    for (var field in this.fields){
        var fieldName = this.fields[field];
        domInputFields += '<label for="'+this.formDivId+'-'+field+'">'
                        +    fieldName
                        + '</label> '
                        + '<input type="text" name="'+field+'" '
                        +       'id="'+this.formDivId+'-'+field+'"'
                        +       'value="" class="text ui-widget-content ui-corner-all"/> ';
    }
    return domInputFields;
};

AhlDisplay.prototype.generateForm = function (){
     var div = $("#"+this.formDivId);
     div.html('<form> '
             + '<div id="p-'+this.formDivId+'-id"> id: </div>'
             + '<fieldset> '
             +    '<label for="'+this.formDivId+'-'+this.namingField+'">Name</label> '
             +    '<input type="text" name="'+this.namingField+'" '
             +           'id="'+this.formDivId+'-'+this.namingField+'"'
             +           'value="" class="text ui-widget-content ui-corner-all"/> '
             +      this.generateInputFieldsForm()
             + '   <input type="hidden" name="id" id="'+this.formDivId+'-id" value=""> '
             + '   <input type="submit" tabindex="-1" style="position:absolute; top:-1000px"> '
             + '</fieldset> '
             + '</form>');
    return this;
};

AhlDisplay.prototype.generateView = function (){
     var div = $("#"+this.viewDivId);
     var domDiv = '<label for="f-'+this.viewDivId+'-'+this.identifyingField+'">ID</label>'
             + '<div id="f-'+this.viewDivId+'-'+this.identifyingField+'"></div>'
             + '<label for="f-'+this.viewDivId+'-'+this.namingField+'">Name</label>'
             + '<div id="f-'+this.viewDivId+'-'+this.namingField+'"></div>';
     
    for (var field in this.fields){
        var fieldName = this.fields[field];
        domDiv += '<label for="f-'+this.viewDivId+'-'+field+'">'
                +    fieldName
                + '</label> '
                + '<div id="f-'+this.viewDivId+'-'+field+'"></div> ';
    }
     div.html( domDiv);
    return this;
};

AhlDisplay.prototype.generateViewDialog   = function (){
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

AhlDisplay.prototype.generateFormDialog = function (){
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

// Could get override to retrieve more complex data
AhlDisplay.prototype.openRecord = function(id,readonly){
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

AhlDisplay.prototype.displayData = function (data, readonly){
   var pId = $('#p-'+this.formDivId+'-id');
   pId.html('id: '+data["id"]);
   for(var fieldname in data){
        var fieldValue = data[fieldname];
        this.displayDataField(fieldname,fieldValue);
   }
   // TODO Display dependant objects.
   this.getDialog(readonly).dialog( "open" );
   return this;
};

AhlDisplay.prototype.update = function (data) {
    var controller = this;
    
    $.ajax({
        type: "POST",
        url: controller.API_POST,
        data: data,
        success: function()
        {   
            if (typeof controller.refresh === 'function'){
                controller.refresh();
            }
        }
      });
};

