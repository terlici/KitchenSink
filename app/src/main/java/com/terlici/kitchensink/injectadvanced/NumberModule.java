package com.terlici.kitchensink.injectadvanced;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class NumberModule {
    @Provides
    public RandomService providesRandomService() {
        return new RandomService(System.currentTimeMillis(), 1000);
    }
}
