const TableUtils = (() => {

    return {
        getAllTdForTh(jQueryThTag) {
            let index = jQueryThTag.index();
            return jQueryThTag.parent().parent().parent().find(`tbody > tr > td:nth-child(${index + 1})`);
        },

        getAllTableRowsForTable(table) {
            return table.querySelectorAll('tbody > tr');
        },

        getCheckboxCheckEventListenerFromTableRow(tableRow, checkboxCheckedCallback) {
            return this.getCheckboxCheckEventListener(tableRow.querySelector('input.table-checkbox'), checkboxCheckedCallback);
        },

        getCheckboxCheckEventListener(checkboxElement, checkboxCheckedCallback) {
            checkboxElement.addEventListener('change', function (eventArgs) {
                const callbackResult = checkboxCheckedCallback(this.checked, checkboxElement);

                if (callbackResult !== undefined && typeof callbackResult === 'boolean') {
                    this.checked = callbackResult;
                }
            });
        },

        setVisibilityOfTableRows(listOfTableRows, visible) {
            let isVisible = visible ? '' : 'none';

            for (let tableRow of listOfTableRows) {
                tableRow.style.display = isVisible;
            }
        },

        createSearchBarForTable(table, searchBarContainer, options) {
            let defaultOptions = {
                showLabel: true,
                searchFieldId: DomUtils.getRandomDomId(),
                searchTypeId: DomUtils.getRandomDomId(),
                hideOptions: true,
                options: [],
            };

            if (options) {
                Object.assign(defaultOptions, options);
            }

            ViewManager.compileView(Views.searchButton, defaultOptions, searchBarHtml => {
                searchBarContainer.insertAdjacentHTML('beforeend', searchBarHtml);

                let searchField = document.getElementById(defaultOptions.searchFieldId);
                let searchTypeField = document.getElementById(defaultOptions.searchTypeId);

                DataTableGridUtils.initTableSearchField(table, searchField, searchTypeField);
            });
        }
    }
})();

