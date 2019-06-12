package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.helper.ElasticSearchManager;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Named;

@Named
@RequestMapping("/")
public class ArtikelController extends MasterController
{
    private String artikelId;
    public String getArtikelId()
    {
        return this.artikelId;
    }

    private Artikel artikel;
    public Artikel getArtikel()
    {
        return this.artikel;
    }

    ElasticSearchManager manager = new ElasticSearchManager();

    @RequestMapping("/artikel")
    public String artikel(String id)
    {
        if (id == null || id == "")
        {
            return view("index","home");
        }
        else
        {
            artikelId = id;
            try
            {
                artikel = manager.getArtikelById(id);
            }
            catch (Exception ex)
            {
                System.out.println(ex);
            }

            return view("artikel","shared");
        }
    }
}
