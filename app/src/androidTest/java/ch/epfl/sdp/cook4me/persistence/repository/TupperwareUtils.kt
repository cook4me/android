package ch.epfl.sdp.cook4me.persistence.repository

import android.net.Uri
import java.io.File

suspend fun TupperwareRepository.addMultipleTestTupperware(files: List<File>) =
    files.mapIndexed { i, file ->
        add(
            "title$i",
            "desc$i",
            Uri.fromFile(file)
        )
    }
