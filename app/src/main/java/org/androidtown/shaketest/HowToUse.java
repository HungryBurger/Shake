package org.androidtown.shaketest;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Button;


public class HowToUse extends FragmentActivity {
    private ViewPager _mViewPager;
    private HowAdapter _adapter;
    private Button btn1, btn2, btn3, btn4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        setUpView();
        setTab();
    }

    private void setUpView() {
        _mViewPager = (ViewPager) findViewById(R.id.viewPager);
        _adapter = new HowAdapter(getApplicationContext(), getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                btnAction(position);
            }

        });

    }

    private void btnAction(int action) {
        switch (action) {
            case 0:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));

                break;
            case 1:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 2:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));

                break;
            case 3:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                break;
        }
    }

    private void initButton() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
    }
}