/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers.dtos;

import dtos.TransactionDto;
import entities.Transaction;

/**
 *
 * @author titou
 */
public class TransactionDtoManager {

    public static TransactionDto getDto(Transaction t) {
        if (t == null) {
            return null;
        }

        TransactionDto tdto = new TransactionDto();
        tdto.id=t.getId();
        tdto.addDate= t.getAddDate();
        tdto.bankTransNum=t.getBankTransNum();
        tdto.state=t.getState();
        tdto.totalPrice=t.getTotalPrice();
        tdto.user=t.getUser().getId();
        return tdto;
    }

/*
    public static Transaction makeTransaction(TransactionDto fdto) {
        Transaction t = new Transaction();
        
        return t;
    }

    public static Transaction mergeOrSave(TransactionDto fdto, EntityManager em) {

    }
*/
}
