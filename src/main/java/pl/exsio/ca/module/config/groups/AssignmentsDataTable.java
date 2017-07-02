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
package pl.exsio.ca.module.config.groups;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
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
@TranslationPrefix("ca.ov_assignments")
public class AssignmentsDataTable extends JPADataTable<OverseerAssignment, Form> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected ServiceGroup group;

    protected EntityProvider preacherEntityProvider;

    public AssignmentsDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(AssignmentsDataTable.class) {
            {
                setColumnHeaders("preacher", "group_no", "assignment_start_date", "assignment_active", "id");
                setVisibleColumns("preacher", "groupNo", "date", "active", "id");
            }
        }, security);
    }

    @Override
    protected void doInit() {
        super.doInit();
        if (this.group == null) {
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
    protected JPAContainer<OverseerAssignment> createContainer() {
        JPAContainer<OverseerAssignment> container = super.createContainer();
        container.addContainerFilter(eq("group", this.group));
        container.sort(new Object[]{"date"}, new boolean[]{false});
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<OverseerAssignment> container) {
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
    protected Layout decorateForm(Form form, EntityItem<? extends OverseerAssignment> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();
        form.addField("preacher", getPreacherField(item));
        form.addField("groupNo", getGroupNoField(item));
        form.addField("date", getDateField(item));
        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }

    @Override
    protected float getControlsExpandRatio() {
        return 1.4f;
    }

    private DateField getDateField(EntityItem<? extends OverseerAssignment> item) {
        DateField date = new DateField(t("date"));
        date.setPropertyDataSource(item.getItemProperty("date"));
        date.setResolution(Resolution.DAY);
        date.addValidator(new NullValidator(t("invalid_date"), false));
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private TextField getGroupNoField(EntityItem<? extends OverseerAssignment> item) {
        TextField groupNo = new TextField(t("groupNo"));
        groupNo.setPropertyDataSource(item.getItemProperty("groupNo"));
        groupNo.setConverter(new StringToLongConverter());
        groupNo.addValidator(new NullValidator(t("invalid_group_no"), false));
        return groupNo;
    }

    private ComboBox getPreacherField(EntityItem<? extends OverseerAssignment> item) {
        JPAContainer<? extends Preacher> groups = JPAContainerFactory.make(this.caEntities.getPreacherClass(), this.preacherEntityProvider.getEntityManager());
        groups.setEntityProvider(this.preacherEntityProvider);
        ComboBox group = new ComboBox(t("preacher"), groups);
        group.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        group.setItemCaptionPropertyId("caption");
        group.setPropertyDataSource(item.getItemProperty("preacher"));
        group.setConverter(new SingleSelectConverter(group));
        group.addValidator(new NullValidator(t("invalid_preacher"), false));
        return group;
    }

    @Override
    protected <S extends OverseerAssignment> Class<S> getEntityClass() {
        return this.caEntities.getOverseerAssignmentClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeDataAddition(Form form, EntityItem<? extends OverseerAssignment> item, JPAContainer<OverseerAssignment> container) {
        item.getItemProperty("group").setValue(this.group);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataAdded(Form form, EntityItem<? extends OverseerAssignment> item, JPAContainer<OverseerAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataUpdated(Form form, EntityItem<? extends OverseerAssignment> item, JPAContainer<OverseerAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataDeleted(EntityItem<? extends OverseerAssignment> item, JPAContainer<OverseerAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void setNewActive() {
        this.caRepositories.getOverseerAssignmentRepository().deactivateAll(this.group);
        OverseerAssignment latest = this.caRepositories.getOverseerAssignmentRepository().findLatest(this.group);
        if (latest != null) {
            this.caRepositories.getOverseerAssignmentRepository().setActive(latest.getId());
        }
    }

    public void setServiceGroup(ServiceGroup group) {
        this.group = group;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setPreacherEntityProvider(EntityProvider preacherEntityProvider) {
        this.preacherEntityProvider = preacherEntityProvider;
    }

}
