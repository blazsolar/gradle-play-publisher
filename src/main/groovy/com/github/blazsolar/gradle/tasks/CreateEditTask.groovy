package com.github.blazsolar.gradle.tasks
import com.github.blazsolar.gradle.helper.AndroidPublisherHelper
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.AppEdit
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction
/**
 * Created by blazsolar on 10/08/14.
 */
class CreateEditTask extends PlayBaseTask {

    private static final Logger log = Logging.getLogger(CreateEditTask.class);

    String applicationName;

    String serviceAccountEmail;

    String keyP12;

    String clientSecretJson;

    @TaskAction
    public void createEdit() {

        String packageName = this.packageName;

        // Create the API service.
        AndroidPublisher service = AndroidPublisherHelper.init(applicationName,
                serviceAccountEmail, keyP12, clientSecretJson);
        AndroidPublisher.Edits edits = service.edits();

        // Create a new edit to make changes.
        AndroidPublisher.Edits.Insert editRequest = edits
                .insert(packageName,
                null /** no content */);
        AppEdit edit = editRequest.execute();
        String editId = edit.getId();
        log.info(String.format("Created edit with id: %s", editId));

        getProject().getTasks().withType(PlayEditTask, { PlayEditTask task ->
            task.editId = editId;
            task.edits = edits;
            task.packageName = packageName;
        });

    }
}
