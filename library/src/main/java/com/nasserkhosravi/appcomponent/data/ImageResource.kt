package com.nasserkhosravi.appcomponent.data

/**
 * Image container for different type resource
 */
import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.util.Log
import android.widget.ImageView
import com.nasserkhosravi.appcomponent.AppContext
import com.nasserkhosravi.appcomponent.Res
import com.nasserkhosravi.appcomponent.ResType
import com.nasserkhosravi.appcomponent.utils.FileUtils
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.IOException

class ImageResource private constructor() : Parcelable {
    fun tag() = "ImageResource"

    private var url: String? = null
    private var assetPath: String? = null
    private var filePath: String? = null
    private var drawableRes: Int = 0

    @ResType
    private var resourceType: Int = 0

    @ResType
    fun getType(): Int {
        return resourceType
    }

    private constructor(@ResType resType: Int) : this() {
        resourceType = resType
    }

    fun getDrawable(): Drawable? {
        when (resourceType) {
            Res.ASSET -> {
                return getDrawableFromAsset()
            }
            Res.DRAWABLE -> return getContext().getDrawable(drawableRes)
            Res.FILE -> return BitmapDrawable(getContext().resources, filePath)
            Res.WEB -> throw NotImplementedError("")
        }
        return null
    }

    fun loadDrawableOn(onLoad: (Bitmap) -> Unit) {
        Picasso.with(getContext()).load(getURI()).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                Log.d(tag(), "onDrawableLoad failed ")
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                onLoad(bitmap!!)
            }
        })
    }

    private fun getDrawableFromAsset(): Drawable? {
        var drawable: Drawable? = null
        try {
            drawable = FileUtils.drawableFromAsset(getContext(), assetPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return drawable
    }

    fun loadInto(imageView: ImageView) {
        Picasso.with(imageView.context).load(getURI()).into(imageView)
    }

    fun getAsBitmap(): Bitmap {
        when (resourceType) {
            Res.FILE -> return FileUtils.extractBitmap(FileUtils.getFileByName(filePath))
            else -> throw NotImplementedError()
        }
    }

    fun getURI(): String {
        return when (resourceType) {
            Res.ASSET -> assetPath!!
            Res.DRAWABLE -> drawableRes.toString()
            Res.FILE -> filePath!!
            Res.WEB -> url!!
            else -> throw NotImplementedError()
        }
    }

    private fun getContext(): Application {
        return AppContext.get()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeString(assetPath)
        dest.writeString(filePath)
        dest.writeInt(drawableRes)
        dest.writeInt(resourceType)
    }

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        assetPath = parcel.readString()
        filePath = parcel.readString()
        drawableRes = parcel.readInt()
        resourceType = parcel.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageResource> = object : Parcelable.Creator<ImageResource> {
            override fun createFromParcel(`in`: Parcel): ImageResource {
                return ImageResource(`in`)
            }

            override fun newArray(size: Int): Array<ImageResource?> {
                return arrayOfNulls(size)
            }
        }

        fun fromAsset(assetPath: String): ImageResource {
            val image = ImageResource(Res.ASSET)
            image.assetPath = assetPath
            return image
        }

        fun fromWeb(url: String): ImageResource {
            val image = ImageResource(Res.WEB)
            image.url = url
            return image
        }

        fun fromFile(fullFilePath: String): ImageResource {
            val image = ImageResource(Res.FILE)
            image.filePath = fullFilePath
            return image
        }

        fun fromDrawable(@DrawableRes drawableRes: Int): ImageResource {
            val image = ImageResource(Res.DRAWABLE)
            image.drawableRes = drawableRes
            return image
        }

        fun from(uri: String, resourceType: Int): ImageResource {
            val image = ImageResource(resourceType)
            when (resourceType) {
                Res.ASSET -> image.assetPath = uri
                Res.DRAWABLE -> image.drawableRes = Integer.parseInt(uri)
                Res.FILE -> image.filePath = uri
                Res.WEB -> image.url = uri
                else -> throw IllegalArgumentException("Un supported resource type")
            }
            return image
        }
    }

}
