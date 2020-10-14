package com.deyi.hfybase.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.deyi.hfybase.R;
import com.deyi.hfybase.baseModule.util.MTitle;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author hfy
 * @description
 * @time 2020/10/14
 */
public class MifareClassicActivity extends Activity {

    NfcAdapter nfcAdapter;
    @BindView(R.id.mTitle)
    MTitle mTitle;
    @BindView(R.id.text)
    TextView text;
    private PendingIntent pendingIntent=null;

    protected final static byte TRANS_CSU = 6; // 如果等于0x06或者0x09，表示刷卡；否则是充值
    protected final static byte TRANS_CSU_CPX = 9; // 如果等于0x06或者0x09，表示刷卡；否则是充值
    public String[][] tenchlists;
    public IntentFilter[] filters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        mTitle.setCenterText("MifareClassic", 18, getResources().getColor(R.color.black));
        initView();
    }


    public void initView() {

        tenchlists = new String[][] { { IsoDep.class.getName() }, { NfcV.class.getName() }, { NfcF.class.getName() }, };
        try {
            filters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
        } catch (IntentFilter.MalformedMimeTypeException e1) {
            e1.printStackTrace();
        }
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 获取默认的NFC控制器，并进行判断
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Log.d("h_bl", "设备不支持NFC！");
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            Log.d("h_bl", "请在系统设置中先启用NFC功能！");
            return;
        }
        Intent intent = this.getIntent(); // 捕获NFC Intent
        praseIntent(intent);

    }

    private void praseIntent(Intent intent) {

        String nfcAction = intent.getAction(); // 解析该Intent的Action
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(nfcAction)) {
            Log.d("h_bl", "ACTION_TECH_DISCOVERED");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // 获取Tag标签，既可以处理相关信息
            for (String tech : tag.getTechList()) {
                Log.d("h_bl", "tech=" + tech);
            }
            IsoDep isodep = IsoDep.get(tag);
            String str = "";
            try {
                isodep.connect();
                //select the card manager applet
                byte[] mf = { (byte) '1', (byte) 'P',
                        (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
                        (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
                        (byte) '0', (byte) '1', };
                String result="";
                byte[] mfRsp = isodep.transceive(getSelectCommand(mf));
                String s=new String(mfRsp);
                Log.d("hfydemo", "mfRsp:" + s);
                //select Main Application
                byte[] wht = { (byte) 0x41, (byte) 0x50,
                        //此处以武汉通为例，其它的卡片参考对应的命令，网上可以查到
                        (byte) 0x31, (byte) 0x2E, (byte) 0x57, (byte) 0x48, (byte) 0x43,
                        (byte) 0x54, (byte) 0x43, };
                byte[] sztRsp = isodep.transceive(getSelectCommand(wht));
                byte[] balance = { (byte) 0x80, (byte) 0x5C, 0x00, 0x02, 0x04};
                byte[] balanceRsp = isodep.transceive(balance);
                String s2=new String(balanceRsp);
                Log.w("hfydemo", "balanceRsp:" + s2);
                if(balanceRsp!=null && balanceRsp.length>4)
                {
                    int cash = byteToInt(balanceRsp, 4);
                    float ba = cash / 100.0f;
                    result+="  余额："+String.valueOf(ba);

                }
                text.setText("数据：\n"+result);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                    try {
                        isodep.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }




    @Override
    protected void onPause() {
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, tenchlists);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 当前app正在前端界面运行，这个时候有intent发送过来，那么系统就会调用onNewIntent回调方法，将intent传送过来
        // 我们只需要在这里检验这个intent是否是NFC相关的intent，如果是，就调用处理方法
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Log.d("h_bl", "onNewIntent");
            praseIntent(intent);
        }

    }

    public static byte byteToHex(byte arg) {
        byte hex = 0;
        if (arg >= 48 && arg <= 57) {
            hex = (byte) (arg - 48);
        } else if (arg >= 65 && arg <= 70) {
            hex = (byte) (arg - 55);
        } else if (arg >= 97 && arg <= 102) {
            hex = (byte) (arg - 87);
        }
        return hex;
    }

    private byte[] getSelectCommand(byte[] aid) {
        final ByteBuffer cmd_pse = ByteBuffer.allocate(aid.length + 6);
        cmd_pse.put((byte) 0x00) // CLA Class
                .put((byte) 0xA4) // INS Instruction
                .put((byte) 0x04) // P1 Parameter 1
                .put((byte) 0x00) // P2 Parameter 2
                .put((byte) aid.length) // Lc
                .put(aid).put((byte) 0x00); // Le
        return cmd_pse.array();
    }

    /**
     * 整条Records解析成ArrayList<byte[]>
     *
     * @param Records
     * @return
     */
    private ArrayList<byte[]> parseRecords(byte[] Records) {
        int max = Records.length / 23;
        Log.d("h_bl", "消费记录有" + max + "条");
        ArrayList<byte[]> ret = new ArrayList<byte[]>();
        for (int i = 0; i < max; i++) {
            byte[] aRecord = new byte[23];
            for (int j = 23 * i, k = 0; j < 23 * (i + 1); j++, k++) {
                aRecord[k] = Records[j];
            }
            ret.add(aRecord);
        }
        for (byte[] bs : ret) {
            Log.d("h_bl", "消费记录有byte[]" + bs); // 有数据。解析正确。
        }
        return ret;
    }

    /**
     * ArrayList<byte[]>记录分析List<String> 一条记录是23个字节byte[] data，对其解码如下
     * data[0]-data[1]:index data[2]-data[4]:over,金额溢出?？？ data[5]-data[8]:交易金额
     * ？？代码应该是（5，4） data[9]:如果等于0x06或者0x09，表示刷卡；否则是充值
     * data[10]-data[15]:刷卡机或充值机编号
     * data[16]-data[22]:日期String.format("%02X%02X.%02X.%02X %02X:%02X:%02X"
     * ,data[16], data[17], data[18], data[19], data[20], data[21], data[22]);
     *
     * @return
     */
    private List<String> parseRecordsToStrings(ArrayList<byte[]>... Records) {
        List<String> recordsList = new ArrayList<String>();
        for (ArrayList<byte[]> record : Records) {
            if (record == null)
                continue;
//            for (byte[] v : record) {
//                StringBuilder r = new StringBuilder();
//                int cash = Util.toInt(v, 5, 4);
//                char t = (v[9] == TRANS_CSU || v[9] == TRANS_CSU_CPX) ? '-' : '+';
//                r.append(String.format("%02X%02X.%02X.%02X %02X:%02X ", v[16], v[17], v[18], v[19], v[20], v[21], v[22]));
//                r.append("   " + t).append(Util.toAmountString(cash / 100.0f));
//                String aLog = r.toString();
//                recordsList.add(aLog);
//            }
        }
        return recordsList;
    }

    // byteArray转化为int
    private int byteToInt(byte[] b, int n) {
        int ret = 0;
        for (int i = 0; i < n; i++) {
            ret = ret << 8;
            ret |= b[i] & 0x00FF;
        }
        if (ret > 100000 || ret < -100000)
            ret -= 0x80000000;
        return ret;
    }
}
