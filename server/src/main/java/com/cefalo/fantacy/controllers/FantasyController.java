package com.cefalo.fantacy.controllers;


import com.cefalo.fantacy.models.Genre;
import com.cefalo.fantacy.models.Movie;
import com.cefalo.fantacy.services.MovieDataService;
import com.cefalo.fantacy.services.FantasyDataService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;


@Controller
@CrossOrigin(maxAge = 3600)
@Path("/api/")
public class FantasyController {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FantasyController.class);

    private MovieDataService movieDataService;

    private FantasyDataService fantasyDataService;

    @Autowired
    public void setFantasyDataService(FantasyDataService fantasyDataService) {
        this.fantasyDataService = fantasyDataService;
    }


    @Autowired
    public void setMovieDataService(MovieDataService movieDataService) {
        this.movieDataService = movieDataService;
    }


    @GET
    @Path("movies")
    @Produces("application/json")
    public List<Genre> listMovies() {
        List<Genre> genresList = movieDataService.getAllGenres();
        for (Genre genre : genresList) {
            List<Movie> movies = movieDataService.getThreeMoviesByGenre(genre.getName());
            genre.setMovies(movies);
        }
        return genresList;
    }

    @GET
    @Path("genre/{id}")
    @Produces("application/json")
    public Genre getTopMoviesOfAGenre(@PathParam("id") long id) {
        Genre genre = movieDataService.findGenreById(id);
        genre.setMovies(movieDataService.getTopMoviesByGenre(genre.getName()));
        return genre;
    }

    @GET
    @Path("movie/{id}")
    @Produces("application/json")
    public Movie getMovieDetails(@PathParam("id") long id) {
        return movieDataService.findMovieById(id);
    }

    @GET
    @Path("cast-crew/{tmdbid}")
    @Produces("application/json")
    public Object getCastAndCrewList(@PathParam("tmdbid") String tmdbId) {
        return fantasyDataService.getCastAndCrewDetails(tmdbId);
    }

    @GET
    @Path("trailer/{id}")
    @Produces("application/json")
    public Object getTrailerUrlOfMovie(@PathParam("id") String movieId) {
        return fantasyDataService.getMovieVideos(movieId);
    }

    @GET
    @Path("related/movies/{id}")
    @Produces("application/json")
    public List<Movie> getRelatedMovies(@PathParam("id") Long movieId) {
        List<Movie> relatedMovies = new ArrayList<>();
        Movie movie = movieDataService.findMovieById(movieId);
        String[] currentGenres = movie.getGenres().split("\\|");
        if (currentGenres.length > 1) {
            for (String genre : currentGenres) {
               List<Movie> randMovies =  movieDataService.getThreeMoviesByGenre(genre);
               relatedMovies.addAll(randMovies);
            }
        }

        return relatedMovies;
    }
}
