const DomUtils = {
    removeClassesFromJQueryElement(jQElement, classesArr) {
        classesArr.forEach((cls) => jQElement.removeClass(cls));
    },

    addClassesToQueryElement(jQElement, classesArr) {
        classesArr.forEach((cls) => jQElement.addClass(cls));
    },

    getComputedStyleVal(elm, css) {
        return (window.getComputedStyle(elm, null).getPropertyValue(css))
    },

    getRandomDomId() {
        return Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5);
    },
};