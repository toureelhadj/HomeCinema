/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs.admin;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import dtos.FilmDto;
import dtos.FilteredListProductsDto;
import dtos.GenreDto;
import dtos.ProductDto;
import dtos.VideoDto;
import ejbs.ManageProductRemote;
import entities.Film;
import entities.Genre;
import entities.Product;
import enums.OrderTypes;
import enums.ProductTypes;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import managers.dtos.FilmDtoManager;
import managers.dtos.GenreDtoManager;
import managers.dtos.ProductDtoManager;
import managers.entities.ManageEntitieFilm;
import managers.entities.ManageEntitieProduct;

/**
 *
 * @author titou
 */
@Stateless
public class ManageProduct implements ManageProductRemote {

    @PersistenceContext
    public EntityManager em;

    @Override
    public Long createProductWithFilm(FilmDto fdto, VideoDto trailer, VideoDto vid, Double price) {
	Film f = ManageEntitieFilm.createFilmWithVideo(fdto, trailer, vid, em);
	Product p = new Product(f, price);
	f.setMain_product(p);
	em.merge(f);
	em.persist(p);
	return p.getId();
    }

    @Override
    public List<ProductDto> getAllProduct() {
	Query q = em.createQuery("From Product p", Product.class);
	//q.setMaxResults(100);
	List<Product> lp = q.getResultList();
	List<ProductDto> lpdto = new ArrayList<ProductDto>();
	for (Product p : lp) {
	    lpdto.add(ProductDtoManager.getDto(p));
	}
	return lpdto;
    }

    @Override
    public Long createProduct(ProductDto pdto) {
	return ManageEntitieProduct.createProduct(pdto, em).getId();
    }

    @Override
    public void addFilms(Long pid, List<FilmDto> lfdto) {
	for (FilmDto fdto : lfdto) {
	    addFilm(pid, fdto, false);
	}
    }

    @Override
    public void addExistingFilms(Long pid, List<Long> lfid) {
	for (Long fid : lfid) {
	    addExistingFilm(pid, fid, false);
	}
    }

    @Override
    public void addExistingFilm(Long pid, Long fid, boolean main) {
	Product p = em.find(Product.class, pid);
	Film f = em.find(Film.class, fid);
	ManageEntitieProduct.linkProductFilm(f, p);
	if (main) {
	    f.setMain_product(p);
	}
	em.merge(p);
	em.merge(f);
    }

    @Override
    public void addFilm(Long pid, FilmDto fdto, boolean main) {
	Product p = em.find(Product.class, pid);
	Film f = ManageEntitieFilm.createFilm(fdto, em);
	ManageEntitieProduct.linkProductFilm(f, p);
	if (main) {
	    f.setMain_product(p);
	}
	em.merge(f);
	em.merge(p);
    }

    @Override
    public List<FilmDto> getFilms(Long pid) {
	Product p = em.find(Product.class, pid);
	List<FilmDto> lfdto = new ArrayList<>();
	for (Film f : p.getFilms()) {
	    lfdto.add(FilmDtoManager.getDto(f));
	}
	return lfdto;
    }

    @Override
    public ProductDto getProduct(Long pid) {
	Product p = em.find(Product.class, pid);
	return ProductDtoManager.getDto(p);
    }

    @Override
    public ProductDto mergeOrSave(ProductDto pdto) {
	return ProductDtoManager.getDto(ProductDtoManager.mergeOrSave(pdto, em));
    }

    @Override
    public FilteredListProductsDto getFilteredProducts(Long actor, Long director, List<Long> lgdto, String str, String year, OrderTypes sort, Integer limit, Integer row, ProductTypes main) {
	String query = "select distinct p From Product p join p.films f join f.genre g join f.actors a join f.directors d ";
	if (row == null) {
	    row = 0;
	}
	if (limit == null) {
	    limit = 100;
	}
	boolean where = false;
	if (actor != null && !actor.equals(0L)) {
	    query += " where  a.id=" + actor;
	    where=true;
	}
	if (director != null && !actor.equals(0L)) {
	    if (where) {
		query += " and ";
	    } else {
		query += " where ";
		where=true;
	    }
	    query += " d.id" + director;
	}
	if (lgdto != null && !lgdto.isEmpty()) {
	    if (where) {
		query += " and (";
		
	    } else {
		query += " where (";
		where=true;
	    }
	    boolean first = true;
	    for ( Long g : lgdto)
	    {
		if (first )
		    first=false;
		else
		    query+=" or ";
		query += " g.id="+g;
	    }
	    query+=" ) ";
	}
	if (str!=null && !str.equals("")  )
	{
	    	    if (where) {
		query += " and (";
		where=true;
	    } else {
		query += " where (";
	    }
		    query += "  f.title like '%"+str+"%' or p.name like '%"+str+"%' ) "; 
	}
	Query q = em.createQuery(query, Product.class);
	q.setFirstResult(row);
	q.setMaxResults(limit);
	List<Product> lpdto = q.getResultList();
	List<ProductDto> res = new ArrayList<>();
	for (Product p : lpdto) {
	    res.add(ProductDtoManager.getDto(p));
	}
	return new FilteredListProductsDto(res, 3500);
    }

    @Override
    public List<GenreDto> getAllGenres() {
	Query q = em.createQuery("From Genre g", Genre.class);
	List<Genre> lg = q.getResultList();
	List<GenreDto> lgdto = new ArrayList<GenreDto>();
	for (Genre g : lg) {
	    lgdto.add(GenreDtoManager.getDto(g));
	}
	return lgdto;
    }
}
