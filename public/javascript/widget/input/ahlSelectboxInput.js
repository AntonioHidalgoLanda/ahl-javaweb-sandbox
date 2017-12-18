/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlSelectboxInput.prototype = new AhlBasicInput();
AhlSelectboxInput.prototype.constructor = AhlSelectboxInput;

// Map label with font awesome: 
// Map value: 

function AhlSelectboxInput(label,fieldId, mapValuesLabel){
    AhlBasicInput.call(this,label,fieldId);
    this.mapValuesLabel = mapValuesLabel;
}

AhlSelectboxInput.prototype.fieldToHtml = function (){
    var html = '<select id="'+this.divId+'-'+this.fieldId+'" '
             +      ' name="'+this.fieldId+'" class="ui-widget-content ui-corner-all"> '
             +  ' <option value=""> </option>';
    for (var olabel in this.mapValuesLabel){
        html+=  ' <option value="'+this.mapValuesLabel[olabel]+'">'+olabel+'</option> ';
    }
    html += '</select>';
    return "";
};

AhlSelectboxInput.prototype.update = function(value){
    this.getFieldDom().val(value).change();
    return this;
};