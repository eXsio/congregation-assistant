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
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.ca.util.XorValidator;
import pl.exsio.ca.util.XorValueChangeListener;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.util.CalendarUtil;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.tr_assignments")
public class AssignmentsDataTable extends JPADataTable<TerrainAssignment, Form> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected EntityProvider serviceGroupEntityProvider;
    
    protected EntityProvider preacherEntityProvider;

    public AssignmentsDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(AssignmentsDataTable.class) {
            {
                setColumnHeaders("owner", "assignment_start_date", "assignment_end_date", "assignment_active", "id");
                setVisibleColumns("owner", "startDate", "endDate", "active", "id");
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
        this.dataComponent.setConverter("startDate", dateConverter);
        this.dataComponent.setConverter("endDate", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainAssignment> createContainer() {
        JPAContainer<TerrainAssignment> container = super.createContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"startDate"}, new boolean[]{false});
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<TerrainAssignment> container) {
        return new Table(this.config.getCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "active":
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
    protected float getControlsExpandRatio() {
        return 1.4f;
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainAssignment> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        Field group = this.getGroupField(item);
        Field preacher = this.getPreacherField(item);

        group.addValueChangeListener(new XorValueChangeListener(preacher));
        preacher.addValueChangeListener(new XorValueChangeListener(group));
        group.addValidator(new XorValidator(preacher));
        preacher.addValidator(new XorValidator(group));
        
        form.addField("group", group);
        form.addField("preacher", preacher);
        form.addField("startDate", this.getStartDateField(item));
        form.addField("endDate", this.getEndDateField(item));
        form.addField("comment", this.getCommentField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }

    @Override
    protected boolean canOpenItem(EntityItem<? extends TerrainAssignment> item) {
        return true;
    }

    private TextArea getCommentField(EntityItem<? extends TerrainAssignment> item) {
        TextArea comment = new TextArea(t("comment"));
        comment.setPropertyDataSource(item.getItemProperty("comment"));
        comment.setNullRepresentation("");
        return comment;
    }

    private DateField getEndDateField(EntityItem<? extends TerrainAssignment> item) {
        DateField end = new DateField(t("end_date"));
        end.setPropertyDataSource(item.getItemProperty("endDate"));
        end.setResolution(Resolution.DAY);
        end.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        return end;
    }

    private DateField getStartDateField(EntityItem<? extends TerrainAssignment> item) {
        DateField start = new DateField(t("start_date"));
        start.setPropertyDataSource(item.getItemProperty("startDate"));
        start.setResolution(Resolution.DAY);
        start.addValidator(new NullValidator(t("invalid_start_date"), false));
        start.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        return start;
    }

    private ComboBox getGroupField(EntityItem<? extends TerrainAssignment> item) throws UnsupportedFilterException {
        JPAContainer<? extends ServiceGroup> groups = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.serviceGroupEntityProvider.getEntityManager());
        groups.setEntityProvider(this.serviceGroupEntityProvider);
        groups.addContainerFilter(eq("archival", false));
        ComboBox group = new ComboBox(t("group"), groups);
        group.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        group.setItemCaptionPropertyId("caption");
        group.setPropertyDataSource(item.getItemProperty("group"));
        group.setConverter(new SingleSelectConverter(group));
        group.setNullSelectionAllowed(true);
        return group;
    }
    
    private ComboBox getPreacherField(EntityItem<? extends TerrainAssignment> item) throws UnsupportedFilterException {
        JPAContainer<? extends Preacher> groups = JPAContainerFactory.make(this.caEntities.getPreacherClass(), this.preacherEntityProvider.getEntityManager());
        groups.setEntityProvider(this.preacherEntityProvider);
        ComboBox preacher = new ComboBox(t("preacher"), groups);
        preacher.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        preacher.setItemCaptionPropertyId("caption");
        preacher.setPropertyDataSource(item.getItemProperty("preacher"));
        preacher.setConverter(new SingleSelectConverter(preacher));
        preacher.setNullSelectionAllowed(true);
        return preacher;
    }

    @Override
    protected <S extends TerrainAssignment> Class<S> getEntityClass() {
        return this.caEntities.getTerrainAssignmentClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeDataAddition(Form form, EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        item.getItemProperty("terrain").setValue(this.terrain);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataAdded(Form form, EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataUpdated(Form form, EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataDeleted(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void setNewActive() {
        this.caRepositories.getTerrainAssignmentRepository().deactivateAll(this.terrain);
        TerrainAssignment latest = this.caRepositories.getTerrainAssignmentRepository().findLatest(this.terrain);
        if (latest != null) {
            this.caRepositories.getTerrainAssignmentRepository().setActive(latest.getId());
        }
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

    public void setPreacherEntityProvider(EntityProvider preacherEntityProvider) {
        this.preacherEntityProvider = preacherEntityProvider;
    }
    
    

}
