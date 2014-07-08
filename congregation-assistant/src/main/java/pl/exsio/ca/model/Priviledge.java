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
    SERVANT("servant", Priviledge.MIXER, Priviledge.MICROPHONE),
    ELDER("elder"),
    MAGAZINES_SERVANT("magazines_servant"),
    TERRAINS_SERVANT("terrains_servant"),
    LITERATURE_SERVANT("literature_servant"),
    ELDERS_COORDINATOR("elders_coordinator", Priviledge.ELDER),
    SERVICE_OVERSEER("service_overseer"),
    SCHOOL_OVERSEER("school_overseer"),
    SOUND_SERVANT("sound_servant"),
    SERVICE_GROUP_OVERSEER("service_group_overseer"),
    ORDERLY("orderly"),
    CONGREAGATION_SECRETARY("congregation_secretary"),
    ACCOUNTS_SERVANT("accounts_servant");

    private static final String TRANSLATION_PREFIX = "ca.priviledge.";

    private final String caption;

    private Priviledge[] subPriviledges;

    Priviledge(String caption, Priviledge... subPriviledgets) {
        this.caption = caption;
        this.subPriviledges = subPriviledgets;
    }

    public String getCaption() {
        return t(TRANSLATION_PREFIX + this.caption);
    }

    public Priviledge[] getSubPriviledges() {
        return subPriviledges;
    }
    
    @Override
    public String toString() {
        return this.getCaption();
    }

}
