package net.prismclient.aether.ui.animation.keyframe

import net.prismclient.aether.ui.style.impl.UIAnimationSheet
import net.prismclient.aether.ui.util.UICopy

/**
 * [UIKeyframe] represents a position in a sequence of keyframes from 0 to 100
 *
 * @author sen
 * @since 3/5/2022
 */
class UIKeyframe(val property: UIAnimationSheet): UICopy<UIKeyframe> {
    override fun copy(): UIKeyframe = UIKeyframe(property.copy() as UIAnimationSheet)
}