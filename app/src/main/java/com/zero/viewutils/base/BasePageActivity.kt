package com.zero.viewutils.base

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zero.viewutils.ui.ClickSwipeRefreshLayout
import com.zero.viewutils.utils.extends.clickChildItemDelay
import com.zero.viewutils.utils.extends.clickItemDelay

/**
 * 结合baseQuickAdapter库使用的recyclerview 分页处理基类
 */
abstract class BasePageActivity<VM : BasePageVM<T,*>, P : BaseQuickAdapter<T, *>, T>
    : BaseActivity<VM>() {

    private var adapter: P? = null

    private val rvList by lazy { setLoadList() }

    private val refresh by lazy { setRefresh() }

    abstract fun emptyLoad()

    /**
     * 设置适配器
     */
    abstract fun setAdapterData(it: MutableList<T>): P

    /**
     * 绑定list
     */
    abstract fun setLoadList(): RecyclerView

    /**
     * 设置刷新无传空
     */
    abstract fun setRefresh(): ClickSwipeRefreshLayout?

    /**
     * 子项点击事件
     */
    abstract fun onChildClick(id:Int,item:T,position:Int)

    /**
     * 列表点击事件
     */
    abstract fun onItemClick(item:T,position:Int)

    /**
     * 注意如果重写此方法记得加上super.initData()
     */
    override fun initData() {
        viewModel.list.observe(this, Observer {
            it?.let {
                L.i("size",it.size.toString())
                if (viewModel.pagePost.isLoad) {
                    loadMore(it)
                } else {
                    setBody(it)
                }
            }
        })
        viewModel.load.observe(this, Observer {
            refresh?.isRefreshing = false
        })
    }


    override fun initListener() {
        refresh?.setOnRefreshListener {
            viewModel.refresh()
        }
        super.initListener()
    }


    private fun setBody(it: MutableList<T>) {
        if (it.isEmpty()) {
            adapter?.setNewData(null)
            emptyLoad()
        } else {
            if (adapter == null) {
                adapter = setAdapterData(it)
                adapter?.let {
                    it.setEnableLoadMore(true)
                    rvList.adapter = adapter
                    it.setOnLoadMoreListener({
                        viewModel.loadMore()
                    }, rvList)
                    rvList.clickChildItemDelay<T>{
                        apt,id,position->
                        onChildClick(id,apt.data[position],position)
                    }
                    rvList.clickItemDelay<T> { apt, position ->
                        onItemClick(apt.data[position],position)
                    }
                }
            } else {
                adapter?.setNewData(it)
            }
            setCanLoad()
        }
    }

    private fun loadMore(it: MutableList<T>) {
        if (it.isNotEmpty()) {
            adapter?.addData(it)
            setCanLoad()
        }
    }

    private fun setCanLoad() {
        if (viewModel.pagePost.isCanLoad) {
            adapter?.loadMoreComplete()
        } else {
            adapter?.loadMoreEnd()
        }
    }

}