$(function() {
    feather.replace();

    $('[data-toggle="tooltip"]').tooltip();

    if ($('#recommendation__loading').length) {
        $('#recommendation__loading').hide().removeClass('d-none').fadeIn(1000);
        $.get("/recommend/results", function(html) {
            $('#recommendation__loading').fadeOut(200, function() {
                $('#recommendation__results').hide().html(html).fadeIn(500);
                feather.replace();
            });
        });
    }

    if ($('#book__prices').length) {
        $.get("/books/isbn/" + isbn10 + "/prices", function (html) {
            $('#book__prices__loading').hide();
            $('#book__prices__results').hide().html(html).show();
            feather.replace();
        });
    }
});
