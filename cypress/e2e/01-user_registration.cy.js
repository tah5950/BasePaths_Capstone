/// <reference types="cypress" />

describe("User Registration Systems Tests", () => {
    it("SYS1 - Create User Success", () => {
        cy.visit("/createaccount");

        cy.get('input[name="username"]').type("cyValidUser");
        cy.get('input[name="password"]').type("cyValidPass1!");

        cy.intercept("POST", "http://localhost:8080/api/user/register").as("register");
        cy.contains("button", /Create/i).click();

        cy.wait("@register").its("response.statusCode").should("eq", 200);
        cy.url().should("include", "/home");
    });

    it("SYS2 - Register Duplicate User", () => {
        cy.registerTestUser();
        cy.visit("/createaccount");

        cy.get('input[name="username"]').type("cyTestUser"); // Will be seeded in database
        cy.get('input[name="password"]').type("cyTestPass1!");

        cy.intercept("POST", "http://localhost:8080/api/user/register").as("register");
        cy.contains("button", /Create/i).click();

        cy.wait("@register").its("response.statusCode").should("eq", 401);
        cy.contains(/Username already exists/i).should("exist");
    });

    it("SYS3 - Register User with invalid password", () => {
        cy.visit("/createaccount");

        cy.get('input[name="username"]').type("cyTestUser1");
        cy.get('input[name="password"]').type("cyTestPass");

        cy.intercept("POST", "http://localhost:8080/api/user/register").as("register");
        cy.contains("button", /Create/i).click();

        cy.wait("@register").its("response.statusCode").should("eq", 401);
        cy.contains(/Error: Password must be 8-32 characters with at least one lowercase letter, one uppercase letter, one number, and one symbol/i).should("exist");
    });
})