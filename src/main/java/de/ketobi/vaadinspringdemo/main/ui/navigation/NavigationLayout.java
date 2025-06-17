package de.ketobi.vaadinspringdemo.main.ui.navigation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.main.ui.navigation.components.AddFolderButton;
import de.ketobi.vaadinspringdemo.main.ui.navigation.components.AddTargetButton;
import de.ketobi.vaadinspringdemo.main.ui.navigation.services.NavigationService;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class NavigationLayout extends VerticalLayout implements BeforeEnterObserver {
    private final NavigationService navigationService;
    private final Div navigationDiv = new Div();

    @Autowired
    public NavigationLayout(NavigationService navigationService) {
        this.navigationService = navigationService;
        setSizeUndefined();
    }

    public void refresh(){
        removeAll();
        navigationDiv.removeAll();

        navigationService.getAllNavigationFolders().forEach(folder -> {
            SideNav folderNav = new SideNav();
            folderNav.setLabel(folder.getLabel());
            folderNav.setCollapsible(true);
            navigationService.getTargetsByFolderId(folder.getId()).forEach(target -> {
                folderNav.addItem(new SideNavItem(target.getLabel(), target.getView()));
            });
            VerticalLayout container = new VerticalLayout();
            container.add(folderNav);
            AddTargetButton addTargetButton = new AddTargetButton(folder.getId(), navigationService, this);
            //<theme-editor-local-classname>
            addTargetButton.addClassName("navigation-layout-button-1");
            container.add(addTargetButton);
            navigationDiv.add(container);
        });
        Button goToausschreibung = new Button("Go to ausschreibung", 
            e -> e.getSource().getUI().ifPresent(ui -> ui.navigate("ausschreibung"))
        );

        add(goToausschreibung);

        Scroller scroller = new Scroller(navigationDiv);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        add(scroller);
        add(new Hr());
        AddFolderButton addFolderButton = new AddFolderButton(navigationService, this);
        //<theme-editor-local-classname>
        addFolderButton.addClassName("navigation-layout-button-1");
        add(addFolderButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (UserService.getCurrentUser() != null) {
            refresh();
        }
    }
}