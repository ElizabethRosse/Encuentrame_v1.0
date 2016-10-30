/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import com.tecnosoft.encuentrame.entity.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tecnosoft.encuentrame.entity.Objeto;
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
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) {
        if (categoria.getObjetoCollection() == null) {
            categoria.setObjetoCollection(new ArrayList<Objeto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Objeto> attachedObjetoCollection = new ArrayList<Objeto>();
            for (Objeto objetoCollectionObjetoToAttach : categoria.getObjetoCollection()) {
                objetoCollectionObjetoToAttach = em.getReference(objetoCollectionObjetoToAttach.getClass(), objetoCollectionObjetoToAttach.getIdObjeto());
                attachedObjetoCollection.add(objetoCollectionObjetoToAttach);
            }
            categoria.setObjetoCollection(attachedObjetoCollection);
            em.persist(categoria);
            for (Objeto objetoCollectionObjeto : categoria.getObjetoCollection()) {
                Categoria oldIdCategoriaOfObjetoCollectionObjeto = objetoCollectionObjeto.getIdCategoria();
                objetoCollectionObjeto.setIdCategoria(categoria);
                objetoCollectionObjeto = em.merge(objetoCollectionObjeto);
                if (oldIdCategoriaOfObjetoCollectionObjeto != null) {
                    oldIdCategoriaOfObjetoCollectionObjeto.getObjetoCollection().remove(objetoCollectionObjeto);
                    oldIdCategoriaOfObjetoCollectionObjeto = em.merge(oldIdCategoriaOfObjetoCollectionObjeto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getIdCategoria());
            Collection<Objeto> objetoCollectionOld = persistentCategoria.getObjetoCollection();
            Collection<Objeto> objetoCollectionNew = categoria.getObjetoCollection();
            List<String> illegalOrphanMessages = null;
            for (Objeto objetoCollectionOldObjeto : objetoCollectionOld) {
                if (!objetoCollectionNew.contains(objetoCollectionOldObjeto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Objeto " + objetoCollectionOldObjeto + " since its idCategoria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Objeto> attachedObjetoCollectionNew = new ArrayList<Objeto>();
            for (Objeto objetoCollectionNewObjetoToAttach : objetoCollectionNew) {
                objetoCollectionNewObjetoToAttach = em.getReference(objetoCollectionNewObjetoToAttach.getClass(), objetoCollectionNewObjetoToAttach.getIdObjeto());
                attachedObjetoCollectionNew.add(objetoCollectionNewObjetoToAttach);
            }
            objetoCollectionNew = attachedObjetoCollectionNew;
            categoria.setObjetoCollection(objetoCollectionNew);
            categoria = em.merge(categoria);
            for (Objeto objetoCollectionNewObjeto : objetoCollectionNew) {
                if (!objetoCollectionOld.contains(objetoCollectionNewObjeto)) {
                    Categoria oldIdCategoriaOfObjetoCollectionNewObjeto = objetoCollectionNewObjeto.getIdCategoria();
                    objetoCollectionNewObjeto.setIdCategoria(categoria);
                    objetoCollectionNewObjeto = em.merge(objetoCollectionNewObjeto);
                    if (oldIdCategoriaOfObjetoCollectionNewObjeto != null && !oldIdCategoriaOfObjetoCollectionNewObjeto.equals(categoria)) {
                        oldIdCategoriaOfObjetoCollectionNewObjeto.getObjetoCollection().remove(objetoCollectionNewObjeto);
                        oldIdCategoriaOfObjetoCollectionNewObjeto = em.merge(oldIdCategoriaOfObjetoCollectionNewObjeto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getIdCategoria();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getIdCategoria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Objeto> objetoCollectionOrphanCheck = categoria.getObjetoCollection();
            for (Objeto objetoCollectionOrphanCheckObjeto : objetoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Objeto " + objetoCollectionOrphanCheckObjeto + " in its objetoCollection field has a non-nullable idCategoria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
