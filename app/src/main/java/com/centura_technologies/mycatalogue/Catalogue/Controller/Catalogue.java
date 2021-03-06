package com.centura_technologies.mycatalogue.Catalogue.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.centura_technologies.mycatalogue.Catalogue.Model.BreadCrumb;
import com.centura_technologies.mycatalogue.Catalogue.Model.Categories;
import com.centura_technologies.mycatalogue.Catalogue.Model.CategoryTree;
import com.centura_technologies.mycatalogue.Catalogue.Model.Products;
import com.centura_technologies.mycatalogue.Catalogue.View.CatalogueAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.CatalogueAdapterNew;
import com.centura_technologies.mycatalogue.Catalogue.View.CategorylistAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.FilterAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.SearchAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.SearchProductsAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.SectionlistAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.TempFilterAdapter;
import com.centura_technologies.mycatalogue.R;
import com.centura_technologies.mycatalogue.Shortlist.Controller.CustomerShortlist;
import com.centura_technologies.mycatalogue.Shortlist.Controller.Shortlist;
import com.centura_technologies.mycatalogue.Shortlist.Model.ShortlistModel;
import com.centura_technologies.mycatalogue.Shortlist.View.CustomerShortlistAdapter;
import com.centura_technologies.mycatalogue.Support.Apis.Sync;
import com.centura_technologies.mycatalogue.Support.DBHelper.DB;
import com.centura_technologies.mycatalogue.Support.DBHelper.StaticData;
import com.centura_technologies.mycatalogue.Support.GenericData;
import com.centura_technologies.mycatalogue.test.CustomRecyclerView;
import com.centura_technologies.mycatalogue.test.GridViewItem;
import com.centura_technologies.mycatalogue.test.LinearLayoutManagerWithSmoothScroller;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.view.View.X;
import static android.view.View.getDefaultSize;
import static com.centura_technologies.mycatalogue.Catalogue.Controller.CatalogueDetails.context;
import static com.centura_technologies.mycatalogue.Support.DBHelper.StaticData.position;
import static java.security.AccessController.getContext;

/**
 * Created by Centura User1 on 19-09-2016.
 */
