package com.coposto.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coposto.models.NavItem;
import com.coposto.Coposto.MainActivity;
import com.coposto.R;

import java.util.List;

public class MyLogin extends Fragment {

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;

    List<NavItem> listNavItems;
    List<Fragment> listFragments;

    private FragmentActivity myContext;

    View v;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        drawerLayout = ((MainActivity)this.getActivity()).getDrawerLayout();
        drawerPane = ((MainActivity)this.getActivity()).getRelativeLayout();
        lvNav = ((MainActivity)this.getActivity()).getListView();

        listNavItems = ((MainActivity)this.getActivity()).getListNavItem();
        listFragments = ((MainActivity)this.getActivity()).getListFragment();

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final int position = 5;
        v = inflater.inflate(R.layout.fragment_login, container, false);
        TextView textView = (TextView) v.findViewById(R.id.signup_button);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // replace the fragment with the selection correspondingly:
                FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_content, listFragments.get(position))
                        .commit();

                ((MainActivity) getActivity()).setTitle("Sign Up");
                //setTitle(listNavItems.get(position).getTitle());
                lvNav.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerPane);

            }
        });


        return v;
    }
}