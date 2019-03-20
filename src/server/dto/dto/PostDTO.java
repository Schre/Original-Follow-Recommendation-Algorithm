package server.dto.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO implements Comparable<PostDTO>{
    public String post_id;
    public String user_id;
    public String category;
    public String type;
    public String date_created;
    public String post_url;
    public String content;

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 20);
    }

    @Override
    public int compareTo(PostDTO o) {
        try {
            Date dateA = new Date(Long.parseLong(this.date_created));
            Date dateB = new Date(Long.parseLong(o.date_created));

            return dateB.compareTo(dateA);
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}