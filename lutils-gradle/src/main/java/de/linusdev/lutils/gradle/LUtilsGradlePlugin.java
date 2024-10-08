package de.linusdev.lutils.gradle;

import de.linusdev.lutils.gradle.constant.JavaConstantGenerator;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.jetbrains.annotations.NotNull;

public class LUtilsGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project target) {
        applyConstantGen(target);
    }

    public void applyConstantGen(@NotNull Project target) {
        TaskProvider<JavaConstantGenerator> genTask = target.getTasks().register("constantGen", JavaConstantGenerator.class);

        target.getPlugins().withId("java", plugin -> {
            target.afterEvaluate(proj -> {
                SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
                SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);

                // Add src
                mainSourceSet.getJava().srcDir(genTask);

                // Ensure the sources are generated before compilation
                target.getTasks().named(mainSourceSet.getCompileJavaTaskName(), JavaCompile.class)
                        .configure(javaCompile -> javaCompile.dependsOn(genTask));
            });
        });
    }

}
