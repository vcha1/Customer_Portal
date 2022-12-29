window.mobilecheck = function () {
    var check = false;
    (function (a) {
        if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true;
    })(navigator.userAgent || navigator.vendor || window.opera);
    return check;
};

/**/

$(document).ready(function () {

    var initialLoading = true;

    var resources = $('#calendar').data('resources');

    var appointment = $('#calendar').data('appointment');

    var renderEvents = $('#calendar').data('render-events') === 'true';

    var allowedToScheduleOnBehalfOfCustomer = $("meta[name='_allowed_to_scheduled_on_behalf_of_customer']").attr("content") === 'true';

    toastr.options = {
        "positionClass": "toast-bottom-full-width",
        "preventDuplicates": true
    };

    $.blockUI({message: '<div id=block-message></div>'});

    $('#startDateTime').datetimepicker({
        format: 'MM/DD/YYYY hh:mm A ZZ'
    });

    $('#go-to-date-picker').datetimepicker({
        format: 'MM/DD/YYYY',
        ignoreReadonly: true
    });

    // Initialize Calendar
    $('#calendar').fullCalendar({

        customButtons: {
            goToDateButton: {
                text: 'go to date',
                click: function () {
                    $('#go-to-date-modal').modal('show');
                    $('#go-to-date-icon').click();
                }
            }
        },

        header: {
            left: 'agendaDay,agendaWeek goToDateButton',
            center: 'title'
        },
        defaultDate: moment.tz(moment.tz.guess()).add(3, 'days').format('YYYY-MM-DD'),
        defaultView: window.mobilecheck() ? "agendaDay" : "agendaWeek",
        contentHeight: 'auto',
        timezone: moment.tz.guess(),
        minTime: '08:00:00',
        maxTime: '17:00:00',
        allDaySlot: false,
        navLinks: false,
        defaultTimedEventDuration: '00:30:00',
        themeSystem: 'bootstrap4',
        slotLabelInterval: 30,
        slotLabelFormat: 'hh:mm A',

        eventClick: function (event, jsEvent, view) {

        },

        loading: function (isLoading, view) {
            if (isLoading) {
                var start = view.start;
                var viewName = view.name;

                if (viewName === 'agendaWeek') {
                    if (initialLoading) {
                        $('#block-message').html(null == start ? 'Checking Availability...' : 'Checking Availability for week of ' + start.format('LL'));
                    } else {
                        $.blockUI({message: null == start ? 'Checking Availability...' : 'Checking Availability for week of ' + start.add(7, 'days').format('LL')});
                    }
                } else if (viewName === 'agendaDay') {
                    if (initialLoading) {
                        $('#block-message').html(null == start ? 'Checking Availability...' : 'Checking Availability for ' + start.format('LL'));
                    } else {
                        $.blockUI({message: null == start ? 'Checking Availability...' : 'Checking Availability for ' + start.add(1, 'days').format('LL')});
                    }
                } else {
                    $.blockUI({message: 'Checking Availability...'});
                }

            }

        },

        eventSources: [

            {

                events: function (start, end, timezone, render) {

                    var request = {
                        resources: resources,
                        start: start.tz(timezone).startOf('day'),
                        end: end.tz(timezone).endOf('day'),
                        timezone: timezone,
                        appointment: appointment,
                        blockDuration: 30,
                        tolerance: 0
                    };

                    $.when(EventService.gettingCurrentEvents(resources, start, end, timezone)).then(function (truckrolls) {

                        return Calendar.createEvents(truckrolls, timezone);

                    }).then(function (events) {

                        var def = $.Deferred();

                        $.when(EventService.gettingSuggestions(request)).then(function (suggestions) {

                            var backgroundEvents = Calendar.createBackgroundEvents(suggestions);
                            def.resolve({
                                events: events,
                                backgroundEvents: backgroundEvents
                            });

                        });

                        return def.promise();

                    }).then(function (allEvents) {

                        var events = [];

                        allEvents.events.forEach(function (event) {
                            events.push(event);
                        });

                        allEvents.backgroundEvents.forEach(function (event) {
                            events.push(event);
                        });

                        render(events);
                    });

                }

            }

        ],

        eventRender: function (event, element, view) {

            // Rendering existing events
            if (event.suggestionCode == null && !renderEvents)
                return false;

            $(element).data(event);

        },

        eventAfterAllRender: function (view) {

            // Render Timezone Abbreviation
            var date = $('.fc-day-header').eq(0).data('date');
            var time = $('tr[data-time]').eq(0).data('time');

            var datetime = moment.tz(date + ' ' + time, moment.tz.guess());

            $('.fc-axis').eq(0).css('text-align', 'center').text(datetime.zoneAbbr());

            var allEvents = $('#calendar').fullCalendar("clientEvents");

            var bgEventExistsWithinView = false;
            for (var i = 0; i < allEvents.length; i++) {
                var event = allEvents[i];
                if ('background' === event.rendering) {
                    bgEventExistsWithinView = true;
                    break;
                }
            }

            if (!bgEventExistsWithinView && initialLoading) {
                var viewName = view.name;
                var nextDate = view.start.add(viewName === 'agendaWeek' ? 7 : 1, 'days');
                Calendar.goToDate(nextDate);
            } else {
                initialLoading = false;
                $.unblockUI();
            }

        }


    })

    $('#confirm-go-to-date-btn').click(function () {
        var date = $('#go-to-date-picker').data("DateTimePicker").date();
        Calendar.goToDate(date);
    });

    $("#calendar").on("click", ".fc-bgevent", function (evt) {

        var event = $(this).data();
        console.log(event);

        if (allowedToScheduleOnBehalfOfCustomer) {

            if (event.suggestionCode === 3) {
                $('#resource').val(event.resource);
                $('#startDateTime').data("DateTimePicker").date(event.start);
                $('#schedule-modal').modal('show');
            }

        } else {
            toastr.warning('We are sorry, but you are not allowed to schedule.');
        }

    }).on("mouseenter", ".fc-bgevent", function (event) {
        $(this).css("opacity", "0.80");
    }).on("mouseleave", ".fc-bgevent", function (event) {
        $(this).css("opacity", "0.40");
    });

});