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
            case 'autocomplete':
                return this.fieldAutocompleteToHtml(fieldId);
            case 'id':
                return this.fieldIdToHtml(fieldId);
            case 'selectbox':
            case 'ratingPicker':
            case 'photo':
            case 'productPicker':
            case 'tags':
            case 'icon':
            case 'coordinateDisplay':
            case 'dateDisplay':
            case 'list':
                console.log("AhlForm.fieldToHtml() has not been completed for the type "+type);
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
    
    this.fieldIdToHtml = function(fieldId){
        var field = this.fields[fieldId];
        var html = '';
        if (field.show){
            html += '<label for="f-'+this.divId+'-'+fieldId+'">'
                 +    field.label
                 + '</label> '
                 + '<div id="'+this.divId+'-'+fieldId+'-show"></div> ';
        }
        html += ' <input type="hidden" name="'+fieldId+'" '
             +       'id="'+this.divId+'-'+fieldId+'"'
             +       'value="" class="text ui-widget-content ui-corner-all"/> ';
         return html;
    };
    
    this.fieldAutocompleteToHtml = function(fieldId){
        var field = this.fields[fieldId];
        var autocomplete = field.autocomplete;
        autocomplete.setDivId(this.divId+'-'+fieldId);
        var html = '<label for="'+autocomplete.filterDiv+'">'
                 +    field.label
                 + '</label> '
                 + '<div id="'+autocomplete.filterDiv+'"></div> ';
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
    
    this.updateIdField = function(fieldId, value){
        var field = this.fields[fieldId];
        if (field.show){
            this.getFieldDom(fieldId+'-show').html(value);
        }
        this.getFieldDom(fieldId).val(value);
    };
    
    this.updateAutocomplete = function(fieldId, value){
        var field = this.fields[fieldId];
        var autocomplete = field.autocomplete;
        autocomplete.bind();
        autocomplete.selectById(value);
    };
}


AhlForm.prototype.setDivId = function (divId){
    this.divId = divId;
};

AhlForm.prototype.setIdField = function (name, label, bShow){
    this.fields[name]={'type':'id', 'label':label, 'show':bShow};
};

AhlForm.prototype.setReadOnlyField = function (name, label){
    this.fields[name]={'type':'readOnly','label':label};
};

AhlForm.prototype.setTextField = function (name, label ){
    this.fields[name]={'type':'text','label':label};
};

AhlForm.prototype.setAutocompleteField = function (name, label, autocomplete ){
    autocomplete.setIdentifyingField(name)
            .setHiddenFieldName(name);
    this.fields[name]={'type':'autocomplete','label':label,'autocomplete':autocomplete};
};

AhlForm.prototype.setSelectboxField = function (name, label){
    this.fields[name]={'type':'selectbox','label':label};
};

AhlForm.prototype.setCoordinatesField = function (name, label){
    this.fields[name]={'type':'coordinatePicker','label':label};
};

AhlForm.prototype.setRatingField = function (name, label){
    this.fields[name]={'type':'ratingPicker','label':label};
};

// Especial, upload file + photo list + camera controller
AhlForm.prototype.setPhotoField = function (name, label){
    this.fields[name]={'type':'photo','label':label};
};

// Especial, product "autocomplete"  
AhlForm.prototype.setProductPickerField = function (name, label){
    this.fields[name]={'type':'productPicker','label':label};
};  

AhlForm.prototype.setTagField = function (name, label){
    this.fields[name]={'type':'tags','label':label};
};

// Especial, field with an icon with hover text
AhlForm.prototype.setReadOnlyIcon = function (name, label){
    this.fields[name]={'type':'icon','label':label};
};

AhlForm.prototype.setReadOnlyCoodinates = function (name, label){
    this.fields[name]={'type':'coordinateDisplay','label':label};
};

AhlForm.prototype.setReadOnlyDate = function (name, label){
    this.fields[name]={'type':'dateDisplay','label':label};
};

AhlForm.prototype.setReadOnlyList = function (name, label){
    this.fields[name]={'type':'list','label':label};
};

// 
AhlForm.prototype.updateValue = function (fieldId, value){
    var field = this.fields[fieldId];
    if (typeof field !== 'undefined'){
        var type = field.type;
        switch (type.toLowerCase()){
            case 'readonly':
                return this.updatePlainText(fieldId,value);
            case 'autocomplete':
                return this.updateAutocomplete(fieldId,value);
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

