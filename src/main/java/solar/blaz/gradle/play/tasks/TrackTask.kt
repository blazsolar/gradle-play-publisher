package solar.blaz.gradle.play.tasks

import com.google.api.services.androidpublisher.model.LocalizedText
import com.google.api.services.androidpublisher.model.Track
import com.google.api.services.androidpublisher.model.TrackRelease
import org.gradle.api.logging.Logging
import java.io.File
import javax.inject.Inject

open class TrackTask @Inject constructor(applicationId: String, artifact: String, private val action: String, private val track: String)
    : PlayEditTask(applicationId, artifact) {

    var userFraction: Double? = null
    var listingDir: File? = null

    override fun perform() {
        assignApkTrack()
    }

    private fun assignApkTrack() {

        var name = "Play Publisher release: " + getArtifactData().versionCodes.joinToString()
        if (name.length > 50) {
            name = name.substring(0, 50)
        }

        val release = TrackRelease()
                .setName(name)
                .setStatus(action)
                .setVersionCodes(getArtifactData().versionCodes)
                .setUserFraction(userFraction)
                .setReleaseNotes(getReleaseNotes())

        val t = Track()
                .setTrack(track)
                .setReleases(listOf(release))

        // Assign apk to beta track.
        val updateTrackRequest = requestEdits()
                .tracks()
                .update(applicationId, requestEditId(), track, t)

        val updatedTrack = updateTrackRequest.execute()
        LOG.info(String.format("Track %s has been updated.", updatedTrack.track))
    }

    private fun getReleaseNotes(): List<LocalizedText> {
        val dir = listingDir
        val all = mutableListOf<LocalizedText>()

        if (dir != null && dir.exists()) {
            dir.listFiles()
                    .filter { it.isDirectory }
                    .forEach {
                        it.listFiles().firstOrNull { it.isFile && it.name == "releaseNotes" }?.let { rn ->
                            all.add(LocalizedText()
                                    .setLanguage(it.name)
                                    .setText(rn.readText()))
                        }
                    }
        }

        return all
    }

    companion object {
        private val LOG = Logging.getLogger(TrackTask::class.java)

        public val TRACK_ALPHA = "alpha"
        public val TRACK_BETA = "beta"
        public val TRACK_INTERNAL = "internal"
        public val TRACK_PRODUCTION = "production"

        public val ACTION_RELEASE = "completed"
        public val ACTION_DRAFT = "draft"
        public val ACTION_ROLOUT = "inProgress"
    }

}
