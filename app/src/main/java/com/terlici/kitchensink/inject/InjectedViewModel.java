package com.terlici.kitchensink.inject;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InjectedViewModel extends ViewModel {

    private final AdvancedService advancedService;

    @Inject
    public InjectedViewModel(AdvancedService advancedService) {
        super();
        this.advancedService = advancedService;
    }

    public String getText() {
        return advancedService.getText() + " - InjectedViewModel";
    }
}
