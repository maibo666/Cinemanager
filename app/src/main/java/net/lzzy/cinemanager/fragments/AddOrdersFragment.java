package net.lzzy.cinemanager.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author lzzy_gxy
 * @date 2019/3/27
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    /** 3.声明接口对象**/
    private OnFragmentInteractionListener listener;
    private OnOrderCreatedListener orderlistener;
    private EditText edtMovieName;
    private TextView tvTime;
    private Spinner spCinemas;
    private EditText edtMoviePrice;
    private CustomDatePicker datePicker;
    private ArrayAdapter<Cinema> adapter;
    private List<Cinema> cinemas;
    private OrderFactory factory= OrderFactory.getInstance();
    private List<Order> orders;
    private ImageView imgQRCode;

    @Override
    protected void populate() {
        /** 5.调用接口方法 **/
        listener.hideSearch();
        edtMovieName = find(R.id.add_order_movie_edt_name);
        tvTime = find(R.id.add_order_tv_time);
        spCinemas = find(R.id.add_order_sp_cinemas);
        edtMoviePrice = find(R.id.add_order_movie_edt_price);
        imgQRCode = find(R.id.add_order_movie_imgQRCode);
        /** 获取Cinema地址 **/
        orders = factory.get();
        cinemas = CinemaFactory.getInstance().get();
        spCinemas.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, cinemas));
        /****/
        initDatePicker();
        find(R.id.add_order_layout_time).setOnClickListener(v -> datePicker.show(tvTime.getText().toString()));
        find(R.id.add_order_movie_btn_QR_code).setOnClickListener(v -> {
            String name=edtMovieName.getText().toString();
            String price=edtMoviePrice.getText().toString();
            String location=spCinemas.getSelectedItem().toString();
            String time=tvTime.getText().toString();
            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(price)){
                Toast.makeText(getActivity(),"信息需要完整",Toast.LENGTH_SHORT).show();
                return;
            }
            String content="["+name+"]"+time+"\n"+location+"  票价为："+price+"元";
            imgQRCode.setImageBitmap(AppUtils.createQRCodeBitmap(content,200,200));
        });
        find(R.id.add_order_movie_btn_save).setOnClickListener(v -> {
            String name=edtMovieName.getText().toString();
            String moviePrice=edtMoviePrice.getText().toString();
            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(moviePrice)){
                Toast.makeText(getActivity(),"信息需要完整",Toast.LENGTH_SHORT).show();
                return;
            }
            float price;
            try{
                price=Float.parseFloat(moviePrice);
            }catch (NumberFormatException e){
                Toast.makeText(getActivity(),"数字格式错误",Toast.LENGTH_SHORT).show();
                return;
            }
            Order order=new Order();
            Cinema cinema=cinemas.get(spCinemas.getSelectedItemPosition());
            order.setCinemaId(cinema.getId());
            order.setMovie(name);
            order.setMovieTime(tvTime.getText().toString());
            order.setPrice(price);
            orderlistener.saveOrder(order);
            edtMovieName.setText("");
            edtMoviePrice.setText("");

        });
        find(R.id.add_order_movie_btn_cancel).setOnClickListener(v -> {
            orderlistener.cancelAddOrder();
        });

        imgQRCode.setOnLongClickListener(v -> {
            Bitmap bitmap=((BitmapDrawable)imgQRCode.getDrawable()).getBitmap();
            Toast.makeText(getActivity(), AppUtils.readQRCode(bitmap),Toast.LENGTH_SHORT).show();
            return true;
        });


    }

    /** 日期选择器 **/
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvTime.setText(now);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MARCH,1);
        String end=sdf.format(calendar.getTime());
        datePicker = new CustomDatePicker(getActivity(), s -> tvTime.setText(s), now, end);
        datePicker.setIsLoop(true);
        datePicker.showSpecificTime(true);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_orders;
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


    /** 4.接口对象赋值 **/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener= (OnFragmentInteractionListener) context;
            orderlistener= (OnOrderCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnFragmentInteractionListener" +
                    "&OnOrderCreatedListener接口");
        }
    }
    /** 4.销毁 **/
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        orderlistener=null;
    }

    /** 取消保存数据
     * 1.声明接口 **/
    public interface OnOrderCreatedListener {
        /**取消**/
        void cancelAddOrder();
        /**保存数据**/
        void saveOrder(Order order);

    }

}
