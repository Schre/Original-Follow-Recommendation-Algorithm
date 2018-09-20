package server.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CostObjectDTO {
    public String name;
    public String user_id;
    public String obj_id;
    public String month;
    public String year;
    public String cost;
}
