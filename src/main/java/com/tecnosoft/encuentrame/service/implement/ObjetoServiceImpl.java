/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.service.implement;

import com.tecnosoft.encuentrame.entity.Objeto;
import com.tecnosoft.encuentrame.repository.IObjetoRepository;
import com.tecnosoft.encuentrame.service.IObjetoService;
import java.util.List;


public class ObjetoServiceImpl implements IObjetoService {
    
    private IObjetoRepository objRep;
    
    /**
     * Guarda los datos en DB
     * @param obj que guardaremos
     * @return el objeto que guardamos
     */
    @Override
    public Objeto saveObj(Objeto obj) {
        return objRep.save(obj);
    }
    
    /**
     * Recupera el objeto de BD
     * @param id del objeto
     * @return obejto que buscamos
     */
    @Override
    public Objeto getObjById(Integer id) {
        return objRep.findOne(id);
    }
    
    /**
     * Elimina el objeto de BD
     * @param id del objeto
     * @return obejto que eliminamos
     */
    @Override
    public void deletObjeto(Integer id) {
        objRep.delete(id);
    }
    
    /**
     * Regresa un lista con todos los elementos de la tabla
     * @return lista de objetos
     */
    @Override
    public List<Objeto> findAllObj() {
        return (List<Objeto>) objRep.findAll();
    }
    
}
