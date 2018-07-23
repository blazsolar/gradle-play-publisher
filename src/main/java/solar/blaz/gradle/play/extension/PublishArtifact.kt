package solar.blaz.gradle.play.extension

import solar.blaz.gradle.play.tasks.TrackTask
import java.io.File
import javax.inject.Inject

open class PublishArtifact @Inject constructor(val name: String) {
    public var clientSecretJson: File? = null
    public var appId: String? = null
    public var action: String = TrackTask.ACTION_RELEASE
    public var track: String? = TrackTask.TRACK_PRODUCTION
    public var userFraction: Double? = null
    public var listingDir: File? = null

    override fun toString(): String {
        return "PublishArtifact(name='$name', clientSecretJson=$clientSecretJson, appId=$appId, action='$action', track=$track, userFraction=$userFraction, listingDir=$listingDir)"
    }


}
