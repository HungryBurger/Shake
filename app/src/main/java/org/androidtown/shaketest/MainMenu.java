package org.androidtown.shaketest;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainMenu extends AppCompatActivity {
        private String[] navItems = { "Contact List", "Customized","Load","Logout"};
        private ListView lvNavList;
        private FrameLayout flContainer;

        private DrawerLayout dlDrawer;
        private ActionBarDrawerToggle dtToggle;
        private boolean isopen = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_menu);

            lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
            flContainer = (FrameLayout) findViewById(R.id.realcontents);

            lvNavList.setAdapter(new ArrayAdapter<String>(MainMenu.this,
                    android.R.layout.simple_list_item_1, navItems));
            lvNavList.setOnItemClickListener(new DrawerItemClickListener());

            dlDrawer = (DrawerLayout)findViewById(R.id.dl_main_drawer);
            View button1 = findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(dlDrawer.isDrawerOpen(Gravity.LEFT)){
                        dlDrawer.closeDrawer(Gravity.LEFT);
                    }else{
                        dlDrawer.openDrawer(Gravity.LEFT);
                    }
                    isopen = !isopen;
                }
            });
        }

        protected void onPostCreate(Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }

        private class DrawerItemClickListener implements
                ListView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:
                        break;

                }
                dlDrawer.closeDrawer(lvNavList);

            }

        }

    }
