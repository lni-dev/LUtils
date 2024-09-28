package de.linusdev.lutils.gradle;

import de.linusdev.lutils.gradle.constant.JavaConstantGenerator;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

public class LUtilsGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project target) {
        applyConstantGen(target);
    }

    public void applyConstantGen(@NotNull Project target) {
        TaskProvider<JavaConstantGenerator> genTask = target.getTasks().register("constantGen", JavaConstantGenerator.class);

        // Add the generated source directory to the main source set
        target.getPlugins().withId("java", plugin -> {
            SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
            SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);

            // Add the generated source directory to the source set
            SourceDirectorySet javaSourceSet = mainSourceSet.getJava();
            javaSourceSet.srcDir(genTask);

            // Ensure the sources are generated before compilation
            target.getTasks().getByName(mainSourceSet.getCompileJavaTaskName()).dependsOn(genTask);
        });
    }

}
