const SearchPresets = (() => {

    return {
        project(table, container) {
            TableUtils.createSearchBarForTable(table, container, {
                showLabel: true,
                hideOptions: false,
                options: [
                    'Id',
                    'Project Name',
                    'Owner'
                ],
                searchFieldId: 'searchProjectsField',
            });
        },

        task(table, container) {
            TableUtils.createSearchBarForTable(table, container, {
                showLabel: true,
                hideOptions: false,
                options: [
                    'Id',
                    'Task Name',
                ],
                searchFieldId: 'searchTasksField',
            });
        },
    };
})();