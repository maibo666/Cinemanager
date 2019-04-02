package net.lzzy.cinemanager.fragments;


import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州市";
    private String area="鱼峰区";
    private TextView tvArea;
    private EditText edtName;
    private CinemaFactory factory;
    /** 3.声明接口对象**/
    private OnFragmentInteractionListener listener;
    private OnCinemaCreatedListener cinemaListener;

    @Override
    protected void populate() {
        /** 5.调用接口方法 **/
        listener.hideSearch();
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
        showDialog();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_cinemas;
    }

    @Override
    public void search(String kw) {

    }

    /** 6.若该片段被隐藏则重新调用接口方法  **/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }



    private void showDialog() {
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v -> {
            cinemaListener.cancelAddCinema();
        });

        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v -> {
            /** 地区选择器 **/
            showLocation();
        });

        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            String location=tvArea.getText().toString();
            if (name.isEmpty()){
                Toast.makeText(getActivity(),"影院名称不能为空",Toast.LENGTH_SHORT).show();
            }else {
                Cinema cinema=new Cinema();
                cinema.setName(name);
                cinema.setArea(area);
                cinema.setCity(city);
                cinema.setProvince(province);
                cinema.setLocation(location);
                edtName.setText("");
                cinemaListener.saveCinema(cinema);
               /* Cinema cinema=new Cinema(name,location,province,city,area);
                factory = CinemaFactory.getInstance();
                factory.addCinema(cinema);*/
            }

        });
    }

    private void showLocation() {
        JDCityPicker cityPicker = new JDCityPicker();
        cityPicker.init(getActivity());
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                AddCinemasFragment.this.province=province.getName();
                AddCinemasFragment.this.city=city.getName();
                AddCinemasFragment.this.area=district.getName();
                String loc=province.getName()+city.getName()+district.getName();
                tvArea.setText(loc);
            }

            @Override
            public void onCancel() {
            }
        });
        cityPicker.showCityPicker();
    }

    /** 4.接口对象赋值 **/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnFragmentInteractionListener) context;
            cinemaListener = (OnCinemaCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现" +
                    "OnFragmentInteractionListener,OnCinemaCreatedListener接口");
        }
    }
    /** 4.销毁 **/
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        cinemaListener =null;
    }

    /** 取消保存数据
     * 1.声明接口 **/
    public interface OnCinemaCreatedListener {
        /**取消**/
        void cancelAddCinema();
        /**保存数据**/
        void saveCinema(Cinema cinema);

    }
}
