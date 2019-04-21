package com.nasserkhosravi.appcomponent

import com.nasserkhosravi.appcomponent.view.ProgressBuilder
import com.nasserkhosravi.appcomponent.view.ProgressType

/**
 * config for [AppComponent]
 */
class ViewComponentConfig {
    /**
     * control back button in toolbar
     */
    var isEnableBackButton = false
    var isEnableToolBar = true

    @ProgressType
    var progressType = ProgressBuilder.CIRCLE_PROGRESS
    //if progress type support text view, this string will be use
    var progressTitle: String? = null
}