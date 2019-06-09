$(function() {
    feather.replace();

    $('[data-toggle="tooltip"]').tooltip();

    if ($('#loading').length) {
        $('#loading').hide().removeClass('d-none').fadeIn(1000);
        $.get("/recommend/results", function (html) {
            $('#loading').fadeOut(200);
            $('#results').hide().html(html).fadeIn(500);
            feather.replace();
        });
    }
});
