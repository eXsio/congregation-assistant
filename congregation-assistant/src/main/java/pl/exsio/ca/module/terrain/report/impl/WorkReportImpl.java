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
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

/**
 *
 * @author exsio
 */
public class WorkReportImpl extends AbstractReportImpl {

    private HorizontalLayout lastReport;

    public WorkReportImpl() {
        this.setMargin(true);
        this.setSpacing(true);
    }

    @Override
    public String getName() {
        return t("ca.report.work");
    }

    @Override
    public Report load() {
        this.removeAllComponents();
        DateField start = this.getStartField();
        DateField end = this.getEndField();
        ComboBox groups = this.getGroupsCombo();
        ComboBox types = this.getTypesCombo();

        Property.ValueChangeListener listener = this.getControlsHandler(start, end, types, groups);
        start.addValueChangeListener(listener);
        end.addValueChangeListener(listener);
        groups.addValueChangeListener(listener);
        types.addValueChangeListener(listener);
        HorizontalLayout controls = new HorizontalLayout();
        controls.setSpacing(true);
        controls.addComponent(start);
        controls.addComponent(end);
        controls.addComponent(groups);
        controls.addComponent(types);
        this.addComponentAsFirst(controls);
        this.showReport(start.getValue(), end.getValue(), null, null);
        return this;
    }

    @Override
    public Long getAclObjectId() {
        return 1l;
    }

    private void showReport(Date start, Date end, ServiceGroup group, TerrainType type) {

        HorizontalLayout report = new HorizontalLayout();
        report.setSpacing(true);

        Table table = getReportTable(start, end, group, type);
        Chart chart = getReportChart(start, end, group, type);

        report.addComponent(table);
        report.addComponent(chart);
        if (this.lastReport != null) {
            this.removeComponent(this.lastReport);
        }
        this.lastReport = report;
        this.addComponent(this.lastReport);

    }

    private Chart getReportChart(Date start, Date end, ServiceGroup group, TerrainType type) {
        IndexedContainer container = getChartContainer(start, end, group, type);
        ContainerDataSeries ds = new ContainerDataSeries(container);
        ds.setYPropertyId("percent");
        ds.setNamePropertyId("label");
        ds.addAttributeToPropertyIdMapping("color", "color");
        ds.setName(t("ca.report.work.status"));

        final Chart chart = new Chart();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.PIE);
        configuration.getTitle().setText(t("ca.report.work.chart"));
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

