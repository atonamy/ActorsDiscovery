package engineertest.android.touche.com.paytouch

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import engineertest.android.touche.com.paytouch.data.realm.DatabaseManager

/**
 * Created by archie on 24/5/17.
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(applicationContext)
        DatabaseManager.init(applicationContext)
    }
}