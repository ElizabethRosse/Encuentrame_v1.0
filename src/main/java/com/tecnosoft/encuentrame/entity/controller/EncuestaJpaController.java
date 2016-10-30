/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import com.tecnosoft.encuentrame.entity.Encuesta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tecnosoft.encuentrame.entity.Post;
import com.tecnosoft.encuentrame.entity.Respuestas;
import com.tecnosoft.encuentrame.entity.controller.exceptions.IllegalOrphanException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class EncuestaJpaController implements Serializable {

    public EncuestaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encuesta encuesta) {
        if (encuesta.getRespuestasCollection() == null) {
            encuesta.setRespuestasCollection(new ArrayList<Respuestas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Post idPost = encuesta.getIdPost();
            if (idPost != null) {
                idPost = em.getReference(idPost.getClass(), idPost.getIdPost());
                encuesta.setIdPost(idPost);
            }
            Collection<Respuestas> attachedRespuestasCollection = new ArrayList<Respuestas>();
            for (Respuestas respuestasCollectionRespuestasToAttach : encuesta.getRespuestasCollection()) {
                respuestasCollectionRespuestasToAttach = em.getReference(respuestasCollectionRespuestasToAttach.getClass(), respuestasCollectionRespuestasToAttach.getIdRespuestas());
                attachedRespuestasCollection.add(respuestasCollectionRespuestasToAttach);
            }
            encuesta.setRespuestasCollection(attachedRespuestasCollection);
            em.persist(encuesta);
            if (idPost != null) {
                idPost.getEncuestaCollection().add(encuesta);
                idPost = em.merge(idPost);
            }
            for (Respuestas respuestasCollectionRespuestas : encuesta.getRespuestasCollection()) {
                Encuesta oldIdEncuestaOfRespuestasCollectionRespuestas = respuestasCollectionRespuestas.getIdEncuesta();
                respuestasCollectionRespuestas.setIdEncuesta(encuesta);
                respuestasCollectionRespuestas = em.merge(respuestasCollectionRespuestas);
                if (oldIdEncuestaOfRespuestasCollectionRespuestas != null) {
                    oldIdEncuestaOfRespuestasCollectionRespuestas.getRespuestasCollection().remove(respuestasCollectionRespuestas);
                    oldIdEncuestaOfRespuestasCollectionRespuestas = em.merge(oldIdEncuestaOfRespuestasCollectionRespuestas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encuesta encuesta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta persistentEncuesta = em.find(Encuesta.class, encuesta.getIdEncuesta());
            Post idPostOld = persistentEncuesta.getIdPost();
            Post idPostNew = encuesta.getIdPost();
            Collection<Respuestas> respuestasCollectionOld = persistentEncuesta.getRespuestasCollection();
            Collection<Respuestas> respuestasCollectionNew = encuesta.getRespuestasCollection();
            List<String> illegalOrphanMessages = null;
            for (Respuestas respuestasCollectionOldRespuestas : respuestasCollectionOld) {
                if (!respuestasCollectionNew.contains(respuestasCollectionOldRespuestas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Respuestas " + respuestasCollectionOldRespuestas + " since its idEncuesta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPostNew != null) {
                idPostNew = em.getReference(idPostNew.getClass(), idPostNew.getIdPost());
                encuesta.setIdPost(idPostNew);
            }
            Collection<Respuestas> attachedRespuestasCollectionNew = new ArrayList<Respuestas>();
            for (Respuestas respuestasCollectionNewRespuestasToAttach : respuestasCollectionNew) {
                respuestasCollectionNewRespuestasToAttach = em.getReference(respuestasCollectionNewRespuestasToAttach.getClass(), respuestasCollectionNewRespuestasToAttach.getIdRespuestas());
                attachedRespuestasCollectionNew.add(respuestasCollectionNewRespuestasToAttach);
            }
            respuestasCollectionNew = attachedRespuestasCollectionNew;
            encuesta.setRespuestasCollection(respuestasCollectionNew);
            encuesta = em.merge(encuesta);
            if (idPostOld != null && !idPostOld.equals(idPostNew)) {
                idPostOld.getEncuestaCollection().remove(encuesta);
                idPostOld = em.merge(idPostOld);
            }
            if (idPostNew != null && !idPostNew.equals(idPostOld)) {
                idPostNew.getEncuestaCollection().add(encuesta);
                idPostNew = em.merge(idPostNew);
            }
            for (Respuestas respuestasCollectionNewRespuestas : respuestasCollectionNew) {
                if (!respuestasCollectionOld.contains(respuestasCollectionNewRespuestas)) {
                    Encuesta oldIdEncuestaOfRespuestasCollectionNewRespuestas = respuestasCollectionNewRespuestas.getIdEncuesta();
                    respuestasCollectionNewRespuestas.setIdEncuesta(encuesta);
                    respuestasCollectionNewRespuestas = em.merge(respuestasCollectionNewRespuestas);
                    if (oldIdEncuestaOfRespuestasCollectionNewRespuestas != null && !oldIdEncuestaOfRespuestasCollectionNewRespuestas.equals(encuesta)) {
                        oldIdEncuestaOfRespuestasCollectionNewRespuestas.getRespuestasCollection().remove(respuestasCollectionNewRespuestas);
                        oldIdEncuestaOfRespuestasCollectionNewRespuestas = em.merge(oldIdEncuestaOfRespuestasCollectionNewRespuestas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encuesta.getIdEncuesta();
                if (findEncuesta(id) == null) {
                    throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encuesta encuesta;
            try {
                encuesta = em.getReference(Encuesta.class, id);
                encuesta.getIdEncuesta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encuesta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Respuestas> respuestasCollectionOrphanCheck = encuesta.getRespuestasCollection();
            for (Respuestas respuestasCollectionOrphanCheckRespuestas : respuestasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encuesta (" + encuesta + ") cannot be destroyed since the Respuestas " + respuestasCollectionOrphanCheckRespuestas + " in its respuestasCollection field has a non-nullable idEncuesta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Post idPost = encuesta.getIdPost();
            if (idPost != null) {
                idPost.getEncuestaCollection().remove(encuesta);
                idPost = em.merge(idPost);
            }
            em.remove(encuesta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encuesta> findEncuestaEntities() {
        return findEncuestaEntities(true, -1, -1);
    }

    public List<Encuesta> findEncuestaEntities(int maxResults, int firstResult) {
        return findEncuestaEntities(false, maxResults, firstResult);
    }

    private List<Encuesta> findEncuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encuesta.class));
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

    public Encuesta findEncuesta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encuesta> rt = cq.from(Encuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
