// 1 - Pacote
package petstore;

// 2 - Bibliotecas

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

// 3 - Classe
public class Store {
    //3.1 - Atributos
    static String uri = "https://petstore.swagger.io/v2/store/order"; // endereço da entidade Pet
    static String ct = "application/json"; // content type
    //3.2 - Métodos e Funções
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    // Incluir - Create - Post
    @Test(priority = 1) // Identifica o metodo ou função como um teste para o TestNG
    public void comprarAnimal() throws IOException {
        String jsonBody = lerJson("db/storeOrder.json");

        // Sintaxe Gherkin
        // Dado - Quando - Entao
        // Given - When - Then
        given()
                .contentType(ct) //comum em API REST - antigo era "text/xml"
                .log().all()
                .body(jsonBody)
                .when() //Quando
                .post(uri)
                .then() //Entao
                .log().all()
                .statusCode(200)
                .body("id", is(5))
                .body("petId", is(3))
                .body("quantity", is (2))
                .body("status", is("placed"))
                .body("complete", is(true))
        ;
    }

    @Test(priority = 2)
    public void consultarCompra(){
        String orderId = "5";

                given()
                        .contentType("application/json")
                        .log().all()

                        .when()
                        .get(uri + "/" + orderId)

                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("id", is(5))
                        .body("petId", is(3))
                        .body("quantity", is (2))
                        .body("status", is("placed"))
                        .body("complete", is(true))
                ;
    }

    @Test(priority = 3)
    public void excluirCompra(){
        String orderId = "5";

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .delete(uri + "/" + orderId)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(orderId))
        ;
    }

}
