/// <reference types="cypress" />

describe("User Login/Logout Tests", () => {
    it("SYS4 - Login User Success", () => {
        cy.visit("/login");

        cy.get('input[name="username"]').type("cyTestUser");
        cy.get('input[name="password"]').type("cyTestPass1!");

        cy.intercept("POST", "http://localhost:8080/api/user/login").as("login");
        cy.contains("button", /Login/i).click();

        cy.wait("@login").its("response.statusCode").should("eq", 200);
        cy.url().should("include", "/home");
    });

    it("SYS5 - Login with Invalid Credentials", () => {
        cy.visit("/login");

        cy.get('input[name="username"]').type("cyTestUser2");
        cy.get('input[name="password"]').type("cyTestPass2!");

        cy.intercept("POST", "http://localhost:8080/api/user/login").as("login");
        cy.contains("button", /Login/i).click();

        cy.wait("@login").its("response.statusCode").should("eq", 401);
        cy.contains(/Error: Invalid Username or Password/i).should("exist");
    });

    it("SYS6 - Logout", () => {
        cy.login();

        cy.visit("/home");

        cy.contains("button", /Logout/i).click();

        cy.window().its("localStorage.token").should("not.exist");
        cy.url().should("include", "/login");
    });
})