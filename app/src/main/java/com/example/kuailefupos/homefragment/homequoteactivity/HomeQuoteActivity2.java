package com.example.kuailefupos.homefragment.homequoteactivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kuailefupos.R;
import com.example.kuailefupos.base.BaseActivity;
import com.example.kuailefupos.bean.IdCardInfo;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.kuailefupos.mefragment.setup.PersonalActivity;
import com.example.kuailefupos.utils.ImageConvertUtil;
import com.example.kuailefupos.utils.TimeUtils;
import com.example.kuailefupos.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * ??????: qgl
 * ???????????????2021/8/17
 * ??????: ????????????2
 */
public class HomeQuoteActivity2 extends BaseActivity implements View.OnClickListener {
    //?????????
    private LinearLayout iv_back;
    // ???????????????
    private SimpleDraweeView id_card_is;
    // ???????????????
    private SimpleDraweeView id_card_the;
    //?????????????????????
    private SimpleDraweeView id_card_pay;
    //???????????????????????????
    private String IdCard1_Url = "";
    //??????
    private String IdUrl1isActive = "1";
    //???????????????????????????
    private String IdCard2_Url = "";
    //??????
    private String IdUrl2isActive = "1";
    //???????????????????????????
    private String Handheld_url = "";
    //??????
    private String IdUrl3isActive = "1";
    // ???????????????
    private String IdName;
    // ???????????????
    private String IdNumber;
    // ??????????????????
    private String IdValidDate;
    //?????????????????????
    private String st1 = "";
    private String s = "";
    //?????????????????????
    private String st2 = "";
    private String t = "";
    // ??????
    private EditText name_ed;
    // ???????????????
    private EditText card_number_ed;
    //????????????
    private TextView home_quote_start_time;
    //????????????
    private TextView home_quote_un_time;
    //???????????????
    private Button submit_bt;

