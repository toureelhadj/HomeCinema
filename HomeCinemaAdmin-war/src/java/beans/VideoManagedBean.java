/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dtos.FilmFicheDto;
import dtos.VideoDto;
import ejbs.ManageFilmRemote;

import ejbs.ManageVideoRemote;
import enums.Lang;
import enums.Langs;
import enums.VideoFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author titou
 */
@ManagedBean
@RequestScoped
public class VideoManagedBean {

    public String getTrailerUrl() {
	return trailer.url;
    }

    public void setTrailerUrl(String trailer) {
	this.trailer.url = trailer;
    }

    public Integer getTrailerResolution() {
	return trailer.resolution;
    }

    public void setTrailerResolution(Integer trailer) {
	this.trailer.resolution = trailer;
    }

    public String getUrl() {

	return vdto.url;
    }

    public void setUrl(String url) {
	this.vdto.url = url;
    }

    public Integer getResolution() {
	return vdto.resolution;
    }

    public void setResolution(Integer resolution) {
	this.vdto.resolution = resolution;
    }
    
        public Langs getLang() {
	return vdto.audio;
    }

    public void setLang(Langs l) {
	this.vdto.audio = l;
    }
    
    public VideoFormat getFormat() {
	return vdto.format;
    }

    public void setFormat(VideoFormat f) {
	this.vdto.format = f;
    }

    public Long getFilm() {
	return film;
    }

    public void setFilm(Long film) {
	this.film = film;
    }

    private ManageFilmRemote filmManager;
    private ManageVideoRemote videoManager;

    private Long film;
    private String title;
    private List<VideoDto> videos;
    private VideoDto trailer =  new VideoDto();
    private VideoDto vdto = new VideoDto();

    public VideoManagedBean() throws NamingException {
	filmManager = (ManageFilmRemote) new InitialContext().lookup("java:global/HomeCinema/HomeCinema-ejb/ManageFilm!ejbs.ManageFilmRemote");
	videoManager = (ManageVideoRemote) new InitialContext().lookup("java:global/HomeCinema/HomeCinema-ejb/ManageVideo!ejbs.ManageVideoRemote");
    }

    public void makeVideo() {
	if (film != null) {
	    FilmFicheDto f = filmManager.getDtoFromId(film,Lang.EN);
	    this.title = f.title;
	    this.videos = f.videos;
	    this.trailer = f.trailler;
	    if (trailer == null) {
		this.trailer = new VideoDto();
	    }
	    if (vdto == null) {
		this.vdto = new VideoDto();
		this.vdto.resolution=240;
		this.vdto.url=" ";
		this.trailer = new VideoDto();
	    }
	} else {
	    this.title = "toutes les videos";
	    this.videos = videoManager.getAllVideo();
	}
    }

    public String getTitle() {
	return title;
    }

    public List<VideoDto> getVideos() {
	return this.videos;
    }

    public void setVideos(List<VideoDto> array) {
	this.videos = array;
    }

    public Integer getTotal() {
	return this.videos.size();
    }

    public void addVideo() {
	this.filmManager.addVideo(film, vdto);
    }

    public void setTrailer() {
	if (trailer.id == null) {
	    this.filmManager.setTrailer(film, trailer);
	} else {
	    this.videoManager.mergeOrSave(trailer);
	}
    }
    
        
    public Langs[] getPossibleLangs() {
      return Langs.values();
    }
    
    public VideoFormat[] getPossibleFormats() {
      return VideoFormat.values();
    }
}
