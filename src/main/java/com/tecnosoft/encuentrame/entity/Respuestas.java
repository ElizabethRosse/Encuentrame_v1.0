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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Karly
 */
@Entity
@Table(name = "RESPUESTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respuestas.findAll", query = "SELECT r FROM Respuestas r"),
    @NamedQuery(name = "Respuestas.findByIdRespuestas", query = "SELECT r FROM Respuestas r WHERE r.idRespuestas = :idRespuestas"),
    @NamedQuery(name = "Respuestas.findByEstado", query = "SELECT r FROM Respuestas r WHERE r.estado = :estado"),
    @NamedQuery(name = "Respuestas.findByRespuesta1", query = "SELECT r FROM Respuestas r WHERE r.respuesta1 = :respuesta1"),
    @NamedQuery(name = "Respuestas.findByRespuesta2", query = "SELECT r FROM Respuestas r WHERE r.respuesta2 = :respuesta2"),
    @NamedQuery(name = "Respuestas.findByRespuesta3", query = "SELECT r FROM Respuestas r WHERE r.respuesta3 = :respuesta3"),
    @NamedQuery(name = "Respuestas.findByRespuesta4", query = "SELECT r FROM Respuestas r WHERE r.respuesta4 = :respuesta4"),
    @NamedQuery(name = "Respuestas.findByRespuesta5", query = "SELECT r FROM Respuestas r WHERE r.respuesta5 = :respuesta5")})
public class Respuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_respuestas")
    private Integer idRespuestas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "respuesta1")
    private String respuesta1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "respuesta2")
    private String respuesta2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "respuesta3")
    private String respuesta3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "respuesta4")
    private String respuesta4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "respuesta5")
    private String respuesta5;
    @JoinColumn(name = "id_encuesta", referencedColumnName = "id_encuesta")
    @ManyToOne(optional = false)
    private Encuesta idEncuesta;
    @JoinColumn(name = "usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private UsrComunidad usuario;

    public Respuestas() {
    }

    public Respuestas(Integer idRespuestas) {
        this.idRespuestas = idRespuestas;
    }

    public Respuestas(Integer idRespuestas, String estado, String respuesta1, String respuesta2, String respuesta3, String respuesta4, String respuesta5) {
        this.idRespuestas = idRespuestas;
        this.estado = estado;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.respuesta3 = respuesta3;
        this.respuesta4 = respuesta4;
        this.respuesta5 = respuesta5;
    }

    public Integer getIdRespuestas() {
        return idRespuestas;
    }

    public void setIdRespuestas(Integer idRespuestas) {
        this.idRespuestas = idRespuestas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public String getRespuesta4() {
        return respuesta4;
    }

    public void setRespuesta4(String respuesta4) {
        this.respuesta4 = respuesta4;
    }

    public String getRespuesta5() {
        return respuesta5;
    }

    public void setRespuesta5(String respuesta5) {
        this.respuesta5 = respuesta5;
    }

    public Encuesta getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Encuesta idEncuesta) {
        this.idEncuesta = idEncuesta;
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
        hash += (idRespuestas != null ? idRespuestas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Respuestas)) {
            return false;
        }
        Respuestas other = (Respuestas) object;
        if ((this.idRespuestas == null && other.idRespuestas != null) || (this.idRespuestas != null && !this.idRespuestas.equals(other.idRespuestas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Respuestas[ idRespuestas=" + idRespuestas + " ]";
    }
    
}
