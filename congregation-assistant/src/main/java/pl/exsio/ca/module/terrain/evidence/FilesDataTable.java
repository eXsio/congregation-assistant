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
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Locale;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainFile;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.field.PluploadField;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.tr_files")
public class FilesDataTable extends JPADataTable<TerrainFile, Form> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected String lastFileName;

    protected String lastMimeType;

    protected long lastSize;

    public FilesDataTable(SecurityContext security) {
        super(Form.class, new TableDataConfig(FilesDataTable.class) {
            {
                setColumnHeaders("title", "name", "created_at", "id");
                setVisibleColumns("title", "name", "createdAt", "id");
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
        this.addDownloadButtonColumn();
    }

    @Override
    protected JPAContainer<TerrainFile> createContainer() {
        JPAContainer<TerrainFile> container = super.createContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"createdAt"}, new boolean[]{false});
        return container;
    }

    @Override
    protected Table createTable(JPAContainer<TerrainFile> container) {
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

    protected void addDownloadButtonColumn() {
        dataComponent.addGeneratedColumn("", new Table.ColumnGenerator() {

            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {

                final Button download = new Button("", FontAwesome.DOWNLOAD);
                final TerrainFile terrainFile = ((EntityItem<TerrainFile>) dataComponent.getItem(itemId)).getEntity();
                Resource res = new StreamResource(new StreamSource() {

                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(terrainFile.getData());
                    }
                }, terrainFile.getName());
                FileDownloader fd = new FileDownloader(res);
                fd.extend(download);
                download.setImmediate(true);
                download.setStyleName("link");
                return download;
            }
        });
    }

    @Override
    protected float getControlsExpandRatio() {
        return 1.4f;
    }

    @Override
    protected Layout decorateForm(Form form, EntityItem<? extends TerrainFile> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        if (mode == DataTable.MODE_ADDITION) {
            form.addField("data", this.getFileField(item));
        }
        form.addField("title", this.getTitleField(item));
        form.addField("description", this.getDescriptionField(item));

        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        return formLayout;
    }

    @Override
    protected boolean canOpenItem(EntityItem<? extends TerrainFile> item) {
        return true;
    }

    private Field getFileField(EntityItem<? extends TerrainFile> item) {

        final PluploadField<byte[]> upload = new PluploadField(byte[].class);
        upload.getUploader().addFileUploadedListener(new Plupload.FileUploadedListener() {

            @Override
            public void onFileUploaded(PluploadFile file) {
                lastFileName = file.getName();
                lastMimeType = file.getType();
                lastSize = file.getSize();
            }
        });

        upload.getUploader().setCaption(t("data"));
        upload.addValidator(new NullValidator("", false));
        upload.setPropertyDataSource(item.getItemProperty("data"));
        upload.getUploader().addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                upload.getUploader().refresh();
            }
        });
        return upload;

    }

    private TextField getTitleField(EntityItem<? extends TerrainFile> item) {
        TextField title = new TextField(t("title"));
        title.setPropertyDataSource(item.getItemProperty("title"));
        title.addValidator(new NullValidator("", false));
        title.setNullRepresentation("");
        return title;
    }

    private TextArea getDescriptionField(EntityItem<? extends TerrainFile> item) {
        TextArea desc = new TextArea(t("decription"));
        desc.setPropertyDataSource(item.getItemProperty("description"));
        desc.setNullRepresentation("");
        return desc;
    }

    @Override
    protected <S extends TerrainFile> Class<S> getEntityClass() {
        return this.caEntities.getTerrainFileClass();
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
    public void beforeDataAddition(Form form, EntityItem<? extends TerrainFile> item, JPAContainer<TerrainFile> container) {
        item.getItemProperty("terrain").setValue(this.terrain);
        item.getItemProperty("mimeType").setValue(this.lastMimeType);
        item.getItemProperty("name").setValue(this.lastFileName);
        item.getItemProperty("size").setValue(this.lastSize);

    }
}
