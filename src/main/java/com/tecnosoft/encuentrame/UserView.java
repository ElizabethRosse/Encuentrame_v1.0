/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame;

/**
 *
 * @author roy
 */
import com.tecnosoft.encuentrame.entity.Categoria;
import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.CategoriaJpaController;
import com.tecnosoft.encuentrame.entity.controller.UsrComunidadJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.springframework.stereotype.Controller;
 
@Controller("userView")
public class UserView {
     
    private String firstname;
    private String lastname;
    
    private List<Objeto> ObjetoList;
 
    
    
    public String getFirstname() {
        return firstname;
    }
 
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
 
    public String getLastname() {
        return lastname;
    }
 
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
 
    public void save() {
        
    }
}
