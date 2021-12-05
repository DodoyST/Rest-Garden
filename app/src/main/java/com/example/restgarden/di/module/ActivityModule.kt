package com.example.restgarden.di.module

import com.example.restgarden.ui.AuthActivity
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.ui.screen.GraveDetailFragment
import com.example.restgarden.ui.screen.HomeFragment
import com.example.restgarden.ui.screen.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
  
  @ContributesAndroidInjector
  abstract fun contributeHomeActivity(): HomeActivity
  
  @ContributesAndroidInjector
  abstract fun contributeAuthActivity(): AuthActivity
  
  @ContributesAndroidInjector
  abstract fun contributeHomeFragment(): HomeFragment
  
  @ContributesAndroidInjector
  abstract fun contributeGraveDetailFragment(): GraveDetailFragment
  
  @ContributesAndroidInjector
  abstract fun contributeSignInFragment(): SignInFragment
}
