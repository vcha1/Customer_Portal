$(function () {
    var durationLabels = {
        '240': '4 Hr.',
        '360': '6 Hr.',
        '480': '1 Day',
        '600': '2 Days',
        '720': '2 Days',
        '840': '2 Days',
        '960': '2 Days'
    };

    $('.duration').each(function () {
        var duration = $(this).data('duration');
        var label = durationLabels[duration];
        if (label) {
            $(this).html(label);
        }
    });
});