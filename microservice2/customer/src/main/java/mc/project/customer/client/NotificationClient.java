package mc.project.customer.client;

import mc.project.customer.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION")
public interface NotificationClient {
    @PostMapping("api/v1/notification")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest);
}
