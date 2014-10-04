$(document).on("click", ".btn-success", function () {
    var enc = $.md5( $('#pass').val() );
    $('#pass').val(enc);
});
