package com.example.kuailefupos.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kuailefupos.R;
import com.example.kuailefupos.base.BaseFragment;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.kuailefupos.homefragment.homewallet.HomeWalletActivity;
import com.example.kuailefupos.mefragment.meabout.MeAboutActivity;
import com.example.kuailefupos.mefragment.meaddres.MeAddressActivity;
import com.example.kuailefupos.mefragment.mebank.MeBankActivity;
import com.example.kuailefupos.mefragment.mefeilv.MeFeilvActivity;
import com.example.kuailefupos.mefragment.meinvitationpolite.MeInvitationPoliteActivity;
import com.example.kuailefupos.mefragment.meorder.MeOrderActivity;
import com.example.kuailefupos.mefragment.mepowerattorney.MePowerAttorneyActivity;
import com.example.kuailefupos.mefragment.mereferees.MeRefereesActivity;
import com.example.kuailefupos.mefragment.meupgrade.MeUpgradeActivity;
import com.example.kuailefupos.mefragment.setup.PersonalActivity;
import com.example.kuailefupos.mefragment.setup.SetUpActivity;
import com.example.kuailefupos.net.HttpRequest;
import com.example.kuailefupos.net.OkHttpException;
import com.example.kuailefupos.net.RequestParams;
import com.example.kuailefupos.net.ResponseCallback;
import com.example.kuailefupos.utils.SPUtils;
import com.example.kuailefupos.views.MyDialog1;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * ??????: qgl
 * ???????????????2020/12/10
 * ??????:?????????Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout main_me_refresh; //????????????
    private SimpleDraweeView me_logo; // ??????
    private ConstraintLayout main_me_or_code; // ?????????
    private TextView main_me_name; // ??????
    private TextView main_me_cooperation_code; // ???????????????
    private TextView main_me_order; // ??????
    private TextView main_me_machine; // ??????
    private TextView main_me_wallet; // ??????
    private TextView main_me_bank_card; // ?????????
    //????????????
    private RelativeLayout me_r1;
    //???????????????
    private RelativeLayout me_r2;
    //?????????
    private RelativeLayout me_r3;
    //??????
    private RelativeLayout me_r4;
    //????????????
    private RelativeLayout me_r5;
    private String nickName;  // ?????????
    private String merchCode;  // ?????????
    private String headUrl;  // ????????????
    private String Token;  // ??????
    private String userId; // ??????ID
    //???????????????1??????2?????????
    private String Code = "1";
    private String servicePhone = ""; //????????????
    //????????????
    private RelativeLayout me_r33;
    //??????
    private String integral;
    //??????
    private String walletAmount;
    private TextView intger_tv;
    private TextView wellat_tv;
    /**
     * ??????activity??????
     *
     * @param requestJson
     * @return
     */
    public static MeFragment newInstance(String requestJson) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * ?????????xml
     *
     * @return
     */
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.mefragment_main;
    }

    /**
     * ???????????????
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        main_me_refresh = rootView.findViewById(R.id.main_me_refresh);
        me_logo = rootView.findViewById(R.id.me_logo);
        main_me_or_code = rootView.findViewById(R.id.main_me_or_code);
        main_me_name = rootView.findViewById(R.id.main_me_name);
        main_me_cooperation_code = rootView.findViewById(R.id.main_me_cooperation_code);
        main_me_order = rootView.findViewById(R.id.main_me_order);
        main_me_machine = rootView.findViewById(R.id.main_me_machine);
        main_me_wallet = rootView.findViewById(R.id.main_me_wallet);
        main_me_bank_card = rootView.findViewById(R.id.main_me_bank_card);
        me_r1 = rootView.findViewById(R.id.me_r1);
        me_r2 = rootView.findViewById(R.id.me_r2);
        me_r3 = rootView.findViewById(R.id.me_r3);
        me_r4 = rootView.findViewById(R.id.me_r4);
        me_r5 = rootView.findViewById(R.id.me_r5);
        me_r33 = rootView.findViewById(R.id.me_r33);
        intger_tv = rootView.findViewById(R.id.intger_tv);
        wellat_tv = rootView.findViewById(R.id.wellat_tv);

    }

    //?????????????????????
    @Override
    protected void initListener() {
        main_me_or_code.setOnClickListener(this);
        main_me_order.setOnClickListener(this);
        main_me_machine.setOnClickListener(this);
        main_me_wallet.setOnClickListener(this);
        main_me_bank_card.setOnClickListener(this);
        me_r1.setOnClickListener(this);
        me_r2.setOnClickListener(this);
        me_r3.setOnClickListener(this);
        me_r4.setOnClickListener(this);
        me_r5.setOnClickListener(this);
        me_r33.setOnClickListener(this);
        SwipeData();
    }

    // ??????????????????
    public void SwipeData() {
        main_me_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        main_me_refresh.setOnRefreshListener(this);
    }

    //????????????
    @Override
    protected void loadData() {
        Token = SPUtils.get(getActivity(), "Token", "").toString();
        userId = SPUtils.get(getActivity(), "userId", "").toString();
        //posData();
        MeData();
    }

    //????????????
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_me_bank_card:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    //???????????????
                    startActivity(new Intent(getActivity(), MeBankActivity.class));
                }
                break;
            case R.id.main_me_order:
                // ??????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeOrderActivity.class));
                }
                break;
            case R.id.main_me_machine:
                // ??????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeUpgradeActivity.class));
                }
                break;
            case R.id.me_r1:
                //????????????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeFeilvActivity.class));
                }
                break;
            case R.id.me_r2:
                //???????????????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeRefereesActivity.class));
                }
                break;
            case R.id.me_r3:
                //?????????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MePowerAttorneyActivity.class));
                }
                break;
            case R.id.me_r4:
                //??????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), SetUpActivity.class));
                }
                break;
            case R.id.me_r5:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    //????????????
                    startActivity(new Intent(getActivity(), MeAboutActivity.class));
                }
                break;
            case R.id.main_me_wallet:
                //????????????
                if (Code.equals("2")) {
                    showDialog();
                } else {

                    //startActivity(new Intent(getActivity(), HomeWalletActivity.class));
                    startActivityForResult(new Intent(getActivity(), HomeWalletActivity.class),140);
                }
                break;
            case R.id.main_me_or_code:
                //????????????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), PersonalActivity.class));
                }
                break;
            case R.id.me_r33:
                //??????????????????
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getActivity(), MeAddressActivity.class);
                    intent.putExtra("status","2");
                    startActivity(intent);
                }
                break;
        }
    }

    //??????
    @Override
    public void onRefresh() {
        //??????????????????
        main_me_refresh.setRefreshing(true);
//        posData();
        MeData();
    }


    private void setText() {
        Uri imgurl=Uri.parse(headUrl);
        // ??????Fresco???????????????????????????
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);

        me_logo.setImageURI(imgurl);
        main_me_name.setText(nickName);
        main_me_cooperation_code.setText("??????????????????" + merchCode);
        intger_tv.setText(integral + "");
        wellat_tv.setText(walletAmount);
    }

    //????????????
    public void posData() {
        //????????????????????????
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        HttpRequest.getHomeDate(params, Token, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                main_me_refresh.setRefreshing(false);
                try {
                    Code = "1";
                    JSONObject result = new JSONObject(responseObj.toString());
                    nickName = result.getJSONObject("data").getString("nickName");
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    headUrl = result.getJSONObject("data").getString("portrait");
                    servicePhone = result.getJSONObject("data").getString("servicePhone");
                    //SPUtils.put(getActivity(), "servicePhone", servicePhone);

                    setText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                main_me_refresh.setRefreshing(false);
                if (failuer.getEcode() == 500001) {
                    Code = "2";
                    showDialog();
                } else {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }
        });

    }

    public void MeData(){
        //????????????????????????
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        HttpRequest.getMeData(params, Token, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                main_me_refresh.setRefreshing(false);
                try {
                    Code = "1";
                    JSONObject result = new JSONObject(responseObj.toString());
                    nickName = result.getJSONObject("data").getString("nickName");
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    headUrl = result.getJSONObject("data").getString("portrait");
//                    servicePhone = result.getJSONObject("data").getString("servicePhone");
//                    SPUtils.put(getActivity(), "servicePhone", servicePhone);
                    integral = result.getJSONObject("data").getString("integral");
                    walletAmount = result.getJSONObject("data").getString("walletAmount");
                    setText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                main_me_refresh.setRefreshing(false);
                if (failuer.getEcode() == 500001) {
                    Code = "2";
                    showDialog();
                } else {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }

        });
    }


    /**
     * ????????????????????????????????????
     */
    public void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.perfect_dialog_layout, null);
        Button perfect_into = view.findViewById(R.id.perfect_into);
        ImageView perfect_out = view.findViewById(R.id.perfect_out);
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        perfect_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        perfect_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), RealNameOnActivity.class);
                intent.putExtra("infoCode", Code);
                startActivity(intent);
            }
        });
    }

    // ????????????dialog
    private void showDialog1(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("???????????????  " + value);
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                dialog.dismiss();
                callPhone(value);
            }
        });
    }


    //??????????????????
    public void onEventMainThread(MeFragment ev) {
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    /**
     * ????????????????????????????????????
     *
     * @param phoneNum ????????????
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shouLog("------------------>","?????????");
        onRefresh();
    }
}
