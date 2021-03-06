package com.example.restgarden.di.module

import com.example.restgarden.ui.AuthActivity
import com.example.restgarden.ui.HomeActivity
import com.example.restgarden.ui.screen.*
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
  
  @ContributesAndroidInjector
  abstract fun contributeProfileFragment(): ProfileFragment
  
  @ContributesAndroidInjector
  abstract fun contributeRegisterFragment(): RegisterFragment
  
  @ContributesAndroidInjector
  abstract fun contributeBookingFragment(): BookingFragment
  
  @ContributesAndroidInjector
  abstract fun contributeBookingListFragment(): BookingListFragment
  
  @ContributesAndroidInjector
  abstract fun contributeBookingDetailFragment(): BookingDetailFragment
  
  @ContributesAndroidInjector
  abstract fun contributeBuyFragment(): BuyFragment
  
  @ContributesAndroidInjector
  abstract fun contributeReSubscribeFragment(): ExtendFragment
  
  @ContributesAndroidInjector
  abstract fun contributeTransactionListFragment(): TransactionListFragment
  
  @ContributesAndroidInjector
  abstract fun contributeTransactionDetailFragment(): TransactionDetailFragment
  
  @ContributesAndroidInjector
  abstract fun contributeUserFormFragment(): UserFormFragment
}
