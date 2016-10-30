/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Karly
 */
@Entity
@Table(name = "CITA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @NamedQuery(name = "Cita.findByIdCita", query = "SELECT c FROM Cita c WHERE c.idCita = :idCita"),
    @NamedQuery(name = "Cita.findByHorario", query = "SELECT c FROM Cita c WHERE c.horario = :horario")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cita")
    private Integer idCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horario")
    @Temporal(TemporalType.TIME)
    private Date horario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCita")
    private Collection<UsrCalificacion> usrCalificacionCollection;
    @JoinColumn(name = "nombre_lugar", referencedColumnName = "nombre_lugar")
    @ManyToOne(optional = false)
    private Lugar nombreLugar;

    public Cita() {
    }

    public Cita(Integer idCita) {
        this.idCita = idCita;
    }

    public Cita(Integer idCita, Date horario) {
        this.idCita = idCita;
        this.horario = horario;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    @XmlTransient
    public Collection<UsrCalificacion> getUsrCalificacionCollection() {
        return usrCalificacionCollection;
    }

    public void setUsrCalificacionCollection(Collection<UsrCalificacion> usrCalificacionCollection) {
        this.usrCalificacionCollection = usrCalificacionCollection;
    }

    public Lugar getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(Lugar nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Cita[ idCita=" + idCita + " ]";
    }
    
}
