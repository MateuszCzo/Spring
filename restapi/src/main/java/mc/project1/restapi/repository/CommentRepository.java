package mc.project1.restapi.repository;

import mc.project1.restapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    public List<Comment> findAllByPostIdIn(List<Long> postsIds);
}
