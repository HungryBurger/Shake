package org.androidtown.shaketest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class HowAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage=4;
    public HowAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context=context;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch(position){
            case 0:
                f=HowPageManager.newInstance(1);
                break;
            case 1:
                f=HowPageManager.newInstance(2);
                break;
            case 2:
                f=HowPageManager.newInstance(3);
                break;
            case 3:
                f=HowPageManager.newInstance(4);
                break;
            default:

                break;
        }
        return f;
    }
    @Override
    public int getCount() {
        return totalPage;
    }

}