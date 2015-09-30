package com.yiyo.safechat.codigo;

import android.graphics.Bitmap;

import com.yiyo.safechat.utilidades.ImageUtil;

/**
 * Created by yiyo on 23/09/15.
 */
public class ItemListConversaciones {

    private String IMG;
    private String CONTACTO;
    private String MENSAJE;
    private String FECHA_HORA;
    private int NUMMSNS;
    private String ID_CONVERSACION;
    private String KEY;
    private String ALIAS;

    public Bitmap getIMG() {
        return ImageUtil.getInstance().getBitmap(IMG);
    }

    public String getIMGString() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getCONTACTO() {
        return CONTACTO;
    }

    public void setCONTACTO(String CONTACTO) {
        this.CONTACTO = CONTACTO;
    }

    public String getMENSAJE() {
        return MENSAJE;
    }

    public void setMENSAJE(String MENSAJE) {
        this.MENSAJE = MENSAJE;
    }

    public String getFECHA_HORA() {
        return FECHA_HORA;
    }

    public void setFECHA_HORA(String FECHA_HORA) {
        this.FECHA_HORA = FECHA_HORA;
    }

    public int getNUMMSNS() {
        return NUMMSNS;
    }

    public void setNUMMSNS(int NUMMSNS) {
        this.NUMMSNS = NUMMSNS;
    }

    public String getID_CONVERSACION() {
        return ID_CONVERSACION;
    }

    public void setID_CONVERSACION(String ID_CONVERSACION) {
        this.ID_CONVERSACION = ID_CONVERSACION;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }
}
