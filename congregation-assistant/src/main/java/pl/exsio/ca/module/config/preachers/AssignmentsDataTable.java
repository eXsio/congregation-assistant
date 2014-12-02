/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;

/**
 *
 * @author exsio
 */
public class AssignmentsDataTable extends JPADataTable<PreacherAssignment, Form> {

    public static final String TRANSLATION_PREFIX = "ca.pr_assignments.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Preacher preacher;

    protected EntityProvider serviceGroupEntityProvider;

    public AssignmentsDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(TRANSLATION_PREFIX) {
            {
                setColumnHeaders("preacher.group", "preacher.assignment_date", "preacher.assignment_active", "id");
                setVisibleColumns("group", "date", "active", "id");
            }
        }, security);
    }

    @Override
    protected void doInit() {
        super.doInit();
        if (this.preacher == null) {
            this.setEnabled(false);
        }
        this.setHeight("250px");
        Converter dateConverter = new StringToDateConverter() {
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            }
        };
        this.dataComponent.setConverter("date", dateConverter);
    }

    @Override
    protected JPAContainer<PreacherAssignment> createContainer() {
        JPAContainer<PreacherAssignment> container = super.createContainer();
        container.addContainerFilter(eq("preacher", this.preacher));
        container.sort(new Object[]{"date"}, new boolean[]{false});
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<PreacherAssignment> container) {
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
    protected Layout decorateForm(Form form, EntityItem<? extends PreacherAssignment> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();
        form.addField("group", getFroupField(item));
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

    private DateField getDateField(EntityItem<? extends PreacherAssignment> item) {
        DateField date = new DateField(t(this.caEntities.getPreacherAssignmentClass().getCanonicalName() + ".date"));
        date.setPropertyDataSource(item.getItemProperty("date"));
        date.setResolution(Resolution.DAY);
        date.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_group"), false));
        date.setDateFormat("yyyy-MM-dd");
        return date;
    }

    private ComboBox getFroupField(EntityItem<? extends PreacherAssignment> item) {
        JPAContainer<? extends ServiceGroup> groups = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.serviceGroupEntityProvider.getEntityManager());
        groups.setEntityProvider(this.serviceGroupEntityProvider);
        ComboBox group = new ComboBox(t(this.caEntities.getPreacherAssignmentClass().getCanonicalName() + ".group"), groups);
        group.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        group.setItemCaptionPropertyId("caption");
        group.setPropertyDataSource(item.getItemProperty("group"));
        group.setConverter(new SingleSelectConverter(group));
        group.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_group"), false));
        return group;
    }

    @Override
    protected <S extends PreacherAssignment> Class<S> getEntityClass() {
        return this.caEntities.getPreacherAssignmentClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeDataAddition(Form form, EntityItem<? extends PreacherAssignment> item, JPAContainer<PreacherAssignment> container) {
        item.getItemProperty("preacher").setValue(this.preacher);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataAdded(Form form, EntityItem<? extends PreacherAssignment> item, JPAContainer<PreacherAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataUpdated(Form form, EntityItem<? extends PreacherAssignment> item, JPAContainer<PreacherAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void dataDeleted(EntityItem<? extends PreacherAssignment> item, JPAContainer<PreacherAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void setNewActive() {
        this.caRepositories.getPreacherAssignmentRepository().deactivateAll(this.preacher);
        PreacherAssignment latest = this.caRepositories.getPreacherAssignmentRepository().findLatest(this.preacher);
        if (latest != null) {
            this.caRepositories.getPreacherAssignmentRepository().setActive(latest.getId());
        }
    }

    public void setPreacher(Preacher preacher) {
        this.preacher = preacher;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

}
