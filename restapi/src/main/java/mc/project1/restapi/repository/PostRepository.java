package mc.project1.restapi.repository;

import mc.project1.restapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    @Query("select p from Post p where title = :title")
    List<Post> findAllByTitle(String title);
}
