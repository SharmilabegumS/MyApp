package com.example.navigationcheck.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import com.example.navigationcheck.MonthFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;

public class MyFragmentStatePagerAdapter extends PagerAdapter {
    private static final String TAG = "FragmentSPA";
    private static final boolean DEBUG = true;
    Calendar calendar = Calendar.getInstance();
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    private long[] mItemIds = new long[]{};
    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    public LinkedList<String> monthList = new LinkedList<String>();
    private Fragment mCurrentPrimaryItem = null;

    public MyFragmentStatePagerAdapter(FragmentManager fm, Context context, LinkedList<String> monthList) {

        mFragmentManager = fm;
        this.monthList = monthList;
        mItemIds = new long[getCount()];
        for (int i = 0; i < mItemIds.length; i++) {
            mItemIds[i] = getItemId(i);
        }
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public MonthFragment getItem(int position) {
        String monthYear = monthList.get(position);
        int month = Integer.parseInt(monthYear.substring(0, 2));
        int year = Integer.parseInt(monthYear.substring(2, 6));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        System.out.println("The fragment: " + position + " " + calendar.getTime());

        return MonthFragment.newInstance(calendar.getTime());
    }

    /**
     * Return a unique identifier for the item at the given position.
     */
    public int getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        long[] newItemIds = new long[getCount()];
        for (int i = 0; i < newItemIds.length; i++) {
            newItemIds[i] = getItemId(i);
        }

        if (!Arrays.equals(mItemIds, newItemIds)) {
            ArrayList<Fragment.SavedState> newSavedState = new ArrayList<Fragment.SavedState>();
            ArrayList<Fragment> newFragments = new ArrayList<Fragment>();

            for (int oldPosition = 0; oldPosition < mItemIds.length; oldPosition++) {
                int newPosition = POSITION_NONE;
                for (int i = 0; i < newItemIds.length; i++) {
                    if (mItemIds[oldPosition] == newItemIds[i]) {
                        newPosition = i;
                        break;
                    }
                }
                if (newPosition >= 0) {
                    if (oldPosition < mSavedState.size()) {
                        Fragment.SavedState savedState = mSavedState.get(oldPosition);
                        if (savedState != null) {
                            while (newSavedState.size() <= newPosition) {
                                newSavedState.add(null);
                            }
                            newSavedState.set(newPosition, savedState);
                        }
                    }
                    if (oldPosition < mFragments.size()) {
                        Fragment fragment = mFragments.get(oldPosition);
                        if (fragment != null) {
                            while (newFragments.size() <= newPosition) {
                                newFragments.add(null);
                            }
                            newFragments.set(newPosition, fragment);
                        }
                    }
                }
            }

            mItemIds = newItemIds;
            mSavedState = newSavedState;
            mFragments = newFragments;
        }

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // If we already have this item instantiated, there is nothing
        // to do.  This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        if (mFragments.size() > position) {
            Fragment f = mFragments.get(position);
            if (f != null) {
                return f;
            }
        }

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
        if (DEBUG) Log.v(TAG, "Adding item #" + position + ": f=" + fragment);
        if (mSavedState.size() > position) {
            Fragment.SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        while (mFragments.size() <= position) {
            mFragments.add(null);
        }
        fragment.setMenuVisibility(false);
        mFragments.set(position, fragment);
        mCurTransaction.add(container.getId(), fragment);

        return fragment;
    }

    public void destroyItemState(int position) {
        mFragments.remove(position);
        mSavedState.remove(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        //position = getItemPosition(object);
        if (DEBUG) Log.v(TAG, "Removing item #" + position + ": f=" + object
                + " v=" + ((Fragment) object).getView());
        if (position >= 0) {
            while (mSavedState.size() <= position) {
                mSavedState.add(null);
            }
            mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment));
            if (position < mFragments.size()) {
                mFragments.set(position, null);
            }
        }

        mCurTransaction.remove(fragment);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = new Bundle();
        if (mItemIds.length > 0) {
            state.putLongArray("itemids", mItemIds);
        }
        if (mSavedState.size() > 0) {
            Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
        }
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment f = mFragments.get(i);
            if (f != null) {
                String key = "f" + i;
                mFragmentManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            mItemIds = bundle.getLongArray("itemids");
            if (mItemIds == null) {
                mItemIds = new long[]{};
            }
            Parcelable[] fss = bundle.getParcelableArray("states");
            mSavedState.clear();
            mFragments.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSavedState.add((Fragment.SavedState) fss[i]);
                }
            }
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    Fragment f = mFragmentManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        }
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
