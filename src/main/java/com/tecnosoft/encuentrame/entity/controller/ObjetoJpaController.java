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
import com.tecnosoft.encuentrame.entity.Categoria;
import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.entity.UsrComunidad;
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
public class ObjetoJpaController implements Serializable {

    public ObjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Objeto objeto) {
        if (objeto.getPostCollection() == null) {
            objeto.setPostCollection(new ArrayList<Post>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = objeto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                objeto.setIdCategoria(idCategoria);
            }
            UsrComunidad usuario = objeto.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUsuario());
                objeto.setUsuario(usuario);
            }
            Collection<Post> attachedPostCollection = new ArrayList<Post>();
            for (Post postCollectionPostToAttach : objeto.getPostCollection()) {
                postCollectionPostToAttach = em.getReference(postCollectionPostToAttach.getClass(), postCollectionPostToAttach.getIdPost());
                attachedPostCollection.add(postCollectionPostToAttach);
            }
            objeto.setPostCollection(attachedPostCollection);
            em.persist(objeto);
            if (idCategoria != null) {
                idCategoria.getObjetoCollection().add(objeto);
                idCategoria = em.merge(idCategoria);
            }
            if (usuario != null) {
                usuario.getObjetoCollection().add(objeto);
                usuario = em.merge(usuario);
            }
            for (Post postCollectionPost : objeto.getPostCollection()) {
                Objeto oldIdObjetoOfPostCollectionPost = postCollectionPost.getIdObjeto();
                postCollectionPost.setIdObjeto(objeto);
                postCollectionPost = em.merge(postCollectionPost);
                if (oldIdObjetoOfPostCollectionPost != null) {
                    oldIdObjetoOfPostCollectionPost.getPostCollection().remove(postCollectionPost);
                    oldIdObjetoOfPostCollectionPost = em.merge(oldIdObjetoOfPostCollectionPost);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Objeto objeto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objeto persistentObjeto = em.find(Objeto.class, objeto.getIdObjeto());
            Categoria idCategoriaOld = persistentObjeto.getIdCategoria();
            Categoria idCategoriaNew = objeto.getIdCategoria();
            UsrComunidad usuarioOld = persistentObjeto.getUsuario();
            UsrComunidad usuarioNew = objeto.getUsuario();
            Collection<Post> postCollectionOld = persistentObjeto.getPostCollection();
            Collection<Post> postCollectionNew = objeto.getPostCollection();
            List<String> illegalOrphanMessages = null;
            for (Post postCollectionOldPost : postCollectionOld) {
                if (!postCollectionNew.contains(postCollectionOldPost)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Post " + postCollectionOldPost + " since its idObjeto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                objeto.setIdCategoria(idCategoriaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUsuario());
                objeto.setUsuario(usuarioNew);
            }
            Collection<Post> attachedPostCollectionNew = new ArrayList<Post>();
            for (Post postCollectionNewPostToAttach : postCollectionNew) {
                postCollectionNewPostToAttach = em.getReference(postCollectionNewPostToAttach.getClass(), postCollectionNewPostToAttach.getIdPost());
                attachedPostCollectionNew.add(postCollectionNewPostToAttach);
            }
            postCollectionNew = attachedPostCollectionNew;
            objeto.setPostCollection(postCollectionNew);
            objeto = em.merge(objeto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getObjetoCollection().remove(objeto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getObjetoCollection().add(objeto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getObjetoCollection().remove(objeto);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getObjetoCollection().add(objeto);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Post postCollectionNewPost : postCollectionNew) {
                if (!postCollectionOld.contains(postCollectionNewPost)) {
                    Objeto oldIdObjetoOfPostCollectionNewPost = postCollectionNewPost.getIdObjeto();
                    postCollectionNewPost.setIdObjeto(objeto);
                    postCollectionNewPost = em.merge(postCollectionNewPost);
                    if (oldIdObjetoOfPostCollectionNewPost != null && !oldIdObjetoOfPostCollectionNewPost.equals(objeto)) {
                        oldIdObjetoOfPostCollectionNewPost.getPostCollection().remove(postCollectionNewPost);
                        oldIdObjetoOfPostCollectionNewPost = em.merge(oldIdObjetoOfPostCollectionNewPost);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = objeto.getIdObjeto();
                if (findObjeto(id) == null) {
                    throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.");
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
            Objeto objeto;
            try {
                objeto = em.getReference(Objeto.class, id);
                objeto.getIdObjeto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Post> postCollectionOrphanCheck = objeto.getPostCollection();
            for (Post postCollectionOrphanCheckPost : postCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Objeto (" + objeto + ") cannot be destroyed since the Post " + postCollectionOrphanCheckPost + " in its postCollection field has a non-nullable idObjeto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = objeto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getObjetoCollection().remove(objeto);
                idCategoria = em.merge(idCategoria);
            }
            UsrComunidad usuario = objeto.getUsuario();
            if (usuario != null) {
                usuario.getObjetoCollection().remove(objeto);
                usuario = em.merge(usuario);
            }
            em.remove(objeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Objeto> findObjetoEntities() {
        return findObjetoEntities(true, -1, -1);
    }

    public List<Objeto> findObjetoEntities(int maxResults, int firstResult) {
        return findObjetoEntities(false, maxResults, firstResult);
    }

    private List<Objeto> findObjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objeto.class));
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

    public Objeto findObjeto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Objeto.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objeto> rt = cq.from(Objeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
