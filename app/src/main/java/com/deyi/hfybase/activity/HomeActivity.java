package com.deyi.hfybase.activity;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.deyi.hfybase.R;
import com.deyi.hfybase.base.BaseActiivty;
import com.deyi.hfybase.util.NfcUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hfy
 * @description
 * @time 2020/10/12
 */
public class HomeActivity extends BaseActiivty {
    @BindView(R.id.bt_nfc)
    Button btNfc;

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        btNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("hfydemo",  "--------------NFC是否开启-------------，" +NfcUtils.NfcCheck(HomeActivity.this));
            }
        });
    }
    //在onResume中开启前台调度
    @Override
    protected void onResume() {
        super.onResume();
        //设定intentfilter和tech-list。如果两个都为null就代表优先接收任何形式的TAG action。也就是说系统会主动发TAG intent。
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
        }else {
            Log.w("hfydemo",  "--------------NfcUtils.mNfcAdapter = null" );
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w("hfydemo",  "--------------NFC-------------" );
        processIntent(intent);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NfcUtils.mNfcAdapter = null;
    }

    //  这块的processIntent() 就是处理卡中数据的方法
    public void processIntent(Intent intent) {
        Parcelable[] rawmsgs = intent.getParcelableArrayExtra(NfcAdapter.ACTION_TECH_DISCOVERED);
        NdefMessage msg = (NdefMessage) rawmsgs[0];
        NdefRecord[] records = msg.getRecords();
        String resultStr = new String(records[0].getPayload());
        // 返回的是NFC检查到卡中的数据
        Log.w("hfydemo", "processIntent: "+resultStr );
        try {
            // 检测卡的id
            String id = NfcUtils.readNFCId(intent);
            Log.w("hfydemo", "processIntent--id: "+id );
            // NfcUtils中获取卡中数据的方法
            String result = NfcUtils.readNFCFromTag(intent);
            Log.w("hfydemo","processIntent--result: "+result );
            // 往卡中写数据
//            ToastUtils.showLong(getActivity(),result);
//            String data = "this.is.write";
//            NfcUtils.writeNFCToTag(data,intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
