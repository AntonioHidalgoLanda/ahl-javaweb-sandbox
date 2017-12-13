/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlTagInput.prototype = new AhlBasicInput();
AhlTagInput.prototype.constructor = AhlTagInput;

function AhlTagInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlTagInput.prototype.fieldToHtml = function (){
    console.log("AhlTagInput has not been completed");
    return "";
};

AhlTagInput.prototype.update = function(value){
    console.log("AhlTagInput has not been completed");
    return this;
};