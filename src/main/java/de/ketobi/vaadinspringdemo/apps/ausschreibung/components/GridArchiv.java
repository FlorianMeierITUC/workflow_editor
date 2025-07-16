package de.ketobi.vaadinspringdemo.apps.ausschreibung.components;

import de.ketobi.vaadinspringdemo.main.entities.ProjectListResponse;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungCreateView;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails.AusschreibungDetailView;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
//import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import com.vaadin.flow.component.combobox.ComboBox;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

public class GridArchiv extends VerticalLayout {

    private final AusschreibungService ausschreibungService;
    private final Mapper mapper;
    private final Grid<Ausschreibung> grid;
    private final ListDataProvider<Ausschreibung> dataProvider;
    private final TextField nrFilter;
    private final TextField itucFilter;
    private final TextField titleFilter;
    private final TextField dateFilter;
    private final TextField kundeFilter;
    private final TextField brancheFilter;
    private final TextField statusFilter;
    private final boolean showFavoritesOnly;
    private final List<Consumer<Ausschreibung>> favoriteListeners = new ArrayList<>();
    private final List<Ausschreibung> items = new ArrayList<>();

    @Autowired
    public GridArchiv(AusschreibungService ausschreibungService, Mapper mapper, boolean showFavoritesOnly) {
        this.ausschreibungService = ausschreibungService;
        this.mapper = mapper;
        this.showFavoritesOnly = showFavoritesOnly;
        // Initialize filters and checkbox before grid/setup
        nrFilter = createFilterTextField();
        itucFilter = createFilterTextField();
        titleFilter = createFilterTextField();
        dateFilter = createFilterTextField();
        kundeFilter = createFilterTextField();
        brancheFilter = createFilterTextField();
        statusFilter = createFilterTextField();

        // Grid setup
        this.grid = new Grid<>(Ausschreibung.class, false);

        this.dataProvider = new ListDataProvider<>(items);

        reload_projects();

        // this.dataProvider = DataProvider.ofCollection(items);
        grid.setDataProvider(dataProvider);
        grid.setAllRowsVisible(true);

        grid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS);
        grid.getElement().getStyle()
                .set("overflow", "hidden")
                .set("border", "none");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        var nrCol = grid.addColumn(new ComponentRenderer<>(a -> {
            Span numberSpan = new Span(a.getAusschreibungsNumber());
            numberSpan.getStyle().set("cursor", "pointer");
            numberSpan.getElement().setAttribute("title", "View Ausschreibung " + a.getAusschreibungsNumber());

            numberSpan.addClickListener(e -> {
                getUI().ifPresent(ui ->
                // this will navigate to /ausschreibung/create/{id}
                ui.navigate(AusschreibungDetailView.class, a.getUuid().toString()));
            });

            return numberSpan;
        })).setHeader("Auschreib. Nr.");

        var itucCol = grid.addColumn(Ausschreibung::getITUCNumber)
                .setHeader("ITUC Nr.");
        var titleCol = grid.addColumn(Ausschreibung::getTitle)
                .setHeader("Titel");
        var dateCol = grid.addColumn(Ausschreibung::getDate)
                .setHeader("Datum");
        var kundeCol = grid.addColumn(Ausschreibung::getKunde)
                .setHeader("Kunde");
        var brancheCol = grid.addColumn(Ausschreibung::getBranche)
                .setHeader("Branche");
        var statusCol = grid.addComponentColumn(a -> {
            ComboBox<String> status = new ComboBox<>();
            status.setItems("Final", "In Bearbeitung", "In Prüfung", "Abgelehnt", "Beendet");
            status.setValue(a.getStatus());
            status.setWidth("100%");
            applyStatusColor(status, a.getStatus());
            status.addValueChangeListener(e -> {
                a.setStatus(e.getValue());
                // ausschreibungService.save(a);
                dataProvider.refreshItem(a);
            });
            return status;
        })
                .setHeader("Status")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setWidth("150px"); // adjust as needed

        grid.addComponentColumn(this::buildFavoriteIcon)
                .setHeader("")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setWidth("24px");
        grid.addComponentColumn(this::buildEditIcon)
                .setHeader("")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setWidth("24px");

        grid.getHeaderRows().clear();

