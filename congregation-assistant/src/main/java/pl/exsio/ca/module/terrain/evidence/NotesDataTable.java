/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.terrain.evidence;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import java.text.DateFormat;
import java.util.Locale;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainNote;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable.TableConfig;
import pl.exsio.frameset.vaadin.ui.support.component.JPADataTable;

/**
 *
 * @author exsio
 */
public class NotesDataTable extends JPADataTable<TerrainNote, Form> {

    public static final String TRANSLATION_PREFIX = "ca.tr_notes.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

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
        this.table.setConverter("createdAt", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainNote> createContainer() {
        JPAContainer<TerrainNote> container = super.createContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"createdAt"}, new boolean[]{false});
        return container;
    }

    public NotesDataTable(SecurityContext security) {
        super(Form.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"note.content", "note.created_at", "note.created_by", "id"});
                setVisibleColumns(new String[]{"content", "createdAt", "createdBy", "id"});
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
    }

    @Override
    protected Table createTable(JPAContainer<TerrainNote> container) {
        return new Table(this.config.getTableCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    default:
                        return super.formatPropertyValue(rowId, colId, property);
                }
            }
        };
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainNote> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        form.addField("content", this.getContentField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }

    @Override
    protected boolean canOpenItem(EntityItem<? extends TerrainNote> item) {
        return true;
    }

    private TextArea getContentField(EntityItem<? extends TerrainNote> item) {
        TextArea desc = new TextArea(t(this.caEntities.getTerrainNoteClass().getCanonicalName() + ".content"));
        desc.setPropertyDataSource(item.getItemProperty("content"));
        desc.setNullRepresentation("");
        return desc;
    }

    @Override
    protected <S extends TerrainNote> Class<S> getEntityClass() {
        return this.caEntities.getTerrainNoteClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    @Override
    public void beforeEntityCreation(EntityItem<? extends TerrainNote> item, JPAContainer<TerrainNote> container) {
        item.getItemProperty("terrain").setValue(this.terrain);

    }

    @Override
    public void entityCreated(EntityItem<? extends TerrainNote> item, JPAContainer<TerrainNote> container) {
    }

}
