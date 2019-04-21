package com.nasserkhosravi.appcomponent.example

import android.os.Bundle
import com.nasserkhosravi.appcomponent.view.BaseComponentActivity

class MainActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //first: config view
        getConfig().isEnableBackButton = true
        getConfig().isEnableToolBar = true//default is true
        //second: set your root layout,for showing snack bar use CoordinateLayout
        getViewGroupManager().viewGroup = findViewById(R.id.rootLayout)
        //third: build components
        getViewComponent().constructLayout()

        //now: use components
        getViewComponent().setTitleToolBar("hello")
    }
}
