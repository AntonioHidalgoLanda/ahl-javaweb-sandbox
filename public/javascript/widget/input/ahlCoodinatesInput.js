/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlCoodinatesInput.prototype = new AhlBasicInput();
AhlCoodinatesInput.prototype.constructor = AhlCoodinatesInput;

function AhlCoodinatesInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlCoodinatesInput.prototype.fieldToHtml = function (){
    console.log("AhlCoodinatesInput has not been completed");
    return "";
};

AhlCoodinatesInput.prototype.update = function(value){
    console.log("AhlCoodinatesInput has not been completed");
    return this;
};