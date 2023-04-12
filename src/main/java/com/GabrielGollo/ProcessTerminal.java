package com.GabrielGollo;

import java.io.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class ProcessTerminal {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static Process runCommand(File whereToRun, String command) throws Exception {
        System.out.println("Running in: " + whereToRun);
        System.out.println("Command: " + command);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(whereToRun);

        if(isWindows) {
            builder.command("cmd.exe", "/c", command);
        }else {
            builder.command("sh", "-c", command);
        }

        Process process = builder.start();

        OutputStream outputStream = process.getOutputStream();
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        printStream(inputStream);
        printStream(errorStream);

        boolean isFinished = process.waitFor(5, TimeUnit.SECONDS);
        outputStream.flush();
        outputStream.close();
        if(!isFinished) {
            process.destroyForcibly();
        }

        return process;
    }

    public static void killAppByName(String name){
        try {
            String command = "taskkill /F /IM " + name;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStream(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int date = new Date().getSeconds();
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);

                if(new Date().getSeconds() - date > 20) {
                    System.out.println("End of log stream");
                    break;
                }
            }

        }
    }
}
