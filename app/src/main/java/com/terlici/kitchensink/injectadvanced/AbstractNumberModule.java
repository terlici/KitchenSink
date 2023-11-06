package com.terlici.kitchensink.injectadvanced;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class AbstractNumberModule {
    @Binds
    abstract public RandomServiceInterface bindsRandomService(RandomService randomService);
}
