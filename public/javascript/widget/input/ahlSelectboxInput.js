/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlSelectboxInput.prototype = new AhlBasicInput();
AhlSelectboxInput.prototype.constructor = AhlSelectboxInput;

function AhlSelectboxInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlSelectboxInput.prototype.fieldToHtml = function (){
    console.log("AhlSelectboxInput has not been completed");
    return "";
};

AhlSelectboxInput.prototype.update = function(value){
    console.log("AhlSelectboxInput has not been completed");
    return this;
};