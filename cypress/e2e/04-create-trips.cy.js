/// <reference types="cypress" />

describe("User Creates Trips", () => {
    beforeEach(() => {
        cy.login();

        cy.visit("/trips");
    });

    it("SYS19 - Open Create Trip Form", () => {
        cy.contains("button", /Create Trip/i).click();

        cy.contains("h2", /Create New Trip/i).should("be.visible");
        cy.get('input[name="tripName"]').should("exist");

        cy.contains("button", /Create/i).should("be.visible");
    });

    it("SYS20 - Create Invalid Trip", () => {
        cy.contains("button", /Create Trip/i).click();

        cy.contains("h2", /Create New Trip/i).should("be.visible");
        
        cy.intercept("POST", "http://localhost:8080/api/trip/create").as("invalidCreate");

        cy.get(".MuiDialog-container").within(() => {
            cy.contains("button", /Create/i).should("be.visible").click();
        });

        cy.wait("@invalidCreate").its("response.statusCode").should("eq", 400);
        cy.contains(/Error: Name must not be blank/i).should("exist");

        cy.contains("h2", /Create New Trip/i).should("be.visible");
    });

    it("SYS21 - Create Valid Trip", () => {
        const newTrip = {
            tripId: 1,
            name: "Cypress Test Trip",
            startDate: new Date("2026-03-25"),
            endDate: new Date("2026-03-29"),
        };

        cy.contains("button", /Create Trip/i).click();

        cy.contains("h2", /Create New Trip/i).should("be.visible");
        
        cy.intercept("POST", "http://localhost:8080/api/trip/create", {
            statuscode: 200,
            body: {
                ...newTrip
            }
        }
        ).as("create");

        cy.get(".MuiDialog-container").within(() => {
            cy.get('input[name="tripName"]').type(newTrip.name);

            cy.get('input[name="startDate"]').then(($input) => {
                const reactInput = $input[0];
                const onChange = reactInput?.__reactProps$?.onChange || reactInput?.onChange;
                onChange?.({ target: { value: newTrip.startDate.toISOString().split("T")[0] } });
            });

            cy.get('input[name="endDate"]').then(($input) => {
                const reactInput = $input[0];
                const onChange = reactInput?.__reactProps$?.onChange || reactInput?.onChange;
                onChange?.({ target: { value: newTrip.endDate.toISOString().split("T")[0] } });
            });

            cy.contains("button", /Create/i).should("be.visible").click();
        });

        cy.wait("@create").its("response.statusCode").should("eq", 200);
        cy.contains(/Trip created successfully/i).should("exist");

        cy.wait(500);
        cy.contains(newTrip.name).should("be.visible");
        cy.get("table").should("contain.text", newTrip.name);
    });
})