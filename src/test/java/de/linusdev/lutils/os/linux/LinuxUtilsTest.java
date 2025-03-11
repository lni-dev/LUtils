package de.linusdev.lutils.os.linux;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisabledIfEnvironmentVariable(named = "EXECUTE_MANUAL_TESTS", matches = "false")
@EnabledOnOs(OS.LINUX)
class LinuxUtilsTest {

    @Test
    void showInExplorer() {
        LinuxUtils.showInExplorer(Paths.get("src"));
    }
}