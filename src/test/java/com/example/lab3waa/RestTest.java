package com.example.lab3waa;
import com.example.lab3waa.Book;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class RestTest {

    @BeforeClass
    public static void setup() {
        RestAssured.port = Integer.valueOf(8080);
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "";
    }

    @Test
    public void testAddBook() {
        Book book = new Book("222", "sebitas", "deport", 12.2);

        given()
                .contentType("application/json")
                .body(book)
                .when()
                .post("/book")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/book/222")
                .then()
                .statusCode(200)
                .and()
                .body("isbn", equalTo("222"))
                .body("author", equalTo("sebitas"))
                .body("title", equalTo("deport"))
                .body("price", equalTo(12.2f));

        given().when().delete("book/222");
    }

    @Test
    public void testGetBook() {
        Book account = new Book("2222", "sebastian", "one", 123d);

        given()
                .contentType("application/json")
                .body(account)
                .when().post("/book").then()
                .statusCode(200);
        // test getting the contact
        given()
                .when()
                .get("/book/2222")
                .then()
                .contentType(ContentType.JSON)
                .and()
                .body("isbn", equalTo("2222"))
                .body("author", equalTo("sebastian"))
                .body("title", equalTo("one"))
                .body("price", equalTo(123f));
        //cleanup
        given()
                .when()
                .delete("/book/2222");
    }

    @Test
    public void testUpdateBook() {
// add the contact
        Book book = new Book("333", "sebastian", "one", 123d);
        Book bookUpdate = new Book("333", "sebastiangil", "two", 128d);

        given()
                .contentType("application/json")
                .body(book)
                .when().post("/book").then()
                .statusCode(200);
//update contact
        given()
                .contentType("application/json")
                .body(bookUpdate)
                .when().put("/book/" + bookUpdate.getIsbn()).then()
                .statusCode(200);
// get the contact and verify
        given()
                .when()
                .get("book/333")
                .then()
                .statusCode(200)
                .and()
                .body("isbn", equalTo("333"))
                .body("author", equalTo("sebastiangil"))
                .body("title", equalTo("two"))
                .body("price", equalTo(128f));
//cleanup
        given()
                .when()
                .delete("book/333");
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book("333", "sebastian", "one", 123d);
        given()
                .contentType("application/json")
                .body(book)
                .when().post("/book").then()
                .statusCode(200);

        given()
                .when()
                .delete("book/333");

        given()
                .when()
                .get("book/333")
                .then()
                .statusCode(404)
                .and()
                .body("msg", equalTo("Book # 333 is not available"));
    }


    @Test
    public void testSearchBooks() {
        Book book1 = new Book("555", "jon", "three", 3.0);
        Book book2 = new Book("444", "doe", "three", 3.0);

        given()
                .contentType("application/json")
                .body(book1)
                .when().post("/book").then()
                .statusCode(200);

        given()
                .contentType("application/json")
                .body(book2)
                .when().post("/book").then()
                .statusCode(200);
        // test getting the contact
        given()
                .when()
                .get("book?author='jon'")
                .then()
                .contentType(ContentType.JSON)
                .and()
                .body("books.isbn", hasItems("555"))
                .body("books.author", hasItems("jon"))
                .body("books.title", hasItems("three"))
                .body("books.price", hasItems(3f));
        //cleanup
        given()
                .when()
                .delete("/book/555");

        given()
                .when()
                .delete("/book/444");
    }

}
