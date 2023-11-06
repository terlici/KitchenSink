package com.terlici.kitchensink.injectadvanced;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.terlici.kitchensink.databinding.FragmentInjectedAdvancedBinding;
import com.terlici.kitchensink.databinding.FragmentInjectedBinding;
import com.terlici.kitchensink.inject.InjectedViewModel;
import com.terlici.kitchensink.inject.SimpleService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InjectedFragment extends Fragment {

    @Inject
    public NumberServiceScoped scopedFirst;
    @Inject
    public NumberServiceScoped scopedSecond;
    @Inject
    public NumberServiceUnscoped unscopedFirst;
    @Inject
    public NumberServiceUnscoped unscopedSecond;
    @Inject
    public RandomService randomService;
    @Inject
    public RandomServiceInterface interfacedRandomService;

    private FragmentInjectedAdvancedBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInjectedAdvancedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.text.setText(
                unscopedFirst.getText() + "\n" +
                unscopedSecond.getText() + "\n" +
                scopedFirst.getText() + "\n" +
                scopedSecond.getText() + "\n" +
                randomService.nextInt() + "\n" +
                interfacedRandomService.nextInt()
        );
    }
}
