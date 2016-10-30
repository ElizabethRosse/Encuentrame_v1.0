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
import com.tecnosoft.encuentrame.entity.Cita;
import com.tecnosoft.encuentrame.entity.UsrCalificacion;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class UsrCalificacionJpaController implements Serializable {

    public UsrCalificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsrCalificacion usrCalificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cita idCita = usrCalificacion.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                usrCalificacion.setIdCita(idCita);
            }
            UsrComunidad usuario = usrCalificacion.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                usrCalificacion.setUsuario(usuario);
            }
            em.persist(usrCalificacion);
            if (idCita != null) {
                idCita.getUsrCalificacionCollection().add(usrCalificacion);
                idCita = em.merge(idCita);
            }
            if (usuario != null) {
                usuario.getUsrCalificacionCollection().add(usrCalificacion);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsrCalificacion usrCalificacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrCalificacion persistentUsrCalificacion = em.find(UsrCalificacion.class, usrCalificacion.getIdCalificacion());
            Cita idCitaOld = persistentUsrCalificacion.getIdCita();
            Cita idCitaNew = usrCalificacion.getIdCita();
            UsrComunidad usuarioOld = persistentUsrCalificacion.getUsuario();
            UsrComunidad usuarioNew = usrCalificacion.getUsuario();
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                usrCalificacion.setIdCita(idCitaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                usrCalificacion.setUsuario(usuarioNew);
            }
            usrCalificacion = em.merge(usrCalificacion);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getUsrCalificacionCollection().remove(usrCalificacion);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getUsrCalificacionCollection().add(usrCalificacion);
                idCitaNew = em.merge(idCitaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getUsrCalificacionCollection().remove(usrCalificacion);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getUsrCalificacionCollection().add(usrCalificacion);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usrCalificacion.getIdCalificacion();
                if (findUsrCalificacion(id) == null) {
                    throw new NonexistentEntityException("The usrCalificacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrCalificacion usrCalificacion;
            try {
                usrCalificacion = em.getReference(UsrCalificacion.class, id);
                usrCalificacion.getIdCalificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usrCalificacion with id " + id + " no longer exists.", enfe);
            }
            Cita idCita = usrCalificacion.getIdCita();
            if (idCita != null) {
                idCita.getUsrCalificacionCollection().remove(usrCalificacion);
                idCita = em.merge(idCita);
            }
            UsrComunidad usuario = usrCalificacion.getUsuario();
            if (usuario != null) {
                usuario.getUsrCalificacionCollection().remove(usrCalificacion);
                usuario = em.merge(usuario);
            }
            em.remove(usrCalificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsrCalificacion> findUsrCalificacionEntities() {
        return findUsrCalificacionEntities(true, -1, -1);
    }

    public List<UsrCalificacion> findUsrCalificacionEntities(int maxResults, int firstResult) {
        return findUsrCalificacionEntities(false, maxResults, firstResult);
    }

    private List<UsrCalificacion> findUsrCalificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsrCalificacion.class));
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

    public UsrCalificacion findUsrCalificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsrCalificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsrCalificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsrCalificacion> rt = cq.from(UsrCalificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
