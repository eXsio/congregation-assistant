/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import static com.vaadin.addon.jpacontainer.filter.Filters.eq;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.PreacherPriviledge;
import pl.exsio.ca.model.Priviledge;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.ui.support.component.ComponentFactory;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;

/**
 *
 * @author exsio
 */
public class PriviledgesDataTable extends DataTable<PreacherPriviledge, Form> implements DataTable.EntityCreationListener<PreacherPriviledge> {

    public static final String TRANSLATION_PREFIX = "ca.priviledges.";

    protected CaEntityFactory caEntities;

    protected Preacher preacher;

    @Override
    protected void doInit() {
        super.doInit();
        if (this.preacher == null) {
            this.setEnabled(false);
        }
        this.setHeight("250px");
    }

    @Override
    protected JPAContainer<PreacherPriviledge> createJPAContainer() {
        JPAContainer<PreacherPriviledge> container = super.createJPAContainer();
        container.addContainerFilter(eq("preacher", this.preacher));
        return container;
    }
    
    @Override
    protected HorizontalLayout decorateControls(HorizontalLayout controls) {
        controls.removeComponent(this.editButton);
        return super.decorateControls(controls);
    }

    public PriviledgesDataTable(SecurityContext security) {
        super(Form.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"preacher.priviledge", "id"});
                setVisibleColumns(new String[]{"priviledge", "id"});
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
    protected Layout decorateForm(Form form, EntityItem<? extends PreacherPriviledge> item, int mode) {

        VerticalLayout formLayout = new VerticalLayout();

        Field priviledge = ComponentFactory.createEnumComboBox(t(this.caEntities.getPreacherPriviledgeClass().getCanonicalName() + ".priviledge"), Priviledge.class);
        priviledge.setPropertyDataSource(item.getItemProperty("priviledge"));
        priviledge.addValidator(new NullValidator(t(TRANSLATION_PREFIX + "not_null"), false));
        form.addField("priviledge", priviledge);
        form.setBuffered(true);
        form.setEnabled(true);

        formLayout.addComponent(form);
        formLayout.setMargin(true);
        return formLayout;
    }

    @Override
    protected <S extends PreacherPriviledge> Class<S> getEntityClass() {
        return this.caEntities.getPreacherPriviledgeClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    @Override
    public void beforeEntityCreation(EntityItem<? extends PreacherPriviledge> item, JPAContainer<PreacherPriviledge> container) {
        item.getItemProperty("preacher").setValue(this.preacher);
    }

    @Override
    public void entityCreated(EntityItem<? extends PreacherPriviledge> item, JPAContainer<PreacherPriviledge> container) {
        
    }

    public void setPreacher(Preacher preacher) {
        this.preacher = preacher;
    }

}
