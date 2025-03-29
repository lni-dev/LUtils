/*
 * Copyright (c) 2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
