package net.lzzy.cinemanager.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/2
 * Description:
 */
public class CinemaOrdersFragments extends BaseFragment {
    private static final String ARG_CINEMA_ID = "argCinemaId";
    private String cinemaId;

    /** 静态方法传参数 **/
    public static CinemaOrdersFragments newInstance(String cinemaId){
        CinemaOrdersFragments fragment=new CinemaOrdersFragments();
        Bundle args=new Bundle();
        args.putString(ARG_CINEMA_ID,cinemaId);
        fragment.setArguments(args);
        return fragment;
    }


    /** 读取静态方法所传的数据 **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            cinemaId=getArguments().getString(ARG_CINEMA_ID);
        }
    }

    @Override
    protected void populate() {
        ListView lv = find(R.id.fragment_cinema_orders_lv);
        View empty=find(R.id.fragment_cinema_orders_none);
        lv.setEmptyView(empty);

        List<Order> orders= OrderFactory.getInstance().getOrdersByCinema(cinemaId);
        GenericAdapter<Order> adapter=new GenericAdapter<Order>(getContext(), R.layout.cinemas_item,orders) {
            @Override
            public void populate(ViewHolder viewHolder, Order order) {
                viewHolder.setTextView(R.id.cinemas_items_name,order.getMovie())
                        .setTextView(R.id.cinemas_items_location,order.getMovieTime());
            }

            @Override
            public boolean persistInsert(Order order) {
                return false;
            }

            @Override
            public boolean persistDelete(Order order) {
                return false;
            }
        };
        lv.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_cinema_orders;
    }

    @Override
    public void search(String kw) {

    }

}
