/// <reference types="cypress" />

describe("Load Data", () => {
    it("SYS15 - Load Ballpark with invalid Secret Key", () => {
        cy.fixture("validBallparks.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/ballpark/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "1279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
                expect(response.body).to.eq("Unauthorized data load")
            });
        })
    });

    it("SYS14 - Load Game with invalid Secret Key", () => {
        cy.fixture("validGames.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "1279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
                expect(response.body).to.eq("Unauthorized data load")
            });
        })
    });

    it("SYS7 - Load Valid Ballparks", () => {
        cy.fixture("validBallparks.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/ballpark/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(200);
                expect(response.body).to.eq("Ballpark data load complete. 2 new entries added.")
                // TODO: Check that data was added with GET Endpoint
            });
        })
    });

    it("SYS8 - Ballpark Data Load data exists", () => {
        cy.fixture("duplicateBallpark.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/ballpark/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(200);
                expect(response.body).to.eq("Ballpark data load complete. 2 new entries added.")
                // TODO: Check that data was added with GET Endpoint
            });
        })
    });

    it("SYS9 - Ballpark Invalid Data Load", () => {
        cy.fixture("invalidBallpark.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/ballpark/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
            });
        })
    });

    it("SYS10 - Load Valid Games", () => {
        cy.fixture("validGames.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(200);
                expect(response.body).to.eq("Game data load complete. 3 new entries added.")
                // TODO: Check that data was added with GET Endpoint
            });
        })
    });

    it("SYS11 - Game Data Load data exists", () => {
        cy.fixture("duplicateGame.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(200);
                expect(response.body).to.eq("Game data load complete. 2 new entries added.")
                // TODO: Check that data was added with GET Endpoint
            });
        })
    });

    it("SYS12 - Game Invalid Data Load", () => {
        cy.fixture("invalidGame.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
            });
        })
    });

    it("SYS12 - Game Invalid Data Load", () => {
        cy.fixture("invalidGame.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
            });
        })
    });

    it("SYS13 - Load Game with Invalid Ballpark", () => {
        cy.fixture("gameInvalidBallparkId.json").then((data) => {
            cy.request({
                method: "POST", 
                url: "http://localhost:8080/api/game/load", 
                body: data,
                headers: {
                    "Content-Type": "application/json",
                    "X-LOAD-KEY": "0279fbdc-41e8-4080-90dc-2a28bd3340ba"
                },
                failOnStatusCode: false
            }).then((response) => {
                expect(response.status).to.eq(403);
            });
        })
    });
})