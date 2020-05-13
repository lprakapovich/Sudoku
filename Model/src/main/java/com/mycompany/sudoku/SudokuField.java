/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.mycompany.sudoku;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
  
    private int value;

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }
    
      @Override 
   public int hashCode() {
       return new HashCodeBuilder().append(value).toHashCode();
   }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SudokuField other = (SudokuField) obj;
        return new EqualsBuilder().append(other.getFieldValue(), value).isEquals();
    } 
   
    
    @Override 
    public String toString() {
        return new StringBuilder().append(value).toString();
    }
    
        @Override
    public SudokuField clone() throws CloneNotSupportedException {
        return (SudokuField) super.clone();
    }
    
    
    @Override 
    public int compareTo(SudokuField field) {
        return this.value - field.value;
    }
}


