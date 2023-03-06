package ch.epfl.sdp.cook4me.persistence.repository

import ch.epfl.sdp.cook4me.persistence.model.Recipe

class RecipeRepository :
    BaseRepository<Recipe>(collectionPath = "recipes", modelClass = Recipe::class.java)
