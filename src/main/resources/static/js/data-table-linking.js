$(function () {
    $(document).on('click', '.col-task-link', function (e) {
        const taskId = $(this).parent().data('task-id');
        location.href = "/tasks/details/" + taskId;
    });

    $(document).on('click', '.col-project-link', function (e) {
        const id = $(this).parent().data('project-id');
        location.href = "/projects/details/" + id;
    });

    /**
     * targets a checkbox and reloads the page, sending param 'skipDisabled' with the checkbox value.
     */
    $('#showOnlyInProgress').on('change', function () {
        const skipCompleted = this.checked;
        location.href = location.href.replace(location.search, '') + `?skipCompleted=${skipCompleted}`;
    });
});