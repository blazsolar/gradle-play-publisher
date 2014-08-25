package com.github.blazsolar.gradle.tasks

import com.google.api.services.androidpublisher.AndroidPublisher
import org.gradle.api.tasks.TaskAction

/**
 * Created by blazsolar on 12/08/14.
 */
abstract class PlayEditTask extends PlayBaseTask {

    AndroidPublisher.Edits edits

    String editId;

    @TaskAction
    public abstract void perform();

}
