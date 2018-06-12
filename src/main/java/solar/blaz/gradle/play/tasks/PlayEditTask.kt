package solar.blaz.gradle.play.tasks

import com.google.api.services.androidpublisher.AndroidPublisher
import org.gradle.api.tasks.TaskAction
import solar.blaz.gradle.play.BasePlayPlugin

abstract class PlayEditTask(applicationId: String, private val artifactName: String) : PlayBaseTask(applicationId) {

    @TaskAction abstract fun perform()

    protected fun requestEdits(): AndroidPublisher.Edits {
        return getEdits()!!
    }

    protected fun requestEditId(): String? {
        return getEditId()!!
    }

    protected fun setEdits(edits: AndroidPublisher.Edits) {
        getArtifactData().edits = edits
    }

    protected fun setEditId(editId: String) {
        getArtifactData().editId = editId
    }

    protected fun getArtifactData(): BasePlayPlugin.ArtifactData {
        return getBasePlugin().getArtifactData(artifactName)
    }

    private fun getEdits(): AndroidPublisher.Edits? {
        return getArtifactData().edits
    }

    private fun getEditId(): String? {
        return getArtifactData().editId
    }

    private fun getBasePlugin(): BasePlayPlugin {
        return if (project.plugins.hasPlugin(BasePlayPlugin::class.java)) {
            project.plugins.getPlugin(BasePlayPlugin::class.java)
        } else {
            project.parent!!.plugins.getPlugin(BasePlayPlugin::class.java)
        }
    }

}
