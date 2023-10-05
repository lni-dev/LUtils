package de.linusdev.lutils.os.windows;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WinUtilsTest {

    @Test
    void showInExplorer() {
        WinUtils.showInExplorer(Paths.get("build.gradle")
                .toAbsolutePath());
    }
}