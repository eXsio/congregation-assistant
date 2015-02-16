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
package pl.exsio.ca.module.config.preachers;

import com.vaadin.addon.jpacontainer.EntityItem;
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
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.security.entity.factory.SecurityEntityFactory;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.form.TabbedForm;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.JPADataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;
import static pl.exsio.jin.translationcontext.TranslationContext.t;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.preachers")
public class PreachersDataTable extends JPADataTable<Preacher, TabbedForm> {

    protected CaEntityFactory caEntities;

    protected SecurityEntityFactory securityEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    public PreachersDataTable(SecurityContext security) {
        super(TabbedForm.class, new TableDataConfig(PreachersDataTable.class) {
            {
                setColumnHeaders("first_name", "last_name", "email", "phone_no", "user", "id");
                setVisibleColumns("firstName", "lastName", "email", "phoneNo", "user", "id");
            }
        }, security);
        this.openEditionAfterAddition = true;
        this.flexibleControls = true;
    }

    @Override
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends Preacher> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("700px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Preacher> ff = new FramesetFieldFactory<>(this.caEntities.getPreacherClass(), this.getClass());
        ff.setSingleSelectType(this.securityEntities.getUserClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"firstName", "lastName", "email", "phoneNo", "user"}));
        form.setBuffered(true);
        form.getField("email").addValidator(new EmailValidator(t("invalid_email")));
        form.setEnabled(true);
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        formLayout.addComponent(form);
        return formLayout;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends Preacher> item) {
        form.getTabs().addTab(this.getPriviledgesTab(item), t("priviledges"));
        form.getTabs().addTab(this.getGroupTab(item), t("group"));
    }

    private Component getPriviledgesTab(EntityItem<? extends Preacher> item) {
        PriviledgesDataTable table = new PriviledgesDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setEntityProvider(this.caEntityProviders.getPreacherPriviledgeEntityProvider());
        table.setPreacher(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        return table.init();
    }

    private Component getGroupTab(EntityItem<? extends Preacher> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setServiceGroupEntityProvider(this.caEntityProviders.getServiceGroupEntityProvider());
        table.setPreacher(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getPreacherAssignmentEntityProvider());
        return table.init();
    }

    protected Map<String, Set<String>> getTabsConfig() {
        return new LinkedHashMap() {
            {
                put(t("basic_data"), new LinkedHashSet() {
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

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

}
