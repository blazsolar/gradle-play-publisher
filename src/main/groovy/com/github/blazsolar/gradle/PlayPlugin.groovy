package com.github.blazsolar.gradle
import com.github.blazsolar.gradle.extension.PlayPluginExtension
import com.github.blazsolar.gradle.tasks.CloseEditTask
import com.github.blazsolar.gradle.tasks.CreateEditTask
import com.github.blazsolar.gradle.tasks.PlayEditTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
/**
 * Created by blazsolar on 08/08/14.
 */
class PlayPlugin implements Plugin<Project> {

    public static final String PLAY_GROUP = "Play publisher";

    public static final String CREATE_EDIT_TASK_NAME = "playCreateEdit";
    public static final String CLOSE_EDIT_TASK_NAME = "playCloseEdit";

    @Override
    void apply(Project project) {

        project.extensions.add("playPublisher", PlayPluginExtension);
        configureCreateEdit(project)
        configureCloseEdit(project)
        configureDependencies(project)

    }

    private void configureCreateEdit(Project project) {

        CreateEditTask createEdit = project.getTasks().create(CREATE_EDIT_TASK_NAME, CreateEditTask.class)
        createEdit.setDescription("Start new google play edit")
        createEdit.setGroup(PLAY_GROUP);
        project.afterEvaluate {
            PlayPluginExtension extension = project.playPublisher;
            createEdit.applicationName = extension.applicationName;
            createEdit.packageName = extension.packageName
            createEdit.serviceAccountEmail = extension.serviceAccountEmail
            createEdit.keyP12 = extension.keyP12
            createEdit.clientSecretJson = extension.clientSecretJson
        }

    }

    private void configureCloseEdit(Project project) {

        Task closeEdit = project.getTasks().create(CLOSE_EDIT_TASK_NAME, CloseEditTask.class)
        closeEdit.setDescription("Commits changes to google play")
        closeEdit.setGroup(PLAY_GROUP)
        closeEdit.dependsOn CREATE_EDIT_TASK_NAME

    }

    private void configureDependencies(Project project) {
        project.afterEvaluate { Project p ->

            p.getTasks().withType(PlayEditTask, { PlayEditTask task ->
                if (!(task instanceof CloseEditTask)) {
                    task.dependsOn CREATE_EDIT_TASK_NAME
                    task.finalizedBy CLOSE_EDIT_TASK_NAME
                }
            })
        }
    }

}
