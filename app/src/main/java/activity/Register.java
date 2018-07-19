package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.cherrycha.material_design.R;

public class Register extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    EditText username,password,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username_etx);
        password = (EditText) findViewById(R.id.password_etx);
        phone = (EditText) findViewById(R.id.phone_etx);
        email = (EditText) findViewById(R.id.email_etx);
        findViewById(R.id.register_button).setOnClickListener(this);


//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        drawerFragment = (FragmentDrawer)
//                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
//        drawerFragment.setDrawerListener(this);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new ShoppingCartFragment();
                title = getString(R.string.title_shopping_cart);
                break;
            case 2:
                fragment = new AddressesFragment();
                title = getString(R.string.title_addresses);
                break;
            case 3:
                fragment = new OrderListFragment();
                title = getString(R.string.title_order_list);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button://读写MifareClassic格式
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
}
