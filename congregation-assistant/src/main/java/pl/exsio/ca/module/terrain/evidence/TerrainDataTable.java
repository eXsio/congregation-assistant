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
import static com.vaadin.addon.jpacontainer.filter.Filters.gteq;
import static com.vaadin.addon.jpacontainer.filter.Filters.joinFilter;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
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
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.TabbedForm;

/**
 *
 * @author exsio
 */
public class TerrainDataTable extends DataTable<Terrain, TabbedForm> {

    public static final String TRANSLATION_PREFIX = "ca.terrains.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    protected ApplicationEventPublisher aep;

    protected transient final Set<Terrain> selectedTerrains;

    protected Button quickNotification;

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
        this.selectedTerrains = new HashSet<>();
        this.quickNotification = new Button("", FontAwesome.CHECK);
        this.quickNotification.setEnabled(false);
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
        if(this.security.canWrite()) {
            this.addSelectTerrainColumn();
        }
    }

    @Override
    protected JPAContainer<Terrain> createJPAContainer() {
        JPAContainer<Terrain> container = super.createJPAContainer();
        container.sort(new Object[]{"lastNotificationDate"}, new boolean[]{true});
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
                        if (assignments != null && !assignments.isEmpty()) {
                            TerrainAssignment lastAssignment = assignments.last();
                            if(!lastAssignment.isExpired() ) {
                                return lastAssignment.getGroup().getCaption();
                            } else {
                                return "";
                            }
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
    protected HorizontalLayout decorateControls(HorizontalLayout controls) {
        controls = super.decorateControls(controls);
        final ComboBox types = this.getTypesCombo();
        final ComboBox groups = this.getGroupsCombo();
        final DateField date = this.getDateField();
        Button printCards = this.getPrintCardsButton(types, groups, date);

        this.handleFilterActions(types, groups, date);

        controls.addComponent(quickNotification);
        controls.addComponent(printCards);
        controls.addComponent(types);
        controls.addComponent(groups);
        controls.addComponent(date);
        controls.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(quickNotification, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(printCards, Alignment.MIDDLE_CENTER);

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
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        return formLayout;
    }
    
    @Override
    protected boolean canOpenItem(EntityItem<? extends Terrain> item) {
        return true;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends Terrain> item) {
        form.getTabs().addTab(this.getGroupTab(item), t(TRANSLATION_PREFIX + "group"));
        form.getTabs().addTab(this.getNotificationTab(item), t(TRANSLATION_PREFIX + "notifications"));
        form.getTabs().addTab(this.getFilesTab(item), t(TRANSLATION_PREFIX + "files"));
        form.getTabs().addTab(this.getNotesTab(item), t(TRANSLATION_PREFIX + "notes"));
    }

    private Component getGroupTab(EntityItem<? extends Terrain> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getTerrainAssignmentEntityProvider());
        return table.init();
    }

    private Component getNotificationTab(EntityItem<? extends Terrain> item) {
        NotificationsDataTable table = new NotificationsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        table.setEntityProvider(this.caEntityProviders.getTerrainNotificationEntityProvider());
        table.setTerrainAssignmentEntityProvider(this.caEntityProviders.getTerrainAssignmentEntityProvider());
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        return table.init();
    }

    private Component getFilesTab(EntityItem<? extends Terrain> item) {
        FilesDataTable table = new FilesDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getTerrainFileEntityProvider());
        return table.init();
    }

    private Component getNotesTab(EntityItem<? extends Terrain> item) {
        NotesDataTable table = new NotesDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setTerrain(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getTerrainNoteEntityProvider());
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

    protected void addSelectTerrainColumn() {
        table.addGeneratedColumn("", new ColumnGenerator() {

            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {

                final CheckBox checkBox = new CheckBox();
                checkBox.setImmediate(true);
                checkBox.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(final ValueChangeEvent event) {
                        Terrain terrain = ((EntityItem<Terrain>) table.getItem(itemId)).getEntity();
                        if ((Boolean) event.getProperty().getValue()) {
                            selectedTerrains.add(terrain);
                        } else {
                            selectedTerrains.remove(terrain);
                        }
                        quickNotification.setEnabled(!selectedTerrains.isEmpty());
                    }
                });

                return checkBox;
            }
        });
    }

    private Button getPrintCardsButton(final ComboBox types, final ComboBox groups, final DateField date) {
        Button printCards = new Button("", FontAwesome.PRINT);
        printCards.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                String type = types.getValue() == null ? "" : ((TerrainType) types.getValue()).getStringRepresentation();
                String group = groups.getValue() == null ? "" : groups.getValue().toString();
                String d = date.getValue() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date.getValue());
                getUI().getPage().open("report/pdf/?report-name=terranCardsPdfReport&type=" + type + "&group=" + group + "&date=" + d, "_blank", false);
            }
        });
        return printCards;
    }

    private void handleFilterActions(ComboBox types, ComboBox groups, DateField date) {
        Property.ValueChangeListener listener = this.getFiltersHandler(types, groups, date);
        types.addValueChangeListener(listener);
        groups.addValueChangeListener(listener);
        date.addValueChangeListener(listener);

        this.handleQuickNotificationClick();
    }

    private DateField getDateField() {
        DateField date = new DateField(t(TRANSLATION_PREFIX + "pick_date"));
        date.setResolution(Resolution.DAY);
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private ComboBox getGroupsCombo() throws UnsupportedFilterException {
        JPAContainer<ServiceGroup> groupsContainer = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.caEntityProviders.getServiceGroupEntityProvider().getEntityManager());
        groupsContainer.setEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        groupsContainer.addContainerFilter(eq("archival", false));
        final ComboBox groups = new ComboBox(t(TRANSLATION_PREFIX + "pick_group"), groupsContainer);
        groups.setConverter(new SingleSelectConverter<ServiceGroup>(groups));
        groups.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        groups.setItemCaptionPropertyId("caption");
        return groups;
    }

    private ComboBox getTypesCombo() {
        final ComboBox types = ComponentFactory.createEnumComboBox(t(TRANSLATION_PREFIX + "pick_type"), TerrainType.class);
        return types;
    }

    private Property.ValueChangeListener getFiltersHandler(final ComboBox types, final ComboBox groups, final DateField date) {
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

                if (date.getValue() != null) {
                    terrains.addContainerFilter(gteq("lastNotificationDate", date.getValue()));
                }
            }
        };
        return listener;
    }

    private void handleQuickNotificationClick() {
        quickNotification.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!selectedTerrains.isEmpty()) {
                    QuickNotifyWindow window = new QuickNotifyWindow(selectedTerrains, (JPAContainer<Terrain>) table.getContainerDataSource());
                    window.setCaEntities(caEntities);
                    window.setCaRepositories(caRepositories);
                    getUI().addWindow(window.init());
                } else {
                    Notification.show(TRANSLATION_PREFIX + "select_terrain", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
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

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider entityProviders) {
        this.caEntityProviders = entityProviders;
    }

}
