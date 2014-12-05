/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.FilmDto;
import dtos.SimpleUserDto;
import dtos.TransactionDto;
import dtos.UserDto;
import dtos.UserDtoNoPw;
import exception.ActivatedCodeException;
import exception.SignupEmailException;
import exception.SignupNickNameException;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 *
 * @author toure
 */
@Remote
public interface ManageUserRemote {

    public UserDto signUp(UserDto user) throws SignupEmailException, SignupNickNameException;
    
    public void save(UserDtoNoPw user);
    
    public UserDto login(String email, String password);
    
    public Set<SimpleUserDto> getAllUser();
    
    public UserDtoNoPw getUser(Long id);
    
    public void removeUser (Long id);
    
    public void mergeOrSave (UserDtoNoPw udto);
    
    public List<FilmDto> getFilms(Long id);
    
    public  List<TransactionDto> getTransaction (Long user);

    public boolean changePassword(Long id, String oldPassword, String newPassword);
    
    public boolean changeEmail(Long id, String email, String newPassword);

    public void activate(Long user, String code) throws ActivatedCodeException;
    
    public void activate(Long user);

    public void deactivate(Long user);
}
