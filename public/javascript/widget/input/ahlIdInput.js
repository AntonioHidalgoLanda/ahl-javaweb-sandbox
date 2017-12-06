/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlIdInput.prototype = new AhlBasicInput();
AhlIdInput.prototype.constructor = AhlIdInput;

function AhlIdInput(label,fieldId,bShow){
    AhlBasicInput.call(this,label,fieldId);
    this.bShow = bShow;
}

AhlIdInput.prototype.fieldToHtml = function (){
    var html = '';
    if (this.bShow){
        html += '<label for="f-'+this.divId+'-'+this.fieldId+'">'
             +    this.label
             + '</label> '
             + '<div id="'+this.this.divId+'-'+this.fieldId+'-show"></div> ';
    }
    html += ' <input type="hidden" name="'+this.fieldId+'" '
         +       'id="'+this.divId+'-'+this.fieldId+'"'
         +       'value="" class="text ui-widget-content ui-corner-all"/> ';
    return html;
};

AhlIdInput.prototype.update = function(value){
    if (this.bShow){
        this.getFieldDom(this.fieldId+'-show').html(value);
    }
    this.getFieldDom().val(value);
    return this;
};