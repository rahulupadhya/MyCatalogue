package com.centura_technologies.mycatalogue.Catalogue.Model;

import java.util.ArrayList;

/**
 * Created by Centura on 28-04-2016.
 */
public class Products {
    private String Id;
    private String SectionId;
    private String CategoryId;
    private String Title;
    private String Description;
    private String SKU;
    private String BarCode;
    private String ImageUrl;
    private ArrayList<String> ProductImages;
    private ArrayList<AttchmentClass> Attachments;
    public  ArrayList<AttachmentGroup> AttachementTree= new ArrayList<AttachmentGroup>();
    private String VideoUrl;
    private String PdfUrl;
    private Double MRP;
    private Double SellingPrice;
    private ArrayList<AttributeClass> Attributes;
    private ArrayList<VarientModel> Variants;
    private String Tags;
    private String Status;
    private Double Weight;
    private boolean WishList;
    private int Version;
    private String SelectedVarient;



    public Products(){
        Id="";
        SectionId="";
        CategoryId="";
        Title="";
        Description="";
        SKU="";
        BarCode="";
        ImageUrl="";
        ProductImages=new ArrayList<String>();
        Attachments=new ArrayList<AttchmentClass>();
        AttachementTree= new ArrayList<AttachmentGroup>();
        VideoUrl="";
        PdfUrl="";
        MRP=0.0;
        SellingPrice=0.0;
        Attributes=new ArrayList<AttributeClass>();
        Variants=new ArrayList<VarientModel>();
        Tags="";
        Status="";
        Weight=0.0;
        WishList=false;
        Version=0;
        SelectedVarient="";
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSectionId() {
        return SectionId;
    }

    public  ArrayList<AttachmentGroup> getAttachementTree() {
        return AttachementTree;
    }

    public  void setAttachementTree(ArrayList<AttachmentGroup> attachementTree) {
        AttachementTree = attachementTree;
    }

    public void setSectionId(String sectionId) {
        SectionId = sectionId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public ArrayList<String> getProductImages() {
        return ProductImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        ProductImages = productImages;
    }

    public ArrayList<AttchmentClass> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<AttchmentClass> attachments) {
        Attachments = attachments;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getPdfUrl() {
        return PdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        PdfUrl = pdfUrl;
    }

    public Double getMRP() {
        return MRP;
    }

    public void setMRP(Double MRP) {
        this.MRP = MRP;
    }

    public Double getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public ArrayList<AttributeClass> getAttributes() {
        return Attributes;
    }

    public void setAttributes(ArrayList<AttributeClass> attributes) {
        Attributes = attributes;
    }

    public ArrayList<VarientModel> getVariants() {
        return Variants;
    }

    public void setVariants(ArrayList<VarientModel> variants) {
        Variants = variants;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }

    public boolean isWishList() {
        return WishList;
    }

    public void setWishList(boolean wishList) {
        WishList = wishList;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public String getSelectedVarient() {
        return SelectedVarient;
    }

    public void setSelectedVarient(String selectedVarient) {
        SelectedVarient = selectedVarient;
    }
}
