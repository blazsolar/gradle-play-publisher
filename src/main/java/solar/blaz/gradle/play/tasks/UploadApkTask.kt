package solar.blaz.gradle.play.tasks

import com.google.api.client.http.FileContent
import com.google.api.services.androidpublisher.model.Apk
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.InputFile
import solar.blaz.gradle.play.helper.AndroidPublisherHelper
import java.io.File
import javax.inject.Inject

open class UploadApkTask @Inject constructor(applicationId: String, artifactName: String, @get:InputFile private val apkFile: File)
    : PlayEditTask(applicationId, artifactName) {

    override fun perform() {
        val apk = uploadApk()
        getArtifactData().versionCodes.add(apk.versionCode.toLong())
    }

    private fun uploadApk(): Apk {
        // Upload new apk to developer console
        val file = FileContent(AndroidPublisherHelper.mimE_TYPE_APK, apkFile)
        val uploadRequest = requestEdits()
                .apks()
                .upload(applicationId, requestEditId(), file)
        val apk = uploadRequest.execute()
        LOG.info("Version code ${apk.versionCode} has been uploaded")
        return apk

    }

    companion object {
        private val LOG = Logging.getLogger(UploadApkTask::class.java)
    }
}
