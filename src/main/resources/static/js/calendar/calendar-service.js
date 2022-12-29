var Calendar = {

    createEvents: function(truckrolls, timezone, appointment) {

		var events = [];

		truckrolls.forEach(function(truckroll) {

		    if(appointment != null && truckroll.id === appointment.rescheduling)
		        return;

            events.push({
                id: truckroll.id,
                title: truckroll.name,
                startMoment: moment.tz(truckroll.startDate, timezone),
                endMoment: moment.tz(truckroll.endDate, timezone),
                start: moment.tz(truckroll.startDate, timezone).format(),
                end: moment.tz(truckroll.endDate, timezone).format(),
                address: truckroll.address,
                needsConfirmation: truckroll.needsConfirmation == null ? false : truckroll.needsConfirmation,
                timeOfConfirmation: truckroll.timeOfConfirmation,
                color: '#01579B',
                url: appointment == null ? '/truck-roll/management/view-detail?id=' + truckroll.id : null
            });

        });

        return events 

    },

    createBackgroundEvents: function(suggestions) {

        var backgroundEvents = [];

        if (suggestions.length == 0)
            return backgroundEvents;

        suggestions.forEach(function(suggestion) {

            var color = null;

            switch (suggestion.suggestionCode) {

                case 3:
                    color = '#4caf50';
                    break;
                default:
                    return;
            }

            backgroundEvents.push({
                title: suggestion.note,
                resource: suggestion.resource,
                start: suggestion.start,
                color: color,
                suggestionCode: suggestion.suggestionCode,
                rendering: 'background',
                className: 'backgroundEvent'
            });

        });

        return backgroundEvents;

    },

    goToDate: function(date) {
        $('#calendar').fullCalendar('gotoDate', date);
    }

};