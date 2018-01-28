package jp.juggler.apng.util

import jp.juggler.apng.ParseError

internal fun ByteArray.getUInt8(pos : Int) = this[pos].toInt() and 255

internal fun ByteArray.getUInt16(pos : Int) = (this.getUInt8(pos) shl 8) or this.getUInt8(pos + 1)

internal fun ByteArray.getInt32(pos : Int) = (this.getUInt8(pos) shl 24) or
	(this.getUInt8(pos + 1) shl 16) or
	(this.getUInt8(pos + 2) shl 8) or
	this.getUInt8(pos + 3)

internal class ByteSequence(
	val array : ByteArray,
	var offset : Int,
	var length : Int
) {
	
	constructor(ba : ByteArray) : this(ba, 0, ba.size)
	
	private inline fun <T> readX(dataSize : Int, block : () -> T) : T {
		if(length < dataSize) throw ParseError("readIntX: unexpected end")
		val v = block()
		offset += dataSize
		length -= dataSize
		return v
	}
	
	fun readUInt8() = readX(1) { array.getUInt8(offset) }
	fun readUInt16() = readX(2) { array.getUInt16(offset) }
	fun readInt32() = readX(4) { array.getInt32(offset) }
}
