package com.example.kuailefupos.homefragment.homeequipment.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.kuailefupos.R;
import com.example.kuailefupos.adapter.ChooseGridViewAdapter1;
import com.example.kuailefupos.adapter.ChooseGridViewAdapter2;
import com.example.kuailefupos.adapter.ChooseGridViewAdapter3;
import com.example.kuailefupos.adapter.MyViewPageAdapter;
import com.example.kuailefupos.base.BaseActivity;
import com.example.kuailefupos.homefragment.homeequipment.adapter.ChooserListAdapter;
import com.example.kuailefupos.homefragment.homeequipment.bean.TerminalActivityBean;
import com.example.kuailefupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.kuailefupos.homefragment.homeequipment.bean.TerminalEvenBusBean1;
import com.example.kuailefupos.homefragment.homeequipment.bean.ScreeningBean;
import com.example.kuailefupos.homefragment.homeequipment.fragment.transfer.IntervalTransferFragment;
import com.example.kuailefupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment;
import com.example.kuailefupos.homefragment.homeequipment.fragment.transfer.SelectTransferFragment1;
import com.example.kuailefupos.homefragment.homeintegral.bean.IntegralMostBean;
import com.example.kuailefupos.net.HttpRequest;
import com.example.kuailefupos.net.OkHttpException;
import com.example.kuailefupos.net.RequestParams;
import com.example.kuailefupos.net.ResponseCallback;
import com.example.kuailefupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ??????: qgl
 * ???????????????2020/12/24
 * ??????:????????????
 */
public class TerminalTransferActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private LinearLayout iv_back;
    ArrayList<String> titleDatas = new ArrayList<>();
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private DrawerLayout drawer_layout;
    private RadioButton terminal_transfer_determine_rb, terminal_transfer_cancel_rb;
    // ???????????????Fragment 1.????????????,2.????????????
    private int fragmentCode = 1;
    private TerminalEvenBusBean busBean;
    private TerminalEvenBusBean1 busBean1;
    //????????????
    private String posType = "";
    /*************************** ?????? ************************/
    //??????Item???
    private List<Integer> showTitle;
    //?????????????????????
    private List<String> menuList = new ArrayList<>();
    //??????????????????
    private List<IntegralMostBean.PosTypeList> homeList = new ArrayList<>();
    //?????????
    private List<IntegralMostBean> memberList = new ArrayList<>();
    //????????????adapter
    private ChooseGridViewAdapter2 madapter;
    //????????????adapter
    private ChooseGridViewAdapter3 madapter3;
    //???????????????GridView
    private MyGridView gvTest;
    //???????????????GridView
    private MyGridView gvTest2;

    @Override
    protected int getLayoutId() {
        // ?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.terminal_transfer_activity;
    }

    @Override
    protected void initView() {
        terminal_transfer_determine_rb = findViewById(R.id.terminal_transfer_determine_rb);
        terminal_transfer_cancel_rb = findViewById(R.id.terminal_transfer_cancel_rb);
        tab_layout = findViewById(R.id.terminal_transfer_tab_layout);
        viewpager = findViewById(R.id.terminal_transfer_viewpager);
        iv_back = findViewById(R.id.iv_back);
        drawer_layout = findViewById(R.id.drawer_layout);
        gvTest = findViewById(R.id.my_grid1);
        gvTest2 = findViewById(R.id.my_grid2);
        //????????????
        madapter = new ChooseGridViewAdapter2(TerminalTransferActivity.this,menuList);
        gvTest.setAdapter(madapter);
        //????????????
        madapter3 = new ChooseGridViewAdapter3(TerminalTransferActivity.this,homeList);
        gvTest2.setAdapter(madapter3);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        terminal_transfer_determine_rb.setOnClickListener(this);
        terminal_transfer_cancel_rb.setOnClickListener(this);
        gvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                homeList.clear();
                homeList.addAll(memberList.get(position).getPosTypeList());
                madapter.setSelectorPosition(position);
                madapter.notifyDataSetInvalidated();
                madapter3.setaBoolean(false);
                madapter3.notifyDataSetInvalidated();
                posType = "";
            }
        });
        gvTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                shouLog("?????????------------>","YES");
                madapter3.setSelectorPosition(position);
                madapter3.setaBoolean(true);
                madapter3.notifyDataSetInvalidated();
                posType = homeList.get(position).getId();
            }
        });
    }

    @Override
    protected void initData() {
        titleDatas.add("????????????");
        titleDatas.add("????????????");
        fragmentList.add(new SelectTransferFragment1());
        fragmentList.add(new IntervalTransferFragment());
        init();
        postData1();
    }

    private void init() {
        MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter(getSupportFragmentManager(), titleDatas, fragmentList);
        tab_layout.setSelectedTabIndicator(0);
        viewpager.setAdapter(myViewPageAdapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(myViewPageAdapter);
    }

    public void setListSize(int value) {
        fragmentCode = value;
        if (fragmentCode == 2) {
            // ??????
            posType = "";
            madapter.newAdd();
        }
        // ???????????????
        drawer_layout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.terminal_transfer_determine_rb:
                // ??????
                posType = "";
                homeList.clear();
                homeList.addAll(memberList.get(0).getPosTypeList());
                madapter.newAdd();
                madapter3.newAdd();
                madapter3.setaBoolean(false);
                break;
            case R.id.terminal_transfer_cancel_rb:
                drawer_layout.closeDrawer(GravityCompat.END);
                // ????????????
                busBean = new TerminalEvenBusBean();
                busBean.setTerminalType(posType);
//                busBean.setMostType(mostType);
                //????????????
//                busBean1 = new TerminalEvenBusBean1();
//                busBean1.setTerminalType(terminalType);
//                busBean1.setMostType(mostType);
//                if (fragmentCode == 1) {
                    EventBus.getDefault().post(busBean);
//                } else {
//                    EventBus.getDefault().post(busBean1);
//                }
                // ???????????????
                break;
        }
    }

    // ??????????????????
    public void postData1() {
        RequestParams params = new RequestParams();
        HttpRequest.getPosBrandTypeAll(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<IntegralMostBean>>() {
                            }.getType());
                    showTitle = new ArrayList<>();
                    for (int i = 0; i < memberList.size() ; i++) {
                        menuList.add(memberList.get(i).getBrandName());
                        showTitle.add(i);
                    }
                    homeList.addAll(memberList.get(0).getPosTypeList());
                    madapter.notifyDataSetChanged();
                    madapter3.notifyDataSetChanged();
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

}
