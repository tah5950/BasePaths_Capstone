/// <reference types="cypress" />

describe("User Views Trips", () => {
    beforeEach(() => {
        cy.login();
    });

    it("SYS23 - Open Generate Trip Form", () => {
        cy.visit("/trips/1");
        cy.contains("button", /Generate Trip/i).click();

        cy.contains("h2", /Generate Trip/i).should("be.visible");

        cy.contains("button", /Generate/i).should("be.visible");
    });

    it("SYS24 - Generate invalid Trip", () => {
        const genTripInvalid = {
            startLat: "100",
            startLon: "0",
            endLat: "0",
            endLon: "0",
            maxTravelHours: "7"
        };
        cy.visit("/trips/1");
        cy.contains("button", /Generate Trip/i).click();

        cy.contains("h2", /Generate Trip/i).should("be.visible");

        cy.get('input[name="startLat"]').type(genTripInvalid.startLat);
        cy.get('input[name="startLon"]').type(genTripInvalid.startLon);
        cy.get('input[name="endLat"]').type(genTripInvalid.endLat);
        cy.get('input[name="endLon"]').type(genTripInvalid.endLon);
        cy.get('input[name="maxHours"]').type(genTripInvalid.maxTravelHours);

        cy.intercept("PUT", "http://localhost:8080/api/trip/generateTrip").as("invalidGenerate");

        cy.get(".MuiDialog-container").within(() => {
            cy.contains("button", /Generate/i).should("be.visible").click();
        });

        cy.wait("@invalidGenerate").its("response.statusCode").should("eq", 400);
        cy.contains(/Error: Invalid Start Latitude/i).should("exist");

        cy.contains("h2", /Generate Trip/i).should("be.visible");
    });

    it("SYS25 - Generate Valid Trip", () => {
        const genTripValid = {
            startLat: "32",
            startLon: "-117",
            endLat: "38",
            endLon: "-121",
            maxTravelHours: "7"
        };
        cy.visit("/trips/1");
        cy.contains("button", /Generate Trip/i).click();

        cy.contains("h2", /Generate Trip/i).should("be.visible");

        cy.get('input[name="startLat"]').type(genTripValid.startLat);
        cy.get('input[name="startLon"]').type(genTripValid.startLon);
        cy.get('input[name="endLat"]').type(genTripValid.endLat);
        cy.get('input[name="endLon"]').type(genTripValid.endLon);
        cy.get('input[name="maxHours"]').type(genTripValid.maxTravelHours);

        cy.intercept("PUT", "http://localhost:8080/api/trip/generateTrip").as("genTripValid");

        cy.get(".MuiDialog-container").within(() => {
            cy.contains("button", /Generate/i).should("be.visible").click();
        });

        cy.wait("@genTripValid").its("response.statusCode").should("eq", 200);

        cy.wait(500);
        cy.contains(genTripValid.startLat).should("be.visible");
        cy.get("table").should("contain.text", "Start");
        cy.get("table").should("contain.text", "San Diego");
        cy.get("table").should("contain.text", "Los Angeles");
        cy.get("table").should("contain.text", "San Francisco");
        cy.get("table").should("contain.text", "End");
    });
})