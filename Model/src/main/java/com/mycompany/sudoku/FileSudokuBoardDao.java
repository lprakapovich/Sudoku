/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sudoku;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {
    
    String filename;
    List<SudokuBoard> boards = new ArrayList<>();

    public FileSudokuBoardDao(String filename) {
        this.filename = filename;
    }
    
    @Override
    public List<SudokuBoard> read() {
        
        List<SudokuBoard> readBoards = null;
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            readBoards = (List<SudokuBoard>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return readBoards;
    }
    
    @Override
    public void write(SudokuBoard board, SudokuBoard another) {

        boards.add(board);
        boards.add(another);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(boards);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void close() {
        System.out.println("Closing resources in FileSudokuBoardDao...");
    }
}
