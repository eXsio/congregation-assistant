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
import static com.vaadin.addon.jpacontainer.filter.Filters.joinFilter;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;

/**
 *
 * @author exsio
 */
public class NotificationsDataTable extends DataTable<TerrainNotification, Form> implements DataTable.EntityCreationListener, DataTable.EntityUpdateListener, DataTable.EntityDeletionListener {

    public static final String TRANSLATION_PREFIX = "ca.notifications.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected EntityProvider serviceGroupEntityProvider;

    protected EntityProvider terrainAssignmentEntityProvider;

    @Override
    protected void doInit() {
        super.doInit();
        if (this.terrain == null) {
            this.setEnabled(false);
        }
        this.setHeight("250px");
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        this.table.setConverter("date", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainNotification> createJPAContainer() {
        JPAContainer<TerrainNotification> container = super.createJPAContainer();
        container.addContainerFilter(joinFilter("assignment", new Filter[]{eq("terrain", this.terrain)}));
        container.sort(new Object[]{"date"}, new boolean[]{false});
        return container;
    }

    public NotificationsDataTable(SecurityContext security) {
        super(Form.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"terrain.notification_date", "terrain.assignment",  "terrain.override_group", "terrain.notification_comment", "id"});
                setVisibleColumns(new String[]{"date", "assignment",  "overrideGroup", "comment", "id"});
                setDeleteButtonLabel(TRANSLATION_PREFIX + "button.delete");
                setDeletionSuccessMessage(TRANSLATION_PREFIX + "msg.deleted");
                setDeletionWindowQuestion(TRANSLATION_PREFIX + "confirmation.delete");
                setEditButtonLabel(TRANSLATION_PREFIX + "button.edit");
                setEditionSuccessMessage(TRANSLATION_PREFIX + "msg.edited");
                setEditionWindowTitle(TRANSLATION_PREFIX + "window.edit");
                setTableCaption("");
            }
        }, security);
        this.addEntityCreatedListener(this);
        this.addEntityDeletedListener(this);
        this.addEntityUpdatedListener(this);
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainNotification> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        final ComboBox assignment = this.getAssignmentField(item);
        form.addField("assignment", assignment);
        final DateField date = this.getDateField(item);
        this.handleDateSelection(date, assignment);
        this.handleAssignmentSelectionChange(assignment, date);
        form.addField("date", date);
        form.addField("overrideGroup", this.getOverrideGroupField(item));
        form.addField("comment", this.getCommentField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }
    
    @Override
    protected boolean canOpenItem(EntityItem<? extends TerrainNotification> item) {
        return true;
    }

    private void handleAssignmentSelectionChange(final ComboBox assignment, final DateField date) {
        assignment.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                date.validate();
            }
        });
    }

    private void handleDateSelection(DateField date, final ComboBox assignment) {
        date.addValidator(new AbstractValidator<Date>(t(TRANSLATION_PREFIX + "invalid_date")) {

            @Override
            protected boolean isValidValue(Date value) {
                EntityItem<? extends TerrainAssignment> selectedAssignmentItem = (EntityItem<? extends TerrainAssignment>) assignment.getContainerDataSource().getItem(assignment.getValue());
                if (selectedAssignmentItem != null) {
                    TerrainAssignment selectedAssignment = selectedAssignmentItem.getEntity();
                    try {
                        new DateRangeValidator(this.getErrorMessage(), selectedAssignment.getStartDate(), selectedAssignment.getEndDate(), Resolution.DAY).validate(value);
                        return true;
                    } catch (InvalidValueException ex) {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public Class<Date> getType() {
                return Date.class;
            }

        });
    }

    private TextArea getCommentField(EntityItem<? extends TerrainNotification> item) {
        TextArea comment = new TextArea(t(this.caEntities.getTerrainNotificationClass().getCanonicalName() + ".comment"));
        comment.setPropertyDataSource(item.getItemProperty("comment"));
        comment.setNullRepresentation("");
        return comment;
    }

    private DateField getDateField(EntityItem<? extends TerrainNotification> item) {
        DateField date = new DateField(t(this.caEntities.getTerrainNotificationClass().getCanonicalName() + ".date"));
        date.setPropertyDataSource(item.getItemProperty("date"));
        date.setResolution(Resolution.DAY);
        date.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_date"), false));
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private ComboBox getOverrideGroupField(EntityItem<? extends TerrainNotification> item) {
        JPAContainer<? extends ServiceGroup> groups = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.serviceGroupEntityProvider.getEntityManager());
        groups.setEntityProvider(this.serviceGroupEntityProvider);
        ComboBox overrideGroup = new ComboBox(t(this.caEntities.getTerrainNotificationClass().getCanonicalName() + ".override_group"), groups);
        overrideGroup.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        overrideGroup.setItemCaptionPropertyId("caption");
        overrideGroup.setPropertyDataSource(item.getItemProperty("overrideGroup"));
        overrideGroup.setConverter(new SingleSelectConverter(overrideGroup));
        return overrideGroup;
    }

    private ComboBox getAssignmentField(EntityItem<? extends TerrainNotification> item) throws Property.ReadOnlyException, UnsupportedFilterException {
        JPAContainer<? extends TerrainAssignment> assignments = JPAContainerFactory.make(this.caEntities.getTerrainAssignmentClass(), this.terrainAssignmentEntityProvider.getEntityManager());
        assignments.setEntityProvider(this.terrainAssignmentEntityProvider);
        assignments.addContainerFilter(eq("terrain", this.terrain));
        ComboBox assignment = new ComboBox(t(this.caEntities.getTerrainNotificationClass().getCanonicalName() + ".assignment"), assignments);
        assignment.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        assignment.setItemCaptionPropertyId("caption");
        assignment.setPropertyDataSource(item.getItemProperty("assignment"));
        assignment.setConverter(new SingleSelectConverter(assignment));
        if (item.getItemProperty("assignment").getValue() == null) {
            TerrainAssignment activeAssignment = this.caRepositories.getTerrainAssignmentRepository().findActive(this.terrain);
            if (activeAssignment instanceof TerrainAssignment) {
                assignment.setValue(activeAssignment.getId());
            }
        }
        assignment.setNullSelectionAllowed(false);
        assignment.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_assignment"), false));
        return assignment;
    }

    @Override
    protected <S extends TerrainNotification> Class<S> getEntityClass() {
        return this.caEntities.getTerrainNotificationClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

    public void setTerrainAssignmentEntityProvider(EntityProvider terrainAssignmentEntityProvider) {
        this.terrainAssignmentEntityProvider = terrainAssignmentEntityProvider;
    }

    @Override
    public void beforeEntityCreation(EntityItem item, JPAContainer container) {
    }

    @Override
    public void entityCreated(EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }

    @Override
    public void beforeEntityUpdate(EntityItem item, JPAContainer container) {
    }

    @Override
    public void entityUpdated(EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }

    @Override
    public void beforeEntityDeletion(EntityItem item, JPAContainer container) {
    }

    @Override
    public void entityDeleted(EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }
    
    protected void updateLastNotificationDate() {
        List<TerrainNotification> notifications = (List<TerrainNotification>) this.caRepositories.getTerrainNotificationRepository().findByTerrain(this.terrain);
  
        if(!notifications.isEmpty()) {
            TerrainNotification notification = notifications.get(0);
            this.terrain.setLastNotificationDate(notification.getDate());
            this.caRepositories.getTerrainRepository().save(this.terrain);
        } else {
           this.terrain.setLastNotificationDate(null);
           this.caRepositories.getTerrainRepository().save(this.terrain);
        }
    }

}
