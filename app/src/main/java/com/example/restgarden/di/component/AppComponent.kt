package com.example.restgarden.di.component

import com.example.restgarden.di.module.*
import com.example.restgarden.util.BaseApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [ApiModule::class, DataModule::class, ActivityModule::class, AuthModule::class, GraveModule::class, UserModule::class, AndroidInjectionModule::class])
@Singleton
interface AppComponent {
  fun inject(baseApplication: BaseApplication)
}
