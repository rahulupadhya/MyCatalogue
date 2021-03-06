package com.centura_technologies.mycatalogue.Catalogue.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.centura_technologies.mycatalogue.Catalogue.Model.AttchmentClass;
import com.centura_technologies.mycatalogue.Catalogue.Model.DescriptionMenuClass;
import com.centura_technologies.mycatalogue.Catalogue.Model.Products;
import com.centura_technologies.mycatalogue.Catalogue.Model.ShortlistProductModel;
import com.centura_technologies.mycatalogue.Catalogue.Model.VarientModel;
import com.centura_technologies.mycatalogue.Catalogue.View.AttchmentsAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.DescriptionAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.DetailMenuAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.DrawerItemsAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.IndividualProdImageAdapter;
import com.centura_technologies.mycatalogue.Catalogue.View.VarientsAdapter;
import com.centura_technologies.mycatalogue.R;
import com.centura_technologies.mycatalogue.Shortlist.Controller.Shortlist;
import com.centura_technologies.mycatalogue.Support.DBHelper.DB;
import com.centura_technologies.mycatalogue.Support.GenericData;
import com.centura_technologies.mycatalogue.Support.DBHelper.StaticData;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.panoramagl.PLImage;
import com.panoramagl.PLManager;

import java.io.File;
import java.util.ArrayList;

import static com.centura_technologies.mycatalogue.R.id.logoff;

/**
 * Created by Centura User1 on 24-08-2016.
 */
