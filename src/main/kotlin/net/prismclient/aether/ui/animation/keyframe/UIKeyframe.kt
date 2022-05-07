package net.prismclient.aether.ui.animation.keyframe

import net.prismclient.aether.ui.style.impl.UIAnimationSheet
import net.prismclient.aether.ui.util.UICopy

/**
 * [UIKeyframe] represents a position in a sequence of keyframes from 0 to 100
 *
 * @author sen
 * @since 3/5/2022
 */
class UIKeyframe(val position: Float, val property: UIAnimationSheet): Comparable<UIKeyframe>, UICopy<UIKeyframe> {
    override fun compareTo(other: UIKeyframe): Int {
        return if (position > other.position) 1 else -1
    }

    override fun copy(): UIKeyframe = UIKeyframe(position, property.copy() as UIAnimationSheet)
}