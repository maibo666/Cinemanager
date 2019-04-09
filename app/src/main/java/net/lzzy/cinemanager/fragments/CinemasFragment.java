package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */

/**
 * @author 2  创建Fragment类 **/
public class CinemasFragment extends BaseFragment {
    public static final String ARGS_CINEMA = "cinema";
    private OnCinemaSelectedListener listener;
    private List<Cinema> cinemas;
    private ListView lv;
    private CinemaFactory factory= CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;

    /** 静态方法传参数 **/
    public static CinemasFragment newInstance(Cinema cinema){
        CinemasFragment fragment=new CinemasFragment();
        Bundle args=new Bundle();
        args.putParcelable(ARGS_CINEMA,cinema);
        fragment.setArguments(args);
        return fragment;
    }
    /** 读取静态方法所传的数据 **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Cinema cinema=getArguments().getParcelable(ARGS_CINEMA);
            this.cinema=cinema;
        }
    }

    @Override
    protected void populate() {
        lv = find(R.id.activity_cinema_lv);
        /** 无数据视图 **/
        View empty=find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        adapter = new GenericAdapter<Cinema>(getActivity(), R.layout.cinemas_item,cinemas) {
            @Override
            public void populate(ViewHolder viewHolder, Cinema cinema) {
                viewHolder.setTextView(R.id.cinemas_items_name,cinema.getName())
                        .setTextView(R.id.cinemas_items_location,cinema.getLocation());
            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onCinemaSelected(adapter.getItem(position).getId().toString());
            }
        });

        if (cinema!=null){
            save(cinema);
        }

    }

    public void save(Cinema cinema){
        adapter.add(cinema);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    public void search(String kw) {
        cinemas.clear();
        if (TextUtils.isEmpty(kw)){
            cinemas.addAll(factory.get());
        }else {
            cinemas.addAll(factory.searchCinemas(kw));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnCinemaSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnCinemaSelectedListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface OnCinemaSelectedListener {
        void onCinemaSelected(String cinemaId);
    }
}