const DataTableGridUtils = (() => {

    const TABLE_ADDABLE_TEMPLATE_CLASS = 'addable-table-row-template';
    const REGEXP_SPECIAL_CHARS = ['[', '^', '$', '.', '|', '?', '*', '+', '(', ')', '/']; //missing '\\'
    const ALL_ELEMENTS_SEARCH_TYPE = 'All';


    //createResizableGrid
    const createTableResizeHelperDiv = (height) => {
        let div = document.createElement('div');
        div.style.top = '0';
        div.style.right = '0';
        div.style.width = '5px';
        div.style.position = 'absolute';
        div.style.cursor = 'pointer';
        div.style.userSelect = 'none';
        div.style.height = `${height}px`;
        return div;
    };

    const calcPaddingDifForColumnResizing = (col) => {
        if (DomUtils.getComputedStyleVal(col, 'box-sizing') === 'border-box') {
            return 0;
        }

        let padLeft = DomUtils.getComputedStyleVal(col, 'padding-left');
        let padRight = DomUtils.getComputedStyleVal(col, 'padding-right');

        return (parseInt(padLeft) + parseInt(padRight));
    };

    const initResizeHelperDiv = (div) => {
        let properties = {
            pageX: null, currentColumn: null, nextColumn: null, currentColumnWidth: null, nextColumnWidth: null,
        };

        const clearProperties = (props) => Object.keys(props).forEach((prKey) => props[prKey] = null);

        //When user presses the mouse, collect the mouse coordinates.
        div.addEventListener('mousedown', (e) => {
            //The parent of the div helper is the column.
            properties.currentColumn = e.target.parentElement;
            properties.nextColumn = properties.currentColumn.nextElementSibling;
            properties.pageX = e.pageX;

            let padding = calcPaddingDifForColumnResizing(properties.currentColumn);

            properties.currentColumnWidth = properties.currentColumn.offsetWidth - padding;
            if (properties.nextColumn) {
                properties.nextColumnWidth = properties.nextColumn.offsetWidth - padding;
            }
        });

        //Add\Remove border on hover to resize helper div.
        div.addEventListener('mouseover', (e) => e.target.style.borderRight = '2px solid rgb(76, 154, 255)');
        div.addEventListener('mouseout', (e) => e.target.style.borderRight = '');

        //Gets mouse coordinates and applies them to the columns' widths.
        document.addEventListener('mousemove', (e) => {
            window.requestAnimationFrame(() => {
                if (!properties.currentColumn) {
                    return;
                }

                //The difference between the starting and the current mouse X coordinates.
                let diffX = e.pageX - properties.pageX;

                //If the column is the right most one, return.
                if (!properties.nextColumn) {
                    return;
                }

                properties.currentColumn.style.width = (properties.currentColumnWidth + diffX) + 'px';
                properties.nextColumn.style.width = (properties.nextColumnWidth - (diffX)) + 'px';
            });
        });

        //When user releases the mouse, clear coordinates.
        document.addEventListener('mouseup', (e) => clearProperties(properties));
    };

    //makeTableHoverable
    /**
     * @param hoveredElement the td tag that was hovered
     * @param hoverableTable the table
     * @param addOrRemoveMethod add|remove
     */
    const handleTableHover = (hoveredElement, hoverableTable, addOrRemoveMethod) => {
        let colIndex = $(hoveredElement).index();

        hoverableTable.find('tr:first').children()[colIndex].classList[addOrRemoveMethod]('hovered-col-head');

        //find all tr tags, skip the first.
        hoverableTable.find('tr:gt(0)').each((ind, el) => {
            //for each tr, get children (td) and get the child at colIndex, get its classList and call remove/add
            $(el).children()[colIndex].classList[addOrRemoveMethod]('hovered-col');
        });
    };

    //makeTableHaveAddableRows
    /**
     * Removes the row and calls onTableRemove if present.
     * If tr has attribute data-row-id, gets the id and passes it.
     * @param table
     */
    const initRemoveRowEvents = (table) => {

        const removeRow = (row) => {
            const rowId = row.data('row-id');

            if (window.onTableRowRemove) {
                window.onTableRowRemove(table, this, rowId);
            }

            row.remove();
        };

        //Used this since rows can be appended at runtime.
        table.on('click', 'td.remove-row-btn', function (e) {
            const parent = $(this).parent();
            removeRow(parent);
        });

        table.on('click', 'td > .remove-row-btn', function (e) {
            const parent = $(this).parent().parent();
            removeRow(parent);
        });
    };

    const initAddRowEvents = (table, buttons, rowTemplate) => {
        buttons.on('click', (e) => {
            let newRow = rowTemplate.cloneNode(true);
            newRow.classList.remove(TABLE_ADDABLE_TEMPLATE_CLASS);

            if (window.onTableRowAdd) {
                window.onTableRowAdd(table, newRow);
            }

            table.find('tbody:first').append(newRow);
        });
    };

    /**
     * Looks for th tags with class .searchable-col and gets all
     * corresponding td tags.
     * <p>
     * Sorts the tags by search type attribute if present.
     *
     * @param table
     */
    const getSearchableColumns = (table) => {
        let allTdElements = {};
        allTdElements[ALL_ELEMENTS_SEARCH_TYPE] = [];


        let allSearchableColumns = table.querySelectorAll('th.searchable-col');

        for (let searchableCol of allSearchableColumns) {
            let allCorrespondingTdTags = TableUtils.getAllTdForTh($(searchableCol)).toArray();
            allTdElements[ALL_ELEMENTS_SEARCH_TYPE] = allTdElements[ALL_ELEMENTS_SEARCH_TYPE].concat(allCorrespondingTdTags);

            if (searchableCol.getAttribute('search-type')) {
                allTdElements[searchableCol.getAttribute('search-type')] = allCorrespondingTdTags;
            }
        }

        return allTdElements;
    };

    const buildSearchRegEx = (searchFieldVal) => {
        searchFieldVal = searchFieldVal.replace(/\\/g, '\\\\');

        for (let specialChar of REGEXP_SPECIAL_CHARS) {
            searchFieldVal = searchFieldVal.replace(new RegExp(`\\${specialChar}`, 'g'), '\\' + specialChar);
        }

        searchFieldVal = searchFieldVal.replace(/\s+/g, '.*?');

        return new RegExp(searchFieldVal, 'gi');
    };

    const search = (table, searchField, searchTypeField) => {
        let searchValue = searchField.value;

        //Since many DOM operations will be executed, we will wrap them in a single animation frame.
        window.requestAnimationFrame(() => {
            let allTableRows = TableUtils.getAllTableRowsForTable(table);

            let isSearchFieldEmpty = !searchValue || searchValue.trim() === '';
            TableUtils.setVisibilityOfTableRows(allTableRows, isSearchFieldEmpty);

            if (isSearchFieldEmpty) {
                return;
            }

            let allSearchableColumns = getSearchableColumns(table);
            let generatedRegEx = buildSearchRegEx(searchValue);

            let searchedColumnsBySearchType = allSearchableColumns[ALL_ELEMENTS_SEARCH_TYPE];
            if (searchTypeField) {
                searchedColumnsBySearchType = allSearchableColumns[searchTypeField.value]
            }

            for (let searchableColumn of searchedColumnsBySearchType) {
                let colSearchData = searchableColumn.getAttribute('data-search');
                if (!colSearchData) {
                    colSearchData = searchableColumn.innerText;
                }

                if (colSearchData && colSearchData.search(generatedRegEx) !== -1) {
                    TableUtils.setVisibilityOfTableRows([searchableColumn.parentNode], true);
                }
            }
        });
    };

    return {
        /**
         * Adds functionality to tables with class 'resizable-table'
         * to be able to resize their columns.
         * @param table - The DOM table element
         */
        createResizableGrid(table) {
            let row = table.getElementsByTagName('tr')[0], columns = row ? row.children : undefined;
            if (!columns) return;

            table.style.overflow = 'hidden';

            for (const column of columns) {
                let resizeHelperDiv = createTableResizeHelperDiv(column.offsetHeight);
                column.style.position = 'relative';
                column.appendChild(resizeHelperDiv);

                initResizeHelperDiv(resizeHelperDiv);
            }
        },

        /**
         * Scans the table for th tags with class col-editable and adds
         * contentEditable attribute to make them editable
         * @param table - The DOM table element
         */
        makeTableEditable(table) {
            table = $(table);
            table.find('th.col-editable').each((ind, el) => {
                let index = $(el).index();

                //For each tbody's tr tags, each th in those tr tags that is on position index + 1.
                table.find(`tbody > tr > td:nth-child(${index + 1})`).attr('contenteditable', 'true');
            });
        },

        /**
         * Makes table hoverable - When hovering over the table, the columns will be highlighted.
         * Listens for hover on each td and add class to each row's corresponding col.
         * @param table
         */
        makeTableHoverable(table) {
            table = $(table);

            //using table.on instead of directly td because row might be appended at runtime.
            table.on('mouseover', 'td', function (e) {
                handleTableHover(this, table, 'add');
            });

            table.on('mouseout', 'td', function (e) {
                handleTableHover(this, table, 'remove');
            });
        },

        /**
         * Makes table able to add rows where there is a hidden row used as a template.
         * @throws Error if there is no tr with class 'addable-table-row-template'.
         * @throws Error if there is not a single element with class 'add-row-btn'.
         * @param table
         * @param tableParent the table container which should hold the add btn
         */
        makeTableHaveAddableRows(table, tableParent) {
            table = $(table);
            tableParent = $(tableParent);

            const rowTemplate = table.find(`tr.${TABLE_ADDABLE_TEMPLATE_CLASS}`)[0];
            const addTemplateButtons = tableParent.find('.add-row-btn');

            if (!rowTemplate) {
                throw new Error('No row template found!');
            }

            if (addTemplateButtons.length < 1) {
                throw new Error('No button for adding rows present in the table container');
            }

            initRemoveRowEvents(table);
            initAddRowEvents(table, addTemplateButtons, rowTemplate);
        },

        makeTableDataExportable(table, parentTable) {
            let buttonsExportData = $(parentTable).find('.export-table-data-btn');
            let exportableCells = $(table).find('th.exportable-col').toArray();

            buttonsExportData.on('click', (e) => {
                let data = {};

                for (let exportableCell of exportableCells) {
                    exportableCell = $(exportableCell);
                    let columnName = exportableCell.attr('name') ? exportableCell.attr('name') : exportableCell.text();
                    data[columnName] = [];

                    let tdTags = TableUtils.getAllTdForTh(exportableCell).toArray();
                    for (let tdTag of tdTags) {
                        tdTag = $(tdTag);

                        //If the cell is template
                        if (tdTag.parent().hasClass(TABLE_ADDABLE_TEMPLATE_CLASS)) {
                            continue;
                        }

                        data[columnName].push(tdTag.attr('value') ? tdTag.attr('value') : tdTag.text());
                    }
                }

                if (window.onDataExport) {
                    window.onDataExport(table, data);
                }
            });
        },

        /**
         * Initializes search events for a given table.
         * Looks for search input field on the parent scope of the table.
         *
         * Adds keyup event which initially hides all rows and then shows the ones that match the search text.
         * Checks for columns with class .searchable-col and uses their value for searching.
         *      The value is the text of the col or 'data-search' property is present.
         *
         * @param table
         * @param tableParent
         */
        makeTableSearchable(table, tableParent) {
            const searchField = tableParent.querySelector('.table-search-field');

            if (!searchField) {
                console.error('No table search field found, put one inside the parent element of the table');
                return;
            }

            this.initTableSearchField(table, searchField);
        },

        initTableSearchField(table, searchField, searchTypeField) {
            searchField.addEventListener('keyup', function (eventArgs) {
                search(table, searchField, searchTypeField);
            });

            if (searchTypeField) {
                searchTypeField.addEventListener('change', function (eventArgs) {
                    search(table, searchField, searchTypeField);
                });
            }
        },
    };
})();

