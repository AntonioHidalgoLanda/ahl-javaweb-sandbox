/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlListInput.prototype = new AhlBasicInput();
AhlListInput.prototype.constructor = AhlListInput;

function AhlListInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlListInput.prototype.fieldToHtml = function (){
    console.log("AhlListInput has not been completed");
    return "";
};

AhlListInput.prototype.update = function(value){
    console.log("AhlListInput has not been completed");
    return this;
};