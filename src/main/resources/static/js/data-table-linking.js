$(function () {
    $('.col-task-link').on('click', function (e) {
        const taskId = $(this).parent().data('task-id');
        location.href = "/tasks/details/" + taskId;
    });

    $('.col-project-link').on('click', function (e) {
        const id = $(this).parent().data('project-id');
        location.href = "/projects/details/" + id;
    });
});