/// <reference types="cypress" />

describe("User Deletes Trip", () => {
    beforeEach(() => {
        cy.login();
    });

    it("SYS26 - Delete Trip", () => {
        cy.visit("/trips/1");
        cy.contains("button", /Delete Trip/i).click();

        cy.contains("h2", /Delete Trip/i).should("be.visible");

        cy.contains("button", /Cancel/i).should("be.visible");

        cy.get('.MuiDialog-paper').contains('button', /Delete/i).click();

        cy.get('.MuiDialog-container').should('not.exist');

        cy.location('pathname').should('eq', '/trips');

        cy.contains('Cypress Test Trip').should('not.exist');
    });
})