package com.example.kuailefupos.homefragment.homequoteactivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.kuailefupos.R;
import com.example.kuailefupos.adapter.HomeQuoteGridViewAdapter;
import com.example.kuailefupos.base.BaseActivity;
import com.example.kuailefupos.datafragment.databill.DataBillActivity;
import com.example.kuailefupos.datafragment.databill.adapter.DataBillDialogGridViewAdapter;
import com.example.kuailefupos.datafragment.databillbean.BillTypeBean;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.AreaAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.CityAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.ProvinceAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameUnActivity;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.CityBean;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.DistrictBean;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.ProvinceBean;
import com.example.kuailefupos.homefragment.homequoteactivity.bean.MerchTypeBean1;
import com.example.kuailefupos.homefragment.homequoteactivity.bean.MerchTypeBean2;
import com.example.kuailefupos.homefragment.homequoteactivity.bean.MerchTypeBean3;
import com.example.kuailefupos.net.HttpRequest;
import com.example.kuailefupos.net.OkHttpException;
import com.example.kuailefupos.net.RequestParams;
import com.example.kuailefupos.net.ResponseCallback;
import com.example.kuailefupos.utils.Utils;
import com.example.kuailefupos.views.MyDialog;
import com.example.kuailefupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ??????: qgl
 * ???????????????2021/8/17
 * ??????: ????????????1
 */
public class HomeQuoteActivity1 extends BaseActivity implements View.OnClickListener {
    //????????????
    private RelativeLayout terminal_rate;
    //????????????TextView
    private TextView terminal_rate_tv;
    //??????????????????
    private List<MerchTypeBean3> list3;
    //?????????????????????
    private String mctType3 = "";
    //???????????????????????????
    private RelativeLayout province_relative;
    //??????????????????????????????TextView
    private TextView province_tv;
    //??????SN???
    private EditText quote_sn_num;
    //?????????????????????
    private EditText quote_phone;
    //????????????
    private Button submit_bt;
     /**********--??????????????????JD ??????--**************/
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private TextView mAreaTv;
    private ImageView mCloseImg;
    private PopupWindow popWindow;
    private View mSelectedLine;
    private View popView;
    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private AreaAdapter mAreaAdapter;
    private List<ProvinceBean> provinceList = new ArrayList<>();
    private List<CityBean> cityList = null;
    private List<DistrictBean> areaList = null;
    private int tabIndex = 0;
    private Context context;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private String province = ""; //?????????
    private String city = ""; //?????????
    private String area = ""; //?????????
    private TextView toast_tv;
    /**********--??????????????????JD ??????--**************/
    //?????????
    private LinearLayout iv_back;
    // ????????????
    public static HomeQuoteActivity1 instance = null;
    // ???????????? 1 ?????? 2 ??????
    private String type = "1";
    //?????????????????????ID
    private String Hid = "";
    //?????? 1 ?????? 2 ??????
    private String bj_type = "";
    /*******************************???????????????????????????************************/
    private String snNum;
    private String Phone;
    private String AddressU;
    private String FeiLv;
    private String ID_url1;
    private String ID_url2;
    private String ID_url3;
    private String ID_url4;
    private String ID_url5;
    private String Name;
    private String IdNum;
    private String onYear;
    private String outYear;
    private String BankNum;
    private String merchantNo;
    /*******************************???????????????????????????************************/

