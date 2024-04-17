package mc.project1.restapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDto
{
    private long id;
    private String title;
    private String content;
    private LocalDateTime created;
}
