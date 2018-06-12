package solar.blaz.gradle.play.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

abstract class PlayBaseTask(@get:Input val applicationId: String) : DefaultTask()
