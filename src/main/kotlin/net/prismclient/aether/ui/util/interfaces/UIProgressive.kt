package net.prismclient.aether.ui.util.interfaces

interface UIProgressive {
    var progress: Float
}

//fun Float.progressive(): UIProgressive {
//    return object: UIProgressive {
//        override var progress: Float
//            get() = this@progressive
//            set(_) { }
//    }
//}