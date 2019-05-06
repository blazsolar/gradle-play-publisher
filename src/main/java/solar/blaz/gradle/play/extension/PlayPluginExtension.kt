package solar.blaz.gradle.play.extension

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

open class PlayPluginExtension(project: Project) {

    val artifacts: NamedDomainObjectContainer<PublishArtifact> = project.container(PublishArtifact::class.java)

    fun artifacts(action: Action<in NamedDomainObjectContainer<PublishArtifact>>) {
        action.execute(artifacts)
    }

}
