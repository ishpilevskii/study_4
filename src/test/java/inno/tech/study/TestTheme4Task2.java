package inno.tech.study;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@Tag("api")
public class TestTheme4Task2 {

    private static final String BASE_URI = "http://localhost:8080";

    public static final RequestSpecification authSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .setAuth(basic("admin", "secret123"))
            .log(LogDetail.ALL)
            .build();

    public static final RequestSpecification noAuthSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private record ProductRequest(String name, Double price) {
    }

    private Response addGood(String name, Double price) {
        return given()
                .spec(authSpec)
                .body(new ProductRequest(name, price))
                .when()
                .post("/goods/add");
    }

    private static Response deleteGood(int id) {
        return given()
                .baseUri(BASE_URI)
                .auth().basic("admin", "secret123")
                .when()
                .delete("/goods/" + id);
    }

    private Response listGoods() {
        return given()
                .spec(noAuthSpec)
                .queryParam("page", 0)
                .queryParam("size", 100)
                .when()
                .get("/goods/list");
    }

    static void clearAllGoods() {
        for (int attempt = 0; attempt < 5; attempt++) {
            Response response = given()
                    .spec(noAuthSpec)
                    .queryParam("page", 0)
                    .queryParam("size", 100)
                    .when()
                    .get("/goods/list");

            List<Map<String, Object>> goods = response.jsonPath().getList("goods");
            if (goods == null || goods.isEmpty()) {
                return;
            }

            for (Map<String, Object> good : goods) {
                Number id = (Number) good.get("id");
                deleteGood(id.intValue());
                }
            }
        }


    @BeforeAll
    static void clearBeforeAll() {
        clearAllGoods();
    }

    @AfterAll
    static void clearAfterAll() {
        clearAllGoods();
    }

    @Nested
    class GoodsListTests {

        @Test
        void listReturns200WhenEmpty() {
            given()
                    .spec(noAuthSpec)
                    .when()
                    .get("/goods/list")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("goods", empty());
        }

        @Test
        void listReturns200WithGoods() {
            Response resp = addGood("list-item", 10.0);
            int id = resp.jsonPath().getInt("data.id");

            try {
                given()
                        .spec(noAuthSpec)
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .when()
                        .get("/goods/list")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("goods[0].id", equalTo(id))
                        .body("goods[0].name", equalTo("list-item"))
                        .body("goods[0].price", equalTo(10.0f));
            } finally {
                deleteGood(id);
            }
        }
    }

    @Nested
    class AddGoodsTests {

        @Test
        void addReturns200() {
            Response newGood = addGood("coffee", 2.5);
            try {
                Assertions.assertThat(newGood.statusCode()).as("Код ответа не 200").isEqualTo(200);
                Assertions.assertThat(newGood.jsonPath().getString("message")).as("Получено не успешное сообщение").isEqualTo("success");
                Assertions.assertThat(newGood.jsonPath().getInt("data.id")).as("В поле data.id отсутствует значение").isNotNull();
                Assertions.assertThat(newGood.jsonPath().getInt("data.id")).as("В поле data.id отрицательное значение").isPositive();
            }
            finally  {




                int testGoodId = newGood.jsonPath().getInt("data.id");

                deleteGood(testGoodId);
            }
        }

        @Test
        void  addReturns400WhenNameEmpty() {
            Response newGood = addGood("", 2.5);
            Assertions.assertThat(newGood.statusCode()).as("Код ответа не 400").isEqualTo(400);
            Assertions.assertThat(newGood.jsonPath().getString("name")).as("Получено не ожидаемое сообщение").isEqualTo("Name cannot be empty");
        }

        @Test
        void addReturns400WhenPriceNegative() {
            Response newGood = addGood("negative", -1.0);
            Assertions.assertThat(newGood.statusCode()).as("Код ответа не 400").isEqualTo(400);
            Assertions.assertThat(newGood.jsonPath().getString("price")).as("Получено не ожидаемое сообщение").isEqualTo("Price can not be less, than 0");
        }

