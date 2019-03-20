package server.filesystem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter {

    public boolean writeFile(String user_id, String file_id, String type, String content) {
        Path textFolder = FSUtils.getUserOwnedDirectory(user_id, type);
        if (!Files.exists(textFolder)) {
            try {
                Files.createDirectory(textFolder);
            }
            catch (Exception e) {
                System.err.println("Failed to create directory at path: " + textFolder);
                e.printStackTrace();
                return false;
            }
        }

        Path textFile = FSUtils.getUserOwnedFile(user_id, type, file_id);
        try {
            Files.createFile(textFile);
            java.io.FileWriter writer = new java.io.FileWriter(textFile.toString());
            BufferedWriter bw = new BufferedWriter(writer);

            writer.write(content);
            writer.flush();
            writer.close();
        }
        catch (IOException ioe) {
            System.err.println("Failed to create file at path: " + textFile);
            ioe.printStackTrace();
            return false;
        }

        // Write content


        return true;
    }

    public boolean editFile(String user_id, String file_id, String type, String content) {
        Path textFile = FSUtils.getUserOwnedFile(user_id, type, file_id);
        try {
            java.io.FileWriter writer = new java.io.FileWriter(textFile.toString());
            writer.write(content);
            writer.close();
        }
        catch (IOException ioe) {
            System.err.println("Failed to create file at path: " + textFile);
            ioe.printStackTrace();
            return false;
        }

        // Write content


        return true;
    }
}
