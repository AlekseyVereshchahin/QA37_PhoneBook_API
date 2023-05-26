package okhttp;

import com.google.gson.Gson;
import dto.*;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class CreateContactsOkhttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9wQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjg1NTI4NDQxLCJpYXQiOjE2ODQ5Mjg0NDF9.8kPl8LQgQtEobrhuCZiCkiGjntPIC5xo_Mw7MCUoAV4";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String id;






    @Test
    public void createContact() throws IOException {

        Random random = new Random();
        int i = random.nextInt(1000);

        ContactRequestDTO contact = ContactRequestDTO.builder()
                .name("Alex"+i)
                .lastName("Lord"+i)
                .email("ghh"+i+"@mail.com")
                .phone("123456789"+i)
                .address("NY")
                .description("friend")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        ContactResponseDTO message = gson.fromJson(response.body().string(), ContactResponseDTO.class);
        System.out.println(response.code());
        System.out.println(message);

        Assert.assertEquals(response.code(), 200);
        System.out.println(response.code());
        Assert.assertTrue(response.isSuccessful());
    }
}
