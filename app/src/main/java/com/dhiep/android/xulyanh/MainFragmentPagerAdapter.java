package com.dhiep.android.xulyanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dhiep.android.xulyanh.EditActivity;
import com.dhiep.android.xulyanh.custom.ObjectAtPositionInterface;
import com.dhiep.android.xulyanh.fragments.AdjustFragment;
import com.dhiep.android.xulyanh.fragments.ColorFragment;
import com.dhiep.android.xulyanh.fragments.FilterFragment;
import com.dhiep.android.xulyanh.fragments.RotateFragment;

import java.util.ArrayList;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements ObjectAtPositionInterface {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm, EditActivity activity) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments.add(new RotateFragment(activity));
        fragments.add(new AdjustFragment(activity));
        fragments.add(new ColorFragment(activity));
        fragments.add(new FilterFragment(activity));
        titles.add("Xoay ảnh");
        titles.add("Điều chỉnh");
        titles.add("Màu sắc");
        titles.add("Bộ lọc");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Object getObjectAtPosition(int position) {
        Fragment res = null;
        try {
            res = fragments.get(position);
        } catch (IndexOutOfBoundsException ex) {}
        return res;
    }
}
