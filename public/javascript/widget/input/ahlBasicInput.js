/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function AhlBasicInput(label,fieldId){
    this.divId = "";
    this.label = label;
    this.fieldId = fieldId;
}

AhlBasicInput.prototype.bindDivId = function (divId){
    this.divId = divId;
    return this;
};

AhlBasicInput.prototype.getFieldDom = function(id){
    var reference = this.fieldId;
    if (typeof id !== 'undefined' && id !== null){
        reference=id;
    }
    return $('#'+this.divId+'-'+reference);
};

AhlBasicInput.prototype.fieldToHtml = function (){
    var html = '<label for="f-'+this.divId+'-'+this.fieldId+'">'
             +    this.label
             + '</label> '
             + '<div id="'+this.divId+'-'+this.fieldId+'"></div> ';
    return html;
};

AhlBasicInput.prototype.update = function(value){
    this.getFieldDom().html(value);
    return this;
};