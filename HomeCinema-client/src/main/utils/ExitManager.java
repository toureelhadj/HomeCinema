/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

/**
 *
 * @author seb
 */
public class ExitManager extends ModuleManager {


  
  public ExitManager() {
    super("exit");
  }

  
  @Override
  public void exec() throws ReturnManager.ReturnException { 
    throw new ReturnManager.ReturnException();
  }
}
