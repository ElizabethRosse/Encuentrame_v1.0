package com.tecnosoft.encuentrame;

import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.UsrComunidadJpaController;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.event.FlowEvent;



 
@ManagedBean(name = "userWizard")
@ViewScoped
public class UserRegisterWizard implements Serializable {
 
    private User user = new User();
     
    private boolean skip;
     
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
     
    public void save() {      

        try {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("encuentrame");
            
            UsrComunidad userToSave = new UsrComunidad();
            userToSave.setUsuario(user.getUsername());
            userToSave.setCorreo(user.getEmail());
            userToSave.setPassword(user.getPassword());
            userToSave.setNombre(user.getNombre());
            userToSave.setApMaterno(user.getAp_materno());
            userToSave.setApPaterno(user.getAp_paterno());
            userToSave.setSexo(user.getGenero());
            userToSave.setFNacimiento(user.getFecha_nacimiento());
            userToSave.setEscuela(user.getEscuela());
            
            UsrComunidadJpaController usercontroll = new UsrComunidadJpaController(entityManagerFactory);
            usercontroll.create(userToSave);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Welcome " + user.getNombre() + " " + user.getAp_paterno()));
        } catch (Exception ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
     
    public boolean isSkip() {
        return skip;
    }
 
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
     
    public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
        
    }
}