const DataTableSortUtils = (() => {

    const DIRECTION_KEY = 'direction';
    const ACTIVE_DIRECTION_CLASS_NAME = 'text-info';

    const directionType = {
        ASC: {name: 'ASC', icon: 'upIcon', value: 1, opposite: 'DESC'},
        DESC: {name: 'DESC', icon: 'downIcon', value: -1, opposite: 'ASC'}
    };

    const initializedColumnsUtils = {
        clearColumnsButtonsState(initializedColumns) {
            for (let column of initializedColumns) {
                column.upIcon.removeClass(ACTIVE_DIRECTION_CLASS_NAME);
                column.downIcon.removeClass(ACTIVE_DIRECTION_CLASS_NAME);
            }
        },

        setActiveIcon(columns, initializedColumn, iconName) {
            this.clearColumnsButtonsState(columns);
            initializedColumn[iconName].addClass(ACTIVE_DIRECTION_CLASS_NAME);
        },
    };

    /**
     * Sets the direction to ASC if it is not present.
     * Adds arrows for up and down.
     * @param columns
     */
    const initSortableColumns = (columns) => {
        let initializedColumns = [];

        columns.each((ind, el) => {
            el = $(el);
            let elementDirection = el.data(DIRECTION_KEY);

            if (!elementDirection || Object.keys(directionType).indexOf(elementDirection) === -1) {
                el.data(DIRECTION_KEY, directionType.ASC.name);
            }

            const buttonsContainer = $('<span class="col-sortable-icons">');
            const upIcon = $('<span>').append('<i class="fa fa-long-arrow-down" aria-hidden="true"></i>');
            const downIcon = $('<span>').append('<i class="fa fa-long-arrow-up" aria-hidden="true"></i>');

            el.append(buttonsContainer.append(upIcon).append(downIcon));

            initializedColumns.push({
                element: el,
                upIcon,
                downIcon,
            });
        });

        return initializedColumns;
    };

    const rearrangeRows = (table, sortedTdTags) => {
        let rows = [];

        for (let tdTag of sortedTdTags) {
            rows.push($(tdTag).parent());
        }

        let tbody = table.find('tbody:first');

        tbody.html('');
        for (let row of rows) {
            tbody.append(row);
        }
    };

    const initSortableColumnsEvents = (table, initializedColumns) => {
        for (let column of initializedColumns) {
            column.element.on('click', function (e) {
                window.requestAnimationFrame((delta) => {
                    let direction = directionType[$(this).data(DIRECTION_KEY)];

                    let sortedTdTags = TableUtils.getAllTdForTh(column.element).sort(function (e1, e2) {
                        e1 = $(e1);
                        e2 = $(e2);

                        let e1DataSort = e1.data('sort') ? e1.data('sort') : e1.text();
                        let e2DataSort = e2.data('sort') ? e2.data('sort') : e2.text();

                        if (!isNaN(e1DataSort) && !isNaN(e2DataSort)) {
                            return (e1DataSort - e2DataSort) * direction.value;
                        }

                        return (e1DataSort + '').localeCompare(e2DataSort + '') * direction.value;
                    });

                    initializedColumnsUtils.setActiveIcon(initializedColumns, column, direction.icon);
                    $(this).data(DIRECTION_KEY, direction.opposite);

                    rearrangeRows(table, sortedTdTags);
                });
            });
        }
    };

    return {
        makeTableSortable(table) {
            table = $(table);
            const sortableColumns = table.find('th.col-sortable');

            let initializedColumns = initSortableColumns(sortableColumns);
            initSortableColumnsEvents(table, initializedColumns);
        },
    }
})();

