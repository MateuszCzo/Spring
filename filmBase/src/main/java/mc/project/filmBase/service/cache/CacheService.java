package mc.project.filmBase.service.cache;

public interface CacheService {
    void evictActorPages();

    void evictFilmPages();

    void evictRatingPages();

    void evictUserPages();


    void evictActor(long id);

    void evictActorFilms(long id);


    void evictFilm(long id);

    void evictFilmActors(long id);

    void evictFilmRatings(long id);

    void evictFilmConfirmedRatings(long id);


    void evictRatingFilm(long id);

    void evictRatingPagesByStatus();

    void evictRatingUser(long id);


    void evictUserRatings(long id);

}
