/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.ca.model.TerrainAssignment;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.util.CalendarUtil;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;

/**
 *
 * @author exsio
 */
public class AssignmentsDataTable extends DataTable<TerrainAssignment, Form> implements DataTable.EntityCreationListener<TerrainAssignment>, DataTable.EntityUpdateListener<TerrainAssignment>, DataTable.EntityDeletionListener<TerrainAssignment> {

    public static final String TRANSLATION_PREFIX = "ca.tr_assignments.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected EntityProvider serviceGroupEntityProvider;

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
        this.table.setConverter("startDate", dateConverter);
        this.table.setConverter("endDate", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainAssignment> createJPAContainer() {
        JPAContainer<TerrainAssignment> container = super.createJPAContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"startDate"}, new boolean[]{false});
        return container;
    }

    public AssignmentsDataTable(SecurityContext security) {
        super(Form.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"terrain.group", "terrain.assignment_start_date", "terrain.assignment_end_date", "preacher.assignment_active", "id"});
                setVisibleColumns(new String[]{"group", "startDate", "endDate", "active", "id"});
                setDeleteButtonLabel(TRANSLATION_PREFIX + "button.delete");
                setDeletionSuccessMessage(TRANSLATION_PREFIX + "msg.deleted");
                setDeletionWindowQuestion(TRANSLATION_PREFIX + "confirmation.delete");
                setEditButtonLabel(TRANSLATION_PREFIX + "button.edit");
                setEditionSuccessMessage(TRANSLATION_PREFIX + "msg.edited");
                setEditionWindowTitle(TRANSLATION_PREFIX + "window.edit");
                setTableCaption("");
            }
        }, security);
        this.addEntityCreatedListener(this);
        this.addEntityUpdatedListener(this);
        this.addEntityDeletedListener(this);
    }
    
    @Override
    protected Table createTable(JPAContainer<TerrainAssignment> container) {
        return new Table(this.config.getTableCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "active":
                        if(property.getValue() != null && ((Boolean) property.getValue())) {
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
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainAssignment> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        form.addField("group", this.getGroupField(item));
        form.addField("startDate", this.getStartDateField(item));
        form.addField("endDate", this.getEndDateField(item));
        form.addField("comment", this.getCommentField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        formLayout.setMargin(true);
        return formLayout;
    }

    private TextArea getCommentField(EntityItem<? extends TerrainAssignment> item) {
        TextArea comment = new TextArea(t(this.caEntities.getTerrainAssignmentClass().getCanonicalName() + ".comment"));
        comment.setPropertyDataSource(item.getItemProperty("comment"));
        comment.setNullRepresentation("");
        return comment;
    }

    private DateField getEndDateField(EntityItem<? extends TerrainAssignment> item) {
        DateField end = new DateField(t(this.caEntities.getTerrainAssignmentClass().getCanonicalName() + ".end_date"));
        end.setPropertyDataSource(item.getItemProperty("endDate"));
        end.setResolution(Resolution.DAY);
        end.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        return end;
    }

    private DateField getStartDateField(EntityItem<? extends TerrainAssignment> item) {
        DateField start = new DateField(t(this.caEntities.getTerrainAssignmentClass().getCanonicalName() + ".start_date"));
        start.setPropertyDataSource(item.getItemProperty("startDate"));
        start.setResolution(Resolution.DAY);
        start.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_start_date"), false));
        start.setDateFormat(CalendarUtil.getDateFormat(this.getLocale()));
        return start;
    }

    private ComboBox getGroupField(EntityItem<? extends TerrainAssignment> item) throws UnsupportedFilterException {
        JPAContainer<? extends ServiceGroup> groups = JPAContainerFactory.make(this.caEntities.getServiceGroupClass(), this.serviceGroupEntityProvider.getEntityManager());
        groups.setEntityProvider(this.serviceGroupEntityProvider);
        groups.addContainerFilter(eq("archival", false));
        ComboBox group = new ComboBox(t(this.caEntities.getTerrainAssignmentClass().getCanonicalName() + ".group"), groups);
        group.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        group.setItemCaptionPropertyId("caption");
        group.setPropertyDataSource(item.getItemProperty("group"));
        group.setConverter(new SingleSelectConverter(group));
        group.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "invalid_group"), false));
        return group;
    }

    @Override
    protected <S extends TerrainAssignment> Class<S> getEntityClass() {
        return this.caEntities.getTerrainAssignmentClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeEntityCreation(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        item.getItemProperty("terrain").setValue(this.terrain);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void entityCreated(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    public void beforeEntityUpdate(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void entityUpdated(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
        this.setNewActive();
        container.refresh();
    }

    @Override
    public void beforeEntityDeletion(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void entityDeleted(EntityItem<? extends TerrainAssignment> item, JPAContainer<TerrainAssignment> container) {
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

}