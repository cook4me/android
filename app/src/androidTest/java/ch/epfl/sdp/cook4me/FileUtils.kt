package ch.epfl.sdp.cook4me

import java.io.File

fun generateTempFiles(count: Int): List<File> =
    (0 until count).map {
        val file = File.createTempFile("temp_", "$it")
        file.writeText("temp$it")
        file.deleteOnExit()
        file
    }
