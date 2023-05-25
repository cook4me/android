package ch.epfl.sdp.cook4me.ui.chat

import android.content.Context
import coil.ImageLoader
import com.getstream.sdk.chat.coil.StreamImageLoaderFactory
import io.getstream.chat.android.compose.ui.util.StreamCoilImageLoaderFactory

//  adapted getstream's DefaultStreamCoilImageLoaderFactory
internal object CoilImageLoaderFactory : StreamCoilImageLoaderFactory {

    /**
     * Loads images in the app, with our default settings.
     */
    private var imageLoader: ImageLoader? = null

    /**
     * Returns either the currently available [ImageLoader] or builds a new one.
     *
     * @param context - The [Context] to build the [ImageLoader] with.
     * @return [ImageLoader] that loads images in the app.
     */
    override fun imageLoader(context: Context): ImageLoader = imageLoader ?: newImageLoader(context)

    /**
     * Builds a new [ImageLoader] using the given Android [Context]. If the loader already exists, we return it.
     *
     * @param context - The [Context] to build the [ImageLoader] with.
     * @return [ImageLoader] that loads images in the app.
     */
    @Synchronized
    private fun newImageLoader(context: Context): ImageLoader {
        imageLoader?.let { return it }
        return ImageLoaderDecorator(StreamImageLoaderFactory(context).newImageLoader())
    }
}
