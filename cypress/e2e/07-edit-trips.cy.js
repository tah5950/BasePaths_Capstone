/// <reference types="cypress" />

describe("User Deletes Trip", () => {
    beforeEach(() => {
        cy.login();
    });

    it("SYS27 - Open Update Trip Form", () => {
        cy.visit("/trips/1");
        cy.contains("button", /Edit Trip/i).click();

        cy.contains("h2", /Edit Trip/i).should("be.visible");

        cy.contains("button", /Update/i).should("be.visible");
    });

    it("SYS28 - Update invalid Trip ", () => {
        const newTrip = {
            tripId: 1,
            name: "",
        };

        cy.visit("/trips/1");
        cy.contains("button", /Edit Trip/i).click();

        cy.contains("h2", /Edit Trip/i).should("be.visible");

        cy.intercept("PUT", "http://localhost:8080/api/trip/updateTrip").as("invalidUpdate");

        cy.get(".MuiDialog-container").within(() => {
            cy.get('input[name="tripName"]').clear();

            cy.contains("button", /Update/i).should("be.visible").click();
        });

        cy.wait("@invalidUpdate").its("response.statusCode").should("eq", 400);
        cy.contains(/Error: Name must not be blank/i).should("exist");

        cy.contains("h2", /Edit Trip/i).should("be.visible");
    });

    it("SYS29 - Update Valid Trip Name ", () => {
        const newTrip = {
            tripId: 1,
            name: "Cypress Test Trip Update",
            startDate: new Date("2026-03-25"),
            endDate: new Date("2026-03-29"),
        };

        cy.visit("/trips/1");
        cy.contains("button", /Edit Trip/i).click();

        cy.contains("h2", /Edit Trip/i).should("be.visible");
        
        cy.intercept("PUT", "http://localhost:8080/api/trip/updateTrip").as("update");

        cy.get(".MuiDialog-container").within(() => {
            cy.get('input[name="tripName"]').type(" Update");

            cy.contains("button", /Update/i).should("be.visible").click();
        });

        cy.wait("@update").its("response.statusCode").should("eq", 200);
        cy.contains(/Trip updated successfully/i).should("exist");

        cy.wait(500);
        cy.contains(newTrip.name).should("be.visible");
        cy.get("table").should("contain.text", "Start");
        cy.get("table").should("contain.text", "San Diego");
        cy.get("table").should("contain.text", "Los Angeles");
        cy.get("table").should("contain.text", "San Francisco");
        cy.get("table").should("contain.text", "End");
    });

    it("SYS30 - Update Valid Trip Dated ", () => {
        const newTrip = {
            tripId: 1,
            name: "Cypress Test Trip",
            startDate: new Date("2026-03-24"),
            endDate: new Date("2026-03-29"),
        };

        cy.visit("/trips/1");
        cy.contains("button", /Edit Trip/i).click();

        cy.contains("h2", /Edit Trip/i).should("be.visible");
        
        cy.intercept("PUT", "http://localhost:8080/api/trip/updateTrip", {
            statuscode: 200,
            body: {
                ...newTrip
            }
        }
        ).as("update");

        cy.get(".MuiDialog-container").within(() => {
            cy.get('input[name="tripName"]').clear();
            cy.get('input[name="tripName"]').type(newTrip.name);

            cy.get('input[name="startDate"]').then(($input) => {
                const reactInput = $input[0];
                const onChange = reactInput?.__reactProps$?.onChange || reactInput?.onChange;
                onChange?.({ target: { value: newTrip.startDate.toISOString().split("T")[0] } });
            });

            cy.contains("button", /Update/i).should("be.visible").click();
        });

        cy.wait("@update").its("response.statusCode").should("eq", 200);
        cy.contains(/Trip updated successfully/i).should("exist");

        cy.wait(500);
        cy.contains(newTrip.name).should("be.visible");
        cy.contains("No trip stops available.").should("be.visible");
    });
});