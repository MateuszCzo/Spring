package mc.project1.restapi.service;

import mc.project1.restapi.model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser
class PostServiceTest
{
    @Autowired
    private PostService postService;

    @Test
    void shouldGetPost()
    {
        // Given

        // When
        Post post = postService.getPost(1L);

        // Then
        Assertions.assertNotNull(post);
        Assertions.assertEquals(post.getId(), 1L);
    }
}