/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sudoku;

import java.util.List;

public interface Dao<T> {

    public List<T> read();

    public void write(T one, T two);
}
