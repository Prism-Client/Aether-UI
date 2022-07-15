package net.prismclient.aether.ui.util.extensions

fun FloatArray.minX(): Float = this[0]
fun FloatArray.minY(): Float = this[1]
fun FloatArray.maxX(): Float = this[2]
fun FloatArray.maxY(): Float = this[3]

fun FloatArray.x(): Float = this[0]
fun FloatArray.y(): Float = this[1]
fun FloatArray.width(): Float = this[2]
fun FloatArray.height(): Float = this[3]

fun FloatArray.red(): Float = this[0]
fun FloatArray.green(): Float = this[1]
fun FloatArray.blue(): Float = this[2]
fun FloatArray.alpha(): Float = this[3]

fun FloatArray.advance(): Float = this[4]