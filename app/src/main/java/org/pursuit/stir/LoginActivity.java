package org.pursuit.stir;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    private SignUpListener signUpListener;

    @BindView(R.id.login_email_editText)
    EditText emailEditText;
    @BindView(R.id.login_password_editText)
    EditText passwordEditText;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_sign_up_textview)
    TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        createRoundLogoImage();
        userLogin();
        userRegistration();
    }

    private void createRoundLogoImage() {
        ImageView imageView = findViewById(R.id.login_logo_imageView);
        Glide.with(getApplicationContext())
                .load(R.mipmap.stir_name_logo)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    private boolean validateUserInput() {
        boolean valid = true;
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        return valid;
    }

    private void signInExistingUser(String email, String password) {
        if (!validateUserInput()) {
            return;
        }
//        showProgressDialog();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.authorization_failed_message,
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                            //msg
                        }
//                        hideProgressDialog();
                    }
                });
    }

    private void userLogin() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInExistingUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                Intent intent = new Intent(LoginActivity.this, MainHostActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userRegistration() {
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpHostActivity.class);
                startActivity(intent);
            }
        });
    }

}
