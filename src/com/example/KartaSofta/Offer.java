package com.example.kartasofta;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 18.03.13
 * Time: 1:59
 * To change this template use File | Settings | File Templates.
 */
public class Offer {
    public String url;
    public String currencyId;
    public String categoryId;
    public String price;
    public String picture;
    public String name;
    public String description;
    public String id;
    public Offer(String id,String url,String categoryId, String currencyId, String picture,String price,String name, String description)
    {
        this.id = id;
        this.url = url;
        this.categoryId = categoryId;
        this.currencyId = currencyId;
        this.picture = picture;
        this.price = price;
        this.name = name;
        this.description = description;
    }
}
