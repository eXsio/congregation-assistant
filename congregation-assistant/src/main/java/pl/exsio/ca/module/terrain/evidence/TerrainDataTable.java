/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.EntityItem;
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
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.form.TabbedForm;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.terrains")
public class TerrainDataTable extends JPADataTable<Terrain, TabbedForm> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    protected transient final Set<Terrain> selectedTerrains;

    protected Button quickNotification;

    public TerrainDataTable(SecurityContext security) {
        super(TabbedForm.class, new TableDataConfig(TerrainDataTable.class) {
            {
                setColumnHeaders("type", "no", "name", "last_notification", "current_group", "archival", "id");
                setVisibleColumns("type", "no", "name", "lastNotificationDate", "assignments", "archival", "id");
            }
        }, security);
        this.openEditionAfterAddition = true;
        this.flexibleControls = true;
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
        this.dataComponent.setConverter("lastNotificationDate", dateConverter);
        if (this.security.canWrite()) {
            this.addSelectTerrainColumn();
        }
        this.dataComponent.setPageLength(30);
    }

    @Override
    protected JPAContainer<Terrain> createContainer() {
        JPAContainer<Terrain> container = super.createContainer();
        container.sort(new Object[]{"lastNotificationDate"}, new boolean[]{true});
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<Terrain> container) {
        return new Table(this.config.getCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "assignments":
                        SortedSet<TerrainAssignment> assignments = (SortedSet<TerrainAssignment>) property.getValue();
                        if (assignments != null && !assignments.isEmpty()) {
                            TerrainAssignment lastAssignment = assignments.last();
                            if (!lastAssignment.isExpired()) {
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
        if (this.security.canWrite()) {
            controls.addComponent(quickNotification);
            controls.setComponentAlignment(quickNotification, Alignment.MIDDLE_CENTER);
        }
        controls.addComponent(printCards);
        controls.addComponent(types);
        controls.addComponent(groups);
        controls.addComponent(date);
        controls.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        controls.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);

        controls.setComponentAlignment(printCards, Alignment.MIDDLE_CENTER);

        return controls;
    }

    @Override
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends Terrain> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("800px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Terrain> ff = new FramesetFieldFactory<>(this.caEntities.getTerrainClass(), this.getClass());
        ff.setSingleSelectType(this.caEntities.getPreacherClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"no", "name", "archival"}));
        form.setBuffered(true);
        form.getField("name").addValidator(new NullValidator(t("invalid_name"), false));
        form.getField("no").addValidator(new NullValidator(t("invalid_no"), false));
        ComboBox type = ComponentFactory.createEnumComboBox(t("type"), TerrainType.class);
        type.setPropertyDataSource(item.getItemProperty("type"));
        type.addValidator(new NullValidator(t("invalid_type"), false));
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
        form.getTabs().addTab(this.getGroupTab(item), t("group"));
        form.getTabs().addTab(this.getNotificationTab(item), t("notifications"));
        form.getTabs().addTab(this.getFilesTab(item), t("files"));
        form.getTabs().addTab(this.getNotesTab(item), t("notes"));
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
        table.setCaEntityProviders(this.caEntityProviders);
        table.setEntityProvider(this.caEntityProviders.getTerrainNotificationEntityProvider());
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
                put(t("basic_data"), new LinkedHashSet() {
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
        dataComponent.addGeneratedColumn("", new ColumnGenerator() {

            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {

                final CheckBox checkBox = new CheckBox();
                checkBox.setImmediate(true);
                checkBox.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(final ValueChangeEvent event) {
                        Terrain terrain = ((EntityItem<Terrain>) dataComponent.getItem(itemId)).getEntity();
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
        DateField date = new DateField(t("pick_date"));
        date.setResolution(Resolution.DAY);
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private ComboBox getGroupsCombo() throws UnsupportedFilterException {
        JPAContainer<ServiceGroup> groupsContainer = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.caEntityProviders.getServiceGroupEntityProvider().getEntityManager());
        groupsContainer.setEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        groupsContainer.addContainerFilter(eq("archival", false));
        final ComboBox groups = new ComboBox(t("pick_group"), groupsContainer);
        groups.setConverter(new SingleSelectConverter<ServiceGroup>(groups));
        groups.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        groups.setItemCaptionPropertyId("caption");
        return groups;
    }

    private ComboBox getTypesCombo() {
        final ComboBox types = ComponentFactory.createEnumComboBox(t("pick_type"), TerrainType.class);
        return types;
    }

    private Property.ValueChangeListener getFiltersHandler(final ComboBox types, final ComboBox groups, final DateField date) {
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                JPAContainer<Terrain> terrains = (JPAContainer<Terrain>) dataComponent.getContainerDataSource();
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
                    QuickNotifyWindow window = new QuickNotifyWindow(selectedTerrains, (JPAContainer<Terrain>) dataComponent.getContainerDataSource());
                    window.setCaEntities(caEntities);
                    window.setCaRepositories(caRepositories);
                    window.setCaEntityProviders(caEntityProviders);
                    getUI().addWindow(window.init());
                } else {
                    Notification.show("select_terrain", Notification.Type.ERROR_MESSAGE);
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

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider entityProviders) {
        this.caEntityProviders = entityProviders;
    }

}
