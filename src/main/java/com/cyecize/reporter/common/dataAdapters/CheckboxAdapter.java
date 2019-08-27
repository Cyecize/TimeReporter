package com.cyecize.reporter.common.dataAdapters;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

@Component
public class CheckboxAdapter implements DataAdapter<Boolean> {

    @Override
    public Boolean resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        final String checked = httpSoletRequest.getBodyParameters().get(paramName);
        return checked != null && checked.equals("on");
     }
}
