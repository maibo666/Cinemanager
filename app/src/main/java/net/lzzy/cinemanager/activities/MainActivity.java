package net.lzzy.cinemanager.activities;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OnFragmentInteractionListener;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , OnFragmentInteractionListener, AddCinemasFragment.OnCinemaCreatedListener ,AddOrdersFragment.OnOrderCreatedListener{
    /** 1 创建获取FragmentManager对象 **/
    private FragmentManager manager=getSupportFragmentManager();
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private SparseArray<String> titleArray=new SparseArray<>();
    private SparseArray<Fragment> fragmentArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 去掉标题栏 **/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();

        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment=manager.findFragmentById(R.id.fragment_container);
                if (fragment!=null){
                    if (fragment instanceof  BaseFragment){
                        ((BaseFragment) fragment).search(kw);
                    }
                }
                return true;
            }
        });

        /** 第一次进入，加载的首个Fragment **/
        manager.beginTransaction().add(R.id.fragment_container,new OrdersFragment()).commit();
    }

    /** 自定义标题栏 **/
    private void setTitleMenu() {
        titleArray.put(R.id.bar_title_tv_add_cinema,"添加影院");
        titleArray.put(R.id.bar_title_tv_view_cinema,"影院列表");
        titleArray.put(R.id.bar_title_tv_add_order,"添加订单");
        titleArray.put(R.id.bar_title_tv_view_order,"订单列表");
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v -> {
            int visible=layoutMenu.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> System.exit(0));
    }

    /** 对标题栏的点击监听 **/
    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        FragmentTransaction transaction=manager.beginTransaction();
        Fragment fragment=fragmentArray.get(v.getId());
        if (fragment==null){
            fragment=createFragment(v.getId());
            fragmentArray.put(v.getId(),fragment);
            transaction.add(R.id.fragment_container,fragment);
        }
        for (Fragment f:manager.getFragments()){
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }


    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_title_tv_add_cinema:
                return new AddCinemasFragment();
            case R.id.bar_title_tv_view_cinema:
                /* *//**  3 启动manager事务替换/添加Fragment 4 提交事务 commit**//*
                manager.beginTransaction()
                        .replace(R.id.fragment_container,new CinemasFragment())
                        .commit();*/
                return new CinemasFragment();
            case R.id.bar_title_tv_add_order:
                return new AddOrdersFragment();
            case R.id.bar_title_tv_view_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }
    /** 2.实现接口 **/
    @Override
    public void hideSearch() {
        search.setVisibility(View.GONE);
    }

    private void hideAndShow(int hideId,int showId){
        Fragment hideFragment=fragmentArray.get(hideId);
        if (hideFragment==null){
            return;
        }
        Fragment showFragment=fragmentArray.get(showId);
        FragmentTransaction transaction=manager.beginTransaction();
        if (showFragment==null){
            showFragment = new CinemasFragment();
            fragmentArray.put(hideId,showFragment);
            transaction.add(R.id.fragment_container,showFragment);
        }
        transaction.hide(hideFragment).show(showFragment).commit();
        tvTitle.setText(titleArray.get(showId));

    }

    @Override
    public void cancelAddCinema() {
        hideAndShow(R.id.bar_title_tv_add_cinema,R.id.bar_title_tv_view_cinema);
        search.setVisibility(View.VISIBLE);
    }


    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemaFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            //创建CinemasFragment同时要传Cinema对象进来
            cinemasFragment=new CinemasFragment(cinema);
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }else {
            ((CinemasFragment)cinemasFragment).save(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void cancelAddOrder() {
        hideAndShow(R.id.bar_title_tv_add_order,R.id.bar_title_tv_view_order);
        search.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveOrder(Order order) {
        Fragment addOrderFragment=fragmentArray.get(R.id.bar_title_tv_add_order);
        if (addOrderFragment==null){
            return;
        }
        Fragment ordersFragment=fragmentArray.get(R.id.bar_title_tv_view_order);
        FragmentTransaction transaction=manager.beginTransaction();
        if (ordersFragment==null){
            //创建CinemasFragment同时要传Order对象进来
            ordersFragment=new OrdersFragment(order);
            fragmentArray.put(R.id.bar_title_tv_view_order,ordersFragment);
            transaction.add(R.id.fragment_container,ordersFragment);
        }else {
            ((OrdersFragment)ordersFragment).save(order);
        }
        transaction.hide(addOrderFragment).show(ordersFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_order));
        search.setVisibility(View.VISIBLE);
    }

}
