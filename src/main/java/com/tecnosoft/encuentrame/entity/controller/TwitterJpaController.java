/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import com.tecnosoft.encuentrame.entity.Twitter;
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
public class TwitterJpaController implements Serializable {

    public TwitterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Twitter twitter) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsrComunidad usuario = twitter.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                twitter.setUsuario(usuario);
            }
            em.persist(twitter);
            if (usuario != null) {
                usuario.getTwitterCollection().add(twitter);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTwitter(twitter.getUsuarioTw()) != null) {
                throw new PreexistingEntityException("Twitter " + twitter + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Twitter twitter) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Twitter persistentTwitter = em.find(Twitter.class, twitter.getUsuarioTw());
            UsrComunidad usuarioOld = persistentTwitter.getUsuario();
            UsrComunidad usuarioNew = twitter.getUsuario();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                twitter.setUsuario(usuarioNew);
            }
            twitter = em.merge(twitter);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getTwitterCollection().remove(twitter);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getTwitterCollection().add(twitter);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = twitter.getUsuarioTw();
                if (findTwitter(id) == null) {
                    throw new NonexistentEntityException("The twitter with id " + id + " no longer exists.");
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
            Twitter twitter;
            try {
                twitter = em.getReference(Twitter.class, id);
                twitter.getUsuarioTw();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The twitter with id " + id + " no longer exists.", enfe);
            }
            UsrComunidad usuario = twitter.getUsuario();
            if (usuario != null) {
                usuario.getTwitterCollection().remove(twitter);
                usuario = em.merge(usuario);
            }
            em.remove(twitter);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Twitter> findTwitterEntities() {
        return findTwitterEntities(true, -1, -1);
    }

    public List<Twitter> findTwitterEntities(int maxResults, int firstResult) {
        return findTwitterEntities(false, maxResults, firstResult);
    }

    private List<Twitter> findTwitterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Twitter.class));
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

    public Twitter findTwitter(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Twitter.class, id);
        } finally {
            em.close();
        }
    }

    public int getTwitterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Twitter> rt = cq.from(Twitter.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