    //???????????????Adapter
    private HomeQuoteGridViewAdapter madapter;
        private List<MerchTypeBean3>flList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homequote_activity_01;
    }

    @Override
    protected void initView() {
        instance = this;
        context = this;
        iv_back = findViewById(R.id.iv_back);
        terminal_rate = findViewById(R.id.terminal_rate);
        terminal_rate_tv = findViewById(R.id.terminal_rate_tv);
        province_relative = findViewById(R.id.province_relative);
        province_tv = findViewById(R.id.province_tv);
        quote_sn_num = findViewById(R.id.quote_sn_num);
        quote_phone = findViewById(R.id.quote_phone);
        submit_bt = findViewById(R.id.submit_bt);
        toast_tv = findViewById(R.id.toast_tv);
        posData();
    }

    @Override
    protected void initListener() {
        terminal_rate.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        province_relative.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        bj_type = getIntent().getStringExtra("bj_type");

        if (type.equals("2")){
         Hid = getIntent().getStringExtra("id");
         posDetail(Hid);
        }
        if (bj_type.equals("3")){
            quote_sn_num.setEnabled(false);
            quote_phone.setEnabled(false);
            quote_phone.setClickable(false);
            province_relative.setClickable(false);
            province_relative.setEnabled(false);
            toast_tv.setVisibility(View.VISIBLE);
        }
    }

    //????????????????????????????????????????????????
    private void posData() {
        RequestParams params = new RequestParams();
        HttpRequest.getAddMerchantMessageType(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    list3 = gson.fromJson(data.getJSONArray("posErminalRatelist").toString(),
                            new TypeToken<List<MerchTypeBean3>>() {
                            }.getType());
                    flList.addAll(list3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.terminal_rate:
                showDialog(flList);
                break;
            case R.id.province_relative:
                showCityPicker();
                break;
            case R.id.submit_bt:
                Intent intent = new Intent(HomeQuoteActivity1.this, HomeQuoteActivity2.class);

                if (TextUtils.isEmpty(quote_sn_num.getText().toString().trim())) {
                    showToast(3, "SN??????");
                    return;
                }

                if (TextUtils.isEmpty(quote_phone.getText().toString().trim())) {
                    showToast(3, "??????");
                    return;
                }

                if (TextUtils.isEmpty(province.trim())) {
                    showToast(3, "???????????????");
                    return;
                }

                if (TextUtils.isEmpty(mctType3.trim())) {
                    showToast(3, "????????????");
                    return;
                }
                if (type.equals("2")){
                    intent.putExtra("quote_sn_num",snNum);
                    intent.putExtra("quote_phone",Phone);
                }else {
                    intent.putExtra("quote_sn_num",quote_sn_num.getText().toString().trim());
                    intent.putExtra("quote_phone",quote_phone.getText().toString().trim());
                }
                intent.putExtra("mctType3",mctType3);
                intent.putExtra("province",province);
                intent.putExtra("city",city);
                intent.putExtra("area",area);
                intent.putExtra("type",type);
                intent.putExtra("bj_type",bj_type);
                if (type.equals("2")){
                    intent.putExtra("Hid",Hid);
                    intent.putExtra("ID_url1",ID_url1);
                    intent.putExtra("ID_url2",ID_url2);
                    intent.putExtra("ID_url3",ID_url3);
                    intent.putExtra("ID_url4",ID_url4);
                    intent.putExtra("ID_url5",ID_url5);
                    intent.putExtra("Name",Name);
                    intent.putExtra("IdNum",IdNum);
                    intent.putExtra("onYear",onYear);
                    intent.putExtra("outYear",outYear);
                    intent.putExtra("BankNum",BankNum);
                    intent.putExtra("merchantNo",merchantNo);
                }
                startActivity(intent);
                break;

        }
    }

    //?????????????????????
    private void posDetail(String id){
        RequestParams params = new RequestParams();
        params.put("merchantNo",id);
        HttpRequest.getEntry(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    snNum = data.getString("sn");
                    Phone = data.getString("phone");
                    AddressU = data.getJSONObject("provinceno").getString("cname") +"-"+ data.getJSONObject("cityno").getString("cname")+"-"+data.getJSONObject("areano").getString("cname");
                    if (!"null".equals(data.getString("feeChlId"))){
                        FeiLv = data.getJSONObject("feeChlId").getString("cname");
                        mctType3 = data.getJSONObject("feeChlId").getString("dictValue");
                    }else {
                        FeiLv = "";
                        mctType3 = "";
                    }
                    province = data.getJSONObject("provinceno").getString("dictValue");
                    city = data.getJSONObject("cityno").getString("dictValue");
                    area = data.getJSONObject("areano").getString("dictValue");
                    Name = data.getString("applicant");
                    IdNum = data.getString("certificateno");
                    onYear = data.getString("certificatestartdate");
                    outYear = data.getString("certificateenddate");
                    BankNum = data.getString("bankcardaccount");
                    merchantNo = data.getString("merchantNo");
                    ID_url1 = data.getString("idcardfront");
                    ID_url2 = data.getString("idcardback");
                    ID_url3 = data.getString("idcardhand");
                    ID_url4 = data.getString("bankcardfront");
                    ID_url5 = data.getString("bankcardback");
                    //???????????????
                    setVaule();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    private void setVaule() {
        quote_sn_num.setText(snNum.substring(0,1) + "********");
        quote_phone.setText(Phone.substring(0,3) + "****" + Phone.substring(Phone.length() - 4));
        province_tv.setText(AddressU);
        terminal_rate_tv.setText(FeiLv);
    }

   /************************************** ???????????????????????????--????????????????????????????????????--****************************************/
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                case 0:
                    provinceList = (List) msg.obj;
                    mProvinceAdapter.notifyDataSetChanged();
                    mCityListView.setAdapter(mProvinceAdapter);
                    break;
                case 1:
                    cityList = (List) msg.obj;
                    mCityAdapter.notifyDataSetChanged();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCityListView.setAdapter(mCityAdapter);
                        tabIndex = 1;
                    }
                    break;
                case 2:
                    areaList = (List) msg.obj;
                    mAreaAdapter.notifyDataSetChanged();
                    if (areaList != null && !areaList.isEmpty()) {
                        mCityListView.setAdapter(mAreaAdapter);
                        tabIndex = 2;
                    }
            }
            updateTabsStyle(tabIndex);
            updateIndicator();
            return true;
        }
    });

    public void showCityPicker() {
        initJDCityPickerPop();
        if (!isShow()) {
            popWindow.showAtLocation(popView, 80, 0, 0);
        }

    }

    private void initJDCityPickerPop() {
        tabIndex = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popView = layoutInflater.inflate(R.layout.pop_jdcitypicker, (ViewGroup) null);
        mCityListView = (ListView) popView.findViewById(R.id.city_listview);
        mProTv = (TextView) popView.findViewById(R.id.province_tv);
        mCityTv = (TextView) popView.findViewById(R.id.city_tv);
        mAreaTv = (TextView) popView.findViewById(R.id.area_tv);
        mCloseImg = (ImageView) popView.findViewById(R.id.close_img);
        mSelectedLine = popView.findViewById(R.id.selected_line);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mCloseImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePop();
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mProTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 0;
                if (mProvinceAdapter != null) {
                    mCityListView.setAdapter(mProvinceAdapter);
                    if (mProvinceAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mProvinceAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 1;
                if (mCityAdapter != null) {
                    mCityListView.setAdapter(mCityAdapter);
                    if (mCityAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mCityAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mAreaTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 2;
                if (mAreaAdapter != null) {
                    mCityListView.setAdapter(mAreaAdapter);
                    if (mAreaAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mAreaAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList(position);
            }
        });
        Utils.setBackgroundAlpha(context, 0.5F);
        updateIndicator();
        updateTabsStyle(-1);
        posCity();
    }

    @SuppressLint("WrongConstant")
    private void updateTabsStyle(int tabIndex) {
        switch (tabIndex) {
            case -1:
            case 0:
                mProTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(8);
                mAreaTv.setVisibility(8);
                break;
            case 1:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(8);
                break;
            case 2:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorSelected));
                mAreaTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(0);
        }

    }

    private void updateIndicator() {
        popView.post(new Runnable() {
            public void run() {
                switch (tabIndex) {
                    case 0:
                        tabSelectedIndicatorAnimation(mProTv).start();
                        break;
                    case 1:
                        tabSelectedIndicatorAnimation(mCityTv).start();
                        break;
                    case 2:
                        tabSelectedIndicatorAnimation(mAreaTv).start();
                }

            }
        });
    }

    private AnimatorSet tabSelectedIndicatorAnimation(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mSelectedLine, "X", new float[]{mSelectedLine.getX(), tab.getX()});
        final ViewGroup.LayoutParams params = mSelectedLine.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(new int[]{params.width, tab.getMeasuredWidth()});
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mSelectedLine.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(new Animator[]{xAnimator, widthAnimator});
        return set;
    }

    private void hidePop() {
        if (isShow()) {
            popWindow.dismiss();
        }
    }

    private boolean isShow() {
        return popWindow.isShowing();
    }

    @SuppressLint("WrongConstant")
    private void updateTabVisible() {
        mProTv.setVisibility(provinceList != null && !provinceList.isEmpty() ? 0 : 8);
        mCityTv.setVisibility(cityList != null && !cityList.isEmpty() ? 0 : 8);
        mAreaTv.setVisibility(areaList != null && !areaList.isEmpty() ? 0 : 8);
    }

    private void selectedList(int position) {
        switch (tabIndex) {
            case 0:
                ProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getCname());
                    mCityTv.setText("?????????");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    posCity1(mProvinceAdapter.getItem(position).getDictValue());
                }
                break;
            case 1:
                CityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    mCityTv.setText("" + cityBean.getCname());
                    mAreaTv.setText("?????????");
                    mCityAdapter.updateSelectedPosition(position);
                    mCityAdapter.notifyDataSetChanged();
                    posCity2(mCityAdapter.getItem(position).getDictValue());
                }
                break;
            case 2:
                DistrictBean districtBean = mAreaAdapter.getItem(position);
                if (districtBean != null) {
                    callback(districtBean);
                }
        }

    }

    private void callback(DistrictBean districtBean) {
        ProvinceBean provinceBean = provinceList != null && !provinceList.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (ProvinceBean) provinceList.get(mProvinceAdapter.getSelectedPosition()) : null;
        CityBean cityBean = cityList != null && !cityList.isEmpty() && mCityAdapter != null && mCityAdapter.getSelectedPosition() != -1 ? (CityBean) cityList.get(mCityAdapter.getSelectedPosition()) : null;
        //???????????????
        province = provinceBean.getDictValue();
        city = cityBean.getDictValue();
        area = districtBean.getDictValue();
        Log.e("??????", provinceBean.getDictValue() + "" + cityBean.getDictValue() + "" + districtBean.getDictValue());
        province_tv.setText(provinceBean + "-" + cityBean + "-" + districtBean);
        hidePop();
    }

    // ?????????????????????
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("dictType", "1");
        params.put("dictLevelCode", "");
        HttpRequest.getCityB(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    provinceList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<ProvinceBean>>() {
                            }.getType());
                    if (provinceList != null && !provinceList.isEmpty()) {
                        mProvinceAdapter = new ProvinceAdapter(context, provinceList);
                        mCityListView.setAdapter(mProvinceAdapter);
                    } else {
                        Log.e("MainActivity.tshi", "?????????????????????????????????");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

        // ?????????????????????
    public void posCity1(String code) {
            RequestParams params = new RequestParams();
            params.put("dictType", "");
            params.put("dictLevelCode", code);
            HttpRequest.getCityB(params, "", new ResponseCallback() {
                @Override
                public void onSuccess(Object responseObj) {
                    //???????????????????????????
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    try {
                        JSONObject result = new JSONObject(responseObj.toString());
                        cityList = gson.fromJson(result.getJSONArray("data").toString(),
                                new TypeToken<List<CityBean>>() {
                                }.getType());
                        mCityAdapter = new CityAdapter(context, cityList);
                        mHandler.sendMessage(Message.obtain(mHandler, 1, cityList));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(OkHttpException failuer) {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            });
        }

    // ??????????????????
    public void posCity2(String code) {
        RequestParams params = new RequestParams();
        params.put("dictType", "");
        params.put("dictLevelCode", code);
        HttpRequest.getCityB(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    areaList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<DistrictBean>>() {
                            }.getType());
                    mAreaAdapter = new AreaAdapter(context, areaList);
                    mHandler.sendMessage(Message.obtain(mHandler, 2, areaList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }
    /***************************************** ??????????????????????????? --????????????????????????????????????***************************************************************/

    /***
     * ????????????
     */
    public void showDialog(List<MerchTypeBean3> mList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_quote_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new HomeQuoteGridViewAdapter(HomeQuoteActivity1.this, mList);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //????????????position?????????adapter?????????
                madapter.changeState(i);
                terminal_rate_tv.setText(mList.get(i).getFeeChlName());
                mctType3 = mList.get(i).getFeeChlId();
            }
        });
        Dialog dialog = new MyDialog(HomeQuoteActivity1.this, true, true, (float) 1).setNewView(view);
        dialog.show();
        data_bill_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
