package mc.project.filmBase.mapper;

import mc.project.filmBase.dto.response.RatingResponse;
import mc.project.filmBase.model.Rating;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingMapper {
    public RatingResponse mapToRatingResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .description(rating.getDescription())
                .rating(rating.getRating())
                .status(rating.getStatus())
                .build();
    }

    public List<RatingResponse> mapToRatingResponse(Collection<Rating> ratings) {
        if (ratings == null) return Collections.emptyList();

        return ratings.stream()
                .map(this::mapToRatingResponse)
                .collect(Collectors.toList());
    }
}
