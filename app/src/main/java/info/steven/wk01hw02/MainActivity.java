package info.steven.wk01hw02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult, header;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        header = findViewById(R.id.header);
        String username = "";
        int userId = -1;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            username = extras.getString("username");
            userId = extras.getInt("userId");
        }

        header.append("Username: " + username + "\nUserId: " + userId);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        int finalUserId = userId;
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                String content = "";

                for (Post post : posts) {
                    if (post.getUserId() == finalUserId) {
                        content = "";
                        content += "ID: " + post.getId() + '\n';
                        content += "User ID: " + post.getUserId() + '\n';
                        content += "Title: " + post.getTitle() + '\n';
                        content += "Text: " + post.getText() + "\n\n";
                    }

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }
}