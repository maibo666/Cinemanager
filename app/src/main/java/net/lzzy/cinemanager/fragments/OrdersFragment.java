package net.lzzy.cinemanager.fragments;



import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;


public class OrdersFragment extends BaseFragment {
    private List<Order> orders;
    private ListView lv;
    private GenericAdapter<Order> adapter;
    private OrderFactory factory = OrderFactory.getInstance();
    private Order order;

    public OrdersFragment(){

    }

    public OrdersFragment(Order order){
        this.order = order;
    }

    @Override
    protected void populate() {
        lv = find(R.id.activity_cinema_lv);
        View empty = find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        order = factory.get();
        adapter = new GenericAdapter<Order>(getContext(),R.layout.cinemas_item,orders) {
            @Override
            public void populate(ViewHolder holder, Order order) {
                String location = CinemaFactory.getInstance()
                        .getById(order.getCinemaId().toString()).toString();
                holder.setTextView(R.id.cinemas_items_name,order.getMovie())
                        .setTextView(R.id.cinemas_items_location,location);
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
        lv.setAdapter(adapter);
        if(order!=null){
            save(order);
        }
    }

    private void save(Order order) {
        adapter.add(order);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    @Override
    public void search(String kw) {
        order.clear();
        if(TextUtils.isEmpty(kw)){
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();
    }
}
