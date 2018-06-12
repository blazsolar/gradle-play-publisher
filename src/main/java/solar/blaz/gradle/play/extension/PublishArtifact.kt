package solar.blaz.gradle.play.extension

import solar.blaz.gradle.play.tasks.TrackTask
import java.io.File
import javax.inject.Inject

open class PublishArtifact @Inject constructor(val name: String) {
    var clientSecretJson: File? = null
    var appId: String? = null
    var action: String = TrackTask.ACTION_RELEASE
    var track: String? = TrackTask.TRACK_PRODUCTION
    var userFraction: Double? = null
    var listingDir: File? = null
}
