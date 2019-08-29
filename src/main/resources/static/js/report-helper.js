$(function () {
    const form = $('#reportTimeForm');

    const selectProjectField = form.find('#selectProject');
    const selectTaskField = form.find('#selectTask');
    const reportedDateFieldId = '#reportedDateTime';

    selectProjectField.selectize({});
    selectTaskField.selectize({});

    const selectProjectFieldSelectizeInstance = selectProjectField[0].selectize;
    const selectTaskFieldSelectizeInstance = selectTaskField[0].selectize;

    const initDateField = (fieldId) => {
        const minDate = new Date();
        minDate.setDate(1);

        const picker = new DatePicker(fieldId);

        DatePickerPresets.dateTimePicker(picker, false);
        picker.setMinDate(minDate);

        picker.initialize();
    };

    const requestItems = (url, callback) => {
        $.ajax({
            url,
            success: function (res) {
                callback(res.response.items);
            },
            error: console.error
        });
    };

    const initializeProjectField = (projectSelectize) => {
        requestItems(`/projects/involved/${USER_ID}`, (projects) => {
            projects.forEach(project => {
                projectSelectize.addOption({value: project.id, text: project.projectName});
            });
        });
    };

    const initializeTaskField = (tasksSelectize, projectField) => {
        projectField.on('change', function (e) {
            const selectedProject = $(this).val();
            if (selectedProject) {
                requestItems(`/tasks/project/${selectedProject}`, (tasks) => {
                    tasksSelectize.clearOptions();
                    tasks.forEach(task => {
                        tasksSelectize.addOption({value: task.id, text: task.taskName});
                    });
                });
            }
        });
    };

    initDateField(reportedDateFieldId);
    initializeProjectField(selectProjectFieldSelectizeInstance);
    initializeTaskField(selectTaskFieldSelectizeInstance, selectProjectField);
});
