package com.example.mylibrary.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.mylibrary.presentation.MainViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {

    fun getMainViewModelFactory(): MainViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: FragmentActivity): MainComponent
    }
}