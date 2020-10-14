package com.deyi.hfybase.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.deyi.hfybase.R;
import com.deyi.hfybase.base.BaseActiivty;
import com.deyi.hfybase.baseModule.util.MTitle;
import com.deyi.hfybase.util.NfcUtil;
import com.deyi.hfybase.util.read.NdefMessageParser;
import com.deyi.hfybase.util.read.ParsedNdefRecord;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * @author hfy
 * @description
 * @time 2020/10/14
 */
public class TechActivity extends BaseActiivty {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.mTitle)
    MTitle mTitle;

    private PendingIntent mPendingIntent;
    private NfcAdapter mNfcAdapter;
    private String TAG="hfydemo";

    @Override
    public int bindLayout() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {
        mTitle.setCenterText("TECH", 18, getResources().getColor(R.color.black));
        resolveIntent(getIntent());
        //初始化nfc
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter == null) {
            Toast.makeText(TechActivity.this, "NFC不可用", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    @Override
    public void loadData() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: ");
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {
                //nfc功能打开了
                //隐式启动
                mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            } else {
                IsToSet(this);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w(TAG,  "onNewIntent: ");
        setIntent(intent);
        if (mNfcAdapter != null) { //有nfc功能
            if (mNfcAdapter.isEnabled()) {//nfc功能打开了
                resolveIntent(getIntent());
            } else {
                IsToSet(this);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
    //初次判断是什么类型的NFC卡
    private void resolveIntent(Intent intent) {
        NdefMessage[] msgs = NfcUtil.getNdefMsg(intent); //重点功能，解析nfc标签中的数据

        if (msgs == null) {
            Toast.makeText(TechActivity.this, "非NFC启动", Toast.LENGTH_SHORT).show();
        } else {
            setNFCMsgView(msgs);
        }

    }

    /**
     * 显示扫描后的信息
     *
     * @param ndefMessages ndef数据
     */
    @SuppressLint("SetTextI18n")
    private void setNFCMsgView(NdefMessage[] ndefMessages) {
        if (ndefMessages == null || ndefMessages.length == 0) {
            return;
        }

//        tvNFCMessage.setText("Payload:" + new String(ndefMessages[0].getRecords()[0].getPayload()) + "\n");

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        text.append(hour + ":" + minute + "\n");
        List<ParsedNdefRecord> records = NdefMessageParser.parse(ndefMessages[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            text.append(record.getViewText() + "\n");
        }
    }

    /**
     * 打开nfc
     * @param activity
     */
    private static void IsToSet(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("是否跳转到设置页面打开NFC功能");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToSet(activity);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private static void goToSet(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
            return;
        }
    }

}
