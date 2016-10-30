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
@Table(name = "TWITTER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Twitter.findAll", query = "SELECT t FROM Twitter t"),
    @NamedQuery(name = "Twitter.findByUsuarioTw", query = "SELECT t FROM Twitter t WHERE t.usuarioTw = :usuarioTw")})
public class Twitter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "usuario_tw")
    private String usuarioTw;
    @JoinColumn(name = "usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private UsrComunidad usuario;

    public Twitter() {
    }

    public Twitter(String usuarioTw) {
        this.usuarioTw = usuarioTw;
    }

    public String getUsuarioTw() {
        return usuarioTw;
    }

    public void setUsuarioTw(String usuarioTw) {
        this.usuarioTw = usuarioTw;
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
        hash += (usuarioTw != null ? usuarioTw.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Twitter)) {
            return false;
        }
        Twitter other = (Twitter) object;
        if ((this.usuarioTw == null && other.usuarioTw != null) || (this.usuarioTw != null && !this.usuarioTw.equals(other.usuarioTw))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Twitter[ usuarioTw=" + usuarioTw + " ]";
    }
    
}
