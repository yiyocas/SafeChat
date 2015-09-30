package com.yiyo.safechat.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yiyo.safechat.codigo.ConexionLocal;
import com.yiyo.safechat.codigo.ConverAdapter;
import com.yiyo.safechat.codigo.ItemConversacion;
import com.yiyo.safechat.codigo.ItemListConversaciones;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yiyo on 16/09/15.
 */
public class ListConversaciones extends Fragment implements AdapterView.OnItemClickListener {

    View view;

    public static ListConversaciones newInstance() {
        return new ListConversaciones();
    }

    public ListConversaciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.listconversaciones, container, false);

        getConversaciones(view);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onNotice, new IntentFilter("Msg"));

        return view;
    }

    private void getConversaciones (View view){
        ConexionLocal conlocal = new ConexionLocal(getContext());
        conlocal.abrir();
        Cursor c = conlocal.Consultar("select key,alias,img,amigo,mensajes.id_conversacion, mensaje, max(fecha) as fecha, max(hora) as hora " +
                "from mensajes,r_usr_conver, contactos " +
                "where r_usr_conver.id_conversacion = mensajes.id_conversacion and contactos.id_mail=r_usr_conver.amigo " +
                "group by mensajes.id_conversacion order by fecha desc, hora desc");

        ArrayList<ItemListConversaciones> list = new ArrayList<ItemListConversaciones>();

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            ItemListConversaciones item = new ItemListConversaciones();
            item.setCONTACTO(c.getString(c.getColumnIndex("amigo")));
            item.setMENSAJE(c.getString(c.getColumnIndex("mensaje")));
            item.setFECHA_HORA(FechaUHora(c.getString(c.getColumnIndex("fecha")),
                    c.getString(c.getColumnIndex("hora"))));
            item.setIMG(c.getString(c.getColumnIndex("img")));
            item.setID_CONVERSACION(c.getString(c.getColumnIndex("id_conversacion")));
            item.setKEY(c.getString(c.getColumnIndex("key")));
            item.setALIAS(c.getString(c.getColumnIndex("alias")));

            ConexionLocal conlocal2 = new ConexionLocal(getContext());
            conlocal2.abrir();

            Cursor c2 = conlocal.Consultar("select count(id_conversacion)as msn from msn_no_leidos");
            for(c2.moveToFirst();!c2.isAfterLast();c2.moveToNext()){
                Log.i("MensajesNoLeidos",c2.getString(c2.getColumnIndex("msn")));
            }

            list.add(item);
        }
        conlocal.cerrar();

        ListView listView = (ListView) view.findViewById(R.id.listas);

        listView.setAdapter(new ConverAdapter(getContext(), list));
        listView.setOnItemClickListener(this);
    }

    private String FechaUHora(String fecha, String hora){

        Calendar c = Calendar.getInstance();


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String hoy = dateFormat.format(date);

        if(hoy.equals(fecha)){
         return hora;
        }else{
         return fecha;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ItemListConversaciones item = (ItemListConversaciones) parent.getItemAtPosition(position);

        //Toast.makeText(getActivity(),item.getID_CONVERSACION(),Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getContext(), Conversacion.class);

        i.putExtra("Id_Conversacion", item.getID_CONVERSACION());
        i.putExtra("Amigo", item.getCONTACTO());
        i.putExtra("Alias", item.getALIAS());
        i.putExtra("IMG", item.getIMGString());
        i.putExtra("key", item.getKEY());
        startActivity(i);

    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent i) {
            Log.i("OnReceive", "Dentro.......");
            ConexionLocal conn = new ConexionLocal(context);

            conn.abrir();

            SQLiteDatabase db = conn.getSQLiteDatabase();

            //conn.Ejecutar("INSERT INTO mensajes (id_conversacion,quien,mensaje,fecha,hora) VALUES ('" + i.getStringExtra("data1") + "','" + i.getStringExtra("data2") + "','"+ i.getStringExtra("data3") +"','" + i.getStringExtra("data4") + "','" + i.getStringExtra("data5") + "')");

            SQLiteStatement stm = conn.getSQLiteDatabase().compileStatement("INSERT INTO mensajes (id_conversacion,quien,mensaje,fecha,hora) VALUES (?,?,?,?,?)");
            stm.bindString(1,i.getStringExtra("data1"));
            stm.bindString(2,i.getStringExtra("data2"));
            stm.bindString(3,i.getStringExtra("data3"));
            stm.bindString(4, i.getStringExtra("data4"));
            stm.bindString(5, i.getStringExtra("data5"));
            stm.execute();

            stm = conn.getSQLiteDatabase().compileStatement("INSERT INTO msn_no_leidos (id_conversacion) VALUES (?)");
            stm.bindString(1, i.getStringExtra("data1"));
            stm.execute();

            conn.cerrar();

            //listMensajes = null;

            //listMensajes = new ArrayList<ItemConversacion>();

            //setMensajesActivity();

            getConversaciones(view);

        }
    };
}

