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

    const initialize = () => {
        let el = $(inputSelector);
        el.datepicker(properties);
        el.attr('readonly', true);
    };

    return {
        setMinDate, setMaxDate, setStartDate, showTodayButton, showTimePicker, initialize,
    }
}

const DatePickerPresets = (() => {

    return {
        datePickerFromToday(datePicker) {
            datePicker.setMinDate(new Date());
            datePicker.showTodayButton();
            datePicker.setStartDate(new Date());

            datePicker.initialize();
        },
    }
})();