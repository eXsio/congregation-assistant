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
package pl.exsio.ca.module.terrain.report.impl;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.dao.TerrainNotificationDao;
import pl.exsio.ca.module.terrain.report.Report;
import static pl.exsio.ca.module.terrain.report.impl.AbstractReportImpl.MODE_REST;
import pl.exsio.jin.annotation.TranslationPrefix;
import static pl.exsio.jin.translationcontext.TranslationContext.t;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.report.event")
public class EventReportImpl extends AbstractReportImpl {

    private HorizontalLayout lastReport;

    public EventReportImpl() {
        this.setMargin(true);
        this.setSpacing(true);
    }

    @Override
    public String getName() {
        return t("name");
    }

    @Override
    public Report load() {
        this.removeAllComponents();
        ComboBox events = this.getEventsCombo();
        ComboBox groups = this.getGroupsCombo();
        ComboBox types = this.getTypesCombo();

        Property.ValueChangeListener listener = this.getControlsHandler(events, types, groups);
        events.addValueChangeListener(listener);
        groups.addValueChangeListener(listener);
        types.addValueChangeListener(listener);
        HorizontalLayout controls = new HorizontalLayout();
        controls.setSpacing(true);
        controls.addComponent(events);
        controls.addComponent(groups);
        controls.addComponent(types);
        this.addComponentAsFirst(controls);
        this.showReport((pl.exsio.ca.model.Event) events.getConvertedValue(), null, null);
        return this;
    }

    @Override
    public Long getAclObjectId() {
        return 1l;
    }

    private void showReport(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {

        HorizontalLayout report = new HorizontalLayout();
        report.setSpacing(true);

        Table table = getReportTable(event, group, type);
        Chart chart = getReportChart(event, group, type);

        this.handleWorkButtonClick(table, event, group, type);
        this.handleRestButtonClick(table, event, group, type);
        VerticalLayout tableContainer = this.createTableContainer(table);

        report.addComponent(tableContainer);
        report.addComponent(chart);
        if (this.lastReport != null) {
            this.removeComponent(this.lastReport);
        }
        this.lastReport = report;
        this.addComponent(this.lastReport);

    }

    private VerticalLayout createTableContainer(final Table table) {
        final HorizontalLayout tableControls = new HorizontalLayout() {
            {
                setSpacing(true);
                addComponent(report);
                addComponent(rest);
            }
        };
        VerticalLayout tableContainer = new VerticalLayout() {
            {
                setSpacing(false);
                setMargin(new MarginInfo(true, false, false, false));
                addComponent(tableControls);
                addComponent(table);
            }
        };
        return tableContainer;
    }

    private void handleRestButtonClick(final Table table, final pl.exsio.ca.model.Event event, final ServiceGroup group, final TerrainType type) {
        rest.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent e) {
                setReportActive();
                table.setContainerDataSource(getRestTableContainer(event, group, type));
                lastMode = MODE_REST;
            }
        });
    }

    private void handleWorkButtonClick(final Table table, final pl.exsio.ca.model.Event event, final ServiceGroup group, final TerrainType type) {
        report.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent e) {
                setWorkActive();
                table.setContainerDataSource(getWorkTableContainer(event, group, type));
                lastMode = MODE_WORK;
            }
        });
    }

    private Chart getReportChart(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {
        IndexedContainer container = getChartContainer(event, group, type);
        Chart chart = prepareReportChart(container);
        return chart;
    }

    private IndexedContainer getChartContainer(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        Set<Terrain> reportTerrains = this.getReportTerrains(event, group, type);
        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(event, group, type);
        Set<Long> ids = new HashSet();
        for (Terrain terrain : itemsMap.keySet()) {
            ids.add(terrain.getId());
        }

        Set<Terrain> restTerrains = this.getAllTerrainsExcludingIds(event.getEndDate(), group, type, ids);
        double allCount = reportTerrains.size() + restTerrains.size();
        double reportCount = reportTerrains.size();

        return this.getChartContainer(allCount, reportCount);
    }

    private Table getReportTable(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws UnsupportedFilterException {
        IndexedContainer container = null;
        if (lastMode == MODE_WORK) {
            container = getWorkTableContainer(event, group, type);
            setWorkActive();
        } else {
            container = getRestTableContainer(event, group, type);
            setReportActive();
        }

        Table table = prepareReportTable(container);
        return table;
    }

    private IndexedContainer getRestTableContainer(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(event, group, type);

        Set<Long> ids = new HashSet();
        for (Terrain terrain : itemsMap.keySet()) {
            ids.add(terrain.getId());
        }
        Set<Terrain> restTerrains = this.getAllTerrainsExcludingIds(event.getEndDate(), group, type, ids);
        return this.getReportRestContainer(restTerrains);
    }

    private IndexedContainer getWorkTableContainer(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(event, group, type);
        return this.getReportWorkContainer(itemsMap);
    }

    private Map<Terrain, TerrainWorkItem> getWorkItemsMap(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {
        Set<TerrainNotification> notifications = this.getNotifications(event, group, type);
        return this.getWorkItemsMap(notifications, type);
    }

    private Set<TerrainNotification> getNotifications(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {
        TerrainNotificationDao dao = this.caRepositories.getTerrainNotificationRepository();
        if (event != null && group == null && type == null) {
            return dao.findByEvent(event);
        } else if (event != null && group != null && type == null) {
            return dao.findByEventAndGroup(event, group);
        } else if (event != null && group != null && type != null) {
            return dao.findByEventAndGroupAndTerrainType(event, group, type);
        } else if (event != null && group == null && type != null) {
            return dao.findByEventAndTerrainType(event, type);
        } else {
            return null;
        }
    }

    private Set<Terrain> getReportTerrains(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {
        TerrainDao dao = this.caRepositories.getTerrainRepository();
        if (event != null && group == null && type == null) {
            return dao.findByEvent(event);
        } else if (event != null && group != null && type == null) {
            return dao.findByGroupAndEvent(group, event);
        } else if (event != null && group != null && type != null) {
            return dao.findByTypeAndGroupAndEvent(type, group, event);
        } else if (event != null && group == null && type != null) {
            return dao.findByTypeAndEvent(type, event);
        } else {
            return null;
        }
    }

    private ComboBox getEventsCombo() {
        JPAContainer<pl.exsio.ca.model.Event> container = JPAContainerFactory.make(this.caEntities.getEventClass(), this.caEntityProviders.getEventEntityProvider().getEntityManager());
        container.setEntityProvider(this.caEntityProviders.getEventEntityProvider());
        container.sort(new Object[]{"startDate"}, new boolean[]{false});
        final ComboBox picker = new ComboBox(t("pick"), container);
        picker.setNullSelectionAllowed(false);
        picker.setConverter(new SingleSelectConverter<pl.exsio.ca.model.Event>(picker));
        if (!picker.getItemIds().isEmpty()) {
            picker.select(picker.getItemIds().toArray()[0]);
        }
        picker.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        picker.setItemCaptionPropertyId("name");
        return picker;
    }

    private Property.ValueChangeListener getControlsHandler(final ComboBox events, final ComboBox types, final ComboBox groups) {
        return new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                showReport((pl.exsio.ca.model.Event) events.getConvertedValue(), (ServiceGroup) groups.getConvertedValue(), (TerrainType) types.getValue());
            }
        };

    }

}
