package com.github.blazsolar.gradle.tasks
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.AppEdit
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
/**
 * Created by blazsolar on 09/08/14.
 */
class CloseEditTask extends PlayEditTask {

    private static final Logger log = Logging.getLogger(CloseEditTask.class);

    @Override
    void perform() {

        if (!didFail()) {
            // Commit changes for edit.
            AndroidPublisher.Edits.Commit commitRequest = edits.commit(packageName, editId);
            AppEdit appEdit = commitRequest.execute();
            log.info(String.format("App edit with id %s has been commited.", appEdit.getId()));
        } else {
            log.info(String.format("Edit canceled due to errors."));
        }

    }

    private boolean didFail() {

        def tasks = project.getTasks().withType(PlayEditTask);

        for (Task task : tasks) {
            if(task.getState().failure != null) {
                return true;
            }
        }

        return false;
    }
}
