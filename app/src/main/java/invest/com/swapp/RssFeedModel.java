package invest.com.swapp;

/**
 * Created by Loey on 27/11/2016.
 */

public class RssFeedModel {

    public String title;
    public String link;
    public String description;
    public String imgLink;

    public RssFeedModel(String title, String link, String description, String imgLink) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.imgLink = imgLink;
    }
}
