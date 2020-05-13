/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sudoku;

import java.io.Serializable;
import java.util.ArrayList;

public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {
   
    @Override
    public boolean solve(SudokuBoard board) {
         
         int cell = 0;
         boolean isEmpty = true;
         
         for (int i = 0; i < 81; i++) {
             if (board.get(i) == 0) {
                 
                 cell = i;
                 isEmpty = false;
                 break;
             }
         }
         
         if (isEmpty) {
             return true;
         }
         
         ArrayList<Integer> numbers = new ArrayList<>();
         for (int i = 1; i < 10; i++) {
             numbers.add(i);
         }
         
         for (int num = 0; num < 9; num++) {
             
             int index = (int) (Math.random() * (numbers.size() - 1)) + 0;
             int number = numbers.get(index);
             board.set(cell, number);
                 
                 if (board.checkBoard()) {
                 
                 if (solve(board)) {
                     return true;
                 } else {
                     board.set(cell, 0);
                 }
              } else { 
                     board.set(cell, 0);
                 }
                 numbers.remove(index);
         }
        return false;
     }
}


