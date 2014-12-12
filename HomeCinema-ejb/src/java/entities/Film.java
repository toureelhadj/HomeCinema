/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import enums.Lang;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author titou
 */
@Entity
@Table(name = "FILMS")
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_FILM")
    private Long id;

    @ManyToMany
    @JoinColumn(name = "PRODUCTS")
    private List<Product> products;

    @JoinColumn(name = "MAIN_PRODUCT")
    @OneToOne
    private Product main_product;

    @Size(max = 255)
    @ElementCollection(fetch = EAGER)
    @MapKeyColumn(name = "locale")
    @CollectionTable(name = "FILM_TITLE", joinColumns = @JoinColumn(name = "film_id"))
    private Map<Lang, String> title;

    @Size(max = 3000)
    @ElementCollection(fetch = EAGER)
    @MapKeyColumn(name = "locale")
    @CollectionTable(name = "FILM_OVERVIEW", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "OVERVIEW", length = 10000)
    private Map<Lang, String> overview;

    @Size(max = 50)
    @Column(name = "COVER_ID")
    private String coverId;

    @Temporal(TemporalType.DATE)
    @Column(name = "RELEASE_DATE")
    private java.util.Date releaseDate;

    @Size(max = 255)
    @JoinColumn(name = "COUNTRIES")
    @ManyToMany
    private Set<Country> countries;

    @Column(name = "RATING", precision = 1, scale = 2)
    private Double rating;

    @Column(name = "RUNTIME")
    private Integer runtime;

    @OneToMany
    @JoinColumn(name = "VIDEO_FILE")
    private Set<Video> videoFile;

    @OneToOne
    @JoinColumn(name = "TRAILLER")
    private Video trailler;

    @ManyToMany
    @JoinTable(name = "ACTORS")
    @JoinColumn(name = "ACTORS")
    private List<Person> actors;

    @ManyToMany
    @JoinTable(name = "DIRECTORS")
    @JoinColumn(name = "DIRECTORS")
    private List<Person> directors;

    @JoinColumn(name = "GENRE")
    @ManyToMany
    private Set<Genre> genre;

    public Film() {
	this.videoFile = new HashSet<>();
	this.products = new ArrayList<>();
	this.genre = new HashSet<>();
	this.actors = new ArrayList<>();
	this.directors = new ArrayList<>();
	this.title = new HashMap<>();
	this.overview = new HashMap<>();
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle(Lang lang) {
	if (title.containsKey(lang)) {
	    return title.get(lang);
	} else {
	    return title.get(Lang.EN);
	}
    }

    public void setTitle(Lang lang, String title) {
	this.title.put(lang, title);
    }

    public List<Product> getProducts() {
	return products;
    }

    public void setProducts(List<Product> products) {
	this.products = products;
    }

    public void addProduct(Product p) {
	this.products.add(p);
    }

    public Product getMain_product() {
	return main_product;
    }

    public void setMain_product(Product main_product) {
	this.main_product = main_product;
    }

    public String getOverview(Lang lang) {
	if (overview.containsKey(lang)) {
	    return overview.get(lang);
	} else {
	    return overview.get(Lang.EN);
	}
    }

    public void setOverview(Lang lang, String overview) {
	this.overview.put(lang, overview);
    }

    public String getCoverId() {
	return coverId;
    }

    public void setCoverId(String coverId) {
	this.coverId = coverId;
    }

    public Date getReleaseDate() {
	return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
	this.releaseDate = releaseDate;
    }

    public Set<Country> getCountries() {
	return countries;
    }

    public void setCountries(Set<Country> countries) {
	this.countries = countries;
    }

    public Double getRating() {
	return rating;
    }

    public void setRating(Double rating) {
	this.rating = rating;
    }

    public Set<Video> getVideoFile() {
	return videoFile;
    }

    public void setVideoFile(Set<Video> videoFile) {
	this.videoFile = videoFile;
    }

    public void addVideoFile(Video videoFile) {
	this.videoFile.add(videoFile);
    }

    public Video getTrailler() {
	return trailler;
    }

    public void setTrailler(Video trailler) {
	this.trailler = trailler;
    }

    public List<Person> getActors() {
	return actors;
    }

    public void setActors(List<Person> actors) {
	this.actors = actors;
    }

    public void addActor(Person a) {
	this.actors.add(a);
    }

    public List<Person> getDirectors() {
	return directors;
    }

    public void setDirectors(List<Person> directors) {
	this.directors = directors;
    }

    public void addDirector(Person d) {
	this.directors.add(d);
    }

    public Set<Genre> getGenre() {
	return genre;
    }

    public void addGenre(Genre g) {
	this.genre.add(g);
    }

    public void setGenre(Set<Genre> genre) {
	this.genre = genre;
    }

    public Integer getRuntime() {
	return runtime;
    }

    public void setRuntime(Integer runtime) {
	this.runtime = runtime;
    }

    public void removeVideo(Video v) {
	this.videoFile.remove(v);
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Film)) {
	    return false;
	}
	Film other = (Film) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "Film{" + "id=" + id + ", products=" + products + ", main_product=" + main_product + ", title=" + title + ", overview=" + overview + ", coverId=" + coverId + ", releaseDate=" + releaseDate + ", countries=" + countries + ", rating=" + rating + ", videoFile=" + videoFile + ", trailler=" + trailler + ", actors=" + actors + ", directors=" + directors + ", genre=" + genre + '}';
    }

    public void removeProduct(Product p) {
	this.products.remove(p);
    }

}
