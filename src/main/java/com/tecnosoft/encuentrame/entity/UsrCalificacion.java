/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Karly
 */
@Entity
@Table(name = "USR_CALIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsrCalificacion.findAll", query = "SELECT u FROM UsrCalificacion u"),
    @NamedQuery(name = "UsrCalificacion.findByIdCalificacion", query = "SELECT u FROM UsrCalificacion u WHERE u.idCalificacion = :idCalificacion"),
    @NamedQuery(name = "UsrCalificacion.findByCalificacion", query = "SELECT u FROM UsrCalificacion u WHERE u.calificacion = :calificacion")})
public class UsrCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_calificacion")
    private Integer idCalificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "calificacion")
    private int calificacion;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne(optional = false)
    private Cita idCita;
    @JoinColumn(name = "usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private UsrComunidad usuario;

    public UsrCalificacion() {
    }

    public UsrCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public UsrCalificacion(Integer idCalificacion, int calificacion) {
        this.idCalificacion = idCalificacion;
        this.calificacion = calificacion;
    }

    public Integer getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public Cita getIdCita() {
        return idCita;
    }

    public void setIdCita(Cita idCita) {
        this.idCita = idCita;
    }

    public UsrComunidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsrComunidad usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCalificacion != null ? idCalificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsrCalificacion)) {
            return false;
        }
        UsrCalificacion other = (UsrCalificacion) object;
        if ((this.idCalificacion == null && other.idCalificacion != null) || (this.idCalificacion != null && !this.idCalificacion.equals(other.idCalificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.UsrCalificacion[ idCalificacion=" + idCalificacion + " ]";
    }
    
}
