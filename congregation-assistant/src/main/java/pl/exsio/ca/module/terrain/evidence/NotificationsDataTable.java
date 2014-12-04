/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
import static com.vaadin.addon.jpacontainer.filter.Filters.and;
import static com.vaadin.addon.jpacontainer.filter.Filters.lteq;
import static com.vaadin.addon.jpacontainer.filter.Filters.gteq;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import static com.vaadin.addon.jpacontainer.filter.Filters.joinFilter;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
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
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.notifications")
public class NotificationsDataTable extends JPADataTable<TerrainNotification, Form> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected CaEntityProviderProvider caEntityProviders;

    public NotificationsDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(NotificationsDataTable.class) {
            {
                setColumnHeaders("notification_date", "assignment", "override_group", "event", "notification_comment", "id");
                setVisibleColumns("date", "assignment", "overrideGroup", "event", "comment", "id");
            }
        }, security);
    }

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
        this.dataComponent.setConverter("date", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainNotification> createContainer() {
        JPAContainer<TerrainNotification> container = super.createContainer();
        container.addContainerFilter(joinFilter("assignment", new Filter[]{eq("terrain", this.terrain)}));
        container.sort(new Object[]{"date"}, new boolean[]{false});
        return container;
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainNotification> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        final ComboBox assignment = this.getAssignmentField(item);
        final DateField date = this.getDateField(item);
        final ComboBox event = this.getEventField(item);

        this.handleDateSelection(date, assignment, event);
        this.handleAssignmentSelectionChange(assignment, date);

        form.addField("assignment", assignment);
        form.addField("date", date);
        form.addField("overrideGroup", this.getOverrideGroupField(item));
        form.addField("event", event);
        form.addField("comment", this.getCommentField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        this.filterEvents(event, date.getValue());

        return formLayout;
    }

    @Override
    protected float getControlsExpandRatio() {
        return 1.4f;
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

    private void handleDateSelection(DateField date, final ComboBox assignment, final ComboBox event) {
        date.addValidator(new AbstractValidator<Date>(t("invalid_date")) {

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

        date.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent e) {
                filterEvents(event, (Date) e.getProperty().getValue());
            }

        });
    }

    private void filterEvents(ComboBox event, Date date) throws UnsupportedFilterException {
        JPAContainer<pl.exsio.ca.model.Event> container = (JPAContainer<pl.exsio.ca.model.Event>) event.getContainerDataSource();
        container.removeAllContainerFilters();
        if (date != null) {
            container.addContainerFilter(and(new Filter[]{lteq("startDate", date), gteq("endDate", date)}));
        } else {
            container.addContainerFilter(eq("id", -1));
        }
    }

    private TextArea getCommentField(EntityItem<? extends TerrainNotification> item) {
        TextArea comment = new TextArea(t("comment"));
        comment.setPropertyDataSource(item.getItemProperty("comment"));
        comment.setNullRepresentation("");
        return comment;
    }

    private DateField getDateField(EntityItem<? extends TerrainNotification> item) {
        DateField date = new DateField(t("date"));
        date.setPropertyDataSource(item.getItemProperty("date"));
        date.setResolution(Resolution.DAY);
        date.addValidator(new NullValidator(t("invalid_date"), false));
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private ComboBox getOverrideGroupField(EntityItem<? extends TerrainNotification> item) {
        JPAContainer<? extends ServiceGroup> groups = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.caEntityProviders.getServiceGroupEntityProvider().getEntityManager());
        groups.setEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        ComboBox overrideGroup = new ComboBox(t("override_group"), groups);
        overrideGroup.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        overrideGroup.setItemCaptionPropertyId("caption");
        overrideGroup.setPropertyDataSource(item.getItemProperty("overrideGroup"));
        overrideGroup.setConverter(new SingleSelectConverter(overrideGroup));
        return overrideGroup;
    }

    private ComboBox getEventField(EntityItem<? extends TerrainNotification> item) {
        JPAContainer<? extends pl.exsio.ca.model.Event> events = JPAContainerFactory.make(this.caEntities.getEventClass(), this.caEntityProviders.getEventEntityProvider().getEntityManager());
        events.setEntityProvider(this.caEntityProviders.getEventEntityProvider());
        events.sort(new Object[]{"startDate"}, new boolean[]{false});
        ComboBox event = new ComboBox(t("event"), events);
        event.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        event.setItemCaptionPropertyId("name");
        event.setPropertyDataSource(item.getItemProperty("event"));
        event.setConverter(new SingleSelectConverter(event));
        return event;
    }

    private ComboBox getAssignmentField(EntityItem<? extends TerrainNotification> item) throws Property.ReadOnlyException, UnsupportedFilterException {
        JPAContainer<? extends TerrainAssignment> assignments = JPAContainerFactory.make(this.caEntities.getTerrainAssignmentClass(), this.caEntityProviders.getTerrainAssignmentEntityProvider().getEntityManager());
        assignments.setEntityProvider(this.caEntityProviders.getTerrainAssignmentEntityProvider());
        assignments.addContainerFilter(eq("terrain", this.terrain));
        ComboBox assignment = new ComboBox(t("assignment"), assignments);
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
        assignment.addValidator(new NullValidator(t("invalid_assignment"), false));
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

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

    @Override
    public void dataAdded(Form form, EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }

    @Override
    public void dataUpdated(Form form, EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }

    @Override
    public void dataDeleted(EntityItem item, JPAContainer container) {
        this.updateLastNotificationDate();
    }

    protected void updateLastNotificationDate() {
        List<TerrainNotification> notifications = (List<TerrainNotification>) this.caRepositories.getTerrainNotificationRepository().findByTerrain(this.terrain);

        if (!notifications.isEmpty()) {
            TerrainNotification notification = notifications.get(0);
            this.terrain.setLastNotificationDate(notification.getDate());
            this.caRepositories.getTerrainRepository().save(this.terrain);
        } else {
            this.terrain.setLastNotificationDate(null);
            this.caRepositories.getTerrainRepository().save(this.terrain);
        }
    }

}
