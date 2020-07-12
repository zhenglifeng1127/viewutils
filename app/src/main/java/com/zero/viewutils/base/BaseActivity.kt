package com.zero.viewutils.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
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

        initStatusBar(R.color.colorPrimaryDark, 0)

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
    private fun initStatusBar(@ColorRes id: Int, alpha: Int) {
        if (alpha in 0..255) {
            if (!isNullTranslucent()) {
                //开启沉浸式
                StatusBarUtils.setTranslucentForCoordinatorLayout(
                    this,
                    ContextCompat.getColor(this, id),
                    alpha
                )
            } else {
                //不开启
                StatusBarUtils.setColor(this, ContextCompat.getColor(this, id), alpha)
            }
        } else {
            RuntimeException("The value of the alpha must between 0 and 255")
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