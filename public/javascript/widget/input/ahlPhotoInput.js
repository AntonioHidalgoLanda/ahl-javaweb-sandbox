/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global AhlBasicInput */

AhlPhotoInput.prototype = new AhlBasicInput();
AhlPhotoInput.prototype.constructor = AhlPhotoInput;

function AhlPhotoInput(label,fieldId){
    AhlBasicInput.call(this,label,fieldId);
}

AhlPhotoInput.prototype.fieldToHtml = function (){
    console.log("AhlPhotoInput has not been completed");
    return "";
};

AhlPhotoInput.prototype.update = function(value){
    console.log("AhlPhotoInput has not been completed");
    return this;
};