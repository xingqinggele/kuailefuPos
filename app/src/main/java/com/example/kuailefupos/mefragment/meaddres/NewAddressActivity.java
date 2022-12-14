package com.example.kuailefupos.mefragment.meaddres;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuailefupos.R;
import com.example.kuailefupos.base.BaseActivity;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.AreaAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.CityAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.adapter.ProvinceAdapter;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.CityBean;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.DistrictBean;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.ProvinceBean;
import com.example.kuailefupos.net.HttpRequest;
import com.example.kuailefupos.net.OkHttpException;
import com.example.kuailefupos.net.RequestParams;
import com.example.kuailefupos.net.ResponseCallback;
import com.example.kuailefupos.useractivity.ModifyPasswordActivity;
import com.example.kuailefupos.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.kuailefupos.utils.Utility.isChinaPhoneLegal;

/**
 * ?????????  qgl
 * ????????? 2021-04-28
 * ????????? ??????????????????
 */
public class NewAddressActivity extends BaseActivity implements View.OnClickListener {
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
    //???????????????
//    private String cityString = "";
    /**********--??????????????????JD ??????--**************/
    //??????
    private EditText name_ed;
    //??????
    private EditText phone_ed;
    //??????
    private TextView city_tv;
    //????????????
    private EditText address_ed;
    //??????????????????
    private TextView new_address_sub;
    //??????????????? ?????? 1 ?????? 2 ??????
    private String addType = "";
    //????????????ID
    private String addId = "";
    //??????????????????
    private String addName = "";
    //??????????????????
    private String addPhone = "";
    //??????????????????
    private String addCity = "";
    //??????????????????
    private String addDetails = "";
    //?????????
    private LinearLayout iv_back;


    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.new_address_activity;
    }

    @Override
    protected void initView() {
        context = NewAddressActivity.this;
        name_ed = findViewById(R.id.name_ed);
        phone_ed = findViewById(R.id.phone_ed);
        city_tv = findViewById(R.id.city_tv);
        address_ed = findViewById(R.id.address_ed);
        new_address_sub = findViewById(R.id.new_address_sub);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        city_tv.setOnClickListener(this);
        new_address_sub.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        addType = getIntent().getStringExtra("type");
        if (addType.equals("2")) {
            addId = getIntent().getStringExtra("id");
            addName = getIntent().getStringExtra("name");
            addPhone = getIntent().getStringExtra("phone");
            addCity = getIntent().getStringExtra("addr");
            addDetails = getIntent().getStringExtra("addrInfo");
            name_ed.setText(addName);
            phone_ed.setText(addPhone);
            city_tv.setText(addCity);
            address_ed.setText(addDetails);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.city_tv:
                showCityPicker();
                break;
            case R.id.new_address_sub:
                if (TextUtils.isEmpty(name_ed.getText().toString().trim())) {
                    showToast(3, "???????????????");
                    return;
                }
                if (!isChinaPhoneLegal(phone_ed.getText().toString().trim())) {
                    showToast(3, "????????????????????????");
                    return;
                }

                if (TextUtils.isEmpty(city_tv.getText().toString().trim())) {
                    showToast(3, "??????????????????");
                    return;
                }
                if (TextUtils.isEmpty(address_ed.getText().toString().trim())) {
                    showToast(3, "?????????????????????");
                    return;
                }
                //??????????????????
                if (addType.equals("1")) {
                    newAddress(name_ed.getText().toString().trim(), phone_ed.getText().toString().trim(), city_tv.getText().toString().trim(), address_ed.getText().toString().trim());
                } else {
                    EditorAddress(addId, name_ed.getText().toString().trim(), phone_ed.getText().toString().trim(), city_tv.getText().toString().trim(), address_ed.getText().toString().trim());
                }
                break;
        }
    }


    /**
     * ??????????????????
     *
     * @param name       ??????
     * @param phone      ??????
     * @param cityString ????????????
     * @param trim1      ????????????
     */
    private void newAddress(String name, String phone, String cityString, String trim1) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("phone", phone);
        params.put("addr", cityString);
        params.put("addrInfo", trim1);
        HttpRequest.getSaveAddress(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                showToast(3, "????????????");
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param name       ??????
     * @param phone      ??????
     * @param cityString ????????????
     * @param trim1      ????????????
     */
    private void EditorAddress(String id, String name, String phone, String cityString, String trim1) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("addr", cityString);
        params.put("addrInfo", trim1);
        HttpRequest.EditorAddress(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                showToast(3, "????????????");
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
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
        city_tv.setText(provinceBean + "" + cityBean + "" + districtBean);
        //cityString = provinceBean + "" + cityBean + "" + districtBean;
        hidePop();
    }

    // ?????????????????????
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("dictType", "1");
        params.put("dictLevelCode", "");
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
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
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
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
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
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

}
