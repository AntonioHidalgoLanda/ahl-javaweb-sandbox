/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlTextInput.prototype = new AhlBasicInput();
AhlTextInput.prototype.constructor = AhlTextInput;

function AhlTextInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlTextInput.prototype.fieldToHtml = function (){
    var html = '<label for="'+this.divId+'-'+this.fieldId+'">'
             +    this.label
             + '</label> '
             + '<input type="text" name="'+this.fieldId+'" '
             +       'id="'+this.divId+'-'+this.fieldId+'"'
             +       'value="" class="text ui-widget-content ui-corner-all"/> ';
     return html;
};

AhlTextInput.prototype.update = function(value){
    this.getFieldDom().val(value);
    return this;
};