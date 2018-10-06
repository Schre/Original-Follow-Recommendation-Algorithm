package server.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CostObjectDTO {
    public String name;
    public String user_id;
    public String obj_id;
    // TODO: add this to postgre
    public String category;
    /*** This is for determining if this is for ACTUALS or BUDGET*/
    // TODO: Add this to postgre
    public String type;
    public String month;
    public String year;
    public String cost;
}