$(() => {
    const tables = document.getElementsByTagName('table');

    for (let table of tables) {
        let tableClasses = table.classList;
        let tableParent = table.parentElement;

        if (tableClasses.contains("resizable-table")) {
            DataTableGridUtils.createResizableGrid(table);
        }

        if (tableClasses.contains('editable-table')) {
            DataTableGridUtils.makeTableEditable(table);
        }

        if (tableClasses.contains('hoverable-table') && tableClasses.contains('table-style')) {
            DataTableGridUtils.makeTableHoverable(table);
        }

        if (tableParent.classList.contains('addable-table-container') && tableClasses.contains('addable-table')) {
            DataTableGridUtils.makeTableHaveAddableRows(table, tableParent);
        }

        if (tableClasses.contains('sortable-table')) {
            DataTableSortUtils.makeTableSortable(table);
        }

        if (tableClasses.contains('data-exportable-table')) {
            DataTableGridUtils.makeTableDataExportable(table, tableParent);
        }

        if (tableClasses.contains('searchable-table')) {
            DataTableGridUtils.makeTableSearchable(table, tableParent);
        }
    }
});

//Example usage of table events.
// window.onTableRowRemove = function (table, row, rowId) {
//     console.log(rowId);
// };
//
// window.onTableRowAdd = function (table, newRow) {
//     console.log(`Added ${newRow}`);
//     $(newRow).data('row-id', 'aiejgiaegj');
// };
//
// window.onDataExport = function (table, data) {
//     console.log(data);
// };