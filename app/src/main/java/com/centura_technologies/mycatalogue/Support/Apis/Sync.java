package com.centura_technologies.mycatalogue.Support.Apis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.centura_technologies.mycatalogue.Catalogue.Model.AttributeClass;
import com.centura_technologies.mycatalogue.Catalogue.Model.Categories;
import com.centura_technologies.mycatalogue.Catalogue.Model.CollectionModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.CustomerModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.InitialModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.Sections;
import com.centura_technologies.mycatalogue.Catalogue.Model.FilterItem;
import com.centura_technologies.mycatalogue.Catalogue.Model.Products;
import com.centura_technologies.mycatalogue.Catalogue.Model.Valuepair;
import com.centura_technologies.mycatalogue.Login.Controller.IntroductionClass;
import com.centura_technologies.mycatalogue.Login.Controller.Login;
import com.centura_technologies.mycatalogue.Login.Controller.Splash;
import com.centura_technologies.mycatalogue.Order.Model.BillingProducts;
import com.centura_technologies.mycatalogue.Order.Model.OrderModel;
import com.centura_technologies.mycatalogue.R;
import com.centura_technologies.mycatalogue.Settings.Controller.Settings;
import com.centura_technologies.mycatalogue.Support.ConfigData;
import com.centura_technologies.mycatalogue.Support.DBHelper.DB;
import com.centura_technologies.mycatalogue.Support.DBHelper.DbHelper;
import com.centura_technologies.mycatalogue.Support.DBHelper.StaticData;
import com.centura_technologies.mycatalogue.Support.GenericData;
import com.centura_technologies.mycatalogue.Support.GetImageFromUrl;
import com.centura_technologies.mycatalogue.Support.ImageCache;
import com.centura_technologies.mycatalogue.Sync.model.SyncSectionsClass;
import com.centura_technologies.mycatalogue.configuration.DataVersion;
import com.centura_technologies.mycatalogue.configuration.SyncAll;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Centura User1 on 22-08-2016.
 */
public class Sync {

    static Gson gson = new Gson();
    static SharedPreferences sharedPreferences;
    static DbHelper db;
    static ArrayList<InitialModel> im;

    public static ArrayList<String> SelectedSectionSync = new ArrayList<>();
    public static boolean SyncCollections = false;

    public static void syncinitial(final Context context) {
        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        //if(GenericData.NetCheck(context)&& sharedPreferences.getString(GenericData.Sp_Status,"").matches("LoggedIn")){
        initialapi(context);
        /*}else if(sharedPreferences.getString(GenericData.Sp_Status,"").matches("LoggedIn")){
            db.loadinitialmodel();
        }*/
    }

