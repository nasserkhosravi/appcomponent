package com.nasserkhosravi.appcomponent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.nasserkhosravi.appcomponent.R
import com.nasserkhosravi.appcomponent.ResourceComponent
import com.nasserkhosravi.appcomponent.utils.JReflectionUtils

abstract class BaseComponentAdapter<M> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ResourceComponent {

    private var context: Context? = null
    override var ctx: Context
        get() = context!!
        set(value) {}

    var items: List<M>? = null
    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        checkCacheContext(parent)
        return getViewHolder(inflateLayout(layoutRes, parent), viewType)
    }

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
    abstract val layoutRes: Int

    fun checkCacheContext(viewGroup: ViewGroup) {
        if (this.context == null) {
            this.context = viewGroup.context
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        vh.itemView.id = R.id.rootView
        itemClickListener?.let {
            bindClickListener(vh, position)
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    //todo: why you create LayoutInflater every time
    fun inflateLayout(@LayoutRes layoutId: Int, container: ViewGroup): View {
        return LayoutInflater.from(this.context).inflate(layoutId, container, false)
    }

    /**
     * bind click listener of view holder of recycle view
     */
    private fun bindClickListener(vh: RecyclerView.ViewHolder, position: Int) {
        vh.itemView.setOnClickListener {
            itemClickListener?.onRecycleItemClick(it, position)
        }
        JReflectionUtils.findFields<View>(vh, ClickListenerBinder::class.java).forEach {
            it.setOnClickListener {
                itemClickListener?.onRecycleItemClick(it, position)
            }
        }
    }

    interface ItemClickListener {
        fun onRecycleItemClick(view: View, position: Int)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ClickListenerBinder