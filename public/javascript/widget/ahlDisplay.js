/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global ahlForm */


function AhlDisplay(api_get, api_post,refreshFunction ){
    this.namingField = "name";
    this.identifyingField = "id";
    this.API_GET = api_get;
    this.API_POST = api_post;
    this.refresh = refreshFunction;
    
    this.viewFields = new AhlForm('');
    this.editFields = new AhlForm('');
}

AhlDisplay.prototype.setNamingField = function(name){
    this.namingField = name;
};

AhlDisplay.prototype.setIdentifyingField = function(id){
    this.identifyingField = id;
};

AhlDisplay.prototype.setViewDivId = function (divId) {
    this.viewFields.setDivId(divId);
    return this;
};

AhlDisplay.prototype.setEditDivId = function (divId) {
    this.editFields.setDivId(divId);
    return this;
};

AhlDisplay.prototype.getDialog = function (readonly) {
    var divId = (readonly)?this.viewFields.divId: this.editFields.divId;
    return $("#"+divId).dialog();
};

AhlDisplay.prototype.displayDataField = function (fieldname,fieldValue){
    this.viewFields.updateValue(fieldname,fieldValue);
    this.editFields.updateValue(fieldname,fieldValue);
    return this;
};

AhlDisplay.prototype.generateView = function (){
    this.viewFields.generateHtml();
    return this;
};

AhlDisplay.prototype.generateEdit = function (){
    this.editFields.generateHtml();
    return this;
};

AhlDisplay.prototype.generateViewDialog   = function (){
     var div = $("#"+this.viewFields.divId);
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

AhlDisplay.prototype.generateEditDialog = function (){
     var div = $("#"+this.editFields.divId);
     var controller = this;
     this.generateEdit();
     div.dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "Update": function(){
            var arrInputs = $('#'+controller.editFields.divId+' input');
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

