package restassured;


import com.jayway.restassured.RestAssured;
import dto.AuthRequestDTO;
import dto.ContactRequestDTO;
import dto.ContactResponseDTO;
import dto.GetAllContactsDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String token ="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9wQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjg1NTI4NDQxLCJpYXQiOjE2ODQ5Mjg0NDF9.8kPl8LQgQtEobrhuCZiCkiGjntPIC5xo_Mw7MCUoAV4";

    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void getAllContactsSuccess(){
        GetAllContactsDTO contactsDTO=given()
                .header("Authorization",token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(GetAllContactsDTO.class);
        List<ContactRequestDTO> list =contactsDTO.getContacts();
        for (ContactRequestDTO contact:list){
            System.out.println(contact.getId());
            System.out.println(contact.getLastName());
            System.out.println("Size of list "+list.size());
        }
    }

    @Test
    public void getAllContactsUnauthorized() { //дописать
        GetAllContactsDTO contactsDTO = given()
                .header("Authorization", "utuygy")
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(GetAllContactsDTO.class);
    }
}
