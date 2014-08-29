/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.ca.app.viewport;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import pl.exsio.frameset.core.repository.provider.CoreRepositoryProvider;
import pl.exsio.frameset.vaadin.account.HorizontalAccountMenu;
import pl.exsio.frameset.vaadin.component.InitializableComponent;
import pl.exsio.frameset.vaadin.component.InitializableHorizontalLayout;
import pl.exsio.frameset.vaadin.navigation.FramesetNavigator;
import pl.exsio.frameset.vaadin.navigation.menu.AccordionMenu;
import pl.exsio.frameset.vaadin.navigation.target.NavigationTarget;
import pl.exsio.frameset.vaadin.ui.Viewport;
import pl.exsio.frameset.vaadin.ui.support.flexer.Flexer;
import pl.exsio.frameset.vaadin.ui.support.flexer.OrderedLayoutHeightFlexerImpl;
import pl.exsio.frameset.vaadin.ui.support.flexer.OrderedLayoutWidthFlexerImpl;

/**
 *
 * @author exsio
 */
public class AppViewport extends InitializableHorizontalLayout implements Viewport {

    /**
     * Menu
     */
    private AccordionMenu menu;

    /**
     * Frame repository
     */
    private transient CoreRepositoryProvider coreRepositories;

    /**
     * Navigation target
     */
    private NavigationTarget navigationTarget;

    /**
     * Frameset Navigator
     */
    protected transient FramesetNavigator navigator;

    /**
     * Account menu
     */
    private HorizontalAccountMenu accountMenu;


    @Override
    protected void doInit() {
        addStyleName("root");
        setSizeFull();
        this.menu.setRootFrame(this.coreRepositories.getFrameRepository().getRootFrame());
        this.menu.init();
        this.navigator.addFrameChangeListener(menu);
        this.accountMenu.init();
        VerticalLayout sidebar = new VerticalLayout() {
            {
                setSizeFull();
                addComponent(accountMenu);
                addComponent(menu);
                setSpacing(true);
                this.setExpandRatio(accountMenu, 1);
                this.setExpandRatio(menu, 8);
                
                Flexer accMenuFlexer = new OrderedLayoutHeightFlexerImpl(this, accountMenu);
                accMenuFlexer.addConstraint(900, 1)
                        .addConstraint(900, 1)
                        .addConstraint(800, 1.1)
                        .addConstraint(700, 1.3)
                        .addConstraint(600, 1.6)
                        .addConstraint(500, 2)
                        .addConstraint(400, 2.3)
                        .addConstraint(300, 3)
                        .attach();
                        
                        

                accountMenu.setSizeFull();
                setStyleName("sidebar");
            }
        };
        addComponent(sidebar);
        VerticalLayout content = new VerticalLayout() {
            {
                addComponent(navigationTarget);

                if (navigationTarget instanceof InitializableComponent) {
                    ((InitializableComponent) navigationTarget).init();
                }

                navigationTarget.setSizeFull();
                setSizeFull();
                setMargin(new MarginInfo(true, true, true, false));
            }
        };

        addComponent(content);
        this.setExpandRatio(sidebar, 1);
        this.setExpandRatio(content, 10);
        this.attachSidebarFlexer(sidebar);
    }
    
    private void attachSidebarFlexer(VerticalLayout sidebar) {
        Flexer sidebarFlexer = new OrderedLayoutWidthFlexerImpl(this, sidebar);
        sidebarFlexer
                .addConstraint(1800, 1)
                .addConstraint(1600, 1.1)
                .addConstraint(1400, 1.3)
                .addConstraint(1200, 1.5)
                .addConstraint(1000, 2.0)
                .attach();
    }
    
    
    public void setCoreRepositories(CoreRepositoryProvider coreRepositories) {
        this.coreRepositories = coreRepositories;
    }

    public void setNavigationTarget(NavigationTarget navigationTarget) {
        this.navigationTarget = navigationTarget;
    }

    public void setAccountMenu(HorizontalAccountMenu accountMenu) {
        this.accountMenu = accountMenu;
    }

    public void setNavigator(FramesetNavigator navigator) {
        this.navigator = navigator;
    }

    public void setMenu(AccordionMenu menu) {
        this.menu = menu;
    }
}