public class CatalogueDetails extends SwipeActivity implements VarientsAdapter.ClickListner, DrawerLayout.DrawerListener {
    ImageView hamburger, logff;
    TextView Title;
    static TextView shortlist,specpane;
    static RecyclerView productdetaillist, drawer_items_recycler, individual_product_images;
    public static ArrayList<Products> allproducts;
    static ImageView openimage, next, previous, media, arrow;
    static TextView title, description, amount, variencetext;
    static LinearLayout varients;
    LinearLayout images, videos, pdfs, panorama;
    static Context context;
    static Products productModel;
    static Dialog dialog;
    private static ViewGroup mRootView;
    float Draweropen = 0;
    DrawerLayout drawer;
    public static ArrayList<String> image;
    static ScrollView scrollView;
    public static String videourl = "";
    public static String pdfurl = "";
    static RecyclerView menulyaout;
    static ImageView productImage;
    static VideoView productDetailvedio;
    LinearLayout toppane;
    static LinearLayout imagelayout, vediolayout, weblayout, pdflayout, panoramalayout, infolayout;
    private int screenhight;
    private RelativeLayout.LayoutParams paramsNotFullscreen;
    static ArrayList<ShortlistProductModel> list;
    static ShortlistProductModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cataloguedetails);
        Title = (TextView) findViewById(R.id.AppbarTittle);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Title.setText("Product Details");
        hamburger = (ImageView) findViewById(R.id.hamburger);
        logff = (ImageView) findViewById(logoff);
        context = CatalogueDetails.this;
        mRootView = (ViewGroup) findViewById(R.id.fulllay);
        allproducts = new ArrayList<Products>();
        productdetaillist = (RecyclerView) findViewById(R.id.productdetaillist);
        individual_product_images = (RecyclerView) findViewById(R.id.individual_product_images);
        drawer_items_recycler = (RecyclerView) findViewById(R.id.drawer_items_recycler1);
        openimage = (ImageView) findViewById(R.id.openimage);
        variencetext = (TextView) findViewById(R.id.variencetext);
        next = (ImageView) findViewById(R.id.next);
        previous = (ImageView) findViewById(R.id.previous);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        amount = (TextView) findViewById(R.id.amount);
        specpane=(TextView)findViewById(R.id.specpane);
        varients = (LinearLayout) findViewById(R.id.varients);
        arrow = (ImageView) findViewById(R.id.arrow);
        shortlist = (TextView) findViewById(R.id.shortlist);
        media = (ImageView) findViewById(R.id.media);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        toppane = (LinearLayout) findViewById(R.id.toppane);
        menulyaout = (RecyclerView) findViewById(R.id.menulyaout);
        imagelayout = (LinearLayout) findViewById(R.id.imagelayout);
        vediolayout = (LinearLayout) findViewById(R.id.vediolayout);
        weblayout = (LinearLayout) findViewById(R.id.weblayout);
        pdflayout = (LinearLayout) findViewById(R.id.pdflayout);
        panoramalayout = (LinearLayout) findViewById(R.id.pptlayout);
        infolayout = (LinearLayout) findViewById(R.id.infolayout);
        productImage = (ImageView) findViewById(R.id.productDetailImageview);
        productDetailvedio = (VideoView) findViewById(R.id.productDetailvedio);
        menulyaout.setLayoutManager(new LinearLayoutManager(CatalogueDetails.this));
        list =new ArrayList<ShortlistProductModel>();
        UiManuplation();

        if (StaticData.ClickedProduct) {
            for (int i = 0; i < DB.getInitialModel().getProducts().size(); i++) {
                if (DB.getInitialModel().getProducts().get(i).getSectionId().matches(StaticData.SelectedSectionId)) {
                    allproducts.add(DB.getInitialModel().getProducts().get(i));
                }
            }
        } else {
            allproducts = StaticData.Currentproducts;
        }

        if (allproducts != null)
            if (allproducts.size() > 0)
                RenderProduct(allproducts.get(StaticData.productposition));
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(this);

        if (allproducts.size() == 1) {
            next.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }
        setOnClicks();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
            paramsNotFullscreen = (RelativeLayout.LayoutParams) productDetailvedio.getLayoutParams();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(paramsNotFullscreen);
            params.setMargins(0, 0, 0, 0);
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            productDetailvedio.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            productDetailvedio.setLayoutParams(paramsNotFullscreen);
        }
    }

    @Override
    protected void previous() {
        if (Draweropen == 0) {
            if (StaticData.productposition > 0)
                RenderProduct(allproducts.get(--StaticData.productposition));
        } else
            drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void next() {
        if (Draweropen == 0) {
            if (StaticData.productposition < allproducts.size() - 1)
                RenderProduct(allproducts.get(++StaticData.productposition));
        } else
            drawer.closeDrawer(Gravity.LEFT);
    }

    private void setOnClicks() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticData.productposition < allproducts.size() - 1) {
                    RenderProduct(allproducts.get(++StaticData.productposition));
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticData.productposition > 0) {
                    RenderProduct(allproducts.get(--StaticData.productposition));
                }
            }
        });
        /*mediatext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(CatalogueDetails.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.media);
                images = (LinearLayout) dialog.findViewById(R.id.image);
                videos = (LinearLayout) dialog.findViewById(R.id.video);
                pdfs = (LinearLayout) dialog.findViewById(R.id.pdf);
                panorama = (LinearLayout) dialog.findViewById(R.id.ppt);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = GenericData.convertDpToPixels(500, CatalogueDetails.this);
                lp.height = GenericData.convertDpToPixels(500, CatalogueDetails.this);
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(CatalogueDetails.this,ImageActivity.class));
                    }
                });
                videos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videourl = allproducts.get(StaticData.position).getVideoUrl();
                        startActivity(new Intent(CatalogueDetails.this, VideoActivity.class));
                    }
                });
                pdfs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pdfurl = allproducts.get(StaticData.position).getPdfUrl();
                        startActivity(new Intent(CatalogueDetails.this, PdfActivity.class));
                    }
                });
                panorama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

            }
        });*/

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CatalogueDetails.this, Shortlist.class));
                finish();
            }
        });
    }

    public static void RenderProduct(final Products productdetail) {
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        checkshortlist(productdetail);
        mRootView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fadein));
        LoadInfo();
        /*ArrayList<DescriptionMenuClass> menudata = new ArrayList<DescriptionMenuClass>();
        for (String imagedata : productdetail.getProductImages()) {
            if (imagedata != null)
                if (!imagedata.matches("")) {
                    menudata.add(new DescriptionMenuClass(imagedata, false, "Image"));
                }
        }

        for (AttchmentClass attachmentobject : productdetail.getAttachments()) {
            if (attachmentobject.AttachmentUrl != null)
                if (!attachmentobject.AttachmentUrl.matches("")) {
                    menudata.add(new DescriptionMenuClass(attachmentobject.AttachmentUrl, true, attachmentobject.AttachmentTitle));
                }
        }*/

        menulyaout.setAdapter(new AttchmentsAdapter(context, productdetail.getAttachementTree()));
        productModel = productdetail;
        image = new ArrayList<String>();
        for (int i = 0; i < productModel.getProductImages().size(); i++)
            if (productModel.getProductImages().get(i) != null)
                if (!productModel.getProductImages().get(i).matches(""))
                    image.add(productModel.getProductImages().get(i));
        if (productModel.getVariants().size() == 0) {
            varients.setVisibility(View.GONE);
        } else {
            varients.setVisibility(View.GONE);
            variencetext.setText(productModel.getSelectedVarient().toString());
        }
        varients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productModel.getVariants().size() == 0) {
                    varients.setVisibility(View.GONE);
                } else if (productModel.getVariants().size() == 1) {
                    varients.setVisibility(View.GONE);
                    VarientsDialog(productModel.getVariants());
                    variencetext.setText(productModel.getSelectedVarient());
                } else {
                    VarientsDialog(productModel.getVariants());
                    variencetext.setText(productModel.getSelectedVarient());
                }
            }
        });

        GenericData.setImage(productModel.getImageUrl(), openimage, context);
        openimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ImageViewer.class);
                i.putExtra("url", productModel.getImageUrl());
                ((Activity) context).startActivity(i);
            }
        });
        title.setText(productModel.getTitle());
        description.setText(GenericData.formatHtml(productModel.getDescription()));
        amount.setText(productModel.getSellingPrice() + "");
        productdetaillist.setLayoutManager(new LinearLayoutManager(context));
        int viewHeight = GenericData.convertDpToPixels(50, context);
        viewHeight = viewHeight * (productModel.getAttributes().size());
        productdetaillist.getLayoutParams().height = viewHeight;
        if(productModel.getAttributes().size()!=0){
            specpane.setVisibility(View.VISIBLE);
            productdetaillist.setVisibility(View.VISIBLE);
            productdetaillist.setAdapter(new DescriptionAdapter(context, productModel.getAttributes()));
        }else {
            specpane.setVisibility(View.GONE);
            productdetaillist.setVisibility(View.GONE);
        }
        drawer_items_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        drawer_items_recycler.setAdapter(new DrawerItemsAdapter(context, allproducts));
        scrollchild();
        LinearLayoutManager layoutManager1 = new GridLayoutManager(context, 2);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        individual_product_images.setLayoutManager(layoutManager1);
        individual_product_images.setAdapter(new IndividualProdImageAdapter(context, productModel.getProductImages(), openimage));
        productClicks(productdetail);

    }

    private static void productClicks(final Products productdetail) {
        shortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                for (ShortlistProductModel model : DB.getShortlistproductmodel()) {
                    if (model.getId().matches(productdetail.getId())) {
                        shortlist.setText("+ Add To Cart");
                        DB.getShortlistproductmodel().remove(model);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    shortlist.setText("- Remove Cart");
                    model=new ShortlistProductModel();
                    model.setId(productdetail.getId());
                    model.setSectionId(productdetail.getSectionId());
                    model.setCategoryId(productdetail.getCategoryId());
                    model.setImageUrl(productdetail.getImageUrl());
                    model.setProductImages(productdetail.getProductImages());
                    model.setTitle(productdetail.getTitle());
                    model.setDescription(productdetail.getDescription());
                    model.setSKU(productdetail.getSKU());
                    model.setBarCode(productdetail.getBarCode());
                    model.setAttachments(productdetail.getAttachments());
                    model.setVideoUrl(productdetail.getVideoUrl());
                    model.setPdfUrl(productdetail.getPdfUrl());
                    model.setMRP(productdetail.getMRP());
                    model.setAmount(0.0);
                    model.setQuantity(0);
                    model.setPrice(productdetail.getSellingPrice());
                    model.setSellingPrice(productdetail.getSellingPrice());
                    model.setAttributes(productdetail.getAttributes());
                    model.setVariants(productdetail.getVariants());
                    model.setTags(productdetail.getTags());
                    model.setStatus(productdetail.getStatus());
                    model.setWeight(productdetail.getWeight());
                    model.setWishList(productdetail.isWishList());
                    model.setVersion(productdetail.getVersion());
                    model.setSelectedVarient(productdetail.getSelectedVarient());
                    list.add(model);
                    DB.setShortlistproductmodel(list);
                }
            }
        });
    }

    private static void checkshortlist(Products product) {
        for (ShortlistProductModel shorlisted : DB.getShortlistproductmodel()) {
            shortlist.setText("+ Add To Cart");
            if (product.getId().matches(shorlisted.getId())) {
                shortlist.setText("- Remove Cart");
                break;
            }
        }
    }

    private static void scrollchild() {
        productdetaillist.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private static void VarientsDialog(ArrayList<VarientModel> model) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_varients_dropdown);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.varientsrecycle);
        switch (model.size()) {
            case 1:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(50, context);
                break;
            case 2:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(100, context);
                break;
            case 3:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(150, context);
                break;
            case 4:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(200, context);
                break;
            case 5:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(150, context);
                break;
            default:
                recyclerView.getLayoutParams().height = GenericData.convertDpToPixels(300, context);
                break;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        VarientsAdapter adapter = new VarientsAdapter(context, model);
        adapter.setClickListner((VarientsAdapter.ClickListner) context);
        recyclerView.setAdapter(adapter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void itemClicked(View v, int position) {
        dialog.cancel();
        productModel.setSelectedVarient(productModel.getVariants().get(position).getVariance());
        variencetext.setText(productModel.getSelectedVarient());
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        Draweropen = slideOffset;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void LoadInfo() {
        imagelayout.setVisibility(View.GONE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        panoramalayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.VISIBLE);
        productDetailvedio.stopPlayback();
    }

    public static void LoadHTML(String url) {
        url = "file:///" +  url;
        imagelayout.setVisibility(View.GONE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.VISIBLE);
        panoramalayout.setVisibility(View.GONE);
        Intent intent=new Intent(context,HTMLPage.class);
        intent.putExtra("URL",url);
        ((Activity)context).startActivity(intent);
        productDetailvedio.stopPlayback();
    }

    public static void LoadVedio(Context context, String url) {
        imagelayout.setVisibility(View.GONE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        panoramalayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.VISIBLE);
        Intent i = new Intent(context, VideoActivity.class);
        i.putExtra("url", url);
        ((Activity) context).startActivity(i);
        /*productDetailvedio.setVideoURI(vidUri);
        productDetailvedio.start();
        MediaController vidControl = new MediaController(context);
        vidControl.setAnchorView(productDetailvedio);
        productDetailvedio.setMediaController(vidControl);*/
    }

    public static void LoadPDF(final Context context, String url) {
        imagelayout.setVisibility(View.GONE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        panoramalayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(context, PdfActivity.class);
        intent.putExtra("url", url);
        productDetailvedio.stopPlayback();
        ((Activity) context).startActivity(intent);
    }

    public static void LoadImage(final Context context, final String url) {
        imagelayout.setVisibility(View.VISIBLE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        panoramalayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.GONE);
        productDetailvedio.stopPlayback();
        GenericData.setImage(url, productImage, context);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageViewer.class);
                i.putExtra("url", url);
                ((Activity) context).startActivity(i);
                //((Activity) context).startActivity(new Intent(context, ImageViewer.class));
            }
        });
    }

    public static void LoadPanorama(Context context, String url) {
        productDetailvedio.stopPlayback();
        imagelayout.setVisibility(View.GONE);
        vediolayout.setVisibility(View.GONE);
        weblayout.setVisibility(View.GONE);
        pdflayout.setVisibility(View.GONE);
        panoramalayout.setVisibility(View.GONE);
        infolayout.setVisibility(View.VISIBLE);
        Intent i = new Intent(context, Panorama.class);
        i.putExtra("url", url);
        ((Activity) context).startActivity(i);
    }

    private void UiManuplation() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenhight = metrics.heightPixels;
        ViewTreeObserver vto1 = toppane.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                toppane.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                toppane.getLayoutParams().height = screenhight - GenericData.convertDpToPixels(90, context);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
