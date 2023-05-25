package okhttp;

import com.google.gson.Gson;
import dto.ContactRequestDTO;
import dto.ErrorDTO;
import dto.GetAllContactsDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsTestsOkhttp {
    String token ="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9wQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjg1NTI4NDQxLCJpYXQiOjE2ODQ5Mjg0NDF9.8kPl8LQgQtEobrhuCZiCkiGjntPIC5xo_Mw7MCUoAV4";
    Gson gson=new Gson();
    OkHttpClient client=new OkHttpClient();

    @Test
    public void getAllContactSuccess() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        GetAllContactsDTO contactsDTO=gson.fromJson(response.body().string(), GetAllContactsDTO.class);
        List<ContactRequestDTO> contacts =contactsDTO.getContacts();
        for (ContactRequestDTO c :contacts) {
            System.out.println(c.getId());
            System.out.println(c.getEmail());
        }


    }

    @Test
    public void getAllContactWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization","ftgvjhb")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(),ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(),"Unauthorized");
    }

}

