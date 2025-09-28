Cypress.Commands.add("login", () => {
  cy.request("POST", "http://localhost:8080/api/user/login", {
    username: "cyTestUser",
    password: "cyTestPass1!"
  }).then((resp) => {
    window.localStorage.setItem("token", resp.body.token);
  });
});