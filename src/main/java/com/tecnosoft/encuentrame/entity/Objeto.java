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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Karly
 */
@Entity
@Table(name = "OBJETO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objeto.findAll", query = "SELECT o FROM Objeto o"),
    @NamedQuery(name = "Objeto.findByIdObjeto", query = "SELECT o FROM Objeto o WHERE o.idObjeto = :idObjeto"),
    @NamedQuery(name = "Objeto.findByFechaE", query = "SELECT o FROM Objeto o WHERE o.fechaE = :fechaE"),
    @NamedQuery(name = "Objeto.findByLugarW", query = "SELECT o FROM Objeto o WHERE o.lugarW = :lugarW"),
    @NamedQuery(name = "Objeto.findByDescripcion", query = "SELECT o FROM Objeto o WHERE o.descripcion = :descripcion"),
    @NamedQuery(name = "Objeto.findByImagen", query = "SELECT o FROM Objeto o WHERE o.imagen = :imagen")})
public class Objeto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_objeto")
    private Integer idObjeto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_e")
    @Temporal(TemporalType.DATE)
    private Date fechaE;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "lugar_w")
    private String lugarW;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "imagen")
    private String imagen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idObjeto")
    private Collection<Post> postCollection;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false)
    private Categoria idCategoria;
    @JoinColumn(name = "usuario", referencedColumnName = "usuario")
    @ManyToOne(optional = false)
    private UsrComunidad usuario;

    public Objeto() {
    }

    public Objeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Objeto(Integer idObjeto, Date fechaE, String lugarW, String descripcion, String imagen) {
        this.idObjeto = idObjeto;
        this.fechaE = fechaE;
        this.lugarW = lugarW;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Date getFechaE() {
        return fechaE;
    }

    public void setFechaE(Date fechaE) {
        this.fechaE = fechaE;
    }

    public String getLugarW() {
        return lugarW;
    }

    public void setLugarW(String lugarW) {
        this.lugarW = lugarW;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @XmlTransient
    public Collection<Post> getPostCollection() {
        return postCollection;
    }

    public void setPostCollection(Collection<Post> postCollection) {
        this.postCollection = postCollection;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
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
        hash += (idObjeto != null ? idObjeto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objeto)) {
            return false;
        }
        Objeto other = (Objeto) object;
        if ((this.idObjeto == null && other.idObjeto != null) || (this.idObjeto != null && !this.idObjeto.equals(other.idObjeto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Objeto[ idObjeto=" + idObjeto + " ]";
    }
    
}
