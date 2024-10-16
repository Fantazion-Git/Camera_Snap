package com.example.camerasnap.di

import com.example.camerasnap.MainActivity
import com.example.camerasnap.presentation.MainViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {

    fun getMainViewModelFactory(): MainViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: MainActivity): MainComponent
    }
}