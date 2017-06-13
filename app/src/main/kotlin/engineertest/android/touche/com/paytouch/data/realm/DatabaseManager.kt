package engineertest.android.touche.com.paytouch.data.realm

import android.content.Context
import co.metalab.asyncawait.async
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by archie on 22/5/17.
 */
class DatabaseManager{

    fun <T> executeDbQuery(execution: (db: Realm) -> T): T {
        val db = realmInstance
        val result = execution(db)
        db.close()
        return result
    }

    fun <T> executeDbQueryAsync(execution: (db: Realm) -> T, result: (result: T?) -> Unit) {
       async {
           var r: T? = null
           await {
               r = executeDbQuery(execution)
           }
           result(r)
       }
    }

    fun release() {
        async.cancelAll()
    }

    companion object {

        private var realmConfig: RealmConfiguration? = null

        fun init(context: Context) {
            Realm.init(context)
        }

        private val realmInstance: Realm
        get() {
            if (realmConfig == null)
                realmConfig = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()

            return Realm.getInstance(realmConfig)
        }
    }

}