package ru.taxidriver_3;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment1());
		fragments.add(new Fragment2());
		fragments.add(new Fragment3());
		fragments.add(new Fragment4());
		fragments.add(new Fragment5());
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	@Override
    public CharSequence getPageTitle(int position) {
		final String TitleLogin = "Логин";
	    final String TitleQueue = "Очередь";
	    final String TitleOrder = "Заказ";
	    final String TitleStay = "Стоянка";
	    final String TitleMap = "Карта";
	    String ShowTitle ="";
	    switch (position) {
	    case 0 : ShowTitle = TitleLogin; return ShowTitle;
	    case 1 : ShowTitle = TitleQueue; return ShowTitle;
	    case 2 : ShowTitle = TitleOrder; return ShowTitle;
	    case 3 : ShowTitle = TitleStay;  return ShowTitle;
	    case 4 : ShowTitle = TitleMap;  return ShowTitle;
	     default : return "";
	    }
    }
}
