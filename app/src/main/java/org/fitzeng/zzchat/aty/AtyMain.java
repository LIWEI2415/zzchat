package org.fitzeng.zzchat.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.adapter.AdapterMainViewPager;
import org.fitzeng.zzchat.view.LayoutChats;
import org.fitzeng.zzchat.view.LayoutContacts;
import org.fitzeng.zzchat.view.LayoutMoments;

import java.util.ArrayList;
import java.util.List;

public class AtyMain extends AppCompatActivity {

    private DrawerLayout drawable;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<TabLayout.Tab> tabList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_main);

        initViews();
    }

    private void initViews() {
        drawable = (DrawerLayout) findViewById(R.id.dl_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        tabLayout = (TabLayout) findViewById(R.id.tl_main);

        tabList = new ArrayList<>();

        AdapterMainViewPager adapter = new AdapterMainViewPager(getSupportFragmentManager());

        adapter.addFragment(new LayoutChats());
        adapter.addFragment(new LayoutContacts());
        adapter.addFragment(new LayoutMoments());

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabList.add(tabLayout.getTabAt(0));
        tabList.add(tabLayout.getTabAt(1));
        tabList.add(tabLayout.getTabAt(2));
        tabList.get(0).setIcon(R.drawable.icon).setText("Chats");
        tabList.get(1).setIcon(R.drawable.icon).setText("Contacts");
        tabList.get(2).setIcon(R.drawable.icon).setText("Moments");
    }
}

