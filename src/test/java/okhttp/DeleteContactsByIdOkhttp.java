package okhttp;

import com.google.gson.Gson;
import dto.*;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DeleteContactsByIdOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9wQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjg1NTI4NDQxLCJpYXQiOjE2ODQ5Mjg0NDF9.8kPl8LQgQtEobrhuCZiCkiGjntPIC5xo_Mw7MCUoAV4";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    String id;

    @BeforeMethod
    public void preCondition() throws IOException {

        ContactRequestDTO contact = ContactRequestDTO.builder()
                .name("Alex")
                .lastName("Lord")
                .email("ghh@gmail.com")
                .phone("123456789123")
                .address("NY")
                .description("fr")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        Assert.assertTrue(response.isSuccessful());

        ContactResponseDTO dto = gson.fromJson(response.body().string(), ContactResponseDTO.class);
        System.out.println(dto.getMessage());

        String [] all=dto.getMessage().split(" ID: ");
        id=all[1];
        System.out.println(id);
    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        DeleteByIdResponseDTO dto = gson.fromJson(response.body().string(), DeleteByIdResponseDTO.class);
        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", "hfdhfth")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");

    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/jutfuygiuhuahjaofijoi")
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: jutfuygiuhuahjaofijoi not found in your contacts!");

    }

}
