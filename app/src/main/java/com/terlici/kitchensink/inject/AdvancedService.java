package com.terlici.kitchensink.inject;

import javax.inject.Inject;

public class AdvancedService {

    @Inject
    public SimpleService simpleService;

    @Inject
    public AdvancedService() {
    }

    public String getText() {
        return simpleService.getText() + " - Advanced Service";
    }
}
