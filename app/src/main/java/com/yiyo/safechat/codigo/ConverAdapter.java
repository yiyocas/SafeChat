package com.yiyo.safechat.codigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyo.safechat.view.R;

import java.util.ArrayList;

/**
 * Created by yiyo on 23/09/15.
 */
public class ConverAdapter extends BaseAdapter {
    private ArrayList<ItemListConversaciones> list;
    private Context context;

    public ConverAdapter(Context context, ArrayList<ItemListConversaciones> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemListConversaciones item = (ItemListConversaciones) getItem(position);

        if(view==null){
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.row_conversacion,null);
        }

        ImageView img = (ImageView) view.findViewById(R.id.imagencontacto_);
        img.setImageBitmap(item.getIMG());

        TextView Contacto = (TextView) view.findViewById(R.id.nomcontacto);
        Contacto.setText(item.getCONTACTO());

        TextView Mensaje = (TextView) view.findViewById(R.id.lastmsn);
        Mensaje.setText(item.getMENSAJE());

        TextView FH = (TextView) view.findViewById(R.id.fechahora);
        FH.setText(item.getFECHA_HORA());

        TextView num = (TextView) view.findViewById(R.id.nummsn);
        if(item.getNUMMSNS()!=0){
            num.setText(item.getNUMMSNS()+"");
            num.setBackgroundResource(R.drawable.circlemsn);
        }else{
            num.setText("0");
            num.setBackgroundResource(R.drawable.ciclemsnnull);
        }


        return view;
    }
}
