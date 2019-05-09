package solar.blaz.gradle.play

import com.google.api.services.androidpublisher.AndroidPublisher
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import solar.blaz.gradle.play.extension.PlayPluginExtension
import solar.blaz.gradle.play.extension.PublishArtifact
import solar.blaz.gradle.play.tasks.CloseEditTask
import solar.blaz.gradle.play.tasks.CreateEditTask
import solar.blaz.gradle.play.tasks.TrackTask

class BasePlayPlugin : Plugin<Project> {

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

            val createEditTask = configureCreateEdit(project, publishArtifact, artifactName, appId)
            val closeEditTask = configureCloseEdit(project, artifactName, appId, name)

            closeEditTask.configure {
                it.dependsOn(createEditTask)
            }
            project.tasks.register(UPLOAD_TASK_NAME_PREFIX + artifactName) {
                it.description = "Upload artifact configuration"
                it.group = PLAY_GROUP
                it.dependsOn(closeEditTask)
            }

            val trackTask = project.tasks.register(TRACK_TASK_NAME_PREFIX + artifactName,
                    TrackTask::class.java, appId, name, publishArtifact.action, publishArtifact.track).apply {
                configure {
                    it.userFraction = publishArtifact.userFraction
                    it.listingDir = publishArtifact.listingDir
                    it.dependsOn(createEditTask)
                }
            }

            closeEditTask.configure {
                it.dependsOn(trackTask)
            }

            val tasks = ArtifactData(createEditTask, trackTask, closeEditTask)
            artifactData[name] = tasks // TODO thread safe
            return tasks
        }
    }

    fun getArtifactData(artifact: String): ArtifactData {
        return artifactData[artifact]!!
    }

    private fun configureCreateEdit(project: Project, publishArtifact: PublishArtifact,
                                    artifactName: String, appId: String): TaskProvider<CreateEditTask> {


        // TODO add version info to project name
        return project.tasks.register(CREATE_EDIT_TASK_NAME_PREFIX + artifactName,
                CreateEditTask::class.java, appId, publishArtifact.name, project.rootProject.name)
                .apply {
                    configure {
                        it.clientSecretJson = publishArtifact.clientSecretJson
                        it.description = "Start new Google Play edit"
                    }
                }

    }

    private fun configureCloseEdit(project: Project, variantName: String, appId: String, artifact: String): TaskProvider<CloseEditTask> {

        return project.tasks.register(CLOSE_EDIT_TASK_NAME_PREFIX + variantName,
                CloseEditTask::class.java, appId, artifact).apply {
            configure {
                it.description = "Commits changes to google play"
            }
        }

    }

    inner class ArtifactData(val createEditTask: TaskProvider<CreateEditTask>, val trackTask: TaskProvider<TrackTask>,
                             val closeEditTask: TaskProvider<CloseEditTask>) {
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
