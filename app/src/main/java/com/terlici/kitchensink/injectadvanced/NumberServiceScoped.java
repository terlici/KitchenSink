package com.terlici.kitchensink.injectadvanced;

import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.scopes.FragmentScoped;

@FragmentScoped
public class NumberServiceScoped {

    private final int number;

    @Inject
    public NumberServiceScoped() {
        number = new Random().nextInt();
    }

    public String getText() {
        return "The scoped number is " + number;
    }
}
