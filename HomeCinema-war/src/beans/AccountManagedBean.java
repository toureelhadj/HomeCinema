/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dtos.CaddieDto;
import dtos.FilmDto;
import dtos.ProductDto;
import ejbs.ManageProductRemote;
import ejbs.ManageTransactionRemote;
import ejbs.ManageUserRemote;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author titou
 */
@ManagedBean
@ViewScoped
public class AccountManagedBean {

    @EJB
    private ManageTransactionRemote transactionManager;

    @EJB
    private ManageProductRemote productManager;

    @EJB
    private ManageUserRemote userManager;

    public CaddieDto cdto;

    public String initBox;

    public Long idUser;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getInitBox() {
        return initBox;
    }

    public void setInitBox(String b) {
        String[] ref = {"films", "caddie", "historique", "infos"};
        List<String> list = Arrays.asList(ref);
        if (!list.contains(b) || b == null) {
            b = ref[0];
        }
        initBox = b;
    }

    public AccountManagedBean() throws NamingException {
        this.cdto = new CaddieDto();
        this.initBox = "films";
    }

    public String getHtmlForLink(String link, String title) {
        return "<p id=\"link-" + link + "\" " + ((link.equals(initBox)) ? "class=\"activated\"" : "") + "><a onclick=\"display('" + link + "'); return false;\" href=\"\">" + title + "</a></p>";
    }

    public String getClassDisplay(String link) {
        if (!link.equals(initBox)) {
            return "box-right display-none";
        } else {
            return "box-right";
        }
    }

    public List<List<String>> getListCaddie() {
        List<List<String>> toReturn = new ArrayList<>();
        this.cdto = transactionManager.getCaddieDto(idUser);
        if (cdto.films.isEmpty()) {
            List<String> toAdd = new ArrayList<>();
            toAdd.add("EMPTY");
            toAdd.add("");
            toReturn.add(toAdd);
            return toReturn;
        }

        int i = 1;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy");

        for (ProductDto pd : cdto.films) {
            List<String> toAdd = new ArrayList<>();
            List<FilmDto> list_films = productManager.getFilms(pd.id);
            if (list_films.size() == 1) {
                FilmDto f = list_films.get(0);
                toAdd.add("SOLO");
                toAdd.add(pd.id.toString());
                toAdd.add(f.id.toString());
                toAdd.add(f.title);
                toAdd.add(pd.price.toString());
                toAdd.add(f.cover);
                toAdd.add(formater.format(f.release_date));
                toAdd.add(i + "");
                toReturn.add(toAdd);
            } else {
                toAdd.add("FIRST");
                toAdd.add(pd.id.toString());
                toAdd.add(list_films.size() + "");
                toAdd.add(pd.name);
                toAdd.add(pd.price.toString());
                toAdd.add(i + "");
                toReturn.add(toAdd);
                for (FilmDto f : list_films) {
                    List<String> toAdd2 = new ArrayList<>();
                    toAdd2.add("SIMPLE");
                    toAdd2.add(pd.id.toString());
                    toAdd2.add(f.id.toString());
                    toAdd2.add(f.cover);
                    toAdd2.add(f.title);
                    toAdd2.add(formater.format(f.release_date));
                    toAdd2.add(i + "");
                    toReturn.add(toAdd2);
                }
            }
            i++;
        }
        return toReturn;
    }

    public void deleteFromCaddie(Long idProduct) throws IOException {
        transactionManager.removeProduct(idUser, idProduct);
        FacesContext.getCurrentInstance().getExternalContext().redirect("moncompte.xhtml?box=caddie");
    }

    public List<List<String>> getListFilms() {
        List<List<String>> toReturn = new ArrayList<>();
        List<FilmDto> list = userManager.getFilms(idUser);

        if (list.isEmpty()) {
            List<String> toAdd = new ArrayList<>();
            toAdd.add("EMPTY");
            toReturn.add(toAdd);
            return toReturn;
        }

        SimpleDateFormat formater = new SimpleDateFormat("yyyy");

        for (FilmDto f : userManager.getFilms(idUser)) {
            List<String> toAdd = new ArrayList<>();
            toAdd.add("SIMPLE");
            toAdd.add(f.cover);
            toAdd.add(f.title);
            toAdd.add(formater.format(f.release_date));
            toAdd.add(f.id.toString());
            toReturn.add(toAdd);
        }

        return toReturn;
    }

    public boolean isOneOfMyFilm(Long idfilm, Long iduser) {
        if (iduser == null) {
            return false;
        }
        for (FilmDto l : userManager.getFilms(iduser)) {
            if (l.id.equals(idfilm)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInMyCaddie(Long idfilm, Long iduser) {
        if (iduser == null) {
            return false;
        }
        for (ProductDto l : transactionManager.getCaddieDto(iduser).films) {
            for (FilmDto f : productManager.getFilms(l.id)) {
                if (f.id.equals(idfilm)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFree(Long idfilm, Long iduser) {
        if (iduser == null) {
            return false;
        }
        return !isInMyCaddie(idfilm, iduser) && !isOneOfMyFilm(idfilm, iduser);
    }

    public void addProductFilmToCaddie(Long iduser, Long idproduct, Long idfilm) throws IOException {
        this.cdto = transactionManager.addProduct(iduser, idproduct);
        FacesContext.getCurrentInstance().getExternalContext().redirect("fiche_film.xhtml?id="+idfilm);
    }
    
    public void checkIsMyFilm (Long idfilm, Long iduser) throws IOException {
        if (!isOneOfMyFilm(idfilm, iduser))
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
}
