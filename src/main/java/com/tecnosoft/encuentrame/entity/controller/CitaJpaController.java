/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity.controller;

import com.tecnosoft.encuentrame.entity.Cita;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tecnosoft.encuentrame.entity.Lugar;
import com.tecnosoft.encuentrame.entity.UsrCalificacion;
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
public class CitaJpaController implements Serializable {

    public CitaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) {
        if (cita.getUsrCalificacionCollection() == null) {
            cita.setUsrCalificacionCollection(new ArrayList<UsrCalificacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lugar nombreLugar = cita.getNombreLugar();
            if (nombreLugar != null) {
                nombreLugar = em.getReference(nombreLugar.getClass(), nombreLugar.getNombreLugar());
                cita.setNombreLugar(nombreLugar);
            }
            Collection<UsrCalificacion> attachedUsrCalificacionCollection = new ArrayList<UsrCalificacion>();
            for (UsrCalificacion usrCalificacionCollectionUsrCalificacionToAttach : cita.getUsrCalificacionCollection()) {
                usrCalificacionCollectionUsrCalificacionToAttach = em.getReference(usrCalificacionCollectionUsrCalificacionToAttach.getClass(), usrCalificacionCollectionUsrCalificacionToAttach.getIdCalificacion());
                attachedUsrCalificacionCollection.add(usrCalificacionCollectionUsrCalificacionToAttach);
            }
            cita.setUsrCalificacionCollection(attachedUsrCalificacionCollection);
            em.persist(cita);
            if (nombreLugar != null) {
                nombreLugar.getCitaCollection().add(cita);
                nombreLugar = em.merge(nombreLugar);
            }
            for (UsrCalificacion usrCalificacionCollectionUsrCalificacion : cita.getUsrCalificacionCollection()) {
                Cita oldIdCitaOfUsrCalificacionCollectionUsrCalificacion = usrCalificacionCollectionUsrCalificacion.getIdCita();
                usrCalificacionCollectionUsrCalificacion.setIdCita(cita);
                usrCalificacionCollectionUsrCalificacion = em.merge(usrCalificacionCollectionUsrCalificacion);
                if (oldIdCitaOfUsrCalificacionCollectionUsrCalificacion != null) {
                    oldIdCitaOfUsrCalificacionCollectionUsrCalificacion.getUsrCalificacionCollection().remove(usrCalificacionCollectionUsrCalificacion);
                    oldIdCitaOfUsrCalificacionCollectionUsrCalificacion = em.merge(oldIdCitaOfUsrCalificacionCollectionUsrCalificacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cita persistentCita = em.find(Cita.class, cita.getIdCita());
            Lugar nombreLugarOld = persistentCita.getNombreLugar();
            Lugar nombreLugarNew = cita.getNombreLugar();
            Collection<UsrCalificacion> usrCalificacionCollectionOld = persistentCita.getUsrCalificacionCollection();
            Collection<UsrCalificacion> usrCalificacionCollectionNew = cita.getUsrCalificacionCollection();
            List<String> illegalOrphanMessages = null;
            for (UsrCalificacion usrCalificacionCollectionOldUsrCalificacion : usrCalificacionCollectionOld) {
                if (!usrCalificacionCollectionNew.contains(usrCalificacionCollectionOldUsrCalificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsrCalificacion " + usrCalificacionCollectionOldUsrCalificacion + " since its idCita field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nombreLugarNew != null) {
                nombreLugarNew = em.getReference(nombreLugarNew.getClass(), nombreLugarNew.getNombreLugar());
                cita.setNombreLugar(nombreLugarNew);
            }
            Collection<UsrCalificacion> attachedUsrCalificacionCollectionNew = new ArrayList<UsrCalificacion>();
            for (UsrCalificacion usrCalificacionCollectionNewUsrCalificacionToAttach : usrCalificacionCollectionNew) {
                usrCalificacionCollectionNewUsrCalificacionToAttach = em.getReference(usrCalificacionCollectionNewUsrCalificacionToAttach.getClass(), usrCalificacionCollectionNewUsrCalificacionToAttach.getIdCalificacion());
                attachedUsrCalificacionCollectionNew.add(usrCalificacionCollectionNewUsrCalificacionToAttach);
            }
            usrCalificacionCollectionNew = attachedUsrCalificacionCollectionNew;
            cita.setUsrCalificacionCollection(usrCalificacionCollectionNew);
            cita = em.merge(cita);
            if (nombreLugarOld != null && !nombreLugarOld.equals(nombreLugarNew)) {
                nombreLugarOld.getCitaCollection().remove(cita);
                nombreLugarOld = em.merge(nombreLugarOld);
            }
            if (nombreLugarNew != null && !nombreLugarNew.equals(nombreLugarOld)) {
                nombreLugarNew.getCitaCollection().add(cita);
                nombreLugarNew = em.merge(nombreLugarNew);
            }
            for (UsrCalificacion usrCalificacionCollectionNewUsrCalificacion : usrCalificacionCollectionNew) {
                if (!usrCalificacionCollectionOld.contains(usrCalificacionCollectionNewUsrCalificacion)) {
                    Cita oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion = usrCalificacionCollectionNewUsrCalificacion.getIdCita();
                    usrCalificacionCollectionNewUsrCalificacion.setIdCita(cita);
                    usrCalificacionCollectionNewUsrCalificacion = em.merge(usrCalificacionCollectionNewUsrCalificacion);
                    if (oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion != null && !oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion.equals(cita)) {
                        oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion.getUsrCalificacionCollection().remove(usrCalificacionCollectionNewUsrCalificacion);
                        oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion = em.merge(oldIdCitaOfUsrCalificacionCollectionNewUsrCalificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cita.getIdCita();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsrCalificacion> usrCalificacionCollectionOrphanCheck = cita.getUsrCalificacionCollection();
            for (UsrCalificacion usrCalificacionCollectionOrphanCheckUsrCalificacion : usrCalificacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cita (" + cita + ") cannot be destroyed since the UsrCalificacion " + usrCalificacionCollectionOrphanCheckUsrCalificacion + " in its usrCalificacionCollection field has a non-nullable idCita field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Lugar nombreLugar = cita.getNombreLugar();
            if (nombreLugar != null) {
                nombreLugar.getCitaCollection().remove(cita);
                nombreLugar = em.merge(nombreLugar);
            }
            em.remove(cita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
