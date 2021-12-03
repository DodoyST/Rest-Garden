package com.example.restgarden.di.module

import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.ui.screen.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
  
  @ContributesAndroidInjector
  abstract fun contributeHomeActivity(): HomeActivity
  
  @ContributesAndroidInjector
  abstract fun contributeHomeFragment(): HomeFragment
}
