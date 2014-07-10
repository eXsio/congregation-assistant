/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationEventPublisher;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.frameset.i18n.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.security.entity.factory.SecurityEntityFactory;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.TabbedForm;

/**
 *
 * @author exsio
 */
public class PreachersDataTable extends DataTable<Preacher, TabbedForm> {

    public static final String TRANSLATION_PREFIX = "ca.preachers.";

    protected CaEntityFactory caEntities;

    protected SecurityEntityFactory securityEntities;

    protected EntityProvider priviledgeEntityProvider;

    protected ApplicationEventPublisher aep;

    protected EntityProvider serviceGroupEntityProvider;

    protected CaRepositoryProvider caRepositories;

    protected EntityProvider preacherAssignmentEntityProvider;

    public PreachersDataTable(SecurityContext security) {
        super(TabbedForm.class, new TableConfig() {
            {
                setAddButtonLabel(TRANSLATION_PREFIX + "button.create");
                setAdditionSuccessMessage(TRANSLATION_PREFIX + "created");
                setAdditionWindowTitle(TRANSLATION_PREFIX + "window.create");
                setColumnHeaders(new String[]{"preacher.first_name", "preacher.last_name", "preacher.email", "preacher.phone_no", "preacher.user", "id"});
                setVisibleColumns(new String[]{"firstName", "lastName", "email", "phoneNo", "user", "id"});
                setDeleteButtonLabel(TRANSLATION_PREFIX + "button.delete");
                setDeletionSuccessMessage(TRANSLATION_PREFIX + "msg.deleted");
                setDeletionWindowQuestion(TRANSLATION_PREFIX + "confirmation.delete");
                setEditButtonLabel(TRANSLATION_PREFIX + "button.edit");
                setEditionSuccessMessage(TRANSLATION_PREFIX + "msg.edited");
                setEditionWindowTitle(TRANSLATION_PREFIX + "window.edit");
                setTableCaption(TRANSLATION_PREFIX + "preachers");
            }
        }, security);
        this.openEditionAfterCreation = true;
    }

    @Override
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends Preacher> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("700px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Preacher> ff = new FramesetFieldFactory<>(this.caEntities.getPreacherClass());
        ff.setSingleSelectType(this.securityEntities.getUserClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"firstName", "lastName", "email", "phoneNo", "user"}));
        form.setBuffered(true);
        form.getField("email").addValidator(new EmailValidator(t(TRANSLATION_PREFIX + "invalid_email")));
        form.setEnabled(true);
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        formLayout.addComponent(form);
        formLayout.setMargin(true);
        return formLayout;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends Preacher> item) {
        form.getTabs().addTab(this.getPriviledgesTab(item), t(TRANSLATION_PREFIX + "priviledges"));
        form.getTabs().addTab(this.getGroupTab(item), t(TRANSLATION_PREFIX + "group"));
    }

    private Component getPriviledgesTab(EntityItem<? extends Preacher> item) {
        PriviledgesDataTable table = new PriviledgesDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.priviledgeEntityProvider);
        table.setPreacher(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        return table.init();
    }

    private Component getGroupTab(EntityItem<? extends Preacher> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.serviceGroupEntityProvider);
        table.setPreacher(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.preacherAssignmentEntityProvider);
        return table.init();
    }

    protected Map<String, Set<String>> getTabsConfig() {
        return new LinkedHashMap() {
            {
                put(TRANSLATION_PREFIX + "basic_data", new LinkedHashSet() {
                    {
                        add("firstName");
                        add("lastName");
                        add("email");
                        add("phoneNo");
                        add("user");
                    }
                });
            }
        };
    }

    @Override
    protected <S extends Preacher> Class<S> getEntityClass() {
        return this.caEntities.getPreacherClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setSecurityEntities(SecurityEntityFactory securityEntities) {
        this.securityEntities = securityEntities;
    }

    public void setPriviledgeEntityProvider(EntityProvider priviledgeEntityProvider) {
        this.priviledgeEntityProvider = priviledgeEntityProvider;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher aep) {
        this.aep = aep;
        super.setApplicationEventPublisher(aep);
    }

    public void setServiceGroupEntityProvider(EntityProvider serviceGroupEntityProvider) {
        this.serviceGroupEntityProvider = serviceGroupEntityProvider;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setPreacherAssignmentEntityProvider(EntityProvider preacherAssignmentEntityProvider) {
        this.preacherAssignmentEntityProvider = preacherAssignmentEntityProvider;
    }

}