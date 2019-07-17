package de.htw.saar.frontend.Configuration;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class UrlConfig
{
    Properties props;
    public UrlConfig()
    {
        try{
            File configFile = new File("config.properties");
            FileReader reader = new FileReader(configFile);
            props = new Properties();
            props.load(reader);
            reader.close();
        }catch(Exception ex){
            System.out.println("Fehler beim Laden der Properties Datei - " + ex);
        }
    }

    public String getProperty(String key)
    {
        return props.getProperty(key);
    }
}
