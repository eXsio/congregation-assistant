/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.preachers;

import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.provider.SecurityContextProvider;
import pl.exsio.frameset.security.entity.factory.SecurityEntityFactory;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaPreachersConfigModule extends VerticalModule {

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;

    protected transient SecurityEntityFactory securityEntities;

    protected transient CaRepositoryProvider caRepositories;

    protected transient CaEntityProviderProvider caEntityProviders;

    public CaPreachersConfigModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        PreachersDataTable table = this.createPreachersDataTable();
        table.setCaEntities(this.caEntities);
        table.setSecurityEntities(this.securityEntities);
        table.setEntityProvider(this.caEntityProviders.getPreacherEntityProvider());
        table.setApplicationEventPublisher(this.aep);
        table.setCaRepositories(this.caRepositories);
        table.setCaEntityProviders(this.caEntityProviders);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected PreachersDataTable createPreachersDataTable() {
        return new PreachersDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setSecurityEntities(SecurityEntityFactory securityEntities) {
        this.securityEntities = securityEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

}
