package info.steven.wk01hw02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private TextView errorMessage;
    private EditText username, password;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorMessage = findViewById(R.id.error_message);
        Button loginButton = findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    errorMessage.setText("Code: " + response.code());
                    return;
                }

                users = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                errorMessage.setText(t.getMessage());
            }
        });

        loginButton.setOnClickListener(view -> {
            boolean isUsernameValid = false, isPasswordValid = false;
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            int userId = -1;

            for (User user : users) {
                if (user.getUsername().equals(usernameText)) {
                    isUsernameValid = true;

                    /* The user's email is also their password */
                    if (user.getEmail().equals(passwordText)) {
                        isPasswordValid = true;
                        userId = user.getId();
                    }

                    break;
                }
            }

            if (isUsernameValid && isPasswordValid) {
                Context context = getApplicationContext();
                startActivity(getIntent(context, usernameText, userId));
            } else if (!isUsernameValid) {
                errorMessage.setText("Username is invalid (Use usernames from API)");
                username.setBackgroundColor(0xFF0000);
            } else {
                errorMessage.setText("Password is invalid (Use user's email)");
                password.setBackgroundColor(0xFF0000);
            }
        });
    }

    public static Intent getIntent(Context c, String username, int userId) {
        Intent intent = new Intent(c, MainActivity.class);
        Bundle extraInfo = new Bundle();
        extraInfo.putString("username", username);
        extraInfo.putInt("userId", userId);
        intent.putExtras(extraInfo);

        return intent;
    }
}