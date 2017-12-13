/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlProductPickerInput.prototype = new AhlBasicInput();
AhlProductPickerInput.prototype.constructor = AhlProductPickerInput;

function AhlProductPickerInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlProductPickerInput.prototype.fieldToHtml = function (){
    console.log("AhlProductPickerInput has not been completed");
    return "";
};

AhlProductPickerInput.prototype.update = function(value){
    console.log("AhlProductPickerInput has not been completed");
    return this;
};