package activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import java.io.IOException;

public class ModelActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private String token;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        name = intent.getStringExtra("name");
        bundle.putString("token",token);
        bundle.putString("name",name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new ShoppingFragment();
                title = getString(R.string.title_shopping);
                break;
            case 2:
//                fragment = new ShoppingCartFragment();
                title = getString(R.string.title_shopping_cart);
                fragment=new CardFragment();
                title=getString(R.string.title_card);
                break;
            case 3:
                fragment = new AddressesFragment();
                title = getString(R.string.title_addresses);
                break;
            case 4:
                fragment = new OrderListFragment();
                title = getString(R.string.title_order_list);
                break;
            default:
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    protected NfcAdapter mNfcAdapter = null;//nfc适配器对象
    protected PendingIntent mPendingIntent = null;//延迟Intent
    protected Tag mTag = null;//nfc标签对象
    private boolean haveMifareClissic=false;//标签是否支持MifareClassic
    private String result,TAG;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(mTag!=null){
            String[] techList=mTag.getTechList();
            System.out.println("支持的technology类型：");
            for (String tech:techList){
                System.out.println(tech);
                if (tech.indexOf("MifareClassic")>0){
                    haveMifareClissic=true;
                }
            }
        }


        result = readBlock();
        System.out.println(result);

        if(result!=null) {
            result = result.substring(result.length() - 1, result.length());
            int value = Integer.parseInt(result);
            //System.out.println(value);

            Fragment fragment = null;
            String title = getString(R.string.app_name);
            Bundle bundle = new Bundle();

            bundle.putString("token",token);

            bundle.putInt("ItemNum", value+1);

            fragment=new ItemInfoFragment();

            if (fragment != null) {
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter= NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter==null){//判断设备是否支持NFC功能
            Toast.makeText(this,"设备不支持NFC功能!",Toast.LENGTH_SHORT);
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()){//判断设备NFC功能是否打开
            Toast.makeText(this,"请到系统设置中打开NFC功能!",Toast.LENGTH_SHORT);
            finish();
            return;
        }
        mPendingIntent=PendingIntent.getActivity(this,0,new Intent(this,getClass()),0);//创建PendingIntent对象,当检测到一个Tag标签就会执行此Intent
    }

    //页面获取到焦点
    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter!=null){
            mNfcAdapter.enableForegroundDispatch(this,mPendingIntent,null,null);//打开前台发布系统，使页面优于其它nfc处理.当检测到一个Tag标签就会执行mPendingItent
        }
    }

    //页面失去焦点
    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!=null){
            mNfcAdapter.disableForegroundDispatch(this);//关闭前台发布系统
        }
    }

    //读取块
    public String readBlock(){
        if (mTag==null){
            Toast.makeText(this,"无法识别的标签！",Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
        if (!haveMifareClissic){
            Toast.makeText(this,"不支持MifareClassic",Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }

        MifareClassic mif = MifareClassic.get(mTag);

        int ttype = mif.getType();
        Log.d(TAG, "MifareClassic tag type: " + ttype);

        int tsize = mif.getSize();
        Log.d(TAG, "tag size: " + tsize);

        int s_len = mif.getSectorCount();
        Log.d(TAG, "tag sector count: " + s_len);

        int b_len = mif.getBlockCount();
        Log.d(TAG, "tag block count: " + b_len);
        try {
            mif.connect();
            if (mif.isConnected()){
                int i =6;
                boolean isAuthenticated = false;

                if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                    isAuthenticated = true;
                } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                    isAuthenticated = true;
                } else if (mif.authenticateSectorWithKeyA(i,MifareClassic.KEY_NFC_FORUM)) {
                    isAuthenticated = true;
                } else {
                    Log.d("TAG", "Authorization denied ");
                }

                if(isAuthenticated) {
                    int block_index = mif.sectorToBlock(i);

                    byte[] block = mif.readBlock(block_index);
                    String s_block = bytesToHexString(block);
                    Log.d(TAG, s_block);
                    return s_block;
                }
            }
            mif.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
}