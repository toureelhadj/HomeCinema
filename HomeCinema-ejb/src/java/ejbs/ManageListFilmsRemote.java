/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.FilmDto;
import dtos.GenreDto;
import java.util.Date;
import java.util.List;

/**
 *
 * @author narcisse
 */
public interface ManageListFilmsRemote {

    public List<FilmDto> findAllFilms();

    public List<FilmDto> FindFilmsByActor(String actorName);

    public List<FilmDto> FindFilmsByYear(Date year);

    public List<FilmDto> FindFilmsByTitle(String title);

    public List<FilmDto> FindTopN(Integer N);

    public List<FilmDto> FindNewFilms();

    public List<FilmDto> FilmsOrderedByalph();
    
    public List<FilmDto> FilmsOrderedByYear() ;

    public List<FilmDto> FindFilmsB(Long actor, Long director, List<GenreDto> lgdto, String str);

}
