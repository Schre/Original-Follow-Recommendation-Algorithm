package server.filesystem;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FSUtils {
    static public Path getUserOwnedFile(String uid, String type, String fid) {
        return Paths.get("./filesystem/" + type + "/" + uid + "/" + fid);
    }

    static public Path getUserOwnedDirectory(String uid, String type) {
        return Paths.get("./filesystem/" + type + "/" + uid);
    }

    static public Path getFile(String url) {
        return Paths.get("./filesystem/" + url);
    }
}
