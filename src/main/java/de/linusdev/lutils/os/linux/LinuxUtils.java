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

package de.linusdev.lutils.os.linux;

import de.linusdev.lutils.os.OsType;
import de.linusdev.lutils.os.OsUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LinuxUtils {

    public static void showInExplorer(@NotNull Path path) {
        OsUtils.requireOsType(OsType.LINUX);

        path = path.toAbsolutePath();

        if(!Files.isDirectory(path)) {
            //xdg-open can only show a directory. Passing a file would open the file
            //in the users preferred application and not show it in the explorer.
            path = path.getParent();
        }

        xdgOpen(path.toString());

    }

    /**
     * See <a href="https://linux.die.net/man/1/xdg-open">xdg-open man page</a>.
     * @param url url to open
     */
    public static void xdgOpen(@NotNull String url) {
        try {
            ProcessBuilder builder = new ProcessBuilder("xdg-open", url);
            builder.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
