/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs.admin;

import dtos.FilmDto;
import dtos.FilteredListProductsDto;
import dtos.GenreDto;
import dtos.ProductDto;
import dtos.VideoDto;
import ejbs.ManageProductRemote;
import entities.Film;
import entities.Genre;
import entities.Product;
import enums.Lang;
import enums.OrderTypes;
import enums.ProductStates;
import enums.ProductTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Long createProductWithFilm(FilmDto fdto, VideoDto trailer, VideoDto vid, Double price,Lang lang) {
	Film f = ManageEntitieFilm.createFilmWithVideo(fdto, trailer, vid,lang, em);
	Product p = new Product(f, price);
	f.setMain_product(p);
	em.merge(f);
	em.persist(p);
	return p.getId();
    }
    @Override
    public List<ProductDto> getAllProduct(Lang lang) {
	Query q = em.createQuery("From Product p", Product.class);
	List<Product> lp = q.getResultList();
	List<ProductDto> lpdto = new ArrayList<ProductDto>();
	for (Product p : lp) {
	    lpdto.add(ProductDtoManager.getDto(p,lang));
	}
	return lpdto;
    }

    @Override
    public Long createProduct(ProductDto pdto,Lang lang) {
	return ManageEntitieProduct.createProduct(pdto,lang, em).getId();
    }

    @Override
    public void addFilms(Long pid,Lang lang ,List<FilmDto> lfdto) {
	for (FilmDto fdto : lfdto) {
	    addFilm(pid, fdto,lang, false);
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
    public void addFilm(Long pid, FilmDto fdto,Lang lang, boolean main) {
	Product p = em.find(Product.class, pid);
	Film f = ManageEntitieFilm.createFilm(fdto,lang, em);
	ManageEntitieProduct.linkProductFilm(f, p);
	if (main) {
	    f.setMain_product(p);
	}
	em.merge(f);
	em.merge(p);
    }

    @Override
    public void removeFilm(Long pid, Long fid) {
	Product p = em.find(Product.class, pid);
	Film f = em.find(Film.class, fid);
	ManageEntitieProduct.unlinkProductFilm(f, p);
	if (Objects.equals(f.getMain_product().getId(), pid)) {
	    f.setMain_product(null);
	}
	if (p.getFilms().isEmpty()) {
	    p.setState(ProductStates.Unactivated);
	}
	em.merge(f);
	em.merge(p);
    }

    @Override
    public List<FilmDto> getFilms(Long pid,Lang lang) {
	Product p = em.find(Product.class, pid);
	List<FilmDto> lfdto = new ArrayList<>();
	for (Film f : p.getFilms()) {
	    lfdto.add(FilmDtoManager.getDto(f,lang));
	}
	return lfdto;
    }

    @Override
    public ProductDto getProduct(Long pid,Lang lang) {
	Product p = em.find(Product.class, pid);
	return ProductDtoManager.getDto(p,lang);
    }

    @Override
    public ProductDto mergeOrSave(ProductDto pdto,Lang lang) {
	return ProductDtoManager.getDto(ProductDtoManager.mergeOrSave(pdto,lang, em),lang);
    }

    @Override
    public FilteredListProductsDto getFilteredProducts(Long actor, Long director, List<Long> lgdto, String str, String year, OrderTypes sort, Integer limit, Integer row, ProductTypes main,Lang lang) {
	boolean or = true;//join p.name pn      join f.title t
	String query = "From Product p join p.films f left join f.genre g left join f.actors a left join f.directors d where p.state=:active ";
	if (row == null) {
	    row = 0;
	}
	if (limit == null) {
	    limit = 100;
	}
	if (actor != null && !actor.equals(0L)) {
	    query += " and a.id=" + actor;
	}
	if (director != null && !director.equals(0L)) {
	    query += "and d.id=" + director;
	}
	if (lgdto != null && !lgdto.isEmpty()) {
	    query += " and (";
	    boolean first = true;
	    for (Long g : lgdto) {
		if (first) {
		    first = false;
		} else {
		    if (or) {
			query += " or ";
		    } else {
			query += " and ";
		    }
		}
		query += " g.id=" + g;
	    }
	    query += " ) ";
	}
	if (str != null && !str.equals("")) {
	    query += " and (  VALUE(t) LIKE '%"+str+"%'  ) ";//or VALUE(pn) LIKE '%"+str+"%'
	}
	if (main.equals(ProductTypes.Main)) {
	    query += " and size(p.films )=1 ";
	} else if (main.equals(ProductTypes.Pack)) {

	    query += " and size(p.films )>1 ";
	} 
	Query qnb = em.createQuery("select COUNT(distinct p) "+ query);
	qnb.setParameter("active", ProductStates.Activated);
	Long nb = (Long) qnb.getSingleResult();
	switch (sort) {
	    case RATING:
		query += "group by p order by  AVG(f.rating) desc ";
		break;
	    case SALES:
		query += "order by p.nbSales desc";
		break;
	    case ALPH:
		//query += "order by VALUE(pn) ";
		break;
	    case NEW:
		query += "order by p.addDate desc";
		break;
	    case RAND:
		row = (int) (Math.random() * (getNbProduct() - limit));
		break;
	}
	if (row < 0) {
	    row = 0;
	}
	Query q = em.createQuery("select distinct p  "+query, Product.class);
	q.setParameter("active", ProductStates.Activated);
	q.setFirstResult(row);
	q.setMaxResults(limit);
	List<Product> lpdto = q.getResultList();
	List<ProductDto> res = new ArrayList<>();
	for (Product p : lpdto) {
	    res.add(ProductDtoManager.getDto(p,lang));
	}
	return new FilteredListProductsDto(res, nb.intValue());
    }

    public Long getNbProduct() {
	String sql = "SELECT COUNT(p) FROM Product p";
	Query q = em.createQuery(sql);
	return (long) q.getSingleResult();
    }

    @Override
    public List<GenreDto> getAllGenres(Lang lang) {
	Query q = em.createQuery("From Genre g", Genre.class);
	List<Genre> lg = q.getResultList();
	List<GenreDto> lgdto = new ArrayList<>();
	for (Genre g : lg) {
	    lgdto.add(GenreDtoManager.getDto(g,lang));
	}
	return lgdto;
    }

    @Override
    public void activate(Long pid) {
	Product p = em.find(Product.class, pid);
	if (!p.getFilms().isEmpty())
	{
	p.setState(ProductStates.Activated);
	em.merge(p);
	}
    }

    @Override
    public void deactivate(Long pid) {
	Product p = em.find(Product.class, pid);
	p.setState(ProductStates.Unactivated);
	em.merge(p);
    }
}
