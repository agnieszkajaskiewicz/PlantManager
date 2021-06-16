package com.ajaskiewicz.PlantManager.web.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileDeleteUtil {

    public static void deleteFile(String deleteDir) throws IOException {
        var directory = new File(deleteDir);

        if (!directory.exists()) {
           log.info("No file to delete");
        }

        else {
            var files = directory.listFiles();
            for (var file : files) {
                file.delete();
                log.info("Deleted file: " + file.getName());
            }
        }

        Files.deleteIfExists(Path.of(deleteDir));
        log.info("Deleted directory: " + deleteDir);
    }
}
