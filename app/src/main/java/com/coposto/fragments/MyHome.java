package com.coposto.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.coposto.Coposto.MainActivity;
import com.coposto.R;
import com.coposto.adapters.MyFragmentPagerAdapter;
import com.coposto.inner.fragments.BringFragment;
import com.coposto.inner.fragments.SendFragment;

import java.util.List;
import java.util.Vector;

public class MyHome extends Fragment implements OnTabChangeListener, OnPageChangeListener {

	private int previous=1;
	TabHost tabHost;
	Thread Draw_Panel;
	private ViewPager viewPager;
	private MyFragmentPagerAdapter myViewPagerAdapter;
	TextView x;
	boolean work=true;
	int i = 0;
	View v;
	MainActivity mainActivity;
	ListView lvNiv;

	public MyHome(MainActivity activity,ListView listView)
	{
		mainActivity=activity;
		lvNiv = listView;
	}

	Boolean pressed=false;

	public void tabHostPressed()
	{
		mainActivity.openDrawePanel();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.tabs_viewpager_layout, container, false);
		i++;

		// init tabhost
		this.initializeTabHost(savedInstanceState);

		// init ViewPager
		this.initializeViewPager();


		//tabHost.getTabWidget().getChildAt(0).setBackgroundColor(getResources().getColor(R.color.transperentColorSelected));

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		work=true;
		((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(0).getLayoutParams()).weight = 0;
		((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(1).getLayoutParams()).weight = 0;
		((LinearLayout.LayoutParams)tabHost.getTabWidget().getChildAt(2).getLayoutParams()).weight = 0;

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().width = ((2*width/5));
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().width = ((2*width/5));
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().width = (width/5);
		tabHost.getTabWidget().getChildAt(2).setBackground(getResources().getDrawable(R.drawable.dot_menu_icon));
		//tabHost.getTabWidget().getChildAt(2).setClickable(false);
		//tabHost.getTabWidget().getChildAt(2).setFocusable(false);


		x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
		x.setTextColor(getResources().getColor(R.color.selected_text));


		Draw_Panel = new Thread(new Runnable() {
			public void run() {
				while (work) {
					tabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							tabHostPressed();
						}
					});
				}
			}
		});

		Draw_Panel.start();


		lvNiv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				work=false;
				mainActivity.listItemisClicked(parent,view,position,id);
			}
		});
		//x = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
		//x.setTextSize(17);

		//for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		//	tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.transperentColorNormal)); //unselected

		return v;
	}

	// fake content for tabhost
	class FakeContent implements TabContentFactory {
		private final Context mContext;

		public FakeContent(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumHeight(0);
			v.setMinimumWidth(0);
			return v;
		}
	}

	private void initializeViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();

		fragments.add(new SendFragment());
		fragments.add(new BringFragment());

		this.myViewPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
		this.viewPager = (ViewPager) v.findViewById(R.id.viewPager);
		this.viewPager.setAdapter(this.myViewPagerAdapter);
		this.viewPager.setOnPageChangeListener(this);

	}

	private void initializeTabHost(Bundle args) {

		tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec tabSpec;

		tabSpec = tabHost.newTabSpec("Отправить");
		tabSpec.setIndicator("Отправить");
		tabSpec.setContent(new FakeContent(getActivity()));
		tabHost.addTab(tabSpec);


		tabSpec = tabHost.newTabSpec("Доставить");
		tabSpec.setIndicator("Доставить");
		tabSpec.setContent(new FakeContent(getActivity()));
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec("");
		tabSpec.setIndicator("");
		tabSpec.setContent(new FakeContent(getActivity()));
		tabHost.addTab(tabSpec);


		tabHost.setOnTabChangedListener(this);
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.tabHost.getCurrentTab();

		if (pos != 2) {

			this.viewPager.setCurrentItem(pos);
			x = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
			x.setTextColor(getResources().getColor(R.color.selected_text));

			x = (TextView) tabHost.getTabWidget().getChildAt((tabHost.getCurrentTab() + 1) % 2).findViewById(android.R.id.title);
			x.setTextColor(getResources().getColor(R.color.normal_text));

			//tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.transperentColorSelected));
			//tabHost.getTabWidget().getChildAt((tabHost.getCurrentTab() + 1) % 2).setBackgroundColor(getResources().getColor(R.color.transperentColorNormal));
			previous=pos;
		} else{

			pos = previous;
			this.viewPager.setCurrentItem(previous);
			tabHost.getTabWidget().getChildAt(previous).setPressed(true);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		if (position != 2)
			this.tabHost.setCurrentTab(position);

	}
}
