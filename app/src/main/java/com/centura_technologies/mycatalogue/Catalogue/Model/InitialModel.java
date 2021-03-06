package com.centura_technologies.mycatalogue.Catalogue.Model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Centura User1 on 23-08-2016.
 */
public class InitialModel {
    public ArrayList<Sections> Sections=new ArrayList<Sections>();
    public ArrayList<Categories> Categories=new ArrayList<Categories>();
    public ArrayList<Products> Products=new ArrayList<Products>();
    public ArrayList<CollectionModel> Collections=new ArrayList<CollectionModel>();

    public InitialModel(){
        Sections= new ArrayList<Sections>();
        Categories=new ArrayList<Categories>();
        Products=new ArrayList<Products>();
        Collections=new ArrayList<CollectionModel>();
    }

    public ArrayList<Sections> getSections() {
        return Sections;
    }

    public void setSections(ArrayList<Sections> sections) {
        Sections = sections;
    }

    public ArrayList<Categories> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        Categories = categories;
    }

    public ArrayList<Products> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<Products> products) {
        Products = products;
    }

    public ArrayList<CollectionModel> getCollections() {
        return Collections;
    }

    public void setCollections(ArrayList<CollectionModel> collections) {
        Collections = collections;
    }
}
