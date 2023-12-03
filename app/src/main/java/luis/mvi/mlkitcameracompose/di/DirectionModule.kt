package luis.mvi.mlkitcameracompose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import luis.mvi.mlkitcameracompose.screen.HomeDirection
import luis.mvi.mlkitcameracompose.screen.HomeDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindHomeDirection(impl: HomeDirectionImpl): HomeDirection
}