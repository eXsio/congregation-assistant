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
package pl.exsio.ca.module.config.groups;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import pl.exsio.ca.model.OverseerAssignment;
import pl.exsio.ca.model.Preacher;
import pl.exsio.ca.model.ServiceGroup;
import pl.exsio.ca.model.entity.factory.CaEntityFactory;
import pl.exsio.ca.model.entity.provider.provider.CaEntityProviderProvider;
import pl.exsio.ca.model.repository.provider.CaRepositoryProvider;
import static pl.exsio.jin.translationcontext.TranslationContext.t;
import pl.exsio.frameset.security.context.SecurityContext;
import pl.exsio.frameset.vaadin.forms.fieldfactory.FramesetFieldFactory;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.AclSubjectDataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.DataTable;
import pl.exsio.frameset.vaadin.ui.support.component.data.form.TabbedForm;
import pl.exsio.frameset.vaadin.ui.support.component.data.table.TableDataConfig;
import pl.exsio.jin.annotation.TranslationPrefix;

/**
 *
 * @author exsio
 */
@TranslationPrefix("ca.groups")
public class GroupsDataTable extends AclSubjectDataTable<ServiceGroup, TabbedForm> {

    protected CaEntityFactory caEntities;

    protected CaRepositoryProvider caRepositories;

    protected CaEntityProviderProvider caEntityProviders;

    public GroupsDataTable(SecurityContext security) {
        super(TabbedForm.class, new TableDataConfig(GroupsDataTable.class) {
            {
                setColumnHeaders("no", "overseer", "archival", "id");
                setVisibleColumns("no", "overseerAssignments", "archival", "id");
            }
        }, security);
        this.flexibleControls = true;
    }

    @Override
    protected Table createTable(JPAContainer<ServiceGroup> container) {
        return new Table(this.config.getCaption(), container) {

            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                switch (colId.toString()) {
                    case "overseerAssignments":
                        SortedSet<OverseerAssignment> assignments = (SortedSet<OverseerAssignment>) property.getValue();
                        if (assignments != null && !assignments.isEmpty()) {
                            OverseerAssignment lastAssignment = assignments.last();
                            return lastAssignment.getPreacher().getCaption();
                        } else {
                            return "";
                        }
                    case "archival":
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
    protected Layout decorateForm(TabbedForm form, EntityItem<? extends ServiceGroup> item, int mode) {

        form.init(this.getTabsConfig());
        form.getLayout().setWidth("600px");
        VerticalLayout formLayout = new VerticalLayout();

        FramesetFieldFactory<? extends Preacher> ff = new FramesetFieldFactory<>(this.caEntities.getPreacherClass(), this.getClass());
        ff.setSingleSelectType(this.caEntities.getPreacherClass(), ComboBox.class);
        form.setFormFieldFactory(ff);
        form.setItemDataSource(item, Arrays.asList(new String[]{"archival"}));
        form.setBuffered(true);
        form.setEnabled(true);
        formLayout.addComponent(form);
        if (mode == DataTable.MODE_EDITION) {
            this.addEditionTabs(form, item);
        }
        return formLayout;
    }

    private void addEditionTabs(TabbedForm form, EntityItem<? extends ServiceGroup> item) {
        form.getTabs().addTab(this.getOverseerTab(item), t("overseer"));
    }

    private Component getOverseerTab(EntityItem<? extends ServiceGroup> item) {
        AssignmentsDataTable table = new AssignmentsDataTable(this.security);
        table.setCaEntities(this.caEntities);
        table.setCaRepositories(this.caRepositories);
        table.setPreacherEntityProvider(this.caEntityProviders.getPreacherEntityProvider());
        table.setServiceGroup(item.getEntity());
        table.setApplicationEventPublisher(this.aep);
        table.setEntityProvider(this.caEntityProviders.getOverseerAssignmentEntityProvider());
        return table.init();
    }

    protected Map<String, Set<String>> getTabsConfig() {
        return new LinkedHashMap() {
            {
                put(t("basic_data"), new LinkedHashSet() {
                    {
                        add("archival");
                    }
                });
            }
        };
    }

    @Override
    protected <S extends ServiceGroup> Class<S> getEntityClass() {
        return this.caEntities.getServiceGroupClass();
    }

    public void setCaEntities(CaEntityFactory caEntities) {
        this.caEntities = caEntities;
    }

    public void setCaRepositories(CaRepositoryProvider caRepositories) {
        this.caRepositories = caRepositories;
    }

    public void setCaEntityProviders(CaEntityProviderProvider caEntityProviders) {
        this.caEntityProviders = caEntityProviders;
    }

}
