package com.nasserkhosravi.appcomponent.utils

import com.nasserkhosravi.appcomponent.view.BaseComponentActivity
import com.nasserkhosravi.appcomponent.view.fragment.BaseComponentFragment

/**
 * Created by Nasser on 11/9/2017.
 */
object FragmentUtils {
    fun replace(frameLayoutId: Int, fragment: BaseComponentFragment, activity: BaseComponentActivity) {
        activity.supportFragmentManager.beginTransaction().replace(frameLayoutId, fragment).commit()
    }
}