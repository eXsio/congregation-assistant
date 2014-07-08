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
    MAGAZINES_SERVANT("magazines_servant", Priviledge.SERVANT),
    TERRAINS_SERVANT("terrains_servant", Priviledge.SERVANT),
    LITERATURE_SERVANT("literature_servant", Priviledge.SERVANT),
    ELDERS_COORDINATOR("elders_coordinator", Priviledge.ELDER),
    SERVICE_OVERSEER("service_overseer", Priviledge.SERVANT),
    SCHOOL_OVERSEER("school_overseer", Priviledge.SERVANT),
    SOUND_SERVANT("sound_servant", Priviledge.SERVANT),
    ACCOUNTS_SERVANT("accounts_servant", Priviledge.SERVANT);

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
