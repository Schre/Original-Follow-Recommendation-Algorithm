package server.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileReader {
    public String readText(String user_id, String type, String file_id) {
        Path textFile = FSUtils.getUserOwnedFile(user_id, type, file_id);
        String ret = null;
        try {
            java.io.FileReader reader = new java.io.FileReader(textFile.toString());
            StringBuilder builder = new StringBuilder();
            char c;
            while ( (c = (char)reader.read()) != (char)-1) {
                builder.append(c);
            }
            ret = builder.toString();
        }
        catch (IOException ioe) {
            System.err.println("Failed to create file at path: " + textFile);
            ioe.printStackTrace();
        }

        return ret;
    }
}
