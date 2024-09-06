package mc.project.online_store.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class OrderResponse {
    private long id;
    private float price;
    private Date date;
}
