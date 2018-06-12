package solar.blaz.gradle.play.tasks

import com.google.api.services.androidpublisher.model.Apk
import org.gradle.api.logging.Logging
import javax.inject.Inject

open class ListingTask @Inject constructor(applicationId: String, artifactName: String) : PlayEditTask(applicationId, artifactName) {
    var listings: Map<String, String>? = null
    override fun perform() {
        // TODO get apk
        //        uploadListings()
    }

    private fun uploadListings(apk: Apk) {

        //        if (listings == null || listings.size() == 0) {
        //            LOG.info("Not APK listings available")
        //            return
        //        }
        //
        //        listings.each { String country, String listing ->
        //
        //            // Update recent changes field in apk listing.
        //            final ApkListing newApkListing = new ApkListing();
        //            newApkListing.setRecentChanges(listing);
        //
        //            AndroidPublisher.Edits.Apklistings.Update updateRecentChangesRequest = edits
        //                    .apklistings()
        //                    .update(packageName, editId, apk.getVersionCode(), country, newApkListing)
        //            updateRecentChangesRequest.execute();
        //            LOG.info("Recent changes for %s updated.", country);
        //
        //        }
        //
        //        LOG.info("Recent changes has been updated.");

    }

    companion object {
        private val LOG = Logging.getLogger(UploadApkTask::class.java)
    }
}
