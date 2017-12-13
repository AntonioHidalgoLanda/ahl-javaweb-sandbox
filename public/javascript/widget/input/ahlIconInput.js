/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlIconInput.prototype = new AhlBasicInput();
AhlIconInput.prototype.constructor = AhlIconInput;

function AhlIconInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlIconInput.prototype.fieldToHtml = function (){
    console.log("AhlIconInput has not been completed");
    return "";
};

AhlIconInput.prototype.update = function(value){
    console.log("AhlIconInput has not been completed");
    return this;
};