package engineertest.android.touche.com.paytouch.injection.components

import dagger.Component
import engineertest.android.touche.com.paytouch.data.DataManager
import engineertest.android.touche.com.paytouch.injection.modules.DataManagerModule
import javax.inject.Singleton

/**
 * Created by archie on 22/5/17.
 */

@Singleton
@Component(modules = arrayOf(DataManagerModule::class))
interface DataManagerComponent {
    fun inject(startupActivity: DataManager)
}