/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import ejbs.Ejbs;
import dtos.UserDto;
import enums.UserStates;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import utils.Message;
import utils.Pages;
import utils.Redirect;

/**
 *
 * @author toure
 */
@ManagedBean
@RequestScoped
public class SignUpManagedBean {

  private UserDto user;
  private String birthDay;

  public SignUpManagedBean() {
    user = new UserDto();
  }

  public void singUp() {
    convertDate(birthDay);
    Ejbs.user().signUp(user);
    Message.Info("Succès de l'inscription !");
    Redirect.redirectTo(Pages.INDEX);
  }

  public void convertDate(String birthDay) {
    try {
      SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
      user.birthDate = df.parse(birthDay);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
  }

  public Long getId() {
    return user.id;
  }

  public void setId(Long id) {
    user.id = id;
  }

  public String getNickName() {
    return user.nickName;
  }

  public void setNickName(String nickName) {
    user.nickName = nickName.trim();
  }

  public String getFirstName() {
    return user.firstName;
  }

  public void setFirstName(String firstName) {
    user.firstName = firstName.trim();
  }

  public String getLastName() {
    return user.lastName;
  }

  public void setLastName(String lastName) {
    user.lastName = lastName.trim();
  }

  public String getPassword() {
    return user.password;
  }

  public void setPassword(String password) {
    user.password = password;
  }

  public String getEmail() {
    return user.email;
  }

  public void setEmail(String email) {
    user.email = email.trim();
  }

  public Date getBirthDate() {
    return user.birthDate;
  }

  public void setBirthDate(Date birthDate) {
    user.birthDate = birthDate;
  }

  public String getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(String birthDay) {
    this.birthDay = birthDay;
  }

  public Date getAddDate() {
    return user.addDate;
  }

  public void setAddDate(Date addDate) {
    user.addDate = addDate;
  }

  public UserStates getState() {
    return user.state;
  }

  public void setState(UserStates state) {
    user.state = state;
  }

}
