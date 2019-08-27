function DatePicker(inputSelector) {

    const properties = {
        language: 'en',
        timeFormat: 'hh:ii AA',
    };

    const setMinDate = (date) => properties.minDate = date;

    const setMaxDate = (date) => properties.maxDate = date;

    const setStartDate = (date) => properties.startDate = date;

    const showTodayButton = () => properties.todayButton = new Date();

    const showTimePicker = () => properties.timepicker = true;

    const setAutoClose = (autoClose) => properties.autoClose = autoClose;

    const initialize = () => {
        const el = $(inputSelector);
        el.datepicker(properties);
        el.attr('autocomplete', 'off');
        el.on('keydown', function (e) {
            e.preventDefault();
        });

        return el;
    };

    return {
        setMinDate, setMaxDate, setStartDate, showTodayButton, showTimePicker, initialize, setAutoClose,
    }
}

const DatePickerPresets = (() => {

    return {
        datePicker(datePicker, initialize = true) {
            datePicker.showTodayButton();
            datePicker.setAutoClose(true);

            if (initialize) {
                datePicker.initialize();
            }
        },

        datePickerFromToday(datePicker, initialize = true) {
            this.datePicker(datePicker, false);
            datePicker.setMinDate(new Date());
            datePicker.setStartDate(new Date());

            if (initialize) {
                datePicker.initialize();
            }
        },

        dateTimePicker(datePicker, initialize = true) {
            datePicker.showTodayButton();
            datePicker.showTimePicker();

            if (initialize) {
                datePicker.initialize();
            }
        },

        dateTimePickerFromToday(datePicker, initialize) {
            this.datePickerFromToday(datePicker, false);
            datePicker.setAutoClose(false);
            datePicker.showTimePicker();

            if (initialize) {
                datePicker.initialize();
            }
        },

        datePickerFrom(datePicker, startDate, initialize = true) {
            this.datePicker(datePicker, false);
            datePicker.setMinDate(startDate);
            datePicker.setStartDate(startDate);

            if (initialize) {
                datePicker.initialize();
            }
        },

        datePickerFromTo(datePicker, startDate, maxDate, initialize = true) {
            this.datePickerFrom(datePicker, startDate, false);
            datePicker.setMaxDate(maxDate);

            if (initialize) {
                datePicker.initialize();
            }
        },
    }
})();