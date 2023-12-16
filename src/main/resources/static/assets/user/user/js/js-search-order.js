search = () =>{
   var phone = document.getElementById("phone1").value
   var idOrder = document.getElementById("id-order").value


   // $.ajax({
   //    url: `/rest/order/search-order?phone=${phone}&id-order=${idOrder}`,
   //    method: "GET",
   //    success: function(resp) {
   //       console.log(resp)
   //    },
   //    error: function(xhr, status, error) {
   //       console.error("Error fetching data:", error);
   //    }
   // });
   location.href=`/rest/order/search-order?phone=${phone}&id-order=${idOrder}`
}

// validateSearchOrder = () =>{
//    var phone = document.getElementById("phone1").value
//    var idOrder = document.getElementById("id-order").value
//
//
// }