$(document).ready(function() {
  let totalPages = 1;

function fetchUsers(startPage) {
  //console.log('startPage: ' +startPage);
  /**
   * get data from Backend's REST API
   */
  $.ajax({
      type : "GET",
      url : "/Admin/User/List",
      data: { 
          page: startPage, 
          size: 5
      },
      success: function(response){
        
        // add table rows
        $("#tbodyUser").replaceWith(response);


      },
      error : function(e) {
        
        console.log("ERROR: ", e);
      }
  });
}

$(document).on("click", "ul.pagination li a", function() {
  var data = $(this).attr('data');
  let val = $(this).text();
  let totalPages = $("#lastPages").val();
  console.log('val: ' + val);

  // click on the NEXT tag
  if(val.toUpperCase() === "« FIRST") {
      let currentActive = $("li.active");
      fetchUsers(0);
      $("li.active").removeClass("active");
  } else if(val.toUpperCase() === "LAST »") {
      let currentActive = $("li.active");
      fetchUsers(totalPages-1);
      $("li.active").removeClass("active");

  } else if(val.toUpperCase() === "NEXT »") {
        let activeValue = parseInt($("ul.pagination li.active").text());
        if(activeValue < totalPages){
            let currentActive = $("li.active");
          startPage = activeValue;
          fetchUsers(startPage);
            // remove .active class for the old li tag
            $("li.active").removeClass("active");
            // add .active to next-pagination li
            currentActive.next().addClass("active");
        }
    } else if(val.toUpperCase() === "« PREV") {
        let activeValue = parseInt($("ul.pagination li.active").text());
        if(activeValue > 1) {
            // get the previous page
          startPage = activeValue - 2;
          fetchUsers(startPage);
            let currentActive = $("li.active");
            currentActive.removeClass("active");
            // add .active to previous-pagination li
            currentActive.prev().addClass("active");
        }
    } else {
      startPage = parseInt(val - 1);
      fetchUsers(startPage);
        // add focus to the li tag
        $("li.active").removeClass("active");
        $(this).parent().addClass("active");
      //$(this).addClass("active");
    }
});

(function(){
  fetchUsers(0);
})();
});

function Del(name) {
  return confirm("Bạn có chắc muốn xóa " + name + "?");
}
