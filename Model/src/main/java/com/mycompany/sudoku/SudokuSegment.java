/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.sudoku;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class SudokuSegment implements Serializable, Cloneable {

    private final List<SudokuField> segment;

    public SudokuSegment() {
        
        segment = Arrays.asList(new SudokuField[SudokuBoard.WIDTH]);
        
        for (int i = 0; i < 9; i++) {
            segment.set(i, new SudokuField());
        }
    }

    public void set(SudokuField field, int cell) {
        segment.set(cell, field);
    }

    
    public int get(int cell) {
        return segment.get(cell).getFieldValue();
    }
    
        
   public SudokuField getField(int i) {
       return segment.get(i);
   }
   
    
    public boolean verify() {
        
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                
                if (segment.get(i).getFieldValue() == 0) {
                    break;
                }
                if (segment.get(i).getFieldValue() == segment.get(j).getFieldValue()) {
                    return false;
                }
            }
        }
        return true; 

    }    
    
     @Override
  public String toString() {
      
      StringBuilder builder = new StringBuilder();

     for (SudokuField field : segment) {
         builder.append(field.toString())
                 .append(" ");
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
      if (!(obj instanceof SudokuSegment)) {
          return false;
      }
      
      SudokuSegment other = (SudokuSegment) obj;
      EqualsBuilder builder = new EqualsBuilder();

      for (SudokuField field : segment) {
          if (!(builder.append(other.get(segment.indexOf(field)), field).isEquals())) {
              return false;
          }
      }      
      return true;
  }
  
  
  @Override 
  public int hashCode() {
      
      HashCodeBuilder builder = new HashCodeBuilder();
      for (SudokuField field : segment) {
          builder.append(field.hashCode());
      }
      return builder.toHashCode();
  }
  
   public SudokuSegment clone() throws CloneNotSupportedException {

      SudokuSegment cloned = (SudokuSegment)SerializationUtils.clone(this);  
      return cloned;
  }
}
