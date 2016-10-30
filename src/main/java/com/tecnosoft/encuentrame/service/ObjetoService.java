/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnosoft.encuentrame.service;

import com.tecnosoft.encuentrame.entity.Objeto;
import java.util.List;

/**
 *
 * @author Dago
 */
public interface ObjetoService {
    List<Objeto> findAll();
}
