export const mockFetchTrips = (trips) => {
  global.fetch = jest.fn().mockResolvedValueOnce({
    ok: true,
    json: () => Promise.resolve(trips),
  });
};
