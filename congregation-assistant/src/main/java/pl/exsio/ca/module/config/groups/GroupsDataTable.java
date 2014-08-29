/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.groups;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.common.DataConfig;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.AclSubjectDataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.form.TabbedForm;

/**
 *
 * @author exsio
 */
public class GroupsDataTable extends AclSubjectDataTable<ServiceGroup, TabbedForm> {

    public static final String TRANSLATION_PREFIX = "ca.groups.";

    protected CaEntityFactory caEntities;

    protected ApplicationEventPublisher aep;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    public GroupsDataTable(SecurityContext security) {
        super(TabbedForm.class, new DataConfig(TRANSLATION_PREFIX) {
            {
                setColumnHeaders(new String[]{"group.no", "group.overseer", "group.archival", "id"});
                setVisibleColumns(new String[]{"no", "overseerAssignments", "archival", "id"});
            }
        }, security);
    }

    @Override
    protected Table createTable(JPAContainer<ServiceGroup> container) {
        return new Table(this.config.getTableCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "overseerAssignments":
                        SortedSet<OverseerAssignment> assignments = (SortedSet<OverseerAssignment>) property.getValue();
                        if (assignments != null && !assignments.isEmpty()) {
                            OverseerAssignment lastAssignment = assignments.last();
                            return lastAssignment.getPreacher().getCaption();
                        } else {
                            return "";
                        }
                    case "archival":
                        if (property.getValue() != null && ((Boolean) property.getValue())) {
                            return t("core.yes");
                        } else {
                            return t("core.no");
                        }
                    default:
                        return super.formatPropertyValue(rowId, colId, property);
                }
            }
        };
    }

    @Override
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends ServiceGroup> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("600px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Preacher> ff = new FramesetFieldFactory<>(this.caEntities.getPreacherClass());
        ff.setSingleSelectType(this.caEntities.getPreacherClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"archival"}));
        form.setBuffered(true);
        form.setEnabled(true);
        formLayout.addComponent(form);
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        return formLayout;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends ServiceGroup> item) {
        form.getTabs().addTab(this.getOverseerTab(item), t(TRANSLATION_PREFIX + "overseer"));
    }

    private Component getOverseerTab(EntityItem<? extends ServiceGroup> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setPreacherEntityProvider(this.caEntityProviders.getPreacherEntityProvider());
        table.setServiceGroup(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getOverseerAssignmentEntityProvider());
        return table.init();
    }

    protected Map<String, Set<String>> getTabsConfig() {
        return new LinkedHashMap() {
            {
                put(TRANSLATION_PREFIX + "basic_data", new LinkedHashSet() {
                    {
                        add("archival");
                    }
                });
            }
        };
    }

    @Override
    protected <S extends ServiceGroup> Class<S> getEntityClass() {
        return this.caEntities.getServiceGroupClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher aep) {
        this.aep = aep;
        super.setApplicationEventPublisher(aep);
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

}
