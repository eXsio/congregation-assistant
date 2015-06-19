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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Collection;
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
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.ca.module.terrain.report.Report;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.report.abstract")
public abstract class AbstractReportImpl extends VerticalLayout implements Report {

    protected static final int MODE_WORK = 0;

    protected static final int MODE_REST = 1;

    protected static final String ACTIVE_BUTTON_STYLE = "button-selected";

    protected Button rest = new Button(t("rest"));
    ;

    protected Button report = new Button(t("report"));
    ;

    protected int lastMode = MODE_WORK;

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

    protected void setWorkActive() {
        report.setStyleName(ACTIVE_BUTTON_STYLE);
        rest.removeStyleName(ACTIVE_BUTTON_STYLE);
    }

    protected void setReportActive() {
        rest.setStyleName(ACTIVE_BUTTON_STYLE);
        report.removeStyleName(ACTIVE_BUTTON_STYLE);
    }

    protected Set<Terrain> getAllTerrainsExcludingIds(Date date, ServiceGroup group, TerrainType type, Collection ids) {
        TerrainDao dao = this.caRepositories.getTerrainRepository();
        if (ids.isEmpty()) {
            ids.add(-1l);
        }
        if (group == null && type == null) {
            return dao.findByAssignmentDateExcludingIds(date, ids);
        } else if (group != null && type == null) {
            return dao.findByGroupAndAssignmentDateExcludingIds(group, date, ids);
        } else if (group != null && type != null) {
            return dao.findByTypeAndGroupAndAssignmentDateExcludingIds(type, group, date, ids);
        } else if (group == null && type != null) {
            return dao.findByTypeAndAssignmentDateExcludingIds(type, date, ids);
        } else {
            return null;
        }
    }

    protected IndexedContainer getChartContainer(double allCount, double reportCount) throws Property.ReadOnlyException {
        IndexedContainer container = new IndexedContainer();
        Double reportPercent = allCount > 0 ? reportCount * 100 / allCount : 0;
        Double restPercent = 100 - reportPercent;
        Color[] colors = new VaadinTheme().getColors();
        container.addContainerProperty("label", String.class, null);
        container.addContainerProperty("percent", Number.class, null);
        container.addContainerProperty("color", Color.class, null);
        Item report = container.addItem("report");
        report.getItemProperty("label").setValue(t("report") + " (" + new Double(reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        report.getItemProperty("percent").setValue(reportPercent);
        report.getItemProperty("color").setValue(colors[4]);
        Item rest = container.addItem("rest");
        rest.getItemProperty("label").setValue(t("rest") + " (" + new Double(allCount - reportCount).intValue() + "/" + new Double(allCount).intValue() + ")");
        rest.getItemProperty("percent").setValue(restPercent);
        rest.getItemProperty("color").setValue(colors[3]);
        container.sort(new Object[]{"terrain_no"}, new boolean[]{true});
        return container;
    }

    protected IndexedContainer getReportRestContainer(Set<Terrain> restTerrains) throws Property.ReadOnlyException {
        IndexedContainer container = prepareReportContainer();
        for (Terrain terrain : restTerrains) {
            Item item = container.addItem(terrain.getId());

            item.getItemProperty("terrain_no").setValue(terrain.getNo());
            item.getItemProperty("terrain_name").setValue(terrain.getName());
            item.getItemProperty("terrain_type").setValue(terrain.getType());
            item.getItemProperty("notification_date").setValue(terrain.getLastNotificationDate());
            if (!terrain.getAssignments().isEmpty()) {
                item.getItemProperty("group").setValue(terrain.getAssignments().last().getOwner().getCaption());
            }
            item.getItemProperty("terrain_id").setValue(terrain.getId());
        }
        return container;
    }

    protected IndexedContainer getReportWorkContainer(Map<Terrain, TerrainWorkItem> itemsMap) throws Property.ReadOnlyException {
        IndexedContainer container = prepareReportContainer();
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

    protected Map<Terrain, TerrainWorkItem> getWorkItemsMap(Set<TerrainNotification> notifications, TerrainType type) {
        Map<Terrain, TerrainWorkItem> itemsMap = new HashMap<>();
        for (TerrainNotification notification : notifications) {
            TerrainAssignment assignment = notification.getAssignment();
            Terrain terrain = assignment.getTerrain();
            TerrainWorkItem item = new TerrainWorkItem();
            item.setDate(notification.getDate());
            item.setGroup(assignment.getOwner().getCaption());
            item.setId(terrain.getId());
            item.setName(terrain.getName());
            item.setNo(terrain.getNo());
            item.setType(type);
            if (itemsMap.containsKey(terrain)) {
                item.setCount(item.getCount() + itemsMap.get(terrain).getCount());
            }
            itemsMap.put(terrain, item);
        }
        return itemsMap;
    }

    protected Chart prepareReportChart(IndexedContainer container) {
        ContainerDataSeries ds = new ContainerDataSeries(container);
        ds.setYPropertyId("percent");
        ds.setNamePropertyId("label");
        ds.addAttributeToPropertyIdMapping("color", "color");
        ds.setName(t("status"));
        final Chart chart = new Chart();
        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.PIE);
        configuration.getTitle().setText(t("chart"));
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

    protected Table prepareReportTable(IndexedContainer container) {
        Table table = new Table("", container);
        Converter dateConverter = new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        table.setConverter("notification_date", dateConverter);
        table.setVisibleColumns(new Object[]{"terrain_type", "terrain_no", "terrain_name", "notification_date", "group", "terrain_id"});
        table.setColumnHeaders(t(new String[]{"table.type", "table.no", "table.name", "table.last_notification_date", "table.owner", "table.id"}));
        table.setWidth("740px");
        return table;
    }

    protected IndexedContainer prepareReportContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("terrain_no", Long.class, null);
        container.addContainerProperty("terrain_name", String.class, "");
        container.addContainerProperty("terrain_type", TerrainType.class, null);
        container.addContainerProperty("notification_date", Date.class, null);
        container.addContainerProperty("group", String.class, "");
        container.addContainerProperty("terrain_id", Long.class, null);
        return container;
    }

    protected ComboBox getGroupsCombo() throws UnsupportedFilterException {
        JPAContainer<ServiceGroup> groupsContainer = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.caEntityProviders.getServiceGroupEntityProvider().getEntityManager());
        groupsContainer.setEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        groupsContainer.addContainerFilter(eq("archival", false));
        final ComboBox groups = new ComboBox(t("group"), groupsContainer);
        groups.setConverter(new SingleSelectConverter<ServiceGroup>(groups));
        groups.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        groups.setItemCaptionPropertyId("caption");
        return groups;
    }

    protected ComboBox getTypesCombo() {
        final ComboBox types = ComponentFactory.createEnumComboBox(t("type"), TerrainType.class);
        return types;
    }

    protected class TerrainWorkItem {

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
