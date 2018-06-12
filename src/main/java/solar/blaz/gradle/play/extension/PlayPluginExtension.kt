package solar.blaz.gradle.play.extension

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

open class PlayPluginExtension(project: Project) {

    val artifacts: NamedDomainObjectContainer<PublishArtifact> = project.container(PublishArtifact::class.java)

    fun artifacts(closure: Closure<*>): NamedDomainObjectContainer<PublishArtifact> {
        return artifacts.configure(closure)
    }
}
