package de.ketobi.vaadinspringdemo.apps.ausschreibung.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class IconGalleryComponent extends VerticalLayout {

    private static final int ICON_SIZE_PX = 160;       // width & height of each icon’s square
    private static final double GAP_RATIO = 0.75;     // gap = 75% of ICON_SIZE
    private static final int ICON_COUNT_PER_ROW = 4;

    public IconGalleryComponent() {
        setPadding(false);
        setMargin(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        // Compute the pixel gap (75% of ICON_SIZE_PX)
        int gapPx = (int) Math.round(ICON_SIZE_PX * GAP_RATIO);

        // Total width of one row = (4 icons × ICON_SIZE) + (3 gaps × gapPx)
        int totalRowWidthPx = (ICON_COUNT_PER_ROW * ICON_SIZE_PX)
                            + ((ICON_COUNT_PER_ROW - 1) * gapPx);

        // First row: exactly that total width, icons aligned at top
        HorizontalLayout row1 = new HorizontalLayout();
        configureRow(row1, totalRowWidthPx, gapPx);
        row1.getStyle().set("margin-bottom", "2rem");
        row1.getStyle().set("margin-top", "2rem");
        // Second row: same setup
        HorizontalLayout row2 = new HorizontalLayout();
        configureRow(row2, totalRowWidthPx, gapPx);

        // Define 8 icons and captions
        VaadinIcon[] icons = {
            VaadinIcon.CHAT,
            VaadinIcon.FILE_ADD,
            VaadinIcon.FILE_SEARCH,
            VaadinIcon.PIE_BAR_CHART,
            VaadinIcon.QUESTION_CIRCLE_O,
            VaadinIcon.QUESTION_CIRCLE_O,
            VaadinIcon.QUESTION_CIRCLE_O,
            VaadinIcon.QUESTION_CIRCLE_O
        };
        String[] captions = {
            "Chat Bot",
            "Ausschreibungs APP",
            "Dokumenten Vergleich",
            "Diagramme generieren",
            "TBD",
            "TBD",
            "TBD",
            "TBD"
        };

        String[] routes = {
            "under-construction",           // route for Chat Bot
            "ausschreibung",// route for Ausschreibungs App
            "under-construction",    // route for Dokumenten Vergleich
            "under-construction",   // route for Diagramme generieren
            "under-construction",         // route for Feature A
            "under-construction",         // route for Feature B
            "under-construction",         // route for Feature C
            "under-construction"          // route for Feature D
        };

         for (int i = 0; i < icons.length; i++) {
            VerticalLayout cell = createCellWithRoute(
                icons[i], captions[i], ICON_SIZE_PX, routes[i]
            );
            if (i < ICON_COUNT_PER_ROW) {
                row1.add(cell);
            } else {
                row2.add(cell);
            }
        }

        add(row1, row2);
    }

    private void configureRow(HorizontalLayout row, int totalWidthPx, int gapPx) {
        // We do NOT call row.setWidthFull(); instead, fix the row’s width
        row.setPadding(false);
        row.setSpacing(false);

        // Align all cells at the top, so icons stay on one horizontal line
        row.setAlignItems(Alignment.START);

        // Set the row’s exact width in pixels
        row.setWidth(totalWidthPx + "px");

        // Use CSS gap to enforce exactly gapPx between items
        row.getStyle().set("gap", gapPx + "px");
    }

    private VerticalLayout createCellWithRoute(
            VaadinIcon vaadinIcon, String caption, int iconSizePx, String route) {

        VerticalLayout cell = new VerticalLayout();
        cell.setWidth(iconSizePx + "px");     // Fix cell width exactly to ICON_SIZE_PX
        cell.setPadding(false);
        cell.setSpacing(true);
        cell.setAlignItems(Alignment.CENTER);

        // 1) A Div wrapper that enforces an ICON_SIZE_PX × ICON_SIZE_PX square
        Div iconWrapper = new Div();
        iconWrapper.setWidth(iconSizePx + "px");
        iconWrapper.setHeight(iconSizePx + "px");
        iconWrapper.getStyle().set("display", "flex");
        iconWrapper.getStyle().set("align-items", "center");
        iconWrapper.getStyle().set("justify-content", "center");
        iconWrapper.getStyle().set("background-color", "var(--ituc-grau-3)");

        // If this cell should be clickable, change cursor style and add a click listener
        if (route != null && !route.isEmpty()) {
            iconWrapper.getStyle().set("cursor", "pointer");
            iconWrapper.addClickListener(event ->
                UI.getCurrent().navigate(route)
            );
        }

        // 2) The icon itself, sized to ~45% of the wrapper so it’s centered
        Icon icon = new Icon(vaadinIcon);
        icon.setSize((int) (iconSizePx * 0.45) + "px");
        iconWrapper.add(icon);

        // 3) Caption underneath, which will wrap within the fixed cell width
        Span label = new Span(caption);
        label.getStyle().set("font-size", "14px");
        label.getStyle().set("text-align", "center");
        label.getStyle().set("word-wrap", "break-word");

        // Add iconWrapper and caption to the cell
        cell.add(iconWrapper, label);
        return cell;
    }
}