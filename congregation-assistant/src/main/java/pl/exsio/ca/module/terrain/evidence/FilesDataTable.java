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
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
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
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.easyuploads.UploadField.StorageMode;
import pl.exsio.ca.model.Terrain;
import pl.exsio.ca.model.TerrainFile;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable.TableConfig;
import pl.exsio.frameset.vaadin.ui.support.component.JPADataTable;

/**
 *
 * @author exsio
 */
public class FilesDataTable extends JPADataTable<TerrainFile, Form> {

    public static final String TRANSLATION_PREFIX = "ca.tr_files.";

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected Terrain terrain;

    protected UploadField uploadField;

    protected String lastFileName;

    protected String lastMimeType;

    protected long lastSize;

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
        this.addDownloadButtonColumn();
    }

    @Override
    protected JPAContainer<TerrainFile> createContainer() {
        JPAContainer<TerrainFile> container = super.createContainer();
        container.addContainerFilter(eq("terrain", this.terrain));
        container.sort(new Object[]{"createdAt"}, new boolean[]{false});
        return container;
    }

    public FilesDataTable(SecurityContext security) {
        super(Form.class, new TableConfig(TRANSLATION_PREFIX) {
            {
                setColumnHeaders(new String[]{"file.title", "file.name", "file.created_at", "id"});
                setVisibleColumns(new String[]{"title", "name", "createdAt", "id"});
            }
        }, security);
        this.addEntityCreatedListener(this);
    }

    @Override
    protected Table createTable(JPAContainer<TerrainFile> container) {
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

    protected void addDownloadButtonColumn() {
        table.addGeneratedColumn("", new Table.ColumnGenerator() {

            @Override
            public Component generateCell(final Table source, final Object itemId, final Object columnId) {

                final Button download = new Button("", FontAwesome.DOWNLOAD);
                final TerrainFile terrainFile = ((EntityItem<TerrainFile>) table.getItem(itemId)).getEntity();
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

    private UploadField getFileField(EntityItem<? extends TerrainFile> item) {
        this.uploadField = new UploadField() {
            @Override
            protected String getDisplayDetails() {
                lastFileName = this.getLastFileName();
                lastMimeType = this.getLastMimeType();
                lastSize = this.getLastFileSize();
                return this.getLastFileName();
            }

        };
        this.uploadField.setButtonCaption(t(this.caEntities.getTerrainFileClass().getCanonicalName() + ".data"));
        this.uploadField.setStorageMode(StorageMode.MEMORY);
        this.uploadField.setFieldType(FieldType.BYTE_ARRAY);
        this.uploadField.addValidator(new NullValidator("", false));
        this.uploadField.setPropertyDataSource(item.getItemProperty("data"));
        return this.uploadField;
    }

    private TextField getTitleField(EntityItem<? extends TerrainFile> item) {
        TextField title = new TextField(t(this.caEntities.getTerrainFileClass().getCanonicalName() + ".title"));
        title.setPropertyDataSource(item.getItemProperty("title"));
        title.addValidator(new NullValidator("", false));
        title.setNullRepresentation("");
        return title;
    }

    private TextArea getDescriptionField(EntityItem<? extends TerrainFile> item) {
        TextArea desc = new TextArea(t(this.caEntities.getTerrainFileClass().getCanonicalName() + ".decription"));
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
    public void beforeEntityCreation(EntityItem<? extends TerrainFile> item, JPAContainer<TerrainFile> container) {
        item.getItemProperty("terrain").setValue(this.terrain);
        item.getItemProperty("mimeType").setValue(this.lastMimeType);
        item.getItemProperty("name").setValue(this.lastFileName);
        item.getItemProperty("size").setValue(this.lastSize);

    }

    @Override
    public void entityCreated(EntityItem<? extends TerrainFile> item, JPAContainer<TerrainFile> container) {
    }

}
