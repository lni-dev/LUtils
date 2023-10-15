package de.linusdev.lutils.os.windows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;

@DisabledIfEnvironmentVariable(named = "EXECUTE_MANUAL_TESTS", matches = "false")
@EnabledOnOs(OS.WINDOWS)
class WinUtilsTest {

    @Test
    void showInExplorer() {
        WinUtils.showInExplorer(Paths.get("build.gradle")
                .toAbsolutePath());
    }
}