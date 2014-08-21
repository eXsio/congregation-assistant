/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.report.impl;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import java.util.Set;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.ca.module.terrain.report.Report;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;

/**
 *
 * @author exsio
 */
public abstract class AbstractReportImpl extends VerticalLayout implements Report {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

    protected Set<Terrain> getAllTerrains(Date date, ServiceGroup group, TerrainType type) {
        TerrainDao dao = this.caRepositories.getTerrainRepository();
        if (group == null && type == null) {
            return dao.findByAssignmentDate(date);
        } else if (group != null && type == null) {
            return dao.findByGroupAndAssignmentDate(group, date);
        } else if (group != null && type != null) {
            return dao.findByTypeAndGroupAndAssignmentDate(type, group, date);
        } else if (group == null && type != null) {
            return dao.findByTypeAndAssignmentDate(type, date);
        } else {
            return null;
        }
    }
    
    protected ComboBox getGroupsCombo() throws UnsupportedFilterException {
        JPAContainer<ServiceGroup> groupsContainer = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.caEntityProviders.getServiceGroupEntityProvider().getEntityManager());
        groupsContainer.setEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        groupsContainer.addContainerFilter(eq("archival", false));
        final ComboBox groups = new ComboBox(t("ca.report.event.group"), groupsContainer);
        groups.setConverter(new SingleSelectConverter<ServiceGroup>(groups));
        groups.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        groups.setItemCaptionPropertyId("caption");
        return groups;
    }

    protected ComboBox getTypesCombo() {
        final ComboBox types = ComponentFactory.createEnumComboBox(t("ca.report.event.type"), TerrainType.class);
        return types;
    }

}
