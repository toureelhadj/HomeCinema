/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dtos.FilmDto;
import dtos.ProductDto;
import dtos.SimpleUserDto;
import dtos.UserDto;
import dtos.VideoDto;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

/**
 *
 * @author titou
 */
public class JeuDeTest2 {

    public static void main(String[] args) {
        try {
            Admin a = new Admin();
/*
            cree american beauty
            cree fight club 
            cree star wars
            cree produit pour les 3
            
            cree user robin
            
            achete american beauty
            
            met fight club dans son panier
            
            
            
            
            */
            
            
            //creer american beauty
            FilmDto f = a.createFilm(14L);
            a.getManageFilmRemote().createFilm(f);
            VideoDto v = new VideoDto();
            v.resolution = 240;
            v.url = "http://geekompagny.ddns.net/ECOM/AmericanBeautyFilm.mp4";
            v.id = a.getManageVideoRemote().createVideo(v);
            a.getManageFilmRemote().addExistingVideo(f.id, v.id);
            VideoDto t = new VideoDto();
            t.resolution = 240;
            t.url = "http://geekompagny.ddns.net/ECOM/AmericanBeautyTrailer.mp4";
            a.getManageFilmRemote().setTrailer(f.id, t);

            //creer un produit
            ProductDto pdto = new ProductDto();
            pdto.name = "Main american beauty";
            pdto.price = 10D;
            pdto.id = a.getManageProductRemote().createProduct(pdto);
            a.getManageProductRemote().addExistingFilm(pdto.id, f.id, true);

                        FilmDto f3 = a.createFilm(11L);
            a.getManageFilmRemote().createFilm(f3);
            VideoDto v3 = new VideoDto();
            v3.resolution = 240;
            v3.url = "http://geekompagny.ddns.net/ECOM/AmericanBeautyFilm.mp4";
            v3.id = a.getManageVideoRemote().createVideo(v3);
            a.getManageFilmRemote().addExistingVideo(f3.id, v3.id);
            VideoDto t3 = new VideoDto();
            t3.resolution = 240;
            t3.url = "http://geekompagny.ddns.net/ECOM/AmericanBeautyTrailer.mp4";
            a.getManageFilmRemote().setTrailer(f3.id, t3);

            //creer un produit
            ProductDto pdto3 = new ProductDto();
            pdto3.name = "star wars";
            pdto3.price = 10D;
            pdto3.id = a.getManageProductRemote().createProduct(pdto3);
            a.getManageProductRemote().addExistingFilm(pdto3.id, f3.id, true);
            
                        //creer fight club
            FilmDto f2 = a.createFilm(550L);
            a.getManageFilmRemote().createFilm(f2);
            VideoDto v2 = new VideoDto();
            v2.resolution = 240;
            v2.url = "http://geekompagny.ddns.net/ECOM/FightClubFilm.mp4";
            v2.id = a.getManageVideoRemote().createVideo(v2);
            a.getManageFilmRemote().addExistingVideo(f2.id, v2.id);
            VideoDto t2 = new VideoDto();
            t2.resolution = 240;
            t2.url = "http://geekompagny.ddns.net/ECOM/FightClubTrailer.mp4";
            a.getManageFilmRemote().setTrailer(f2.id, t2);

            //creer un produit
            ProductDto pdto2 = new ProductDto();
            pdto2.name = "Main FC";
            pdto2.price = 8D;
            pdto2.id = a.getManageProductRemote().createProduct(pdto2);
            a.getManageProductRemote().addExistingFilm(pdto2.id, f2.id, true);
            
            
            
            
            //creer user robin
            UserDto u = new UserDto();
            u.birthDate = new Date();
            u.email = "truc@mail.net";
            u.firstName = "robin";
            u.nickName = "grandchamp";
            u.password = "password";

            a.getManageUserRemote().signUp(u);
            u = a.getManageUserRemote().login(u.email, u.password);
            
            
            //achat american beauty
            a.getManagetransactionRemote().addProduct(u.id, pdto.id);
            Long trans = a.getManagetransactionRemote().validate(u.id);
            a.getManagetransactionRemote().validatePayement(trans, 42L);
            
            //met fight club dans panier
            a.getManagetransactionRemote().addProduct(u.id, pdto2.id);
            
            //print
            System.out.println("Film");
            List<FilmDto> lfdto = a.getManageFilmRemote().getAllFilm();
            for (FilmDto fdto : lfdto) {
                System.out.println(fdto.title);
            }
            System.out.println("Product");
            List<ProductDto> lpdto = a.getManageProductRemote().getAllProduct();
            for (ProductDto udto : lpdto) {
                System.out.println(udto.name);
            }
            System.out.println("User");
            Set<SimpleUserDto> ludto = a.getManageUserRemote().getAllUser();
            for (SimpleUserDto udto : ludto) {
                System.out.println(udto.nickName);
            }
            
            //test
            
            a.getManageFilmRemote().addGenres(14L, a.getGenre(14L));
            a.getManageFilmRemote().addActors(14L, a.getCast(14L));
            a.getManageFilmRemote().addDirectors(14L, a.getDirectors(14L));
            
        } catch (JSONException ex) {
            Logger.getLogger(JeuDeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JeuDeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JeuDeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
