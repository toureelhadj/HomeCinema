/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import enums.TransactionStates;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author seb
 */
@Entity
@Table(name = "TRANSACTIONS")
public class Transaction implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Long id;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "ADD_DATE")
  private java.util.Date addDate;
  
  @Column(name = "_STATE")
  private TransactionStates state;
  
  @Column(name = "BANK_TRANS_NUM")
  private Long bankTransNum;
  
  @OneToMany
  @Column(name = "PRODUCTS")
  private List<Product> products;

  @ManyToOne
  @Column(name = "USER")
  private User user;
  
  @Column(name = "TOTAL_PRICE")
  private Integer totalPrice;
  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getAddDate() {
    return addDate;
  }

  public void setAddDate(Date addDate) {
    this.addDate = addDate;
  }

  public TransactionStates getState() {
    return state;
  }

  public void setState(TransactionStates state) {
    this.state = state;
  }

  public Long getBankTransNum() {
    return bankTransNum;
  }

  public void setBankTransNum(Long bankTransNum) {
    this.bankTransNum = bankTransNum;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
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
    if (!(object instanceof Transaction)) {
      return false;
    }
    Transaction other = (Transaction) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Transaction{" + "id=" + id + ", addDate=" + addDate + ", state=" + state + ", bankTransNum=" + bankTransNum + ", products=" + products + ", user=" + user + ", totalPrice=" + totalPrice + '}';
  }


  
}