package solar.blaz.gradle.play.tasks

import org.gradle.api.logging.Logging
import javax.inject.Inject

open class CloseEditTask @Inject constructor(applicationId: String, artifactName: String) : PlayEditTask(applicationId, artifactName) {

    override fun perform() {
        // Commit changes for edit.
        val appEdit = requestEdits()
                .commit(applicationId, requestEditId())
                .execute()
        LOG.info("App edit with id ${appEdit.id} has been committed.")
    }

    companion object {
        private val LOG = Logging.getLogger(CloseEditTask::class.java)
    }
}
