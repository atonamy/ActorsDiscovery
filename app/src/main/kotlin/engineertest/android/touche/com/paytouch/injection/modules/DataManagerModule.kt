package engineertest.android.touche.com.paytouch.injection.modules

import dagger.Module
import dagger.Provides
import engineertest.android.touche.com.paytouch.network.ActorsApi
import engineertest.android.touche.com.paytouch.network.ActorsApiBuilder

/**
 * Created by archie on 22/5/17.
 */

@Module
class DataManagerModule {
    @Provides
    fun provideActorsApi(): ActorsApi {
        return ActorsApiBuilder().build()
    }
}