package de.htw.saar.frontend.controller;


import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.service.ElasticSearchService;
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

    ElasticSearchService manager = new ElasticSearchService();

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

    @RequestMapping("/artikeledit")
    public String artikeledit(String id, String toggle,String value)
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
                // Update Artikel to favorite or remove favorite
                artikel = manager.getArtikelById(id);

                if(toggle.equals("favorite"))
                {
                    if(value.equals("false")){
                        artikel.setStarred(false);
                    } else {
                        artikel.setStarred(true);
                    }

                }
                else if(toggle.equals("spaeteransehen"))
                {
                    if(value.equals("false")){
                        artikel.setRead_later(false);
                    } else {
                        artikel.setRead_later(true);
                    }
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex);
            }

            return view("artikel","shared");
        }
    }

}