    /*******?????????????????????************/
    private String snCode = ""; //??????SN???
    private String uPhone = ""; //????????????
    private String fType = ""; //????????????
    private String province = ""; //??????????????????
    private String city = ""; //??????????????????
    private String area = ""; //??????????????????
    /*******?????????????????????************/
    // ???????????? 1 ?????? 2 ??????
    private String type = "1";
    //?????????????????????ID
    private String Hid = "";
    private String ID_url4;
    private String ID_url5;
    //    private String Name;
//    private String IdNum;
    private String BankNum;
    private String merchantNo;
    // ????????????
    public static HomeQuoteActivity2 instance = null;
    //?????? 1 ?????? 2 ??????
    private String bj_type = "";
    final private int IdCardIn = 1004;
    final private int IdCardOn = 1005;
    private PopupWindow popWindow;
    private View popView;

    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homequote_activity_02;
    }

    @Override
    protected void initView() {
        instance = this;
        iv_back = findViewById(R.id.iv_back);
        id_card_is = findViewById(R.id.id_card_is);
        name_ed = findViewById(R.id.name_ed);
        card_number_ed = findViewById(R.id.card_number_ed);
        id_card_the = findViewById(R.id.id_card_the);
        id_card_pay = findViewById(R.id.id_card_pay);
        home_quote_start_time = findViewById(R.id.home_quote_start_time);
        home_quote_un_time = findViewById(R.id.home_quote_un_time);
        submit_bt = findViewById(R.id.submit_bt);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        id_card_is.setOnClickListener(this);
        id_card_the.setOnClickListener(this);
        id_card_pay.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        home_quote_start_time.setOnClickListener(this);
        home_quote_un_time.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        snCode = getIntent().getStringExtra("quote_sn_num");
        uPhone = getIntent().getStringExtra("quote_phone");
        fType = getIntent().getStringExtra("mctType3");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        area = getIntent().getStringExtra("area");
        type = getIntent().getStringExtra("type");
        bj_type = getIntent().getStringExtra("bj_type");
        shouLog("????????????2?????????????????????--------", "---------------");
        shouLog("snCode=", snCode);
        shouLog("uPhone=", uPhone);
        shouLog("fType=", fType);
        shouLog("province=", province);
        shouLog("city=", city);
        shouLog("area=", area);
        shouLog("type=", type);
        shouLog("bj_type=", bj_type);
        shouLog("????????????2?????????????????????--------", "---------------");
        if (type.equals("2")) {
            Hid = getIntent().getStringExtra("Hid");
            IdCard1_Url = getIntent().getStringExtra("ID_url1");
            IdCard2_Url = getIntent().getStringExtra("ID_url2");
            Handheld_url = getIntent().getStringExtra("ID_url3");
            ID_url4 = getIntent().getStringExtra("ID_url4");
            ID_url5 = getIntent().getStringExtra("ID_url5");
            IdName = getIntent().getStringExtra("Name");
            IdNumber = getIntent().getStringExtra("IdNum");
            s = getIntent().getStringExtra("onYear");
            t = getIntent().getStringExtra("outYear");
            BankNum = getIntent().getStringExtra("BankNum");
            merchantNo = getIntent().getStringExtra("merchantNo");
            name_ed.setText(IdName.substring(0, 1) + "**");
            card_number_ed.setText(IdNumber.substring(0, 3) + "**********" + IdNumber.substring(IdNumber.length() - 4));
            home_quote_start_time.setText(s);
            home_quote_un_time.setText(t);
        }
        if (bj_type.equals("3")) {
            id_card_is.setEnabled(false);
            id_card_the.setEnabled(false);
            id_card_pay.setEnabled(false);
            name_ed.setClickable(false);
            card_number_ed.setClickable(false);
            home_quote_start_time.setClickable(false);
            home_quote_un_time.setClickable(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_quote_start_time:
                selectTime(home_quote_start_time, 1);
                break;
            case R.id.home_quote_un_time:
                selectTime(home_quote_un_time, 2);
                break;
            // ?????????????????????
            case R.id.id_card_is:
                showEditPhotoWindow(IdCardIn);
                break;
            // ?????????????????????
            case R.id.id_card_the:
                showEditPhotoWindow(IdCardOn);
                break;
            // ???????????????????????????
            case R.id.id_card_pay:
                PictureSelector
                        .create(HomeQuoteActivity2.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
                break;
            case R.id.submit_bt:
                Intent intent = new Intent(HomeQuoteActivity2.this, HomeQuoteActivity3.class);
                if (TextUtils.isEmpty(IdCard1_Url)) {
                    showToast(3, "????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(IdCard2_Url)) {
                    showToast(3, "????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(Handheld_url)) {
                    showToast(3, "????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(name_ed.getText().toString().trim())) {
                    showToast(3, "????????????");
                    return;
                }
                if (TextUtils.isEmpty(card_number_ed.getText().toString().trim())) {
                    showToast(3, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(s)) {
                    showToast(3, "?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(t)) {
                    showToast(3, "?????????????????????");
                    return;
                }
                /*****???????????????******************/
                intent.putExtra("snCode", snCode);
                intent.putExtra("uPhone", uPhone);
                intent.putExtra("fType", fType);
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                /*****???????????????******************/
                intent.putExtra("IdUrl1", IdCard1_Url);
                intent.putExtra("IdUrl1isActive", IdUrl1isActive);
                intent.putExtra("IdUrl2", IdCard2_Url);
                intent.putExtra("IdUrl2isActive", IdUrl2isActive);
                intent.putExtra("IdUrl3", Handheld_url);
                intent.putExtra("IdUrl3isActive", IdUrl3isActive);
                intent.putExtra("startTime", s);
                intent.putExtra("endTime", t);
                intent.putExtra("type", type);
                intent.putExtra("bj_type", bj_type);
                if (type.equals("2")) {
                    intent.putExtra("Hid", Hid);
                    intent.putExtra("ID_url4", ID_url4);
                    intent.putExtra("ID_url5", ID_url5);
                    intent.putExtra("BankNum", BankNum);
                    intent.putExtra("merchantNo", merchantNo);
                    intent.putExtra("fName", IdName);
                    intent.putExtra("fNumber", IdNumber);
                } else {
                    intent.putExtra("fName", name_ed.getText().toString().trim());
                    intent.putExtra("fNumber", card_number_ed.getText().toString().trim());
                }
                startActivity(intent);
                break;
        }
    }

    /**
     * ?????????
     *
     * @param value
     */
    private void showEditPhotoWindow(int value) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popView = layoutInflater.inflate(R.layout.popwindow_main, (ViewGroup) null);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(popView, 80, 0, 0);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        TextView tv_select_pic = popView.findViewById(R.id.tv_photo);
        TextView tv_pai_pic = popView.findViewById(R.id.tv_photograph);
        TextView tv_cancl = popView.findViewById(R.id.tv_cancle);
        LinearLayout layout = popView.findViewById(R.id.dialog_ll);
        tv_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                if (value == IdCardIn) {

                    getIDCardIn();
                } else {
                    getIDCardOn();
                }
            }
        });
        tv_pai_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                PictureSelector
                        .create(HomeQuoteActivity2.this, value)
                        .selectPicture(false);
            }
        });
        tv_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });

    }

    /**
     * ???????????????????????????
     */
    private void initSdk(String secretId, String secretKey) {
        // ??????????????????
        OcrType ocrType = OcrType.BankCardOCR; // ???????????????????????????????????????
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .OcrType(ocrType)
                .ModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // ?????????SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    // ???????????????????????????
    private void setResultListData() {
        if (IdName != null && !IdName.isEmpty()) {
            name_ed.setText(IdName);
            card_number_ed.setText(IdNumber);
        }
        if (IdValidDate != null && !IdValidDate.isEmpty()) {
            shouLog("1------------>", IdValidDate);
            st1 = IdValidDate.substring(0, IdValidDate.indexOf("-"));
            st2 = IdValidDate.substring(st1.length() + 1);
            shouLog("2------------>", st1);
            shouLog("3------------>", st2);
            s = st1.replace(".", "");
            t = st2.replace(".", "");
            home_quote_start_time.setText(s);
            home_quote_un_time.setText(t);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
    }

    /************************************** ?????????????????? ***********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                IdUrl3isActive = "2";
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                Handheld_url = file.getPath();
                                id_card_pay.setImageBitmap(BitmapFactory.decodeFile(Handheld_url));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == IdCardIn) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                IdUrl1isActive = "2";
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                IdCard1_Url = file.getPath();
                                id_card_is.setImageBitmap(BitmapFactory.decodeFile(IdCard1_Url));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == IdCardOn) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                IdUrl2isActive = "2";
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                IdCard2_Url = file.getPath();
                                id_card_the.setImageBitmap(BitmapFactory.decodeFile(IdCard2_Url));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        }
    }

    /************************************** ?????????????????? ***********************************************************************/
    //?????????????????????
    private void getIDCardIn() {
        initSdk(getSecretId(), getSecretKey());
        OcrSDKKit.getInstance().startProcessOcr(HomeQuoteActivity2.this, OcrType.IDCardOCR_FRONT, null,
                new ISDKKitResultListener() {
                    @Override
                    public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                        IdCardInfo tempIdCardInfo = new Gson().fromJson(response, IdCardInfo.class);
                        Log.e("response", tempIdCardInfo.getRequestId());
                        Log.e("response", tempIdCardInfo.toString());
                        Bitmap bitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
                        try {
                            if (bitmap != null)
                                id_card_is.setImageBitmap(bitmap);
                            IdCard1_Url = ImageConvertUtil.getFile(bitmap).getCanonicalPath();
                            IdUrl1isActive = "2";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        IdName = tempIdCardInfo.getName();
                        IdNumber = tempIdCardInfo.getIdNum();
                        setResultListData();
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("requestId", requestId);
                        IdName = "";
                        IdNumber = "";
                    }
                });
    }

    //???????????????
    private void getIDCardOn() {
        initSdk(getSecretId(), getSecretKey());
        //???????????????
        OcrSDKKit.getInstance().startProcessOcr(HomeQuoteActivity2.this, OcrType.IDCardOCR_BACK, null,
                new ISDKKitResultListener() {
                    @Override
                    public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                        IdCardInfo tempIdCardInfo = new Gson().fromJson(response, IdCardInfo.class);
                        Log.e("response", tempIdCardInfo.getRequestId());
                        Bitmap bitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
                        try {
                            if (bitmap != null)
                                id_card_the.setImageBitmap(bitmap);
                            IdCard2_Url = ImageConvertUtil.getFile(bitmap).getCanonicalPath();
                            IdUrl2isActive = "2";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        IdValidDate = tempIdCardInfo.getValidDate();
                        setResultListData();
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("11111requestId", requestId);
                        IdValidDate = "";
                    }
                });


    }

    /**
     * ????????????
     */
    private void selectTime(TextView textView, int type) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //????????????
                if (type == 1) {
                    s = TimeUtils.getNewTimes(date);
                }
                //????????????
                else {
                    t = TimeUtils.getNewTimes(date);
                }
                textView.setText(TimeUtils.getNewTimes(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("???", "???", "???", "???", "???", "???")
                .build();
        pvTime.show();
    }
}
