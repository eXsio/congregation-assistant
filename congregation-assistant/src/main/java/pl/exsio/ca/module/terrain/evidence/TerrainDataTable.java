/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import static com.vaadin.addon.jpacontainer.filter.Filters.joinFilter;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.AclSubjectDataTable;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.TabbedForm;

/**
 *
 * @author exsio
 */
public class TerrainDataTable extends AclSubjectDataTable<Terrain, TabbedForm> {

    public static final String TRANSLATION_PREFIX = "ca.terrains.";

    protected CaEntityFactory caEntities;

    protected EntityProvider serviceGroupEntityProvider;

    protected CaRepositoryProvider caRepositories;

    protected EntityProvider terrainAssignmentEntityProvider;

    protected EntityProvider terrainNotificationEntityProvider;

    protected ApplicationEventPublisher aep;

    public TerrainDataTable(SecurityContext security) {
        super(TabbedForm.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"terrain.type", "terrain.no", "terrain.name", "terrain.last_notification", "terrain.current_group", "terrain.archival", "id"});
                setVisibleColumns(new String[]{"type", "no", "name", "lastNotificationDate", "assignments", "archival", "id"});
                setDeleteButtonLabel(TRANSLATION_PREFIX + "button.delete");
                setDeletionSuccessMessage(TRANSLATION_PREFIX + "msg.deleted");
                setDeletionWindowQuestion(TRANSLATION_PREFIX + "confirmation.delete");
                setEditButtonLabel(TRANSLATION_PREFIX + "button.edit");
                setEditionSuccessMessage(TRANSLATION_PREFIX + "msg.edited");
                setEditionWindowTitle(TRANSLATION_PREFIX + "window.edit");
                setTableCaption("");
            }
        }, security);
        this.openEditionAfterCreation = true;
    }
    
    @Override
    protected void doInit() {
        super.doInit();
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        this.table.setConverter("lastNotificationDate", dateConverter);
    }
    
    protected JPAContainer<Terrain> createJPAContainer() {
        JPAContainer<Terrain> container = super.createJPAContainer();
        container.sort(new Object[] { "lastNotificationDate" }, new boolean[] { true });
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<Terrain> container) {
        return new Table(this.config.getTableCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "assignments":
                        SortedSet<TerrainAssignment> assignments = (SortedSet<TerrainAssignment>) property.getValue();
                        if (assignments!= null && !assignments.isEmpty()) {
                            return assignments.first().getGroup().getCaption();
                        } else {
                            return "";
                        }
                    default:
                        return super.formatPropertyValue(rowId, colId, property);
                }
            }
        };
    }

    @Override
    protected HorizontalLayout decorateControls(HorizontalLayout controls) {
        controls = super.decorateControls(controls);
        final ComboBox types = ComponentFactory.createEnumComboBox(null, TerrainType.class);
        JPAContainer<ServiceGroup> groupsContainer = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.serviceGroupEntityProvider.getEntityManager());
        groupsContainer.setEntityProvider(this.serviceGroupEntityProvider);
        final ComboBox groups = new ComboBox(null, groupsContainer);
        groups.setConverter(new SingleSelectConverter<ServiceGroup>(groups));
        groups.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        groups.setItemCaptionPropertyId("caption");
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                JPAContainer<Terrain> terrains = (JPAContainer<Terrain>) table.getContainerDataSource();
                terrains.removeAllContainerFilters();
                if (types.getValue() != null) {
                    terrains.addContainerFilter(eq("type", types.getValue()));
                }

                if (groups.getValue() != null) {
                    terrains.addContainerFilter(joinFilter("assignments", new Filter[]{eq("active", true), eq("group", groups.getValue())}));
                }
            }
        };
        types.addValueChangeListener(listener);
        groups.addValueChangeListener(listener);
        controls.addComponent(types);
        controls.addComponent(groups);
        return controls;
    }

    @Override
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends Terrain> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("800px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Terrain> ff = new FramesetFieldFactory<>(this.caEntities.getTerrainClass());
        ff.setSingleSelectType(this.caEntities.getPreacherClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"no", "name", "archival"}));
        form.setBuffered(true);
        form.getField("name").addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_name"), false));
        form.getField("no").addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_no"), false));
        ComboBox type = ComponentFactory.createEnumComboBox(t(this.caEntities.getTerrainClass().getCanonicalName() + ".type"), TerrainType.class);
        type.setPropertyDataSource(item.getItemProperty("type"));
        type.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_type"), false));
        form.addField("type", type);
        form.setEnabled(true);
        formLayout.addComponent(form);
        formLayout.setMargin(true);
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        return formLayout;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends Terrain> item) {
        form.getTabs().addTab(this.getGroupTab(item), t(TRANSLATION_PREFIX + "group"));
        form.getTabs().addTab(this.getNotificationTab(item), t(TRANSLATION_PREFIX + "notifications"));
    }

    private Component getGroupTab(EntityItem<? extends Terrain> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.serviceGroupEntityProvider);
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.terrainAssignmentEntityProvider);
        return table.init();
    }

    private Component getNotificationTab(EntityItem<? extends Terrain> item) {
        NotificationsDataTable table = new NotificationsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.serviceGroupEntityProvider);
        table.setEntityProvider(this.terrainNotificationEntityProvider);
        table.setTerrainAssignmentEntityProvider(this.terrainAssignmentEntityProvider);
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        return table.init();
    }

    protected Map<String, Set<String>> getTabsConfig() {
        return new LinkedHashMap() {
            {
                put(TRANSLATION_PREFIX + "basic_data", new LinkedHashSet() {
                    {
                        add("no");
                        add("name");
                        add("type");
                        add("archival");
                    }
                });
            }
        };
    }

    @Override
    protected <S extends Terrain> Class<S> getEntityClass() {
        return this.caEntities.getTerrainClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher aep) {
        this.aep = aep;
        super.setApplicationEventPublisher(aep);
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setTerrainAssignmentEntityProvider(EntityProvider terrainAssignmentEntityProvider) {
        this.terrainAssignmentEntityProvider = terrainAssignmentEntityProvider;
    }

    public void setTerrainNotificationEntityProvider(EntityProvider terrainNotificationEntityProvider) {
        this.terrainNotificationEntityProvider = terrainNotificationEntityProvider;
    }

}
