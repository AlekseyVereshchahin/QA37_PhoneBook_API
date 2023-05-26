package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactRequestDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9wQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjg1NTI4NDQxLCJpYXQiOjE2ODQ5Mjg0NDF9.8kPl8LQgQtEobrhuCZiCkiGjntPIC5xo_Mw7MCUoAV4";
    String id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";


        int i = new Random().nextInt(1000)+1000;
        ContactRequestDTO contactRequestDTO=ContactRequestDTO.builder()
                .name("Donna"+i)
                .lastName("Donna11"+i)
                .email("donna"+i+"@gmail.com")
                .phone("865786879"+i)
                .address("TA")
                .build();
        String message=given()
                .body(contactRequestDTO)
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message");
        System.out.println(message);
        String[] all=message.split("ID: ");
        id=all[1];
        System.out.println(id);
    }

    @Test
    public void deleteContactById(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",equalTo("Contact was deleted!"));
    }

    @Test
    public void deleteContactByIdWrongToken() {
        given()
                .header("Authorization","uyfuy")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401);
    }
}
