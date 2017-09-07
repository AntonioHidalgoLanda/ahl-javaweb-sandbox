/* 
 * Post
 * The submit button will manage the different post calls to each entity:
 *    Create Igotit
 *    Add Photos
 *    Add Tags
 * 
 * 
 * 
 *  Ajax example with jQuery
 *  
 *  
 *  // this is the id of the submit button
$("#submitButtonId").click(function() {

    var url = "path/to/your/script.php"; // the script where you handle the form input.

    $.ajax({
           type: "POST",
           url: url,
           data: $("#idForm").serialize(), // serializes the form's elements.
           success: function(data)
           {
               alert(data); // show response from the php script.
           }
         });

    return false; // avoid to execute the actual submit of the form.
});

 * 
 *  Simple post
 *      Generate form
 *  Photo post
 *      Generate add photos form
 *  Add tags
 *      Generate add tags form
 *  rating
 *  Find Product
 *  Add products
 *  Create Product
 *  Create Brand
 * 
 * Visibility
 * 
 * See the wardrove of my connections (followers and friends)
 * 
 * See the wardrove of one connection
 * 
 * See my wardrove
 * 
 */


