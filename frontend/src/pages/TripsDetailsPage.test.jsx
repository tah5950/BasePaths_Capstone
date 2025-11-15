import React from "react";
import { render, screen, fireEvent, waitFor, within} from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import TripsPage from "./TripsPage";
import TripsDetailsPage from "./TripsDetailsPage";
import { getToken } from "../utils/authUtils";
import { mockFetchTripDetailsPage, mockFetchTripDetailsPageBase } from "../utils/test-utils";

// Mock getToken call for API calls
jest.mock("../utils/authUtils", () => ({
  getToken: jest.fn(),
}));

// Mock navigate to test routing behavior
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

// Mock getToken call
beforeEach(() => {
    getToken.mockReturnValue("mockToken");
});

// Reset Mocks after each test
afterEach(() => {
    jest.restoreAllMocks();
});

describe("View Trip Frontend Unit Tests", () => {
    test("FUT17 - Trip Details Page shows Trip Data", async () => {
        mockFetchTripDetailsPage();

        render(
            <MemoryRouter initialEntries={["/trip/1"]}>
            <Routes>
                <Route path="/trip/:tripId" element={<TripsDetailsPage />} />
            </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Test Trip")).toBeInTheDocument();
            expect(screen.getByText(/Trip Overview/i)).toBeInTheDocument();
        });

        // Verify api fetch calls
        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/1$/),
            expect.objectContaining({
            headers: { Authorization: "Bearer mockToken" },
            })
        );

        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/ballpark\/2680$/),
            expect.objectContaining({
            headers: { Authorization: "Bearer mockToken" },
            })
        );

        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/game\/0d3c3efc-80cb-4323-a44c-3e6d40555080$/),
            expect.objectContaining({
            headers: { Authorization: "Bearer mockToken" },
            })
        );

        expect(screen.getByText(/Dates:/i)).toBeInTheDocument();
        expect(screen.getByText(/Generated:/i)).toBeInTheDocument();
        expect(screen.getByText(/Max Hours Per Day:/i)).toBeInTheDocument();
        expect(screen.getByText(/Start Coordinate:/i)).toBeInTheDocument();
        expect(screen.getByText(/End Coordinate:/i)).toBeInTheDocument();

        await waitFor(() => {
            const table = screen.getByRole("table");
            const bodyRows = within(table).getAllByRole("row").slice(1); // skip header
            expect(bodyRows.length).toBe(5);
        });

        expect(screen.getByText("Petco Park")).toBeInTheDocument();
        expect(screen.getByText((content) => content.includes("Arizona Diamondbacks") 
                && content.includes("Los Angeles Dodgers"))).toBeInTheDocument();
        expect(screen.getByText("San Francisco")).toBeInTheDocument();
    });

    test("FUT18 - Trip Details Page shows N/A fields and FUT19 - Trip Details Page with no trip stops", async () => {
        mockFetchTripDetailsPageBase();

        render(
            <MemoryRouter initialEntries={["/trip/2"]}>
            <Routes>
                <Route path="/trip/:tripId" element={<TripsDetailsPage />} />
            </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Test Trip Base")).toBeInTheDocument();
            expect(screen.getByText(/Trip Overview/i)).toBeInTheDocument();
        });

        // Verify api fetch calls
        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/2$/),
            expect.objectContaining({
            headers: { Authorization: "Bearer mockToken" },
            })
        );

        expect(screen.getByText(/Dates:/i)).toBeInTheDocument();
        expect(screen.getByText(/Generated:/i)).toBeInTheDocument();
        expect(screen.getByText(/Max Hours Per Day:/i)).toBeInTheDocument();
        expect(screen.getByText(/Start Coordinate:/i)).toBeInTheDocument();
        expect(screen.getByText(/End Coordinate:/i)).toBeInTheDocument();

        expect(screen.getAllByText("N/A").length).toBe(3);
    });

    test("FUT19 - Trip Details Page with no trip stops", async () => {
        mockFetchTripDetailsPageBase();

        render(
            <MemoryRouter initialEntries={["/trip/2"]}>
            <Routes>
                <Route path="/trip/:tripId" element={<TripsDetailsPage />} />
            </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Test Trip Base")).toBeInTheDocument();
            expect(screen.getByText(/Trip Overview/i)).toBeInTheDocument();
        });

        // Verify api fetch calls
        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/2$/),
            expect.objectContaining({
            headers: { Authorization: "Bearer mockToken" },
            })
        );

        expect(screen.getByText(/Dates:/i)).toBeInTheDocument();
        expect(screen.getByText(/Generated:/i)).toBeInTheDocument();
        expect(screen.getByText(/Max Hours Per Day:/i)).toBeInTheDocument();
        expect(screen.getByText(/Start Coordinate:/i)).toBeInTheDocument();
        expect(screen.getByText(/End Coordinate:/i)).toBeInTheDocument();

        expect(screen.getByText("No trip stops available.")).toBeInTheDocument();
    });
});