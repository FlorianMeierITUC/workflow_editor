package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.Component;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;

public class AusschreibungDokumenteUploadForm extends VerticalLayout {

    public AusschreibungDokumenteUploadForm(Ausschreibung ausschreibung) {
        setSpacing(true);
        setPadding(true);

        add(new H2("Schritt 2: Kriterien definieren"));

        H3 infoTitle = new H3("Eingetragene Projektinformationen");
        VerticalLayout infoCard = new VerticalLayout();
        infoCard.getStyle()
            .set("background-color", "#f4f5f7")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("box-shadow", "0 1px 3px rgba(0,0,0,0.05)");
        infoCard.setSpacing(false);

        infoCard.add(
            createInfoRow("📄 Ausschreibungs Nr.", ausschreibung.getAusschreibungsNumber()),
            createInfoRow("🆔 ITUC Nr.", ausschreibung.getITUCNumber()),
            createInfoRow("🏢 PartnerFirma", ausschreibung.getPartnerFirma()),
            createInfoRow("👤 Projektkontakt", ausschreibung.getProjectkKontakt()),
            createInfoRow("✉️ Projektkontakt E-Mail", ausschreibung.getProjectkKontaktEmail()),
            createInfoRow("🏦 Kunde", ausschreibung.getKunde()),
            createInfoRow("🏷️ Branche", ausschreibung.getBranche()),
            createInfoRow("📝 Titel", ausschreibung.getTitel()),
            createInfoRow("🗒️ Notizen", ausschreibung.getNotizen())
        );

        TextArea uploadArea = new TextArea("Kriterien (noch leer)");
        uploadArea.setWidthFull();

        add(infoTitle, infoCard, uploadArea);
    }

    private Component createInfoRow(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidthFull();
        row.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Div labelDiv = new Div(new Text(label));
        labelDiv.getStyle().set("font-weight", "600");

        Div valueDiv = new Div(new Text(value != null ? value : "-"));
        valueDiv.getStyle().set("color", "#555");

        row.add(labelDiv, valueDiv);
        return row;
    }
}
