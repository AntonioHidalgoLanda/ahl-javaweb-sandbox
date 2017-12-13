/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlRatingPickerInput.prototype = new AhlBasicInput();
AhlRatingPickerInput.prototype.constructor = AhlRatingPickerInput;

function AhlRatingPickerInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlRatingPickerInput.prototype.fieldToHtml = function (){
    console.log("AhlRatingPickerInput has not been completed");
    return "";
};

AhlRatingPickerInput.prototype.update = function(value){
    console.log("AhlRatingPickerInput has not been completed");
    return this;
};