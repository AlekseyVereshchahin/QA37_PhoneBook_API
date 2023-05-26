package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTestsRA {
    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }

    @Test
    public void registrationSuccess(){
        int i=(int)(System.currentTimeMillis()/1000)%3600;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("pop"+i+"@gmail.com")
                .password("Ppop12345$")
                .build();
        String token=given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");
        System.out.println(token);
    }

    @Test
    public void registrationWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("popgmail.com")
                .password("Ppop12345$")
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.username",containsString("must be a well-formed email address"));
    }

    @Test
    public void registrationWrongPassword(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("pop@gmail.com")
                .password("Ppop12345")
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.password",containsString("At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]"));
    }

    @Test
    public void registrationDuplicateUser() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("pop@gmail.com")
                .password("Ppop12345$")
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message", containsString("User already exists"));

    }
}
