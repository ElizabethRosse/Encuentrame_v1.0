/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import com.tecnosoft.encuentrame.entity.Facebook;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class FacebookJpaController implements Serializable {

    public FacebookJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facebook facebook) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrComunidad usuario = facebook.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                facebook.setUsuario(usuario);
            }
            em.persist(facebook);
            if (usuario != null) {
                usuario.getFacebookCollection().add(facebook);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFacebook(facebook.getUsuarioFb()) != null) {
                throw new PreexistingEntityException("Facebook " + facebook + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facebook facebook) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facebook persistentFacebook = em.find(Facebook.class, facebook.getUsuarioFb());
            UsrComunidad usuarioOld = persistentFacebook.getUsuario();
            UsrComunidad usuarioNew = facebook.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                facebook.setUsuario(usuarioNew);
            }
            facebook = em.merge(facebook);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getFacebookCollection().remove(facebook);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getFacebookCollection().add(facebook);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = facebook.getUsuarioFb();
                if (findFacebook(id) == null) {
                    throw new NonexistentEntityException("The facebook with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facebook facebook;
            try {
                facebook = em.getReference(Facebook.class, id);
                facebook.getUsuarioFb();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facebook with id " + id + " no longer exists.", enfe);
            }
            UsrComunidad usuario = facebook.getUsuario();
            if (usuario != null) {
                usuario.getFacebookCollection().remove(facebook);
                usuario = em.merge(usuario);
            }
            em.remove(facebook);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facebook> findFacebookEntities() {
        return findFacebookEntities(true, -1, -1);
    }

    public List<Facebook> findFacebookEntities(int maxResults, int firstResult) {
        return findFacebookEntities(false, maxResults, firstResult);
    }

    private List<Facebook> findFacebookEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facebook.class));
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

    public Facebook findFacebook(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facebook.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacebookCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facebook> rt = cq.from(Facebook.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
