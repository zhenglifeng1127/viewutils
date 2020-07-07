package com.zero.viewutils.utils

class ConvertUtils {

    companion object{
        /**
         * byteArray转hex
         */
        fun byte2hex(byteArray: ByteArray): String {
            val strBuff = StringBuilder()
            for (b in byteArray) {
                if (Integer.toHexString(0xFF and b.toInt()).length == 1)
                    strBuff.append("0").append(Integer.toHexString(0xFF and b.toInt()))
                else
                    strBuff.append(Integer.toHexString(0xFF and b.toInt()))
            }
            return strBuff.toString()
        }

        /**
         * hex转byteArray
         */
        fun hex2Byte(hex: String): ByteArray {
            val len = hex.length / 2
            val result = ByteArray(len)
            val achar = hex.toCharArray()
            for (i in 0 until len) {
                val pos = i * 2
                result[i] = (toByte(achar[pos]) shl 4 or toByte(achar[pos + 1])).toByte()
            }
            return result
        }


        private fun toByte(c: Char): Int {
            val b = "0123456789ABCDEF".indexOf(c).toByte()
            return b.toInt()
        }

    }
}