    public static void initialapi(final Context context) {
        db = new DbHelper(context);
        im = new ArrayList<InitialModel>();
        SyncCustomerList(context);
        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        ArrayList<Sections> model = new ArrayList<Sections>();
        RequestQueue queue = Volley.newRequestQueue(context);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        params.put("SectionVersion", DataVersion.SectionVersion + "");
        params.put("CategoryVersion", DataVersion.CategoryVersion + "");
        params.put("ProductVersion", DataVersion.ProductVersion + "");
        params.put("CollectionVersion", DataVersion.CollectionVersion + "");
        GenericData.ShowDialog(context, "Loading...", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.Initial, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GenericData.ShowDialog(context, "Loading...", false);
                if (response.optString("IsSuccess").matches("true")) {
                    try {
                        ArrayList<ImageCache> allMedia = new ArrayList<ImageCache>();
                        InitialModel temp = new InitialModel();
                        JSONObject jsonObject = response.getJSONObject("Data");
                        temp = gson.fromJson(jsonObject.toString(), InitialModel.class);
                        for (int i = 0; i < temp.getProducts().size(); i++) {
                            if (DataVersion.ProductVersion < temp.getProducts().get(i).getVersion())
                                DataVersion.ProductVersion = temp.getProducts().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getProducts().get(i).getImageUrl(), temp.getProducts().get(i).getId(), context);
                            allMedia.add(param);
                            for (int d = 0; d < temp.getProducts().get(i).getProductImages().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getProductImages().get(d), temp.getProducts().get(i).getId() + d + "", context);
                                allMedia.add(param);
                            }
                            for (int d = 0; d < temp.getProducts().get(i).getAttachments().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getAttachments().get(d).AttachmentUrl, temp.getProducts().get(i).getId() + "attachment" + d + "", context);
                                allMedia.add(param);

                            }
                        }
                        for (int i = 0; i < temp.getSections().size(); i++) {
                            if (DataVersion.SectionVersion < temp.getSections().get(i).getVersion())
                                DataVersion.SectionVersion = temp.getSections().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getSections().get(i).getImageUrl(), temp.getSections().get(i).getId(), context);
                            allMedia.add(param);
                        }
                        for (int i = 0; i < temp.getCategories().size(); i++) {
                            if (DataVersion.CategoryVersion < temp.getCategories().get(i).getVersion())
                                DataVersion.CategoryVersion = temp.getCategories().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getCategories().get(i).getImageUrl(), temp.getCategories().get(i).getId(), context);
                            allMedia.add(param);
                        }
                        for (int j = 0; j < temp.getCollections().size(); j++) {
                            if (DataVersion.CollectionVersion < temp.getCollections().get(j).getVersion())
                                DataVersion.CollectionVersion = temp.getCollections().get(j).getVersion();
                            ImageCache param = new ImageCache(temp.getCollections().get(j).getImageUrl(), temp.getCollections().get(j).getId(), context);
                            allMedia.add(param);
                        }
                        GenericData.imagesChached = true;
                        updateInitialmodel(temp,context);
                        LoadAsyncData(allMedia, context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                GenericData.ShowDialog(context, "Loading...", false);
                Log.d("Error", "Error");
            }
        });
        queue.add(jsonObjectRequest);
    }

    public static void SyncCustomerList(final Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(GenericData.MyPref, mContext.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.CustomerInitial, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.optString("IsSuccess").matches("true")) {
                    ArrayList<CustomerModel> model = new ArrayList<CustomerModel>();
                    try {
                        Type customertype = new TypeToken<ArrayList<CustomerModel>>() {}.getType();
                        model = gson.fromJson(response.getJSONObject("Data").getString("Customers").toString(), customertype);
                        DB.setCustomers(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error", "Error");
            }
        });
        queue.add(jsonObjectRequest);
    }

    static int syncposition = 0;
    static int syncSize = 0;

    private static void LoadAsyncData(final ArrayList<ImageCache> allMedia, Context context) {
        syncposition = -1;
        syncSize = allMedia.size();
        if (context != null && syncSize>0)
            GenericData.ShowDialog(context, "Loading Media", true);

        final GetImageFromUrl getImageFromUrl = new GetImageFromUrl();
        loadnext(allMedia, getImageFromUrl, context);
    }

    private static void loadnext(final ArrayList<ImageCache> allMedia, GetImageFromUrl getImageFromUrl, final Context context) {
        if (getImageFromUrl.getStatus() != AsyncTask.Status.RUNNING) {
            if (syncSize - 1 > syncposition) {
                syncposition++;
                if (context != null && syncSize>0)
                    GenericData.SetDialogMessage("Loading " + syncposition + " out of " + syncSize);
                getImageFromUrl = new GetImageFromUrl();
                getImageFromUrl.execute(allMedia.get(syncposition));
            } else {
                if (context != null)
                    GenericData.ShowDialog(context, "Loading Media", false);
                if (ConfigData.SYNCNOW) {
                    SyncAll.done();
                } else
                    Settings.allicon.setImageResource(R.drawable.checkcircle);
                return;
            }
        }
        final GetImageFromUrl finalGetImageFromUrl = getImageFromUrl;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadnext(allMedia, finalGetImageFromUrl, context);
            }
        }, 500);

    }


    public static void SyncSectionList(final Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(GenericData.MyPref, mContext.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        params.put("Version", DataVersion.SectionVersion + "");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.SectionList, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.optString("IsSuccess").matches("true")) {
                    ArrayList<Sections> model = new ArrayList<Sections>();
                    InitialModel temp = new InitialModel();
                    try {

                        ArrayList<ImageCache> allMedia = new ArrayList<ImageCache>();
                        JSONObject jsonObject = response.getJSONObject("Data");
                        temp = gson.fromJson(jsonObject.toString(), InitialModel.class);
                        model = (temp.getSections());
                        for (int i = 0; i < model.size(); i++) {
                            if (DataVersion.SectionVersion < model.get(i).getVersion())
                                DataVersion.SectionVersion = model.get(i).getVersion();
                            ImageCache param = new ImageCache(model.get(i).getImageUrl(), model.get(i).getId(), mContext);
                            allMedia.add(param);
                        }
                        GenericData.imagesChached = true;
                        updateInitialmodel(temp,mContext);
                        LoadAsyncData(allMedia, mContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error", "Error");
            }
        });
        queue.add(jsonObjectRequest);
    }

    public static void syncFilters(Context mContext, ArrayList<Products> model) {
        StaticData.filter = "";
        ArrayList<Products> prod = new ArrayList<Products>();
        for (int j = 0; j < model.size(); j++) {
            prod.add(model.get(j));
        }
        Double max = getMax(prod);

        StaticData.filtermodel.setMaxprice(max);
        StaticData.filtermodel.setMinprice(0.0);

        StaticData.filtermodel.item = new ArrayList<FilterItem>();
        ArrayList<AttributeClass> allattr = new ArrayList<AttributeClass>();
        ArrayList<FilterItem> list = new ArrayList<FilterItem>();
        ArrayList<String> AttName = new ArrayList<String>();

        list.clear();
        for (int j = 0; j < prod.size(); j++) {
            for (int q = 0; q < prod.get(j).getAttributes().size(); q++) {
                allattr.add(prod.get(j).getAttributes().get(q));
                AttName.add(prod.get(j).getAttributes().get(q).getAttributeName());
            }
        }
        Set<String> attrname = new HashSet<String>(AttName);
        for (String str : attrname) {
            FilterItem att = new FilterItem();
            ArrayList<String> Values = new ArrayList<String>();
            att.setTitle(str);
            for (AttributeClass data : allattr) {
                if (data.getAttributeName().matches(str)) {
                    String tempvaluepair = data.getAttributeValue();
                    if (!tempvaluepair.matches(""))
                        Values.add(tempvaluepair);
                }
            }
            Set<String> attrval = new HashSet<String>(Values);
            ArrayList<Valuepair> finalValues = new ArrayList<Valuepair>();
            for (String tempvalue : attrval) {
                Valuepair tempvalpair = new Valuepair();
                tempvalpair.ValueName = tempvalue;
                finalValues.add(tempvalpair);
            }
            att.setValue(finalValues);
            list.add(att);
        }
        StaticData.filtermodel.setItem(list);

    }

    public static Double getMax(ArrayList<Products> list) {
        Double max = Double.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSellingPrice() > max) {
                max = list.get(i).getSellingPrice();
            }
        }
        return max;
    }

    public static void syncroniseCollections(final Context context) {
        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        ArrayList<Sections> model = new ArrayList<Sections>();
        RequestQueue queue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        params.put("Version", DataVersion.CollectionVersion + "");
        //GenericData.ShowDialog(context, "Loading...", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.collectionlist, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //GenericData.ShowDialog(context,"Loading...",false);
                if (response.optString("IsSuccess").matches("true")) {
                    try {
                        ArrayList<ImageCache> allMedia = new ArrayList<ImageCache>();
                        InitialModel temp = new InitialModel();
                        JSONObject jsonObject = response.getJSONObject("Data");
                        temp = gson.fromJson(jsonObject.toString(), InitialModel.class);
                        for (int i = 0; i < temp.getProducts().size(); i++) {
                            if (DataVersion.ProductVersion < temp.getProducts().get(i).getVersion())
                                DataVersion.ProductVersion = temp.getProducts().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getProducts().get(i).getImageUrl(), temp.getProducts().get(i).getId(), context);
                            allMedia.add(param);
                            for (int d = 0; d < temp.getProducts().get(i).getProductImages().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getProductImages().get(d), temp.getProducts().get(i).getId() + d + "", context);
                                allMedia.add(param);
                            }
                            for (int d = 0; d < temp.getProducts().get(i).getAttachments().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getAttachments().get(d).AttachmentUrl, temp.getProducts().get(i).getId() + "attachment" + d + "", context);
                                allMedia.add(param);
                            }
                        }

                        for (int i = 0; i < temp.getCategories().size(); i++) {
                            if (DataVersion.CategoryVersion < temp.getCategories().get(i).getVersion())
                                DataVersion.CategoryVersion = temp.getCategories().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getCategories().get(i).getImageUrl(), temp.getCategories().get(i).getId(), context);
                            allMedia.add(param);
                        }

                        for (int j = 0; j < temp.getCollections().size(); j++) {

                            if (DataVersion.CollectionVersion < temp.getCollections().get(j).getVersion())
                                DataVersion.CollectionVersion = temp.getCollections().get(j).getVersion();
                            ImageCache param = new ImageCache(temp.getCollections().get(j).getImageUrl(), temp.getCollections().get(j).getId(), context);
                            allMedia.add(param);
                        }
                        GenericData.imagesChached = true;
                        updateInitialmodel(temp, context);
                        LoadAsyncData(allMedia, context);
                        if (SelectedSectionSync.size() > 0)
                            syncroniseSections(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //GenericData.ShowDialog(context,"Loading...",false);
                Log.d("Error", "Error");
            }
        });
        queue.add(jsonObjectRequest);
    }


    public static void syncroniseSections(final Context context) {
        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        ArrayList<Sections> model = new ArrayList<Sections>();
        RequestQueue queue = Volley.newRequestQueue(context);
        SyncSectionsClass obj = new SyncSectionsClass();
        obj.StoreCode = sharedPreferences.getString(GenericData.Sp_StoreCode, "");
        obj.SectionIds = SelectedSectionSync;
        /*params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        params.put("SectionIds", String.valueOf(SelectedSectionSync));*/
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(gson.toJson(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.sectiondata, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //GenericData.ShowDialog(context,"Loading...",false);
                if (response.optString("IsSuccess").matches("true")) {
                    try {
                        ArrayList<ImageCache> allMedia = new ArrayList<ImageCache>();
                        InitialModel temp = new InitialModel();
                        JSONObject jsonObject = response.getJSONObject("Data");
                        temp = gson.fromJson(jsonObject.toString(), InitialModel.class);
                        for (String id : SelectedSectionSync) {
                            for (Sections section : DB.getSectionlist()) {
                                if (section.getId().matches(id)) {
                                    temp.getSections().add(section);
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < temp.getCategories().size(); i++) {
                            if (DataVersion.CategoryVersion < temp.getCategories().get(i).getVersion())
                                DataVersion.CategoryVersion = temp.getCategories().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getCategories().get(i).getImageUrl(), temp.getCategories().get(i).getId(), context);
                            allMedia.add(param);
                        }

                        for (int i = 0; i < temp.getProducts().size(); i++) {
                            if (DataVersion.ProductVersion < temp.getProducts().get(i).getVersion())
                                DataVersion.ProductVersion = temp.getProducts().get(i).getVersion();
                            ImageCache param = new ImageCache(temp.getProducts().get(i).getImageUrl(), temp.getProducts().get(i).getId(), context);
                            allMedia.add(param);
                            for (int d = 0; d < temp.getProducts().get(i).getProductImages().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getProductImages().get(d), temp.getProducts().get(i).getId() + d + "", context);
                                allMedia.add(param);
                            }
                            for (int d = 0; d < temp.getProducts().get(i).getAttachments().size(); d++) {
                                param = new ImageCache(temp.getProducts().get(i).getAttachments().get(d).AttachmentUrl, temp.getProducts().get(i).getId() + "attachment" + d + "", context);
                                allMedia.add(param);
                            }
                        }

                        for (int j = 0; j < temp.getCollections().size(); j++) {
                            ImageCache param = new ImageCache(temp.getCollections().get(j).getImageUrl(), temp.getCollections().get(j).getId(), context);
                            allMedia.add(param);
                        }
                        LoadAsyncData(allMedia, context);
                        GenericData.imagesChached = true;
                        updateInitialmodel(temp, context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //GenericData.ShowDialog(context,"Loading...",false);
                Log.d("Error", "Error");
            }
        });
        queue.add(jsonObjectRequest);
    }

    public static void updateCustomerData(ArrayList<CustomerModel> model,Context context){
        ArrayList<CustomerModel> newCustomers = new ArrayList<CustomerModel>();
        for (CustomerModel newcustomer :model) {
            boolean matched = false;
            for (int i = 0; i < DB.getCustomers().size(); i++) {
                CustomerModel maincustomer = new CustomerModel();
                maincustomer = DB.getCustomers().get(i);
                if (newcustomer.getId().matches(maincustomer.getId())) {
                    matched = true;
                    DB.getCustomers().remove(maincustomer);
                    DB.getCustomers().add(i, newcustomer);
                    break;
                }
            }
            if (!matched)
                newCustomers.add(newcustomer);
        }
        for (CustomerModel customer : newCustomers) {
            DB.getCustomers().add(customer);
        }
        db = new DbHelper(context);
        db.savecustomers();
        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        /*SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(GenericData.Sp_SectionVersion,DataVersion.SectionVersion+"");
        editor.commit();*/
        db.loadinitialmodel();
        db.loadcustomers();
    }

    public static void updateInitialmodel(InitialModel temp, Context context) {
        //collections Update
        ArrayList<CollectionModel> newcollections = new ArrayList<CollectionModel>();
        for (CollectionModel newcollection : temp.getCollections()) {
            boolean matched = false;
            for (int i = 0; i < DB.getInitialModel().getCollections().size(); i++) {
                CollectionModel maincollection = new CollectionModel();
                maincollection = DB.getInitialModel().getCollections().get(i);
                if (newcollection.getId().matches(maincollection.getId())) {
                    matched = true;
                    DB.getInitialModel().getCollections().remove(maincollection);
                    DB.getInitialModel().getCollections().add(i, newcollection);
                    break;
                }
            }
            if (!matched)
                newcollections.add(newcollection);
        }
        for (CollectionModel collection : newcollections) {
            DB.getInitialModel().getCollections().add(collection);
        }


        //products update
        ArrayList<Products> newProducts = new ArrayList<Products>();
        for (Products newproduct : temp.getProducts()) {
            boolean matched = false;
            for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
                Products mainproduct = new Products();
                mainproduct = DB.getInitialModel().getProducts().get(i);
                if (newproduct.getId().matches(mainproduct.getId())) {
                    matched = true;
                    DB.getInitialModel().getProducts().remove(mainproduct);
                    DB.getInitialModel().getProducts().add(i, newproduct);
                    break;
                }
            }
            if (!matched)
                newProducts.add(newproduct);
        }
        for (Products newproduct : newProducts) {
            DB.getInitialModel().getProducts().add(newproduct);
        }


        //Sections Update
        ArrayList<Sections> newSections = new ArrayList<Sections>();
        for (Sections newSection : temp.getSections()) {
            boolean matched = false;
            for (int i = 0; i < DB.getInitialModel().getSections().size(); i++) {
                Sections mainSection = new Sections();
                mainSection = DB.getInitialModel().getSections().get(i);
                if (newSection.getId().matches(mainSection.getId())) {
                    matched = true;
                    DB.getInitialModel().getSections().remove(mainSection);
                    DB.getInitialModel().getSections().add(i, newSection);
                    break;
                }
            }
            if (!matched)
                newSections.add(newSection);
        }
        for (Sections newSection : newSections) {
            DB.getInitialModel().getSections().add(newSection);
        }


        //Sections Update
        ArrayList<Categories> newCategories = new ArrayList<Categories>();
        for (Categories newCategory : temp.getCategories()) {
            boolean matched = false;
            for (int i = 0; i < DB.getInitialModel().getCategories().size(); i++) {
                Categories mainCategory = new Categories();
                mainCategory = DB.getInitialModel().getCategories().get(i);
                if (newCategory.getId().matches(mainCategory.getId())) {
                    matched = true;
                    DB.getInitialModel().getCategories().remove(mainCategory);
                    DB.getInitialModel().getCategories().add(i, newCategory);
                    break;
                }
            }
            if (!matched)
                newCategories.add(newCategory);
        }
        for (Categories newCatagory : newCategories) {
            DB.getInitialModel().getCategories().add(newCatagory);
        }
        db = new DbHelper(context);
        db.saveinitialmodel();

        sharedPreferences = context.getSharedPreferences(GenericData.MyPref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(GenericData.Sp_StoragePath,ConfigData.selectedStoregePath);
        editor.putString(GenericData.Sp_StorageLoaction,ConfigData.selectedStoregelocation);
        editor.putString(GenericData.Sp_StorageFolder,ConfigData.selectedStoregefolder);
        editor.putString(GenericData.Sp_CategoryVersion,DataVersion.CategoryVersion+"");
        editor.putString(GenericData.Sp_CollectionVersion,DataVersion.CollectionVersion+"");
        editor.putString(GenericData.Sp_ProductVersion,DataVersion.ProductVersion+"");
        editor.putString(GenericData.Sp_SectionVersion,DataVersion.SectionVersion+"");
        editor.commit();

        db.loadinitialmodel();


    }

    public static void BillingProducts() {
        ArrayList<BillingProducts> billprodlist;
        BillingProducts billprod;
        billprodlist = new ArrayList<BillingProducts>();
        for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
            billprod = new BillingProducts();
            billprod.setId(DB.getInitialModel().getProducts().get(i).getId());
            billprod.setTitle(DB.getInitialModel().getProducts().get(i).getTitle());
            billprod.setDescription(DB.getInitialModel().getProducts().get(i).getDescription());
            billprod.setSectionId(DB.getInitialModel().getProducts().get(i).getSectionId());
            billprod.setCategoryId(DB.getInitialModel().getProducts().get(i).getCategoryId());
            billprod.setSKU(DB.getInitialModel().getProducts().get(i).getSKU());
            billprod.setBarCode(DB.getInitialModel().getProducts().get(i).getBarCode());
            billprod.setImageUrl(DB.getInitialModel().getProducts().get(i).getImageUrl());
            billprod.setVideoUrl(DB.getInitialModel().getProducts().get(i).getVideoUrl());
            billprod.setPdfUrl(DB.getInitialModel().getProducts().get(i).getPdfUrl());
            billprod.setMRP(DB.getInitialModel().getProducts().get(i).getMRP());
            billprod.setAmount(0.0);
            billprod.setQuantity(0);
            billprod.setPrice(DB.getInitialModel().getProducts().get(i).getSellingPrice());
            billprod.setSellingPrice(DB.getInitialModel().getProducts().get(i).getSellingPrice());
            billprod.setTags(DB.getInitialModel().getProducts().get(i).getTags());
            billprod.setStatus(DB.getInitialModel().getProducts().get(i).getStatus());
            billprod.setWeight(DB.getInitialModel().getProducts().get(i).getWeight());
            billprod.setWishList(DB.getInitialModel().getProducts().get(i).isWishList());
            billprod.setSelectedVarient(DB.getInitialModel().getProducts().get(i).getSelectedVarient());
            billprod.setProductImages(DB.getInitialModel().getProducts().get(i).getProductImages());
            billprod.setAttributes(DB.getInitialModel().getProducts().get(i).getAttributes());
            billprod.setVariants(DB.getInitialModel().getProducts().get(i).getVariants());
            billprodlist.add(billprod);
        }
        DB.setBillprodlist(billprodlist);
    }


    public static void ServerSyncOrders(final Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(GenericData.MyPref, mContext.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.ServerSyncOrders, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.optString("IsSuccess").matches("true"))
                    Toast.makeText(mContext, "Synced Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Synced Failed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "error");
            }
        });
        queue.add(jsonObjectRequest);
    }

    public static void ServerSyncShortlist(final Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(GenericData.MyPref, mContext.MODE_PRIVATE);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("StoreCode", sharedPreferences.getString(GenericData.Sp_StoreCode, ""));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.ServerShortlistOrders, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.optString("IsSuccess").matches("true"))
                    Toast.makeText(mContext, "Synced Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, "Synced Failed", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "error");
            }
        });
        queue.add(jsonObjectRequest);
    }
}
