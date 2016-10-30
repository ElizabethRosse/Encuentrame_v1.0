/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.repository;

import com.tecnosoft.encuentrame.entity.Objeto;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;



/**
 *
 * @author RoyCM
 */
public interface IObjetoRepository extends PagingAndSortingRepository<Objeto, Integer>{
    
   /**
    * Aqui implementan metodos propios de busqueda
    */
     
}
