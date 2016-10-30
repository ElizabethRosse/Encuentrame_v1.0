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
import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.entity.Encuesta;
import com.tecnosoft.encuentrame.entity.Post;
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
public class PostJpaController implements Serializable {

    public PostJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Post post) {
        if (post.getEncuestaCollection() == null) {
            post.setEncuestaCollection(new ArrayList<Encuesta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objeto idObjeto = post.getIdObjeto();
            if (idObjeto != null) {
                idObjeto = em.getReference(idObjeto.getClass(), idObjeto.getIdObjeto());
                post.setIdObjeto(idObjeto);
            }
            Collection<Encuesta> attachedEncuestaCollection = new ArrayList<Encuesta>();
            for (Encuesta encuestaCollectionEncuestaToAttach : post.getEncuestaCollection()) {
                encuestaCollectionEncuestaToAttach = em.getReference(encuestaCollectionEncuestaToAttach.getClass(), encuestaCollectionEncuestaToAttach.getIdEncuesta());
                attachedEncuestaCollection.add(encuestaCollectionEncuestaToAttach);
            }
            post.setEncuestaCollection(attachedEncuestaCollection);
            em.persist(post);
            if (idObjeto != null) {
                idObjeto.getPostCollection().add(post);
                idObjeto = em.merge(idObjeto);
            }
            for (Encuesta encuestaCollectionEncuesta : post.getEncuestaCollection()) {
                Post oldIdPostOfEncuestaCollectionEncuesta = encuestaCollectionEncuesta.getIdPost();
                encuestaCollectionEncuesta.setIdPost(post);
                encuestaCollectionEncuesta = em.merge(encuestaCollectionEncuesta);
                if (oldIdPostOfEncuestaCollectionEncuesta != null) {
                    oldIdPostOfEncuestaCollectionEncuesta.getEncuestaCollection().remove(encuestaCollectionEncuesta);
                    oldIdPostOfEncuestaCollectionEncuesta = em.merge(oldIdPostOfEncuestaCollectionEncuesta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Post post) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Post persistentPost = em.find(Post.class, post.getIdPost());
            Objeto idObjetoOld = persistentPost.getIdObjeto();
            Objeto idObjetoNew = post.getIdObjeto();
            Collection<Encuesta> encuestaCollectionOld = persistentPost.getEncuestaCollection();
            Collection<Encuesta> encuestaCollectionNew = post.getEncuestaCollection();
            List<String> illegalOrphanMessages = null;
            for (Encuesta encuestaCollectionOldEncuesta : encuestaCollectionOld) {
                if (!encuestaCollectionNew.contains(encuestaCollectionOldEncuesta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encuesta " + encuestaCollectionOldEncuesta + " since its idPost field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idObjetoNew != null) {
                idObjetoNew = em.getReference(idObjetoNew.getClass(), idObjetoNew.getIdObjeto());
                post.setIdObjeto(idObjetoNew);
            }
            Collection<Encuesta> attachedEncuestaCollectionNew = new ArrayList<Encuesta>();
            for (Encuesta encuestaCollectionNewEncuestaToAttach : encuestaCollectionNew) {
                encuestaCollectionNewEncuestaToAttach = em.getReference(encuestaCollectionNewEncuestaToAttach.getClass(), encuestaCollectionNewEncuestaToAttach.getIdEncuesta());
                attachedEncuestaCollectionNew.add(encuestaCollectionNewEncuestaToAttach);
            }
            encuestaCollectionNew = attachedEncuestaCollectionNew;
            post.setEncuestaCollection(encuestaCollectionNew);
            post = em.merge(post);
            if (idObjetoOld != null && !idObjetoOld.equals(idObjetoNew)) {
                idObjetoOld.getPostCollection().remove(post);
                idObjetoOld = em.merge(idObjetoOld);
            }
            if (idObjetoNew != null && !idObjetoNew.equals(idObjetoOld)) {
                idObjetoNew.getPostCollection().add(post);
                idObjetoNew = em.merge(idObjetoNew);
            }
            for (Encuesta encuestaCollectionNewEncuesta : encuestaCollectionNew) {
                if (!encuestaCollectionOld.contains(encuestaCollectionNewEncuesta)) {
                    Post oldIdPostOfEncuestaCollectionNewEncuesta = encuestaCollectionNewEncuesta.getIdPost();
                    encuestaCollectionNewEncuesta.setIdPost(post);
                    encuestaCollectionNewEncuesta = em.merge(encuestaCollectionNewEncuesta);
                    if (oldIdPostOfEncuestaCollectionNewEncuesta != null && !oldIdPostOfEncuestaCollectionNewEncuesta.equals(post)) {
                        oldIdPostOfEncuestaCollectionNewEncuesta.getEncuestaCollection().remove(encuestaCollectionNewEncuesta);
                        oldIdPostOfEncuestaCollectionNewEncuesta = em.merge(oldIdPostOfEncuestaCollectionNewEncuesta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = post.getIdPost();
                if (findPost(id) == null) {
                    throw new NonexistentEntityException("The post with id " + id + " no longer exists.");
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
            Post post;
            try {
                post = em.getReference(Post.class, id);
                post.getIdPost();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The post with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Encuesta> encuestaCollectionOrphanCheck = post.getEncuestaCollection();
            for (Encuesta encuestaCollectionOrphanCheckEncuesta : encuestaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Post (" + post + ") cannot be destroyed since the Encuesta " + encuestaCollectionOrphanCheckEncuesta + " in its encuestaCollection field has a non-nullable idPost field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Objeto idObjeto = post.getIdObjeto();
            if (idObjeto != null) {
                idObjeto.getPostCollection().remove(post);
                idObjeto = em.merge(idObjeto);
            }
            em.remove(post);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Post> findPostEntities() {
        return findPostEntities(true, -1, -1);
    }

    public List<Post> findPostEntities(int maxResults, int firstResult) {
        return findPostEntities(false, maxResults, firstResult);
    }

    private List<Post> findPostEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Post.class));
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

    public Post findPost(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Post.class, id);
        } finally {
            em.close();
        }
    }

    public int getPostCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Post> rt = cq.from(Post.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
