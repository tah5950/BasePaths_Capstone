import { useState, useEffect } from "react";
import { useLoaderData, useLocation, useNavigate, useParams } from "react-router-dom";
import { Container, Card, CardContent, Typography, Box, Button, Alert, 
    TableContainer, Paper, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import { MuiNavbar } from "../components/MuiNavbar";
import GenerateTripForm from "../components/GenerateTripForm";
import { getToken } from "../utils/authUtils";
import { API_BASE_URL } from "../config";

function TripsDetailsPage() {
    const { tripId } = useParams();
    const { state } = useLocation();
    const [trip, setTrip] = useState(state?.trip || null);
    const [stops, setStops] = useState([]);
    const [error, setError] = useState("");

    const [openForm, setOpenForm] = useState(false);
    
    const handleOpen = () => setOpenForm(true);
    const handleClose = () => setOpenForm(false);

    const handleTripGenerated = (newTrip) => {
        setTrip(newTrip);
    }
    
    useEffect(() => {
        if (!trip) {
            const fetchTripDetails = async () => {
                try {
                    const response = await fetch(`${API_BASE_URL}/api/trip/${tripId}`, {
                        headers: { Authorization: `Bearer ${getToken()}` },
                    });

                    if(!response.ok) {
                        const errorData = await response.json();
                        throw new Error(errorData.error || "Failed to fetch trip details")
                    }

                    const data = await response.json();
                    setTrip(data);
                } catch (e) {
                    setError(e.message);
                } 
            };

            fetchTripDetails();
        }
    }, [trip, tripId]);

    useEffect(() =>  {
        if(!trip?.tripStops){
            return;
        }

        const fetchStopDetails = async () => {
            const stopDetails = await Promise.all(
                trip.tripStops.map(async (stop) => {
                    let ballparkName = "N/A";
                    let gameDetails = "N/A"

                    if(stop.ballparkId){
                        try{
                            const ballparkResponse = await fetch(`${API_BASE_URL}/api/ballpark/${stop.ballparkId}`, {
                                headers: { Authorization: `Bearer ${getToken()}` },
                            });
                            if(ballparkResponse.ok) {
                                const data = await ballparkResponse.json();
                                ballparkName = data.name;
                            }
                        } catch(e) {}
                    }

                    if(stop.gameId){
                        try{
                            const gameResponse = await fetch(`${API_BASE_URL}/api/game/${stop.gameId}`, {
                                headers: { Authorization: `Bearer ${getToken()}` },
                            });
                            if(gameResponse.ok) {
                                const data = await gameResponse.json();
                                gameDetails = `${data.awayTeam} @ ${data.homeTeam}`;
                            }
                        } catch(e) {}
                    }

                    return {
                        ...stop,
                        ballparkName,
                        gameDetails,
                    }
                })
            )

            setStops(stopDetails);
        };

        fetchStopDetails();
    }, [trip]);

    const columns = [
        {
            accessorKey: "date",
            header: "Date",
            Cell: ({ cell }) => formatDate(cell.getValue()),
        },
        { accessorKey: "location", header: "Location", Cell: ({ cell }) => cell.getValue() || "N/A" },
        { accessorKey: "ballparkName", header: "Ballpark" },
        { accessorKey: "gameDetails", header: "Game" },
    ]

    const formatDate = (date) =>
        date ? new Date(date.split("T")[0] + "T00:00:00").toLocaleDateString("en-US", {
            weekday: "short",
            month: "short",
            day: "numeric",
            year: "numeric",
    }): "N/A";
    
    if(error) {
        return (
            <Box maxWidth="600px" mx="auto" mt={4}>
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    if(!trip) return null;

    return (
        <>
            <MuiNavbar />
            <Container maxWidth={false} sx= {{display:"flex", alignItems:"left", justifyContent:"top", height: "100vh", width:"100vw", flexDirection:"column"}}>
                <Box sx={{ display: "flex", justifyContent: "start", mt: 2, mb: 3 }}>
                    <Typography variant="h4" gutterBottom>
                        {trip.name}
                    </Typography>
                    <Button onClick={handleOpen} variant="contained" color="primary" sx={{ ml: 1 }}>
                        Generate Trip
                    </Button>
                </Box>

                <GenerateTripForm
                    open={openForm}
                    onClose={handleClose}
                    onTripGenerated={handleTripGenerated}
                    trip={trip}
                />

                <Box sx={{ px: 2 }}>
                    <Card sx={{ mb: 4 }}>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>Trip Overview</Typography>
                            <Typography><strong>Dates:</strong> {formatDate(trip.startDate)} - {formatDate(trip.endDate)} </Typography>
                            <Typography><strong>Generated:</strong> {trip.isGenerated ? "Yes" : "No"} </Typography>
                            <Typography><strong>Max Hours Per Day:</strong> {trip.maxHoursPerDay ? trip.maxHoursPerDay : "N/A"} </Typography>
                            <Typography><strong>Start Coordinate:</strong> {trip.startLatitude != null && trip.startLongitude != null
                                ? `${trip.startLatitude}, ${trip.startLongitude}` : "N/A"} </Typography>
                            <Typography><strong>End Coordinate:</strong> {trip.endLatitude != null && trip.endLongitude != null
                                ? `${trip.endLatitude}, ${trip.endLongitude}` : "N/A"} </Typography>
                        </CardContent>
                    </Card>
                </Box>

                <Box sx={{ px: 2, pb: 6 }}>
                    <Typography variant="h6" gutterBottom>Trip Stops</Typography>
                    {stops.length > 0 ? (
                        <MaterialReactTable
                            columns={columns}
                            data={stops}
                            enableTopToolbar={false}
                            enableBottomToolbar={false}
                            enableSorting
                            initialState={{ sorting: [{ id: "date", desc: false }] }}
                        />
                    ) : (
                        <Typography color="text.secondary" mt={2}>
                        No trip stops available.
                        </Typography>
                    )}
                </Box>
            </Container>
        </>
    );
}

export default TripsDetailsPage;