        @Test
        void addReturns400WhenNameDuplicated() {
            Response firstGood = addGood("unique-name", 1.0);
            Response secondGood = addGood("unique-name", 1.0);

            Assertions.assertThat(secondGood.statusCode()).as("Код ответа не 400").isEqualTo(400);
            Assertions.assertThat(secondGood.jsonPath().getString("message")).as("Получено не ожидаемое сообщение").isEqualTo("Good with name 'unique-name' already exists!");
            int id = firstGood.jsonPath().getInt("data.id");
            deleteGood(id);
        }
    }

    @Nested
    class GetGoodsByIdTests {

        private int id;
        private final String name = "by-id";
        private final Double price = 7.5;

        @BeforeEach
        void setUp() {
            Response idGood = addGood(name, price);
            id = idGood.jsonPath().getInt("data.id");
        }

        @AfterEach
        void tearDown() {
            deleteGood(id);
        }

        @Test
        void getByIdReturns200() {
            given()
                    .spec(authSpec)
                    .when()
                    .get("/goods/" + id)
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("id", equalTo(id))
                    .body("name", equalTo(name))
                    .body("price", equalTo(price.floatValue()));
        }

        @Test
        void getByIncorrectId() {
            int fakeId = id + 1;
            given()
                    .spec(authSpec)
                    .when()
                    .get("/goods/" + fakeId)
                    .then()
                    .log().all()
                    .statusCode(404)
                    .body("id", nullValue())
                    .body("message", equalTo("Good with id '" + fakeId + "' is not found!"));
        }
    }

    @Nested
    class PatchGoodsTests {

        @Test
        void patchReturns200() {
            Response resp = addGood("coffee", 3.0);
            int id = resp.jsonPath().getInt("data.id");
            try {
                given()
                        .spec(authSpec)
                        .body(new ProductRequest("patched", 9.9))
                        .when()
                        .patch("/goods/" + id)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("id", equalTo(id))
                        .body("name", equalTo("patched"))
                        .body("price", equalTo(9.9f));
            } finally {
                deleteGood(id);
            }
        }

        @Test
        void patchReturns400WhenNameEmpty() {
            Response resp = addGood("coffee", 3.0);
            int id = resp.jsonPath().getInt("data.id");
            try {
                given()
                        .spec(authSpec)
                        .body(new ProductRequest("", 1.0))
                        .when()
                        .patch("/goods/" + id)
                        .then()
                        .log().all()
                        .statusCode(400)
                        .body("name", equalTo("Name cannot be empty"));
            } finally {
                deleteGood(id);
            }
        }

        @Test
        void patchReturns400WhenNameDuplicated() {
            Response fakeResp = addGood("taken-name", 3.0);
            int fakeId = fakeResp.jsonPath().getInt("data.id");
            Response resp = addGood("coffee", 3.0);
            int id = resp.jsonPath().getInt("data.id");
            try {

                given()
                        .spec(authSpec)
                        .body(new ProductRequest("taken-name", 1.0))
                        .when()
                        .patch("/goods/" + id)
                        .then()
                        .log().all()
                        .statusCode(400);
            } finally {
                deleteGood(id);
                deleteGood(fakeId);
            }
        }

        @Test
        void patchReturns404WhenNotFound() {
            given()
                    .spec(authSpec)
                    .body(new ProductRequest("ghost", 1.0))
                    .when()
                    .patch("/goods/999999")
                    .then()
                    .log().all()
                    .statusCode(404);
        }
    }

    @Nested
    class DeleteGoodsTests {

        @Test
        void deleteReturns200() {
            Response resp = addGood("to-delete", 4.0);
            int id =  resp.jsonPath().getInt("data.id");

            Response respDeleteGoods = deleteGood(id);

            Assertions.assertThat(respDeleteGoods.statusCode()).as("Статус код не 200").isEqualTo(200);

            Response listGoods = listGoods();
            Assertions.assertThat(listGoods.statusCode()).as("Статус код не 200").isEqualTo(200);
            Assertions.assertThat(listGoods.jsonPath().getList("goods")).as("Список товаров не пустой").isEmpty();
        }

        @Test
        void deleteReturns404WhenNotFound() {
            given()
                    .spec(authSpec)
                    .when()
                    .delete("/goods/999999")
                    .then()
                    .log().all()
                    .statusCode(404);
        }
    }
}
