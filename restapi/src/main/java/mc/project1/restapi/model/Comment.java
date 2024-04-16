package mc.project1.restapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment
{
    @Id
    private long id;
    private String content;
    private LocalDateTime created;
}
