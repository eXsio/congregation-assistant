/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.provider.SecurityContextProvider;
import pl.exsio.frameset.security.entity.factory.SecurityEntityFactory;
import pl.exsio.frameset.vaadin.module.VerticalModule;

/**
 *
 * @author exsio
 */
public class CaPreachersConfigModule extends VerticalModule {

    private transient EntityProvider preacherEntityProvider;

    private transient EntityProvider priviledgeEntityProvider;

    @Autowired
    private transient ApplicationEventPublisher aep;

    private transient CaEntityFactory caEntities;

    protected SecurityEntityFactory securityEntities;

    protected EntityProvider serviceGroupEntityProvider;

    private transient EntityProvider preacherAssignmentEntityProvider;

    protected CaRepositoryProvider caRepositories;

    public CaPreachersConfigModule() {
        this.setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.removeAllComponents();
        PreachersDataTable table = this.createPreachersDataTable();
        table.setCaEntities(this.caEntities);
        table.setSecurityEntities(this.securityEntities);
        table.setEntityProvider(this.preacherEntityProvider);
        table.setPriviledgeEntityProvider(this.priviledgeEntityProvider);
        table.setApplicationEventPublisher(this.aep);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.serviceGroupEntityProvider);
        table.setPreacherAssignmentEntityProvider(this.preacherAssignmentEntityProvider);
        this.addComponent(table.init());
        this.setMargin(true);
    }

    protected PreachersDataTable createPreachersDataTable() {
        return new PreachersDataTable(SecurityContextProvider.getFor(this.frame));
    }

    public void setPreacherEntityProvider(EntityProvider entityProvider) {
        this.preacherEntityProvider = entityProvider;
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setSecurityEntities(SecurityEntityFactory securityEntities) {
        this.securityEntities = securityEntities;
    }

    public void setPriviledgeEntityProvider(EntityProvider priviledgeEntotyProvider) {
        this.priviledgeEntityProvider = priviledgeEntotyProvider;
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setPreacherAssignmentEntityProvider(EntityProvider preacherAssignmentEntityProvider) {
        this.preacherAssignmentEntityProvider = preacherAssignmentEntityProvider;
    }

}
