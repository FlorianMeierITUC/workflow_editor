package de.ketobi.vaadinspringdemo.apps.ausschreibung.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;

public class AusschreibungSummaryGrid extends VerticalLayout {

    public AusschreibungSummaryGrid(Ausschreibung ausschreibung) {
        setSpacing(true);
        setPadding(true);
        setWidthFull();

        add(new H3("Grundinformations Übersicht"));

        Div gridWrapper = new Div();
        gridWrapper.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "1fr 1fr 1fr 1fr")
            .set("gap", "1.5rem")
            .set("margin-bottom", "2rem")
            .set("width", "100%");

        VerticalLayout leftColumnTitle = new VerticalLayout();
        leftColumnTitle.setPadding(false);
        leftColumnTitle.setSpacing(true);
        leftColumnTitle.setWidthFull();
        leftColumnTitle.add(
            createSummaryRow("Ausschreibungs Nr.:"),
            createSummaryRow("ITUC Nr.:"),
            createSummaryRow("Kunde:"),
            createSummaryRow("Branche:"),
            createSummaryRow("Projektkontakt:"),
            createSummaryRow("Projektkontakt E-Mail:")
        );

        VerticalLayout leftColumnInfo = new VerticalLayout();
        leftColumnInfo.setPadding(false);
        leftColumnInfo.setSpacing(true);
        leftColumnInfo.setWidthFull();
        leftColumnInfo.add(
            createSummaryRow(ausschreibung.getAusschreibungsNumber()),
            createSummaryRow(ausschreibung.getITUCNumber()),
            createSummaryRow(ausschreibung.getKunde()),
            createSummaryRow(ausschreibung.getBranche()),
            createSummaryRow(ausschreibung.getProjectKontakt()),
            createSummaryRow(ausschreibung.getProjectKontaktEmail())
        );

        VerticalLayout rightColumnTitle = new VerticalLayout();
        rightColumnTitle.setPadding(false);
        rightColumnTitle.setSpacing(true);
        rightColumnTitle.setWidthFull();
        rightColumnTitle.add(
            createSummaryRow("Title:"),
            createSummaryRow("Kurznotizen:")
        );

        VerticalLayout rightColumnInfo = new VerticalLayout();
        rightColumnInfo.setPadding(false);
        rightColumnInfo.setSpacing(true);
        rightColumnInfo.setWidthFull();
        rightColumnInfo.add(
            createSummaryRow(ausschreibung.getTitle()),
            createSummaryRow(ausschreibung.getNotizen())
        );

        gridWrapper.add(leftColumnTitle, leftColumnInfo, rightColumnTitle, rightColumnInfo);
        add(gridWrapper);

        Div line = new Div();
        line.getStyle()
            .set("height", "1px")
            .set("background-color", "black")
            .set("width", "100%");

        add(line);
    }

    private Div createSummaryRow(String text) {
        Div row = new Div();
        row.getStyle()
            .set("display", "flex")
            .set("gap", "0.5rem")
            .set("margin-bottom", "1rem");

        Div valueDiv = new Div(new Text(text != null && !text.isEmpty() ? text : "-"));
        row.add(valueDiv);
        return row;
    }
}
