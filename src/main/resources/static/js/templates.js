const Views = {
    searchButton: 'common/searchButton.hbs',
};

/**
 * Takes care of loading template files from the file system using the lazy load method.
 */
const ViewLoader = (() => {
    const VIEW_BASE_DIR = '/static/templates/';
    let loadedViews = {};

    const getView = (viewName, successCallback, failCallback) => {
        $.get(VIEW_BASE_DIR + viewName).then((viewContent) => {
            loadedViews[viewName] = viewContent;
            successCallback(viewContent);
        }).catch((err) => failCallback ? failCallback(err) : alert(`${viewName} was not found!`));
    };

    return {
        loadView(viewName, successCallback, failCallback) {
            if (!viewName.endsWith('.hbs')) {
                viewName += '.hbs';
            }

            if (loadedViews[viewName]) {
                successCallback(loadedViews[viewName]);
                return;
            }

            getView(viewName, successCallback, failCallback);
        }
    }
})();

const ViewManager = (() => {
    //Register global variable for locale phrases as a helper.
    Handlebars.registerHelper('dict', function (phrase) {
        return Dictionary[phrase] ? Dictionary[phrase] : 'Invalid Phrase';
    });

    Handlebars.registerHelper('if_eq', function (v1, v2, options) {
        if (v1 == v2) {
            return options.fn(this);
        }

        return options.inverse(this);
    });

    let DEFAULT_CONTAINER = $(document.body);

    return {
        /**
         * @param viewName
         * @param templateContainer - the HTML element that will keep hold of the view, defaults to document.body
         * @param data - the data for handlebars
         * @param resultCallback
         */
        showView(viewName, templateContainer, data, resultCallback) {
            const result = (resStatus) => resultCallback ? resultCallback(resStatus) : null;

            if (!templateContainer) {
                templateContainer = DEFAULT_CONTAINER;
            }

            ViewLoader.loadView(viewName, (viewData) => {
                templateContainer.html(Handlebars.compile(viewData)(data));
                result(true);
            });
        },

        compileView(viewName, data, resultCallback) {
            ViewLoader.loadView(viewName, (viewData) => {
                resultCallback(Handlebars.compile(viewData)(data));
            });
        },
    }
})();