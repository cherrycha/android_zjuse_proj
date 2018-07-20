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

public class Register extends AppCompatActivity implements  View.OnClickListener {

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
