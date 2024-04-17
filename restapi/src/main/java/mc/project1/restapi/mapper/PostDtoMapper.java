package mc.project1.restapi.mapper;

import mc.project1.restapi.dto.PostDto;
import mc.project1.restapi.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostDtoMapper
{
    private PostDtoMapper() {}

    public static List<PostDto> mapToPostDtos(List<Post> posts)
    {
        return posts.stream()
                .map(PostDtoMapper::mapToPostDto)
                .collect(Collectors.toList());
    }

    public static PostDto mapToPostDto(Post post)
    {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .created(post.getCreated())
                .build();
    }
}
