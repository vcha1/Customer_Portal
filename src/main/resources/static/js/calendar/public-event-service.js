/**
 *
 */

var EventService = {

    /*
     * @Params: calendar name, start date, end date
     */
    gettingCurrentEvents: function (resources, startDate, endDate, timezone) {

        var def = $.Deferred();

        var start = startDate.tz(timezone).startOf('day').format();
        var end = endDate.tz(timezone).endOf('day').format();

        var ids = [];
        for (var i = 0; i < resources.length; i++) {
            ids.push(resources[i].id);
        }

        console.log('Retrieving events between ' + start + ' AND ' + end);

        $.ajax({

            url: '/public/event',
            dataType: 'json',
            method: "GET",
            traditional: true,
            contentType: 'application/json; charset=utf-8',
            data: {
                calendarIds: ids,
                start: start,
                end: end
            },
            success: function (truckrolls) {
                def.resolve(truckrolls);
            },

            error: function (error) {
                def.reject(error);
            }

        });

        return def.promise();

    },

    /*
     * Return a promise; promise return array suggestions
     */
    gettingSuggestions: function (truckRollSuggestionRequest) {

        var $ret = $.Deferred();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/public/suggestion',
            dataType: 'json',
            method: "POST",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(truckRollSuggestionRequest),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (suggestions) {
                $ret.resolve(suggestions);
            },
            error: function (error) {
                $ret.reject('there was an error while retrieving suggestions\n'
                    + error);
            }
        });

        return $ret.promise();
    }

}