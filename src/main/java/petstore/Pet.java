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
public class Pet {
    //3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/pet"; // endereço da entidade Pet

    //3.2 - Métodos e Funções
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    // Incluir - Create - Post
    @Test(priority = 1) // Identifica o metodo ou função como um teste para o TestNG
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        // Sintaxe Gherkin
        // Dado - Quando - Entao
        // Given - When - Then
        given()
                .contentType("application/json") //comum em API REST - antigo era "text/xml"
                .log().all()
                .body(jsonBody)
        .when() //Quando
                .post(uri)
        .then() //Entao
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("status", is ("available"))
                .body("category.name", is("AX2345LORT"))
        ;
    }

    @Test(priority = 2)
    public void consultarPet (){
        String petId = "3110199231";

        String token =
        given()
               .contentType("application/json")
               .log().all()

        .when()
                .get(uri + "/" + petId)

        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("category.name", is("AX2345LORT"))
                .body("status", is("available"))
        .extract()
                .path("category.name")


        ;
        System.out.print("O token é:" + token);

    }
    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("status", is("sold"))
        ;
    }


    @Test(priority = 4)
    public void uploadImagem() {
        String petId = "3110199231";
        String additionalMetadata = "Teste de upload de imagem";
        String filePath = "C:/Users/cris_/Downloads/eu.png";

        given()
                .multiPart("file", new File(filePath))
                .multiPart("additionalMetadata", additionalMetadata)
                .when()
                .post(uri + "/" + petId + "/uploadImage")
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
        ;
    }

    @Test(priority = 5)
    public void excluirPet(){
        String petId = "3110199231";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }

}
