/// <reference types="cypress" />

describe("User Views Trips", () => {
    beforeEach(() => {
        cy.login();
    });

    it("SYS18 - Trips Page Loads", () => {
        cy.visit("/home");
        cy.contains("button", /Trips/i).click();

        cy.contains("h4", /Trips/i).should("be.visible");

        cy.contains("button", /Create Trip/i).should("be.visible");

        cy.get('table').should('exist');

        cy.intercept("GET", "http://localhost:8080/api/trip/getCurrentUserTrips").as("getTrips");
    });

    it("SYS22 - Open Trip Details page", () => {
        cy.visit("/trips");
        cy.contains("button", /Trips/i).click();

        cy.get('table').should('exist');

        cy.get('table tbody tr').first().as('firstRow');

        cy.get('@firstRow').within(() => {
            cy.get('td').eq(0).invoke('text').then(t => t.trim()).as('tripName'); 
            cy.get('td').eq(1).invoke('text').then(t => t.trim()).as('startDate');
            cy.get('td').eq(2).invoke('text').then(t => t.trim()).as('endDate');    
        });

        cy.intercept("GET", "http://localhost:8080/api/trip/getCurrentUserTrips").as("getTrips");

        cy.get('table tbody tr').first().dblclick();

        cy.url().should('include', '/trips/1');

        cy.get('@tripName').then((name) => {
            cy.contains(name).should('be.visible');
        });

        cy.get('@startDate').then((start) => {
            cy.contains(start).should('be.visible');
        });

        cy.get('@endDate').then((end) => {
            cy.contains(end).should('be.visible');
        });

        // Optionally verify other fields
        cy.contains(/Generated:/).should('be.visible');
        cy.contains(/Max Hours Per Day:/).should('be.visible');
        cy.contains(/Start Coordinate:/).should('be.visible');
        cy.contains(/End Coordinate:/).should('be.visible');

    });
})