import React from "react";
import { render, screen, fireEvent, waitFor} from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import TripsPage from "../pages/TripsPage";

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

describe("Navbar Unit tests", () => {
    test("FUT10 - Logout routes to Login", async () => {

        localStorage.setItem("token", "test-token");

        render(
            <MemoryRouter initialEntries={["/home"]}>
                <Routes>
                    <Route path="/login" element={<Login />}/>
                    <Route path="/home" element={<Home />}/>
                </Routes>
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", {name: /Logout/i}));

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/login");
        });
        expect(localStorage.getItem("token")).toBeNull();
    });

    test("FUT16 - Trips routes to TripsPage", async () => {

        localStorage.setItem("token", "test-token");

        render(
            <MemoryRouter initialEntries={["/home"]}>
                <Routes>
                    <Route path="/trips" element={<TripsPage />}/>
                    <Route path="/home" element={<Home />}/>
                </Routes>
            </MemoryRouter>
        );

        fireEvent.click(screen.getByRole("button", {name: /Trips/i}));

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/trips");
        });
    });
});