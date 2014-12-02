/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.and;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import static com.vaadin.addon.jpacontainer.filter.Filters.gteq;
import static com.vaadin.addon.jpacontainer.filter.Filters.lteq;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.vaadin.component.InitializableWindow;

/**
 *
 * @author exsio
 */
public class QuickNotifyWindow extends InitializableWindow {

    protected static final String TRANSLATION_PREFIX = "ca.qnotify.";

    protected final Set<Terrain> terrainsToNotify;

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    protected final JPAContainer<Terrain> terrainsContainer;

    public QuickNotifyWindow(Set<Terrain> terrainsToNotify, JPAContainer<Terrain> terrainsContainer) {
        super(t(TRANSLATION_PREFIX + "title"));
        this.terrainsToNotify = terrainsToNotify;
        this.terrainsContainer = terrainsContainer;
    }

    @Override
    protected void doInit() {
        this.setHeight("240px");
        this.setWidth("280px");

        final DateField date = this.getDateField();
        final ComboBox event = this.getEventField();

        this.filterEvents(event, date.getValue());
        this.handleDateSelection(date, event);

        Button save = getSaveButton(date, event);
        Button cancel = getCancelButton();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponent(save);
        controls.addComponent(cancel);
        controls.setMargin(true);
        controls.setSpacing(true);

        layout.setSpacing(true);
        layout.addComponent(date);
        layout.addComponent(event);
        layout.setMargin(true);
        layout.addComponent(controls);
        this.setModal(true);
        this.setResizable(false);
        this.setDraggable(false);
        this.setContent(layout);

    }

    private Button getCancelButton() {
        Button cancel = new Button("", FontAwesome.TIMES);
        cancel.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        return cancel;
    }

    private Button getSaveButton(final DateField date, final ComboBox event) {
        Button save = new Button("", FontAwesome.FLOPPY_O);
        save.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent e) {
                save(date.getValue(), (pl.exsio.ca.model.Event) event.getConvertedValue());
            }
        });
        return save;
    }

    private void handleDateSelection(DateField date, final ComboBox event) {
        date.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent e) {
                filterEvents(event, (Date) e.getProperty().getValue());
            }
        });
    }

    private DateField getDateField() {
        final DateField date = new DateField("");
        date.setDateFormat("yyyy-MM-dd");
        date.setResolution(Resolution.DAY);
        return date;
    }

    private ComboBox getEventField() {
        JPAContainer<? extends pl.exsio.ca.model.Event> events = JPAContainerFactory.make(this.caEntities.getEventClass(), this.caEntityProviders.getEventEntityProvider().getEntityManager());
        events.setEntityProvider(this.caEntityProviders.getEventEntityProvider());
        events.sort(new Object[]{"startDate"}, new boolean[]{false});
        ComboBox event = new ComboBox("", events);
        event.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        event.setItemCaptionPropertyId("name");
        event.setConverter(new SingleSelectConverter(event));
        return event;
    }

    private void filterEvents(ComboBox event, Date date) throws UnsupportedFilterException {
        JPAContainer<pl.exsio.ca.model.Event> container = (JPAContainer<pl.exsio.ca.model.Event>) event.getContainerDataSource();
        container.removeAllContainerFilters();
        if (date != null) {
            container.addContainerFilter(and(new Container.Filter[]{lteq("startDate", date), gteq("endDate", date)}));
        } else {
            container.addContainerFilter(eq("id", -1));
        }
    }

    protected void save(Date date, pl.exsio.ca.model.Event event) {
        Set<Terrain> wrongDates = new HashSet();
        for (Terrain terrain : this.terrainsToNotify) {

            List<TerrainAssignment> assignments = (List<TerrainAssignment>) this.caRepositories.getTerrainAssignmentRepository().findByTerrainAndDate(terrain, date);
            if (!assignments.isEmpty()) {
                TerrainNotification notification = this.caEntities.newTerrainNotification();
                notification.setAssignment(assignments.get(0));
                notification.setDate(date);
                notification.setEvent(event);
                this.caRepositories.getTerrainNotificationRepository().save(notification);
                this.updateLastNotificationDate(terrain);
            } else {
                wrongDates.add(terrain);
            }
        }
        if (!wrongDates.isEmpty()) {
            Notification.show(t(TRANSLATION_PREFIX + "wrong_dates") + ": " + wrongDates.toString(), Notification.Type.ERROR_MESSAGE);
        } else {
            Notification.show(t(TRANSLATION_PREFIX + "success"));
        }
        this.terrainsContainer.refresh();
        this.terrainsToNotify.clear();
        this.close();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

    protected void updateLastNotificationDate(Terrain terrain) {
        List<TerrainNotification> notifications = (List<TerrainNotification>) this.caRepositories.getTerrainNotificationRepository().findByTerrain(terrain);

        if (!notifications.isEmpty()) {
            TerrainNotification notification = notifications.get(0);
            terrain.setLastNotificationDate(notification.getDate());
            this.caRepositories.getTerrainRepository().save(terrain);
        } else {
            terrain.setLastNotificationDate(null);
            this.caRepositories.getTerrainRepository().save(terrain);
        }
    }

}
