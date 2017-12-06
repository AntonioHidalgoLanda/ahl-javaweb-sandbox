/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * #Future Elements
 * table (read only)
 * hiden
 * Select (name, label, listOption)
 * Autocomplete
 * Multiselect (name, label, listOption)
 * Radio
 * Checkbox
 * Calendar
 * 
 */




function AhlForm(divId){
    this.fields = {};
    this.divId = divId;
    
    this.fields['id']= new AhlIdInput('id', 'id', true);   
}


AhlForm.prototype.setDivId = function (divId){
    this.divId = divId;
};

AhlForm.prototype.setField = function (name, field){
    this.fields[name] = field;
    return this;
};

// @Deprecated
AhlForm.prototype.setIdField = function (name, label, bShow){
    this.fields[name]= new AhlIdInput(name, label, bShow);
};

// @Deprecated
AhlForm.prototype.setReadOnlyField = function (name, label){
    this.fields[name] = new AhlBasicInput(label,name);
    return this;
};

// @Deprecated
AhlForm.prototype.setTextField = function (name, label ){
    this.fields[name]= new AhlTextInput(label,name);
};

// @Deprecated
AhlForm.prototype.setAutocompleteField = function (name, label, autocomplete ){
    this.fields[name] = new AhlAutocompleteInput(name, label, autocomplete );
};

// 
AhlForm.prototype.updateValue = function (fieldId, value){
    var field = this.fields[fieldId];
    field.update();
    return this;
};
AhlForm.prototype.generateHtml = function (){
    var div = $("#"+this.divId);
    var html = "<form> <fieldset>";
    for (var fieldId in this.fields){
        var field = this.fields[fieldId];
        field.bindDivId(this.divId);
        html += field.fieldToHtml();
    }
    html+="</fieldset> </form> ";
    div.html(html);
    return this;
};

