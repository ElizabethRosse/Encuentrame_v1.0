/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.repository;

import com.tecnosoft.encuentrame.entity.Objeto;
import java.io.Serializable;
import org.springframework.data.repository.PagingAndSortingRepository;



/**
 *
 * @author Dago
 */
public interface ObjetoRepository extends PagingAndSortingRepository<Objeto, Integer>{    
}
