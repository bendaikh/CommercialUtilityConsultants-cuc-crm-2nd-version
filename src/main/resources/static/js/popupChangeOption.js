$(document).ready(function() {
  $("#permit").change(function() {
    var sel = $(this).val();
    $("#" + sel).modal();
  });
});