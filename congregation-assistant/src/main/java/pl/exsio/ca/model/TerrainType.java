/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model;

import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;

/**
 *
 * @author exsio
 */
public enum TerrainType {
    
    CITY("city"),
    VILLAGE("village"),
    PHONE("phone");
    
    private static final String TRANSLATION_PREFIX = "ca.terrain_type.";

    private final String caption;

    TerrainType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return t(TRANSLATION_PREFIX + this.caption);
    }
    
    @Override
    public String toString() {
        return this.getCaption();
    }
}
