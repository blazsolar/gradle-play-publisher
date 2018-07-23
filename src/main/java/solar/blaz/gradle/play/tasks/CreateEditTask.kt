package solar.blaz.gradle.play.tasks

import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import solar.blaz.gradle.play.helper.AndroidPublisherHelper
import java.io.File
import javax.inject.Inject

open class CreateEditTask @Inject constructor(applicationId: String, artifactName: String, private val applicationName: String)
    : PlayEditTask(applicationId, artifactName) {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    var clientSecretJson: File? = null

    override fun perform() {
        // Create the API service.
        println(clientSecretJson)
        val edits = AndroidPublisherHelper.init(applicationName, clientSecretJson!!).edits()

        // Create a new edit to make changes.
        val edit = edits.insert(applicationId, null).execute()
        val editId = edit.id

        setEdits(edits)
        setEditId(editId)

        LOG.info("Created edit with id: $editId")


    }

    companion object {
        private val LOG = Logging.getLogger(CreateEditTask::class.java)
    }
}