        HeaderRow filterRow = grid.appendHeaderRow();
        filterRow.getCell(nrCol).setComponent(nrFilter);
        filterRow.getCell(itucCol).setComponent(itucFilter);
        filterRow.getCell(titleCol).setComponent(titleFilter);
        filterRow.getCell(dateCol).setComponent(dateFilter);
        filterRow.getCell(kundeCol).setComponent(kundeFilter);
        filterRow.getCell(brancheCol).setComponent(brancheFilter);
        filterRow.getCell(statusCol).setComponent(statusFilter);

        // Data provider
        // Assemble view
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(topBar, grid);
        // expand(grid);
        applyFilters();
    }

    private TextField createFilterTextField() {
        TextField tf = new TextField();
        tf.addClassName("filter-field");
        tf.setValueChangeMode(ValueChangeMode.EAGER);
        tf.setClearButtonVisible(true);
        tf.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        tf.setWidthFull();
        tf.addValueChangeListener(e -> applyFilters());
        return tf;
    }

    private void applyFilters() {
        dataProvider.clearFilters();
        if (showFavoritesOnly) {
            dataProvider.addFilter(Ausschreibung::isFavorite);
        }
        applyTextFilter(nrFilter, Ausschreibung::getAusschreibungsNumber);
        applyTextFilter(itucFilter, Ausschreibung::getITUCNumber);
        applyTextFilter(titleFilter, Ausschreibung::getTitle);
        applyTextFilter(dateFilter, a -> a.getDate() != null ? a.getDate().toString() : "");
        applyTextFilter(kundeFilter, Ausschreibung::getKunde);
        applyTextFilter(brancheFilter, Ausschreibung::getBranche);
        applyTextFilter(statusFilter, Ausschreibung::getStatus);
    }

    public void addFavoriteToggleListener(Consumer<Ausschreibung> listener) {
        favoriteListeners.add(listener);
    }

    private void applyTextFilter(TextField field, Function<Ausschreibung, String> provider) {
        String val = field.getValue().trim().toLowerCase();
        if (!val.isEmpty()) {
            dataProvider.addFilter(a -> {
                String v = provider.apply(a);
                return v != null && v.toLowerCase().contains(val);
            });
        }
    }

    private Component buildFavoriteIcon(Ausschreibung a) {
        Icon star = a.isFavorite() ? VaadinIcon.STAR.create() : VaadinIcon.STAR_O.create();
        star.getStyle().set("cursor", "pointer");
        star.setColor(a.isFavorite() ? "gold" : "");
        star.addClickListener(e -> {
            a.setFavorite(!a.isFavorite());
            // ausschreibungService.save(a);
            dataProvider.refreshAll();
            favoriteListeners.forEach(l -> l.accept(a));
        });
        return star;
    }

    private Component buildEditIcon(Ausschreibung a) {
        Icon edit = VaadinIcon.EDIT.create();
        edit.getStyle().set("cursor", "pointer");
        edit.getElement().setAttribute("title", "Edit this Ausschreibung");
        edit.addClickListener(e -> {
            getUI().ifPresent(ui ->
            // this will navigate to /ausschreibung/create/{id}
            ui.navigate(AusschreibungCreateView.class, a.getUuid().toString()));
        });
        return edit;
    }

    public void reload_projects() {
        UI ui = UI.getCurrent();
        this.ausschreibungService.listProjects()
                .map(ProjectListResponse::getProjects)
                .map(this.mapper::mapAll)
                .defaultIfEmpty(Collections.emptyList())
                .subscribe(ausschreibungen -> {
                    ui.access(() -> {
                        this.items.clear();
                        this.items.addAll(ausschreibungen);
                        this.dataProvider.refreshAll();
                        applyFilters();
                    });
                });

    }

    private void applyStatusColor(ComboBox<String> combo, String status) {

        combo.getElement().getStyle().remove("color").remove("backgroundColor");

        switch (status) {
            case "Final":
                combo.getElement().getStyle().set("color", "var(--ituc-success-color)");
                break;
            case "In Bearbeitung":
                combo.getElement().getStyle().set("color", "var(--ituc-bearbeitung-color)");
                break;
            case "Abgelehnt":
                combo.getElement().getStyle().set("color", "var(--ituc-error-color)");
                break;
            case "In Prüfung":
                combo.getElement().getStyle().set("color", "var(--ituc-warning-color)");
                break;
            case "Beendet":
                combo.getElement().getStyle().set("color", "var(--ituc-grau)");
                break;
            default:
                // leave default color
        }
    }
}