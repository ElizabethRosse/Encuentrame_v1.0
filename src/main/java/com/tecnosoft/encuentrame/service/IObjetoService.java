/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.service;

import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.repository.IObjetoRepository;
import java.util.List;


public interface IObjetoService  {
    Objeto saveObj(Objeto obj);
    Objeto getObjById(Integer id);
    void deletObjeto(Integer id);
    List<Objeto> findAllObj();
}
