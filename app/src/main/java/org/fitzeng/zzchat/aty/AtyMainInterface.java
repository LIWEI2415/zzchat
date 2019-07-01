package org.fitzeng.zzchat.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.adapter.BlankFragment;

import java.util.ArrayList;
import java.util.List;


public class AtyMainInterface extends AppCompatActivity implements View .OnClickListener{

    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;

    private Button bt_add;
    private Button bt_chat;
    private Button bt_personal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_main_interface);
        initView();
    }

    private void initView() {
        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
        mFragments = new ArrayList<>(4);
        mFragments.add(BlankFragment.newInstance("今日"));
        mFragments.add(BlankFragment.newInstance("记录"));
        mFragments.add(BlankFragment.newInstance("通讯录"));
        mFragments.add(BlankFragment.newInstance("设置"));
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

        bt_add = (Button) findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);
        bt_chat = (Button) findViewById(R.id.bt_chat);
        bt_chat.setOnClickListener(this);
        bt_personal = (Button) findViewById(R.id.bt_personal);
        bt_personal.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add:
                Intent intent_write = new Intent(AtyMainInterface.this,ChooseDiary.class);
                startActivity(intent_write);
                break;
            case R.id.bt_chat:
                Intent intent_chat = new Intent(AtyMainInterface.this,AtyMain.class);
                startActivity(intent_chat);
                break;
            case R.id.bt_personal:
                Intent intent_personal = new Intent(AtyMainInterface.this,Personal.class);
                startActivity(intent_personal);
                break;
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }


}
