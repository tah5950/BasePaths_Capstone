import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import GenerateTripForm from "./GenerateTripForm";
import { getToken } from "../utils/authUtils";
import { mockFullTripBase } from "../utils/test-utils";

const mockOnTripGenerated = jest.fn();
const mockOnClose = jest.fn();

// Mock getToken call for API calls
jest.mock("../utils/authUtils", () => ({
  getToken: jest.fn(),
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

describe("GenerateTripForm Frontend Unit Tests", () => {
    test("FUT21 - Generate Trip Form shows Error", async () => {
        const genTripInvalid = {
            startLat: "100",
            startLon: "0",
            endLat: "0",
            endLon: "0",
            maxTravelHours: "7"
        };

        global.fetch.mockResolvedValueOnce({
            ok: false,
            status: 400,
            json: async () => ({ error: "Invalid Start Latitude"}),
        });

        render(
            <GenerateTripForm
                open={true}
                onClose={mockOnClose}
                onTripGenerated={mockOnTripGenerated}
                trip={mockFullTripBase}
            />
        );

        fireEvent.change(screen.getByLabelText(/Start Latitude/i), {
            target: { value: genTripInvalid.startLat },
        });
        
        fireEvent.change(screen.getByLabelText(/Start Longitude/i), {
            target: { value: genTripInvalid.startLon },
        });

        fireEvent.change(screen.getByLabelText(/End Latitude/i), {
            target: { value: genTripInvalid.endLat },
        });

        fireEvent.change(screen.getByLabelText(/End Longitude/i), {
            target: { value: genTripInvalid.endLon },
        });

        fireEvent.change(screen.getByLabelText(/Max Travel Hours Per Day/i), {
            target: { value: genTripInvalid.maxTravelHours },
        });
        
        fireEvent.click(screen.getByRole("button", {name: /Generate/i}));

        // Verify api call
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringMatching(/\/api\/trip\/generateTrip$/),
            expect.objectContaining({
                method: "PUT",
                headers: { 
                    Authorization: "Bearer mockToken",
                    "Content-Type": "application/json",
                },
                body: expect.stringContaining(genTripInvalid.startLat),
            })
        );

        await waitFor(() => {
            expect(screen.queryByText(/Error: Invalid Start Latitude/i)).toBeInTheDocument();
        });
    });
});