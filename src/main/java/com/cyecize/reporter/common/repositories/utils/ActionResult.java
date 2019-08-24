package com.cyecize.reporter.common.repositories.utils;

public class ActionResult<T> {

    private T result;

    public ActionResult() {

    }

    public T get() {
        return (T) this.result;
    }

    public void set(T result) {
        this.result = result;
    }
}
