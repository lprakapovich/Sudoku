/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.tips;

import java.util.ListResourceBundle;

public class Tips_pl extends ListResourceBundle {

    private final Object[][] contents = {
        {"SIMPLE", "DLA POCZĄTKUJĄCYCH"},
        {"MEDIUM", "DLA DOŚWIADCZONYCH"},
        {"HARD", "DLA AMBITNYCH!"}
    };

    @Override
    protected final Object[][] getContents() {
        return contents;
    }
}
