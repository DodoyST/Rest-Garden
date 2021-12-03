package com.example.restgarden.util

import android.app.Application
import com.example.restgarden.di.component.AppComponent
import com.example.restgarden.di.component.DaggerAppComponent
import com.example.restgarden.di.module.DataModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApplication : Application(), HasAndroidInjector {
  @Inject
  lateinit var dispatchAndroidInjector: DispatchingAndroidInjector<Any>
  
  private fun appComponent(): AppComponent =
    DaggerAppComponent.builder().dataModule(DataModule(this@BaseApplication)).build()
  
  override fun onCreate() {
    super.onCreate()
    appComponent().inject(this)
  }
  
  override fun androidInjector(): AndroidInjector<Any> = dispatchAndroidInjector
}
