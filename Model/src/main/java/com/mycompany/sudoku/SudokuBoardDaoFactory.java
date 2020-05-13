/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sudoku;


public class SudokuBoardDaoFactory {
    
    public Dao<SudokuBoard> getFileDao(String filename) {
        
        Dao<SudokuBoard> dao = new FileSudokuBoardDao(filename);
        return dao;
    }
}
