package de.htw.saar.frontend.helper;

import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelDisplay;

/**
 * Stellt Funktionen bereit um originale Datenbankobjekte in eine verkleinerte Datentransportvariante zu bringen
 */
public class MinifyObject
{
    public ArtikelDisplay getMinifyArtikel(Artikel artikel)
    {
        ArtikelDisplay result = new ArtikelDisplay();
        result.id = artikel.getId();
        result.caption = artikel.getTitle();
        result.content = artikel.getMarkdown_content();
        result.thumbnail = artikel.getThumbnail();
        result.date = artikel.getCreatedAsString();
        return result;
    }
}
