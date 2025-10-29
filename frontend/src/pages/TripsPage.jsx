import { useState, useEffect } from "react";
import { Container, Typography, Box, Button, Alert } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import { MuiNavbar } from "../components/MuiNavbar";
import CreateTripForm from "../components/CreateTripForm";
import { getToken } from "../utils/authUtils";
import { API_BASE_URL } from "../config";

function TripsPage() {
    const [trips, setTrips] = useState([]);
    const [openForm, setOpenForm] = useState(false);
    const [message, setMessage] = useState("");

    const handleOpen = () => setOpenForm(true);
    const handleClose = () => setOpenForm(false);

    const handleTripCreated = (newTrip) => {
        console.log("Trip created: ", newTrip);
        // TODO: Refresh Trip List
    }

    useEffect(() => {
        fetchTrips();
    }, []);

    const fetchTrips = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/api/trip/getCurrentUserTrips`, {
                headers: { Authorization: `Bearer ${getToken()}` },
            });
            if(response.ok) {
                const data = await response.json();
                setTrips(data);
            }
            else {
                const errorData = await response.json()
                const error = errorData.error;
                setMessage(`Error: ${error}`);
            }
        } catch (error) {
            setMessage("Error: " + error.message);
        }
    };

    const columns = [
        { accessorKey: "name", header: "Trip Name" },
        { 
            accessorKey: "startDate", 
            header: "Start Date",
            Cell: ({ cell }) => {
                const date = new Date(cell.getValue());
                return date.toLocaleDateString(); 
            },
        },
        { 
            accessorKey: "endDate", 
            header: "End Date",
            Cell: ({ cell }) => {
                const date = new Date(cell.getValue());
                return date.toLocaleDateString(); 
            },
        },
    ]
    
    return (
        <>
            <MuiNavbar />
            <Container maxWidth={false} sx= {{display:"flex", alignItems:"left", justifyContent:"top", height: "100vh", width:"100vw", flexDirection:"column"}}>
                <Box sx={{ display: "flex", justifyContent: "start", mt: 2, mb: 3 }}>
                    <Typography variant="h4" fontWeight="bold">
                        Trips
                    </Typography>
                    <Button onClick={handleOpen} variant="contained" color="primary" sx={{ ml: 1 }}>
                        Create Trip
                    </Button>
                </Box>
                <CreateTripForm
                    open={openForm}
                    onClose={handleClose}
                    onTripCreated={handleTripCreated}
                />
                {message && (
                    <Alert severity={message.startsWith("Error") ? "error" : "success"} sx={{ mb: 2 }}>
                        {message}
                    </Alert>
                )}
                <MaterialReactTable
                    columns={columns}
                    data={trips}
                    enableTopToolbar={false}
                />
            </Container>
        </>
    );
}

export default TripsPage;