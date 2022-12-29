$(function () {
    var durationLabels = {
        '15': '15 Mins.',
        '30': '30 Mins.',
        '60': '1 Hr.',
        '120': '2 Hr.',
        '180': '3 Hr.',
        '240': '4 Hr.',
        '300': '5 Hr.',
        '360': '6 Hr.',
        '420': '7 Hr.',
        '480': '8 Hr.'
    };

    $('.duration').each(function () {
        var duration = $(this).data('duration');
        var label = durationLabels[duration];
        if (label) {
            $(this).html(label);
        }
    });
});