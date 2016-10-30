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
import com.tecnosoft.encuentrame.entity.Lugar;
import com.tecnosoft.encuentrame.entity.controller.exceptions.IllegalOrphanException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.NonexistentEntityException;
import com.tecnosoft.encuentrame.entity.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Karly
 */
public class LugarJpaController implements Serializable {

    public LugarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lugar lugar) throws PreexistingEntityException, Exception {
        if (lugar.getCitaCollection() == null) {
            lugar.setCitaCollection(new ArrayList<Cita>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Cita> attachedCitaCollection = new ArrayList<Cita>();
            for (Cita citaCollectionCitaToAttach : lugar.getCitaCollection()) {
                citaCollectionCitaToAttach = em.getReference(citaCollectionCitaToAttach.getClass(), citaCollectionCitaToAttach.getIdCita());
                attachedCitaCollection.add(citaCollectionCitaToAttach);
            }
            lugar.setCitaCollection(attachedCitaCollection);
            em.persist(lugar);
            for (Cita citaCollectionCita : lugar.getCitaCollection()) {
                Lugar oldNombreLugarOfCitaCollectionCita = citaCollectionCita.getNombreLugar();
                citaCollectionCita.setNombreLugar(lugar);
                citaCollectionCita = em.merge(citaCollectionCita);
                if (oldNombreLugarOfCitaCollectionCita != null) {
                    oldNombreLugarOfCitaCollectionCita.getCitaCollection().remove(citaCollectionCita);
                    oldNombreLugarOfCitaCollectionCita = em.merge(oldNombreLugarOfCitaCollectionCita);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLugar(lugar.getNombreLugar()) != null) {
                throw new PreexistingEntityException("Lugar " + lugar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lugar lugar) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lugar persistentLugar = em.find(Lugar.class, lugar.getNombreLugar());
            Collection<Cita> citaCollectionOld = persistentLugar.getCitaCollection();
            Collection<Cita> citaCollectionNew = lugar.getCitaCollection();
            List<String> illegalOrphanMessages = null;
            for (Cita citaCollectionOldCita : citaCollectionOld) {
                if (!citaCollectionNew.contains(citaCollectionOldCita)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cita " + citaCollectionOldCita + " since its nombreLugar field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Cita> attachedCitaCollectionNew = new ArrayList<Cita>();
            for (Cita citaCollectionNewCitaToAttach : citaCollectionNew) {
                citaCollectionNewCitaToAttach = em.getReference(citaCollectionNewCitaToAttach.getClass(), citaCollectionNewCitaToAttach.getIdCita());
                attachedCitaCollectionNew.add(citaCollectionNewCitaToAttach);
            }
            citaCollectionNew = attachedCitaCollectionNew;
            lugar.setCitaCollection(citaCollectionNew);
            lugar = em.merge(lugar);
            for (Cita citaCollectionNewCita : citaCollectionNew) {
                if (!citaCollectionOld.contains(citaCollectionNewCita)) {
                    Lugar oldNombreLugarOfCitaCollectionNewCita = citaCollectionNewCita.getNombreLugar();
                    citaCollectionNewCita.setNombreLugar(lugar);
                    citaCollectionNewCita = em.merge(citaCollectionNewCita);
                    if (oldNombreLugarOfCitaCollectionNewCita != null && !oldNombreLugarOfCitaCollectionNewCita.equals(lugar)) {
                        oldNombreLugarOfCitaCollectionNewCita.getCitaCollection().remove(citaCollectionNewCita);
                        oldNombreLugarOfCitaCollectionNewCita = em.merge(oldNombreLugarOfCitaCollectionNewCita);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = lugar.getNombreLugar();
                if (findLugar(id) == null) {
                    throw new NonexistentEntityException("The lugar with id " + id + " no longer exists.");
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
            Lugar lugar;
            try {
                lugar = em.getReference(Lugar.class, id);
                lugar.getNombreLugar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lugar with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cita> citaCollectionOrphanCheck = lugar.getCitaCollection();
            for (Cita citaCollectionOrphanCheckCita : citaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lugar (" + lugar + ") cannot be destroyed since the Cita " + citaCollectionOrphanCheckCita + " in its citaCollection field has a non-nullable nombreLugar field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(lugar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lugar> findLugarEntities() {
        return findLugarEntities(true, -1, -1);
    }

    public List<Lugar> findLugarEntities(int maxResults, int firstResult) {
        return findLugarEntities(false, maxResults, firstResult);
    }

    private List<Lugar> findLugarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lugar.class));
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

    public Lugar findLugar(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lugar.class, id);
        } finally {
            em.close();
        }
    }

    public int getLugarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lugar> rt = cq.from(Lugar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
