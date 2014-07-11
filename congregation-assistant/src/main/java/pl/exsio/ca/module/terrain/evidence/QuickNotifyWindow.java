/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
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
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
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

    protected final JPAContainer<Terrain> terrainsContainer;

    public QuickNotifyWindow(Set<Terrain> terrainsToNotify, JPAContainer<Terrain> terrainsContainer) {
        super(t(TRANSLATION_PREFIX + "title"));
        this.terrainsToNotify = terrainsToNotify;
        this.terrainsContainer = terrainsContainer;
    }

    @Override
    protected void doInit() {
        this.setHeight("200px");
        this.setWidth("250px");
        final DateField date = new DateField("");
        date.setDateFormat("yyyy-MM-dd");
        date.setResolution(Resolution.DAY);

        Button save = new Button("", FontAwesome.FLOPPY_O);
        Button cancel = new Button("", FontAwesome.TIMES);

        save.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                save(date.getValue());
            }
        });

        cancel.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponent(save);
        controls.addComponent(cancel);
        controls.setMargin(true);
        controls.setSpacing(true);

        layout.setSpacing(true);
        layout.addComponent(date);
        layout.setMargin(true);
        layout.addComponent(controls);
        this.setModal(true);
        this.setResizable(false);
        this.setDraggable(false);
        this.setContent(layout);

    }

    protected void save(Date date) {
        Set<Terrain> wrongDates = new HashSet();
        for (Terrain terrain : this.terrainsToNotify) {

            List<TerrainAssignment> assignments = (List<TerrainAssignment>) this.caRepositories.getTerrainAssignmentRepository().findByTerrainAndDate(terrain, date);
            if (!assignments.isEmpty()) {
                TerrainNotification notification = this.caEntities.newTerrainNotification();
                notification.setAssignment(assignments.get(0));
                notification.setDate(date);
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
        this.close();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
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
