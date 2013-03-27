package com.example.KartaSofta;

/**
 * Created with IntelliJ IDEA.
 * User: ag3r
 * Date: 3/21/13
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class Category
{
    private String categoryName;
    private String categoryId;
    private String parentId;
    public Category (String categoryName, String categoryId,String parentId)
    {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.parentId = parentId;
    }
    public String getParentId()
    {
        return parentId;
    }
    public String getCategoryName()
    {
        return categoryName;
    }
    public String getCategoryId()
    {
        return categoryId;
    }
}
