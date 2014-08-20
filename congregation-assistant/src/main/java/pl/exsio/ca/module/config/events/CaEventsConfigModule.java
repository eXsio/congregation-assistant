/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.events;

import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.provider.SecurityContextProvider;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaEventsConfigModule extends VerticalModule {

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;

    protected transient CaRepositoryProvider caRepositories;

    protected transient CaEntityProviderProvider caEntityProviders;

    public CaEventsConfigModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        EventsDataTable table = this.createEventsDataTable();
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.caEntityProviders.getEventEntityProvider());
        table.setApplicationEventPublisher(this.aep);
        table.setCaRepositories(this.caRepositories);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected EventsDataTable createEventsDataTable() {
        return new EventsDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

}
