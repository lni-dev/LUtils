package de.linusdev.lutils.os.windows;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WinUtils {

    public static void showInExplorer(@NotNull Path path) {
        path = path.toAbsolutePath();

        try {
            ProcessBuilder builder = new ProcessBuilder("explorer",
                    (Files.isDirectory(path) ? "" : "/select,") + path.toAbsolutePath());

            builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
