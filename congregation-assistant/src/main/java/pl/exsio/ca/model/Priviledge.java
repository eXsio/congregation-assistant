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
    MAGAZINES_OVERSEER("magazines_overseer", Priviledge.SERVANT),
    TERRAINS_OVERSEER("terrains_overseer", Priviledge.SERVANT),
    LITERATURE_OVERSEER("literature_overseer", Priviledge.SERVANT),
    ELDERS_COORDINATOR("elders_coordinator", Priviledge.ELDER),
    SERVICE_OVERSEER("service_overseer", Priviledge.SERVANT),
    SOUND_OVERSEER("sound_overseer", Priviledge.SERVANT);

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

}
