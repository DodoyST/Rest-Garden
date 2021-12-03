package com.example.restgarden.di.component

import com.example.restgarden.di.module.ActivityModule
import com.example.restgarden.di.module.ApiModule
import com.example.restgarden.di.module.DataModule
import com.example.restgarden.di.module.GraveModule
import com.example.restgarden.util.BaseApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class, DataModule::class, ActivityModule::class, GraveModule::class, AndroidInjectionModule::class])
@Singleton
interface AppComponent {
  fun inject(baseApplication: BaseApplication)
}
