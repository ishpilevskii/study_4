package inno.tech.study;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class TestTheme4Task1 {

    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost:8080")
            .setContentType(ContentType.JSON)
            .setAuth(basic("admin", "secret123"))
            .log(LogDetail.ALL)
            .build();

    private record bodyGoods(String name, Double price) {}

    private int addGood(String name, Double price) {


        return given()
                .spec(requestSpec)
                .body(new bodyGoods(name, price))
                .when()
                .post("/goods/add")
                .then().log().all().statusCode(200).extract().path("data.id");

    }

    private void deleteGood(int idGood) {
        given().spec(requestSpec)
                .delete("/goods/" + idGood)
                .then()
                .log().all().statusCode(200);
    }

    @Test
    void testGoodsList() {
        given()
                .baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods", empty())
        ;
    }

    @Test
    void testSpecGoodsList() {
        given()
                .spec(requestSpec)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods", empty());
    }

    @Nested
    class WhenGoodsAdded {
        private final String name = "coffee";
        private final Double price = 2.50;
        private int idGood;

        @BeforeEach
        void setUo() {
            idGood = addGood(name, price);
        }

        @AfterEach
        void tearDown() {
            deleteGood(idGood);
        }

        @Test
        void testAddGoodGetListGoods() {
            given()
                    .spec(requestSpec)
                    .when()
                    .get("/goods/list")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("goods[0].id", equalTo(idGood))
                    .body("goods[0].name", equalTo(name))
                    .body("goods[0].price", equalTo(price.floatValue()));
        }

        @Test
        void testAssertJAddGoodGetListGoods() {
            Response response = given().spec(requestSpec).when().get("/goods/list");
            Assertions.assertThat(response.statusCode()).as("Код ответа не 200").isEqualTo(200);
            Assertions.assertThat(response.jsonPath().getInt("goods[0].id")).as("id товара не соответствует").isEqualTo(idGood);
            Assertions.assertThat(response.jsonPath().getString("goods[0].name")).as("Name товара не соответствует").isEqualTo(name);
            Assertions.assertThat(response.jsonPath().getFloat("goods[0].price")).as("Цена товара не соответствует").isEqualTo(price.floatValue());

        }
    }

}
