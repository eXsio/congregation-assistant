/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.report.impl;

import com.vaadin.addon.charts.Chart;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Calendar;
import java.util.Date;
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
import static pl.exsio.jin.translationcontext.TranslationContext.t;

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

    private void showReport(final Date start, final Date end, final ServiceGroup group, final TerrainType type) {

        HorizontalLayout report = new HorizontalLayout();
        report.setSpacing(true);

        final Table table = getReportTable(start, end, group, type);
        Chart chart = getReportChart(start, end, group, type);

        this.handleWorkButtonClick(table, start, end, group, type);
        this.handleRestButtonClick(table, start, end, group, type);
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

    private void handleRestButtonClick(final Table table, final Date start, final Date end, final ServiceGroup group, final TerrainType type) {
        rest.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                setReportActive();
                table.setContainerDataSource(getRestTableContainer(start, end, group, type));
                lastMode = MODE_REST;
            }
        });
    }

    private void handleWorkButtonClick(final Table table, final Date start, final Date end, final ServiceGroup group, final TerrainType type) {
        report.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                setWorkActive();
                table.setContainerDataSource(getWorkTableContainer(start, end, group, type));
                lastMode = MODE_WORK;
            }
        });
    }

    private Chart getReportChart(Date start, Date end, ServiceGroup group, TerrainType type) {
        IndexedContainer container = getChartContainer(start, end, group, type);
        Chart chart = prepareReportChart(container);
        return chart;
    }

    private IndexedContainer getChartContainer(Date start, Date end, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {

        Set<Terrain> reportTerrains = this.getReportTerrains(start, end, group, type);
        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(start, end, group, type);
        Set<Long> ids = new HashSet();
        for (Terrain terrain : itemsMap.keySet()) {
            ids.add(terrain.getId());
        }
        Set<Terrain> restTerrains = this.getAllTerrainsExcludingIds(end, group, type, ids);
        double allCount = reportTerrains.size() + restTerrains.size();
        double reportCount = reportTerrains.size();

        return this.getChartContainer(allCount, reportCount);
    }

    private Table getReportTable(Date start, Date end, ServiceGroup group, TerrainType type) throws UnsupportedFilterException {
        IndexedContainer container = null;
        if (lastMode == MODE_WORK) {
            container = getWorkTableContainer(start, end, group, type);
            setWorkActive();
        } else {
            container = getRestTableContainer(start, end, group, type);
            setReportActive();
        }

        Table table = prepareReportTable(container);
        return table;
    }

    private IndexedContainer getRestTableContainer(Date start, Date end, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {

        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(start, end, group, type);
        Set<Long> ids = new HashSet();
        for (Terrain terrain : itemsMap.keySet()) {
            ids.add(terrain.getId());
        }
        Set<Terrain> restTerrains = this.getAllTerrainsExcludingIds(end, group, type, ids);
        return this.getReportRestContainer(restTerrains);
    }

    private IndexedContainer getWorkTableContainer(Date start, Date end, ServiceGroup group, TerrainType type) throws Property.ReadOnlyException {

        Map<Terrain, TerrainWorkItem> itemsMap = getWorkItemsMap(start, end, group, type);
        return this.getReportWorkContainer(itemsMap);
    }

    private Map<Terrain, TerrainWorkItem> getWorkItemsMap(Date start, Date end, ServiceGroup group, TerrainType type) {
        Set<TerrainNotification> notifications = this.getNotifications(start, end, group, type);
        return this.getWorkItemsMap(notifications, type);
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
                if (start.getValue() != null && end.getValue() != null && end.getValue().compareTo(start.getValue()) > 0) {
                    showReport(start.getValue(), end.getValue(), (ServiceGroup) groups.getConvertedValue(), (TerrainType) types.getValue());
                }
            }
        };

    }

}
