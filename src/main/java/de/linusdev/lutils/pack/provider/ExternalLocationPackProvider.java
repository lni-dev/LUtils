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

package de.linusdev.lutils.pack.provider;

import de.linusdev.lutils.io.FileUtils;
import de.linusdev.lutils.other.debug.DebugInfoStringBuilder;
import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.errors.PackException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A simple {@link PackProvider}, which provides packs from a specific directory.
 * The packs inside the directory can be extracted or a zip file.
 */
public class ExternalLocationPackProvider implements PackProvider {

    private final @NotNull Path packsPath;
    private final @NotNull Function<Path, AbstractPack> packExtractedCreator;
    private final @NotNull Function<Path, AbstractPack> packArchiveCreator;

    public ExternalLocationPackProvider(
            @NotNull Path packsPath,
            @NotNull Function<Path, AbstractPack> packExtractedCreator,
            @NotNull Function<Path, AbstractPack> packArchiveCreator
    ) throws IOException {
        this.packsPath = packsPath;
        this.packExtractedCreator = packExtractedCreator;
        this.packArchiveCreator = packArchiveCreator;

        if(!Files.exists(packsPath))
            Files.createDirectory(packsPath);

        if(!Files.isDirectory(packsPath))
            throw new IllegalStateException("'" + packsPath + "' is not a directory");
    }

    @Override
    public @NotNull List<@NotNull AbstractPack> provide() throws PackException {

        try(var files = Files.list(packsPath)) {
            List<Path> collectedFiles = files.toList();
            ArrayList<AbstractPack> packs = new ArrayList<>(collectedFiles.size());

            for (Path file : collectedFiles) {
                AbstractPack potentialPack;
                if(Files.isDirectory(file)) {
                    potentialPack = packExtractedCreator.apply(file);

                } else if(Files.isRegularFile(file) && "zip".equals(FileUtils.getFileEnding(file))) {
                    potentialPack = packArchiveCreator.apply(file);

                } else {
                    // Skip all random files
                    continue;
                }

                if(!potentialPack.isPack())
                    continue;

                packs.add(potentialPack);
            }

            return packs;

        } catch (IOException e) {
            throw new PackException(null, "Cannot read packs from '" + packsPath.toAbsolutePath() + "'", e);
        }
    }

    @Override
    public @NotNull String name() {
        return "PackProvider(" + packsPath.toAbsolutePath() + ")";
    }

    @Override
    public @NotNull String debug_info_string(int maxDepth) {
        return new DebugInfoStringBuilder(this, name(), maxDepth)
                .addInformation("path", packsPath.toAbsolutePath())
                .build();
    }
}
