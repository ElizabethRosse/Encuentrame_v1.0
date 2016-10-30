/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.service.implement;

import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.repository.ObjetoRepository;
import com.tecnosoft.encuentrame.service.ObjetoService;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dago
 */

@Service
public class ObjetoServiceImplement implements ObjetoService, Serializable {

    @Autowired
    ObjetoRepository objRepo;
    
    @Override
    public List<Objeto> findAll() {
        List<Objeto> objList = (List) objRepo.findAll();
        return objList;
    }
    
    
}
