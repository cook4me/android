package ch.epfl.sdp.cook4me.ui.chat

import ch.epfl.sdp.cook4me.persistence.repository.ProfileImageRepository
import coil.ComponentRegistry
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult

const val PROFILE_IMAGE_PREFIX = "load profile image"

internal class ImageLoaderDecorator(
    private val loader: ImageLoader,
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepository()
) : ImageLoader {
    override val components: ComponentRegistry
        get() = loader.components
    override val defaults: DefaultRequestOptions
        get() = loader.defaults
    override val diskCache: DiskCache?
        get() = loader.diskCache
    override val memoryCache: MemoryCache?
        get() = loader.memoryCache

    override fun enqueue(request: ImageRequest): Disposable = loader.enqueue(request)

    // Unfortunately, there doesn't exist a way to store an image for each chat user
    // This is a workaround to load images related to the chat either from the profile data or from getstream
    override suspend fun execute(request: ImageRequest): ImageResult =
        if (request.data is String && (request.data as String).startsWith(
                PROFILE_IMAGE_PREFIX
            )
        ) {
            val image = profileImageRepository.getProfile(
                (request.data as String).removePrefix(
                    PROFILE_IMAGE_PREFIX
                )
            )
            loader.execute(
                ImageRequest.Builder(request.context)
                    .data(image)
                    .build()
            )
        } else {
            loader.execute(request)
        }

    override fun newBuilder(): ImageLoader.Builder = loader.newBuilder()

    override fun shutdown() = loader.shutdown()
}
