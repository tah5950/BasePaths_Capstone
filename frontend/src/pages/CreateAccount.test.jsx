import React from "react";
import { render, screen, fireEvent, waitFor} from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import CreateAccount from "./CreateAccount";
import Home from "./Home";

// Mock navigate to test routing behavior
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

// Mock fetch globally to control what calls to backend api return
beforeEach(() => {
    jest.spyOn(global, "fetch");
    mockNavigate.mockReset();
});

// Reset Mocks after each test
afterEach(() => {
    jest.restoreAllMocks();
});

describe("CreateAccount Frontend Unit Tests", () => {
    test("FUT1 - Register Valid User", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            text: async () => "",
        })
        render(
        <MemoryRouter initialEntries={["/createaccount"]}>
            <Routes>
                <Route path="/createaccount" element={<CreateAccount/>}/>
                <Route path="/home" element={<Home />}/>
            </Routes>
        </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: { value: "validUser" },
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: "validPass1!" },
        });

        fireEvent.click(screen.getByRole("button", {name: /Create/i}));

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/home");
        });
    });
});