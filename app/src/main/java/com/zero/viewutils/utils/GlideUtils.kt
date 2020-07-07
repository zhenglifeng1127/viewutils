package com.zero.viewutils.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.zero.viewutils.R
import com.zero.viewutils.utils.other.CornersTransformation


@SuppressLint("CheckResult")
class GlideUtils {
    companion object {

        fun cornerTop(con: Context, url: String, view: ImageView, size: Int) {
            Glide.with(con).load(url).apply(
                getCornersReq(ReqType.CORNER_TOP, size).override(
                    view.measuredWidth,
                    view.measuredHeight
                )
            ).into(view)

        }

        fun localImage(con: Context, id: Int, view: ImageView) {
            Glide.with(con).load(id).into(view)
        }

        //设置长宽
        fun centerFit(context: Context, url: String, view: ImageView,defaultId:Int = R.drawable.bg_defalut_fit) {
            Glide.with(context).load(url)
                .apply(
                    getReq(
                        ReqType.CENTER_FIT,
                        defaultId
                    ).override(view.measuredWidth, view.measuredHeight)
                )
                .into(view)
        }

        fun banner(context: Context, url: String, view: ImageView,defaultId:Int = R.drawable.bg_defalut_fit) {
            Glide.with(context).load(url)
                .apply(
                    getReq(
                        ReqType.BANNER,
                        defaultId
                    ).override(view.measuredWidth, view.measuredHeight)
                )
                .into(view)
        }


        fun corner(context: Context, url: String, view: ImageView, size: Int) {
            Glide.with(context).load(url)
                .apply(
                    getCornersReq(ReqType.CORNER_ALL, size).override(
                        view.measuredWidth,
                        view.measuredHeight
                    )
                )
                .into(view)
        }


        private fun getReq(type: ReqType, defaultImg: Int): RequestOptions {
            val req = RequestOptions()
            when (type) {
                ReqType.CENTER_FIT -> {
                    req.fitCenter().placeholder(defaultImg)
                        .error(defaultImg)
                }
                else -> {

                }
            }
            return req.skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        }


        private fun getCornersReq(type: ReqType, size: Int): RequestOptions {
            var req = RequestOptions()
            when (type) {
                ReqType.CORNER_TOP -> {
                    val leftTop =
                        CornersTransformation(
                            size,
                            0,
                            CornersTransformation.CornerType.TOP_LEFT
                        )
                    //顶部右边圆角
                    val rightTop =
                        CornersTransformation(
                            size,
                            0,
                            CornersTransformation.CornerType.TOP_RIGHT
                        )
                    req.transform(MultiTransformation(leftTop, rightTop))
                }
                ReqType.CORNER_ALL -> {
                    req = RequestOptions.bitmapTransform(
                        CornersTransformation(
                            size,
                            0,
                            CornersTransformation.CornerType.ALL
                        )
                    )
                }
                else -> {

                }
            }
            return req.skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }

    enum class ReqType {
        CORNER_TOP,
        CORNER_ALL,
        BANNER,
        CENTER_FIT
    }
}