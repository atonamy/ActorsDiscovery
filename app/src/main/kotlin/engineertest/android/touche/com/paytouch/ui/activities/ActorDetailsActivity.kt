package engineertest.android.touche.com.paytouch.ui.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import android.view.WindowManager
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.data.DataManager
import engineertest.android.touche.com.paytouch.data.view_model.ActorViewModel
import engineertest.android.touche.com.paytouch.databinding.ActivityActorDetailsBinding
import engineertest.android.touche.com.paytouch.ui.adapters.FilmsAdapter

class ActorDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: ActorViewModel
    private val dataManager = DataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar?.hide()
        viewModel = ActorViewModel(this)
        initActorDetails {
            DataBindingUtil.setContentView<ActivityActorDetailsBinding>(this, R.layout.activity_actor_details)
                    .viewModel = viewModel
        }
    }

    fun initActorDetails(done: () -> Unit) {
        if(!intent.hasExtra("actor_id")) {
            finish()
            return
        }
        dataManager.getActor(intent.getIntExtra("actor_id", 0), {
            if(it != null) {
                viewModel.icon = ContextCompat.getDrawable(ActorDetailsActivity@this,
                        R.drawable.pin_black)
                viewModel.viewFontColor = ContextCompat
                        .getColor(ActorDetailsActivity@this, R.color.colorPrimaryDark)
                viewModel.setModel(it)
                viewModel.layoutManager = LinearLayoutManager(this)
                viewModel.adapter = FilmsAdapter(ActorDetailsActivity@this, it.filmography)
                viewModel.onClickListener  = {
                    onBackPressed()
                }
                done()
            }
            else
                finish()
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        dataManager.release()
    }

}
