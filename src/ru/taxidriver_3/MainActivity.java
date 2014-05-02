package ru.taxidriver_3;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		MyPagerAdapter pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager)findViewById(R.id.myViewPager);
		pager.setAdapter(pageAdapter);
		pager.setOffscreenPageLimit(3);
		
		
	}
	
	

}