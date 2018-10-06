package server.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionalItemDTO {
    public String date;
    public String transaction;
    public double amount;
    public String description;
    public String paymentFrequency;
    public String category;
    public String id;
    public String uid;
}