    private IndexedContainer getChartContainer(Date start, Date end, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        IndexedContainer container = new IndexedContainer();
        Set<Terrain> reportTerrains = this.getReportTerrains(start, end, group, type);
        Set<Terrain> allTerrains = this.getAllTerrains(start, group, type);
        double allCount = allTerrains.size();
        double reportCount = reportTerrains.size();
        Double reportPercent = reportCount * 100 / allCount;
        Double restPercent = 100 - reportPercent;
        Color[] colors = new VaadinTheme().getColors();
        container.addContainerProperty("label", String.class, null);
        container.addContainerProperty("percent", Number.class, null);
        container.addContainerProperty("color", Color.class, null);
        Item report = container.addItem("report");
        report.getItemProperty("label").setValue(t("ca.report.work.report") + " (" + new Double(reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        report.getItemProperty("percent").setValue(reportPercent);
        report.getItemProperty("color").setValue(colors[4]);
        Item rest = container.addItem("rest");
        rest.getItemProperty("label").setValue(t("ca.report.work.rest") + " (" + new Double(allCount - reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        rest.getItemProperty("percent").setValue(restPercent);
        rest.getItemProperty("color").setValue(colors[3]);
        return container;
    }

    private Table getReportTable(Date start, Date end, ServiceGroup group, TerrainType type) throws UnsupportedFilterException {

        IndexedContainer container = getTableContainer(start, end, group, type);

        Table table = new Table("", container);
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        table.setConverter("notification_date", dateConverter);
        table.setVisibleColumns(new Object[]{"terrain_type", "terrain_no", "terrain_name", "notification_date", "group", "terrain_id"});
        table.setColumnHeaders(t(new String[]{"event_terrain.type", "event_terrain.no", "event_terrain.name", "terrain.last_notification_date", "event_terrain.group", "id"}));
        table.setWidth("700px");
        return table;
    }

    private IndexedContainer getTableContainer(Date start, Date end, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("terrain_no", Long.class, null);
        container.addContainerProperty("terrain_name", String.class, null);
        container.addContainerProperty("terrain_type", TerrainType.class, null);
        container.addContainerProperty("notification_date", Date.class, null);
        container.addContainerProperty("group", String.class, null);
        container.addContainerProperty("terrain_id", Long.class, null);

        Map<Terrain, TerrainWorkItem> itemsMap = new HashMap<>();

        for (TerrainNotification notification : this.getNotifications(start, end, group, type)) {
            TerrainAssignment assignment = notification.getAssignment();
            Terrain terrain = assignment.getTerrain();
            TerrainWorkItem item = new TerrainWorkItem();
            item.setDate(notification.getDate());
            item.setGroup(assignment.getGroup().getCaption());
            item.setId(terrain.getId());
            item.setName(terrain.getName());
            item.setNo(terrain.getNo());
            item.setType(type);
            if (itemsMap.containsKey(terrain)) {
                item.setCount(item.getCount() + itemsMap.get(terrain).getCount());
            }
            itemsMap.put(terrain, item);
        }

        for (Terrain terrain : itemsMap.keySet()) {
            TerrainWorkItem workItem = itemsMap.get(terrain);
            Item item = container.addItem(workItem.getId());

            item.getItemProperty("terrain_no").setValue(workItem.getNo());
            item.getItemProperty("terrain_name").setValue(workItem.getName() + " (" + workItem.getCount() + "x)");
            item.getItemProperty("terrain_type").setValue(workItem.getType());
            item.getItemProperty("notification_date").setValue(workItem.getDate());
            item.getItemProperty("group").setValue(workItem.getGroup());
            item.getItemProperty("terrain_id").setValue(workItem.getId());
        }
        container.sort(new Object[]{"terrain_no"}, new boolean[]{true});
        return container;
    }

    private Set<TerrainNotification> getNotifications(Date start, Date end, ServiceGroup group, TerrainType type) {
        TerrainNotificationDao dao = this.caRepositories.getTerrainNotificationRepository();
        if (group == null && type == null) {
            return dao.findByDateRange(start, end);
        } else if (group != null && type == null) {
            return dao.findByDateRangeAndGroup(start, end, group);
        } else if (group != null && type != null) {
            return dao.findByDateRangeAndGroupAndTerrainType(start, end, group, type);
        } else if (group == null && type != null) {
            return dao.findByDateRangeAndTerrainType(start, end, type);
        } else {
            return null;
        }
    }

    private Set<Terrain> getReportTerrains(Date start, Date end, ServiceGroup group, TerrainType type) {
        TerrainDao dao = this.caRepositories.getTerrainRepository();
        if (group == null && type == null) {
            return dao.findByNotificationDateRange(start, end);
        } else if (group != null && type == null) {
            return dao.findByGroupAndNotificationDateRange(group, start, end);
        } else if (group != null && type != null) {
            return dao.findByTypeAndGroupAndNotificationDateRange(type, group, start, end);
        } else if (group == null && type != null) {
            return dao.findByTypeAndNotificationDateRange(type, start, end);
        } else {
            return null;
        }
    }

    protected DateField getEndField() {
        DateField end = new DateField(t("ca.report.work.end"));
        end.setResolution(Resolution.MINUTE);
        Calendar cal = Calendar.getInstance();
        end.setRequired(true);
        cal.setTime(new Date());
        end.setSizeFull();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        end.setValue(cal.getTime());
        end.setDateFormat("yyyy-MM-dd");
        return end;
    }

    protected DateField getStartField() {
        DateField start = new DateField(t("ca.report.work.start"));
        start.setResolution(Resolution.MINUTE);
        start.setSizeFull();
        start.setRequired(true);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DATE, 1);
        start.setValue(cal.getTime());
        start.setDateFormat("yyyy-MM-dd");
        return start;
    }

    private Property.ValueChangeListener getControlsHandler(final DateField start, final DateField end, final ComboBox types, final ComboBox groups) {
        return new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (start.getValue() != null && end.getValue() != null) {
                    showReport(start.getValue(), end.getValue(), (ServiceGroup) groups.getConvertedValue(), (TerrainType) types.getValue());
                }
            }
        };

    }

    private class TerrainWorkItem {

        private Long no;

        private String name;

        private TerrainType type;

        private Date date;

        private String group;

        private Long id;

        private Long count = 1l;

        public Long getNo() {
            return no;
        }

        public void setNo(Long no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TerrainType getType() {
            return type;
        }

        public void setType(TerrainType type) {
            this.type = type;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

    }

}
