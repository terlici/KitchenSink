package com.terlici.kitchensink.inject;

import javax.inject.Inject;

public class SimpleService {

    @Inject
    public SimpleService() {
    }

    public String getText() {
        return "Simple Service";
    }
}
