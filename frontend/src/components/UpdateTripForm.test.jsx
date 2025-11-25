import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import UpdateTripForm from "./UpdateTripForm";
import { getToken } from "../utils/authUtils";

const mockOnTripUpdated = jest.fn();
const mockOnClose = jest.fn();

// Mock getToken call for API calls
jest.mock("../utils/authUtils", () => ({
  getToken: jest.fn(),
}));

jest.mock("@mui/x-date-pickers", () => ({
    DatePicker: ({ value, onChange, label }) => (
        <input
            aria-label={label}
            value={value || ""}
            onChange={(e) => onChange(new Date(e.target.value))}
        />
    ),
    LocalizationProvider: ({ children }) => <div>{children}</div>,
}));

// Mock getToken call
beforeEach(() => {
    getToken.mockReturnValue("mockToken");
    jest.spyOn(global, "fetch");
    jest.useFakeTimers();
});

// Reset Mocks after each test
afterEach(() => {
    jest.restoreAllMocks();
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
});

describe("UpdateTripForm Frontend Unit Tests", () => {
    test("FUT26 - Update Trip Form Submits successfully", async () => {
        const currentTrip = {
            tripId: 1,
            name: "Test Trip",
            startDate: "2026-03-25",
            endDate: "2026-03-29"
        };

        const newTrip = {
            tripId: 1,
            name: "Test Trip UPDATE",
            startDate: "2026-03-25",
            endDate: "2026-03-29"
        };

        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => newTrip,
        });

        render(
            <UpdateTripForm
                open={true}
                onClose={mockOnClose}
                onTripUpdated={mockOnTripUpdated}
                trip={currentTrip}
            />
        );

        fireEvent.change(screen.getByLabelText(/Trip Name/i), {
            target: { value: newTrip.name },
        });
        
        fireEvent.click(screen.getByRole("button", {name: /Update/i}));

        // Verify api call
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/updateTrip$/),
            expect.objectContaining({
                method: "PUT",
                headers: { 
                    Authorization: "Bearer mockToken",
                    "Content-Type": "application/json",
                },
                body: expect.stringContaining(newTrip.name),
            })
        );

        await waitFor(() => {
            expect(screen.queryByText(/Trip updated successfully!/i)).toBeInTheDocument();
        });

        jest.advanceTimersByTime(1000);

        await waitFor(() => {
            expect(mockOnTripUpdated).toHaveBeenCalledWith(newTrip);
            expect(mockOnClose).toHaveBeenCalled();
        });
    });

    test("FUT27 - Update Trip Form shows Error", async () => {
        const currentTrip = {
            tripId: 1,
            name: "Test Trip",
            startDate: "2026-03-25",
            endDate: "2026-03-29"
        };
        
        const newTrip = {
            tripId: 1,
            name: "",
            startDate: "2026-03-25",
            endDate: "2026-03-29"
        };

        global.fetch.mockResolvedValueOnce({
            ok: false,
            status: 400,
            json: async () => ({ error: "Name must not be blank"}),
        });

        render(
            <UpdateTripForm
                open={true}
                onClose={mockOnClose}
                onTripUpdated={mockOnTripUpdated}
                trip={currentTrip}
            />
        );

        fireEvent.change(screen.getByLabelText(/Trip Name/i), {
            target: { value: newTrip.name },
        });
        
        fireEvent.click(screen.getByRole("button", {name: /Update/i}));

        // Verify api call
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/updateTrip$/),
            expect.objectContaining({
                method: "PUT",
                headers: { 
                    Authorization: "Bearer mockToken",
                    "Content-Type": "application/json",
                },
                body: expect.stringContaining(newTrip.startDate),
            })
        );

        await waitFor(() => {
            expect(screen.queryByText(/Error: Name must not be blank/i)).toBeInTheDocument();
        });
    });
});