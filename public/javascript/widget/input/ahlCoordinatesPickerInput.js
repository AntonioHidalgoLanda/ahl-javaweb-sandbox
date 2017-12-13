/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlCoordinatesPickerInput.prototype = new AhlBasicInput();
AhlCoordinatesPickerInput.prototype.constructor = AhlCoordinatesPickerInput;

function AhlCoordinatesPickerInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlCoordinatesPickerInput.prototype.fieldToHtml = function (){
    console.log("AhlCoordinatesPickerInput has not been completed");
    return "";
};

AhlCoordinatesPickerInput.prototype.update = function(value){
    console.log("AhlCoordinatesPickerInput has not been completed");
    return this;
};