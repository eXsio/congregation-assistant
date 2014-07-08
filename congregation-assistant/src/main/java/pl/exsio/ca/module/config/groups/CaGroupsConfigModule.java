/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.groups;

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
public class CaGroupsConfigModule extends VerticalModule {

    private transient EntityProvider serviceGroupEntityProvider;

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;


    public CaGroupsConfigModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        GroupsDataTable table = this.createGroupsDataTable();
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.serviceGroupEntityProvider);
        table.setApplicationEventPublisher(this.aep);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected GroupsDataTable createGroupsDataTable() {
        return new GroupsDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setServiceGroupEntityProvider(EntityProvider entityProvider) {
        this.serviceGroupEntityProvider = entityProvider;
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

}
