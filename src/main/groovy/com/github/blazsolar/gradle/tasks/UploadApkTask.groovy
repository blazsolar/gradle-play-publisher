package com.github.blazsolar.gradle.tasks
import com.github.blazsolar.gradle.helper.AndroidPublisherHelper
import com.google.api.client.http.AbstractInputStreamContent
import com.google.api.client.http.FileContent
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.Apk
import com.google.api.services.androidpublisher.model.ApkListing
import com.google.api.services.androidpublisher.model.Track
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
/**
 * Created by blazsolar on 10/08/14.
 */
public class UploadApkTask extends PlayEditTask {

    private static final Logger log = Logging.getLogger(UploadApkTask.class)

    public static final String TRACK_ALPHA = "alpha"
    public static final String TRACK_BETA = "beta"
    public static final String TRACK_ROLLOUT = "rollout"
    public static final String TRACK_PRODUCTION = "production"

    String apkPath = "app/build/outputs/apk/app-release.apk"

    String track = TRACK_BETA;

    double userFraction = 0.05;

    Map<String, String> listings;

    @Override
    void perform() {

        Apk apk = uploadApk()
        assignApkTrack(apk)
        uploadListings(apk)

    }

    private Apk uploadApk() {

        // Upload new apk to developer console
        final String apkPath = apkPath;
        final AbstractInputStreamContent apkFile =
                new FileContent(AndroidPublisherHelper.MIME_TYPE_APK, new File(apkPath));
        AndroidPublisher.Edits.Apks.Upload uploadRequest = edits
                .apks()
                .upload(
                    packageName,
                    editId,
                    apkFile
                );
        Apk apk = uploadRequest.execute();
        log.info(String.format("Version code %d has been uploaded",
                apk.getVersionCode()));
        return apk;

    }

    private void assignApkTrack(Apk apk) {

        List<Integer> apkVersionCodes = new ArrayList<>();
        apkVersionCodes.add(apk.getVersionCode());

        Track t = new Track();
        t.setVersionCodes(apkVersionCodes);

        if (TRACK_ROLLOUT.equals(track)) {
            t.setUserFraction(userFraction)
        }

        // Assign apk to beta track.
        AndroidPublisher.Edits.Tracks.Update updateTrackRequest = edits
                .tracks()
                .update(
                    packageName,
                    editId,
                    track,
                    t
                );
        Track updatedTrack = updateTrackRequest.execute();
        log.info(String.format("Track %s has been updated.", updatedTrack.getTrack()));
    }

    private void uploadListings(Apk apk) {

        if (listings == null || listings.size() == 0) {
            log.info("Not APK listings available")
            return
        }

        listings.each { String country, String listing ->

            // Update recent changes field in apk listing.
            final ApkListing newApkListing = new ApkListing();
            newApkListing.setRecentChanges(listing);

            AndroidPublisher.Edits.Apklistings.Update updateRecentChangesRequest = edits
                    .apklistings()
                    .update(packageName,
                        editId,
                        apk.getVersionCode(),
                        country,
                        newApkListing
                    );
            updateRecentChangesRequest.execute();
            log.info("Recent changes for %s updated.", country);

        }

        log.info("Recent changes has been updated.");

    }

}
