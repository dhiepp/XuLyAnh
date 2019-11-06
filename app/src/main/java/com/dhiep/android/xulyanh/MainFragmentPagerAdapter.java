package com.dhiep.android.xulyanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dhiep.android.xulyanh.custom.ObjectAtPositionInterface;
import com.dhiep.android.xulyanh.fragments.AdjustFragment;
import com.dhiep.android.xulyanh.fragments.FilterFragment;
import com.dhiep.android.xulyanh.fragments.RotateFragment;

import java.util.ArrayList;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements ObjectAtPositionInterface {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    public void resetAdjustments() {
        ((AdjustFragment)fragments.get(1)).reset();
        fragments.get(0).onResume();
        fragments.get(2).onResume();
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
