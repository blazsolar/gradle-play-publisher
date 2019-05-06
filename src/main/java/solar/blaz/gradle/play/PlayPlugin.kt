package solar.blaz.gradle.play

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import solar.blaz.gradle.play.BasePlayPlugin.ArtifactData
import solar.blaz.gradle.play.extension.PublishArtifact
import solar.blaz.gradle.play.tasks.UploadApkTask

class PlayPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!isAndroidProject(project)) {
            throw IllegalStateException("Play Publish plugin only works with Android application projects but \"" + project.name + "\" is none")
        }

        val parent = project.parent
        val basePlugin: Any
        basePlugin = if (parent != null && parent.plugins.hasPlugin(BasePlayPlugin::class.java)) {
            parent.plugins.getPlugin(BasePlayPlugin::class.java)
        } else {
            project.plugins.apply(BasePlayPlugin::class.java)
        }

        project.afterEvaluate {
            createTasks(it, basePlugin)
        }
    }

    private fun createTasks(project: Project, basePlugin: BasePlayPlugin) {

        val extensions = basePlugin.extensions

        extensions.artifacts.all { artifact ->
            val baseTasks = basePlugin.getVariantTasks(artifact)

            val androidExtension = project.extensions.getByType(AppExtension::class.java)
            androidExtension.applicationVariants.all { variant ->
                if (variant.buildType.name == "release" && artifact.appId == variant.applicationId) {
                    addVariantTasks(project, variant, baseTasks, artifact)
                }
            }
        }

    }

    private fun addVariantTasks(project: Project, variant: ApplicationVariant, baseTasks: ArtifactData,
                                artifact: PublishArtifact) {

        val createEditTask = baseTasks.createEditTask

        val appId = variant.applicationId
        val artifactName = artifact.name.capitalize()

        variant.outputs.all { output ->

            val outputNamePostfix = output.filterTypes.joinToString("") { it.capitalize() }
            val tasksName = UPLOAD_APK_TASK_NAME_PREFIX + artifactName + output.name + outputNamePostfix

            val uploadTask = project.tasks.create(tasksName, UploadApkTask::class.java, appId, artifact.name, output.outputFile)

            uploadTask.dependsOn(variant.assemble)
            uploadTask.dependsOn(createEditTask)
            baseTasks.trackTask.dependsOn(uploadTask)
        }

    }

    companion object {

        fun isAndroidProject(project: Project): Boolean {
            return project.plugins.hasPlugin("com.android.application")
        }

        val PLAY_GROUP = "Play Publisher"
        private const val UPLOAD_APK_TASK_NAME_PREFIX = "playUploadApk"
    }
}
