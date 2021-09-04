package info.steven.wk01hw02;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static List<User> getUsers() {
        List<User> users;

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
                    return;
                }

                users = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                return;
            }
        });

        return users;
    }

    @Test
    public void validUsername() {
        List<User> users = getUsers();

        boolean isUsernameValid = false;
        String usernameText = "Bret";


        for (User user : users) {
            if (user.getUsername().equals(usernameText)) {
                isUsernameValid = true;
                break;
            }
        }

        assertEquals(true, isUsernameValid == true);
    }

    @Test
    public void invalidUsername() {
        List<User> users = getUsers();

        boolean isUsernameValid = false;
        String usernameText = "B";


        for (User user : users) {
            if (user.getUsername().equals(usernameText)) {
                isUsernameValid = true;
                break;
            }
        }

        assertEquals(false, isUsernameValid == false);
    }

    @Test
    public void validPassword() {
        List<User> users = getUsers();

        boolean isPasswordValid = false;
        String usernameText = "Bret";
        String passwordText = "Sincere@april.biz";

        for (User user : users) {
            if (user.getUsername().equals(usernameText)) {
                /* The user's email is also their password */
                if (user.getEmail().equals(passwordText)) {
                    isPasswordValid = true;
                }

                break;
            }
        }

        assertEquals(true, isPasswordValid == true);
    }

    @Test
    public void invalidPassword() {
        List<User> users = getUsers();

        boolean isPasswordValid = false;
        String usernameText = "foo";
        String passwordText = "bar";

        for (User user : users) {
            if (user.getUsername().equals(usernameText)) {
                /* The user's email is also their password */
                if (user.getEmail().equals(passwordText)) {
                    isPasswordValid = true;
                }

                break;
            }
        }

        assertEquals(false, isPasswordValid == false);
    }
}