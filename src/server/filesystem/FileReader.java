package server.filesystem;

import server.dto.dto.PostDTO;
import server.restapi.databaserestservice.PostRestService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileReader {
    public String readFile(String user_id, String type, String file_id) {
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

    public File getImage(String user_id, String file_id) {
        Path textFile = FSUtils.getUserOwnedFile(user_id, "image", file_id);
        File f = new File(textFile.toString());
        if (!f.exists()) {
            f = new File(FSUtils.getFile("image/defaults/newusericon").toString());
        }

        return f;
    }

    public List<PostDTO> readFiles(String user_id, String type) {
        Path typeDirectory = FSUtils.getUserOwnedDirectory(user_id, type);
        String ret = null;
        try {
            // get all files in directory
            //PostRestService service = new PostRestService();
            File directory = new File(typeDirectory.toString());
            for (File file : directory.listFiles()) {

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
