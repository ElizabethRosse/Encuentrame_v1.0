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
@Table(name = "FACEBOOK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facebook.findAll", query = "SELECT f FROM Facebook f"),
    @NamedQuery(name = "Facebook.findByUsuarioFb", query = "SELECT f FROM Facebook f WHERE f.usuarioFb = :usuarioFb")})
public class Facebook implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "usuario_fb")
    private String usuarioFb;
    @JoinColumn(name = "usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private UsrComunidad usuario;

    public Facebook() {
    }

    public Facebook(String usuarioFb) {
        this.usuarioFb = usuarioFb;
    }

    public String getUsuarioFb() {
        return usuarioFb;
    }

    public void setUsuarioFb(String usuarioFb) {
        this.usuarioFb = usuarioFb;
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
        hash += (usuarioFb != null ? usuarioFb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facebook)) {
            return false;
        }
        Facebook other = (Facebook) object;
        if ((this.usuarioFb == null && other.usuarioFb != null) || (this.usuarioFb != null && !this.usuarioFb.equals(other.usuarioFb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Facebook[ usuarioFb=" + usuarioFb + " ]";
    }
    
}
