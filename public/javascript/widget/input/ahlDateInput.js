/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlDateInput.prototype = new AhlBasicInput();
AhlDateInput.prototype.constructor = AhlDateInput;

function AhlDateInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlDateInput.prototype.fieldToHtml = function (){
    console.log("AhlDateInput has not been completed");
    return "";
};

AhlDateInput.prototype.update = function(value){
    console.log("AhlDateInput has not been completed");
    return this;
};