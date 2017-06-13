package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog



internal fun showDialog(ctx: Context, type: Int, title: String, message: String){
    val error: SweetAlertDialog = SweetAlertDialog(ctx, type)
    error.titleText = title
    error.contentText = message
    error.show()
}

fun Context.showError(title: String, message: String) {
    showDialog(this, SweetAlertDialog.ERROR_TYPE, title, message)
}

fun Context.showWarning(title: String, message: String) {
    showDialog(this, SweetAlertDialog.WARNING_TYPE, title, message)
}
