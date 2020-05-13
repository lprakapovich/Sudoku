package com.mycompany.sudoku;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SudokuBoard implements Serializable, Cloneable {
        
    static final int LENGTH = 81;
    static final int WIDTH = 9;
    static final int BOX_WIDTH = 3;
    
    private final List<SudokuField> board = Arrays.asList(new SudokuField[LENGTH]);
    private final List<SudokuColumn> columns = Arrays.asList(new SudokuColumn[WIDTH]);
    private final List<SudokuRow> rows = Arrays.asList(new SudokuRow[WIDTH]);
    private final List<SudokuBox> boxes = Arrays.asList(new SudokuBox[WIDTH]);
   
    private final SudokuSolver solver; 

    public SudokuBoard(SudokuSolver solver) {

        this.solver = solver;

        int index;
        for (SudokuField field : board) {
            index = board.indexOf(field);
            board.set(index, new SudokuField());
        }

        for (int i = 0; i < WIDTH; i++) {

            rows.set(i, new SudokuRow());
            columns.set(i, new SudokuColumn());

            for (int j = 0; j < WIDTH; j++) {
                rows.get(i).set(board.get(i * WIDTH + j), j);
                columns.get(i).set(board.get(i + WIDTH * j), j);
            }
        }
        
         for (int i = 0; i < BOX_WIDTH; i++) {
            for (int j = 0; j < BOX_WIDTH; j++) {
                boxes.set(i * BOX_WIDTH + j, new SudokuBox());
                
                for (int x = 0; x < BOX_WIDTH; x++) {
                    for (int y = 0; y < BOX_WIDTH; y++) {
                        boxes.get(i * BOX_WIDTH + j).set(board.get((i * 27)
                                + (j * 3) + (x * 9 + y)), x * BOX_WIDTH + y);
                    }
                }
            }
         }            
    }

    public void solveGame() {
        solver.solve(this);
    }
    
    public void prepareBoardForGame(DifficultyLevel level) {
        
        int numberOfFieldsToRemove = 0;
  
        if (null != level) {
            switch (level) {
                case SIMPLE:
                    numberOfFieldsToRemove = 30;
                    break;
                case MEDIUM:
                    numberOfFieldsToRemove = 40;
                    break;
                case HARD:
                    numberOfFieldsToRemove = 60;
                    break;
                default:
                    numberOfFieldsToRemove = 30; 
			break;
            }
        }   
        removeRandomFields(numberOfFieldsToRemove);
    }
    
    
    public void removeRandomFields(int numberOfFieldsToRemove) {
                
        while (numberOfFieldsToRemove > 0) {
            int cell = (int) (Math.random() * (LENGTH - 1)) + 0;
            if (board.get(cell).getFieldValue() != 0) {
                board.get(cell).setFieldValue(0);
                numberOfFieldsToRemove--;
            }
        }
    }
   
    public List<SudokuField> getBoard() {
        return board;
    }
    
    public SudokuRow getRow(int index) {
        return rows.get(index);
    }
    
    public SudokuColumn getColumn(int index) {
        return columns.get(index); 
    }
    
    public SudokuBox getBox(int index) {
        return boxes.get(index);
    }

    public SudokuField getField(int index) {
        return board.get(index);
    }

    public boolean checkBoard() {
        
        for (SudokuRow row : rows) {
            if (!row.verify()) {
                return false;
            }
        }
        
        for (SudokuColumn column : columns) {
            if (!column.verify()) {
                return false;
            }
        }
        
        for (SudokuBox box : boxes) {
            if (!box.verify()) {
                return false;
            }
        }
        return true;
    }
    
    public int get(int cell) {
        return board.get(cell).getFieldValue();
    }
    
    
    public void set(int cell, int value) {
        board.get(cell).setFieldValue(value);
    }
       
    @Override
    public String toString() {
        
       StringBuilder builder = new StringBuilder();
        
        for (SudokuRow row : rows) {
            
            builder.append(row.toString())
                   .append("\n");
        }
        
        return builder.toString();
    }
    
    
    @Override 
    public boolean equals(Object obj) {
        
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SudokuBoard)) {
            return false;
        }    
        
        SudokuBoard other = (SudokuBoard) obj;
        EqualsBuilder builder = new EqualsBuilder();
        
        for (SudokuField field : board) {
            
            if (!(builder.append(other.get(board.indexOf(field)), field).isEquals())) {
                return false;
            }
        }
        return true;
    }
    
   
    @Override 
    public int hashCode() {
        
        HashCodeBuilder builder = new HashCodeBuilder();
        
        for (SudokuField field : board) {
            builder.append(field.hashCode());
        }
        return builder.toHashCode();
    }
    
    public SudokuBoard clone() throws CloneNotSupportedException {

        SudokuBoard cloned = (SudokuBoard) SerializationUtils.clone(this);
        return cloned;
    }
    
   public static void main(String[] args) {} 
}
