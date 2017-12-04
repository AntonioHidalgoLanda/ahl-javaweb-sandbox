/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function AhlWall(api_get,api_post,openRowFunction){
    this.namingField = "name";
    this.API_GET = api_get;
    this.divId = "";
}

AhlWall.prototype.setId = function(id){
    this.divId = id;
};

AhlWall.prototype.setFilter = function(){
    console.log("AhlWall.setFilter() has not been completed");
};

AhlWall.prototype.fetch = function(){
    console.log("AhlWall.fetch() has not been completed");
};
