package solar.blaz.gradle.play.helper

import com.android.annotations.NonNull
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.repackaged.com.google.common.base.Preconditions
import com.google.api.client.repackaged.com.google.common.base.Strings
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.AndroidPublisher.Builder
import com.google.api.services.androidpublisher.AndroidPublisherScopes
import org.apache.commons.logging.LogFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.GeneralSecurityException

/**
 * Helper class to initialize the publisher APIs client library.
 *
 *
 * Before making any calls to the API through the client library you need to
 * call the [AndroidPublisherHelper.init] method. This will run
 * all precondition checks for for client id and secret setup properly in
 * resources/client_secrets.json and authorize this client against the API.
 *
 */
object AndroidPublisherHelper {

    private val log = LogFactory.getLog(AndroidPublisherHelper::class.java)
    val mimE_TYPE_APK = "application/vnd.android.package-archive"
    /** Global instance of the JSON factory.  */
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    /** Global instance of the HTTP transport.  */
    private var HTTP_TRANSPORT: HttpTransport? = null

    /**
     * Performs all necessary setup steps for running requests against the API.
     *
     * @param applicationName the name of the application: com.example.app
     * @param the Service Account Email (empty if using
     * installed application)
     * @return the {@Link AndroidPublisher} service
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Throws(IOException::class, GeneralSecurityException::class)
    fun init(applicationName: String,
             @NonNull jsonFile: File): AndroidPublisher {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(applicationName),
                "applicationName cannot be null or empty!")

        // Authorization.
        newTrustedTransport()
        val credential = GoogleCredential.fromStream(FileInputStream(jsonFile), HTTP_TRANSPORT!!,
                JSON_FACTORY).createScoped(setOf(AndroidPublisherScopes.ANDROIDPUBLISHER))

        // Set up and return API client.
        return Builder(HTTP_TRANSPORT!!, JSON_FACTORY, credential).setApplicationName(applicationName).build()
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    private fun newTrustedTransport() {
        if (null == HTTP_TRANSPORT) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        }

    }
}
