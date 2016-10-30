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
import javax.annotation.PostConstruct;


 
@ManagedBean(name = "userModify")
@ViewScoped
public class UserModify implements Serializable {
 
    private User user = new User();
    private UsrComunidad userc;
     
    private boolean skip;

    @PostConstruct
    public void init() {
        try {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("encuentrame");
            UsrComunidadJpaController usercontroll = new UsrComunidadJpaController(entityManagerFactory);
            userc = usercontroll.findUsrComunidad("302128970"); //Aqui se debe de poner el campo de usuario de un usuario 
                                                                //existente para modificar sus datos
                                                                //Estos se debe cambiar una vez que esta implementada el manejo de session
                                                                //Se compra el user delusuario logeado
                                                                                   
            user.setAp_materno(userc.getApMaterno());
            user.setAp_paterno(userc.getApPaterno());
            user.setEmail(userc.getCorreo());
            user.setEscuela(userc.getEscuela());
            user.setFecha_nacimiento(userc.getFNacimiento());
            user.setGenero(userc.getSexo());
            user.setNombre(userc.getNombre());
            user.setUsername(userc.getUsuario());

        } catch (Exception ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        }        
        //user = service.createCars(100);
    }
    
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
            
            userc.setCorreo(user.getEmail());
            userc.setPassword(user.getPassword());
            
            UsrComunidadJpaController usercontroll = new UsrComunidadJpaController(entityManagerFactory);
            usercontroll.edit(userc);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Modificacion Exitosa"));
        } catch (Exception ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
     
 
}