package com.talking.clock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class ScriptRunner {

    private final boolean isWindows;
    private List<String> output;

    public ScriptRunner() {
        this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public List<String> getTime(Optional<String> time) {
        if(time.isPresent()){
            return this.getTime("sh", "talking_clock.sh", time.get());
        }
        return this.getTime("sh", "talking_clock.sh");
    }

    private List<String> getTime(String... command) {

        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            //builder.command("cmd.exe", "/c", "dir");
            return Arrays.asList("sorry windows script was not in scope.");
        } else {
            builder.command(command);
        }
        builder.directory(new File("/Users/udaypatil/IdeaProjects/talking.clock/src/main/java/com/talking/clock/shell/script"));
        try {
            Process process = builder.start();
            output = new ArrayList<>();
            Future future = Executors.newSingleThreadExecutor().submit(() -> new BufferedReader(new InputStreamReader(process.getInputStream())).lines().forEach(output::add));
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("error exit code: {}", exitCode);
            }
            future.get(10, TimeUnit.SECONDS);
            return output;
        } catch (IOException e) {
            log.error("error starting new process.{}", e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            log.error("error executing task.{}", e.getMessage());
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            log.error("execution interrupted.{}", e.getMessage());
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            log.error("timeout waiting for command output.{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
