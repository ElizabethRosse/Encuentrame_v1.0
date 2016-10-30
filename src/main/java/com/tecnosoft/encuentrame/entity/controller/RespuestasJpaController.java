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
import com.tecnosoft.encuentrame.entity.Encuesta;
import com.tecnosoft.encuentrame.entity.Respuestas;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class RespuestasJpaController implements Serializable {

    public RespuestasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Respuestas respuestas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta idEncuesta = respuestas.getIdEncuesta();
            if (idEncuesta != null) {
                idEncuesta = em.getReference(idEncuesta.getClass(), idEncuesta.getIdEncuesta());
                respuestas.setIdEncuesta(idEncuesta);
            }
            UsrComunidad usuario = respuestas.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                respuestas.setUsuario(usuario);
            }
            em.persist(respuestas);
            if (idEncuesta != null) {
                idEncuesta.getRespuestasCollection().add(respuestas);
                idEncuesta = em.merge(idEncuesta);
            }
            if (usuario != null) {
                usuario.getRespuestasCollection().add(respuestas);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Respuestas respuestas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Respuestas persistentRespuestas = em.find(Respuestas.class, respuestas.getIdRespuestas());
            Encuesta idEncuestaOld = persistentRespuestas.getIdEncuesta();
            Encuesta idEncuestaNew = respuestas.getIdEncuesta();
            UsrComunidad usuarioOld = persistentRespuestas.getUsuario();
            UsrComunidad usuarioNew = respuestas.getUsuario();
            if (idEncuestaNew != null) {
                idEncuestaNew = em.getReference(idEncuestaNew.getClass(), idEncuestaNew.getIdEncuesta());
                respuestas.setIdEncuesta(idEncuestaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                respuestas.setUsuario(usuarioNew);
            }
            respuestas = em.merge(respuestas);
            if (idEncuestaOld != null && !idEncuestaOld.equals(idEncuestaNew)) {
                idEncuestaOld.getRespuestasCollection().remove(respuestas);
                idEncuestaOld = em.merge(idEncuestaOld);
            }
            if (idEncuestaNew != null && !idEncuestaNew.equals(idEncuestaOld)) {
                idEncuestaNew.getRespuestasCollection().add(respuestas);
                idEncuestaNew = em.merge(idEncuestaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getRespuestasCollection().remove(respuestas);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getRespuestasCollection().add(respuestas);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = respuestas.getIdRespuestas();
                if (findRespuestas(id) == null) {
                    throw new NonexistentEntityException("The respuestas with id " + id + " no longer exists.");
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
            Respuestas respuestas;
            try {
                respuestas = em.getReference(Respuestas.class, id);
                respuestas.getIdRespuestas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuestas with id " + id + " no longer exists.", enfe);
            }
            Encuesta idEncuesta = respuestas.getIdEncuesta();
            if (idEncuesta != null) {
                idEncuesta.getRespuestasCollection().remove(respuestas);
                idEncuesta = em.merge(idEncuesta);
            }
            UsrComunidad usuario = respuestas.getUsuario();
            if (usuario != null) {
                usuario.getRespuestasCollection().remove(respuestas);
                usuario = em.merge(usuario);
            }
            em.remove(respuestas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Respuestas> findRespuestasEntities() {
        return findRespuestasEntities(true, -1, -1);
    }

    public List<Respuestas> findRespuestasEntities(int maxResults, int firstResult) {
        return findRespuestasEntities(false, maxResults, firstResult);
    }

    private List<Respuestas> findRespuestasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Respuestas.class));
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

    public Respuestas findRespuestas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Respuestas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Respuestas> rt = cq.from(Respuestas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