public class Catalogue extends AppCompatActivity {
    public static Toolbar toolbar;
    static RecyclerView cat_filterlist;
    ImageView listicon;
    public static DrawerLayout drawer;
    public static ImageView searchicon;
    RelativeLayout nocategory,quickview;
    static RelativeLayout fabpane,leftlay;
    static LinearLayout searchlayout, filterlayout, productlayout, filtericon, sortlay,fulllay,recyclerviewlayout;
    public static LinearLayout rightdrawer;
    public static EditText editsearch;
    Spinner spinner;
    TextView apply, clear;
    static RecyclerView recyclerview, recyclerview1, sectionrecycler;
    static RecyclerView productsrecyclerview;
    public static SearchProductsAdapter adapter;
    public static SearchAdapter adapter1;
    ArrayList<String> suggestionsData = new ArrayList<String>();
    public static ArrayList<CategoryTree> categories;
    Products filterprod;
    ArrayList<Products> categoryproducts = new ArrayList<Products>();
    public static ArrayList<Categories> category = new ArrayList<Categories>();
    List<String> sortby = new ArrayList<String>();
    List<String> filterlist;
    RelativeLayout.LayoutParams params;
    RelativeLayout specificationpane;
    static public String SearchString = "";
    static int SearchPageNumber = 0;
    static String item = "";
    TextView categoryicon;
    public static boolean grid_to_listflag = false;
    static ArrayList<Products> Localshortlist=new ArrayList<Products>();
    static ArrayList<Products> filterattr=new ArrayList<Products>();
    ArrayList<Products> list;
    boolean leftdraweropen=false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        toolbar.setTitle("My Catalogue");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        leftlay=(RelativeLayout)findViewById(R.id.leftlay);
        rightdrawer = (LinearLayout) findViewById(R.id.rightdrawer);
        filtericon = (LinearLayout) findViewById(R.id.filtericon);
        sortlay = (LinearLayout) findViewById(R.id.sortlay);
        categoryicon = (TextView) findViewById(R.id.categoryicon);
        fulllay= (LinearLayout) findViewById(R.id.fulllay);
        listicon = (ImageView) findViewById(R.id.listicon);
        cat_filterlist = (RecyclerView) findViewById(R.id.cat_filterlist);
        searchicon = (ImageView) findViewById(R.id.searchicon);
        quickview = (RelativeLayout) findViewById(R.id.quickview);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nocategory = (RelativeLayout) findViewById(R.id.nocategory);
        searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
        filterlayout = (LinearLayout) findViewById(R.id.filterlayout);
        productlayout = (LinearLayout) findViewById(R.id.productlayout);
        recyclerviewlayout=(LinearLayout)findViewById(R.id.recyclerviewlayout);
        fabpane = (RelativeLayout) findViewById(R.id.fabpane);
        specificationpane = (RelativeLayout) findViewById(R.id.specificationpane);
        params = (RelativeLayout.LayoutParams) (specificationpane).getLayoutParams();
        editsearch = (EditText) findViewById(R.id.editsearch);
        spinner = (Spinner) findViewById(R.id.spinner);
        apply = (TextView) findViewById(R.id.applyfilter);
        clear = (TextView) findViewById(R.id.cancelfiltertest);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview1 = (RecyclerView) findViewById(R.id.recyclerview1);
        sectionrecycler = (RecyclerView) findViewById(R.id.sectionrecycler);
        productsrecyclerview = (RecyclerView) findViewById(R.id.productsrecyclerview);
        //OverScrollDecoratorHelper.setUpOverScroll(productsrecyclerview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        //OverScrollDecoratorHelper.setUpOverScroll(productsrecyclerview);
        sectionrecycler.setLayoutManager(new LinearLayoutManager(Catalogue.this));
        cat_filterlist.setLayoutManager(new LinearLayoutManager(Catalogue.this));
        recyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 3));
        recyclerview1.setLayoutManager(new LinearLayoutManager(Catalogue.this));
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        /*float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 720) {
            //Device is a 10" tablet
            productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 5));

        }
        else if (smallestWidth > 600) {
            //Device is a 7" tablet
            productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 3));

        }*/

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;
        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;
        double diagonalInches = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));
        if (diagonalInches >= 8 && diagonalInches <= 10) {
            //Device is a 10" tablet
            productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 4));
        }
        else if (diagonalInches >= 5 && diagonalInches <= 8) {
            //Device is a 7" tablet
            productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 3));
        }
       // productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 5));

        adapter = new SearchProductsAdapter(Catalogue.this);
        adapter1 = new SearchAdapter(Catalogue.this, suggestionsData);

        sortby.add("Price low-high");
        sortby.add("Price high-low");
        sortby.add("A to Z");
        sortby.add("Z to A");

        leftdraweropen=true;

        productslist();
        categorylist();
        SortSpinner();
        setsuggestiondata();
        searchset();
        OnClicks();
        Sync.syncFilters(Catalogue.this, StaticData.Currentproducts);
        StaticData.ProductsInGrid = true;
        StaticData.ProductsInList = false;
        InitializeAdapter(Catalogue.this);
        InitialzationSectionAdapter(Catalogue.this);
        InitialzationCategoryAdapter(Catalogue.this, null);
        /*if (drawer.isDrawerOpen(leftdrawer)) {
            InitialzationSectionAdapter(Catalogue.this);
            InitialzationCategoryAdapter(Catalogue.this, null);
        }*/
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void SearchLogic() {
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().matches(""))
                {
                    productsrecyclerview.setAdapter(new CatalogueAdapter(Catalogue.this,StaticData.Currentproducts));
                }
                else {
                    DB.getInitialModel().setProducts(new ArrayList<Products>());
                    list=new ArrayList<Products>();
                    for (Products tempshortlist:Localshortlist) {
                        Boolean matched=false;
                        if(tempshortlist.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                            matched=true;
                        if(matched)
                            list.add(tempshortlist);
                    }
                    productsrecyclerview.setAdapter(new CatalogueAdapter(Catalogue.this,list));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static void productslist() {
        StaticData.Currentproducts = new ArrayList<Products>();
        if (StaticData.SelectedSection) {
            StaticData.SelectedCategoryId = DB.getInitialModel().getCategories().get(0).getId();
        }
        if (StaticData.SelectedCollection) {
            for (int j = 0; j < DB.getInitialModel().getProducts().size(); j++) {
                for (int k = 0; k < StaticData.SelectedCollectionProducts.size(); k++) {
                    if (DB.getInitialModel().getProducts().get(j).getId().matches(StaticData.SelectedCollectionProducts.get(k))) {
                        StaticData.Currentproducts.add(DB.getInitialModel().getProducts().get(j));
                        StaticData.SelectedCollection = false;
                    }
                }
            }
        } else if (StaticData.SelectedCategoryId.matches("-1")) {
            for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
                StaticData.Currentproducts.add(DB.getInitialModel().getProducts().get(i));
            }
        } else {
            for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
                if (DB.getInitialModel().getProducts().get(i).getCategoryId().matches(StaticData.SelectedCategoryId))
                    StaticData.Currentproducts.add(DB.getInitialModel().getProducts().get(i));
            }
        }
    }


    public static void categorylist() {
        categories = new ArrayList<CategoryTree>();
        for (int i = 0; i < DB.getTreelist().size(); i++) {
            categories.add(DB.getTreelist().get(i));
        }
    }

    private void OnClicks() {

        filtericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(rightdrawer);
                cat_filterlist.setAdapter(new TempFilterAdapter(Catalogue.this, StaticData.filtermodel.getItem()));
                searchlayout.setVisibility(View.GONE);
            }
        });

        sortlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Catalogue.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_sort);
                TextView lowtohigh, hightolow, atoz, ztoa;
                lowtohigh = (TextView) dialog.findViewById(R.id.lowtohigh);
                hightolow = (TextView) dialog.findViewById(R.id.hightolow);
                atoz = (TextView) dialog.findViewById(R.id.atoz);
                ztoa = (TextView) dialog.findViewById(R.id.ztoa);
                switch (item) {
                    case "Price low-high":
                        lowtohigh.setTextColor(Color.parseColor("#f37021"));
                        break;
                    case "Price high-low":
                        hightolow.setTextColor(Color.parseColor("#f37021"));
                        break;
                    case "A to Z":
                        atoz.setTextColor(Color.parseColor("#f37021"));
                        break;
                    case "Z to A":
                        ztoa.setTextColor(Color.parseColor("#f37021"));
                        break;
                }
                lowtohigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item = "Price low-high";
                        dialog.cancel();
                        InitializeAdapter(Catalogue.this);
                    }
                });
                hightolow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item = "Price high-low";
                        dialog.cancel();
                        InitializeAdapter(Catalogue.this);
                    }
                });
                atoz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item = "A to Z";
                        dialog.cancel();
                        InitializeAdapter(Catalogue.this);
                    }
                });
                ztoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item = "Z to A";
                        dialog.cancel();
                        InitializeAdapter(Catalogue.this);
                    }
                });
                dialog.show();
            }
        });

        categoryicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.GONE);
                drawer.closeDrawer(rightdrawer);
                InitialzationSectionAdapter(Catalogue.this);
                //InitialzationCategoryAdapter(Catalogue.this, null);
            }
        });

        listicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.GONE);
                productlayout.setVisibility(View.VISIBLE);
                if (grid_to_listflag) {
                    quickview.setVisibility(View.GONE);
                    StaticData.ProductsInGrid = true;
                    StaticData.ProductsInList = false;
                    //productsrecyclerview.setLayoutManager(new GridLayoutManager(Catalogue.this, 3));
                    listicon.setImageResource(R.drawable.ic_format_list_bulleted_white_24dp);
                    grid_to_listflag = false;
                } else {
                    quickview.setVisibility(View.VISIBLE);
                    setspecificationstoRight();
                    StaticData.ProductsInGrid = false;
                    StaticData.ProductsInList = true;
                    listicon.setImageResource(R.drawable.ic_view_grid_white_24dp);
                    grid_to_listflag = true;
                }
                InitializeAdapter(Catalogue.this);
            }
        });

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlayout.setVisibility(View.VISIBLE);
                productlayout.setVisibility(View.GONE);
                SearchApi(Catalogue.this);
                //SearchLogic();

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productslist();
                categoryproducts = new ArrayList<Products>();
                categoryproducts = StaticData.Currentproducts;
                if (StaticData.filter != "") {
                    boolean matched = false;
                    filterprod = new Products();
                    StaticData.Currentproducts = new ArrayList<Products>();
                    StaticData.filter = StaticData.filter.substring(1, StaticData.filter.length());
                    filterlist = new ArrayList<String>(Arrays.asList(StaticData.filter.split(",")));
                    for (int i = 0; i < categoryproducts.size(); i++) {
                        matched = false;
                        for (int j = 0; j < categoryproducts.get(i).getAttributes().size(); j++) {
                            if (matched)
                                break;
                            for (int k = 0; k < filterlist.size(); k++) {
                                if (categoryproducts.get(i).getAttributes().get(j).getAttributeValue().matches(filterlist.get(k))) {
                                    filterprod = categoryproducts.get(i);
                                    StaticData.Currentproducts.add(filterprod);
                                    matched = true;
                                    break;
                                }
                            }
                        }
                    }
                    InitializeAdapter(Catalogue.this);
                    drawer.closeDrawer(rightdrawer);
                } else {
                    Toast.makeText(Catalogue.this, "Filter cannot be apply without selected", Toast.LENGTH_SHORT).show();
                    InitializeAdapter(Catalogue.this);
                    drawer.closeDrawer(rightdrawer);
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticData.filter = "";
                for (int x = 0; x < StaticData.filtermodel.getItem().size(); x++) {
                    for (int y = 0; y < StaticData.filtermodel.getItem().get(x).getValue().size(); y++) {
                        StaticData.filtermodel.getItem().get(x).getValue().get(y).Selected = false;
                    }
                }
                cat_filterlist.setAdapter(new TempFilterAdapter(Catalogue.this, StaticData.filtermodel.getItem()));
            }
        });
    }

    private void SortSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Catalogue.this, R.layout.spinner_item, sortby);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                InitializeAdapter(Catalogue.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();
        spinnerDrawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner.setBackground(spinnerDrawable);
        } else {
            spinner.setBackgroundDrawable(spinnerDrawable);
        }
    }

    public void searchset() {
        editsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchlayout.setVisibility(View.VISIBLE);
                    productlayout.setVisibility(View.GONE);
                    searchicon.performClick();
                    return true;
                }
                return false;
            }
        });

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchProductsAdapter.data = new ArrayList<Products>();
                SearchAdapter.data = new ArrayList<String>();
                if (s.length() > 0) {
                    for (String temp : suggestionsData) {
                        if (temp.toLowerCase().contains(s.toString().toLowerCase()))
                            SearchAdapter.data.add(temp);
                        nocategory.setVisibility(View.GONE);
                        searchlayout.setVisibility(View.VISIBLE);
                        adapter1.notifyDataSetChanged();
                    }
                    recyclerview.setAdapter(adapter1);
                } else {
                    SearchAdapter.data = new ArrayList<String>();
                    adapter1.notifyDataSetChanged();
                    SearchProductsAdapter.data = new ArrayList<Products>();
                    adapter.notifyDataSetChanged();
                    recyclerview.setVisibility(View.VISIBLE);
                    nocategory.setVisibility(View.GONE);
                    searchlayout.setVisibility(View.GONE);
                    searchicon.performClick();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setsuggestiondata() {
        suggestionsData = new ArrayList<String>();
        for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
            suggestionsData.add(DB.getInitialModel().getProducts().get(i).getTitle());
        }
        searchlayout.setVisibility(View.VISIBLE);
        recyclerview1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }

    private void SearchApi(final Context context) {
        SearchString = editsearch.getText().toString();
        SearchPageNumber = 0;
        SearchProductsAdapter.data = new ArrayList<Products>();
        Products model = new Products();
        for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
            if (DB.getInitialModel().getProducts().get(i).getTitle().toLowerCase().contains(SearchString.toLowerCase()))
                SearchProductsAdapter.data.add(DB.getInitialModel().getProducts().get(i));
        }
        StaticData.Currentproducts = SearchProductsAdapter.data;
        adapter.notifyDataSetChanged();
        if (SearchProductsAdapter.data.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            nocategory.setVisibility(View.VISIBLE);
        } else {
            nocategory.setVisibility(View.GONE);
            searchlayout.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview.setAdapter(adapter);
        }
    }

    public static void InitialzationCategoryAdapter(Context context, CategoryTree categoryTree) {
    }

    public static void InitialzationSectionAdapter(Context context) {
        sectionrecycler.setAdapter(new SectionlistAdapter(context, null));
    }

    public static void InitializeAdapter(Context context) {
        productsrecyclerview.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fadein));
        searchlayout.setVisibility(View.GONE);
        productlayout.setVisibility(View.VISIBLE);
        if (item.matches("Price low-high")) {
            Collections.sort(StaticData.Currentproducts, new Comparator<Products>() {
                public int compare(Products p1, Products p2) {
                    if (p1.getSellingPrice() == p2.getSellingPrice())
                        return 0;
                    return p1.getSellingPrice() < p2.getSellingPrice() ? -1 : 1;
                }
            });
        } else if (item.matches("Price high-low")) {
            Collections.sort(StaticData.Currentproducts, new Comparator<Products>() {
                public int compare(Products p1, Products p2) {
                    if (p1.getSellingPrice() == p2.getSellingPrice())
                        return 0;
                    return p1.getSellingPrice() > p2.getSellingPrice() ? -1 : 1;
                }
            });
        } else if (item.matches("A to Z")) {
            Collections.sort(StaticData.Currentproducts, new Comparator<Products>() {
                public int compare(Products v1, Products v2) {
                    if (v1.getTitle().toLowerCase() == v2.getTitle().toLowerCase())
                        return 0;
                    return v1.getTitle().toLowerCase().compareTo(v2.getTitle().toLowerCase());
                }
            });
        } else {
            Collections.sort(StaticData.Currentproducts, new Comparator<Products>() {
                public int compare(Products v1, Products v2) {
                    if (v1.getTitle().toLowerCase() == v2.getTitle().toLowerCase())
                        return 0;
                    return v2.getTitle().toLowerCase().compareTo(v1.getTitle().toLowerCase());
                }
            });
        }

        //productsrecyclerview.setNestedScrollingEnabled(false);
        productsrecyclerview.setAdapter(new CatalogueAdapter(context, StaticData.Currentproducts));
        //productsrecyclerview.setAdapter(new CatalogueAdapterNew(context, products));
        for (int k=0;k<DB.getInitialModel().getProducts().size();k++){
            filterattr.add(DB.getInitialModel().getProducts().get(k));
        }
        Sync.syncFilters(context, filterattr);
        if (StaticData.filtermodel.getItem() != null)
            cat_filterlist.setAdapter(new TempFilterAdapter(context, StaticData.filtermodel.getItem()));
    }

    void setspecificationstoRight() {
        params.addRule(RelativeLayout.RIGHT_OF, R.id.leftlayout);
        params.addRule(RelativeLayout.BELOW, 0);
        specificationpane.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem logout = menu.findItem(R.id.logout);
        logout.setVisible(false);
        MenuItem slideshow = menu.findItem(R.id.slideshow);
        slideshow.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shortlist) {
            startActivity(new Intent(Catalogue.this, Shortlist.class));
        }
        if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(rightdrawer))
                drawer.closeDrawer(rightdrawer);
            else
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializeAdapter(Catalogue.this);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Catalogue Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
