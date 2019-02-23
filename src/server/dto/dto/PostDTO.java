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

    public static String generateId() {
        return UUID.randomUUID().toString().substring(0, 20);
    }

    @Override
    public int compareTo(PostDTO o) {
        try {
            SimpleDateFormat sdfA = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfB = new SimpleDateFormat("yyyy-MM-dd");
            Date dA = sdfA.parse(this.date_created);
            Date dB = sdfB.parse(o.date_created);
            return dA.compareTo(dB);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}