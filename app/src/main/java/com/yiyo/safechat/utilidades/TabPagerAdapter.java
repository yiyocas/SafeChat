package com.yiyo.safechat.utilidades;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.yiyo.safechat.view.ListContactos;
import com.yiyo.safechat.view.ListConversaciones;

/**
 * Created by yiyo on 16/09/15.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter{

    private int TOTAL_TABS = 2;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                return new ListContactos();

            case 1:
                return new ListConversaciones();
        }

        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TOTAL_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "CONTACTOS";
        }else{
            return "CONVERSACIONES";
        }
    }
}
