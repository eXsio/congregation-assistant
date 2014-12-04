/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.security.userdetails.UserDetailsProvider;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.tr_notes")
public class NotesDataTable extends JPADataTable<TerrainNote, Form> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    public NotesDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(NotesDataTable.class) {
            {
                setColumnHeaders("content", "created_at", "created_by", "id");
                setVisibleColumns("content", "createdAt", "createdBy", "id");
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
        this.dataComponent.setConverter("createdAt", dateConverter);
    }

    @Override
    protected JPAContainer<TerrainNote> createContainer() {
        JPAContainer<TerrainNote> container = super.createContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"createdAt"}, new boolean[]{false});
        return container;
    }

    @Override
    protected boolean canAddItem() {
        return true;
    }

    @Override
    protected boolean canOpenItem(EntityItem<? extends TerrainNote> item) {
        return true;
    }

    @Override
    protected boolean canSaveItem(EntityItem<? extends TerrainNote> item) {
        return item.getEntity().getCreatedBy().equals(UserDetailsProvider.getUserDetails().getUsername()) || this.security.canWrite();
    }

    @Override
    protected boolean canDeleteItem(EntityItem<? extends TerrainNote> item) {
        return item.getEntity().getCreatedBy().equals(UserDetailsProvider.getUserDetails().getUsername()) || this.security.canDelete();
    }

    @Override
    protected Table createTable(JPAContainer<TerrainNote> container) {
        return new Table(this.config.getCaption(), container) {

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
    protected float getControlsExpandRatio() {
        return 1.4f;
    }

    private TextArea getContentField(EntityItem<? extends TerrainNote> item) {
        TextArea desc = new TextArea(t("content"));
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
    public void beforeDataAddition(Form form, EntityItem<? extends TerrainNote> item, JPAContainer<TerrainNote> container) {
        item.getItemProperty("terrain").setValue(this.terrain);

    }

}
