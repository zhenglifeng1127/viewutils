package com.zero.viewutils.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * 时间工具类
 */
class DateTimeUtils {
    enum class DATETIME {
        DAY,
        HOUR,
        MINUTE,
        SS
    }


    companion object {

        private val weekDays by lazy {
            arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        }

        /**
         * 获取某年某月有多少天
         * @param year 年份
         * @param month 月份
         */
        fun getDayOfMonth(year: Int, month: Int): Int {
            val c = Calendar.getInstance()
            c.set(year, month, 0) //输入类型为int类型
            return c.get(Calendar.DAY_OF_MONTH)
        }

        /**
         * 判断当前是周几
         *
         * @param dt 日期（起码包含年月日）
         *
         */
        fun getWeekOfDate(dt: Date): String {
            val cal = Calendar.getInstance()
            cal.time = dt
            var w = cal.get(Calendar.DAY_OF_WEEK) - 1
            if (w < 0)
                w = 0
            return weekDays[w]
        }

        /**
         *获取当前月第几天
         */
        fun day(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.DAY_OF_MONTH)
        }

        /**
         * 获取现在是当天的第几小时
         */
        fun hour(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.HOUR_OF_DAY)
        }

        /**
         * 获取当前秒数
         */
        fun second(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.SECOND)
        }

        /**
         * 获取当前分针数
         */
        fun minute(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.MINUTE)
        }

        /**
         * 年份
         */
        fun year(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.YEAR)
        }

        /**
         * 月份
         */
        fun month(): Int {
            val c = Calendar.getInstance()
            return c.get(Calendar.MONTH) + 1
        }

        /**
         *计算两个日期的差值
         */
        fun between(cal1: Calendar, cal2: Calendar, typeCode:  DATETIME): Long {
            var res = abs(cal1.timeInMillis - cal2.timeInMillis)
            when (typeCode) {
                DATETIME.DAY -> res /= (24 * 60 * 60 * 1000)
                DATETIME.MINUTE -> res /= (60 * 1000)
                DATETIME.SS -> res /= 1000
                DATETIME.HOUR -> res /= (60 * 60 * 1000)
            }
            return res
        }

        /**
         * 日期与当前时间的绝对值差
         */
        fun betweenNow(date: String, style: String, typeCode:  DATETIME): Long {
            var res = abs(
                toLong(
                    date,
                    style
                ) - Calendar.getInstance().timeInMillis
            )
            when (typeCode) {
                DATETIME.DAY -> res /= (24 * 60 * 60 * 1000)
                DATETIME.MINUTE -> res /= (60 * 1000)
                DATETIME.SS -> res /= 1000
                DATETIME.HOUR -> res /= (60 * 60 * 1000)
                else -> {
                }
            }
            return res
        }


        /**
         *计算时间戳的差值
         */
        fun between(cal1: Long, cal2: Long, typeCode:  DATETIME): Long {
            var res = abs(cal1 - cal2)
            when (typeCode) {
                DATETIME.DAY -> res /= (24 * 60 * 60 * 1000)
                DATETIME.MINUTE -> res /= (60 * 1000)
                DATETIME.SS -> res /= 1000
                DATETIME.HOUR -> res /= (60 * 60 * 1000)
            }
            return res
        }


        fun time(): Long {
            return Calendar.getInstance().timeInMillis
        }


        /**
         * 日期转时间戳
         */
        fun toLong(strDate: String, timeStyle: String): Long {
            val sdf = SimpleDateFormat(timeStyle, Locale.CHINA)
            val cal = Calendar.getInstance()
            try {
                val date = sdf.parse(strDate)
                if (date != null)
                    cal.time = date
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return cal.timeInMillis
        }

        /**
         * 格式转化
         */
        fun dateToDate(strDate: String, timeStyle: String, timeStyle_now: String): String {

            val sdf = SimpleDateFormat(timeStyle, Locale.CHINA)
            val sdf1 = SimpleDateFormat(timeStyle_now, Locale.CHINA)
            try {
                val date = sdf.parse(strDate)
                return sdf1.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return strDate
        }


        /**
         * 获取相隔日期
         */
        fun getDay(start: Calendar, day: Int, code: Int, formatStyle: String): String {
            var time1 = (day * 24 * 3600 * 1000).toLong()
            when (code) {
                0 -> time1 += start.timeInMillis
                else -> time1 = start.timeInMillis - time1
            }
            val cal = GregorianCalendar()
            cal.timeInMillis = time1
            return SimpleDateFormat(formatStyle, Locale.CHINA).format(cal.time)
        }

        //输出毫秒时间戳
        fun getTimeText(): String {
            return System.currentTimeMillis().toString() + ""
        }
    }
}