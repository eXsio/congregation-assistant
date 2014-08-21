/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.report.impl;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ContainerDataSeries;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.themes.VaadinTheme;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.TerrainNotification;
import pl.exsio.ca.model.TerrainType;
import pl.exsio.ca.model.dao.TerrainDao;
import pl.exsio.ca.model.dao.TerrainNotificationDao;
import pl.exsio.ca.module.terrain.report.Report;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;

/**
 *
 * @author exsio
 */
public class EventReportImpl extends AbstractReportImpl {

    private HorizontalLayout lastReport;

    public EventReportImpl() {
        this.setMargin(true);
        this.setSpacing(true);
    }

    @Override
    public String getName() {
        return t("ca.report.event");
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

        report.addComponent(table);
        report.addComponent(chart);
        if (this.lastReport != null) {
            this.removeComponent(this.lastReport);
        }
        this.lastReport = report;
        this.addComponent(this.lastReport);

    }

    private Chart getReportChart(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) {
        IndexedContainer container = getChartContainer(event, group, type);
        ContainerDataSeries ds = new ContainerDataSeries(container);
        ds.setYPropertyId("percent");
        ds.setNamePropertyId("label");
        ds.addAttributeToPropertyIdMapping("color", "color");
        ds.setName(t("ca.report.event.status"));

        final Chart chart = new Chart();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.PIE);
        configuration.getTitle().setText(t("ca.report.event.chart"));
        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setPointFormat("{point.y}%");
        configuration.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);

        configuration.setPlotOptions(plotOptions);
        configuration.setSeries(ds);
        chart.drawChart(configuration);
        chart.setWidth("450px");
        return chart;
    }

    private IndexedContainer getChartContainer(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        IndexedContainer container = new IndexedContainer();
        Set<Terrain> reportTerrains = this.getReportTerrains(event, group, type);
        Set<Terrain> allTerrains = this.getAllTerrains(event.getStartDate(), group, type);
        double allCount = allTerrains.size();
        double reportCount = reportTerrains.size();
        Double reportPercent = reportCount * 100 / allCount;
        Double restPercent = 100 - reportPercent;
        Color[] colors = new VaadinTheme().getColors();
        container.addContainerProperty("label", String.class, null);
        container.addContainerProperty("percent", Number.class, null);
        container.addContainerProperty("color", Color.class, null);
        Item report = container.addItem("report");
        report.getItemProperty("label").setValue(t("ca.report.event.report") + " (" + new Double(reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        report.getItemProperty("percent").setValue(reportPercent);
        report.getItemProperty("color").setValue(colors[4]);
        Item rest = container.addItem("rest");
        rest.getItemProperty("label").setValue(t("ca.report.event.rest") + " (" + new Double(allCount - reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        rest.getItemProperty("percent").setValue(restPercent);
        rest.getItemProperty("color").setValue(colors[3]);
        return container;
    }

    private Table getReportTable(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws UnsupportedFilterException {

        IndexedContainer container = getTableContainer(event, group, type);

        Table table = new Table("", container);
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        table.setConverter("notification_date", dateConverter);
        table.setVisibleColumns(new Object[]{"terrain_type", "terrain_no", "terrain_name", "notification_date", "group", "terrain_id"});
        table.setColumnHeaders(t(new String[]{"event_terrain.type", "event_terrain.no", "event_terrain.name", "event_terrain.notification_date", "event_terrain.group", "id"}));
        table.setWidth("600px");
        return table;
    }

    private IndexedContainer getTableContainer(pl.exsio.ca.model.Event event, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("terrain_no", Long.class, null);
        container.addContainerProperty("terrain_name", String.class, null);
        container.addContainerProperty("terrain_type", TerrainType.class, null);
        container.addContainerProperty("notification_date", Date.class, null);
        container.addContainerProperty("group", String.class, null);
        container.addContainerProperty("terrain_id", Long.class, null);
        for (TerrainNotification notification : this.getNotifications(event, group, type)) {
            Item item = container.addItem(notification.getId());
            TerrainAssignment assignment = notification.getAssignment();
            Terrain terrain = assignment.getTerrain();

            item.getItemProperty("terrain_no").setValue(terrain.getNo());
            item.getItemProperty("terrain_name").setValue(terrain.getName());
            item.getItemProperty("terrain_type").setValue(terrain.getType());
            item.getItemProperty("notification_date").setValue(notification.getDate());
            item.getItemProperty("group").setValue(assignment.getGroup().getCaption());
            item.getItemProperty("terrain_id").setValue(terrain.getId());
        }
        container.sort(new Object[]{"terrain_no"}, new boolean[]{true});
        return container;
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
        final ComboBox picker = new ComboBox(t("ca.report.event.pick"), container);
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
