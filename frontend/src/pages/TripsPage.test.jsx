import React from "react";
import { render, screen, fireEvent, waitFor} from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import TripsPage from "./TripsPage";
import TripsDetailsPage from "./TripsDetailsPage";
import { getToken } from "../utils/authUtils";
import { mockFetchTrips } from "../utils/test-utils";

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

const mockTrips = [
{
    "tripId": 1,
    "name": "Test Trip",
    "startDate": "2026-03-25T00:00:00.000+00:00",
    "endDate": "2026-03-29T00:00:00.000+00:00",
    "startLatitude": 32.72,
    "startLongitude": -117.16,
    "endLatitude": 38.58,
    "endLongitude": -121.49,
    "isGenerated": true,
    "maxHoursPerDay": 8,
    "userId": 1,
    "tripStops": [
    {
        "tripStopId": 319,
        "date": "2026-03-25T00:00:00.000+00:00",
        "location": "Start",
        "ballparkId": null,
        "gameId": null
    },
    {
        "tripStopId": 320,
        "date": "2026-03-26T00:00:00.000+00:00",
        "location": "Los Angeles",
        "ballparkId": 22,
        "gameId": "07e436a7-6300-4d03-889d-f5b0a33c120b"
    },
    {
        "tripStopId": 321,
        "date": "2026-03-27T00:00:00.000+00:00",
        "location": "San Diego",
        "ballparkId": 2680,
        "gameId": "0351fddc-1f4c-407a-934f-8426e6448ab3"
    },
    {
        "tripStopId": 322,
        "date": "2026-03-28T00:00:00.000+00:00",
        "location": "San Francisco",
        "ballparkId": 2395,
        "gameId": "0d3c3efc-80cb-4323-a44c-3e6d40555080"
    },
    {
        "tripStopId": 323,
        "date": "2026-03-29T00:00:00.000+00:00",
        "location": "End",
        "ballparkId": null,
        "gameId": null
    }
    ]
},
{
    "tripId": 2,
    "name": "Test long",
    "startDate": "2025-11-07T00:00:00.000+00:00",
    "endDate": "2025-11-21T00:00:00.000+00:00",
    "startLatitude": null,
    "startLongitude": null,
    "endLatitude": null,
    "endLongitude": null,
}];

// Mock getToken call
beforeEach(() => {
    getToken.mockReturnValue("mockToken");
});

// Reset Mocks after each test
afterEach(() => {
    jest.restoreAllMocks();
});


describe("Create Trip Frontend Unit Tests", () => {
    test("FUT11 - Trips Page Loads Data Successfully", async () => {
        mockFetchTrips(mockTrips);

        render(
            <MemoryRouter>
                <TripsPage />
            </MemoryRouter>
        );

        // Verify api fetch call
        expect(fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/getCurrentUserTrips$/),
            expect.objectContaining({
                headers: { Authorization: "Bearer mockToken" },
            })
        );

        await waitFor(() => {
            expect(screen.getByText(/Test Trip/i)).toBeInTheDocument();
        });
    });

    test("FUT12 - Trips Page handles API failure", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Failed to fetch"));

        render(
            <MemoryRouter>
                <TripsPage />
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText(/Error: Failed to fetch/i)).toBeInTheDocument();
        });
    });

    test("FUT13 - Double Click Row Route to Details page", async () => {
        mockFetchTrips(mockTrips);

        render(
            <MemoryRouter initialEntries={["/trips"]}>
                <Routes>
                    <Route path="/trips" element={<TripsPage />}/>
                    <Route path="/trips/1" element={<TripsDetailsPage />}/>
                </Routes>
            </MemoryRouter>
        );

        await waitFor(() => {
            expect(screen.getByText(/Test Trip/i)).toBeInTheDocument();
        });

        const row = screen.getByText(/Test Trip/i)
        fireEvent.doubleClick(row);

        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/trips/1",
                expect.objectContaining({
                    state: expect.objectContaining({
                        trip: expect.objectContaining({
                            tripId: 1,
                            name: "Test Trip",
                        }),
                    }),
                })
            );
        });
    });

    test("FUT28 - Open Create Form", async () => {
        render(
            <MemoryRouter>
                <TripsPage />
            </MemoryRouter>
        );

        expect(screen.queryByRole("dialog")).not.toBeInTheDocument();

        fireEvent.click(screen.getByRole("button", {name: /Create Trip/i}));

        expect(screen.queryByRole("dialog")).toBeInTheDocument();
    });
});