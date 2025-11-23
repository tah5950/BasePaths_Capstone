export const mockFullTrip = {
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
};

export const mockFullTripBase = {
    "tripId": 2,
    "name": "Test Trip Base",
    "startDate": "2025-11-08T00:00:00.000+00:00",
    "endDate": "2025-11-15T00:00:00.000+00:00",
    "startLatitude": null,
    "startLongitude": null,
    "endLatitude": null,
    "endLongitude": null,
    "isGenerated": false,
    "maxHoursPerDay": null,
    "userId": 1,
    "tripStops": []
};

export const mockSDBallpark = {
  "ballparkId": 2680,
  "name": "Petco Park",
  "city": "San Diego"
}

export const mockLABallpark = {
  "ballparkId": 22,
  "name": "Dodger Stadium",
  "city": "Los Angeles"
}

export const mockSFBallpark = {
  "ballparkId": 2395,
  "name": "Oracle Park",
  "city": "San Francisco"
}

export const mockSDGame = {
  "gameId": "0351fddc-1f4c-407a-934f-8426e6448ab3",
  "homeTeam": "San Diego Padres",
  "awayTeam": "Detroit Tigers"
}

export const mockLAGame = {
  "ballparkId": "07e436a7-6300-4d03-889d-f5b0a33c120b",
  "homeTeam": "Los Angeles Dodgers",
  "awayTeam": "Arizona Diamondbacks"
}

export const mockSFGame = {
  "ballparkId": "0d3c3efc-80cb-4323-a44c-3e6d40555080",
  "homeTeam": "San Francisco Giants",
  "awayTeam": "New York Yankees"
}


export const mockFetchTrips = (trips) => {
  global.fetch = jest.fn().mockResolvedValueOnce({
    ok: true,
    json: () => Promise.resolve(trips),
  });
};

export const mockFetchTripDetailsPage= () => {
  global.fetch = jest.fn((url) => {
    if (url.includes("/api/trip/1")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockFullTrip),
      });
    }

    if (url.includes("/api/ballpark/2680")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockSDBallpark),
      });
    }

    if (url.includes("/api/ballpark/22")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockLABallpark),
      });
    }

    if (url.includes("/api/ballpark/2395")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockSFBallpark),
      });
    }

    if (url.includes("/api/game/0351fddc-1f4c-407a-934f-8426e6448ab3")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockSDGame),
      });
    }

    if (url.includes("/api/game/07e436a7-6300-4d03-889d-f5b0a33c120b")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockLAGame),
      });
    }

    if (url.includes("/api/game/0d3c3efc-80cb-4323-a44c-3e6d40555080")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockSFGame),
      });
    }

    return Promise.resolve({ ok: false, json: async () => ({ error: "Unknown URL" }) });
  });
};

export const mockFetchTripDetailsPageBase= () => {
  global.fetch = jest.fn((url) => {
    if (url.includes("/api/trip/2")) {
      return Promise.resolve({
        ok: true,
        json: async () => (mockFullTripBase),
      });
    }

    return Promise.resolve({ ok: false, json: async () => ({ error: "Unknown URL" }) });
  });
};

export const mockDeleteTripSuccess = () => {
  const prevFetch = global.fetch;
  global.fetch = jest.fn((url, options) => {
    if (options?.method === "DELETE" && url.includes("/api/trip/delete/1")) {
      return Promise.resolve({
        ok: true,
        status: 204
      });
    }
    return prevFetch(url, options);
  });
};

export const mockDeleteTripFailure = () => {
  const prevFetch = global.fetch;
  global.fetch = jest.fn((url, options) => {
    if (options?.method === "DELETE" && url.includes("/api/trip/delete/1")) {
      return Promise.reject(new Error("Network Error"));
    }
    return prevFetch(url, options);
  });
};