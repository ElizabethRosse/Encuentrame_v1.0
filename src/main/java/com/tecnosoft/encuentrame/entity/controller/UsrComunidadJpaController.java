/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tecnosoft.encuentrame.entity.Administrador;
import java.util.ArrayList;
import java.util.Collection;
import com.tecnosoft.encuentrame.entity.Facebook;
import com.tecnosoft.encuentrame.entity.UsrCalificacion;
import com.tecnosoft.encuentrame.entity.Respuestas;
import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.entity.Twitter;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.exceptions.IllegalOrphanException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class UsrComunidadJpaController implements Serializable {

    public UsrComunidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsrComunidad usrComunidad) throws PreexistingEntityException, Exception {
        if (usrComunidad.getAdministradorCollection() == null) {
            usrComunidad.setAdministradorCollection(new ArrayList<Administrador>());
        }
        if (usrComunidad.getFacebookCollection() == null) {
            usrComunidad.setFacebookCollection(new ArrayList<Facebook>());
        }
        if (usrComunidad.getUsrCalificacionCollection() == null) {
            usrComunidad.setUsrCalificacionCollection(new ArrayList<UsrCalificacion>());
        }
        if (usrComunidad.getRespuestasCollection() == null) {
            usrComunidad.setRespuestasCollection(new ArrayList<Respuestas>());
        }
        if (usrComunidad.getObjetoCollection() == null) {
            usrComunidad.setObjetoCollection(new ArrayList<Objeto>());
        }
        if (usrComunidad.getTwitterCollection() == null) {
            usrComunidad.setTwitterCollection(new ArrayList<Twitter>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Administrador> attachedAdministradorCollection = new ArrayList<Administrador>();
            for (Administrador administradorCollectionAdministradorToAttach : usrComunidad.getAdministradorCollection()) {
                administradorCollectionAdministradorToAttach = em.getReference(administradorCollectionAdministradorToAttach.getClass(), administradorCollectionAdministradorToAttach.getIdAdministrador());
                attachedAdministradorCollection.add(administradorCollectionAdministradorToAttach);
            }
            usrComunidad.setAdministradorCollection(attachedAdministradorCollection);
            Collection<Facebook> attachedFacebookCollection = new ArrayList<Facebook>();
            for (Facebook facebookCollectionFacebookToAttach : usrComunidad.getFacebookCollection()) {
                facebookCollectionFacebookToAttach = em.getReference(facebookCollectionFacebookToAttach.getClass(), facebookCollectionFacebookToAttach.getUsuarioFb());
                attachedFacebookCollection.add(facebookCollectionFacebookToAttach);
            }
            usrComunidad.setFacebookCollection(attachedFacebookCollection);
            Collection<UsrCalificacion> attachedUsrCalificacionCollection = new ArrayList<UsrCalificacion>();
            for (UsrCalificacion usrCalificacionCollectionUsrCalificacionToAttach : usrComunidad.getUsrCalificacionCollection()) {
                usrCalificacionCollectionUsrCalificacionToAttach = em.getReference(usrCalificacionCollectionUsrCalificacionToAttach.getClass(), usrCalificacionCollectionUsrCalificacionToAttach.getIdCalificacion());
                attachedUsrCalificacionCollection.add(usrCalificacionCollectionUsrCalificacionToAttach);
            }
            usrComunidad.setUsrCalificacionCollection(attachedUsrCalificacionCollection);
            Collection<Respuestas> attachedRespuestasCollection = new ArrayList<Respuestas>();
            for (Respuestas respuestasCollectionRespuestasToAttach : usrComunidad.getRespuestasCollection()) {
                respuestasCollectionRespuestasToAttach = em.getReference(respuestasCollectionRespuestasToAttach.getClass(), respuestasCollectionRespuestasToAttach.getIdRespuestas());
                attachedRespuestasCollection.add(respuestasCollectionRespuestasToAttach);
            }
            usrComunidad.setRespuestasCollection(attachedRespuestasCollection);
            Collection<Objeto> attachedObjetoCollection = new ArrayList<Objeto>();
            for (Objeto objetoCollectionObjetoToAttach : usrComunidad.getObjetoCollection()) {
                objetoCollectionObjetoToAttach = em.getReference(objetoCollectionObjetoToAttach.getClass(), objetoCollectionObjetoToAttach.getIdObjeto());
                attachedObjetoCollection.add(objetoCollectionObjetoToAttach);
            }
            usrComunidad.setObjetoCollection(attachedObjetoCollection);
            Collection<Twitter> attachedTwitterCollection = new ArrayList<Twitter>();
            for (Twitter twitterCollectionTwitterToAttach : usrComunidad.getTwitterCollection()) {
                twitterCollectionTwitterToAttach = em.getReference(twitterCollectionTwitterToAttach.getClass(), twitterCollectionTwitterToAttach.getUsuarioTw());
                attachedTwitterCollection.add(twitterCollectionTwitterToAttach);
            }
            usrComunidad.setTwitterCollection(attachedTwitterCollection);
            em.persist(usrComunidad);
            for (Administrador administradorCollectionAdministrador : usrComunidad.getAdministradorCollection()) {
                UsrComunidad oldUsuarioOfAdministradorCollectionAdministrador = administradorCollectionAdministrador.getUsuario();
                administradorCollectionAdministrador.setUsuario(usrComunidad);
                administradorCollectionAdministrador = em.merge(administradorCollectionAdministrador);
                if (oldUsuarioOfAdministradorCollectionAdministrador != null) {
                    oldUsuarioOfAdministradorCollectionAdministrador.getAdministradorCollection().remove(administradorCollectionAdministrador);
                    oldUsuarioOfAdministradorCollectionAdministrador = em.merge(oldUsuarioOfAdministradorCollectionAdministrador);
                }
            }
            for (Facebook facebookCollectionFacebook : usrComunidad.getFacebookCollection()) {
                UsrComunidad oldUsuarioOfFacebookCollectionFacebook = facebookCollectionFacebook.getUsuario();
                facebookCollectionFacebook.setUsuario(usrComunidad);
                facebookCollectionFacebook = em.merge(facebookCollectionFacebook);
                if (oldUsuarioOfFacebookCollectionFacebook != null) {
                    oldUsuarioOfFacebookCollectionFacebook.getFacebookCollection().remove(facebookCollectionFacebook);
                    oldUsuarioOfFacebookCollectionFacebook = em.merge(oldUsuarioOfFacebookCollectionFacebook);
                }
            }
            for (UsrCalificacion usrCalificacionCollectionUsrCalificacion : usrComunidad.getUsrCalificacionCollection()) {
                UsrComunidad oldUsuarioOfUsrCalificacionCollectionUsrCalificacion = usrCalificacionCollectionUsrCalificacion.getUsuario();
                usrCalificacionCollectionUsrCalificacion.setUsuario(usrComunidad);
                usrCalificacionCollectionUsrCalificacion = em.merge(usrCalificacionCollectionUsrCalificacion);
                if (oldUsuarioOfUsrCalificacionCollectionUsrCalificacion != null) {
                    oldUsuarioOfUsrCalificacionCollectionUsrCalificacion.getUsrCalificacionCollection().remove(usrCalificacionCollectionUsrCalificacion);
                    oldUsuarioOfUsrCalificacionCollectionUsrCalificacion = em.merge(oldUsuarioOfUsrCalificacionCollectionUsrCalificacion);
                }
            }
            for (Respuestas respuestasCollectionRespuestas : usrComunidad.getRespuestasCollection()) {
                UsrComunidad oldUsuarioOfRespuestasCollectionRespuestas = respuestasCollectionRespuestas.getUsuario();
                respuestasCollectionRespuestas.setUsuario(usrComunidad);
                respuestasCollectionRespuestas = em.merge(respuestasCollectionRespuestas);
                if (oldUsuarioOfRespuestasCollectionRespuestas != null) {
                    oldUsuarioOfRespuestasCollectionRespuestas.getRespuestasCollection().remove(respuestasCollectionRespuestas);
                    oldUsuarioOfRespuestasCollectionRespuestas = em.merge(oldUsuarioOfRespuestasCollectionRespuestas);
                }
            }
            for (Objeto objetoCollectionObjeto : usrComunidad.getObjetoCollection()) {
                UsrComunidad oldUsuarioOfObjetoCollectionObjeto = objetoCollectionObjeto.getUsuario();
                objetoCollectionObjeto.setUsuario(usrComunidad);
                objetoCollectionObjeto = em.merge(objetoCollectionObjeto);
                if (oldUsuarioOfObjetoCollectionObjeto != null) {
                    oldUsuarioOfObjetoCollectionObjeto.getObjetoCollection().remove(objetoCollectionObjeto);
                    oldUsuarioOfObjetoCollectionObjeto = em.merge(oldUsuarioOfObjetoCollectionObjeto);
                }
            }
            for (Twitter twitterCollectionTwitter : usrComunidad.getTwitterCollection()) {
                UsrComunidad oldUsuarioOfTwitterCollectionTwitter = twitterCollectionTwitter.getUsuario();
                twitterCollectionTwitter.setUsuario(usrComunidad);
                twitterCollectionTwitter = em.merge(twitterCollectionTwitter);
                if (oldUsuarioOfTwitterCollectionTwitter != null) {
                    oldUsuarioOfTwitterCollectionTwitter.getTwitterCollection().remove(twitterCollectionTwitter);
                    oldUsuarioOfTwitterCollectionTwitter = em.merge(oldUsuarioOfTwitterCollectionTwitter);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsrComunidad(usrComunidad.getUsuario()) != null) {
                throw new PreexistingEntityException("UsrComunidad " + usrComunidad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsrComunidad usrComunidad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrComunidad persistentUsrComunidad = em.find(UsrComunidad.class, usrComunidad.getUsuario());
            Collection<Administrador> administradorCollectionOld = persistentUsrComunidad.getAdministradorCollection();
            Collection<Administrador> administradorCollectionNew = usrComunidad.getAdministradorCollection();
            Collection<Facebook> facebookCollectionOld = persistentUsrComunidad.getFacebookCollection();
            Collection<Facebook> facebookCollectionNew = usrComunidad.getFacebookCollection();
            Collection<UsrCalificacion> usrCalificacionCollectionOld = persistentUsrComunidad.getUsrCalificacionCollection();
            Collection<UsrCalificacion> usrCalificacionCollectionNew = usrComunidad.getUsrCalificacionCollection();
            Collection<Respuestas> respuestasCollectionOld = persistentUsrComunidad.getRespuestasCollection();
            Collection<Respuestas> respuestasCollectionNew = usrComunidad.getRespuestasCollection();
            Collection<Objeto> objetoCollectionOld = persistentUsrComunidad.getObjetoCollection();
            Collection<Objeto> objetoCollectionNew = usrComunidad.getObjetoCollection();
            Collection<Twitter> twitterCollectionOld = persistentUsrComunidad.getTwitterCollection();
            Collection<Twitter> twitterCollectionNew = usrComunidad.getTwitterCollection();
            List<String> illegalOrphanMessages = null;
            for (Administrador administradorCollectionOldAdministrador : administradorCollectionOld) {
                if (!administradorCollectionNew.contains(administradorCollectionOldAdministrador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Administrador " + administradorCollectionOldAdministrador + " since its usuario field is not nullable.");
                }
            }
            for (Facebook facebookCollectionOldFacebook : facebookCollectionOld) {
                if (!facebookCollectionNew.contains(facebookCollectionOldFacebook)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Facebook " + facebookCollectionOldFacebook + " since its usuario field is not nullable.");
                }
            }
            for (UsrCalificacion usrCalificacionCollectionOldUsrCalificacion : usrCalificacionCollectionOld) {
                if (!usrCalificacionCollectionNew.contains(usrCalificacionCollectionOldUsrCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsrCalificacion " + usrCalificacionCollectionOldUsrCalificacion + " since its usuario field is not nullable.");
                }
            }
            for (Respuestas respuestasCollectionOldRespuestas : respuestasCollectionOld) {
                if (!respuestasCollectionNew.contains(respuestasCollectionOldRespuestas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Respuestas " + respuestasCollectionOldRespuestas + " since its usuario field is not nullable.");
                }
            }
            for (Objeto objetoCollectionOldObjeto : objetoCollectionOld) {
                if (!objetoCollectionNew.contains(objetoCollectionOldObjeto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Objeto " + objetoCollectionOldObjeto + " since its usuario field is not nullable.");
                }
            }
            for (Twitter twitterCollectionOldTwitter : twitterCollectionOld) {
                if (!twitterCollectionNew.contains(twitterCollectionOldTwitter)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Twitter " + twitterCollectionOldTwitter + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Administrador> attachedAdministradorCollectionNew = new ArrayList<Administrador>();
            for (Administrador administradorCollectionNewAdministradorToAttach : administradorCollectionNew) {
                administradorCollectionNewAdministradorToAttach = em.getReference(administradorCollectionNewAdministradorToAttach.getClass(), administradorCollectionNewAdministradorToAttach.getIdAdministrador());
                attachedAdministradorCollectionNew.add(administradorCollectionNewAdministradorToAttach);
            }
            administradorCollectionNew = attachedAdministradorCollectionNew;
            usrComunidad.setAdministradorCollection(administradorCollectionNew);
            Collection<Facebook> attachedFacebookCollectionNew = new ArrayList<Facebook>();
            for (Facebook facebookCollectionNewFacebookToAttach : facebookCollectionNew) {
                facebookCollectionNewFacebookToAttach = em.getReference(facebookCollectionNewFacebookToAttach.getClass(), facebookCollectionNewFacebookToAttach.getUsuarioFb());
                attachedFacebookCollectionNew.add(facebookCollectionNewFacebookToAttach);
            }
            facebookCollectionNew = attachedFacebookCollectionNew;
            usrComunidad.setFacebookCollection(facebookCollectionNew);
            Collection<UsrCalificacion> attachedUsrCalificacionCollectionNew = new ArrayList<UsrCalificacion>();
            for (UsrCalificacion usrCalificacionCollectionNewUsrCalificacionToAttach : usrCalificacionCollectionNew) {
                usrCalificacionCollectionNewUsrCalificacionToAttach = em.getReference(usrCalificacionCollectionNewUsrCalificacionToAttach.getClass(), usrCalificacionCollectionNewUsrCalificacionToAttach.getIdCalificacion());
                attachedUsrCalificacionCollectionNew.add(usrCalificacionCollectionNewUsrCalificacionToAttach);
            }
            usrCalificacionCollectionNew = attachedUsrCalificacionCollectionNew;
            usrComunidad.setUsrCalificacionCollection(usrCalificacionCollectionNew);
            Collection<Respuestas> attachedRespuestasCollectionNew = new ArrayList<Respuestas>();
            for (Respuestas respuestasCollectionNewRespuestasToAttach : respuestasCollectionNew) {
                respuestasCollectionNewRespuestasToAttach = em.getReference(respuestasCollectionNewRespuestasToAttach.getClass(), respuestasCollectionNewRespuestasToAttach.getIdRespuestas());
                attachedRespuestasCollectionNew.add(respuestasCollectionNewRespuestasToAttach);
            }
            respuestasCollectionNew = attachedRespuestasCollectionNew;
            usrComunidad.setRespuestasCollection(respuestasCollectionNew);
            Collection<Objeto> attachedObjetoCollectionNew = new ArrayList<Objeto>();
            for (Objeto objetoCollectionNewObjetoToAttach : objetoCollectionNew) {
                objetoCollectionNewObjetoToAttach = em.getReference(objetoCollectionNewObjetoToAttach.getClass(), objetoCollectionNewObjetoToAttach.getIdObjeto());
                attachedObjetoCollectionNew.add(objetoCollectionNewObjetoToAttach);
            }
            objetoCollectionNew = attachedObjetoCollectionNew;
            usrComunidad.setObjetoCollection(objetoCollectionNew);
            Collection<Twitter> attachedTwitterCollectionNew = new ArrayList<Twitter>();
            for (Twitter twitterCollectionNewTwitterToAttach : twitterCollectionNew) {
                twitterCollectionNewTwitterToAttach = em.getReference(twitterCollectionNewTwitterToAttach.getClass(), twitterCollectionNewTwitterToAttach.getUsuarioTw());
                attachedTwitterCollectionNew.add(twitterCollectionNewTwitterToAttach);
            }
            twitterCollectionNew = attachedTwitterCollectionNew;
            usrComunidad.setTwitterCollection(twitterCollectionNew);
            usrComunidad = em.merge(usrComunidad);
            for (Administrador administradorCollectionNewAdministrador : administradorCollectionNew) {
                if (!administradorCollectionOld.contains(administradorCollectionNewAdministrador)) {
                    UsrComunidad oldUsuarioOfAdministradorCollectionNewAdministrador = administradorCollectionNewAdministrador.getUsuario();
                    administradorCollectionNewAdministrador.setUsuario(usrComunidad);
                    administradorCollectionNewAdministrador = em.merge(administradorCollectionNewAdministrador);
                    if (oldUsuarioOfAdministradorCollectionNewAdministrador != null && !oldUsuarioOfAdministradorCollectionNewAdministrador.equals(usrComunidad)) {
                        oldUsuarioOfAdministradorCollectionNewAdministrador.getAdministradorCollection().remove(administradorCollectionNewAdministrador);
                        oldUsuarioOfAdministradorCollectionNewAdministrador = em.merge(oldUsuarioOfAdministradorCollectionNewAdministrador);
                    }
                }
            }
            for (Facebook facebookCollectionNewFacebook : facebookCollectionNew) {
                if (!facebookCollectionOld.contains(facebookCollectionNewFacebook)) {
                    UsrComunidad oldUsuarioOfFacebookCollectionNewFacebook = facebookCollectionNewFacebook.getUsuario();
                    facebookCollectionNewFacebook.setUsuario(usrComunidad);
                    facebookCollectionNewFacebook = em.merge(facebookCollectionNewFacebook);
                    if (oldUsuarioOfFacebookCollectionNewFacebook != null && !oldUsuarioOfFacebookCollectionNewFacebook.equals(usrComunidad)) {
                        oldUsuarioOfFacebookCollectionNewFacebook.getFacebookCollection().remove(facebookCollectionNewFacebook);
                        oldUsuarioOfFacebookCollectionNewFacebook = em.merge(oldUsuarioOfFacebookCollectionNewFacebook);
                    }
                }
            }
            for (UsrCalificacion usrCalificacionCollectionNewUsrCalificacion : usrCalificacionCollectionNew) {
                if (!usrCalificacionCollectionOld.contains(usrCalificacionCollectionNewUsrCalificacion)) {
                    UsrComunidad oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion = usrCalificacionCollectionNewUsrCalificacion.getUsuario();
                    usrCalificacionCollectionNewUsrCalificacion.setUsuario(usrComunidad);
                    usrCalificacionCollectionNewUsrCalificacion = em.merge(usrCalificacionCollectionNewUsrCalificacion);
                    if (oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion != null && !oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion.equals(usrComunidad)) {
                        oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion.getUsrCalificacionCollection().remove(usrCalificacionCollectionNewUsrCalificacion);
                        oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion = em.merge(oldUsuarioOfUsrCalificacionCollectionNewUsrCalificacion);
                    }
                }
            }
            for (Respuestas respuestasCollectionNewRespuestas : respuestasCollectionNew) {
                if (!respuestasCollectionOld.contains(respuestasCollectionNewRespuestas)) {
                    UsrComunidad oldUsuarioOfRespuestasCollectionNewRespuestas = respuestasCollectionNewRespuestas.getUsuario();
                    respuestasCollectionNewRespuestas.setUsuario(usrComunidad);
                    respuestasCollectionNewRespuestas = em.merge(respuestasCollectionNewRespuestas);
                    if (oldUsuarioOfRespuestasCollectionNewRespuestas != null && !oldUsuarioOfRespuestasCollectionNewRespuestas.equals(usrComunidad)) {
                        oldUsuarioOfRespuestasCollectionNewRespuestas.getRespuestasCollection().remove(respuestasCollectionNewRespuestas);
                        oldUsuarioOfRespuestasCollectionNewRespuestas = em.merge(oldUsuarioOfRespuestasCollectionNewRespuestas);
                    }
                }
            }
            for (Objeto objetoCollectionNewObjeto : objetoCollectionNew) {
                if (!objetoCollectionOld.contains(objetoCollectionNewObjeto)) {
                    UsrComunidad oldUsuarioOfObjetoCollectionNewObjeto = objetoCollectionNewObjeto.getUsuario();
                    objetoCollectionNewObjeto.setUsuario(usrComunidad);
                    objetoCollectionNewObjeto = em.merge(objetoCollectionNewObjeto);
                    if (oldUsuarioOfObjetoCollectionNewObjeto != null && !oldUsuarioOfObjetoCollectionNewObjeto.equals(usrComunidad)) {
                        oldUsuarioOfObjetoCollectionNewObjeto.getObjetoCollection().remove(objetoCollectionNewObjeto);
                        oldUsuarioOfObjetoCollectionNewObjeto = em.merge(oldUsuarioOfObjetoCollectionNewObjeto);
                    }
                }
            }
            for (Twitter twitterCollectionNewTwitter : twitterCollectionNew) {
                if (!twitterCollectionOld.contains(twitterCollectionNewTwitter)) {
                    UsrComunidad oldUsuarioOfTwitterCollectionNewTwitter = twitterCollectionNewTwitter.getUsuario();
                    twitterCollectionNewTwitter.setUsuario(usrComunidad);
                    twitterCollectionNewTwitter = em.merge(twitterCollectionNewTwitter);
                    if (oldUsuarioOfTwitterCollectionNewTwitter != null && !oldUsuarioOfTwitterCollectionNewTwitter.equals(usrComunidad)) {
                        oldUsuarioOfTwitterCollectionNewTwitter.getTwitterCollection().remove(twitterCollectionNewTwitter);
                        oldUsuarioOfTwitterCollectionNewTwitter = em.merge(oldUsuarioOfTwitterCollectionNewTwitter);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usrComunidad.getUsuario();
                if (findUsrComunidad(id) == null) {
                    throw new NonexistentEntityException("The usrComunidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrComunidad usrComunidad;
            try {
                usrComunidad = em.getReference(UsrComunidad.class, id);
                usrComunidad.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usrComunidad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Administrador> administradorCollectionOrphanCheck = usrComunidad.getAdministradorCollection();
            for (Administrador administradorCollectionOrphanCheckAdministrador : administradorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the Administrador " + administradorCollectionOrphanCheckAdministrador + " in its administradorCollection field has a non-nullable usuario field.");
            }
            Collection<Facebook> facebookCollectionOrphanCheck = usrComunidad.getFacebookCollection();
            for (Facebook facebookCollectionOrphanCheckFacebook : facebookCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the Facebook " + facebookCollectionOrphanCheckFacebook + " in its facebookCollection field has a non-nullable usuario field.");
            }
            Collection<UsrCalificacion> usrCalificacionCollectionOrphanCheck = usrComunidad.getUsrCalificacionCollection();
            for (UsrCalificacion usrCalificacionCollectionOrphanCheckUsrCalificacion : usrCalificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the UsrCalificacion " + usrCalificacionCollectionOrphanCheckUsrCalificacion + " in its usrCalificacionCollection field has a non-nullable usuario field.");
            }
            Collection<Respuestas> respuestasCollectionOrphanCheck = usrComunidad.getRespuestasCollection();
            for (Respuestas respuestasCollectionOrphanCheckRespuestas : respuestasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the Respuestas " + respuestasCollectionOrphanCheckRespuestas + " in its respuestasCollection field has a non-nullable usuario field.");
            }
            Collection<Objeto> objetoCollectionOrphanCheck = usrComunidad.getObjetoCollection();
            for (Objeto objetoCollectionOrphanCheckObjeto : objetoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the Objeto " + objetoCollectionOrphanCheckObjeto + " in its objetoCollection field has a non-nullable usuario field.");
            }
            Collection<Twitter> twitterCollectionOrphanCheck = usrComunidad.getTwitterCollection();
            for (Twitter twitterCollectionOrphanCheckTwitter : twitterCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UsrComunidad (" + usrComunidad + ") cannot be destroyed since the Twitter " + twitterCollectionOrphanCheckTwitter + " in its twitterCollection field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usrComunidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsrComunidad> findUsrComunidadEntities() {
        return findUsrComunidadEntities(true, -1, -1);
    }

    public List<UsrComunidad> findUsrComunidadEntities(int maxResults, int firstResult) {
        return findUsrComunidadEntities(false, maxResults, firstResult);
    }

    private List<UsrComunidad> findUsrComunidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsrComunidad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UsrComunidad findUsrComunidad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsrComunidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsrComunidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsrComunidad> rt = cq.from(UsrComunidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
