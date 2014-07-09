/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.provider.SecurityContextProvider;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaTerrainEvidenceModule extends VerticalModule {

    private transient EntityProvider terrainEntityProvider;

    private transient EntityProvider terrainAssignmentEntityProvider;

    private transient EntityProvider terrainFileEntityProvider;

    private transient EntityProvider terrainNoteEntityProvider;

    private transient EntityProvider terrainNotificationEntityProvider;

    private transient EntityProvider serviceGroupEntityProvider;

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;

    private transient CaRepositoryProvider caRepositories;

    public CaTerrainEvidenceModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        TerrainDataTable table = this.createTerrainDataTable();
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.terrainEntityProvider);
        table.setApplicationEventPublisher(this.aep);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.serviceGroupEntityProvider);
        table.setTerrainAssignmentEntityProvider(this.terrainAssignmentEntityProvider);
        table.setTerrainNotificationEntityProvider(this.terrainNotificationEntityProvider);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected TerrainDataTable createTerrainDataTable() {
        return new TerrainDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setTerrainEntityProvider(EntityProvider terrainEntityProvider) {
        this.terrainEntityProvider = terrainEntityProvider;
    }

    public void setTerrainAssignmentEntityProvider(EntityProvider terrainAssignmentEntityProvider) {
        this.terrainAssignmentEntityProvider = terrainAssignmentEntityProvider;
    }

    public void setTerrainFileEntityProvider(EntityProvider terrainFileEntityProvider) {
        this.terrainFileEntityProvider = terrainFileEntityProvider;
    }

    public void setTerrainNoteEntityProvider(EntityProvider terrainNoteEntityProvider) {
        this.terrainNoteEntityProvider = terrainNoteEntityProvider;
    }

    public void setTerrainNotificationEntityProvider(EntityProvider terrainNotificationEntityProvider) {
        this.terrainNotificationEntityProvider = terrainNotificationEntityProvider;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

}
