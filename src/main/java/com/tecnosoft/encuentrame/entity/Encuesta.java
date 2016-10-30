/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Karly
 */
@Entity
@Table(name = "ENCUESTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encuesta.findAll", query = "SELECT e FROM Encuesta e"),
    @NamedQuery(name = "Encuesta.findByIdEncuesta", query = "SELECT e FROM Encuesta e WHERE e.idEncuesta = :idEncuesta"),
    @NamedQuery(name = "Encuesta.findByPregunta1", query = "SELECT e FROM Encuesta e WHERE e.pregunta1 = :pregunta1"),
    @NamedQuery(name = "Encuesta.findByPregunta2", query = "SELECT e FROM Encuesta e WHERE e.pregunta2 = :pregunta2"),
    @NamedQuery(name = "Encuesta.findByPregunta3", query = "SELECT e FROM Encuesta e WHERE e.pregunta3 = :pregunta3"),
    @NamedQuery(name = "Encuesta.findByPregunta4", query = "SELECT e FROM Encuesta e WHERE e.pregunta4 = :pregunta4"),
    @NamedQuery(name = "Encuesta.findByPregunta5", query = "SELECT e FROM Encuesta e WHERE e.pregunta5 = :pregunta5")})
public class Encuesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_encuesta")
    private Integer idEncuesta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta1")
    private String pregunta1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta2")
    private String pregunta2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta3")
    private String pregunta3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta4")
    private String pregunta4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta5")
    private String pregunta5;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEncuesta")
    private Collection<Respuestas> respuestasCollection;
    @JoinColumn(name = "id_post", referencedColumnName = "id_post")
    @ManyToOne(optional = false)
    private Post idPost;

    public Encuesta() {
    }

    public Encuesta(Integer idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public Encuesta(Integer idEncuesta, String pregunta1, String pregunta2, String pregunta3, String pregunta4, String pregunta5) {
        this.idEncuesta = idEncuesta;
        this.pregunta1 = pregunta1;
        this.pregunta2 = pregunta2;
        this.pregunta3 = pregunta3;
        this.pregunta4 = pregunta4;
        this.pregunta5 = pregunta5;
    }

    public Integer getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Integer idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(String pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public String getPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(String pregunta4) {
        this.pregunta4 = pregunta4;
    }

    public String getPregunta5() {
        return pregunta5;
    }

    public void setPregunta5(String pregunta5) {
        this.pregunta5 = pregunta5;
    }

    @XmlTransient
    public Collection<Respuestas> getRespuestasCollection() {
        return respuestasCollection;
    }

    public void setRespuestasCollection(Collection<Respuestas> respuestasCollection) {
        this.respuestasCollection = respuestasCollection;
    }

    public Post getIdPost() {
        return idPost;
    }

    public void setIdPost(Post idPost) {
        this.idPost = idPost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEncuesta != null ? idEncuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encuesta)) {
            return false;
        }
        Encuesta other = (Encuesta) object;
        if ((this.idEncuesta == null && other.idEncuesta != null) || (this.idEncuesta != null && !this.idEncuesta.equals(other.idEncuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tecnosoft.encuentrame.entity.Encuesta[ idEncuesta=" + idEncuesta + " ]";
    }
    
}
