package mc.project.filmBase.service.impl;

import mc.project.filmBase.service.cache.CacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {
    @CacheEvict("ActorPages")
    public void evictActorPages() {}

    @CacheEvict("ActorPages")
    public void evictFilmPages() {}

    @CacheEvict("ActorPages")
    public void evictRatingPages() {}

    @CacheEvict("ActorPages")
    public void evictUserPages() {}


    @CacheEvict("ActorPages")
    public void evictActor(long id) {}

    @CacheEvict("ActorPages")
    public void evictActorFilms(long id) {}


    @CacheEvict("ActorPages")
    public void evictFilm(long id) {}

    @CacheEvict("ActorPages")
    public void evictFilmActors(long id) {}

    @CacheEvict("ActorPages")
    public void evictFilmRatings(long id) {}

    @CacheEvict("ActorPages")
    public void evictFilmConfirmedRatings(long id) {}


    @CacheEvict("ActorPages")
    public void evictRatingFilm(long id) {}

    @CacheEvict("ActorPages")
    public void evictRatingPagesByStatus() {}

    @CacheEvict("ActorPages")
    public void evictRatingUser(long id) {}


    @CacheEvict("ActorPages")
    public void evictUserRatings(long id) {}
}
