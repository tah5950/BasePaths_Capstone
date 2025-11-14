import '@cypress/code-coverage/support';
import * as React from "react";

Cypress.Commands.add("registerTestUser", () => {
  cy.request("POST", "http://localhost:8080/api/user/register", {
    username: "cyTestUser",
    password: "cyTestPass1!"
  },
).then((resp) => {
    window.localStorage.setItem("token", resp.body.token);
  });
});

Cypress.Commands.add("login", () => {
  cy.request("POST", "http://localhost:8080/api/user/login", {
    username: "cyTestUser",
    password: "cyTestPass1!"
  }).then((resp) => {
    const token = resp.body.token;
    window.localStorage.setItem("token", token);
    Cypress.env("token", token);
    return token;   
  });
});

Cypress.Commands.add("loginWithUI", () => {
  cy.visit("/login");

  cy.get('input[name="username"]').type("cyTestUser");
  cy.get('input[name="password"]').type("cyTestPass1!");

  cy.intercept("POST", "http://localhost:8080/api/user/login").as("login");
  cy.contains("button", /Login/i).click();

  cy.wait("@login").its("response.statusCode").should("eq", 200);
});

Cypress.Commands.add("createTrip", () => {
  const newTrip = {
    tripId: 1,
    name: "Cypress Test Trip",
    startDate: new Date("2026-03-25"),
    endDate: new Date("2026-03-29"),
  };

  cy.login().then((token) => {

    cy.request({
      method: "POST",
      url: "http://localhost:8080/api/trip/create",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: newTrip,
       failOnStatusCode: false
    }).then((resp) => {
      expect(resp.status).to.eq(200);
    });
  });
});