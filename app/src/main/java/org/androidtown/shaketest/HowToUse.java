package org.androidtown.shaketest;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class HowToUse extends FragmentActivity {
    private ViewPager _mViewPager;
    private HowAdapter _adapter;
    Button btn1, btn2, btn3, btn4, btn5,btn6 ,btn7;
    Button skip_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        setUpView();
        setTab();
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 1:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 2:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 3:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 4:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 5:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                break;
            case 6:
                btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
                btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                break;
        }
    }

    private void initButton() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        skip_btn = (Button) findViewById(R.id.skip_btn);

        btn1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        btn2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn5.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn6.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
        btn7.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.ourConcept2));
    }
}