/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlAutocompleteInput.prototype = new AhlBasicInput();
AhlAutocompleteInput.prototype.constructor = AhlAutocompleteInput;

function AhlAutocompleteInput(label,fieldId,autocomplete){
    AhlBasicInput.call(this,label,fieldId);
    this.autocomplete = autocomplete;
    this.autocomplete.setIdentifyingField(fieldId)
            .setHiddenFieldName(fieldId);
}

AhlAutocompleteInput.prototype.fieldToHtml = function (){
    this.autocomplete.setDivId(this.divId+'-'+this.fieldId);
    var html = '<label for="'+this.autocomplete.filterDiv+'">'
             +    this.label
             + '</label> '
             + '<div id="'+this.autocomplete.filterDiv+'"></div> ';
    return html;
};

AhlAutocompleteInput.prototype.update = function(value){
        this.autocomplete.bind();
        this.autocomplete.selectById(value);
    return this;
};