package ch.epfl.sdp.cook4me.persistence.model

@Suppress("DataClassShouldBeImmutable")
// need mutable data class to use toObjects and toObject function of firestore
data class Recipe(
    var name: String = ""
)
