package de.htw.saar.frontend.model;

public class PageNavigation
{
    private int index;
    private String url;
    private boolean active;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActiveClass()
    {
        if(this.active)
        {
            return "active";
        }
        else
        {
            return "";
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
