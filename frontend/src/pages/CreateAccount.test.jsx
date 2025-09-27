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
            json: async () => ({ token: "test-token "}),
        })

        localStorage.setItem("token", "test-token");

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

    test("FUT2 - Register User Invalid Username", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false,
            text: async () => "Username must be 6- 16 alphanumeric characters only.",
        });

        render(
            <MemoryRouter>
                <CreateAccount />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: { value: "invalidUser#" },
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: "validPass1!" },
        });

        fireEvent.click(screen.getByRole("button", {name: /Create/i}));

        await waitFor(() => {
            expect(screen.getByText(/Error: Username must be 6- 16 alphanumeric characters only./i)).toBeInTheDocument();
        });
    });

    test("FUT3 - Register User Invalid Password", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false,
            text: async () => "Password must be 8-32 characters with at least one lowercase letter, one uppercase letter, one number, and one symbol",
        });

        render(
            <MemoryRouter>
                <CreateAccount />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: { value: "validUser#" },
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: "invalidPass" },
        });

        fireEvent.click(screen.getByRole("button", {name: /Create/i}));

        await waitFor(() => {
            expect(screen.getByText(/Error: Password must be 8-32 characters with at least one lowercase letter, one uppercase letter, one number, and one symbol/i)).toBeInTheDocument();
        });
    });

    test("FUT4 - Create Account Form Renders", () => {
        render(
            <MemoryRouter>
                <CreateAccount />
            </MemoryRouter>
        );

        expect(screen.getByLabelText(/Username/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
        expect(screen.getByRole("button", { name: /Create/i })).toBeInTheDocument();
    });

    test("FUT5 - Create Account with API Error", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false,
            text: async () => "Failed to fetch",
        });

        render(
          <MemoryRouter>
            <CreateAccount />
          </MemoryRouter>  
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: {value: "validUser"},
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: {value: "validPass1!"},
        });

        fireEvent.click(screen.getByRole("button", { name: /Create/i }));

        await waitFor(() => {
            expect(screen.getByText(/Error: Failed to fetch/i)).toBeInTheDocument();
        });
    });
});