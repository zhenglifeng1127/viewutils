package com.zero.viewutils.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zero.viewutils.utils.StatusBarUtils
import com.kingja.loadsir.core.LoadSir
import com.zero.viewutils.R
import com.zero.viewutils.ui.LoadingPopup
import com.zero.viewutils.utils.PopupUtils
import com.zero.viewutils.utils.extends.getActiveNetworkInfo
import com.zero.viewutils.utils.extends.getResColor
import com.zero.viewutils.utils.extends.hideInput
import com.zero.viewutils.utils.extends.isShouldHideKeyboard
import com.zero.viewutils.utils.singleton.AppManager
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<VM : BaseVM> : AppCompatActivity() {

    private var loading: LoadingPopup? = null

    protected lateinit var viewModel: VM

    private val service by lazy {
        LoadSir.getDefault().register(bindBody()) {
            checkNet(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        AppManager.add(this)

        setContentView(bindView())

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏


        viewModel = ViewModelProvider(this).get(viewModel())

        viewModel.initData(intent.extras)

        initStatusBar(R.color.white)

        initArgs()

        initMap(savedInstanceState)

        checkNet(false)

        initData()

        viewModel.loading.observe(this, Observer {
            if (it) {
                loading = PopupUtils.showLoad()
            } else {
                loading?.dismiss()
            }
        })



        initListener()

        lifecycle.addObserver(viewModel)
    }

    open fun initMap(savedInstanceState: Bundle?) {

    }

    protected abstract fun viewModel(): Class<VM>

    /**
     * 初始化配置
     */
    protected abstract fun initArgs()

    /**
     * 初始化数据调用
     */
    protected abstract fun initData()

    open fun initListener() {

    }

    protected abstract fun bindBody(): Any

    /**
     * 绑定DB布局
     */
    abstract fun bindView(): Int

    open fun isNullTranslucent(): Boolean = false

    /**
     * 状态栏配置
     */
    private fun initStatusBar(@ColorRes id: Int) {

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R->{
                window.setDecorFitsSystemWindows(false)
                window.statusBarColor = getResColor(id)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> { //5.0及以上
                val decorView: View = window.decorView
                val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                decorView.systemUiVisibility = option
                //根据上面设置是否对状态栏单独设置颜色
                window.statusBarColor = getResColor(id) //设置状态栏背景色
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> { //4.4到5.0
                val localLayoutParams: WindowManager.LayoutParams = window.attributes
                localLayoutParams.flags =
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
            }
            else -> {
                L.i("低于4.4的android系统版本不存在沉浸式状态栏")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //android6.0以后可以对状态栏文字颜色和图标进行修改
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }



    /**
     * 销毁数据时调用
     */
    override fun onDestroy() {
        AppManager.remove(this)
        hideInput()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {

        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let {
                if (it.isShouldHideKeyboard(ev)) {
                    hideInput()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun checkNet(isRefresh:Boolean) {
        if (!getActiveNetworkInfo()) {
            service.showCallback(NetCallback::class.java)
        }else{
            if(isRefresh){
                viewModel.onCreate()
            }
            service.showSuccess()
        }
    }

}