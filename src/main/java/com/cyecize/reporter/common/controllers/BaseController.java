package com.cyecize.reporter.common.controllers;

import com.cyecize.summer.common.models.ModelAndView;

public class BaseController {

    private ModelAndView finalizeView(ModelAndView modelAndView) {
        //add additional logic here like globals for example
        //modelAndView.addObject("dict", this.localLanguage.dictionary());
        return modelAndView;
    }

    protected ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }

    protected ModelAndView redirectWithError(String url, String errorMsg) {
        return this.redirect(url + "?error=" + errorMsg);
    }

    protected ModelAndView redirectWithInfo(String url, String infoMsg) {
        return this.redirect(url + "?info=" + infoMsg);
    }

    protected ModelAndView redirectWithSuccess(String url, String successMsg) {
        return this.redirect(url + "?success=" + successMsg);
    }

    protected ModelAndView view(String viewName) {
        return this.view(viewName, new ModelAndView());
    }

    protected ModelAndView view(String viewName, String modelName, Object model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(modelName, model);
        return this.view(viewName, modelAndView);
    }

    protected ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.setView(viewName);
        return this.finalizeView(modelAndView);
    }
}
