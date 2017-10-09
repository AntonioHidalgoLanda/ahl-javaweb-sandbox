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
    this.fields = {'id':{'type':'readOnly','label':'id'}};
    this.divId = divId;
    
    /* Note, this is better done using inheritance, and making each field an 
     * object */
    this.fieldToHtml = function(fieldId){
        var type = this.fields[fieldId].type;
        switch (type.toLowerCase()){
            case 'text':
            case 'input':
                return this.fieldTextToHtml(fieldId);
            default :
                return this.fieldReadOnlyToHtml(fieldId);
        }
    };
    
    this.fieldReadOnlyToHtml = function(fieldId){
        var field = this.fields[fieldId];
        var html = '<label for="f-'+this.divId+'-'+fieldId+'">'
                 +    field.label
                 + '</label> '
                 + '<div id="'+this.divId+'-'+fieldId+'"></div> ';
         return html;
    };
    
    this.fieldTextToHtml = function(fieldId){
        var field = this.fields[fieldId];
        var html = '<label for="'+this.divId+'-'+fieldId+'">'
                 +    field.label
                 + '</label> '
                 + '<input type="text" name="'+fieldId+'" '
                 +       'id="'+this.divId+'-'+fieldId+'"'
                 +       'value="" class="text ui-widget-content ui-corner-all"/> ';
         return html;
    };
    
    this.getFieldDom = function(fieldId){
        return $('#'+this.divId+'-'+fieldId);
    };
    
    this.updatePlainText = function(fieldId, value){
        this.getFieldDom(fieldId).html(value);
    };
    
    this.updateField = function(fieldId, value){
        this.getFieldDom(fieldId).val(value);
    };
}


AhlForm.prototype.setDivId = function (divId){
    this.divId = divId;
};


AhlForm.prototype.setReadOnlyField = function (name, label){
    this.fields[name]={'type':'readOnly','label':label};
};

AhlForm.prototype.setTextField = function (name, label ){
    this.fields[name]={'type':'text','label':label};
};

// 
AhlForm.prototype.updateValue = function (fieldId, value){
    var field = this.fields[fieldId];
    if (typeof field !== 'undefined'){
        var type = field.type;
        switch (type.toLowerCase()){
            case 'readonly':
                return this.updatePlainText(fieldId,value);
            default :
                return this.updateField(fieldId,value);
        }
    }
};
AhlForm.prototype.generateHtml = function (){
    var div = $("#"+this.divId);
    var html = "<form> <fieldset>";
    for (var fieldId in this.fields){
        html += this.fieldToHtml(fieldId);
    }
    html+="</fieldset> </form> ";
    div.html(html);
    return this;
};

