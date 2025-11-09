import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import CreateTripForm from "./CreateTripForm";
import { getToken } from "../utils/authUtils";

const mockOnTripCreated = jest.fn();
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

describe("CreateTripForm Frontend Unit Tests", () => {
    test("FUT14 - Create Trip Form Submits successfully", async () => {
        const newTrip = {
            tripId: 1,
            name: "Test Trip",
            startDate: "2026-03-25",
            endDate: "2026-03-29"
        };

        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => newTrip,
        });

        render(
            <CreateTripForm
                open={true}
                onClose={mockOnClose}
                onTripCreated={mockOnTripCreated}
            />
        );

        fireEvent.change(screen.getByLabelText(/Trip Name/i), {
            target: { value: newTrip.name },
        });
        
        fireEvent.change(screen.getByLabelText(/Start Date/i), {
            target: { value: newTrip.startDate },
        });

        fireEvent.change(screen.getByLabelText(/End Date/i), {
            target: { value: newTrip.endDate },
        });
        
        fireEvent.click(screen.getByRole("button", {name: /Create/i}));

        // Verify api call
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/create$/),
            expect.objectContaining({
                method: "POST",
                headers: { 
                    Authorization: "Bearer mockToken",
                    "Content-Type": "application/json",
                },
                body: expect.stringContaining(newTrip.name),
            })
        );

        await waitFor(() => {
            expect(screen.queryByText(/Trip created successfully!/i)).toBeInTheDocument();
        });

        jest.advanceTimersByTime(1000);

        await waitFor(() => {
            expect(mockOnTripCreated).toHaveBeenCalledWith(newTrip);
            expect(mockOnClose).toHaveBeenCalled();
            expect(screen.queryByText(/Trip created successfully!/i)).toBeInTheDocument();
        });
    });

    test("FUT15 - Create Trip Form shows Error", async () => {
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
            <CreateTripForm
                open={true}
                onClose={mockOnClose}
                onTripCreated={mockOnTripCreated}
            />
        );

        fireEvent.change(screen.getByLabelText(/Trip Name/i), {
            target: { value: newTrip.name },
        });
        
        fireEvent.change(screen.getByLabelText(/Start Date/i), {
            target: { value: newTrip.startDate },
        });

        fireEvent.change(screen.getByLabelText(/End Date/i), {
            target: { value: newTrip.endDate },
        });
        
        fireEvent.click(screen.getByRole("button", {name: /Create/i}));

        // Verify api call
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/create$/),
            expect.objectContaining({
                method: "POST",
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