package www.cheemarket.com.javadesmaeili;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import www.cheemarket.com.javadesmaeili.Customview.Dialogs;
import www.cheemarket.com.javadesmaeili.Adapter.Adapter;
import www.cheemarket.com.javadesmaeili.Structure.KalaStructure;
import www.cheemarket.com.javadesmaeili.Structure.SliderStructure;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static Activity thisactivity;


    private static RecyclerView RecyclerViewList1;
    private static RecyclerView RecyclerViewList4;
    private static RecyclerView RecyclerViewList6;
    private static RecyclerView.LayoutManager LayoutManagerList1;
    private static RecyclerView.LayoutManager LayoutManagerList4;
    private static RecyclerView.LayoutManager LayoutManagerList6;
    public static RecyclerView.Adapter AdapterList1;
    public static RecyclerView.Adapter AdapterList4;
    public static RecyclerView.Adapter AdapterList6;
    public static ArrayList<KalaStructure> mdatasetList1;
    public static ArrayList<KalaStructure> mdatasetList4;
    public static ArrayList<KalaStructure> mdatasetList6;


    private static ImageView[] imgs = new ImageView[6];

    static ScrollView scroll;
    private static Webservice req;
    private static DrawerLayout drawer;
    private static TextView txtprofile;
    public static SharedPreferences pre;
    NavigationView navigationView;
    public static ViewPager viewPager;
    public static CircleIndicator circleIndicator;

    public static boolean needpagework = false;

    static String Datetimeserver;
    static TextView h;
    static TextView m;
    static TextView s;

    static int saat = 0;
    static int daghighe = 0;
    static int saniye = 0;


    @Override
    protected void onResume() {
        super.onResume();

        Slider.setsliders();

        G.CurrentActivity = this;
        if (pre.contains("Username") && pre.contains("Id")) {
            if (!pre.getString("Username", "Error").equals("Error") && !pre.getString("Id", "Error").equals("")) {
              //  txtprofile.setText(pre.getString("Username", "Error"));
                Textconfig.settext(txtprofile,pre.getString("Username", "Error"));
                G.userid = Long.parseLong(pre.getString("Id", "Error"));
            }
        }


        navigationView.getMenu().findItem(R.id.exit).setChecked(false);
        navigationView.getMenu().findItem(R.id.category).setChecked(false);
        navigationView.getMenu().findItem(R.id.sabadkharid).setChecked(false);
        navigationView.getMenu().findItem(R.id.address).setChecked(false);

        if (needpagework) {
            needpagework = false;
            pagework();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        thisactivity = this;
        G.CurrentActivity = this;

        pre = getSharedPreferences("Barana", MODE_PRIVATE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtprofile = (TextView) header.findViewById(R.id.txtprofile);


        RecyclerViewList1 = (RecyclerView) findViewById(R.id.List1);
        RecyclerViewList4 = (RecyclerView) findViewById(R.id.List4);
        RecyclerViewList6 = (RecyclerView) findViewById(R.id.List6);
        RecyclerViewList1.setFocusable(false);
        RecyclerViewList4.setFocusable(false);
        RecyclerViewList6.setFocusable(false);

        imgs[0] = (ImageView) findViewById(R.id.post2);
        imgs[1] = (ImageView) findViewById(R.id.post3);
        imgs[2] = (ImageView) findViewById(R.id.post4);
        imgs[3] = (ImageView) findViewById(R.id.post5);
        imgs[4] = (ImageView) findViewById(R.id.post6);
        imgs[5] = (ImageView) findViewById(R.id.post8);

        circleIndicator = findViewById(R.id.circle);
        viewPager = findViewById(R.id.view_pager);

        h = (TextView) findViewById(R.id.h);
        m = (TextView) findViewById(R.id.m);
        s = (TextView) findViewById(R.id.s);


        scroll = (ScrollView) findViewById(R.id.scroll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ImageView shoplogo = (ImageView) findViewById(R.id.shoplogo);

        shoplogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(G.CurrentActivity,SabadActivity.class);
                //  startActivity(intent);
            }
        });

        ImageView searchlogo = (ImageView) findViewById(R.id.searchlogo);
        searchlogo.setOnClickListener(G.onClickListener);

        if (!G.readNetworkStatus()) {
            // Intent intent = new Intent(G.CurrentActivity,Networkactivity.class);
            // startActivity(intent);

        } else {

            // addview.add("App");
            pagework();

        }


    }


    public static void pagework() {

        if (pre.contains("Username") && pre.contains("Id")) {
            if (!pre.getString("Username", "Error").equals("Error") && !pre.getString("Id", "Error").equals("")) {
                txtprofile.setText(pre.getString("Username", "Error"));
                Textconfig.settext(txtprofile,pre.getString("Username", "Error"));
                G.userid = Long.parseLong(pre.getString("Id", "Error"));
            }
        }
        index = -1;

        Dialogs.Checkpermissions();

        mdatasetList1 = new ArrayList<KalaStructure>();
        mdatasetList4 = new ArrayList<KalaStructure>();
        mdatasetList6 = new ArrayList<KalaStructure>();


        RecyclerViewList1.setHasFixedSize(true);
        LayoutManagerList1 = new LinearLayoutManager(G.CurrentActivity, LinearLayoutManager.HORIZONTAL, false);
        RecyclerViewList1.setLayoutManager(LayoutManagerList1);
        AdapterList1 = new Adapter(mdatasetList1, R.layout.listone);
        RecyclerViewList1.setAdapter(AdapterList1);


        RecyclerViewList4.setHasFixedSize(true);
        LayoutManagerList4 = new LinearLayoutManager(G.CurrentActivity, LinearLayoutManager.HORIZONTAL, false);
        RecyclerViewList4.setLayoutManager(LayoutManagerList4);
        AdapterList4 = new Adapter(mdatasetList4, R.layout.listone);
        RecyclerViewList4.setAdapter(AdapterList4);


        RecyclerViewList6.setHasFixedSize(true);
        LayoutManagerList6 = new LinearLayoutManager(G.CurrentActivity, LinearLayoutManager.HORIZONTAL, false);
        RecyclerViewList6.setLayoutManager(LayoutManagerList6);
        AdapterList6 = new Adapter(mdatasetList6, R.layout.listone);
        RecyclerViewList6.setAdapter(AdapterList6);

        req = new Webservice()
                .setDatalistener(datalistener)
                .downloaddata(G.Baseurl + "Store.php?action=Firstpagedata", null);


        txtprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (G.userid == -1) {
                    //     Intent intent = new Intent(G.CurrentActivity, LoginActivity.class);
                    //   G.CurrentActivity.startActivity(intent);
                }
            }
        });


    }

    static int index = -1;
    static OndownloadListener datalistener = new OndownloadListener() {


        @Override
        public void Oncompelet(final String input) throws JSONException {

            //  index = -1;
            mdatasetList1.clear();
            mdatasetList4.clear();
            mdatasetList6.clear();

            JSONArray array = new JSONArray(input);

            for (int i = 0; i < array.length(); i++) {
                final JSONObject jsonObject = array.getJSONObject(i);

                if (jsonObject.has("Name") && jsonObject.getString("Postimage").equals("")) {

                    KalaStructure kalaStructure = Converts.convertinputdata(jsonObject);

                    if (jsonObject.getString("Location").equals("1")) {
                        mdatasetList1.add(kalaStructure);
                    } else if (jsonObject.getString("Location").equals("7")) {
                        mdatasetList4.add(kalaStructure);
                    } else if (jsonObject.getString("Location").equals("9")) {
                        mdatasetList6.add(kalaStructure);
                    }

                } else {

                    if (jsonObject.getString("Location").equals("2")) {
                        index = 0;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("3")) {
                        index = 1;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("4")) {
                        index = 2;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("5")) {
                        index = 3;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("6")) {
                        index = 4;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("8")) {
                        index = 5;
                        showimage(imgs[index], jsonObject);
                    } else if (jsonObject.getString("Location").equals("10")) {
                        SliderStructure sliderStructure = new SliderStructure();
                        sliderStructure.Postimage = jsonObject.getString("Postimage");
                        sliderStructure.Caption = "";
                        Slider.array.add(sliderStructure);
                    }


                    setonclicks(imgs[index],jsonObject);


                }


            }

            Slider.setsliders();


            G.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    AdapterList1.notifyDataSetChanged();
                    AdapterList4.notifyDataSetChanged();
                    AdapterList6.notifyDataSetChanged();
                }
            });
            Datetimeserver = mdatasetList1.get(0).Datetime1;
            if (Datetimeserver.equals("0000-00-00 00:00:00")) {
                G.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        Textconfig.settext(h,saat + "");
                        Textconfig.settext(m,daghighe + "");
                        Textconfig.settext(s,saniye + "");

                    }
                });

                return;
            }
            Log.i("LOG", "Date =" + Datetimeserver);
            int roz = Integer.parseInt(Datetimeserver.substring(Datetimeserver.lastIndexOf("-") + 1, Datetimeserver.lastIndexOf("-") + 3).trim());
            roz = roz * 24;
            saat = roz + Integer.parseInt(Datetimeserver.substring(Datetimeserver.indexOf(" ") + 1, Datetimeserver.indexOf(" ") + 3).replace(":", ""));///
            daghighe = Integer.parseInt(Datetimeserver.substring(Datetimeserver.indexOf(":") + 1, Datetimeserver.indexOf(":") + 3).replace(":", ""));

            saniye = Integer.parseInt(Datetimeserver.substring(Datetimeserver.lastIndexOf(":") + 1, Datetimeserver.length()));

            Log.i("LOG", "saat =" + saat + "//daghighe =" + daghighe + "//" + saniye);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        while (true) {

                            G.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {

                                    Textconfig.settext(h,saat + "");
                                    Textconfig.settext(m,daghighe + "");
                                    Textconfig.settext(s,saniye + "");

                                }
                            });

                            if (saat == 0 && daghighe == 0 && saniye == 0) {

                                break;
                            }
                            Thread.sleep(1000);
                            if (saniye > 0) {
                                saniye--;
                            } else if (daghighe > 0) {
                                daghighe--;
                                saniye = 60;
                            } else if (saat > 0) {
                                saat--;
                                daghighe = 60;

                            }


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();


        }

        @Override
        public void onProgressDownload(int percent) {

        }
    };


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.category) {
            Intent intent = new Intent(G.CurrentActivity,  Dastebandimahsolat.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            G.userid = -1;
            SharedPreferences.Editor editor = ActivityMain.pre.edit();
            editor.putString("Username", "");
            editor.putString("Id","");
            editor.apply();
            txtprofile.setText("وارد شوید / ثبت نام کنید");

        } else if (id == R.id.address) {
            Intent intent = new Intent(G.CurrentActivity,  Address.class);
            startActivity(intent);
        }else if(id == R.id.sabadkharid){
            if(G.userid == -1){
                Intent intent = new Intent(G.CurrentActivity,  LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(G.CurrentActivity,  SabadActivity.class);
                startActivity(intent);
            }

        }else if(id == R.id.yourorders){
            if(G.userid == -1){
                Intent intent = new Intent(G.CurrentActivity,  LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(G.CurrentActivity,  Orders.class);
                startActivity(intent);
            }

        }else if(id == R.id.alaghemandiha){
            if(G.userid == -1){
                Intent intent = new Intent(G.CurrentActivity,  LoginActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(G.CurrentActivity,  alaghemandiha.class);
                startActivity(intent);
            }

        }
*/

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(G.context, permission) != PackageManager.PERMISSION_GRANTED) {

                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("دسترسی ها");
                alertDialog.setMessage("شما درسترسی های زیر  را رد کردید این برنامه بدون این درسترسی ها کار نمیکند" +
                        "\n" + permission);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "تلاش دوباره", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        alertDialog.dismiss();
                        Dialogs.Checkpermissions();

                    }
                });
                alertDialog.show();

                break;
            }
        }

    }

    private static void showimage(final ImageView img, final JSONObject jsonObject) {

        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                try {



                    Picasso.get()
                            .load(G.Baseurl + "Listimages/" + jsonObject.getString("Postimage") + "/" + jsonObject.getString("Postimage") + ".png")
                            .resize(G.IMAGES_HEIGHT, G.IMAGES_WIDTH)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(img, new Callback() {
                                @Override
                                public void onSuccess() {
                                    MaterialImageLoading.animate(img).setDuration(1000).start();
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private  static  void setonclicks(final ImageView img, final JSONObject jsonObject){


        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (jsonObject.has("Subcategori") && !jsonObject.getString("Subcategori").equals("")) {

                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                G.HANDLER.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            Intent intent = new Intent(G.CurrentActivity, android.widget.Adapter.class);//Subdastebandi
                                            intent.putExtra("frommain", jsonObject.getString("Subcategori"));
                                            G.CurrentActivity.startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            }
                        });

                    } else if (jsonObject.has("Name") && !jsonObject.getString("Name").equals("")) {
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Converts.openactivity(jsonObject, ActivityMain.class);


                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
