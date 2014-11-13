/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.CaddieDto;
import javax.ejb.Remote;

/**
 *
 * @author titou
 */
@Remote
public interface ManageTransactionRemote {

    public CaddieDto getCaddieDto (Long id_user);
    
    public CaddieDto addProduct(Long user, Long id);
    
    public Long validate(Long user);
    
    public void validatePayement(Long id,Long btn);
}
