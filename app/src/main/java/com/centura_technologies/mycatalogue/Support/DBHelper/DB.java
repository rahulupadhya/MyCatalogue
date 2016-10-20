package com.centura_technologies.mycatalogue.Support.DBHelper;

import com.centura_technologies.mycatalogue.Activity.Model.ActivitiesModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.CategoryTree;
import com.centura_technologies.mycatalogue.Catalogue.Model.InitialModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.Products;
import com.centura_technologies.mycatalogue.Catalogue.Model.Sections;
import com.centura_technologies.mycatalogue.Leads.Model.LeadsModel;
import com.centura_technologies.mycatalogue.Order.Model.BillingProducts;
import com.centura_technologies.mycatalogue.Shortlist.Model.ShortlistModel;

import java.util.ArrayList;

/**
 * Created by Centura User1 on 10-08-2016.
 */
public class DB {
    private static InitialModel initialModel = new InitialModel();
    private static LeadsModel leadmodel = new LeadsModel();
    private static ArrayList<LeadsModel> leads = new ArrayList<LeadsModel>();
    private static ActivitiesModel activitymodel = new ActivitiesModel();
    private static ArrayList<ActivitiesModel> activities = new ArrayList<ActivitiesModel>();
    private static Sections sectionmodel = new Sections();
    private static ArrayList<Sections> sectionlist = new ArrayList<Sections>();
    private static CategoryTree tree = new CategoryTree();
    private static ArrayList<CategoryTree> treelist = new ArrayList<CategoryTree>();
    private static BillingProducts billingProducts = new BillingProducts();
    private static ArrayList<BillingProducts> billprodlist = new ArrayList<BillingProducts>();
    private static Products shortlisted = new Products();
    private static ArrayList<Products> shortlistedlist = new ArrayList<Products>();
    private static ShortlistModel shortlistModel=new ShortlistModel();
    private static ArrayList<ShortlistModel> shortlistModels=new ArrayList<ShortlistModel>();


    public static InitialModel getInitialModel() {
        return initialModel;
    }

    public static LeadsModel getLeadmodel() {
        return leadmodel;
    }

    public static Sections getSectionmodel() {
        return sectionmodel;
    }

    public static void setLeadmodel(LeadsModel leadmodel) {
        DB.leadmodel = leadmodel;
    }

    public static ArrayList<LeadsModel> getLeads() {
        return leads;
    }

    public static void setLeads(ArrayList<LeadsModel> leads) {
        DB.leads = leads;
    }

    public static ActivitiesModel getActivitymodel() {
        return activitymodel;
    }

    public static void setActivitymodel(ActivitiesModel activitymodel) {
        DB.activitymodel = activitymodel;
    }

    public static ArrayList<ActivitiesModel> getActivities() {
        return activities;
    }

    public static void setActivities(ArrayList<ActivitiesModel> activities) {
        DB.activities = activities;
    }


    public static void setSectionmodel(Sections sectionmodel) {
        DB.sectionmodel = sectionmodel;
    }

    public static ArrayList<Sections> getSectionlist() {
        return sectionlist;
    }

    public static void setSectionlist(ArrayList<Sections> sectionlist) {
        DB.sectionlist = sectionlist;
    }

    public static Products getShortlisted() {
        return shortlisted;
    }

    public static void setShortlisted(Products shortlisted) {
        DB.shortlisted = shortlisted;
    }

    public static ArrayList<Products> getShortlistedlist() {
        return shortlistedlist;
    }

    public static void setShortlistedlist(ArrayList<Products> shortlistedlist) {
        DB.shortlistedlist = shortlistedlist;
    }

    public static CategoryTree getTree() {
        return tree;
    }

    public static void setTree(CategoryTree tree) {
        DB.tree = tree;
    }

    public static ArrayList<CategoryTree> getTreelist() {
        return treelist;
    }

    public static void setTreelist(ArrayList<CategoryTree> treelist) {
        DB.treelist = treelist;
    }

    public static BillingProducts getBillingProducts() {
        return billingProducts;
    }

    public static void setBillingProducts(BillingProducts billingProducts) {
        DB.billingProducts = billingProducts;
    }

    public static ArrayList<BillingProducts> getBillprodlist() {
        return billprodlist;
    }

    public static void setBillprodlist(ArrayList<BillingProducts> billprodlist) {
        DB.billprodlist = billprodlist;
    }

    public static ShortlistModel getShortlistModel() {
        return shortlistModel;
    }

    public static void setShortlistModel(ShortlistModel shortlistModel) {
        DB.shortlistModel = shortlistModel;
    }

    public static ArrayList<ShortlistModel> getShortlistModels() {
        return shortlistModels;
    }

    public static void setShortlistModels(ArrayList<ShortlistModel> shortlistModels) {
        DB.shortlistModels = shortlistModels;
    }

    public static void AddLead(LeadsModel newlead) {
        //Validate model
        leads.add(newlead);
    }

    public static void UpdateLead(LeadsModel editlead) {
        //Validate model
        leads.add(editlead);
    }

    public static void AddActivity(ActivitiesModel newactivity) {
        //Validate model
        activities.add(newactivity);
    }

    public static void EditActivity(ActivitiesModel editactivity) {
        //Validate model
        activities.add(editactivity);
    }

    public static void setInitialModel(InitialModel initialModel) {
        DB.initialModel = initialModel;
    }
}
