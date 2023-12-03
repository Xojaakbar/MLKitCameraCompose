package luis.mvi.mlkitcameracompose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import luis.mvi.mlkitcameracompose.navigation.AppNavigator
import luis.mvi.mlkitcameracompose.navigation.NavigationDispatcher
import luis.mvi.mlkitcameracompose.navigation.NavigationHandler

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigator(impl: NavigationDispatcher): AppNavigator

    @Binds
    fun bindNavigationHandler(impl: NavigationDispatcher): NavigationHandler
}


