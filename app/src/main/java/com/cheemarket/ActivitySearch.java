package com.cheemarket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cheemarket.Adapter.Adapter;
import com.cheemarket.Customview.badgelogo;
import com.cheemarket.Structure.PoductStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ActivitySearch extends AppCompatActivity {


    private static RecyclerView RecyclerViewList;
    private static LinearLayoutManager LayoutManagerList;
    private static Adapter AdapterList;
    private static ArrayList<PoductStructure> mdatasetkalaforonekala;
    private static ArrayList<PoductStructure> mdatasetkalafortwokala;
    private static TextView error;
    static Button btnview;
    static Button btnsort;
    private static String sort = "Nothing";
    private badgelogo badge;
    @Override
    protected void onResume() {
        super.onResume();
        G.CurrentActivity = this;
        Commands.setbadgenumber(badge);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        G.CurrentActivity = this;

        error = (TextView) findViewById(R.id.error);
        ImageView shoplogo = (ImageView) findViewById(R.id.shoplogo);
        ImageView searchlogo = (ImageView) findViewById(R.id.searchlogo);
        badge = (badgelogo) findViewById(R.id.badgelogo);
        Commands.setbadgenumber(badge);
        searchlogo.setVisibility(View.GONE);

        shoplogo.setVisibility(View.GONE);
        searchlogo.setVisibility(View.GONE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconified(false);
        RecyclerViewList = (RecyclerView) findViewById(R.id.List);
        RecyclerViewList.setFocusable(false);
        btnview = (Button) findViewById(R.id.btnview);
        btnsort = (Button) findViewById(R.id.btnsort);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                searchView.setIconified(false);
            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdapterList != null) {

                    AdapterList.changelayout(RecyclerViewList, AdapterList, LayoutManagerList);
                }

            }
        });
        btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder b = new AlertDialog.Builder(G.CurrentActivity);
                b.setTitle("فیلتر");
                String[] types = {"بدون فیلتر", "قیمت از کم به زیاد", "قیمت از زیاد به کم"};
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                sort = "Nothing";
                                namayeshkalaha(searchView.getQuery().toString());
                                break;
                            case 1:
                                sort = "Ascending";
                                namayeshkalaha(searchView.getQuery().toString());
                                break;
                            case 2:
                                sort = "Descending";
                                namayeshkalaha(searchView.getQuery().toString());
                                break;
                        }
                    }
                });

                b.show();

            }
        });

        mdatasetkalaforonekala = new ArrayList<PoductStructure>();
        mdatasetkalafortwokala = new ArrayList<PoductStructure>();
        RecyclerViewList.setHasFixedSize(true);
        LayoutManagerList = new LinearLayoutManager(G.CurrentActivity);
        RecyclerViewList.setLayoutManager(LayoutManagerList);
        AdapterList = new Adapter(mdatasetkalaforonekala, mdatasetkalafortwokala, R.layout.listtwo);
        RecyclerViewList.setAdapter(AdapterList);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                reset();

                namayeshkalaha(query);
                Commands.addview(query + " سرچ شد");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    private static void reset(){
        error.setVisibility(View.GONE);
        AdapterList.resetlayout(RecyclerViewList, AdapterList, LayoutManagerList);
        if (mdatasetkalaforonekala != null) {
            mdatasetkalaforonekala.clear();
        }
        if (mdatasetkalafortwokala != null) {
            mdatasetkalafortwokala.clear();
        }

        AdapterList.notifyDataSetChanged();

    }
    public static void namayeshkalaha(final String query) {

         reset();


        final Callback mycall = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Webservice.handelerro(e, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        G.HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(G.context,"مشکلی در ارتیاط با سرور پیش آمد دوباره سعی کنید", Toast.LENGTH_LONG).show();
                            }
                        });
                        return null;
                    }
                },G.CurrentActivity);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String input = response.body().string();

                if (input.equals("[]")) {
                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            error.setVisibility(View.VISIBLE);
                        }
                    });

                    return;
                }
                try {

                    JSONArray array = new JSONArray(input);
                    PoductStructure kala = new PoductStructure();
                    boolean kalaone = true;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        if (i == 0) {
                            if (mdatasetkalafortwokala.size() > 0) {

                                if (mdatasetkalafortwokala.get(mdatasetkalafortwokala.size() - 1).Id2 == null || mdatasetkalafortwokala.get(mdatasetkalafortwokala.size() - 1).Id2.equals("")) {
                                    Commands.convertinputdata(object, mdatasetkalafortwokala.get(mdatasetkalafortwokala.size() - 1), false);
                                }

                            }
                        }


                        PoductStructure temp = new PoductStructure();
                        Commands.convertinputdata(object, temp, true);
                        mdatasetkalaforonekala.add(temp);


                        if (kalaone) {
                            kala = new PoductStructure();
                            Commands.convertinputdata(object, kala, kalaone);
                            if (i == array.length() - 1) {
                                mdatasetkalafortwokala.add(kala);
                            }
                        } else {
                            Commands.convertinputdata(object, kala, kalaone);
                            mdatasetkalafortwokala.add(kala);
                        }
                        kalaone = !kalaone;
                    }


                    G.HANDLER.post(new Runnable() {
                        @Override
                        public void run() {

                            AdapterList.notifyItemInserted(AdapterList.getItemCount() + 1);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };


        ArrayList<Webservice.requestparameter> array1 = new ArrayList<>();
        Webservice.requestparameter param1 = new Webservice.requestparameter();
        param1.key = "text";
        param1.value = query;

        Webservice.requestparameter param2 = new Webservice.requestparameter();
        param2.key = "sort";
        param2.value = sort;

        array1.add(param1);
        array1.add(param2);
        Webservice.request("search", mycall, array1);


    }
}
