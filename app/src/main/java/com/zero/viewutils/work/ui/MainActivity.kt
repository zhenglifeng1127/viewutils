package com.zero.viewutils.work.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.zero.viewutils.R
import com.zero.viewutils.base.BaseActivity
import com.zero.viewutils.base.L
import com.zero.viewutils.utils.ToolbarUtils
import com.zero.viewutils.work.vm.NormalVM
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_toolbar_normal.*

class MainActivity : BaseActivity<NormalVM>(){

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun viewModel(): Class<NormalVM> {
        return NormalVM::class.java
    }

    override fun initArgs() {
        L.i("start","-----------------------")
    }

    override fun initData() {
        ToolbarUtils.Builder().apply {
            toolbar = _toolbar
            titleView = _toolbarTitle
            titleText = "首页"
        }.build()
    }

    override fun bindBody(): Any {
        return mainBody
    }

    override fun bindView(): Int {
        return R.layout.activity_main
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            666->{
                L.i("this photo click back and get data")
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}