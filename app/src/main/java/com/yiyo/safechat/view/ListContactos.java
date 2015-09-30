package com.yiyo.safechat.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yiyo.safechat.codigo.CardViewDataAdapter;
import com.yiyo.safechat.codigo.Conexion;
import com.yiyo.safechat.codigo.ConexionLocal;
import com.yiyo.safechat.codigo.ItemContacto;
import com.yiyo.safechat.utilidades.CheckCuentaDivice;
import com.yiyo.safechat.utilidades.Ipconfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yiyo on 15/09/15.
 */
public class ListContactos extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CardViewDataAdapter adapterx;


    public static ListContactos newInstance() {
        return new ListContactos();
    }

    public ListContactos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contactos, container, false);

        if(!CheckCuentaDivice.IsCuenta(getContext())){
            Intent intent = new Intent(getContext(),Setting.class);
            intent.putExtra("Cuenta",false);
            startActivity(intent);
        }else{
            Init(view);
            adapterx = new CardViewDataAdapter(VerDatos(false));
            StartRefresh();
            swipeRefreshLayout.setRefreshing(false);
        }


        return view;
    }

    private void Init(View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);


        recyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorScheme(new int[]{android.R.color.holo_blue_light, android.R.color.white, android.R.color.holo_blue_light, android.R.color.white});
    }

    private void StartRefresh(){
        adapterx.SetOnItemClickListener(new CardViewDataAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(getContext(), Conversacion.class);

                ArrayList<String> list = new ArrayList<String>();

                list.add(VerDatos(false).get(position).getMail());
                list.add(new InfoDivice(getContext()).getId_mail());
                Collections.sort(list);

                String id_conversacion = list.get(0) + "-->" + list.get(1);  //Servira para formular el id_conversacion

                i.putExtra("Id_Conversacion", id_conversacion);
                i.putExtra("Amigo", VerDatos(false).get(position).getMail());
                i.putExtra("Alias", VerDatos(false).get(position).getNombre());
                i.putExtra("IMG", VerDatos(false).get(position).getImagenContacto());
                i.putExtra("key", VerDatos(false).get(position).getKey());
                startActivity(i);

                //Toast.makeText(MainActivity.this,VerDatos().get(position).getNombre(),Toast.LENGTH_LONG).show();
            }
        });
        adapter = adapterx;
        recyclerView.setAdapter(adapter);
    }


    public ArrayList<ItemContacto> VerDatos(boolean consqlserver){

        ConexionLocal conlocal = new ConexionLocal(getContext());
        conlocal.abrir();
        if(consqlserver){
            Conexion cone = new Conexion(Ipconfig.getInstance("Consultas2.php"));

            List<NameValuePair> Parametros = new ArrayList<NameValuePair>( );
            Parametros.add(new BasicNameValuePair("id_mail",Ipconfig.getIam(getContext())));

            JSONArray respjson = cone.leer(Parametros);

            if(respjson != null){
                for(int i=0;i<respjson.length();i++){
                    JSONObject c = null;
                    try {
                        c = respjson.getJSONObject(i);
                        if(c.getString("row0").equals("0")){
                            Toast.makeText(getContext(), "No se encontro ningun usuario registrado", Toast.LENGTH_SHORT).show();
                        }else{
                            conlocal.Ejecutar("INSERT OR REPLACE INTO contactos VALUES ('" + c.getString("mail") + "','" + c.getString("alias") + "','" + c.getString("key") + "','"+ c.getString("img") +"')");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Toast.makeText(getContext(),"Error de Conexion",Toast.LENGTH_SHORT).show();
            }
        }

        ArrayList<ItemContacto> arraylist = new ArrayList<ItemContacto>( );


        Cursor c = conlocal.Consultar("SELECT * FROM contactos order by alias");


        boolean icono = true;
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            ItemContacto item = new ItemContacto();
            item.setNombre(c.getString(c.getColumnIndex("alias")));
            item.setMail(c.getString(c.getColumnIndex("id_mail")));
            item.setImagenContacto(c.getString(c.getColumnIndex("img")));
            item.setKey(c.getString(c.getColumnIndex("key")));
            arraylist.add(item);
        }

        return arraylist;
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new MyRunnable(ListContactos.this).run();
                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
            }
        }, 5 * 1000);

    }


    private static class MyRunnable implements Runnable {

        private final ListContactos objmain;

        MyRunnable(final ListContactos objmain) {
            this.objmain = objmain;
        }

        public void run() {
            objmain.adapterx = new CardViewDataAdapter(objmain.VerDatos(true));
            objmain.StartRefresh();
            objmain.swipeRefreshLayout.setRefreshing(false);
        }
    }
}
