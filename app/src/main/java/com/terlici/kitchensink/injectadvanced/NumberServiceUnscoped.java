package com.terlici.kitchensink.injectadvanced;

import android.app.Application;
import android.content.Context;

import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class NumberServiceUnscoped {

    private final Context context;
    private final Application application;
    private final int number;

    @Inject
    public NumberServiceUnscoped(@ApplicationContext Context context, Application application) {
        this.context = context;
        this.application = application;
        number = new Random().nextInt();
    }

    public String getText() {
        return "The unscoped number is " + number;
    }
}
