package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
data class FirestoreTupperware(
    var title: String = "",
    var description: String = "",
    var user: String = ""
)


data class TupperwareWithImage(
    val title: String,
    val description: String,
    val user: String,
    val image: ByteArray // It's recommended to use overwrite equals/hashCode if a byte array is used
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TupperwareWithImage

        if (title != other.title) return false
        if (description != other.description) return false
        if (user != other.user) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
