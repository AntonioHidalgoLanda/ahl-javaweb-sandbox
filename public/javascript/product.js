/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * browse
 * 
 * search
 * 
 * create
 * 
 * rate
 */

// search
// generate form

function generateProductSearchForm(formContainerId,resultContainerId){
    var formContainer=document.getElementById(formContainerId);
    var form = document.createElement("form");
    form.method = 'GET';
    form.id = formContainerId + "-form";
    form.onsubmit = 
    form.onsubmit = function (){
        var url = "/products"; // the script where you handle the form input.
        var resultContainer=document.getElementById(resultContainerId);
        resultContainer.innerHTML = "loading...";
        //var data = $("#"+form.id).serialize();
        var data = {'extended' : 'false'};
        var name = $("[name='name']", form).val();
        var brandid = $("[name='brandid']", form).val();
        if (name !== ""){
            data["name"] = name;
        }
        if (brandid !== ""){
            data["brandid"] = brandid;
        }
        //alert(JSON.stringify(data)); 

        $.ajax({
               type: "GET",
               url: url,
               data: data, // serializes the form's elements.
               success: function(data)
               {
                   var resultContainer=document.getElementById(resultContainerId);
                   resultContainer.innerHTML = "";
                   for (var element in data){
                       var product = data[element];
                       var entry = document.createElement("div");
                       entry.class = "product result-entry";
                       entry.id = "product_"+product.id;
                       // entry.innerHTML = JSON.stringify(data[element]);
                       entry.innerHTML += '<h4>'+product.name+'</h4>'
                             + '<div><strong>SKU: </strong>'+product.sku+'</div>'
                             + '<div><strong>Brand: </strong>'+product.brandid+'</div>'
                             + '<div><strong>Details: </strong>'+product.brandedIgotitId+'</div>'
                             + '';
                       resultContainer.appendChild(entry);
                   }
               }
             });

        return false; // avoid to execute the actual submit of the form.
    };
    form.innerHTML = ' \
    <div> \
       name: \
       <input name="name" type="text"></input> \
       <input type="checkbox" name="criteria" value="name" checked> \
    </div> ';
    form.innerHTML += ' \
    <div> \
       brand: \
       <input name="brandid" type="number"></input> \
       <input type="checkbox" name="criteria" value="brand"> \
    </div> ';
    form.innerHTML += ' \
    <input type="submit" value="Submit">\
    ';
    formContainer.appendChild(form);
}

// search by tags

// submit BrowseForm

// browse


// create


// rate


