package solar.blaz.gradle.play

import com.google.api.services.androidpublisher.AndroidPublisher
import org.gradle.api.Plugin
import org.gradle.api.Project
import solar.blaz.gradle.play.extension.PlayPluginExtension
import solar.blaz.gradle.play.extension.PublishArtifact
import solar.blaz.gradle.play.tasks.CloseEditTask
import solar.blaz.gradle.play.tasks.CreateEditTask
import solar.blaz.gradle.play.tasks.TrackTask

class BasePlayPlugin : Plugin<Project> {

    // TODO not var
    val extensions: PlayPluginExtension
        get() = project.extensions.getByType(PlayPluginExtension::class.java)

    private val artifactData = mutableMapOf<String, ArtifactData>()
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

        project.extensions.create("playPublisher", PlayPluginExtension::class.java, project)
    }

    fun getVariantTasks(publishArtifact: PublishArtifact): ArtifactData {

        val name = publishArtifact.name
        val mapTasks = artifactData[name]
        if (mapTasks != null) {
            return mapTasks
        } else {
            val appId = publishArtifact.appId!!
            val artifactName = name.capitalize()

            val uploadTask = project.tasks.create(UPLOAD_TASK_NAME_PREFIX + artifactName) {
                it.description = "Upload artifact configuration"
                it.group = PLAY_GROUP
            }
            val createEditTask = configureCreateEdit(project, publishArtifact, artifactName, appId)
            val closeEditTask = configureCloseEdit(project, artifactName, appId, name)

            closeEditTask.dependsOn(createEditTask)
            uploadTask.dependsOn(closeEditTask)

            val trackTask = project.tasks.create(TRACK_TASK_NAME_PREFIX + artifactName,
                    TrackTask::class.java, appId, name, publishArtifact.action, publishArtifact.track).apply {
                userFraction = publishArtifact.userFraction
                listingDir = publishArtifact.listingDir
            }
            trackTask.dependsOn(createEditTask)
            closeEditTask.dependsOn(trackTask)

            val tasks = ArtifactData(createEditTask, trackTask, closeEditTask)
            artifactData[name] = tasks// TODO thread safe
            return tasks
        }
    }

    fun getArtifactData(artifact: String): ArtifactData {
        return artifactData[artifact]!!
    }

    private fun configureCreateEdit(project: Project, publishArtifact: PublishArtifact,
                                    artifactName: String, appId: String): CreateEditTask {

        // TODO add version info to project name
        return project.tasks.create(CREATE_EDIT_TASK_NAME_PREFIX + artifactName,
                CreateEditTask::class.java, appId, publishArtifact.name, project.rootProject.name)
                .apply {
                    clientSecretJson = publishArtifact.clientSecretJson
                    description = "Start new Google Play edit"
                }

    }

    private fun configureCloseEdit(project: Project, variantName: String, appId: String, artifact: String): CloseEditTask {

        return project.tasks.create(CLOSE_EDIT_TASK_NAME_PREFIX + variantName,
                CloseEditTask::class.java, appId, artifact).apply {
            description = "Commits changes to google play"
        }

    }

    inner class ArtifactData(val createEditTask: CreateEditTask, val trackTask: TrackTask, val closeEditTask: CloseEditTask) {

        var edits: AndroidPublisher.Edits? = null
        var editId: String? = null
        val versionCodes = mutableListOf<Long>()

    }

    companion object {
        val PLAY_GROUP = "Play Publisher"
        val UPLOAD_TASK_NAME_PREFIX = "playUpload"
        val CREATE_EDIT_TASK_NAME_PREFIX = "playCreateEdit"
        val CLOSE_EDIT_TASK_NAME_PREFIX = "playCloseEdit"
        private val TRACK_TASK_NAME_PREFIX = "playTrack"
    }
}
