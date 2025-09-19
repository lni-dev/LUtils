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

package de.linusdev.lutils.io.tmp;

import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.io.FileUtils;
import de.linusdev.lutils.other.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TmpDir {

    private static final @NotNull String INFO_FILE_NAME = ".lutils-tmp-info.json";

    private final @NotNull Path path;
    private @NotNull TmpDirInfo info;

    public TmpDir(@NotNull Path path) throws IOException {
        this.path = path;

        try(var reader = Files.newBufferedReader(path.resolve(INFO_FILE_NAME))) {
            Json infoJson = JsonParser.DEFAULT_INSTANCE.parseReader(reader);
            this.info = TmpDirInfo.ofJson(infoJson);
        } catch (ParseException e) {
            throw new IOException("Illegal info file in tmp dir", e);
        }
    }

    public TmpDir(@NotNull Path path, long deleteAfter) throws IOException {
        this.path = path;
        this.info = new TmpDirInfo(System.currentTimeMillis(), false, deleteAfter);

        try(var writer = Files.newBufferedWriter(path.resolve(INFO_FILE_NAME))) {
            JsonParser.DEFAULT_INSTANCE.writeData(writer, info.getData());
        }
    }

    public @NotNull Path getPath() {
        return path;
    }

    public void deleteIfItShouldBeDeleted() throws IOException {
        if(info.delete() || (info.created() + info.deleteAfter()) < System.currentTimeMillis()) {
            FileUtils.deleteDirectoryRecursively(path);
        }
    }

    public void markForDeletion() throws IOException {
        info = new TmpDirInfo(info.created(), true, info.deleteAfter());
        try(var writer = Files.newBufferedWriter(path.resolve(INFO_FILE_NAME))) {
            JsonParser.DEFAULT_INSTANCE.writeData(writer, info.getData());
        }
    }

    public void delete() throws IOException {
        // Mark it first incase deletion fails.
        markForDeletion();
        try {
            FileUtils.deleteDirectoryRecursively(path);
        } catch (IOException ignored) {
            // This is fine we marked it before, it will be deleted some other time.
        }
    }

    public void deleteOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
