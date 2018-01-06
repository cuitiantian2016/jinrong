package com.honglu.future.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.honglu.future.ui.circle.circlemain.CircleMainFragment;
import com.honglu.future.ui.home.fragment.HomeFragment;
import com.honglu.future.ui.live.LiveFragment;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.trade.fragment.TradeFragment;
import com.honglu.future.ui.usercenter.fragment.UserCenterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment选择管理
 * xiejingwen
 */
public class FragmentFactory {

    private static List<Fragment>  list;
    public static Fragment        lastFragment;
    private static FragmentManager manager;

    public enum FragmentStatus {
        None,
        Home,
        Trade,
        Circle,
        Account,
        Live
    }


    public static void clear() {
        if (list != null)
            list.clear();
        if (manager != null) {
            int count = manager.getBackStackEntryCount();
            while (count >= 0) {
                manager.popBackStackImmediate();
                count--;
            }
        }
        manager = null;
    }
    public static void changeFragment(FragmentManager manager, FragmentStatus status, int id) {
        FragmentFactory.manager = manager;
        FragmentTransaction transaction = manager.beginTransaction();
        if (list == null)
            list = new ArrayList<>();
        Fragment selectFragment = null;
        switch (status) {
            case None:
                return;
            case Home:
                selectFragment = HomeFragment.getInstance();
                break;
            case Trade:
                selectFragment = TradeFragment.getInstance();
                break;
            case Circle:
                selectFragment = CircleMainFragment.getInstance();
                break;
            case Account:
                selectFragment = UserCenterFragment.getInstance();
                break;
            case Live:
                selectFragment = LiveFragment.getInstance();
            default:
                break;
        }
        //change
        if (list.contains(selectFragment)) {
            transaction.hide(lastFragment).show(selectFragment).commitAllowingStateLoss();
        } else {
            if (list.size() == 0)
                transaction.add(id, selectFragment).commitAllowingStateLoss();
            else
                transaction.hide(lastFragment).add(id, selectFragment).commitAllowingStateLoss();
            list.add(selectFragment);
        }
        lastFragment = selectFragment;
    }

}
