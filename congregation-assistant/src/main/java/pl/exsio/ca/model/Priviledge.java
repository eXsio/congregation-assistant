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
public enum Priviledge {

    MIXER("mixer"),
    MICROPHONE("microphone"),
    LECTOR("lector"),
    SERVANT("servant"),
    ELDER("elder"),
    MAGAZINES_SERVANT("magazines_servant"),
    TERRAINS_SERVANT("terrains_servant"),
    LITERATURE_SERVANT("literature_servant"),
    ELDERS_COORDINATOR("elders_coordinator"),
    SERVICE_OVERSEER("service_overseer"),
    SCHOOL_OVERSEER("school_overseer"),
    SOUND_SERVANT("sound_servant"),
    SERVICE_GROUP_OVERSEER("service_group_overseer"),
    ORDERLY("orderly"),
    CONGREAGATION_SECRETARY("congregation_secretary"),
    PIONEER("pioneer"),
    PIONEER_HELPER("pioneer_helper"),
    PIONEER_SPECIAL("pioneer_special"),
    DISTRICT_OVERSEER("district_overseer"),
    ACCOUNTS_SERVANT("accounts_servant");

    private static final String TRANSLATION_PREFIX = "ca.priviledge.";

    private final String caption;

    Priviledge(String caption) {
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
