package com.example.kuailefupos.mefragment.setup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.kuailefupos.R;
import com.example.kuailefupos.base.BaseActivity;
import com.example.kuailefupos.bean.IdCardInfo;
import com.example.kuailefupos.cos.CosServiceFactory;
import com.example.kuailefupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.SmallinformationBean;
import com.example.kuailefupos.net.HttpRequest;
import com.example.kuailefupos.net.OkHttpException;
import com.example.kuailefupos.net.RequestParams;
import com.example.kuailefupos.net.ResponseCallback;
import com.example.kuailefupos.utils.ImageConvertUtil;
import com.example.kuailefupos.utils.TimeUtils;
import com.example.kuailefupos.utils.Utility;
import com.example.kuailefupos.views.MyDialog1;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 作者: qgl
 * 创建日期：2022/3/31
 * 描述:签约界面
 */
public class MeSigningActivity extends BaseActivity implements View.OnClickListener {
    private Button signing_btn;
    private LinearLayout iv_back;
    //0 未签约 1签约失败 2 审核中 3 签约成功
    private String Signing = "0";
    private String ret_msg = "";
    private TextView prompt_tv;
    /*******************后加的**********************/
    private SimpleDraweeView id_card_is;
    private SimpleDraweeView id_card_the;
    private EditText id_number_ed;
    private EditText bank_number_ed;
    private EditText phone_number_ed;
    private LinearLayout merchant_detail_line1;
    final private int IdCardIn = 10010;
    final private int IdCardOn = 10011;
    private View popView;
    private PopupWindow popWindow;
    //身份证正面照片地址
    private String IdCard1_Url = "";
    //标识
    private String IdUrl1isActive = "1";
    //身份证反面照片地址
    private String IdCard2_Url = "";
    //标识
    private String IdUrl2isActive = "1";
    private String region = "ap-beijing"; // 存储桶地区
    private String folderName = "";
    private CosXmlService cosXmlService;
    private TransferManager transferManager;
    private COSXMLUploadTask cosxmlTask;
    private String IdUrl1 = "";
    private String IdUrl2="";

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.mesigning_layout;
    }

    @Override
    protected void initView() {
        // 初始化腾讯存储桶
        cosXmlService = CosServiceFactory.getCosXmlService(this, region, getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);

        signing_btn = findViewById(R.id.signing_btn);
        iv_back = findViewById(R.id.iv_back);
        prompt_tv = findViewById(R.id.prompt_tv);

        id_card_is = findViewById(R.id.id_card_is);
        id_card_the = findViewById(R.id.id_card_the);
        id_number_ed = findViewById(R.id.id_number_ed);
        bank_number_ed = findViewById(R.id.bank_number_ed);
        phone_number_ed = findViewById(R.id.phone_number_ed);
        merchant_detail_line1 = findViewById(R.id.merchant_detail_line1);


        Signing = getIntent().getStringExtra("Signing");
        ret_msg = getIntent().getStringExtra("ret_msg");
        if (Signing.equals("0")) {
            merchant_detail_line1.setVisibility(View.VISIBLE);
            signing_btn.setVisibility(View.VISIBLE);
            prompt_tv.setVisibility(View.INVISIBLE);
        } else if (Signing.equals("1")) {
            merchant_detail_line1.setVisibility(View.VISIBLE);
            signing_btn.setVisibility(View.VISIBLE);
            prompt_tv.setVisibility(View.VISIBLE);
            prompt_tv.setText(ret_msg);
        } else if (Signing.equals("2")) {
            merchant_detail_line1.setVisibility(View.GONE);
            signing_btn.setVisibility(View.GONE);
            prompt_tv.setVisibility(View.VISIBLE);
            prompt_tv.setText(ret_msg);
        }
    }

    @Override
    protected void initListener() {
        signing_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        id_card_is.setOnClickListener(this);
        id_card_the.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signing_btn:
                if (TextUtils.isEmpty(IdCard1_Url)) {
                    showToast(3, "银行卡正面照片");
                    return;
                }
                if (TextUtils.isEmpty(IdCard2_Url)) {
                    showToast(3, "银行卡反面照片");
                    return;
                }
                if (TextUtils.isEmpty(id_number_ed.getText().toString().trim())) {
                    showToast(3, "身份证号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(bank_number_ed.getText().toString().trim())) {
                    showToast(3, "银行卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(phone_number_ed.getText().toString().trim())) {
                    showToast(3, "手机不能为空");
                    return;
                }

                showDialog("您是否签约？");
                break;
            case R.id.iv_back:
                finish();
                break;
            // 获取身份证正面
            case R.id.id_card_is:
                showEditPhotoWindow(IdCardIn);
                break;
            // 获取身份证反面
            case R.id.id_card_the:
                showEditPhotoWindow(IdCardOn);
                break;
        }
    }

    private void posSigningData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getSigningTo(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject object = new JSONObject(responseObj.toString());
                    showToast(2, object.getString("msg"));
                    finish();
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


    //设置支付密码提示Dialog
    private void showDialog(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(value);
        Dialog dialog = new MyDialog1(MeSigningActivity.this, true, true, (float) 0.7).setNewView(view);
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
                dialog.dismiss();
                loadDialog.show();
                folderName = "signing" + "/" + id_number_ed.getText().toString().trim() + "/" + TimeUtils.getNowTime("day");
                List<SmallinformationBean> beans = new ArrayList<>();
                if(IdUrl1isActive.equals("2")){
                    SmallinformationBean bean1 = new SmallinformationBean();
                    bean1.setName("1");
                    bean1.setUrl(IdCard1_Url);
                    bean1.setAcitvie(IdUrl1isActive);
                    beans.add(bean1);
                }
                if(IdUrl2isActive.equals("2")){
                    SmallinformationBean bean1 = new SmallinformationBean();
                    bean1.setName("2");
                    bean1.setUrl(IdCard2_Url);
                    bean1.setAcitvie(IdUrl2isActive);
                    beans.add(bean1);
                }
                if (beans.size() < 1) {
                    posData();
                }
                else {
                    Log.e("------》", "需要上传图片");
                    for (int i = 0; i < beans.size(); i++) {
                        upload(beans, folderName);
                    }
                }
            }
        });
    }


    /**
     * 弹出框
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
                        .create(MeSigningActivity.this, value)
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


    //身份证证明识别
    private void getIDCardIn() {
        initSdk(getSecretId(), getSecretKey());
        OcrSDKKit.getInstance().startProcessOcr(MeSigningActivity.this, OcrType.IDCardOCR_FRONT, null,
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
                        id_number_ed.setText(tempIdCardInfo.getIdNum());
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("requestId", requestId);
                        id_number_ed.setText("");
                    }
                });
    }

    //身份证反面
    private void getIDCardOn() {
        initSdk(getSecretId(), getSecretKey());
        //身份证反面
        OcrSDKKit.getInstance().startProcessOcr(MeSigningActivity.this, OcrType.IDCardOCR_BACK, null,
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
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("11111requestId", requestId);
                    }
                });


    }


    /**
     * 腾讯卡片识别初始化
     */
    private void initSdk(String secretId, String secretKey) {
        // 启动参数配置
        OcrType ocrType = OcrType.BankCardOCR; // 设置默认的业务识别，银行卡
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .OcrType(ocrType)
                .ModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // 初始化SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
    }


    /************************************** 选取照片开始 ***********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IdCardIn) {
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



    private void posData(){
        RequestParams params = new RequestParams();
        params.put("userId",getUserId());
        params.put("merchIdcard",id_number_ed.getText().toString().trim());
        params.put("merchBankMobile",phone_number_ed.getText().toString().trim());
        params.put("merchBankCardno",bank_number_ed.getText().toString().trim());
        params.put("merchIdcardPositive",IdUrl1);
        params.put("merchIdcardBack",IdUrl2);
        HttpRequest.postAddReceiverNew(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                if (loadDialog.isShowing()){
                    loadDialog.dismiss();
                }
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3,result.getString("msg"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(OkHttpException failuer) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    /**
     * 上传图片,对象存储
     */
    private void upload(List<SmallinformationBean> list, String newfolderName) {
        int i = 0;
        if (TextUtils.isEmpty(list.get(i).getUrl())||list.get(i).getAcitvie().equals("1")) {
            list.remove(i);
            return;
        }
        if (cosxmlTask == null) {
            File file = new File(list.get(i).getUrl());
            String cosPath;
            if (TextUtils.isEmpty(newfolderName)) {
                cosPath = file.getName();
            } else {
                cosPath = newfolderName + File.separator + file.getName();
            }
            cosxmlTask = transferManager.upload(getBucketName(), cosPath, list.get(i).getUrl(), null);
            Log.e("参数-------》", getBucketName() + "----" + cosPath + "---" + list.get(i).getUrl());

            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(final TransferState state) {
                    // refreshUploadState(state);
                }
            });
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long target) {
                    // refreshUploadProgress(complete, target);
                }
            });
            cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                    cosxmlTask = null;
                    Log.e("1111", "成功");
                    if (list.get(i).getName().equals("1")) {
                        IdUrl1 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("身份证正面", cOSXMLUploadTaskResult.accessUrl);
                    } else{
                        IdUrl2 = cOSXMLUploadTaskResult.accessUrl;
                        Log.e("身份证反面", cOSXMLUploadTaskResult.accessUrl);
                    }
                    setResult(RESULT_OK);
                    list.remove(i);
                    if (list.size() < 1) {
                        Log.e("----------》", "没有了去提交后台吧");
                        posData();
                    } else {
                        upload(list, newfolderName);
                    }
                }
                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                        Log.e("1111", "上传失败");
                        loadDialog.dismiss();
                        showToast(2,"图片上传失败！请重新选择图片");
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });

        }
    }
}