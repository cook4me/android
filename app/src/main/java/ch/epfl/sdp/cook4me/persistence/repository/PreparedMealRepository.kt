package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.PreparedMeal


class PreparedMealRepository :
    BaseRepository<PreparedMeal>(collectionPath = "preparedMeal", modelClass = PreparedMeal::class.java)
