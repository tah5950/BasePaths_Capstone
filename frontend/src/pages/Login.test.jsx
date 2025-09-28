import React from "react";
import { render, screen, fireEvent, waitFor} from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Login from "./Login";
import Home from "./Home";
import CreateAccount from "./CreateAccount";

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

describe("Login Frontend Unit Tests", () => {
    test("FUT6 - Login Valid User", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({ token: "test-token "}),
        })

        localStorage.setItem("token", "test-token");

        render(
            <MemoryRouter initialEntries={["/login"]}>
                <Routes>
                    <Route path="/login" element={<Login />}/>
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

        fireEvent.click(screen.getByRole("button", {name: /Login/i}));

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/home");
        });
    });

    test("FUT7 - Click Sign Up Button", async () => {
        render(
            <MemoryRouter initialEntries={["/login"]}>
                <Routes>
                    <Route path="/login" element={<Login />}/>
                    <Route path="/createaccount" element={<CreateAccount />}/>
                </Routes>
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", {name: /Sign Up/i}));

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/createaccount");
        });
    });

    test("FUT8 - Login Invalid Credentials", async () => {
        render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: { value: "invalidUser#" },
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: "invalidPass" },
        });

        fireEvent.click(screen.getByRole("button", {name: /Login/i}));

        await waitFor(() => {
            expect(screen.getByText(/Error: Invalid Username or Password/i)).toBeInTheDocument();
        });
    });

    test("FUT9 - Login API error", async () => {
        global.fetch.mockResolvedValueOnce(new Error("Failed to fetch"))

        render(
            <MemoryRouter>
                <Login />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByLabelText(/Username/i), {
            target: { value: "validUser" },
        });

        fireEvent.change(screen.getByLabelText(/Password/i), {
            target: { value: "validPass1!" },
        });

        fireEvent.click(screen.getByRole("button", {name: /Login/i}));

        await waitFor(() => {
            expect(screen.getByText(/Error: Failed to fetch/i)).toBeInTheDocument();
        });
    });
});